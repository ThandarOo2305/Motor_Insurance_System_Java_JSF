package org.ace.accounting.dto;

import java.math.BigDecimal;

import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.currency.Currency;

public class dailyPostingDto {
	private CurrencyChartOfAccount ccoa;
	private Currency currency;
	private ChartOfAccount headCOA;
	private ChartOfAccount groupCOA;
	private BigDecimal localAmount;
	private BigDecimal homeAmount;

	public dailyPostingDto(CurrencyChartOfAccount ccoa, Currency currency, ChartOfAccount headCOA, ChartOfAccount groupCOA, BigDecimal localAmount, BigDecimal homeAmount) {
		super();
		this.ccoa = ccoa;
		this.currency = currency;
		this.headCOA = headCOA;
		this.groupCOA = groupCOA;
		this.localAmount = localAmount;
		this.homeAmount = homeAmount;
	}

	public CurrencyChartOfAccount getCcoa() {
		return ccoa;
	}

	public void setCcoa(CurrencyChartOfAccount ccoa) {
		this.ccoa = ccoa;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public ChartOfAccount getHeadCOA() {
		return headCOA;
	}

	public void setHeadCOA(ChartOfAccount headCOA) {
		this.headCOA = headCOA;
	}

	public ChartOfAccount getGroupCOA() {
		return groupCOA;
	}

	public void setGroupCOA(ChartOfAccount groupCOA) {
		this.groupCOA = groupCOA;
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

}
