/**
 * 
 */
package org.ace.accounting.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.faces.bean.ManagedProperty;

import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICoaService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.trantype.TranCode;
import org.ace.accounting.system.trantype.TranType;

/**
 * @author Aung
 *
 */
public class DayBookReportDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Used to retrieve the subAcName so called AcName, AcCode, AcType From COA
	 * as in previous version of VB's VW_DayBook
	 */
	private ChartOfAccount coa;

	/*
	 * Used to retrieve the Eno interms of ID as in previous version of VB's
	 * VW_DayBook
	 */
	private String eNo;

	private Currency currency;

	private String narration;

	private Branch branch;

	/*
	 * Used to retrieve the settlementDate instead of createdDate as in previous
	 * version of VB's VW_DayBook
	 */
	private Date settlementDate;

	private TranType tranType;

	/**
	 * Local Amount of TLF when tranType's transCode is equal to
	 * TranCode.CRDEBIT,TranCode.TRDEBIT,TranCode.CRCREDIT,TranCode.TRCREDIT
	 */
	private BigDecimal crDebit;
	private BigDecimal trDebit;
	private BigDecimal crCredit;
	private BigDecimal trCredit;

	/**
	 * Home Amount of TLF when tranType's transCode is equal to
	 * TranCode.CRDEBIT,TranCode.TRDEBIT,TranCode.CRCREDIT,TranCode.TRCREDIT
	 */
	private BigDecimal crDebit_Home;
	private BigDecimal trDebit_Home;
	private BigDecimal crCredit_Home;
	private BigDecimal trCredit_Home;

	private BigDecimal localAmount;
	private BigDecimal homeAmount;

	private ChartOfAccount groupCOA;
	private Boolean homeCurrency;
	private boolean homeCurrencyConverted;

	@ManagedProperty(value = "#{CoaService}")
	private ICoaService coaService;

	/*
	 * private BigDecimal debitAmount; private BigDecimal creditAmount; private
	 * BigDecimal debitHomeAmount; private BigDecimal creditHomeAmount;
	 * 
	 * private BigDecimal totalDebitAmount; private BigDecimal
	 * totalCreditAmount; private BigDecimal totalDebitHomeAmount; private
	 * BigDecimal totalCreditHomeAmount;
	 */

	public DayBookReportDto(ChartOfAccount coa, String eNo, Currency currency, String narration, Branch branch, Date settlementDate, TranType tranType, BigDecimal localAmount,
			BigDecimal homeAmount) {
		super();
		this.coa = coa;
		// this.homeCurrency = Boolean.valueOf(homeCurrency);
		this.eNo = eNo;
		this.currency = currency;
		this.narration = narration;
		this.branch = branch;
		this.settlementDate = settlementDate;
		this.tranType = tranType;
		this.localAmount = localAmount == null ? BigDecimal.ZERO : localAmount;
		this.homeAmount = homeAmount == null ? BigDecimal.ZERO : homeAmount;

		if (this.tranType.getTranCode().equals(TranCode.CSDEBIT)) {
			this.crDebit = this.localAmount;
			this.crDebit_Home = this.homeAmount;
		}

		if (this.tranType.getTranCode().equals(TranCode.CSCREDIT)) {
			this.crCredit = this.localAmount;
			this.crCredit_Home = this.homeAmount;
		}

		if (this.tranType.getTranCode().equals(TranCode.TRDEBIT)) {
			this.trDebit = this.localAmount;
			this.trDebit_Home = this.homeAmount;
		}

		if (this.tranType.getTranCode().equals(TranCode.TRCREDIT)) {
			this.trCredit = this.localAmount;
			this.trCredit_Home = this.homeAmount;
		}

	}

	public DayBookReportDto(ChartOfAccount coa, String eNo, Currency currency, String narration, Branch branch, Date settlementDate, TranType tranType, BigDecimal localAmount,
			BigDecimal homeAmount, String homeCurrency, Boolean homeCurrencyConverted) {
		super();

		this.coa = coa;
		this.eNo = eNo;
		this.currency = currency;
		this.narration = narration;
		this.branch = branch;
		this.settlementDate = settlementDate;
		this.tranType = tranType;
		this.localAmount = localAmount;
		this.homeAmount = homeAmount;

		if (homeCurrency.equals("true")) {
			if (this.tranType.getTranCode().equals(TranCode.CSDEBIT)) {
				this.crDebit = this.localAmount;
				this.crDebit_Home = this.homeAmount;
			}

			if (this.tranType.getTranCode().equals(TranCode.CSCREDIT)) {
				this.crCredit = this.localAmount;
				this.crCredit_Home = this.homeAmount;
			}

			if (this.tranType.getTranCode().equals(TranCode.TRDEBIT)) {
				this.trDebit = this.localAmount;
				this.trDebit_Home = this.homeAmount;
			}

			if (this.tranType.getTranCode().equals(TranCode.TRCREDIT)) {
				this.trCredit = this.localAmount;
				this.trCredit_Home = this.homeAmount;
			}
		}

		if (homeCurrencyConverted) {
			this.homeCurrencyConverted = homeCurrencyConverted;
		}

		if (this.tranType.getTranCode().equals(TranCode.CSDEBIT)) {
			this.crDebit = this.localAmount;
			this.crDebit_Home = this.homeAmount;
		}

		if (this.tranType.getTranCode().equals(TranCode.CSCREDIT)) {
			this.crCredit = this.localAmount;
			this.crCredit_Home = this.homeAmount;
		}

		if (this.tranType.getTranCode().equals(TranCode.TRDEBIT)) {
			this.trDebit = this.localAmount;
			this.trDebit_Home = this.homeAmount;
		}

		if (this.tranType.getTranCode().equals(TranCode.TRCREDIT)) {
			this.trCredit = this.localAmount;
			this.trCredit_Home = this.homeAmount;
		}

	}

	public DayBookReportDto(ChartOfAccount coa, String eNo, Currency currency, String narration, Branch branch, Date settlementDate, TranType tranType, BigDecimal crDebit,
			BigDecimal trDebit, BigDecimal crCredit, BigDecimal trCredit, BigDecimal crDebit_Home, BigDecimal trDebit_Home, BigDecimal crCredit_Home, BigDecimal trCredit_Home) {
		super();
		this.coa = coa;
		this.eNo = eNo;
		this.currency = currency;
		this.narration = narration;
		this.branch = branch;
		this.settlementDate = settlementDate;
		this.tranType = tranType;
		this.crDebit = crDebit;
		this.trDebit = trDebit;
		this.crCredit = crCredit;
		this.trCredit = trCredit;
		this.crDebit_Home = crDebit_Home;
		this.trDebit_Home = trDebit_Home;
		this.crCredit_Home = crCredit_Home;
		this.trCredit_Home = trCredit_Home;
	}

	public DayBookReportDto(BigDecimal crDebit, BigDecimal trDebit, BigDecimal crCredit, BigDecimal trCredit, BigDecimal crDebit_Home, BigDecimal trDebit_Home,
			BigDecimal crCredit_Home, BigDecimal trCredit_Home) {
		super();
		this.crDebit = crDebit;
		this.trDebit = trDebit;
		this.crCredit = crCredit;
		this.trCredit = trCredit;
		this.crDebit_Home = crDebit_Home;
		this.trDebit_Home = trDebit_Home;
		this.crCredit_Home = crCredit_Home;
		this.trCredit_Home = trCredit_Home;
	}

	public DayBookReportDto() {
		super();
	}

	public ChartOfAccount getCoa() {
		return coa;
	}

	public void setCoa(ChartOfAccount coa) {
		this.coa = coa;
	}

	public String geteNo() {
		return eNo;
	}

	public void seteNo(String eNo) {
		this.eNo = eNo;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public TranType getTranType() {
		return tranType;
	}

	public void setTranType(TranType tranType) {
		this.tranType = tranType;
	}

	public BigDecimal getCrDebit() {
		return crDebit;
	}

	public void setCrDebit(BigDecimal crDebit) {
		this.crDebit = crDebit;
	}

	public BigDecimal getTrDebit() {
		return trDebit;
	}

	public void setTrDebit(BigDecimal trDebit) {
		this.trDebit = trDebit;
	}

	public BigDecimal getCrCredit() {
		return crCredit;
	}

	public void setCrCredit(BigDecimal crCredit) {
		this.crCredit = crCredit;
	}

	public BigDecimal getTrCredit() {
		return trCredit;
	}

	public void setTrCredit(BigDecimal trCredit) {
		this.trCredit = trCredit;
	}

	public BigDecimal getCrDebit_Home() {
		return crDebit_Home;
	}

	public void setCrDebit_Home(BigDecimal crDebit_Home) {
		this.crDebit_Home = crDebit_Home;
	}

	public BigDecimal getTrDebit_Home() {
		return trDebit_Home;
	}

	public void setTrDebit_Home(BigDecimal trDebit_Home) {
		this.trDebit_Home = trDebit_Home;
	}

	public BigDecimal getCrCredit_Home() {
		return crCredit_Home;
	}

	public void setCrCredit_Home(BigDecimal crCredit_Home) {
		this.crCredit_Home = crCredit_Home;
	}

	public BigDecimal getTrCredit_Home() {
		return trCredit_Home;
	}

	public void setTrCredit_Home(BigDecimal trCredit_Home) {
		this.trCredit_Home = trCredit_Home;
	}

	public BigDecimal getLocalAmount() {
		return localAmount;
	}

	public void setLocalAmount(BigDecimal localAmount) {
		this.localAmount = localAmount;
	}

	public BigDecimal getHomeAmount() {
		return homeAmount;
	}

	public void setHomeAmount(BigDecimal homeAmount) {
		this.homeAmount = homeAmount;
	}

	public ChartOfAccount getGroupCOA() {
		return groupCOA;
	}

	public void setGroupCOA(ChartOfAccount groupCOA) {
		this.groupCOA = groupCOA;
	}

	public Boolean getHomeCurrency() {
		return homeCurrency;
	}

	public void setHomeCurrency(Boolean homeCurrency) {
		this.homeCurrency = homeCurrency;
	}




	public boolean isHomeCurrencyConverted() {
		return homeCurrencyConverted;
	}

	public void setHomeCurrencyConverted(boolean homeCurrencyConverted) {
		this.homeCurrencyConverted = homeCurrencyConverted;
	}

	public void setHomeCurrencyConverted(Boolean homeCurrencyConverted) {
		this.homeCurrencyConverted = homeCurrencyConverted;
	}

	@Override
	public String toString() {
		return "DayBookReportDto [coa=" + coa + ", eNo=" + eNo + ", currency=" + currency + ", narration=" + narration + ", branch=" + branch + ", settlementDate=" + settlementDate
				+ ", tranType=" + tranType + ", crDebit=" + crDebit + ", trDebit=" + trDebit + ", crCredit=" + crCredit + ", trCredit=" + trCredit + ", crDebit_Home="
				+ crDebit_Home + ", trDebit_Home=" + trDebit_Home + ", crCredit_Home=" + crCredit_Home + ", trCredit_Home=" + trCredit_Home + ", localAmount=" + localAmount
				+ ", homeAmount=" + homeAmount + "]";
	}

}
