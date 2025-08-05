package org.ace.accounting.dto;

import org.ace.accounting.system.chartaccount.AccountType;

public class CoaListingDto {
	private String acCode;
	private String acName;
	private AccountType acType;

	public CoaListingDto(String acCode, String acName, AccountType acType) {
		this.acCode = acCode;
		this.acName = acName;
		this.acType = acType;
	}

	public CoaListingDto() {

	}

	public String getAcCode() {
		return acCode;
	}

	public String getAcName() {
		return acName;
	}

	public String getAcType() {
		return acType.name();
	}

}
