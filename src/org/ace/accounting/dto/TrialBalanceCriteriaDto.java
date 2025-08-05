package org.ace.accounting.dto;

import java.io.Serializable;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.report.TrialBalanceAccountType;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;

public class TrialBalanceCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CurrencyType currencyType;
	private Currency currency;
	private boolean homeCurrencyConverted;
	private Branch branch;
	private TrialBalanceAccountType accountType;
	private int requiredYear;
	private int requiredMonth;
	private boolean isGroup;
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

	public boolean isHomeCurrencyConverted() {
		return homeCurrencyConverted;
	}

	public void setHomeCurrencyConverted(boolean homeCurrencyConverted) {
		this.homeCurrencyConverted = homeCurrencyConverted;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public TrialBalanceAccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(TrialBalanceAccountType accountType) {
		this.accountType = accountType;
	}

	public int getRequiredYear() {
		return requiredYear;
	}

	public void setRequiredYear(int requiredYear) {
		this.requiredYear = requiredYear;
	}

	public int getRequiredMonth() {
		return requiredMonth;
	}

	public void setRequiredMonth(int requiredMonth) {
		this.requiredMonth = requiredMonth;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	public boolean isGroup() {
		return isGroup;
	}

	public String getBudgetYear() {
		return budgetYear;
	}

	public void setBudgetYear(String budgetYear) {
		this.budgetYear = budgetYear;
	}

}
