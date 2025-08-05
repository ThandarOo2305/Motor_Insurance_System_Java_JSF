package org.ace.java.component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Zaw Than Oo
 */
public class FormatID {
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
	
	/**
	 * This method is used to format plain id (plain number). Mostly, this
	 * method is used in setId() methods of entities.
	 * <p>
	 * E.g. Input: id=523, prefix="ACE", maxLength=15 Output: ACE000000000523
	 * <p>
	 * 
	 * @param id
	 *            id must not be null.
	 * @param prefix
	 *            prefix must not be null.
	 * @param maxLength
	 *            maximum length of the id string and it must not be negative
	 *            number.
	 * @return a formatted id string
	 */
	public static String formatId(String id, String prefix, int serialLength) {
		// for fake id
		boolean isGenerate = false;
		try {
			Long.parseLong(id);
			isGenerate = true;
		} catch (NumberFormatException e) {
			isGenerate = false;
		}
		// end
		if (prefix != null && !prefix.startsWith("null") && !id.startsWith(prefix.substring(0, 8)) && isGenerate) {
			int length = id.length();
			for (; (serialLength - length)  > 0; length++) {
				id = '0' + id;
			}
			id = prefix + id + getDateString();
		}
		return id;
	}

	private static String getDateString() {
		return simpleDateFormat.format(new Date());
	}

	public static void main(String args[]) {
		System.out.println(formatId("202", "INSU00010001", 10));
		System.out.println(formatId("202", "INSU00010001", 10).length());
	}
}
