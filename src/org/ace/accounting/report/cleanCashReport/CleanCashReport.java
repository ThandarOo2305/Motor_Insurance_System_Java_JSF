package org.ace.accounting.report.cleanCashReport;

import java.math.BigDecimal;

/***************************************************************************************
 * @Date 2016-04-05
 * @author PPA
 * @Version 1.0
 * @Purpose This class serves as Clean Cash Report Object
 * 
 ***************************************************************************************/

public class CleanCashReport {

	// Account Code.
	private String aCode;
	// Account Name.
	private String acName;
	// Amount for Debit Field.
	private BigDecimal debit;
	// Amount for Credit Field.
	private BigDecimal credit;

	public CleanCashReport() {
	}

	// Constructor with parameters.
	public CleanCashReport(String aCode, String acName, BigDecimal debit, BigDecimal credit) {
		this.aCode = aCode;
		this.acName = acName;
		this.debit = debit;
		this.credit = credit;
	}

	// Getter.
	public String getaCode() {
		return aCode;
	}

	// Getter.
	public String getAcName() {
		return acName;
	}

	// Getter.
	public BigDecimal getDebit() {
		return debit;
	}

	// Getter.
	public BigDecimal getCredit() {
		return credit;
	}

	// Setter.
	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	// Setter.
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

}
