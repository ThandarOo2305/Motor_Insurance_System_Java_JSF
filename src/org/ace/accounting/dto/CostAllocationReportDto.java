package org.ace.accounting.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CostAllocationReportDto {

	private String acCode;
	private String acName;
	private String acType;
	private String hacCode;
	private String hacName;
	private String currencyCode;
	private String branchCode;
	private BigDecimal rate;
	private BigDecimal total;
	private Map<String, BigDecimal> departmentAmountMap;

	public CostAllocationReportDto() {
	}

	public CostAllocationReportDto(String acCode, String acName, String acType, String hacCode, String hacName, String currencyCode, String branchCode, BigDecimal rate,
			BigDecimal total) {
		this.acCode = acCode;
		this.acName = acName;
		this.acType = acType;
		this.hacCode = hacCode;
		this.hacName = hacName;
		this.currencyCode = currencyCode;
		this.branchCode = branchCode;
		this.rate = rate;
		this.total = total;
	}

	public String getAcCode() {
		return acCode;
	}

	public void setAcCode(String acCode) {
		this.acCode = acCode;
	}

	public String getHacName() {
		return hacName;
	}

	public void setHacName(String hacName) {
		this.hacName = hacName;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Map<String, BigDecimal> getDepartmentAmountMap() {
		return departmentAmountMap == null ? new HashMap<>() : departmentAmountMap;
	}

	public void putToMap(String dCode, BigDecimal amount) {
		if (departmentAmountMap == null) {
			departmentAmountMap = new HashMap<String, BigDecimal>();
		}
		departmentAmountMap.put(dCode, amount);
	}

	public String getAcName() {
		return acName;
	}

	public void setAcName(String acName) {
		this.acName = acName;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}

	public String getHacCode() {
		return hacCode;
	}

	public void setHacCode(String hacCode) {
		this.hacCode = hacCode;
	}

}
