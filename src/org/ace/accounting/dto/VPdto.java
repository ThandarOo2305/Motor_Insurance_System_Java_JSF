package org.ace.accounting.dto;

import java.math.BigDecimal;
import java.util.Date;

public class VPdto {
	private Date settlementDate;
	private String voucherNo;
	private String acCode;
	private String acName;
	private String narration;
	private String currencyCode;
	private BigDecimal debit;
	private BigDecimal credit;

	public VPdto(Date settlementDate, String voucherNo, String acCode, String acName, String narration, String currencyCode, BigDecimal debit, BigDecimal credit) {
		super();
		this.settlementDate = settlementDate;
		this.voucherNo = voucherNo;
		this.acCode = acCode;
		this.acName = acName;
		this.narration = narration;
		this.currencyCode = currencyCode;
		this.debit = debit;
		this.credit = credit;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getAcCode() {
		return acCode;
	}

	public void setAcCode(String acCode) {
		this.acCode = acCode;
	}

	public String getAcName() {
		return acName;
	}

	public void setAcName(String acName) {
		this.acName = acName;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
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

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

}
