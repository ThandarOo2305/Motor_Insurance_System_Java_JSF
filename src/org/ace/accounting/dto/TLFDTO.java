package org.ace.accounting.dto;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.system.chartaccount.AccountCodeType;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.department.Department;
import org.ace.accounting.system.trantype.TranType;

public class TLFDTO {
	
	private String customerId;

	private CurrencyChartOfAccount ccoa;

	private BigDecimal homeAmount;

	private BigDecimal localAmount;

	private Currency currency;

	private String chequeNo;

	private String bankId;

	private String purchaseOrderId;

	private String loanId;

	private String narration;

	private TranType tranType;

	private Department department;

	private String orgnTLFID;

	private BigDecimal rate;

	private Date settlementDate;

	private boolean reverse;

	private String referenceNo;

	private String referenceType;

	private String eNo;

	private boolean paid;

	private String tlfNo;

	private boolean clearing;

	private boolean isRenewal;
	
	private BigDecimal homeExchangeRate;
	private String createdUserId;
	private CurrencyType currencyType;
	private boolean homeCurrency;
	private boolean homeCurrencyConverted;
	private boolean isGroup;
	private AccountCodeType accountCodetype;
	
	public TLFDTO() {
	}
	
	
	
	public TLFDTO(CurrencyChartOfAccount ccoa,BigDecimal rate, BigDecimal homeAmount, BigDecimal localAmount) {
		super();
		this.ccoa = ccoa;
		this.rate = rate;
		this.homeAmount = homeAmount;
		this.localAmount = localAmount;
	
	}
	
	
	public String getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(String createdUserId) {
		this.createdUserId = createdUserId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public CurrencyChartOfAccount getCcoa() {
		return ccoa;
	}
	public void setCcoa(CurrencyChartOfAccount ccoa) {
		this.ccoa = ccoa;
	}
	public BigDecimal getHomeAmount() {
		return homeAmount;
	}
	public void setHomeAmount(BigDecimal homeAmount) {
		this.homeAmount = homeAmount;
	}
	public BigDecimal getLocalAmount() {
		return localAmount;
	}
	public void setLocalAmount(BigDecimal localAmount) {
		this.localAmount = localAmount;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public String getChequeNo() {
		return chequeNo;
	}
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getPurchaseOrderId() {
		return purchaseOrderId;
	}
	public void setPurchaseOrderId(String purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}
	public String getLoanId() {
		return loanId;
	}
	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}
	public String getNarration() {
		return narration;
	}
	public void setNarration(String narration) {
		this.narration = narration;
	}
	public TranType getTranType() {
		return tranType;
	}
	public void setTranType(TranType tranType) {
		this.tranType = tranType;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public String getOrgnTLFID() {
		return orgnTLFID;
	}
	public void setOrgnTLFID(String orgnTLFID) {
		this.orgnTLFID = orgnTLFID;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public Date getSettlementDate() {
		return settlementDate;
	}
	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}
	public boolean isReverse() {
		return reverse;
	}
	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	public String getReferenceType() {
		return referenceType;
	}
	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}
	public String geteNo() {
		return eNo;
	}
	public void seteNo(String eNo) {
		this.eNo = eNo;
	}
	public boolean isPaid() {
		return paid;
	}
	public void setPaid(boolean paid) {
		this.paid = paid;
	}
	public String getTlfNo() {
		return tlfNo;
	}
	public void setTlfNo(String tlfNo) {
		this.tlfNo = tlfNo;
	}
	public boolean isClearing() {
		return clearing;
	}
	public void setClearing(boolean clearing) {
		this.clearing = clearing;
	}
	public boolean isRenewal() {
		return isRenewal;
	}
	public void setRenewal(boolean isRenewal) {
		this.isRenewal = isRenewal;
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
	public boolean isHomeCurrencyConverted() {
		return homeCurrencyConverted;
	}
	public void setHomeCurrencyConverted(boolean homeCurrencyConverted) {
		this.homeCurrencyConverted = homeCurrencyConverted;
	}
	public boolean isGroup() {
		return isGroup;
	}
	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}
	public AccountCodeType getAccountCodetype() {
		return accountCodetype;
	}
	public void setAccountCodetype(AccountCodeType accountCodetype) {
		this.accountCodetype = accountCodetype;
	}
	public BigDecimal getHomeExchangeRate() {
		return homeExchangeRate;
	}

	public void setHomeExchangeRate(BigDecimal homeExchangeRate) {
		this.homeExchangeRate = homeExchangeRate;
	}
	

}
