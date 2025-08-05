package org.ace.accounting.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class VLdto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Date settlementDate;
	private String voucherNo;
	private String acCode;
	private String acName;
	private String narration;
	private String curCode;
	private BigDecimal exchangeRate;
	private BigDecimal debit;
	private BigDecimal credit;
	private int srNo;

	public VLdto(Date settlementDate, String voucherNo, String acCode, String acName, String narration, String curCode, BigDecimal exchangeRate, BigDecimal debit,
			BigDecimal credit) {
		this.settlementDate = settlementDate;
		this.voucherNo = voucherNo;
		this.acCode = acCode;
		this.acName = acName;
		this.narration = narration;
		this.curCode = curCode;
		this.exchangeRate = exchangeRate;
		this.debit = debit;
		this.credit = credit;
	}

	public VLdto(BigDecimal debit, BigDecimal credit) {
		super();
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

	public String getCurCode() {
		return curCode;
	}

	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
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

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getNarration() {
		return narration;
	}

	public int getSrNo() {
		return srNo;
	}

	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}

}
