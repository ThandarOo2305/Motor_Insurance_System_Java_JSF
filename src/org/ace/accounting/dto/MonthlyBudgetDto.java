package org.ace.accounting.dto;

import org.ace.accounting.system.chartaccount.BfRate;

public class MonthlyBudgetDto {
	private String acCode;
	private String dCode;
	private String currencyCode;
	private BfRate bfRate;
	private BfRate oldBFRate;
	private String ccoaid;
	private String branchCode;

	private String curid;
	private String coaid;
	private String branchid;
	private String departmentid;
	private boolean isEdit = false;

	public MonthlyBudgetDto(String acCode, String dCode, String currencyCode, BfRate bfRate, String ccoaid, String branchCode) {
		super();
		this.acCode = acCode;
		this.dCode = dCode;
		this.currencyCode = currencyCode;
		this.bfRate = bfRate == null ? new BfRate() : bfRate;
		this.oldBFRate = new BfRate(getBfRate());
		this.ccoaid = ccoaid;
		this.branchCode = branchCode;
	}

	public MonthlyBudgetDto() {
		this.bfRate = new BfRate();
	}

	public String getAcCode() {
		return acCode;
	}

	public void setAcCode(String acCode) {
		this.acCode = acCode;
	}

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public BfRate getBfRate() {
		return bfRate;
	}

	public void checkBF(BfRate rate) {
		if (!this.oldBFRate.equals(rate)) {
			isEdit = true;
		} else {
			isEdit = false;
		}
	}

	public void setBfRate(BfRate bfRate) {
		this.bfRate = bfRate;
	}

	public void setCcoaid(String ccoaid) {
		this.ccoaid = ccoaid;
	}

	public String getCcoaid() {
		return ccoaid;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getCurid() {
		return curid;
	}

	public void setCurid(String curid) {
		this.curid = curid;
	}

	public String getCoaid() {
		return coaid;
	}

	public void setCoaid(String coaid) {
		this.coaid = coaid;
	}

	public String getBranchid() {
		return branchid;
	}

	public void setBranchid(String branchid) {
		this.branchid = branchid;
	}

	public String getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid;
	}

	public boolean isEdit() {
		return isEdit;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acCode == null) ? 0 : acCode.hashCode());
		result = prime * result + ((bfRate == null) ? 0 : bfRate.hashCode());
		result = prime * result + ((branchCode == null) ? 0 : branchCode.hashCode());
		result = prime * result + ((branchid == null) ? 0 : branchid.hashCode());
		result = prime * result + ((ccoaid == null) ? 0 : ccoaid.hashCode());
		result = prime * result + ((coaid == null) ? 0 : coaid.hashCode());
		result = prime * result + ((curid == null) ? 0 : curid.hashCode());
		result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
		result = prime * result + ((dCode == null) ? 0 : dCode.hashCode());
		result = prime * result + ((departmentid == null) ? 0 : departmentid.hashCode());
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
		MonthlyBudgetDto other = (MonthlyBudgetDto) obj;
		if (acCode == null) {
			if (other.acCode != null)
				return false;
		} else if (!acCode.equals(other.acCode))
			return false;
		if (bfRate == null) {
			if (other.bfRate != null)
				return false;
		} else if (!bfRate.equals(other.bfRate))
			return false;
		if (branchCode == null) {
			if (other.branchCode != null)
				return false;
		} else if (!branchCode.equals(other.branchCode))
			return false;
		if (branchid == null) {
			if (other.branchid != null)
				return false;
		} else if (!branchid.equals(other.branchid))
			return false;
		if (ccoaid == null) {
			if (other.ccoaid != null)
				return false;
		} else if (!ccoaid.equals(other.ccoaid))
			return false;
		if (coaid == null) {
			if (other.coaid != null)
				return false;
		} else if (!coaid.equals(other.coaid))
			return false;
		if (curid == null) {
			if (other.curid != null)
				return false;
		} else if (!curid.equals(other.curid))
			return false;
		if (currencyCode == null) {
			if (other.currencyCode != null)
				return false;
		} else if (!currencyCode.equals(other.currencyCode))
			return false;
		if (dCode == null) {
			if (other.dCode != null)
				return false;
		} else if (!dCode.equals(other.dCode))
			return false;
		if (departmentid == null) {
			if (other.departmentid != null)
				return false;
		} else if (!departmentid.equals(other.departmentid))
			return false;
		return true;
	}

	public void setAllZero() {
		this.bfRate.setAllZero();
		checkBF(bfRate);
	}

}
