package org.ace.accounting.web.listing;

public enum VoucherListingType {
	CC("Cash Credit Vouchers"), CD("Cash Debit Vouchers"), T("Journal Vouchers"), A("All vouchers");

	private String label;

	private VoucherListingType(String label) {
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
