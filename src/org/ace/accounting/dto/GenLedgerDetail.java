package org.ace.accounting.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class GenLedgerDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal debit;
	private BigDecimal credit;
	private BigDecimal dblBalance;
	private Date settlementDate;
	private int srNo;

	public GenLedgerDetail(BigDecimal debit, BigDecimal credit, BigDecimal dblBalance, Date settlementDate, int srNo) {
		this.debit = debit;
		this.credit = credit;
		this.dblBalance = dblBalance;
		this.settlementDate = settlementDate;
		this.srNo = srNo;
	}

	public GenLedgerDetail() {
		super();
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public BigDecimal getDblBalance() {
		return dblBalance;
	}

	public void setDblBalance(BigDecimal dblBalance) {
		this.dblBalance = dblBalance;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public int getSrNo() {
		return srNo;
	}

	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((credit == null) ? 0 : credit.hashCode());
		result = prime * result + ((dblBalance == null) ? 0 : dblBalance.hashCode());
		result = prime * result + ((debit == null) ? 0 : debit.hashCode());
		result = prime * result + ((settlementDate == null) ? 0 : settlementDate.hashCode());
		result = prime * result + srNo;
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
		GenLedgerDetail other = (GenLedgerDetail) obj;
		if (credit == null) {
			if (other.credit != null)
				return false;
		} else if (!credit.equals(other.credit))
			return false;
		if (dblBalance == null) {
			if (other.dblBalance != null)
				return false;
		} else if (!dblBalance.equals(other.dblBalance))
			return false;
		if (debit == null) {
			if (other.debit != null)
				return false;
		} else if (!debit.equals(other.debit))
			return false;
		if (settlementDate == null) {
			if (other.settlementDate != null)
				return false;
		} else if (!settlementDate.equals(other.settlementDate))
			return false;
		if (srNo != other.srNo)
			return false;
		return true;
	}

}
