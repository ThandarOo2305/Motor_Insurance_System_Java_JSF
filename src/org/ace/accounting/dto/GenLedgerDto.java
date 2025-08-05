package org.ace.accounting.dto;

import java.io.Serializable;
import java.util.List;

import org.ace.accounting.system.chartaccount.ChartOfAccount;

public class GenLedgerDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ChartOfAccount coa;
	private List<GenLedgerDetail> detailList;

	public GenLedgerDto() {
		super();
	}

	public ChartOfAccount getCoa() {
		return coa;
	}

	public void setCoa(ChartOfAccount coa) {
		this.coa = coa;
	}

	public List<GenLedgerDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<GenLedgerDetail> detailList) {
		this.detailList = detailList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coa == null) ? 0 : coa.hashCode());
		result = prime * result + ((detailList == null) ? 0 : detailList.hashCode());
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
		GenLedgerDto other = (GenLedgerDto) obj;
		if (coa == null) {
			if (other.coa != null)
				return false;
		} else if (!coa.equals(other.coa))
			return false;
		if (detailList == null) {
			if (other.detailList != null)
				return false;
		} else if (!detailList.equals(other.detailList))
			return false;
		return true;
	}

}
