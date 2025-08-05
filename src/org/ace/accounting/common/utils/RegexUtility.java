package org.ace.accounting.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtility {

	private static final Pattern BACKSLASH = Pattern.compile("\\\\");
	private static final Pattern DOT = Pattern.compile("\\.");
	private static final Pattern IPV4_PATTERN = Pattern
			.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

	private static final Pattern IPV6_STD_PATTERN = Pattern
			.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

	private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern
			.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

	public static boolean isNumber(String value) {
		return value.matches("^\\d+$");
	}

	public static boolean isNumeric(String value) {
		return value.matches("^[-+]?\\d+(\\.\\d+)?$");
	}

	public static boolean isNumberWithDecimal(String value, int noOfDecimal) {
		String pattern = "^\\d+\\.\\d{" + noOfDecimal + "}$";
		return value.matches(pattern);
	}

	public static boolean isIPv4Address(final String input) {
		return IPV4_PATTERN.matcher(input).matches();
	}

	public static boolean isIPv6StdAddress(final String input) {
		return IPV6_STD_PATTERN.matcher(input).matches();
	}

	public static boolean isIPv6HexCompressedAddress(final String input) {
		return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
	}

	public static boolean isIPv6Address(final String input) {
		return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
	}

	public static String substBackslashes(String value) {
		if (value == null) {
			return null;
		}

		Matcher matcher = BACKSLASH.matcher(value);
		return matcher.find() ? matcher.replaceAll("\\/") : value;
	}

	/**
	 * Returns package name for the Java class as a path separated with forward
	 * slash ("/"). Method is used to lookup resources that are located in
	 * package subdirectories. For example, a String "a/b/c" will be returned
	 * for class name "a.b.c.ClassName".
	 */
	static String getPackagePath(String className) {
		if (className == null) {
			return "";
		}

		Matcher matcher = DOT.matcher(className);
		if (matcher.find()) {
			String path = matcher.replaceAll("\\/");
			return path.substring(0, path.lastIndexOf("/"));
		} else {
			return "";
		}
	}

	public static boolean isValidEmailAddress(String email) {
		String email_pattern = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(email_pattern);
	}

	public static String find(String patternStr, CharSequence input) {
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	/*
	 * #Japanese postal codes zipJP=^\d{3}-\d{4}$
	 * 
	 * #US postal codes zipUS=^\d{5}\p{Punct}?\s?(?:\d{4})?$
	 * 
	 * #Dutch postal code zipNL=^[0-9]{4}\s*[a-zA-Z]{2}$
	 * 
	 * #Argentinean postal code zipAR=^\d{3}-\d{4}$
	 * 
	 * #Swedish postal code zipSE=^(s-|S-){0,1}[0-9]{3}\s?[0-9]{2}$
	 * 
	 * #Canadian postal code zipCA=^([A-Z]\d[A-Z]\s\d[A-Z]\d)$
	 * 
	 * #UK postal code zipUK=^[a-zA-Z]{1,2}[0-9][0-9A-Za-z]{0,1}
	 * {0,1}[0-9][A-Za-z]{2}$
	 */
	public static boolean isZipValid(String zip, String zipCodePattern) {
		return zip.matches(zipCodePattern);
	}
	
	public static boolean isPhoneValid(String phone) {
	    boolean retval = false;
	    String phoneNumberPattern = "(\\d-)?(\\d{3}-)?\\d{3}-\\d{4}";

	    retval = phone.matches(phoneNumberPattern);
	    return retval;
	}

	public static void replaceByRegex(String inputStr, String regex,
			String replace) {
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(inputStr);
		inputStr = m.replaceAll(replace);
	}

	public static void appendReplace(String inputStr, String regex,
			String replace, StringBuffer sb) {
		if (sb == null) {
			return;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(inputStr);
		m.appendReplacement(sb, replace);
	}

	public static void replaceFirst(String inputStr, String regex,
			String replace) {
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(inputStr);
		inputStr = m.replaceFirst(replace);
	}
}
