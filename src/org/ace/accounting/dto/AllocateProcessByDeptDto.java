package org.ace.accounting.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AllocateProcessByDeptDto {
	private String hacCode;
	private String currencyCode;
	private String branchCode;
	private BigDecimal rate;
	private Map<String, BigDecimal> departmentAmountMap;

	public AllocateProcessByDeptDto() {
	}

	public AllocateProcessByDeptDto(String hacCode, String currencyCode, String branchCode, BigDecimal rate) {
		this.hacCode = hacCode;
		this.currencyCode = currencyCode;
		this.branchCode = branchCode;
		this.rate = rate;
	}

	public String getHacCode() {
		return hacCode;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public Map<String, BigDecimal> getDepartmentAmountMap() {
		return departmentAmountMap;
	}

	public void setDepartmentAmountMap(Map<String, BigDecimal> departmentAmountMap) {
		this.departmentAmountMap = departmentAmountMap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((branchCode == null) ? 0 : branchCode.hashCode());
		result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
		result = prime * result + ((departmentAmountMap == null) ? 0 : departmentAmountMap.hashCode());
		result = prime * result + ((hacCode == null) ? 0 : hacCode.hashCode());
		result = prime * result + ((rate == null) ? 0 : rate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AllocateProcessByDeptDto other = (AllocateProcessByDeptDto) obj;
		if (branchCode == null) {
			if (other.branchCode != null)
				return false;
		} else if (!branchCode.equals(other.branchCode))
			return false;
		if (currencyCode == null) {
			if (other.currencyCode != null)
				return false;
		} else if (!currencyCode.equals(other.currencyCode))
			return false;
		if (departmentAmountMap == null) {
			if (other.departmentAmountMap != null)
				return false;
		} else if (!departmentAmountMap.equals(other.departmentAmountMap))
			return false;
		if (hacCode == null) {
			if (other.hacCode != null)
				return false;
		} else if (!hacCode.equals(other.hacCode))
			return false;
		if (rate == null) {
			if (other.rate != null)
				return false;
		} else if (!rate.equals(other.rate))
			return false;
		return true;
	}

	public void putToMap(String dCode, BigDecimal amount) {
		if (departmentAmountMap == null) {
			departmentAmountMap = new HashMap<String, BigDecimal>();
		}
		departmentAmountMap.put(dCode, amount);
	}
}
