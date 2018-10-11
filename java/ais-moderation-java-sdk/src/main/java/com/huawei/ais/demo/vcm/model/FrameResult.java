package com.huawei.ais.demo.vcm.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FrameResult {

	@JsonProperty("frame_begin")
	private int frameBegin;

	@JsonProperty("frame_end")
	private int frameEnd;

	@JsonProperty("frame_suggestion")
	private Suggestion frameSuggestion;

	@JsonProperty("suspect_categories")
	private Set<Category> suspectCategories = new HashSet<>();

	@JsonProperty("detail")
	private Map<Category, Object> categoryResults = new HashMap<>();

	public int getFrameBegin() {
		return frameBegin;
	}

	public int getFrameEnd() {
		return frameEnd;
	}

	public Suggestion getFrameSuggestion() {
		return frameSuggestion;
	}

	public Set<Category> getSuspectCategories() {
		return suspectCategories;
	}

}
