package com.huawei.ais.demo.vct.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tag {

    @JsonProperty("count")
    int count;

    @JsonProperty("tag")
    private String tag;

    @JsonProperty("confidence")
    private float confidence;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
