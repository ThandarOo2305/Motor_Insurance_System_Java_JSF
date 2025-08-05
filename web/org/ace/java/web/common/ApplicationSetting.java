package org.ace.java.web.common;

import java.util.TimeZone;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@SessionScoped
@ManagedBean(name = "ApplicationSetting")
public class ApplicationSetting {

	private static final String DATE_TIME_FORMAT = "dd-MM-yyyy hh:mm a";
	private static final String DATE_FORMAT = "dd-MM-yyyy";
	private static final String CURRENCY_FORMAT = "##,###.00";
	private static final String CURRENCY_FORMAT2 = "##,##0.0000";
	private static final String CURRENCY_FORMAT3 = "##,##0.00";
	private static final String NUMBER_FORMAT = "##,###.00";
	private static final String NUMBER_FORMAT2 = "##,###.0000";
	private static final String PERCENT_FORMAT = "0.#####";
	private static final String DATE_DAY_MONTH_FORMAT = "dd-MMMM";
	private static final String MONTH_PICKER = "MM-yyyy";
	private static final String LOGO= "pdf-report/fni-logo.png";
	

	public static String getCompanyLogoFilePath() {
			return LOGO;
	}
	

	public String getDateDayMonthFormat() {
		return DATE_DAY_MONTH_FORMAT;
	}

	public String getTimeZone() {
		return TimeZone.getDefault().getID();
	}

	public String getDateTimeFormat() {
		return DATE_TIME_FORMAT;
	}

	public String getDateFormat() {
		return DATE_FORMAT;
	}

	public String getCurrencyFormat() {
		return CURRENCY_FORMAT;
	}

	public String getCurrencyFormat2() {
		return CURRENCY_FORMAT2;
	}

	public String getCurrencyFormat3() {
		return CURRENCY_FORMAT3;
	}

	public String getNumberFormat() {
		return NUMBER_FORMAT;
	}

	public String getNumberFormat2() {
		return NUMBER_FORMAT2;
	}

	public String getPercentFormat() {
		return PERCENT_FORMAT;
	}

	public String getMonthPicker() {
		return MONTH_PICKER;
	}
}
