package org.ace.accounting.common;

import javax.persistence.Embeddable;

@Embeddable
public enum Salutation {
	
	Mr("Mr."),Mrs("Mrs."),Miss("Miss");
	private String label;

	private Salutation(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	

}
