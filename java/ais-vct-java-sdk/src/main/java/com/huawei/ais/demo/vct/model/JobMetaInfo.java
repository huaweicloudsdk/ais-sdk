package com.huawei.ais.demo.vct.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JobMetaInfo {
    @JsonProperty("url")
    private String url;

    @JsonProperty("frame_interval")
    private int frameInterval = 5;

    @JsonProperty("language")
    private Language language = Language.CHINESE;

    @JsonProperty("threshold")
    private int threshold = 0;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFrameInterval() {
        return frameInterval;
    }

    public void setFrameInterval(int frameInterval) {
        this.frameInterval = frameInterval;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
