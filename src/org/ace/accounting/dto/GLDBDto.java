package org.ace.accounting.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class GLDBDto implements Serializable {
	private static final long serialVersionUID = -942840791679626123L;
	private BigDecimal debit;
	private BigDecimal credit;
	private Date settlementDate;

	public GLDBDto(Date settlementDate, BigDecimal debit, BigDecimal credit) {
		this.settlementDate = settlementDate;
		this.debit = debit;
		this.credit = credit;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}
}
