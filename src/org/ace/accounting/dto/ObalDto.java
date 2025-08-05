package org.ace.accounting.dto;

import java.math.BigDecimal;

import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.AccountType;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.department.Department;

public class ObalDto {
	private String acCode;
	private String acName;
	private BigDecimal oBal;
	private BigDecimal hoBal;
	private BigDecimal oldOBal;
	private BigDecimal oldHoBal;
	private AccountType acType;
	private Branch branch;
	private Department department;
	private Currency currency;
	private String ccoaId;
	private String headId;
	private String groupId;

	public ObalDto() {

	}

	public ObalDto(String acCode, String acName, BigDecimal oBal, BigDecimal hoBal, AccountType acType, Branch branch, Department department, Currency currency, String ccoaId,
			String headId, String groupId) {
		this.acCode = acCode;
		this.acName = acName;
		this.oBal = oBal;
		this.oldOBal = new BigDecimal(getoBal().doubleValue());
		this.hoBal = hoBal;
		this.oldHoBal = new BigDecimal(getHoBal().doubleValue());
		this.acType = acType;
		this.branch = branch;
		this.department = department;
		this.currency = currency;
		this.ccoaId = ccoaId;
		this.headId = headId;
		this.groupId = groupId;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
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

	public BigDecimal getoBal() {
		return oBal == null ? new BigDecimal(0) : oBal;
	}

	public void setoBal(BigDecimal oBal) {
		this.oBal = oBal;
	}

	public BigDecimal getHoBal() {
		return hoBal == null ? new BigDecimal(0) : hoBal;
	}

	public void setHoBal(BigDecimal hoBal) {
		this.hoBal = hoBal;
	}

	public AccountType getAcType() {
		return acType;
	}

	public void setAcType(AccountType acType) {
		this.acType = acType;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getCcoaId() {
		return ccoaId;
	}

	public void setCcoaId(String ccoaId) {
		this.ccoaId = ccoaId;
	}

	public boolean isError() {
		double oBal = getoBalDoubleValue();
		double hoBal = getHoBalDoubleValue();
		if ((oBal != 0 && hoBal == 0)) {
			return true;
		} else if (oBal == 0 && hoBal != 0) {
			return true;
		} else {
			return false;
		}
	}

	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acCode == null) ? 0 : acCode.hashCode());
		result = prime * result + ((acName == null) ? 0 : acName.hashCode());
		result = prime * result + ((acType == null) ? 0 : acType.hashCode());
		result = prime * result + ((branch == null) ? 0 : branch.hashCode());
		result = prime * result + ((ccoaId == null) ? 0 : ccoaId.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((headId == null) ? 0 : headId.hashCode());
		result = prime * result + ((hoBal == null) ? 0 : hoBal.hashCode());
		result = prime * result + ((oBal == null) ? 0 : oBal.hashCode());
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
		ObalDto other = (ObalDto) obj;
		if (acCode == null) {
			if (other.acCode != null)
				return false;
		} else if (!acCode.equals(other.acCode))
			return false;
		if (acName == null) {
			if (other.acName != null)
				return false;
		} else if (!acName.equals(other.acName))
			return false;
		if (acType != other.acType)
			return false;
		if (branch == null) {
			if (other.branch != null)
				return false;
		} else if (!branch.equals(other.branch))
			return false;
		if (ccoaId == null) {
			if (other.ccoaId != null)
				return false;
		} else if (!ccoaId.equals(other.ccoaId))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (headId == null) {
			if (other.headId != null)
				return false;
		} else if (!headId.equals(other.headId))
			return false;
		if (hoBal == null) {
			if (other.hoBal != null)
				return false;
		} else if (!hoBal.equals(other.hoBal))
			return false;
		if (oBal == null) {
			if (other.oBal != null)
				return false;
		} else if (!oBal.equals(other.oBal))
			return false;
		return true;
	}

	public double getoBalDoubleValue() {
		return getoBal().doubleValue();
	}

	public double getHoBalDoubleValue() {
		return getHoBal().doubleValue();
	}

	public boolean isUpdated() {
		boolean result = false;
		if (getoBal().doubleValue() != oldOBal.doubleValue()) {
			result = true;
		} else if (getHoBal().doubleValue() != oldHoBal.doubleValue()) {
			result = true;
		}
		return result;

	}
}
