package org.ace.accounting.system.chartaccount;

public enum AccountCodeType {

	HEAD("Head Code"), GROUP("Group Code"), DETAIL("Detail");

	private String label;

	private AccountCodeType(String label) {
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
