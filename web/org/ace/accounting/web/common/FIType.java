package org.ace.accounting.web.common;

import java.util.ArrayList;
import java.util.List;

public enum FIType {
	DIGIT("Digit"), OPERATOR("Operator"), BRACE("Brace"), COMMADOT("CommaDot");

	String label;

	private FIType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public static FIType getType(String c) {
		if (Character.isDigit(c.charAt(0))) {
			return DIGIT;
		}

		if (c.equals("{") || c.equals("}")) {
			return BRACE;
		}

		if (c.equals("+") || c.equals("-") || c.equals("*") || c.equals("/")) {
			return OPERATOR;
		}

		if (c.equals(",") || c.equals(".")) {
			return COMMADOT;
		}
		return null;
	}

	@SuppressWarnings("null")
	public static List<FIType> set(List<FIType> list, FIType... params) {
		list = new ArrayList<FIType>();
		if (params != null || params.length != 0) {
			for (int x = 0; x < params.length; x++) {
				list.add(params[x]);
			}
		}
		return list;
	}
}
