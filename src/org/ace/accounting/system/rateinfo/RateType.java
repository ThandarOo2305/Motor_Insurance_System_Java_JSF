package org.ace.accounting.system.rateinfo;

public enum RateType {
	
	/*
	 * CS --> For Cash Voucher (Debit Voucher and Credit Voucher)
	 * TT --> For Transfer Voucher
	 */
	CS("CS"), TT("TT");

	private String label;

	private RateType(String label) {
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
