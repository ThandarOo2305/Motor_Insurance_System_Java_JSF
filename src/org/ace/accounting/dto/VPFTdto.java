package org.ace.accounting.dto;

import java.util.Date;

public class VPFTdto {
	private Date startDate;
	private Date endDate;
	private String voucherNo;

	public VPFTdto() {
	}

	public VPFTdto(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	

}
