package org.ace.accounting.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.ace.accounting.system.chartaccount.persistence.interfaces.ICcoaDAO;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.Months;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DateUtils {

	private static ICcoaDAO ccoaDAO;

	@Autowired
	public void setCcoaDAO(ICcoaDAO ccoaDAO) {
		DateUtils.ccoaDAO = ccoaDAO;
	}

	private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	private static SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm:ss a");

	public static String formatDateToString(Date date) {
		return formatter.format(date);
	}

	public static String formatDateToStringTime(Date date) {
		return timeFormatter.format(date);
	}

	public static Date formatStringToDate(String string) {
		Date date = null;
		try {
			date = formatter.parse(string);
		} catch (ParseException e) {
		}
		return date;
	}

	public static Date formatDate(Date date) {
		Date result = null;
		try {
			result = formatter.parse(formatter.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static int getYearFromDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		return year;
	}

	public static int getMonthFromDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		return month;
	}

	public static int getDayFromDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return day;
	}

	public static SimpleDateFormat getFormatter() {
		return formatter;
	}

	public static Date resetStartDate(Date startDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date resetEndDate(Date endDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	/**
	 * Subtract day from given date.
	 * 
	 * @param date
	 * @param day
	 * @return date that is subtracted by given day
	 */
	public static Date minusDays(Date date, int day) {
		DateTime dateTime = new DateTime(date, DateTimeZone.getDefault());
		return dateTime.minusDays(day).toDate();
	}

	/**
	 * Add day from given date.
	 * 
	 * @param date
	 * @param day
	 * @return date that is added by given day
	 */
	public static Date plusDays(Date date, int day) {
		DateTime dateTime = new DateTime(date, DateTimeZone.getDefault());
		return dateTime.plusDays(day).toDate();
	}

	/**
	 * Add Year from given date.
	 * 
	 * @param date
	 * @param year
	 * @return date that is added by given day
	 */
	public static Date plusYears(Date date, int year) {
		DateTime dateTime = new DateTime(date, DateTimeZone.getDefault());
		return dateTime.plusYears(year).toDate();
	}

	/**
	 * Add Month from given date.
	 * 
	 * @param date
	 * @param month
	 * @return date that is added by given day
	 */
	public static Date plusMonths(Date date, int month) {
		DateTime dateTime = new DateTime(date, DateTimeZone.getDefault());
		return dateTime.plusMonths(month).toDate();
	}

	/**
	 * Calculate number of days between start date and end date.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param isTimeInclude
	 * @param isDayCount
	 * @return number of days between start date and end date
	 */
	public static int daysBetween(Date startDate, Date endDate, boolean isTimeInclude, boolean isDayCount) {
		// reset time in calculation if isTimeInclude is false
		if (!isTimeInclude) {
			startDate = resetStartDate(startDate);
			endDate = resetStartDate(endDate);
		}
		DateTime start = new DateTime(startDate);
		DateTime end = new DateTime(endDate);
		if (isDayCount) {
			end = end.plusDays(1);
		}
		return Days.daysBetween(start, end).getDays();
	}

	public static int monthsBetween(Date startDate, Date endDate, boolean isTimeInclude, boolean isDayCount) {
		// reset time in calculation if isTimeInclude is false
		if (!isTimeInclude) {
			startDate = resetStartDate(startDate);
			endDate = resetStartDate(endDate);
		}
		DateTime start = new DateTime(startDate);
		DateTime end = new DateTime(endDate);
		if (isDayCount) {
			end = end.plusDays(1);
		}
		return Months.monthsBetween(start, end).getMonths();
	}

	/**
	 * Check DayOfMonth and MonthOfYear of two dates are equal.
	 * 
	 * @param firstDate
	 * @param secondDate
	 */
	public static boolean equalDayAndMonth(DateTime first, DateTime second) {
		if ((first.getDayOfMonth() == second.getDayOfMonth()) && (first.getMonthOfYear() == second.getMonthOfYear())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * The method to check the Parameter Date is within budget year or not.
	 * 
	 * @param paramDate
	 * 
	 * @return true if paramDate have within budget year, false else.
	 */
	public static boolean isIntervalBugetYear(Date paramDate) {
		DateTime start = new DateTime(resetStartDate(BusinessUtil.getBudgetStartDate()));
		DateTime end = new DateTime(resetEndDate(BusinessUtil.getBudgetEndDate()));
		DateTime param = new DateTime(resetStartDate(paramDate));
		Interval interval = new Interval(start, end);
		return interval.contains(param);
	}

	public static List<Integer> getMonthsOfYear() {
		List<Integer> monthList = new ArrayList<>();
		for (int i = 1; i <= 12; i++) {
			monthList.add(i);
		}
		return monthList;
	}

	public static List<Integer> getActiveYears() {
		List<String> budgetYearList = ccoaDAO.findAllBudgetYear();
		List<Integer> yearList = new ArrayList<>();
		int startYear = 3000;
		int endYear = 0;
		for (String budgetYear : budgetYearList) {
			// TODO - two records in prev_ccoa has no budget
			if (budgetYear != null) {
				if (Integer.parseInt(budgetYear.split("/")[0]) < startYear) {
					startYear = Integer.parseInt(budgetYear.split("/")[0]);
				}
				if (Integer.parseInt(budgetYear.split("/")[0]) > endYear) {
					endYear = Integer.parseInt(budgetYear.split("/")[0]);
				}
			}
		}
		for (int i = startYear; i <= endYear; i++) {
			yearList.add(i);
		}
		// if the current budget year is 2015/2016 , then endyear is 2015
		// following handle the 2016 if the year is already 2016 but budget
		// year is still 2015/2016
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		if (endYear < cal.get(Calendar.YEAR))
			yearList.add(endYear + 1);

		return yearList;
	}

}
