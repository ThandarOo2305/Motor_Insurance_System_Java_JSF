package org.ace.accounting.dto;

public class AllocateByDeptDto {
	private String acCode;
	private String dCode;
	private String acType;

	public AllocateByDeptDto() {
		super();
	}

	public AllocateByDeptDto(String acCode, String dCode, String acType) {
		super();
		this.acCode = acCode;
		this.dCode = dCode;
		this.acType = acType;
	}

	public String getAcCode() {
		return acCode;
	}

	public String getdCode() {
		return dCode;
	}

	public String getAcType() {
		return acType;
	}

}
