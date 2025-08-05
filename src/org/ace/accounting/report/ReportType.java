package org.ace.accounting.report;

public enum ReportType {
	PL("Profit & Loss"), BS("Balance Sheet"), IE("Income & Expenditure"), TB("Trial Balance");

	private String label;

	private ReportType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return label;
	}
}
