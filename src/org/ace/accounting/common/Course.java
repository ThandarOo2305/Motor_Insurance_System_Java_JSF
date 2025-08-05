package org.ace.accounting.common;

import javax.persistence.Embeddable;

@Embeddable
public enum Course {
	JAVA("Java"),PHP("Php"),PYTHON("Python"),ANGULAR("Angular"),JAVASCRIPT("JavaScript");
	
	private String label;

	private Course(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	

}
