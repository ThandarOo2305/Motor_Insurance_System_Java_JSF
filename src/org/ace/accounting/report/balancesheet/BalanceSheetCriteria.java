package org.ace.accounting.report.balancesheet;

import java.util.Date;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;

public class BalanceSheetCriteria {

	private CurrencyType currencyType;
	private Currency currency;
	private Branch branch;
	private boolean isMonth;
	private boolean isHomeCur;
	private boolean isHomeConverted;
	private Date startDate;
	private Date endDate;
	private String reportType;
	private String budgetYear;

	public CurrencyType getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(CurrencyType currencyType) {
		this.currencyType = currencyType;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public boolean isMonth() {
		return isMonth;
	}

	public void setMonth(boolean isMonth) {
		this.isMonth = isMonth;
	}

	public boolean isHomeCur() {
		return isHomeCur;
	}

	public void setHomeCur(boolean isHomeCur) {
		this.isHomeCur = isHomeCur;
	}

	public boolean isHomeConverted() {
		return isHomeConverted;
	}

	public void setHomeConverted(boolean isHomeConverted) {
		this.isHomeConverted = isHomeConverted;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = DateUtils.resetStartDate(startDate);
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = DateUtils.resetStartDate(endDate);
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getBudgetYear() {
		return budgetYear;
	}

	public void setBudgetYear(String budgetYear) {
		this.budgetYear = budgetYear;
	}

}
