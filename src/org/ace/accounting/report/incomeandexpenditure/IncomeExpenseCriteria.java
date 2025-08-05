package org.ace.accounting.report.incomeandexpenditure;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;

public class IncomeExpenseCriteria {

	private CurrencyType currencyType;
	private Currency currency;
	private Branch branch;
	private boolean isQuarterly;
	private boolean isGroup;
	private boolean isHomeCur;
	private boolean isHomeConverted;
	private int year;
	private int month;
	private int quarter;

	public IncomeExpenseCriteria() {
	}

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

	public boolean isGroup() {
		return isGroup;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	public boolean isHomeCur() {
		return isHomeCur;
	}

	public void setHomeCur(boolean isHomeCur) {
		this.isHomeCur = isHomeCur;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public boolean isHomeConverted() {
		return isHomeConverted;
	}

	public void setHomeConverted(boolean isHomeConverted) {
		this.isHomeConverted = isHomeConverted;
	}

	public boolean isQuarterly() {
		return isQuarterly;
	}

	public void setQuarterly(boolean isQuarterly) {
		this.isQuarterly = isQuarterly;
	}

	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}

	public int getQuarter() {
		return quarter;
	}

}
