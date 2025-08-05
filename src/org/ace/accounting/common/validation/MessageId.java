package org.ace.accounting.common.validation;

public class MessageId {

	/* System Error */
	public static final String SYSTEM_ERROR = "SYSTEM_ERROR";

	/* When Login Failing */
	public static final String LOGIN_FAILED = "LOGIN_FAILED";

	/* When user is not admin and doesn't have branch */
	public static final String USER_NO_BRANCH = "USER_NO_BRANCH";

	/* When insert successfully */
	public static final String INSERT_SUCCESS = "INSERT_SUCCESS";

	/* When update successfully */
	public static final String UPDATE_SUCCESS = "UPDATE_SUCCESS";

	/* When delete successfully */
	public static final String DELETE_SUCCESS = "DELETE_SUCCESS";

	//////////////////////////////// Application Message
	//////////////////////////////// ////////////////////////////////////

	/* When Account Code are used in transaction. */
	public static final String TRANS_USED = "TRANS_USED";

	/* When Chart Account Code already exist in database. */
	public static final String ACCODE_EXISTED = "ACCODE_EXISTED";

	/* When IBSB Account Code already exist in database. */
	public static final String IBSBCODE_EXISTED = "IBSBCODE_EXISTED";

	/* When Code No already exist in database. */
	public static final String DUPLICATE_CODE = "DUPLICATE_CODE";

	/* When Home Currency already exist in database. */
	public static final String HOME_CUR_USED = "HOME_CUR_USED";

	public static final String CUR_USED_TRANS = "CUR_USED_TRANS";

	public static final String DEPT_USED_TRANS = "DEPT_USED_TRANS";

	public static final String REQUIRED_ACCOUNTTYPE = "REQUIRED_ACCOUNTTYPE";

	public static final String PERCENTAGE_ERROR = "PERCENTAGE_ERROR";

	public static final String OUT_OF_BALANCE = "OUT_OF_BALANCE";

	public static final String REQUIRED_EXCHANGERATE = "REQUIRED_EXCHANGERATE";

	public static final String NO_DATA_TODELETE = "NO_DATA_TODELETE";

	public static final String REQUIRE_REPORT_TYPE = "REQUIRE_REPORT_TYPE";

	public static final String REPORT_ERROR = "REPORT_ERROR";

	public static final String NO_RESULT = "NO_RESULT";

	/*
	 * Report Format Entry
	 */
	public static final String DUPE_FORMATFILE = "DUPE_FORMATFILE";

	public static final String RANGE_ERROR = "RANGE_ERROR";

	public static final String RANGE_EXCEEDED = "RANGE_EXCEEDED";

	public static final String INVALID_START = "INVALID_START";

	public static final String DUPE_CHAR = "DUPE_CHAR";

	public static final String LINE_EXCEEDED = "LINE_EXCEEDED";

	public static final String ERROR_FORMULA_FOUND = "ERROR_FORMULA_FOUND";

	public static final String INVALID_FORMAT = "INVALID_FORMAT";

	public static final String INVALID_OCB = "INVALID_OCB";

	public static final String INVALID_CCB = "INVALID_CCB";

	public static final String REQUIRED_OCB = "REQUIRED_OCB";

	public static final String REQUIRED_CCB = "REQUIRED_CCB";

	/**
	 * Currency Management
	 */
	public final static String REQUIRED_CURRENCY = "REQUIRED_CURRENCY";

	/**
	 * Voucher
	 */
	public static final String REQUIRED_ACCOUNTCODE = "REQUIRED_ACCOUNTCODE";
	public static final String REQUIRED = "REQUIRED";
	public static final String NO_EXCHANGE_RATE = "NO_EXCHANGE_RATE";
	public static final String CREDIT_DEBIT_INBALANCE = "CREDIT_DEBIT_INBALANCE";
	public static final String AMOUNT_DATE_INBALANCE = "AMOUNT_DATE_INBALANCE";
	public static final String VALUE = " FAIL_TO_INSERT";

	/**
	 * Edit Voucher
	 */
	public static final String DEBIT_CREDIT_BALANCE = "DEBIT_CREDIT_BALANCE";
	public static final String NO_DATA_TOSAVE = "NO_DATA_TOSAVE";
	public static final String NO_DATA_TOEDIT = "NO_DATA_TOEDIT";

	/**
	 * Voucher Listing
	 */
	public static final String SE_INVALID_DATE = "SE_INVALID_DATE";

	public static final String START_DATE_EXCEEDED = "START_DATE_EXCEEDED";

	public static final String END_DATE_EXCEEDED = "END_DATE_EXCEEDED";

	/**
	 * Account Ledger Listing
	 */
	public static final String OPENING_BALANCE_ERROR = "OPENING_BALANCE_ERROR";
	public static final String DATE_EXCEEDED = "DATE_EXCEEDED";
	public static final String ENDDATE_INVALID = "ENDDATE_INVALID";

	/**
	 * Voucher Printing
	 */
	public static final String SE_REQUIRED = "SE_REQUIRED";
	public static final String VOUCHER_INVALID = "VOUCHER_INVALID";
	public static final String REQUIRED_FROMVOUCHER = "REQUIRED_FROMVOUCHER";
	public static final String REQUIRED_TOVOUCHER = "REQUIRED_TOVOUCHER";

	/**
	 * Daily Posting
	 */
	public static final String POSTING_EXCEDDED_ERROR = "POSTING_EXCEDDED_ERROR";
	public static final String POSTING_DONE_ERROR = "POSTING_DONE_ERROR";
	public static final String POSTING_SUCCESS = "POSTING_SUCCESS";
	public static final String CLONING_SUCCESS = "CLONING_SUCCESS";
	public static final String VALID_POSTING = "VALID_POSTING";
	public static final String VALID_CLONING = "VALID_CLONING";
	public static final String VALID_VOUCHER_DATE = "VALID_VOUCHER_DATE";
	public static final String VOUCHER_DATE_EXCEDDED_ERROR = "VOUCHER_DATE_EXCEDDED_ERROR";

	/**
	 * Yearly Posting
	 */
	public static final String YP_POSTINGDATE_ERROR = "YP_POSTINGDATE_ERROR";
	public static final String YP_BUDGETEDATE_ERROR = "YP_BUDGETEDATE_ERROR";
	public static final String YP_POSTING_NOTREQUIRED = "YP_POSTING_NOTREQUIRED";
	public static final String YP_POSTING_SUCCEEDED = "YP_POSTING_SUCCEEDED";
	public static final String HO_POSTING_SUCCEEDED = "HO_POSTING_SUCCEEDED";

	/**
	 * BranchValidator
	 */
	public static final String BRANCH_USED_TRANS = "BRANCH_USED_TRANS";

	/**
	 * Clean Cash Report
	 */
	public static final String GREATER_THAN_CURRENT_DATE = "GREATER_THAN_CURRENT_DATE";
	public static final String NO_RECORDS = "NO_RECORDS";

	/**
	 * Report Statement
	 */
	public static final String CUR_REQUIRED = "CUR_REQUIRED";
	public static final String MONTH_EXCEEDED = "MONTH_EXCEEDED";
	public static final String NO_SUITABLE_ACCOUNT = "NO_SUITABLE_ACCOUNT";

	/**
	 * COST_ALLOCATION
	 */
	public static final String COST_ALLOCATION = "COST_ALLOCATION";

	/**
	 * Voucher Edit Password
	 */
	public static final String INVALID_USER = "INVALID_USER";
	public static final String WRONG_PASSWORD = "WRONG_PASSWORD";

	/* Change Password */
	public static final String OLD_PASSWORD_DOES_NOT_MATCH = "OLD_PASSWORD_DOES_NOT_MATCH";
	public static final String DOES_NOT_MATCH_CONFIRM_PASSWORD = "DOES_NOT_MATCH_CONFIRM_PASSWORD";
	public static final String SUCCESS_CHANGE_PASSWORD = "SUCCESS_CHANGE_PASSWORD";
	public final static String DUPLICATE_ORDER = "DUPLICATE_ORDER";
	public final static String LANGUAGE_ALREADY_EXISTS = "LANGUAGE_ALREADY_EXISTS";

}
