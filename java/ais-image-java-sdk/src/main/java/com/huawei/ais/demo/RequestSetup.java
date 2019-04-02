package com.huawei.ais.demo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestSetup {
    public static Map<String,String> bgmUrls = new ConcurrentHashMap<>();
    static{
        bgmUrls.put("cn-north-1", "https://obs-test-llg.obs.cn-north-1.myhuaweicloud.com/bgm_recognition");
        bgmUrls.put("ap-southeast-1", "https://obs-hk-image.obs.ap-southeast-1.myhwclouds.com/obs_testdata/bgm_recognition");
    }

    public int connectionTimeout = 5000; //连接目标url超时限制参数
    public int connectionRequestTimeout = 1000;//连接池获取可用连接超时限制参数
    public int socketTimeout =  5000;//获取服务器响应数据超时限制参数
    private static final String ENDPOINT_TEMPLATE = "https://image.%s.myhuaweicloud.com";

    public RequestSetup() {
    }


    public RequestSetup(int connectionTimeout, int connectionRequestTimeout, int socketTimeout) {
        this.connectionTimeout = connectionTimeout;
        this.connectionRequestTimeout = connectionRequestTimeout;
        this.socketTimeout = socketTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public static String getEndpointTemplate() {
        return ENDPOINT_TEMPLATE;
    }

    public static String getASrBgmUrl(String regionName){
        return bgmUrls.get(regionName);
    }




}
