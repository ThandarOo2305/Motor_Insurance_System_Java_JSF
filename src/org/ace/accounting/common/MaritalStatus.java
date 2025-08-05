package org.ace.accounting.common;


public enum MaritalStatus {
	
	SINGLE("Single"),MARRIED("Married");
	
	private String label;
	
	

	private MaritalStatus(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
