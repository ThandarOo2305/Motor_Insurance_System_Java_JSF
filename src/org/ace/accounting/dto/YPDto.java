package org.ace.accounting.dto;

public class YPDto {
	private String id;
	private String acCode;
	private String acName;
	private String groupId;
	private String headId;

	public YPDto() {
	}

	public YPDto(String id, String acCode, String acName, String groupId, String headId) {
		this.id = id;
		this.acCode = acCode;
		this.acName = acName;
		this.groupId = groupId;
		this.headId = headId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}

}
