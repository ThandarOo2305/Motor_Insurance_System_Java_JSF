package org.ace.accounting.report.balancesheet;

import java.math.BigDecimal;

public class BalanceSheetDTO {
	private String id;
	private String acType;
	private String acCode;
	private String acCodeType;
	private String acName;
	private boolean isShowHide;
	private boolean status;

	private BigDecimal m1;
	private BigDecimal m2;
	private BigDecimal m3;
	private BigDecimal m4;
	private BigDecimal m5;
	private BigDecimal m6;
	private BigDecimal m7;
	private BigDecimal m8;
	private BigDecimal m9;
	private BigDecimal m10;
	private BigDecimal m11;
	private BigDecimal m12;
	// private BigDecimal total;

	private String parentId;
	private String parentName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}

	public String getAcCode() {
		return acCode;
	}

	public void setAcCode(String acCode) {
		this.acCode = acCode;
	}

	public String getAcCodeType() {
		return acCodeType;
	}

	public void setAcCodeType(String acCodeType) {
		this.acCodeType = acCodeType;
	}

	public String getAcName() {
		return acName;
	}

	public void setAcName(String acName) {
		this.acName = acName;
	}

	public BigDecimal getM1() {
		return m1;
	}

	public void setM1(BigDecimal m1) {
		this.m1 = m1;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public BigDecimal getM2() {
		return m2;
	}

	public void setM2(BigDecimal m2) {
		this.m2 = m2;
	}

	public BigDecimal getM3() {
		return m3;
	}

	public void setM3(BigDecimal m3) {
		this.m3 = m3;
	}

	public BigDecimal getM4() {
		return m4;
	}

	public void setM4(BigDecimal m4) {
		this.m4 = m4;
	}

	public BigDecimal getM5() {
		return m5;
	}

	public void setM5(BigDecimal m5) {
		this.m5 = m5;
	}

	public BigDecimal getM6() {
		return m6;
	}

	public void setM6(BigDecimal m6) {
		this.m6 = m6;
	}

	public BigDecimal getM7() {
		return m7;
	}

	public void setM7(BigDecimal m7) {
		this.m7 = m7;
	}

	public BigDecimal getM8() {
		return m8;
	}

	public void setM8(BigDecimal m8) {
		this.m8 = m8;
	}

	public BigDecimal getM9() {
		return m9;
	}

	public void setM9(BigDecimal m9) {
		this.m9 = m9;
	}

	public BigDecimal getM10() {
		return m10;
	}

	public void setM10(BigDecimal m10) {
		this.m10 = m10;
	}

	public BigDecimal getM11() {
		return m11;
	}

	public void setM11(BigDecimal m11) {
		this.m11 = m11;
	}

	public BigDecimal getM12() {
		return m12;
	}

	public void setM12(BigDecimal m12) {
		this.m12 = m12;
	}

	public boolean isShowHide() {
		return isShowHide;
	}

	public void setShowHide(boolean isShowHide) {
		this.isShowHide = isShowHide;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
