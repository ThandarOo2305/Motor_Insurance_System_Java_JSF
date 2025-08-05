package org.ace.accounting.report;

public enum TrialBalanceAccountType {
	Gl_ACODE("By GL Account Code"),IBSB_ACODE("By IBSB Account Code");
	
	private String label;

	private TrialBalanceAccountType(String label) {
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
