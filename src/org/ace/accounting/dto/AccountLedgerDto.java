package org.ace.accounting.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.ace.accounting.system.chartaccount.AccountType;
import org.ace.accounting.system.trantype.TranCode;

public class AccountLedgerDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private ChartOfAccount coa;

	private String coaId;
	private AccountType acType;

	private String currencyId;
	private String branchId;
	private TranCode tranCode;

	private BigDecimal debit;
	private BigDecimal credit;
	private BigDecimal homeDebit;
	private BigDecimal homeCredit;
	// private Currency currency;
	// private Branch branch;
	private String narration;
	private BigDecimal dblBalance;
	private Date settlementDate;
	private int srNo;
	private String eNo;
	// private TranType tranType;

	public AccountLedgerDto() {
		super();
	}

	public AccountLedgerDto(String coaId, AccountType acType, BigDecimal debit, BigDecimal credit, String narration, BigDecimal dblBalance, Date settlementDate, int srNo) {
		super();
		this.coaId = coaId;
		this.acType = acType;

		this.debit = debit;
		this.credit = credit;
		this.narration = narration;
		this.dblBalance = dblBalance;
		this.settlementDate = settlementDate;
		this.srNo = srNo;
	}

	// customized to get debit, credit, homeDebit, homeCredit according to
	// TransType's TranCode
	public AccountLedgerDto(String coaId, AccountType acType, BigDecimal debit, BigDecimal credit, BigDecimal homeDebit, BigDecimal homeCredit, String narration, String currencyId,
			String branchId, TranCode tranCode) {
		super();
		this.coaId = coaId;
		this.acType = acType;
		this.currencyId = currencyId;
		this.branchId = branchId;
		this.narration = narration;
		this.tranCode = tranCode;
		this.debit = debit;
		this.credit = credit;
		this.homeDebit = homeDebit;
		this.homeCredit = homeCredit;
		if (!(tranCode.equals(TranCode.CSCREDIT) || tranCode.equals(TranCode.TRCREDIT))) {
			this.credit = BigDecimal.ZERO;
			this.homeCredit = BigDecimal.ZERO;
		}
		if (!(tranCode.equals(TranCode.CSDEBIT) || tranCode.equals(TranCode.TRDEBIT))) {
			this.debit = BigDecimal.ZERO;
			this.homeDebit = BigDecimal.ZERO;

		}
		
	}

	// customized to get debit, credit, homeDebit, homeCredit according to
	// TransType's TranCode
	public AccountLedgerDto(String coaId, AccountType acType, BigDecimal debit, BigDecimal credit, BigDecimal homeDebit, BigDecimal homeCredit, String narration,
			Date settlementDate, String currencyId, String branchId, TranCode tranCode, String eNo) {
		super();
		this.coaId = coaId;
		this.acType = acType;
		this.debit = debit;
		this.credit = credit;
		this.homeDebit = homeDebit;
		this.homeCredit = homeCredit;
		this.currencyId = currencyId;
		this.branchId = branchId;
		this.narration = narration;
		this.settlementDate = settlementDate;
		this.tranCode = tranCode;
		if (!(tranCode.equals(TranCode.CSCREDIT) || tranCode.equals(TranCode.TRCREDIT))) {
			this.credit = BigDecimal.ZERO;
			this.homeCredit = BigDecimal.ZERO;
		}
		if (!(tranCode.equals(TranCode.CSDEBIT) || tranCode.equals(TranCode.TRDEBIT))) {
			this.debit = BigDecimal.ZERO;
			this.homeDebit = BigDecimal.ZERO;
		}
		this.eNo = eNo;
	}

	public AccountLedgerDto(String coaId, AccountType acType, BigDecimal debit, BigDecimal credit, BigDecimal homeDebit, BigDecimal homeCredit, String narration,
			Date settlementDate, String eNo) {
		super();
		this.coaId = coaId;
		this.acType = acType;
		this.debit = debit;
		this.credit = credit;
		this.homeDebit = homeDebit;
		this.homeCredit = homeCredit;
		this.narration = narration;
		this.settlementDate = settlementDate;
		this.eNo = eNo;

	}

	public AccountLedgerDto(String coaId, AccountType acType, BigDecimal debit, BigDecimal credit, String narration, Date settlementDate, String eNo) {
		super();
		this.coaId = coaId;
		this.acType = acType;
		this.debit = debit;
		this.credit = credit;
		this.narration = narration;
		this.settlementDate = settlementDate;
		this.eNo = eNo;
	}

	public AccountLedgerDto(String coaId, AccountType acType, BigDecimal debit, BigDecimal credit, BigDecimal homeDebit, BigDecimal homeCredit) {
		super();
		this.coaId = coaId;
		this.acType = acType;
		this.debit = debit;
		this.credit = credit;
		this.homeDebit = homeDebit;
		this.homeCredit = homeCredit;
	}

	public AccountLedgerDto(String coaId, AccountType acType, BigDecimal debit, BigDecimal credit) {
		super();
		this.coaId = coaId;
		this.acType = acType;
		this.debit = debit;
		this.credit = credit;

	}

	public AccountLedgerDto(String coaId, AccountType acType, BigDecimal debit, BigDecimal credit, BigDecimal homeDebit, BigDecimal homeCredit, String narration) {
		super();
		this.coaId = coaId;
		this.acType = acType;
		this.debit = debit;
		this.credit = credit;
		this.homeDebit = homeDebit;
		this.homeCredit = homeCredit;
		this.narration = narration;
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

	public BigDecimal getHomeDebit() {
		return homeDebit;
	}

	public void setHomeDebit(BigDecimal homeDebit) {
		this.homeDebit = homeDebit;
	}

	public BigDecimal getHomeCredit() {
		return homeCredit;
	}

	public void setHomeCredit(BigDecimal homeCredit) {
		this.homeCredit = homeCredit;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public BigDecimal getDblBalance() {
		return dblBalance;
	}

	public void setDblBalance(BigDecimal dblBalance) {
		this.dblBalance = dblBalance;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public int getSrNo() {
		return srNo;
	}

	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}

	public String getCoaId() {
		return coaId;
	}

	public void setCoaId(String coaId) {
		this.coaId = coaId;
	}

	public AccountType getAcType() {
		return acType;
	}

	public void setAcType(AccountType acType) {
		this.acType = acType;
	}

	public String getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public TranCode getTranCode() {
		return tranCode;
	}

	public void setTranCode(TranCode tranCode) {
		this.tranCode = tranCode;
	}

	public String geteNo() {
		return eNo;
	}

	public void seteNo(String eNo) {
		this.eNo = eNo;
	}

}
