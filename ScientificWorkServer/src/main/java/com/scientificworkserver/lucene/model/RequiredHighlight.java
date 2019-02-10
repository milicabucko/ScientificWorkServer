package com.scientificworkserver.lucene.model;

public final class RequiredHighlight {

	private String fieldName;
	private String value;

	public RequiredHighlight() {
	}

	public RequiredHighlight(String fieldName, String value) {
		this.fieldName = fieldName;
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getValue() {
		return value;
	}

}
