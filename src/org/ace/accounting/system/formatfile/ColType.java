package org.ace.accounting.system.formatfile;

public enum ColType {
	L("Left"), R("Right"), B("Both");

	private String label;

	private ColType(String label) {
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
