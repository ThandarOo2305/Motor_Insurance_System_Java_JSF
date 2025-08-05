package org.ace.accounting.report.incomeandexpenditure;

import java.math.BigDecimal;

public class IncomeAndExpenditureDto {

	private String acCode;
	private String acName;
	private String acType;
	// this month is for monthly
	private BigDecimal thisMonth;

	// m 12 columns for quarterly reports
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

	// m is total ( one ) column for quarterly and one column according to
	// month for monthly
	private BigDecimal yearToDate;

	public IncomeAndExpenditureDto() {
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

	public BigDecimal getThisMonth() {
		return thisMonth;
	}

	public void setThisMonth(BigDecimal thisMonth) {
		this.thisMonth = thisMonth;
	}

	public BigDecimal getYearToDate() {
		return yearToDate;
	}

	public void setYearToDate(BigDecimal yearToDate) {
		this.yearToDate = yearToDate;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}

	public BigDecimal getM1() {
		return m1;
	}

	public void setM1(BigDecimal m1) {
		this.m1 = m1;
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

	@Override
	public String toString() {
		return acCode + " ," + acName + " ," + yearToDate;
	}
}
