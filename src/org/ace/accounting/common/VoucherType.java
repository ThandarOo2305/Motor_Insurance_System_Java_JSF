 package org.ace.accounting.common;

public enum VoucherType {
	DEBIT("DEBIT"), CREDIT("CREDIT"), CASH("CASH"), JOURNAL("JOURNAL") ;

	private String label;

	private VoucherType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return this.label;
	}
}