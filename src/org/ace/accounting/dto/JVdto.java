package org.ace.accounting.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.trantype.TranCode;
import org.ace.accounting.system.trantype.TranType;

public class JVdto {
	private Currency cur;

	private CurrencyChartOfAccount ccoa;

	private TranCode tranCode;

	private String chequeNo;

	private BigDecimal exchangeRate;

	private BigDecimal amount;

	private BigDecimal homeAmount;

	private String narration;

	private TranType tranType;

	private Branch branch;

	private Date settlementDate;

	// private String voucherNo;

	public JVdto() {
		homeAmount = BigDecimal.ZERO;
	}

	public CurrencyChartOfAccount getCcoa() {
		return ccoa;
	}

	public void setCcoa(CurrencyChartOfAccount ccoa) {
		this.ccoa = ccoa;
	}

	public Currency getCur() {
		return cur;
	}

	public void setCur(Currency cur) {
		this.cur = cur;
	}

	public TranCode getTranCode() {
		return tranCode;
	}

	public TranType getTranType() {
		return tranType;
	}

	public void setTranType(TranType tranType) {
		this.tranType = tranType;
	}

	public void setTranCode(TranCode tranCode) {
		this.tranCode = tranCode;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public void setHomeAmount(BigDecimal homeAmount) {
		this.homeAmount = homeAmount;
	}

	public BigDecimal getHomeAmount() {
		return homeAmount;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((branch == null) ? 0 : branch.hashCode());
		result = prime * result + ((ccoa == null) ? 0 : ccoa.hashCode());
		result = prime * result + ((chequeNo == null) ? 0 : chequeNo.hashCode());
		result = prime * result + ((cur == null) ? 0 : cur.hashCode());
		result = prime * result + ((exchangeRate == null) ? 0 : exchangeRate.hashCode());
		result = prime * result + ((homeAmount == null) ? 0 : homeAmount.hashCode());
		result = prime * result + ((narration == null) ? 0 : narration.hashCode());
		result = prime * result + ((settlementDate == null) ? 0 : settlementDate.hashCode());
		result = prime * result + ((tranCode == null) ? 0 : tranCode.hashCode());
		result = prime * result + ((tranType == null) ? 0 : tranType.hashCode());
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
		JVdto other = (JVdto) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (branch == null) {
			if (other.branch != null)
				return false;
		} else if (!branch.equals(other.branch))
			return false;
		if (ccoa == null) {
			if (other.ccoa != null)
				return false;
		} else if (!ccoa.equals(other.ccoa))
			return false;
		if (chequeNo == null) {
			if (other.chequeNo != null)
				return false;
		} else if (!chequeNo.equals(other.chequeNo))
			return false;
		if (cur == null) {
			if (other.cur != null)
				return false;
		} else if (!cur.equals(other.cur))
			return false;
		if (exchangeRate == null) {
			if (other.exchangeRate != null)
				return false;
		} else if (!exchangeRate.equals(other.exchangeRate))
			return false;
		if (homeAmount == null) {
			if (other.homeAmount != null)
				return false;
		} else if (!homeAmount.equals(other.homeAmount))
			return false;
		if (narration == null) {
			if (other.narration != null)
				return false;
		} else if (!narration.equals(other.narration))
			return false;
		if (settlementDate == null) {
			if (other.settlementDate != null)
				return false;
		} else if (!settlementDate.equals(other.settlementDate))
			return false;
		if (tranCode != other.tranCode)
			return false;
		if (tranType == null) {
			if (other.tranType != null)
				return false;
		} else if (!tranType.equals(other.tranType))
			return false;
		return true;
	}

}
