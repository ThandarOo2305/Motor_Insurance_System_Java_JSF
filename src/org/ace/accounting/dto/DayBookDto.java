package org.ace.accounting.dto;

import java.io.Serializable;
import java.util.Date;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;

/**
 * Entity representing the DayBook's Report
 * 
 * @author Aung
 * @since 1.0.0
 * @date 2016/03/30
 * 
 */
public class DayBookDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date requiredDate;
	boolean allBranch;
	private Branch branch;
	private boolean homeCurrency;
	private boolean homeCurrencyConverted;
	private Currency currency;
	private boolean admin;
	private CurrencyType currencyType;

	public Date getRequiredDate() {
		return requiredDate;
	}

	public void setRequiredDate(Date requiredDate) {
		this.requiredDate = requiredDate;
	}

	public boolean isAllBranch() {
		return allBranch;
	}

	public void setAllBranch(boolean allBranch) {
		this.allBranch = allBranch;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
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

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public CurrencyType getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(CurrencyType currencyType) {
		this.currencyType = currencyType;
	}

}
