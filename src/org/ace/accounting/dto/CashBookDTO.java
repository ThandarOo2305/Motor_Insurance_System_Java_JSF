package org.ace.accounting.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class CashBookDTO {
	private Date cashBookDate;
	private String acCode;
	private String acName;
	private BigDecimal openingBalance;
	private List<CashBookDetail> details;

	public CashBookDTO(Date cashBookDate, String acCode, String acName, BigDecimal openingBalance) {
		this.cashBookDate = cashBookDate;
		this.acName = acName;
		this.openingBalance = openingBalance;
		this.acCode = acCode;
	}

	public Date getCashBookDate() {
		return cashBookDate;
	}

	public String getAcCode() {
		return acCode;
	}

	public String getAcName() {
		return acName;
	}

	public BigDecimal getOpeningBalance() {
		return openingBalance == null ? BigDecimal.ZERO : openingBalance;
	}

	public void addOpeningBalance(BigDecimal openingBalance) {
		if (openingBalance != null) {
			//to CHECK
			//this.openingBalance=this.openingBalance.add(openingBalance);
			this.openingBalance.add(openingBalance);
		}
	}

	public List<CashBookDetail> getDetails() {
		return details;
	}

	public void setDetails(List<CashBookDetail> details) {
		this.details = details;
	}

}
