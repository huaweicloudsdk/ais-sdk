package com.huawei.ais.demo.vcm.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JobMetaInfo {
	@JsonProperty("url")
	private String url;

	@JsonProperty("frame_interval")
	private int frameInterval = 5;

	@JsonProperty("categories")
	private Set<Category> categories = new HashSet<>(Category.values().length);

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

	public Set<Category> getCategories() {
		return categories;
	}

	public boolean addCategory(Category category) {
		return categories.add(category);
	}
}
