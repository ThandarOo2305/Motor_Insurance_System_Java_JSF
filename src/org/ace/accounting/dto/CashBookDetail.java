package org.ace.accounting.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.ace.accounting.system.chartaccount.AccountType;

public class CashBookDetail {
	private Date settlementDate;
	private String contraCode;
	private String contraName;
	private String narration;
	private AccountType acType;
	private BigDecimal debit;
	private BigDecimal credit;
	private BigDecimal balance;

	public CashBookDetail(Date settlementDate, String contraCode, String contraName, String narration, AccountType acType, BigDecimal debit, BigDecimal credit) {
		this.settlementDate = settlementDate;
		this.contraCode = contraCode;
		this.contraName = contraName;
		this.narration = narration;
		this.acType = acType;
		this.debit = debit;
		this.credit = credit;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public String getContraCode() {
		return contraCode;
	}

	public String getContraName() {
		return contraName;
	}

	public String getNarration() {
		return narration;
	}

	public AccountType getAcType() {
		return acType;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public BigDecimal getBalance() {
		return balance == null ? new BigDecimal(0) : balance;
	}

}
