package com.huawei.ais.demo.obs;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * copy from OBS Java SDK
 */
public class ConcurrentUploadChannel {

    private static final int PART_SIZE_IN_MB_DEFAULT = 5;
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);
    private static List<PartEtag> partETags = Collections.synchronizedList(new ArrayList<PartEtag>());
    private ObsClient obsClient;
    private String bucketName;
    private String objectKey;
    private File file;

    public ConcurrentUploadChannel(ObsClient obsClient, String bucketName, String objectKey, File file) {
        this.obsClient = obsClient;
        this.bucketName = bucketName;
        this.objectKey = objectKey;
        this.file = file;
    }

    public void upload() {
        upload(PART_SIZE_IN_MB_DEFAULT);
    }

    public void upload(int partSizeInMB) {
        String uploadId = claimUploadId();
        System.out.println("Claiming a new upload id " + uploadId + "\n");

        long partSize = partSizeInMB * 1024 * 1024L;// 5MB
        long fileLength = file.length();
        long partCount = fileLength % partSize == 0 ? fileLength / partSize : fileLength / partSize + 1;

        if (partCount > 10000) {
            throw new RuntimeException("Total parts count should not exceed 10000");
        } else {
            System.out.println("Total parts count " + partCount + "\n");
        }

        //Upload multiparts to your bucket
        System.out.println("Begin to upload multiparts to OBS from a file\n");
        for (int i = 0; i < partCount; i++) {
            long offset = i * partSize;
            long currPartSize = (i + 1 == partCount) ? fileLength - offset : partSize;
            executorService.execute(new PartUploader(
                    obsClient, bucketName, objectKey, file, offset, currPartSize, i + 1, uploadId));
        }

        //Waiting for all parts finished
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Verify whether all parts are finished
        if (partETags.size() != partCount) {
            throw new IllegalStateException("Upload multiparts fail due to some parts are not finished yet");
        } else {
            System.out.println("Succeed to complete multiparts into an object named " + objectKey + "\n");
        }

        listAllParts(uploadId);

        completeMultipartUpload(uploadId);

    }

    private String claimUploadId() throws ObsException {
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, objectKey);
        InitiateMultipartUploadResult result = obsClient.initiateMultipartUpload(request);
        return result.getUploadId();
    }

    private void completeMultipartUpload(String uploadId) throws ObsException {
        // Make part numbers in ascending order
        Collections.sort(partETags, new Comparator<PartEtag>() {
            @Override
            public int compare(PartEtag o1, PartEtag o2) {
                return o1.getPartNumber() - o2.getPartNumber();
            }
        });

        System.out.println("Completing to upload multiparts\n");
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(bucketName, objectKey, uploadId, partETags);
        obsClient.completeMultipartUpload(completeMultipartUploadRequest);
    }

    private void listAllParts(String uploadId) throws ObsException {
        System.out.println("Listing all parts......");
        ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, objectKey, uploadId);
        ListPartsResult partListing = obsClient.listParts(listPartsRequest);

        for (Multipart part : partListing.getMultipartList()) {
            System.out.println("\tPart#" + part.getPartNumber() + ", ETag=" + part.getEtag());
        }
        System.out.println();
    }

    private static class PartUploader implements Runnable {

        private ObsClient obsClient;

        private String bucketName;

        private String objectKey;

        private File sampleFile;

        private long offset;

        private long partSize;

        private int partNumber;

        private String uploadId;

        PartUploader(ObsClient obsClient, String bucketName, String objectKey, File sampleFile,
                     long offset, long partSize, int partNumber, String uploadId) {
            this.obsClient = obsClient;
            this.bucketName = bucketName;
            this.objectKey = objectKey;
            this.sampleFile = sampleFile;
            this.offset = offset;
            this.partSize = partSize;
            this.partNumber = partNumber;
            this.uploadId = uploadId;
        }

        @Override
        public void run() {
            try {
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setObjectKey(objectKey);
                uploadPartRequest.setUploadId(this.uploadId);
                uploadPartRequest.setFile(this.sampleFile);
                uploadPartRequest.setPartSize(this.partSize);
                uploadPartRequest.setOffset(this.offset);
                uploadPartRequest.setPartNumber(this.partNumber);

                UploadPartResult uploadPartResult = obsClient.uploadPart(uploadPartRequest);
                System.out.println("Part#" + this.partNumber + " done\n");
                partETags.add(new PartEtag(uploadPartResult.getEtag(), uploadPartResult.getPartNumber()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
