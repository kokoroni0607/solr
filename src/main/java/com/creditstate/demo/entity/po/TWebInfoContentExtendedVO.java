package com.creditstate.demo.entity.po;

import java.io.Serializable;

public class TWebInfoContentExtendedVO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String contentId;
	
	private String label;
	
	private String value;

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
