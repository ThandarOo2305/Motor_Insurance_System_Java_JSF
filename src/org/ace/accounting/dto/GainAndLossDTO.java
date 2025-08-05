package org.ace.accounting.dto;

import java.math.BigDecimal;

public class GainAndLossDTO {

	private String acCode;
	private String acName;
	private String currencyCode;
	private String branchName;
	private BigDecimal monthlyAmount;
	private BigDecimal homeAmount;
	private BigDecimal differenceAmount;

	private String currencyId;

	private String branchId;

	private int reportMonth;
	private int reportYear;

	public GainAndLossDTO() {

	}

	public GainAndLossDTO(String acCode, String acName, String currencyCode, String branchName,
			BigDecimal monthlyAmount, BigDecimal homeAmount, BigDecimal differenceAmount) {
		super();
		this.acCode = acCode;
		this.acName = acName;
		this.currencyCode = currencyCode;
		this.branchName = branchName;
		this.monthlyAmount = monthlyAmount;
		this.homeAmount = homeAmount;
		this.differenceAmount = differenceAmount;
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

	public BigDecimal getMonthlyAmount() {
		return monthlyAmount;
	}

	public void setMonthlyAmount(BigDecimal monthlyAmount) {
		this.monthlyAmount = monthlyAmount;
	}

	public BigDecimal getHomeAmount() {
		return homeAmount;
	}

	public void setHomeAmount(BigDecimal homeAmount) {
		this.homeAmount = homeAmount;
	}

	public BigDecimal getDifferenceAmount() {
		return differenceAmount;
	}

	public void setDifferenceAmount(BigDecimal differenceAmount) {
		this.differenceAmount = differenceAmount;
	}

	public int getReportMonth() {
		return reportMonth;
	}

	public void setReportMonth(int reportMonth) {
		this.reportMonth = reportMonth;
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

	public int getReportYear() {
		return reportYear;
	}

	public void setReportYear(int reportYear) {
		this.reportYear = reportYear;
	}

}
