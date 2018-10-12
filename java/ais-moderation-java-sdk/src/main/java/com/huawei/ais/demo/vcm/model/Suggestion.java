package com.huawei.ais.demo.vcm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Suggestion {

	@JsonProperty("pass")
	PASS,

	@JsonProperty("review")
	REVIEW,

	@JsonProperty("block")
	BLOCK
}
