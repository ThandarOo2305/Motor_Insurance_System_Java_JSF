package org.ace.accounting.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.ace.accounting.system.chartaccount.AccountType;

public class CoaEntryDto {
	private Date settlementDate;
	private String acCode;
	private String acName;
	private String narration;
	private AccountType acType;
	private BigDecimal debit;
	private BigDecimal credit;
	private BigDecimal balance;

	public CoaEntryDto(Date settlementDate, String acCode, String acName, String narration, AccountType acType, BigDecimal debit, BigDecimal credit) {
		this.settlementDate = settlementDate;
		this.acCode = acCode;
		this.acName = acName;
		this.narration = narration;
		this.acType = acType;
		this.debit = debit;
		this.credit = credit;
	}

	public CoaEntryDto(Date settlementDate, String acCode, String acName, String narration, BigDecimal debit, BigDecimal credit, BigDecimal balance) {
		this.settlementDate = settlementDate;
		this.acCode = acCode;
		this.acName = acName;
		this.narration = narration;
		this.debit = debit;
		this.credit = credit;
		this.balance = balance;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public String getAcCode() {
		return acCode;
	}

	public void setAcCode(String acCode) {
		this.acCode = acCode;
	}

	public String getAcName() {
		return acName;
	}

	public void setAcName(String acName) {
		this.acName = acName;
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

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getNarration() {
		return narration;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public void setAcType(AccountType acType) {
		this.acType = acType;
	}

	public AccountType getAcType() {
		return acType;
	}
}
