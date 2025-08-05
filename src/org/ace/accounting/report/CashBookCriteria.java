package org.ace.accounting.report;

import java.util.Date;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;

public class CashBookCriteria {
	private CurrencyType currencyType;
	private Branch branch;
	private Currency currency;
	private Date fromDate;
	private Date toDate;
	private boolean homeCurrency;
	private boolean homeCurrencyConverted;

	public CashBookCriteria() {

	}

	public CurrencyType getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(CurrencyType currencyType) {
		this.currencyType = currencyType;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public boolean isHomeCurrency() {
		return homeCurrency;
	}

	public void setHomeCurrency(boolean homeCurrency) {
		this.homeCurrency = homeCurrency;
	}

	public boolean isHomeCurrencyConverted() {
		return homeCurrencyConverted;
	}

	public void setHomeCurrencyConverted(boolean homeCurrencyConverted) {
		this.homeCurrencyConverted = homeCurrencyConverted;
	}
	
	

}
