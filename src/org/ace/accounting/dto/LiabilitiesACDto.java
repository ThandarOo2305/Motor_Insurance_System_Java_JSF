package org.ace.accounting.dto;

import java.math.BigDecimal;

public class LiabilitiesACDto {
	private String acCode;
	private String dCode;
	private BigDecimal cBal;

	public LiabilitiesACDto() {
	}

	public LiabilitiesACDto(String acCode, String dCode) {
		this.acCode = acCode;
		this.dCode = dCode;
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

	public BigDecimal getcBal() {
		return cBal;
	}

	public void setcBal(BigDecimal cBal) {
		this.cBal = cBal;
	}

	@Override
	public String toString() {
		return acCode + " , " + dCode + " , " + cBal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acCode == null) ? 0 : acCode.hashCode());
		result = prime * result + ((cBal == null) ? 0 : cBal.hashCode());
		result = prime * result + ((dCode == null) ? 0 : dCode.hashCode());
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
		LiabilitiesACDto other = (LiabilitiesACDto) obj;
		if (acCode == null) {
			if (other.acCode != null)
				return false;
		} else if (!acCode.equals(other.acCode))
			return false;
		if (cBal == null) {
			if (other.cBal != null)
				return false;
		} else if (!cBal.equals(other.cBal))
			return false;
		if (dCode == null) {
			if (other.dCode != null)
				return false;
		} else if (!dCode.equals(other.dCode))
			return false;
		return true;
	}

}
