package org.ace.accounting.dto;

import java.io.Serializable;
import java.util.List;

import org.ace.accounting.system.chartaccount.ChartOfAccount;

public class AccountLedgerGroupDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private ChartOfAccount coa;
	private List <AccountLedgerDto> accountLedgerList;
	public ChartOfAccount getCoa() {
		return coa;
	}
	public void setCoa(ChartOfAccount coa) {
		this.coa = coa;
	}
	public List<AccountLedgerDto> getAccountLedgerList() {
		return accountLedgerList;
	}
	public void setAccountLedgerList(List<AccountLedgerDto> accountLedgerList) {
		this.accountLedgerList = accountLedgerList;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountLedgerList == null) ? 0 : accountLedgerList.hashCode());
		result = prime * result + ((coa == null) ? 0 : coa.hashCode());
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
		AccountLedgerGroupDto other = (AccountLedgerGroupDto) obj;
		if (accountLedgerList == null) {
			if (other.accountLedgerList != null)
				return false;
		} else if (!accountLedgerList.equals(other.accountLedgerList))
			return false;
		if (coa == null) {
			if (other.coa != null)
				return false;
		} else if (!coa.equals(other.coa))
			return false;
		return true;
	}
	
	

}
