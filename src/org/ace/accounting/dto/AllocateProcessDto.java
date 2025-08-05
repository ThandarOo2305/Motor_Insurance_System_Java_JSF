package org.ace.accounting.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AllocateProcessDto {
	private String acCode;
	private String acType;
	private String currencyCode;
	private BigDecimal rate;
	private BigDecimal balance;
	private BigDecimal total;
	private Map<String, BigDecimal> departmentAmountMap;

	public AllocateProcessDto() {

	}

	public AllocateProcessDto(String acCode, String acType, String currencyCode, BigDecimal rate, BigDecimal total) {
		super();
		this.acCode = acCode;
		this.acType = acType;
		this.currencyCode = currencyCode;
		this.rate = rate;
		this.total = total;
	}

	public AllocateProcessDto(String acCode, BigDecimal balance, String acType, String currencyCode, BigDecimal rate) {
		super();
		this.acCode = acCode;
		this.balance = balance;
		this.acType = acType;
		this.currencyCode = currencyCode;
		this.rate = rate;
	}

	public String getAcCode() {
		return acCode;
	}

	public String getAcType() {
		return acType;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public BigDecimal getTotal() {
		return total;
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

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}
