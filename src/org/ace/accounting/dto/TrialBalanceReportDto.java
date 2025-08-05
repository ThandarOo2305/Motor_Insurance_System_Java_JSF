package org.ace.accounting.dto;

import java.math.BigDecimal;

import org.ace.accounting.system.chartaccount.AccountType;

public class TrialBalanceReportDto {
	private String acode;
	private String acname;
	private AccountType acType;
	private BigDecimal mDebit;
	private BigDecimal mCredit;
	private BigDecimal debit;
	private BigDecimal credit;

	public TrialBalanceReportDto() {
	}

	public String getAcode() {
		return acode;
	}

	public void setAcode(String acode) {
		this.acode = acode;
	}

	public String getAcname() {
		return acname;
	}

	public void setAcname(String acname) {
		this.acname = acname;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public AccountType getAcType() {
		return acType;
	}

	public void setAcType(AccountType acType) {
		this.acType = acType;
	}

	public BigDecimal getmCredit() {
		return mCredit;
	}

	public void setmCredit(BigDecimal mCredit) {
		this.mCredit = mCredit;
	}

	public BigDecimal getmDebit() {
		return mDebit;
	}

	public void setmDebit(BigDecimal mDebit) {
		this.mDebit = mDebit;
	}

}
