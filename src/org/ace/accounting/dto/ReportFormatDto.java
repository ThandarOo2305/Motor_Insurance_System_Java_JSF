package org.ace.accounting.dto;

public class ReportFormatDto {
	private String formatType;
	private String formatName;

	public ReportFormatDto() {
	}

	public ReportFormatDto(String formatType, String formatName) {
		this.formatType = formatType;
		this.formatName = formatName;
	}

	public String getFormatType() {
		return formatType;
	}

	public void setFormatType(String formatType) {
		this.formatType = formatType;
	}

	public String getFormatName() {
		return formatName;
	}

	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}

}
