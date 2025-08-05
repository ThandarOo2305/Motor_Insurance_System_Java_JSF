package org.ace.accounting.dto;

import java.io.Serializable;

import org.ace.accounting.system.chartaccount.AccountType;

public class CcoaDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String acCode;
	private String acName;
	private AccountType acType;
	private String department;
	private String currency;
	private String branch;

	public CcoaDto(String id, String acCode, String acName, AccountType acType, String department, String currency, String branch) {
		this.id = id;
		this.acCode = acCode;
		this.acName = acName;
		this.acType = acType;
		this.department = department;
		this.currency = currency;
		this.branch = branch;
	}

	public String getId() {
		return id;
	}

	public String getAcCode() {
		return acCode;
	}

	public String getAcName() {
		return acName;
	}

	public AccountType getAcType() {
		return acType;
	}

	public String getDepartment() {
		return department;
	}

	public String getCurrency() {
		return currency;
	}

	public String getBranch() {
		return branch;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acCode == null) ? 0 : acCode.hashCode());
		result = prime * result + ((acName == null) ? 0 : acName.hashCode());
		result = prime * result + ((acType == null) ? 0 : acType.hashCode());
		result = prime * result + ((branch == null) ? 0 : branch.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		CcoaDto other = (CcoaDto) obj;
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
