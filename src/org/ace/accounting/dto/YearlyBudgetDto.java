package org.ace.accounting.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class YearlyBudgetDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ccoaId;
	private String acCode;
	private String acName;
	private String currencyCode;
	private String dCode;
	private String branchCode;
	private BigDecimal bF;
	private BigDecimal oldBF;

	private String coaId;
	private String curId;
	private String branchId;
	private String departmentId;
	private boolean isEdit = false;

	public YearlyBudgetDto(String ccoaId, String acCode, String acName, String currencyCode, String dCode, String branchCode, BigDecimal bF) {
		super();
		this.ccoaId = ccoaId;
		this.acCode = acCode;
		this.acName = acName;
		this.currencyCode = currencyCode;
		this.dCode = dCode;
		this.branchCode = branchCode;
		this.bF = bF;
		this.oldBF = bF;
	}

	public YearlyBudgetDto(String ccoaId, BigDecimal bF) {
		super();
		this.ccoaId = ccoaId;
		this.bF = bF;
	}

	public void checkBF(BigDecimal bF) {

		if ((this.oldBF == null || this.oldBF.doubleValue() != bF.doubleValue()) && bF.doubleValue() != 0) {
			isEdit = true;
		} else {
			isEdit = false;
		}
	}

	public YearlyBudgetDto() {
		super();
	}

	public String getCcoaId() {
		return ccoaId;
	}

	public void setCcoaId(String ccoaId) {
		this.ccoaId = ccoaId;
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

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public BigDecimal getbF() {
		return bF;
	}

	public void setbF(BigDecimal bF) {
		this.bF = bF;
	}

	public BigDecimal getOldBF() {
		return oldBF;
	}

	public void setOldBF(BigDecimal oldBF) {
		this.oldBF = oldBF;
	}

	public String getCoaId() {
		return coaId;
	}

	public void setCoaId(String coaId) {
		this.coaId = coaId;
	}

	public String getCurId() {
		return curId;
	}

	public void setCurId(String curId) {
		this.curId = curId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

}
