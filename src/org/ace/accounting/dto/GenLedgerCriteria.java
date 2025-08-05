package org.ace.accounting.dto;

import java.util.Date;

import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;

public class GenLedgerCriteria {

	private Currency currency;
	private Branch branch;
	private Date startDate;
	private Date endDate;

	private boolean isAllBranch;
	private boolean isHomeCurrency;
	private boolean isHomeCurrencyConverted;

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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isAllBranch() {
		return isAllBranch;
	}

	public void setAllBranch(boolean isAllBranch) {
		this.isAllBranch = isAllBranch;
	}

	public boolean isHomeCurrency() {
		return isHomeCurrency;
	}

	public void setHomeCurrency(boolean isHomeCurrency) {
		this.isHomeCurrency = isHomeCurrency;
	}

	public boolean isHomeCurrencyConverted() {
		return isHomeCurrencyConverted;
	}

	public void setHomeCurrencyConverted(boolean isHomeCurrencyConverted) {
		this.isHomeCurrencyConverted = isHomeCurrencyConverted;
	}

}
