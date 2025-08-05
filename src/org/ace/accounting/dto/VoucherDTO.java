package org.ace.accounting.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.currency.Currency;

public class VoucherDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Currency currency;

	private CurrencyChartOfAccount ccoa;

	private BigDecimal homeExchangeRate;

	private BigDecimal homeAmount;

	private BigDecimal localAmount;

	private BigDecimal amount;

	private String narration;

	private Date settlementDate;

	private String createdUserId;

	private BigDecimal rate;

	// private String voucherNo;

	public VoucherDTO() {
		super();
	}

	public VoucherDTO(CurrencyChartOfAccount ccoa, BigDecimal rate, BigDecimal homeAmount, BigDecimal localAmount) {
		super();
		this.ccoa = ccoa;
		this.rate = rate;
		this.homeAmount = homeAmount;
		this.localAmount = localAmount;

	}

	public VoucherDTO(Currency currency, CurrencyChartOfAccount ccoa, BigDecimal homeExchangeRate, BigDecimal homeAmount, BigDecimal localAmount, String narration,
			Date settlementDate) {
		super();
		this.currency = currency;
		this.ccoa = ccoa;
		this.homeExchangeRate = homeExchangeRate;
		this.homeAmount = homeAmount;
		this.localAmount = localAmount;
		this.narration = narration;
		this.settlementDate = settlementDate;
	}

	public Currency getCurrency() {
		return currency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public CurrencyChartOfAccount getCcoa() {
		return ccoa;
	}

	public void setCcoa(CurrencyChartOfAccount ccoa) {
		this.ccoa = ccoa;
	}

	public BigDecimal getHomeExchangeRate() {
		return homeExchangeRate;
	}

	public void setHomeExchangeRate(BigDecimal homeExchangeRate) {
		this.homeExchangeRate = homeExchangeRate;
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

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public String getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(String createdUserId) {
		this.createdUserId = createdUserId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((ccoa == null) ? 0 : ccoa.hashCode());
		result = prime * result + ((createdUserId == null) ? 0 : createdUserId.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((homeAmount == null) ? 0 : homeAmount.hashCode());
		result = prime * result + ((homeExchangeRate == null) ? 0 : homeExchangeRate.hashCode());
		result = prime * result + ((localAmount == null) ? 0 : localAmount.hashCode());
		result = prime * result + ((narration == null) ? 0 : narration.hashCode());
		result = prime * result + ((rate == null) ? 0 : rate.hashCode());
		result = prime * result + ((settlementDate == null) ? 0 : settlementDate.hashCode());
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
		VoucherDTO other = (VoucherDTO) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (ccoa == null) {
			if (other.ccoa != null)
				return false;
		} else if (!ccoa.equals(other.ccoa))
			return false;
		if (createdUserId == null) {
			if (other.createdUserId != null)
				return false;
		} else if (!createdUserId.equals(other.createdUserId))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (homeAmount == null) {
			if (other.homeAmount != null)
				return false;
		} else if (!homeAmount.equals(other.homeAmount))
			return false;
		if (homeExchangeRate == null) {
			if (other.homeExchangeRate != null)
				return false;
		} else if (!homeExchangeRate.equals(other.homeExchangeRate))
			return false;
		if (localAmount == null) {
			if (other.localAmount != null)
				return false;
		} else if (!localAmount.equals(other.localAmount))
			return false;
		if (narration == null) {
			if (other.narration != null)
				return false;
		} else if (!narration.equals(other.narration))
			return false;
		if (rate == null) {
			if (other.rate != null)
				return false;
		} else if (!rate.equals(other.rate))
			return false;
		if (settlementDate == null) {
			if (other.settlementDate != null)
				return false;
		} else if (!settlementDate.equals(other.settlementDate))
			return false;
		return true;
	}

}
