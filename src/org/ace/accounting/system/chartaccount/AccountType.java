package org.ace.accounting.system.chartaccount;

public enum AccountType {
	A("Assets"), E("Expenses"), L("Liabilities"), I("Incomes");

	private String label;

	private AccountType(String label) {
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
