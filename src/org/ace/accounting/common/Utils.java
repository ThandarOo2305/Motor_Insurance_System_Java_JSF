package org.ace.accounting.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.GregorianChronology;

import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;

public class Utils {
	private static final int BUFFER_SIZE = 4096;

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

	public boolean checkOneYear(Date startDate, Date endDate) {
		DateTime start = new DateTime(startDate);
		DateTime end = new DateTime(endDate);
		return Months.monthsBetween(start, end).getMonths() == 12 ? true : false;
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
	 * Calculate period between start date and end date.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param isTimeInclude
	 * @param isDayCount
	 * @return period between start date and end date
	 */
	public static Period getPeriod(Date startDate, Date endDate, boolean isTimeInclude, boolean isDayCount) {
		PeriodType periodType = PeriodType.yearMonthDay();
		if (isTimeInclude) {
			periodType = PeriodType.yearMonthDayTime();
		} else {
			startDate = resetStartDate(startDate);
			endDate = resetEndDate(endDate);
		}
		LocalDateTime start = new LocalDateTime(startDate);
		LocalDateTime end = new LocalDateTime(endDate);
		if (!isDayCount) {
			end = end.minusDays(1);
		}
		Period period = new Period(start, end, periodType);
		return period;
	}

	/**
	 * Create date by given year, month and date.
	 * 
	 * @param year
	 * @param month
	 * @param date
	 * @return date of given year, month and date.
	 */
	public static Date createDate(int year, int month, int date) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * Return number of days in given year and month.
	 * 
	 * @param year
	 * @param month
	 * @return number of days by given year and month
	 */
	public static int getDaysNumber(int year, int month) {
		Date date = Utils.createDate(year, month, 1);
		return GregorianChronology.getInstance(DateTimeZone.getDefault()).dayOfMonth().getMaximumValue(date.getTime());
	}

	public static boolean isDateBetween(Date startDate, Date endDate, Date checkDate) {
		return checkDate.after(startDate) && checkDate.before(endDate);
	}

	/**
	 * Return the index of Maximum date.
	 * 
	 * @param DateList
	 * @return index of maximum date.
	 */
	public static int getIndexOfMaxDate(List<Date> dateList) {
		Date maxDate = null;
		if (dateList.size() == 1) {
			maxDate = dateList.get(0);
		} else {
			maxDate = dateList.get(0);
			for (Date d : dateList) {
				if (d.after(maxDate)) {
					maxDate = d;
				}
			}
		}
		return dateList.indexOf(maxDate);
	}

	public static String getCurrencyFormatString(Double value) {
		DecimalFormat formatter = new DecimalFormat("##,###,###,###,###,###,##0.00");
		return formatter.format(value);
	}

	/**
	 * @param stringDate
	 *            : format must be dd-MM-yyyy
	 */
	public static Date getDate(String stringDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		try {
			date = formatter.parse(stringDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getDateFormatString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		return formatter.format(date);
	}
	
	public static String getYearMonthDateFormatString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}


	public static String getDatabaseDateString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return formatter.format(date);
	}

	public static String getFlatDateString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return formatter.format(date);
	}

	public static String getMonthAndYearFormat(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy");
		return formatter.format(date);
	}

	public static String getYearFormat(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		return formatter.format(date);

	}

	public static String formatDateTimeWithAMOrPM(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
		return dateFormat.format(date).toString();
	}

	public static Date getCSCDate(String stringDate) {
		// single d and M determines no leading zero i.e 9/9/2017 instead of
		// 09/09/2017
		SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy");
		Date date = null;
		try {
			date = formatter.parse(stringDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getCSCDateFormatString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy");
		return formatter.format(date);
	}

	public static boolean isAfter(Date date1, Date date2) {
		return Utils.resetStartDate(date1).after(Utils.resetStartDate(date2));
	}

	public static boolean isBefore(Date date1, Date date2) {
		return Utils.resetStartDate(date1).before(Utils.resetStartDate(date2));
	}

	public static int getDayOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		DateTime dateTime = new DateTime(cal.getTime(), DateTimeZone.getDefault());
		return dateTime.getDayOfYear();
	}

	public static double getPercentOf(double percent, double amount) {
		return Utils.divide(amount * percent, 100);
	}

	public static double getPercentOn(double percent, double amount, double baseValue) {
		return (amount / baseValue) * percent;
	}

	public static <T> boolean isNull(T t) {
		if (t == null) {
			return true;
		}
		return false;
	}

	public static <T> boolean isNotNull(T t) {
		if (t != null) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param year
	 * @param month
	 * @return java.util.Date
	 */
	public static Date getStartDate(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		Date starDate = calendar.getTime();
		return Utils.resetStartDate(starDate);
	}

	/**
	 * 
	 * @param year
	 * @param month
	 * @return java.util.Date
	 */
	public static Date getEndDate(int year, int month) {
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(Calendar.MONTH, month);
		calendar2.set(Calendar.YEAR, year);
		DateTime dateTime = new DateTime(calendar2.getTime(), DateTimeZone.getDefault());
		calendar2.set(Calendar.DAY_OF_MONTH, dateTime.dayOfMonth().withMaximumValue().getDayOfMonth());
		Date endDate = calendar2.getTime();
		return Utils.resetEndDate(endDate);
	}

	/**
	 * @param if
	 *            month is 0, "JANUARY" will returned.
	 */
	public static String getMonthString(int month) {
		if (month == 0) {
			return "JANUARY";
		}
		if (month == 1) {
			return "FEBRUARY";
		}
		if (month == 2) {
			return "MARCH";
		}
		if (month == 3) {
			return "APRIL";
		}
		if (month == 4) {
			return "MAY";
		}
		if (month == 5) {
			return "JUNE";
		}
		if (month == 6) {
			return "JULY";
		}
		if (month == 7) {
			return "AUGUST";
		}
		if (month == 8) {
			return "SEPTEMBER";
		}
		if (month == 9) {
			return "OCTOBER";
		}
		if (month == 10) {
			return "NOVEMBER";
		}
		if (month == 11) {
			return "DECEMBER";
		}
		return null;
	}

	public static String getMonthStringWithLowerCase(int month) {
		if (month == 0) {
			return "January";
		}
		if (month == 1) {
			return "February";
		}
		if (month == 2) {
			return "March";
		}
		if (month == 3) {
			return "April";
		}
		if (month == 4) {
			return "May";
		}
		if (month == 5) {
			return "June";
		}
		if (month == 6) {
			return "July";
		}
		if (month == 7) {
			return "August";
		}
		if (month == 8) {
			return "September";
		}
		if (month == 9) {
			return "October";
		}
		if (month == 10) {
			return "November";
		}
		if (month == 11) {
			return "December";
		}
		return null;
	}

	/*
	 * public static MonthType getMonthType(int month) { switch (month) { case 0:
	 * return MonthType.JAN; case 1: return MonthType.FEB; case 2: return
	 * MonthType.MAR; case 3: return MonthType.APR; case 4: return MonthType.MAY;
	 * case 5: return MonthType.JUN; case 6: return MonthType.JUL; case 7: return
	 * MonthType.AUG; case 8: return MonthType.SEP; case 9: return MonthType.OCT;
	 * case 10: return MonthType.NOV; case 11: return MonthType.DEC; } return
	 * MonthType.JAN; }
	 */
	/**
	 * Calculate age for next year.
	 * 
	 * @param dateOfBirth
	 * @return age for next year
	 */
	public static int getAgeForNextYear(Date dateOfBirth) {
		Calendar cal_1 = Calendar.getInstance();
		int currentYear = cal_1.get(Calendar.YEAR);
		Calendar cal_2 = Calendar.getInstance();
		cal_2.setTime(dateOfBirth);
		cal_2.set(Calendar.YEAR, currentYear);

		if (new Date().after(cal_2.getTime())) {
			Calendar cal_3 = Calendar.getInstance();
			cal_3.setTime(dateOfBirth);
			int year_1 = cal_3.get(Calendar.YEAR);
			int year_2 = cal_1.get(Calendar.YEAR) + 1;
			return year_2 - year_1;
		} else {
			Calendar cal_3 = Calendar.getInstance();
			cal_3.setTime(dateOfBirth);
			int year_1 = cal_3.get(Calendar.YEAR);
			int year_2 = cal_1.get(Calendar.YEAR);
			return year_2 - year_1;
		}
	}

	/**
	 * Calculate age for next year.
	 * 
	 * @param dateOfBirth
	 * @return age for next year
	 */
	public static int getAgeForNextYear(Date dateOfBirth, Date calculateOnDate) {
		Calendar cal_1 = Calendar.getInstance();
		cal_1.setTime(calculateOnDate);
		int currentYear = cal_1.get(Calendar.YEAR);
		Calendar cal_2 = Calendar.getInstance();
		cal_2.setTime(dateOfBirth);
		cal_2.set(Calendar.YEAR, currentYear);

		if (calculateOnDate.after(cal_2.getTime())) {
			Calendar cal_3 = Calendar.getInstance();
			cal_3.setTime(dateOfBirth);
			int year_1 = cal_3.get(Calendar.YEAR);
			int year_2 = cal_1.get(Calendar.YEAR) + 1;
			return year_2 - year_1;
		} else {
			Calendar cal_3 = Calendar.getInstance();
			cal_3.setTime(dateOfBirth);
			int year_1 = cal_3.get(Calendar.YEAR);
			int year_2 = cal_1.get(Calendar.YEAR);
			return year_2 - year_1;
		}
	}

	public static int getAgeForOldYear(Date commDate, Date dateOfBirth) {
		Calendar cal_1 = Calendar.getInstance();
		cal_1.setTime(commDate);
		int currentYear = cal_1.get(Calendar.YEAR);
		Calendar cal_2 = Calendar.getInstance();
		cal_2.setTime(dateOfBirth);
		cal_2.set(Calendar.YEAR, currentYear);

		if (commDate.after(cal_2.getTime())) {
			Calendar cal_3 = Calendar.getInstance();
			cal_3.setTime(dateOfBirth);
			int year_1 = cal_3.get(Calendar.YEAR);
			int year_2 = cal_1.get(Calendar.YEAR) + 1;
			return year_2 - year_1;
		} else {
			Calendar cal_3 = Calendar.getInstance();
			cal_3.setTime(dateOfBirth);
			int year_1 = cal_3.get(Calendar.YEAR);
			int year_2 = cal_1.get(Calendar.YEAR);
			return year_2 - year_1;
		}
	}

	public static String getAge(Date dob) {
		Period period = getPeriod(dob);
		return period.getYears() + " Y";
	}

	public static Period getPeriod(Date date) {
		DateTime startDate = new DateTime(date);
		Calendar cal = Calendar.getInstance();
		Date todayDate = cal.getTime();
		DateTime endDate = new DateTime(todayDate);
		Period period = new Period(startDate, endDate);
		return period;
	}


	public static boolean isNullOrEmpty(String value) {
		if (value == null || value.isEmpty()) {
			return true;
		}
		return false;
	}

	public static double divide(double dividend, double divisor, int precision, RoundingMode roundingMode) {
		BigDecimal dividendBig = new BigDecimal(dividend);
		BigDecimal divisorBig = new BigDecimal(divisor);
		return dividendBig.divide(divisorBig, precision, roundingMode).doubleValue();
	}

	/**
	 * Calculate month.main
	 * 
	 * 
	 * @param MonthType
	 * @return integer : if MomthType=January , return 0. If MomthType=February,
	 *         return 1.
	 */
	/*
	 * public static int getMonthNumber(MonthType month) { int result = 0; switch
	 * (month) { case JAN: result = 0; break; case FEB: result = 1; break; case MAR:
	 * result = 2; break; case APR: result = 3; break; case MAY: result = 4; break;
	 * case JUN: result = 5; break; case JUL: result = 6; break; case AUG: result =
	 * 7; break; case SEP: result = 8; break; case OCT: result = 9; break; case NOV:
	 * result = 10; break; case DEC: result = 11; break; default: break; } return
	 * result; }
	 */

	public static Date addMonth(Date date, int month) {
		DateTime dateTime = new DateTime(date, DateTimeZone.getDefault());
		return dateTime.plusMonths(month).toDate();
	}

	public static Date substractMonth(Date date, int month) {
		DateTime dateTime = new DateTime(date, DateTimeZone.getDefault());
		return dateTime.minusMonths(month).toDate();
	}

	public static Date addDay(Date date, int day) {
		DateTime dateTime = new DateTime(date, DateTimeZone.getDefault());
		return dateTime.plusDays(day).toDate();
	}

	public static Date substractDay(Date date, int day) {
		DateTime dateTime = new DateTime(date, DateTimeZone.getDefault());
		return dateTime.minusDays(day).toDate();
	}

	/**
	 * Return value with two decimal point. Round mode.
	 * 
	 * @param value
	 * @return two decimal point value
	 */
	public static double getEightDecimalPoint(double value) {
		BigDecimal bigDecimal = new BigDecimal(value);
		bigDecimal = bigDecimal.setScale(8, RoundingMode.HALF_UP);
		return bigDecimal.doubleValue();
	}

	public static double getTwoDecimalPoint(double value) {
		BigDecimal bigDecimal = new BigDecimal(value);
		bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
		return bigDecimal.doubleValue();
	}

	public static double getOneDecimalPoint(double value) {
		BigDecimal bigDecimal = new BigDecimal(value);
		bigDecimal = bigDecimal.setScale(1, RoundingMode.HALF_UP);
		return bigDecimal.doubleValue();
	}

	/**
	 * Return Percentage of amount on base value. Round mode with two decimal
	 * point.
	 * 
	 * @param amount
	 * @param baseValue
	 * @return percentage of amount on base value
	 */
	public static double getTwoDecimalPercent(double amount, double baseValue) {
		double perent = getPercentOn(100, amount, baseValue);
		return getTwoDecimalPoint(perent);
	}

	public static double getEightDecimalPercent(double amount, double baseValue) {
		double perent = getPercentOn(100, amount, baseValue);
		return getEightDecimalPoint(perent);
	}

	public static double divide(double dividend, double divisor) {
		BigDecimal dividendBig = new BigDecimal(dividend);
		BigDecimal divisorBig = new BigDecimal(divisor);
		return dividendBig.divide(divisorBig, 2, RoundingMode.HALF_UP).doubleValue();
	}

	public static double multiply(double num1, double num2) {
		BigDecimal numBig1 = new BigDecimal(num1);
		BigDecimal numBig2 = new BigDecimal(num2);
		return numBig1.multiply(numBig2).doubleValue();
	}

	public static double nagateIfNagative(double value) {
		if (value < 0) {
			BigDecimal bigValue = new BigDecimal(value);
			return bigValue.negate().doubleValue();
		}
		return value;
	}

	public static boolean isNagative(double value) {
		return (value < 0) ? true : false;
	}

	public static double nagate(double value) {
		BigDecimal bigValue = new BigDecimal(value);
		return bigValue.negate().doubleValue();
	}

	public static int daysOfFinancialYear(Date paramDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(paramDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		LocalDate localDate = null;
		if (month == Calendar.JANUARY || month == Calendar.FEBRUARY || month == Calendar.MARCH) {
			localDate = new LocalDate(year - 1, Calendar.APRIL, 1);
		} else {
			localDate = new LocalDate(year, Calendar.APRIL, 1);
		}
		int numberOfDays = Days.daysBetween(localDate, localDate.plusMonths(12)).getDays();
		return numberOfDays;
	}

	/* unused */
	public static <T> boolean isNCB(Date policyEndDate) {
		Date fromDate = substractMonth(policyEndDate, 2);
		fromDate = substractDay(fromDate, 15);
		Date currentDate = new Date();
		Date toDate = addMonth(policyEndDate, 1);
		toDate = addDay(toDate, 1);
		fromDate = Utils.resetStartDate(fromDate);
		toDate = Utils.resetEndDate(toDate);
		if (fromDate.before(currentDate) && currentDate.before(toDate)) {
			return true;
		} else {
			return false;
		}
	}

	public static <T> boolean isYearlyPolicy(Date policyStartDate, Date policyEndDate, Date proposalStartDate, Date proposalEndDate) {
		DateTime oldstart = new DateTime(policyStartDate);
		DateTime oldend = new DateTime(policyEndDate);
		DateTime newstart = new DateTime(proposalStartDate);
		DateTime newend = new DateTime(proposalEndDate);
		return (Months.monthsBetween(oldstart, oldend).getMonths() == 12) && (Months.monthsBetween(newstart, newend).getMonths() == 12);
	}

	public static boolean isExpiredRenewal(Date policyEndDate, int allowNextMonth) {
		Calendar oldEndDate = Calendar.getInstance();
		oldEndDate.setTime(policyEndDate);
		oldEndDate.add(Calendar.MONTH, allowNextMonth);
		Calendar todayDate = Calendar.getInstance();
		if (oldEndDate.after(todayDate))
			return false;
		else
			return true;
	}

	public static double round2Decimal(double d) {
		d = Math.round(d * 100) / 100.0d;
		return d;
	}

	public static String getResourceAsString(String filepath) throws IOException {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filepath);
		StringWriter writer = new StringWriter();
		IOUtils.copy(inputStream, writer);
		return writer.toString();
	}

	public static void unzip(String zipFilePath, String destDirectory) throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		ZipEntry entry = zipIn.getNextEntry();
		// iterates over entries in the zip file
		while (entry != null) {
			String filePath = destDirectory + File.separator + entry.getName();
			if (!entry.isDirectory()) {
				// if the entry is a file, extracts it
				extractFile(zipIn, filePath);
			} else {
				// if the entry is a directory, make the directory
				File dir = new File(filePath);
				dir.mkdir();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
	}

	private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}

	/*
	 * public static List<String> getTableNameList(String tableName) { List<String>
	 * result = new ArrayList<String>(); if (ZipFileName.TLF.equals(tableName)) {
	 * result.add("TLF"); return result; } else if
	 * (ZipFileName.SYSTEM.equals(tableName)) { result.add("Country");
	 * result.add("TypesOfSport"); result.add("Occupation");
	 * result.add("BuildingOccupation"); result.add("Industry");
	 * result.add("Qualification"); result.add("Religion");
	 * result.add("RelationShip"); result.add("Province"); result.add("Township");
	 * result.add("Branch"); result.add("WorkShop"); result.add("ClassOfInsurance");
	 * result.add("Company"); // result.add("CoinsuranceCompany");
	 * result.add("Organization"); result.add("SaleMan"); return result; } else if
	 * (ZipFileName.TRAVEL.equals(tableName)) { result.add("City");
	 * result.add("Occurrence"); result.add("Express"); return result; } else if
	 * (ZipFileName.PAYMENT.equals(tableName)) { result.add("Currency");
	 * result.add("Deno"); result.add("Bank"); result.add("BankBranch");
	 * result.add("PaymentType"); return result; } else if
	 * (ZipFileName.USERAUTHORITY.equals(tableName)) { result.add("Role");
	 * result.add("User"); return result; } else if
	 * (ZipFileName.MOTOR.equals(tableName)) { result.add("TypeOfBody");
	 * result.add("MotorType"); result.add("Manufacture");
	 * result.add("VehiclePart"); return result; } else if
	 * (ZipFileName.FIRE.equals(tableName)) { result.add("Roof");
	 * result.add("Wall"); result.add("Floor"); result.add("MainCover");
	 * result.add("BuildingClass"); result.add("SettingVariable"); return result; }
	 * else if (ZipFileName.PRODUCT.equals(tableName)) { result.add("AddOn");
	 * result.add("KeyFactor"); result.add("ProductGroup"); result.add("Product");
	 * return result; }
	 * 
	 * return null; }
	 * 
	 * public static Map<Integer, Unit> getUnits(int maxUnit, boolean flag) {
	 * Map<Integer, Unit> unitMap = new HashMap<Integer, Unit>(); if (flag) { for
	 * (int i = 1; i <= maxUnit; i++) { Unit unit = new Unit(new
	 * DefaultProcessor().getName(i).toUpperCase(), i); unitMap.put(i, unit); } }
	 * else { Unit unit = new Unit(new
	 * DefaultProcessor().getName(maxUnit).toUpperCase(), maxUnit); unitMap.put(0,
	 * unit); } return unitMap; }
	 */
	public static boolean isEmpty(String value) {
		if (value == null || value.isEmpty()) {
			return true;
		}
		return false;
	}

	public static String getSystemPath() {
		Object context = FacesContext.getCurrentInstance().getExternalContext().getContext();
		String systemPath = ((ServletContext) context).getRealPath("/");
		return systemPath;
	}

	/*
	 * public static String getCompleteName(String initialId, Name name) { String
	 * result = ""; if (name != null) { if (initialId != null &&
	 * !initialId.isEmpty()) { result = initialId + " "; } if (name.getFirstName()
	 * != null && !name.getFirstName().isEmpty()) { result = result +
	 * name.getFirstName(); } if (name.getMiddleName() != null &&
	 * !name.getMiddleName().isEmpty()) { result = result + " " +
	 * name.getMiddleName(); } if (name.getLastName() != null &&
	 * !name.getLastName().isEmpty()) { result = result + " " + name.getLastName();
	 * } } return result; }
	 */

	public static String getLastNCharFromString(String string, int i) {
		String result = string.trim().substring(string.length() - i);
		return result;
	}

	/*
	 * public static Date addYears(Date date, int years) { return
	 * org.apache.commons.lang.time.DateUtils.addYears(date, years); }
	 */

	/**
	 * @param month
	 *            : if month = 0, it is January. If month = 11, it is December.
	 */
	public static Date getStartDateOfMonth(int month, int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/**
	 * @param month
	 *            : if month = 0, it is January. If month = 11, it is December.
	 */
	public static Date getEndDateOfMonth(int month, int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR, year);
		DateTime dateTime = new DateTime(cal.getTime());
		DateTime lastTime = dateTime.dayOfMonth().withMaximumValue();
		return new Date(lastTime.getMillis());
	}

	public static String formattedCurrency(double value) {
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
		StringBuffer buffer = new StringBuffer(numberFormat.format(value));
		return buffer.substring(1).toString();
	}

	/**
	 * @param double
	 *            : if double = 1.0, return String 1.0000%.
	 */
	public static String formattedPercentage(double value) {
		DecimalFormat decimalFormat = new DecimalFormat("##,##0.0000");
		StringBuffer buffer = new StringBuffer(decimalFormat.format(value) + "%");
		return buffer.toString();
	}

	/**
	 * @param Date
	 *            : if Date, return Date eg. 12-30-2000.
	 */
	public static String formattedDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(date);
	}

	/**
	 * @param Date
	 * @param String
	 *            return Date string with dateFormat.
	 */
	public static String formattedDate(Date date, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}

	public static void removeBlankPages(JasperPrint jp) {
		List<JRPrintPage> pages = jp.getPages();
		for (Iterator<JRPrintPage> i = pages.iterator(); i.hasNext();) {
			JRPrintPage page = (JRPrintPage) i.next();
			if (page.getElements().size() == 0)
				i.remove();
		}
	}
}
