package org.ace.accounting.common;

import javax.persistence.Embeddable;

@Embeddable
public enum Gender {
	MALE("Male"),FEMALE("Female");
	
	private String label;

	private Gender(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
