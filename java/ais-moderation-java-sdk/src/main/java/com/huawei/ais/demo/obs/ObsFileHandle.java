package com.huawei.ais.demo.obs;

import com.obs.services.model.HttpMethodEnum;

public class ObsFileHandle {

    private static final long SHARE_EXPIRE_TIMEOUT_DEFAULT = 300; //s

    private String bucketName = "";
    private String objectKey = "";
    private SimpleObsClient obsClientTool = null;

    private boolean isDeleted = false;

    protected ObsFileHandle(String bucketName, String objectKey, SimpleObsClient obsClientTool) {
        this.bucketName = bucketName;
        this.objectKey = objectKey;
        this.obsClientTool = obsClientTool;
    }

    /**
     * 获取OBS文件的临时授权下载链接，默认有效期为300秒
     *
     * @return 临时授权下载链接
     */
    public String generateSharedDownloadUrl() {
        return generateSharedDownloadUrl(SHARE_EXPIRE_TIMEOUT_DEFAULT);
    }

    /**
     * 获取OBS文件的临时授权下载链接
     *
     * @param expireTime 链接有效期，单位为秒
     * @return 临时授权下载链接
     */
    public String generateSharedDownloadUrl(long expireTime) {
        if (!isDeleted) {
            return obsClientTool.generateSignedUrl(HttpMethodEnum.GET, bucketName, objectKey, expireTime);
        } else {
            throw new IllegalStateException("file has been deleted already!");
        }
    }

    /**
     * 从OBS中删除此文件
     */
    public void delete() {
        if (!isDeleted) {
            obsClientTool.deleteFile(bucketName, objectKey);
            isDeleted = true;
        }
    }
}
