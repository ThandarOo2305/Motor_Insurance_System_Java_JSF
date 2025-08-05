package org.ace.accounting.system.webPage;

public enum WebpageGroup {
	SETUP("Setup Entry"), TRANSACTION("Transaction Entry"), ENQUIRY("Enquiry"), LISTING("Listing"), POSTING("Posting"), REPORTS("Reports");

	private String label;

	private WebpageGroup(String label) {
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
