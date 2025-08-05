package org.ace.accounting.common;

public enum FormatStyle {
	VF("Vertical Format"), HF("Horizontal Format");

	private String label;

	private FormatStyle(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
