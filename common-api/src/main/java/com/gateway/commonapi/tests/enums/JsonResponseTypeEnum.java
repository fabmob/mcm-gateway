package com.gateway.commonapi.tests.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum JsonResponseTypeEnum {
	JSON_ARRAY("JSON_ARRAY"), JSON_OBJECT("JSON_OBJECT"), EMPTY("EMPTY");

	private String value;
}