package org.ace.accounting.dto;

public class ReportAccountDto {
	private String acCode;
	private String acName;

	public ReportAccountDto(String acCode, String acName) {
		this.acCode = acCode;
		this.acName = acName;
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

}
