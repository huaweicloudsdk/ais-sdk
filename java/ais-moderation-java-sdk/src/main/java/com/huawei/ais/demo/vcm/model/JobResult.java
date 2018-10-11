package com.huawei.ais.demo.vcm.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

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

	@JsonProperty("suggestion")
	protected Suggestion suggestion;

	@JsonProperty("frames")
	protected List<FrameResult> frameResults = new LinkedList<>();

	@JsonProperty("cause")
	protected String cause;

	protected JobResult() {
	}

	public String getId() {
		return id;
	}

	public JobStatus getStatus() {
		return status;
	}

	public Suggestion getSuggestion() {
		return suggestion;
	}

	public List<FrameResult> getFrameResults() {
		return frameResults;
	}

	public String getCause() {
		return cause;
	}


	@Override
	public int hashCode() {

		return Objects.hash(id, status, createTime, updateTime, suggestion, frameResults);
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
				Objects.equals(suggestion, that.suggestion) &&
				Objects.equals(frameResults, that.frameResults);
	}

	@Override
	public String toString() {
		return "VideoJobDetail{" +
				", id='" + id + '\'' +
				", status=" + status +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", suggestion=" + suggestion +
				", frameResults=" + frameResults +
				"} " + super.toString();
	}

}
