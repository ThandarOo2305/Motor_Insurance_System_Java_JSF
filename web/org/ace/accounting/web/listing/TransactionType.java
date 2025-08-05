package org.ace.accounting.web.listing;

public enum TransactionType {
	VOC("Voucher Only"), T("Transaction Only"), A("All");

	private String label;

	private TransactionType(String label) {
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
