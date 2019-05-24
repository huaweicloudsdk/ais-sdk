package com.huawei.ais.demo.vct.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class JobResult {

    @JsonProperty("job_id")
    protected String id;

    @JsonProperty("status")
    protected JobStatus status;

    @JsonProperty("create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX", timezone = "UTC")
    protected Date createTime;

    @JsonProperty("update_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX", timezone = "UTC")
    protected Date updateTime;

    @JsonProperty("tags")
    protected List<Tag> tags = new LinkedList<>();

    protected JobResult() {
    }

    public String getId() {
        return id;
    }

    public JobStatus getStatus() {
        return status;
    }

    public List<Tag> getTags() {
        return tags;
    }


    @Override
    public int hashCode() {

        return Objects.hash(id, status, createTime, updateTime, tags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobResult)) {
            return false;
        }
        JobResult that = (JobResult) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public String toString() {
        return "VideoJobDetail{" +
                ", id='" + id + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", tags=" + tags +
                "} " + super.toString();
    }

}
