package org.ace.accounting.dto;

import java.util.ArrayList;
import java.util.List;

import org.ace.accounting.system.chartaccount.AccountCodeType;
import org.ace.accounting.system.chartaccount.AccountType;

public class CoaDialogCriteriaDto {
	private AccountCodeType accountCodeType;
	private List<AccountType> accountTypes;

	public CoaDialogCriteriaDto() {
		accountTypes = new ArrayList<AccountType>();
	}

	public void setAccountCodeType(AccountCodeType accountCodeType) {
		this.accountCodeType = accountCodeType;
	}

	public AccountCodeType getAccountCodeType() {
		return accountCodeType;
	}

	public void setAccountTypes(List<AccountType> accountTypes) {
		this.accountTypes = accountTypes;
	}

	public List<AccountType> getAccountTypes() {
		return accountTypes;
	}

	public void addAccountTypes(AccountType acType) {
		if (!accountTypes.contains(acType)) {
			accountTypes.add(acType);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountCodeType == null) ? 0 : accountCodeType.hashCode());
		result = prime * result + ((accountTypes == null) ? 0 : accountTypes.hashCode());
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
		CoaDialogCriteriaDto other = (CoaDialogCriteriaDto) obj;
		if (accountCodeType != other.accountCodeType)
			return false;
		if (accountTypes == null) {
			if (other.accountTypes != null)
				return false;
		} else if (!accountTypes.equals(other.accountTypes))
			return false;
		return true;
	}
}
