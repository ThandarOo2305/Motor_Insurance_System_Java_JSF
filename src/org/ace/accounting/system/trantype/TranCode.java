package org.ace.accounting.system.trantype;

public enum TranCode {
	CSCREDIT("CSCREDIT"), CSDEBIT("CSDEBIT"), TRDEBIT("TRDEBIT"), TRCREDIT("TRCREDIT");

	private String label;

	private TranCode(String label) {
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
