package org.ace.accounting.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.ace.accounting.system.chartaccount.ChartOfAccount;

public class EnquiryLedgerDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String acCode;
	private String acName;
	private String currencyCode;
	private BigDecimal m;
	private String branchCode;

	private String curid;
	private String coaid;
	private String branchid;
	private ChartOfAccount coa;

	public EnquiryLedgerDto() {
	}

	public EnquiryLedgerDto(String id, String acCode, String acName, String currencyCode, BigDecimal m, String branchCode) {
		this.id = id;
		this.acCode = acCode;
		this.acName = acName;
		this.currencyCode = currencyCode;
		this.m = m;
		this.branchCode = branchCode;
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

	public String getCurrencyCode() {
		return currencyCode;
	}

	public BigDecimal getM() {
		return m == null ? new BigDecimal(0) : m;
	}

	public String getBranchCode() {
		return branchCode;
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

	public ChartOfAccount getCoa() {
		return coa;
	}

	public void setCoa(ChartOfAccount coa) {
		this.coa = coa;
		if (coa == null)
			this.coaid = null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acCode == null) ? 0 : acCode.hashCode());
		result = prime * result + ((acName == null) ? 0 : acName.hashCode());
		result = prime * result + ((branchCode == null) ? 0 : branchCode.hashCode());
		result = prime * result + ((branchid == null) ? 0 : branchid.hashCode());
		result = prime * result + ((coaid == null) ? 0 : coaid.hashCode());
		result = prime * result + ((curid == null) ? 0 : curid.hashCode());
		result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
		result = prime * result + ((m == null) ? 0 : m.hashCode());
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
		EnquiryLedgerDto other = (EnquiryLedgerDto) obj;
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
		if (m == null) {
			if (other.m != null)
				return false;
		} else if (!m.equals(other.m))
			return false;
		return true;
	}

}
