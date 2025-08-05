package org.ace.accounting.dto;

import java.io.Serializable;
import java.util.Date;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;

public class LedgerDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CurrencyType currencyType;
	private boolean homeCurrency;
	private Currency currency;
	private boolean homeCurrencyConverted;
	private boolean allBranch;
	private Date startDate;
	private Date endDate;
	private Branch branch;
	// private ChartOfAccount coa;
	private boolean groupAccount;
	private boolean admin;
	private boolean isBeforeBudgetEnd;
	private boolean isBeforeDisabled;

	public LedgerDto() {
		super();
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

	public boolean isHomeCurrency() {
		return homeCurrency;
	}

	public void setHomeCurrency(boolean homeCurrency) {
		this.homeCurrency = homeCurrency;
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

	public boolean isAllBranch() {
		return allBranch;
	}

	public void setAllBranch(boolean allBranch) {
		this.allBranch = allBranch;
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

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	// public ChartOfAccount getCoa() {
	// return coa;
	// }
	//
	// public void setCoa(ChartOfAccount coa) {
	// this.coa = coa;
	// }

	public boolean isGroupAccount() {
		return groupAccount;
	}

	public void setGroupAccount(boolean groupAccount) {
		this.groupAccount = groupAccount;
	}

	public boolean isBeforeBudgetEnd() {
		return isBeforeBudgetEnd;
	}

	public void setBeforeBudgetEnd(boolean isBeforeBudgetEnd) {
		this.isBeforeBudgetEnd = isBeforeBudgetEnd;
	}

	public boolean isBeforeDisabled() {
		return isBeforeDisabled;
	}

	public void setBeforeDisabled(boolean isBeforeDisabled) {
		this.isBeforeDisabled = isBeforeDisabled;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (admin ? 1231 : 1237);
		result = prime * result + (allBranch ? 1231 : 1237);
		result = prime * result + ((branch == null) ? 0 : branch.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((currencyType == null) ? 0 : currencyType.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + (groupAccount ? 1231 : 1237);
		result = prime * result + (homeCurrency ? 1231 : 1237);
		result = prime * result + (homeCurrencyConverted ? 1231 : 1237);
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LedgerDto other = (LedgerDto) obj;
		if (admin != other.admin)
			return false;
		if (allBranch != other.allBranch)
			return false;
		if (branch == null) {
			if (other.branch != null)
				return false;
		} else if (!branch.equals(other.branch))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (currencyType != other.currencyType)
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (groupAccount != other.groupAccount)
			return false;
		if (homeCurrency != other.homeCurrency)
			return false;
		if (homeCurrencyConverted != other.homeCurrencyConverted)
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}

}
