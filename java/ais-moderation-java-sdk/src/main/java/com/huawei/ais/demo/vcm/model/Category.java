package com.huawei.ais.demo.vcm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Category {
	@JsonProperty("politics")
	POLITICS,

	@JsonProperty("terrorism")
	TERRORISM,

	@JsonProperty("porn")
	PORN
}
