package com.huawei.ais.demo.obs;

import com.huawei.ais.common.ProxyHostInfo;
import com.huawei.ais.sdk.AisAccess;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import com.obs.services.model.*;

import java.io.File;
import java.io.IOException;

/**
 * 简易OBS Client封装，提供了桶的创建、删除，文件的上传、删除、生成临时授权链接功能
 */
public class SimpleObsClient {

    private String region;

    private static final long BIG_FILE_THRESHOLD = 100 * 1024 * 1024; //Byte

    private static final int CONN_TIMEOUT_DEFAULT = 10000; //ms
    private static final int SOCKET_TIMEOUT_DEFAULT = 30000; //ms

    private ObsClient obsClient;

    public String getRegion() {
        return region;
    }

    /**
     * 使用AisAccess中配置的AK/SK创建简易OBS客户端，请确保对应的用户已开通OBS服务
     */
    public SimpleObsClient(AisAccess aisAccess) {
        this(aisAccess.getAk(), aisAccess.getSk(), aisAccess.getRegion());
        this.region = aisAccess.getRegion();
    }

    /**
     * 使用isAccess中配置的AK/SK创建简易OBS客户端，请确保对应的用户已开通OBS服务<br/>
     * 并给客户端配置代理
     *
     * @param proxyHostInfo
     */
    public SimpleObsClient(AisAccess aisAccess, ProxyHostInfo proxyHostInfo) {
        this(aisAccess.getAk(), aisAccess.getSk(), aisAccess.getRegion());
    }

    /**
     * 使用自定义的AK/SK创建简易OBS客户端，请确保对应的用户已开通OBS服务
     *
     * @param ak
     * @param sk
     */
    public SimpleObsClient(String ak, String sk, String regionName) {
        this(ak, sk, regionName, null);
    }

    /**
     * 使用自定义的AK/SK创建简易OBS客户端，请确保对应的用户已开通OBS服务<br/>
     * 并给客户端配置代理
     *
     * @param ak
     * @param sk
     * @param proxyHostInfo
     */
    public SimpleObsClient(String ak, String sk, String regionName, ProxyHostInfo proxyHostInfo) {
        this(ak, sk, regionName, CONN_TIMEOUT_DEFAULT, SOCKET_TIMEOUT_DEFAULT, proxyHostInfo);
    }

    /**
     * 使用自定义的AK/SK创建简易OBS客户端，请确保对应的用户已开通OBS服务<br/>
     * 同时指定此客户端连接服务端的连接超时和等待响应超时时间
     *
     * @param ak
     * @param sk
     * @param connTimeout   连接超时时间，ms
     * @param socketTimeout 等待响应超时时间，ms
     */
    public SimpleObsClient(String ak, String sk, String regionName, int connTimeout, int socketTimeout) {
        this(ak, sk, regionName, connTimeout, socketTimeout, null);
    }

    /**
     * 使用自定义的AK/SK创建简易OBS客户端，请确保对应的用户已开通OBS服务<br/>
     * 同时指定此客户端连接服务端的连接超时和等待响应超时时间<br/>
     * 并给客户端配置代理
     *
     * @param ak
     * @param sk
     * @param connTimeout   连接超时时间，ms
     * @param socketTimeout 等待响应超时时间，ms
     * @param proxyHostInfo
     */
    public SimpleObsClient(String ak, String sk, String regionName, int connTimeout, int socketTimeout, ProxyHostInfo proxyHostInfo) {
        String endPoint = "obs." + regionName + ".myhwclouds.com";

        ObsConfiguration config = new ObsConfiguration();
        config.setSocketTimeout(connTimeout);
        config.setConnectionTimeout(socketTimeout);
        config.setEndPoint(endPoint);
        config.setHttpsOnly(true);
        if (proxyHostInfo != null) {
            config.setHttpProxy(proxyHostInfo.getHostName(), proxyHostInfo.getPort(), proxyHostInfo.getUserName(),
                    proxyHostInfo.getPassword(), "");
        }
        obsClient = new ObsClient(ak, sk, config);
    }

    /**
     * 创建桶
     *
     * @param bucketName 桶名称
     */
    public void createBucket(String bucketName) {
        ObsBucket obsBucket = obsClient.createBucket(bucketName, region);
        System.out.println(
                "Create bucket success. BucketName: " + obsBucket.getBucketName()
                        + "; CreationDate: " + obsBucket.getCreationDate()
                        + "; Location: " + obsBucket.getLocation());
    }

    /**
     * 删除桶
     *
     * @param bucketName 桶名称
     */
    public void deleteBucket(String bucketName) {
        obsClient.deleteBucket(bucketName);
    }

    /**
     * 上传文件到OBS，直接放在桶中， {bucket}/file<br/>
     *
     * @param bucketName 桶名称
     * @param filePath   本地文件位置
     * @return OBS文件句柄
     */
    public ObsFileHandle uploadFile(String bucketName, String filePath) {
        return uploadFile(bucketName, "", filePath);
    }

    /**
     * 上传文件到OBS中，并会放在指定的文件夹里（没有会创建），{bucket}/{folder}/file
     *
     * @param bucketName 桶名称
     * @param folderName 文件夹名称
     * @param filePath   本地文件位置
     * @return OBS文件句柄
     */
    public ObsFileHandle uploadFile(String bucketName, String folderName, String filePath) {
        File file = new File(filePath);
        String objectKey = folderName + file.getName();

        if (file.length() > BIG_FILE_THRESHOLD) {
            ConcurrentUploadChannel channel = new ConcurrentUploadChannel(obsClient, bucketName, objectKey, file);
            channel.upload();
        } else {
            obsClient.putObject(bucketName, objectKey, file);
        }
        System.out.println("upload file " + filePath + " successfully!\n");
        return new ObsFileHandle(bucketName, objectKey, this);
    }

    /**
     * 定位OBS中已存在的文件，获取其句柄
     *
     * @param bucketName 桶名称
     * @param key        文件key，值为文件在OBS中的完整路径{bucket}/[folder]/file
     * @return OBS文件句柄
     */
    public ObsFileHandle locateFile(String bucketName, String key) {
        ObjectMetadata objectMetadata = obsClient.getObjectMetadata(bucketName, key);
        if (objectMetadata != null) {
            return new ObsFileHandle(bucketName, key, this);
        } else {
            throw new IllegalArgumentException(key + " is not found in " + bucketName);
        }
    }

    protected String generateSignedUrl(HttpMethodEnum method, String bucketName, String objectKey, long expireTime) {
        TemporarySignatureRequest req = new TemporarySignatureRequest(method, expireTime);
        req.setBucketName(bucketName);
        req.setObjectKey(objectKey);
        TemporarySignatureResponse res = obsClient.createTemporarySignature(req);
        System.out.println("temporary signature url:");
        System.out.println("\t" + res.getSignedUrl());

        return res.getSignedUrl();
    }

    protected void deleteFile(String bucketName, String objectKey) {
        obsClient.deleteObject(bucketName, objectKey);
    }

    /**
     * 销毁OBS客户端
     */
    public void close() {
        if (obsClient != null) {
            try {
                obsClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
