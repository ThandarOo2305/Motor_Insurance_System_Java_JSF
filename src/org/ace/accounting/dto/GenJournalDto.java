package org.ace.accounting.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GenJournalDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String voucherNo;
	private Date settlementDate;
	private int srNo;
	private List<VLdto> voucherList;
	// private VLdto grandTotal;

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public List<VLdto> getVoucherList() {
		return voucherList;
	}

	public void setVoucherList(List<VLdto> voucherList) {
		this.voucherList = voucherList;
	}

	public int getSrNo() {
		return srNo;
	}

	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((settlementDate == null) ? 0 : settlementDate.hashCode());
		result = prime * result + ((voucherList == null) ? 0 : voucherList.hashCode());
		result = prime * result + ((voucherNo == null) ? 0 : voucherNo.hashCode());
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
		GenJournalDto other = (GenJournalDto) obj;
		if (settlementDate == null) {
			if (other.settlementDate != null)
				return false;
		} else if (!settlementDate.equals(other.settlementDate))
			return false;
		if (voucherList == null) {
			if (other.voucherList != null)
				return false;
		} else if (!voucherList.equals(other.voucherList))
			return false;
		if (voucherNo == null) {
			if (other.voucherNo != null)
				return false;
		} else if (!voucherNo.equals(other.voucherNo))
			return false;
		return true;
	}

}
