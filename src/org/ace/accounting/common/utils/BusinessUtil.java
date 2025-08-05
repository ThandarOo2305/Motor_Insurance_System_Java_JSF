package org.ace.accounting.common.utils;

import java.util.Date;

import org.ace.accounting.system.setup.persistence.interfaces.ISetupDAO;
import org.ace.accounting.system.setup.persistence.interfaces.ISetup_HistoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessUtil {

	public static final String DEPARTMENT_STATUS = "DEPARTMENT_STATUS";

	public static final String MULTI_CURRENCY = "MULTI_CURRENCY";

	public static final String HOMECURRENCY_ID = "HOMECURRENCY_ID";

	public static final String BRANCH_ID = "BRANCH_ID";

	public static final String BUDGETEDATE = "BUDGETEDATE";

	public static final String BUDGETSDATE = "BUDGETSDATE";

	public static final String BUDSMTH = "BUDSMTH";

	public static final String BACKDATE = "BACKDATE";

	private static ISetupDAO setupDAO;

	private static ISetup_HistoryDAO setupHistoryDAO;

	public static final String EDIT_PASSWORD = "EDITPASSWORD";

	@Autowired
	public void setSetupDAO(ISetupDAO setupDAO) {
		BusinessUtil.setupDAO = setupDAO;
	}

	@Autowired
	public void setSetupHistoryDAO(ISetup_HistoryDAO setupHistoryDAO) {
		BusinessUtil.setupHistoryDAO = setupHistoryDAO;
	}

	public static String getMonthStatus(int month) {
		return "M" + month;
	}

	public static String getMonthSRCStatus(int month) {
		return "MSRC" + month;
	}

	public static String getMonthStatusJPQLField(int month) {
		return "monthlyRate.m" + month;
	}

	public static String getMonthSRCStatusJPQLField(int month) {

		return "msrcRate.msrc" + month;
	}

	public static String getBudgetStatus(int month) {
		return "BF" + month;
	}

	public static String getBudgetSRCStatus(int month) {
		return "BFSRC" + month;
	}

	public static boolean isDepartment() {
		return Boolean.parseBoolean(setupDAO.findSetupValueByVariable(DEPARTMENT_STATUS));
	}

	public static boolean isMultiCurrency() {
		return Boolean.parseBoolean(setupDAO.findSetupValueByVariable(MULTI_CURRENCY));
	}

	public static String getHomeCurrency() {
		return setupDAO.findSetupValueByVariable(HOMECURRENCY_ID);
	}

	public static String getMainBranch() {
		return setupDAO.findSetupValueByVariable(BRANCH_ID);
	}

	public static Date getBudgetEndDate() {
		// return
		// DateUtils.formatStringToDate(setupDAO.findSetupValueByVariable(BUDGETEDATE));
		return DateUtils.formatStringToDate(setupDAO.findSetupValueByVariable(BUDGETEDATE));
	}

	// prev_setup data
	public static Date getPrevBudgetEndDate(String budgetYear) {
		// return
		// DateUtils.formatStringToDate(setupDAO.findSetupValueByVariable(BUDGETEDATE));
		return DateUtils.formatStringToDate(setupHistoryDAO.findSetupHistoryValueByVariable(BUDGETEDATE, budgetYear));
	}

	public static Date getBudgetStartDate() {
		// return
		// DateUtils.formatStringToDate(setupDAO.findSetupValueByVariable(BUDGETSDATE));
		return DateUtils.formatStringToDate(setupDAO.findSetupValueByVariable(BUDGETSDATE));
	}

	public static Date getPrevBudgetStartDate(String budgetYear) {
		// return
		// DateUtils.formatStringToDate(setupDAO.findSetupValueByVariable(BUDGETSDATE));
		return DateUtils.formatStringToDate(setupHistoryDAO.findSetupHistoryValueByVariable(BUDGETSDATE, budgetYear));
	}

	public static int getBudSmth() {
		// return Integer.parseInt(setupDAO.findSetupValueByVariable(BUDSMTH));
		return Integer.parseInt(setupDAO.findSetupValueByVariable(BUDSMTH));
	}

	public static int getPrevBudSmth(String budgetYear) {
		// return Integer.parseInt(setupDAO.findSetupValueByVariable(BUDSMTH));
		return Integer.parseInt(setupHistoryDAO.findSetupHistoryValueByVariable(BUDSMTH, budgetYear));
	}

	public static Date getBackDate() {
		Date date = DateUtils.formatStringToDate(setupDAO.findSetupValueByVariable(BACKDATE));
		return date;
	}

	public static String getCurrentBudget() {
		// return Integer.parseInt(setupDAO.findSetupValueByVariable(BUDSMTH));
		return setupDAO.findSetupBudgetByVariable(BUDSMTH);
	}

	public static String getPosition(int i) {
		// for formula checking error pointing
		i++;
		if (i == 1 || (i % 100) == 1) {
			return i + "st";
		} else if (i == 2 || (i % 100) == 2) {
			return i + "nd";
		} else if (i == 3 || (i % 100) == 3) {
			return i + "rd";
		} else if (i > 3 && i < 21 || (i % 100) > 3 && (i % 100) < 21) {
			return i + "th";
		} else if ((i % 10) == 1) {
			return i + "st";
		} else if ((i % 10) == 2) {
			return i + "nd";
		} else if ((i % 10) == 3) {
			return i + "rd";
		}
		return "th";
	}

	public static String getMonthlyRateFormula(int value) {
		int budSmth = getBudSmth();
		int budInfo = value - budSmth + 1;
		if (budInfo <= 0) {
			budInfo = budInfo + 12;
		}
		return "m" + budInfo;
	}

	public static String getBudgetInfo(Date pDate, int mode) {

		int budSmth = getBudSmth();
		String budInfo = "";
		int year = DateUtils.getYearFromDate(pDate);
		// int nextYear = year + 1;

		switch (mode) {
			case 1:// Show Budget Year Eg. 00#01

				if (budSmth == 1) {
					budInfo = String.valueOf(year).substring(2) + "#" + String.valueOf(year).substring(2);
				} else {
					if (DateUtils.getMonthFromDate(pDate) < budSmth) {
						budInfo = String.valueOf(year - 1).substring(2) + "#" + String.valueOf(year).substring(2);
					} else {
						budInfo = String.valueOf(year).substring(2) + "#" + String.valueOf(year + 1).substring(2);
					}
				}

				break;

			case 2:// Show Budget Year Eg.2000/2001

				if (budSmth == 1) {
					budInfo = year + "/" + year;
				} else {
					int pMonth = DateUtils.getMonthFromDate(pDate) + 1;
					if (budSmth > pMonth) {
						budInfo = (year - 1) + "/" + year;
					} else {
						budInfo = year + "/" + (year + 1);
					}
				}
				break;

			case 3:// Show Budget Month Eg.M1, M2
				int month = DateUtils.getMonthFromDate(pDate) + 1;
				budInfo = String.valueOf((month < budSmth ? month + 12 : month) + 1 - budSmth);
				break;
			case 4:// Show Quarter Eg. Q1,Q2,Q3,Q4

				int counter = 0;
				int mStart = budSmth;
				int mEnd = budSmth + 2;
				int period = 1;

				for (int i = budSmth; i <= budSmth + 12; i++) {
					counter = counter + 1;

					if (counter % 3 == 0) {
						mStart = (mEnd + 1) > 12 ? mEnd + 1 - 12 : mEnd + 1;
						mEnd = (mStart + 2) > 12 ? mStart + 2 - 12 : mStart + 2;
						period = period + 1;
					}

					if (mStart <= DateUtils.getMonthFromDate(pDate) && mEnd >= DateUtils.getMonthFromDate(pDate)) {
						budInfo = period + "";
						break;
					}
				}

				break;
		}

		return budInfo;
	}

	public static String getPrevBudgetInfo(Date pDate, String budgetYear, int mode) {

		int budSmth = getPrevBudSmth(budgetYear);
		String budInfo = "";
		int year = DateUtils.getYearFromDate(pDate);
		// int nextYear = year + 1;

		switch (mode) {
			case 1:// Show Budget Year Eg. 00#01

				if (budSmth == 1) {
					budInfo = String.valueOf(year).substring(2) + "#" + String.valueOf(year).substring(2);
				} else {
					if (DateUtils.getMonthFromDate(pDate) < budSmth) {
						budInfo = String.valueOf(year - 1).substring(2) + "#" + String.valueOf(year).substring(2);
					} else {
						budInfo = String.valueOf(year).substring(2) + "#" + String.valueOf(year + 1).substring(2);
					}
				}

				break;

			case 2:// Show Budget Year Eg.2000/2001

				if (budSmth == 1) {
					budInfo = year + "/" + year;
				} else {
					int pMonth = DateUtils.getMonthFromDate(pDate) + 1;
					if (budSmth > pMonth) {
						budInfo = (year - 1) + "/" + year;
					} else {
						budInfo = year + "/" + (year + 1);
					}
				}
				break;

			case 3:// Show Budget Month Eg.M1, M2
				int month = DateUtils.getMonthFromDate(pDate) + 1;
				budInfo = String.valueOf((month < budSmth ? month + 12 : month) + 1 - budSmth);
				break;
			case 4:// Show Quarter Eg. Q1,Q2,Q3,Q4

				int counter = 0;
				int mStart = budSmth;
				int mEnd = budSmth + 2;
				int period = 1;

				for (int i = budSmth; i <= budSmth + 12; i++) {
					counter = counter + 1;

					if (counter % 3 == 0) {
						mStart = (mEnd + 1) > 12 ? mEnd + 1 - 12 : mEnd + 1;
						mEnd = (mStart + 2) > 12 ? mStart + 2 - 12 : mStart + 2;
						period = period + 1;
					}

					if (mStart <= DateUtils.getMonthFromDate(pDate) && mEnd >= DateUtils.getMonthFromDate(pDate)) {
						budInfo = period + "";
						break;
					}
				}

				break;
		}

		return budInfo;
	}

	public static boolean getPwdControl(Date budgetStartDate, Date minDate, Date settlementDate) {

		boolean isEdit;

		if ((settlementDate.after(budgetStartDate) || settlementDate.equals(budgetStartDate)) && (settlementDate.before(minDate) || settlementDate.equals(minDate))) {
			isEdit = true;
		} else {
			isEdit = false;
		}
		return isEdit;
	}

	// TODO FIXME PSH
	// remove comment when yearly posting is run
	/**
	 * WARNING !!! ONLY FOR YEAR POSTING
	 */
	@SuppressWarnings("unused")
	public static void increaseBudgetYear() {
		Date budgetStartDate = DateUtils.plusYears(getBudgetStartDate(), 1);
		Date budgetEndDate = DateUtils.plusYears(getBudgetEndDate(), 1);
		// setupDAO.updateSetupValueByVariable(BUDGETSDATE,
		// DateUtils.formatDateToString(budgetStartDate));
		// setupDAO.updateSetupValueByVariable(BUDGETEDATE,
		// DateUtils.formatDateToString(budgetEndDate));
		// appSetting.setProperty(BUDGETSDATE,
		// DateUtils.formatDateToString(budgetStartDate));
		// appSetting.setProperty(BUDGETEDATE,
		// DateUtils.formatDateToString(budgetEndDate));
	}

	public static String getVoucherEditPassword() {
		return setupDAO.findSetupValueByVariable(EDIT_PASSWORD);
	}
}
