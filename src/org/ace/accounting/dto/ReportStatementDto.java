package org.ace.accounting.dto;

import java.math.BigDecimal;

import org.ace.accounting.system.chartaccount.AccountType;
import org.ace.accounting.system.formatfile.ColType;

public class ReportStatementDto {
	private int lno;
	private int rlno;
	private String acCode;
	private String rAcCode;
	private String dCode;
	private String desp;
	private String rDesp;
	private boolean isShowHide;
	private String amountTotal;
	private String status;
	private BigDecimal oBal = BigDecimal.ZERO;
	private BigDecimal amt = BigDecimal.ZERO;
	private BigDecimal rAmt = BigDecimal.ZERO;
	private BigDecimal cBal = BigDecimal.ZERO;
	private BigDecimal bfAmount = BigDecimal.ZERO;
	private BigDecimal total = BigDecimal.ZERO;
	private ColType colType;
	private AccountType acType;
	private AccountType racType;

	public ReportStatementDto() {
	}

	public boolean isCbalObalAmtZero() {
		if (this.cBal.equals(BigDecimal.ZERO) && this.oBal.equals(BigDecimal.ZERO) && this.amt.equals(BigDecimal.ZERO))
			return true;
		else
			return false;

	}

	public int getLno() {
		return lno;
	}

	public void setLno(int lno) {
		this.lno = lno;
	}

	public int getRlno() {
		return rlno;
	}

	public void setRlno(int rlno) {
		this.rlno = rlno;
	}

	public String getAcCode() {
		return acCode;
	}

	public void setAcCode(String acCode) {
		this.acCode = acCode;
	}

	public String getrAcCode() {
		return rAcCode;
	}

	public void setrAcCode(String rAcCode) {
		this.rAcCode = rAcCode;
	}

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public String getDesp() {
		return desp;
	}

	public void setDesp(String desp) {
		this.desp = desp;
	}

	public String getrDesp() {
		return rDesp;
	}

	public void setrDesp(String rDesp) {
		this.rDesp = rDesp;
	}

	public boolean isShowHide() {
		return isShowHide;
	}

	public void setShowHide(boolean isShowHide) {
		this.isShowHide = isShowHide;
	}

	public String getAmountTotal() {
		return amountTotal;
	}

	public void setAmountTotal(String amountTotal) {
		this.amountTotal = amountTotal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getoBal() {
		return oBal;
	}

	public void setoBal(BigDecimal oBal) {
		this.oBal = oBal;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public BigDecimal getrAmt() {
		return rAmt;
	}

	public void setrAmt(BigDecimal rAmt) {
		this.rAmt = rAmt;
	}

	public BigDecimal getcBal() {
		return cBal;
	}

	public void setcBal(BigDecimal cBal) {
		this.cBal = cBal;
	}

	public BigDecimal getBfAmount() {
		return bfAmount;
	}

	public void setBfAmount(BigDecimal bfAmount) {
		this.bfAmount = bfAmount;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public ColType getColType() {
		return colType;
	}

	public void setColType(ColType colType) {
		this.colType = colType;
	}

	public AccountType getAcType() {
		return acType;
	}

	public void setAcType(AccountType acType) {
		this.acType = acType;
	}

	public AccountType getRacType() {
		return racType;
	}

	public void setRacType(AccountType racType) {
		this.racType = racType;
	}

	
	@Override
	public String toString() {
		return lno + " , " + acType + " , " + racType + " , " + racType + " , " + rlno + " , " + acCode + " , " + rAcCode + " , " + dCode + " , " + desp + " , " + rDesp + " , "
				+ isShowHide + " , " + amountTotal + " , " + status + " , " + oBal + " , " + amt + " , " + rAmt + " , " + cBal + " , " + bfAmount + " , " + total + " , " + colType;
	}
}
