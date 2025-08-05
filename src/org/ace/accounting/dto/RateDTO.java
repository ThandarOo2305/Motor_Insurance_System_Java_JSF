package org.ace.accounting.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.ace.accounting.system.chartaccount.AccountType;
import org.ace.accounting.system.chartaccount.ChartOfAccount;

public class RateDTO {
	private String accCode;

	private String accName;

	private BigDecimal homeDebitAmount;
	private BigDecimal homeCreditAmount;

	private BigDecimal localDebitAmount;
	private BigDecimal localCreditAmount;

	private double currencyRate;

	private BigDecimal homeAmount;

	private BigDecimal monthlyAmount;

	private BigDecimal difference;

	private int reportMonth;

	private String currencyId;

	private String branchId;

	private String currencyCode;

	private String branchName;

	private AccountType accountType;

	private List<ChartOfAccount> coaList;

	public RateDTO() {
		super();
	}

	public RateDTO(RateDTO temp) {
		accCode = temp.getAccCode();
		accName = temp.getAccName();
		homeDebitAmount = temp.getHomeDebitAmount();
		homeCreditAmount = temp.getHomeCreditAmount();
		localDebitAmount = temp.getLocalDebitAmount();
		localCreditAmount = temp.getLocalCreditAmount();
		currencyRate = temp.getCurrencyRate();
		homeAmount = temp.getHomeAmount();
		monthlyAmount = temp.getMonthlyAmount();
		difference = temp.getDifference();
		reportMonth = temp.getReportMonth();
		currencyId = temp.getCurrencyId();
		branchId = temp.getBranchId();
		currencyCode = temp.getCurrencyCode();
		branchName = temp.getBranchName();
		accountType = temp.getAccountType();
		coaList = temp.getCoaList();
	}

	public RateDTO(String accCode, String accName, BigDecimal homeDebitAmount, BigDecimal homeCreditAmount,
			BigDecimal localDebitAmount, BigDecimal localCreditAmount, double currencyRate, String currencyCode,
			String branchName, AccountType accountType) {

		super();
		this.accCode = accCode;
		this.accName = accName;
		this.homeDebitAmount = homeDebitAmount;
		this.homeCreditAmount = homeCreditAmount;
		this.localDebitAmount = localDebitAmount;
		this.localCreditAmount = localCreditAmount;
		this.currencyRate = currencyRate;
		this.currencyCode = currencyCode;
		this.branchName = branchName;
		this.accountType = accountType;
	}

	public BigDecimal getMonthlyAmount() {
		return monthlyAmount;
	}

	public void setMonthlyAmount(BigDecimal monthlyAmount) {
		this.monthlyAmount = monthlyAmount;
	}

	public BigDecimal getDifference() {
		return difference;
	}

	public void setDifference(BigDecimal difference) {
		this.difference = difference;
	}

	public int getReportMonth() {
		return reportMonth;
	}

	public void setReportMonth(int reportMonth) {
		this.reportMonth = reportMonth;
	}

	public List<ChartOfAccount> getCoaList() {
		if (coaList == null) {
			coaList = new ArrayList<ChartOfAccount>();
		}
		return coaList;
	}

	public void setCoaList(List<ChartOfAccount> coaList) {
		this.coaList = coaList;
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

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getAccCode() {
		return accCode;
	}

	public void setAccCode(String accCode) {
		this.accCode = accCode;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public BigDecimal getHomeDebitAmount() {
		return homeDebitAmount;
	}

	public void setHomeDebitAmount(BigDecimal homeDebitAmount) {
		this.homeDebitAmount = homeDebitAmount;
	}

	public BigDecimal getHomeCreditAmount() {
		return homeCreditAmount;
	}

	public void setHomeCreditAmount(BigDecimal homeCreditAmount) {
		this.homeCreditAmount = homeCreditAmount;
	}

	public BigDecimal getLocalDebitAmount() {
		return localDebitAmount;
	}

	public void setLocalDebitAmount(BigDecimal localDebitAmount) {
		this.localDebitAmount = localDebitAmount;
	}

	public BigDecimal getLocalCreditAmount() {
		return localCreditAmount;
	}

	public void setLocalCreditAmount(BigDecimal localCreditAmount) {
		this.localCreditAmount = localCreditAmount;
	}

	public double getCurrencyRate() {
		return currencyRate;
	}

	public void setCurrencyRate(double currencyRate) {
		this.currencyRate = currencyRate;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public BigDecimal getHomeAmount() {
		return homeAmount;
	}

	public void setHomeAmount(BigDecimal homeAmount) {
		this.homeAmount = homeAmount;
	}

}
