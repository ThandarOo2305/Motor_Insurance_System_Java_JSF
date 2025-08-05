package org.ace.accounting.common;

public enum ReportFormat {
	AF("Actual Figures"), BAF("Budgeted & Actual Figures");

	private String label;

	private ReportFormat(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
