package org.ace.accounting.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.ace.accounting.system.chartaccount.AccountType;
import org.ace.accounting.system.trantype.TranCode;

public class GenLedgerSummaryDTO {
	private String accountCode;
	private String accountName;
	private String narration;
	private AccountType accountType;
	private TranCode tranCode;
	private BigDecimal beginningBalance;
	private BigDecimal creditAmount;
	private BigDecimal debitAmount;
	private BigDecimal netBalance;
	private BigDecimal endingBalance;
	private Date settlementDate;

	public GenLedgerSummaryDTO(String accountCode, String accountName,String narration, AccountType accountType, BigDecimal beginningBalance, BigDecimal creditAmount, BigDecimal debitAmount,
			Date settlementDate) {
		this.accountCode = accountCode;
		this.accountName = accountName;
		this.narration = narration;
		this.accountType = accountType;
		this.beginningBalance = beginningBalance;
		this.creditAmount = creditAmount;
		this.debitAmount = debitAmount;
		this.settlementDate = settlementDate;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public BigDecimal getBeginningBalance() {
		return beginningBalance;
	}

	public BigDecimal getCreditAmount() {
		return creditAmount;
	}

	public BigDecimal getDebitAmount() {
		return debitAmount;
	}

	public BigDecimal getNetBalance() {
		return netBalance;
	}

	public BigDecimal getEndingBalance() {
		return endingBalance;
	}

	public TranCode getTranCode() {
		return tranCode;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setEndingBalance(BigDecimal endingBalance) {
		this.endingBalance = endingBalance;
	}

	public void setBeginningBalance(BigDecimal beginningBalance) {
		this.beginningBalance = beginningBalance;
	}

	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}

	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public void setNetBalance(BigDecimal netBalance) {
		this.netBalance = netBalance;
	}
	
}
