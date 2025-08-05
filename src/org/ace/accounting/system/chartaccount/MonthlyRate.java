package org.ace.accounting.system.chartaccount;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Embeddable;

@Embeddable
public class MonthlyRate implements Serializable{
	
	private static final long serialVersionUID = 1L;

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

	private BigDecimal m13;

	public MonthlyRate() {
		m1 = BigDecimal.ZERO;
		m2 = BigDecimal.ZERO;
		m3 = BigDecimal.ZERO;
		m4 = BigDecimal.ZERO;
		m5 = BigDecimal.ZERO;
		m6 = BigDecimal.ZERO;
		m7 = BigDecimal.ZERO;
		m8 = BigDecimal.ZERO;
		m9 = BigDecimal.ZERO;
		m10 = BigDecimal.ZERO;
		m11 = BigDecimal.ZERO;
		m12 = BigDecimal.ZERO;
		m13 = BigDecimal.ZERO;
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

	public BigDecimal getM13() {
		return m13;
	}

	public void setM13(BigDecimal m13) {
		this.m13 = m13;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m1 == null) ? 0 : m1.hashCode());
		result = prime * result + ((m10 == null) ? 0 : m10.hashCode());
		result = prime * result + ((m11 == null) ? 0 : m11.hashCode());
		result = prime * result + ((m12 == null) ? 0 : m12.hashCode());
		result = prime * result + ((m13 == null) ? 0 : m13.hashCode());
		result = prime * result + ((m2 == null) ? 0 : m2.hashCode());
		result = prime * result + ((m3 == null) ? 0 : m3.hashCode());
		result = prime * result + ((m4 == null) ? 0 : m4.hashCode());
		result = prime * result + ((m5 == null) ? 0 : m5.hashCode());
		result = prime * result + ((m6 == null) ? 0 : m6.hashCode());
		result = prime * result + ((m7 == null) ? 0 : m7.hashCode());
		result = prime * result + ((m8 == null) ? 0 : m8.hashCode());
		result = prime * result + ((m9 == null) ? 0 : m9.hashCode());
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
		MonthlyRate other = (MonthlyRate) obj;
		if (m1 == null) {
			if (other.m1 != null)
				return false;
		} else if (!m1.equals(other.m1))
			return false;
		if (m10 == null) {
			if (other.m10 != null)
				return false;
		} else if (!m10.equals(other.m10))
			return false;
		if (m11 == null) {
			if (other.m11 != null)
				return false;
		} else if (!m11.equals(other.m11))
			return false;
		if (m12 == null) {
			if (other.m12 != null)
				return false;
		} else if (!m12.equals(other.m12))
			return false;
		if (m13 == null) {
			if (other.m13 != null)
				return false;
		} else if (!m13.equals(other.m13))
			return false;
		if (m2 == null) {
			if (other.m2 != null)
				return false;
		} else if (!m2.equals(other.m2))
			return false;
		if (m3 == null) {
			if (other.m3 != null)
				return false;
		} else if (!m3.equals(other.m3))
			return false;
		if (m4 == null) {
			if (other.m4 != null)
				return false;
		} else if (!m4.equals(other.m4))
			return false;
		if (m5 == null) {
			if (other.m5 != null)
				return false;
		} else if (!m5.equals(other.m5))
			return false;
		if (m6 == null) {
			if (other.m6 != null)
				return false;
		} else if (!m6.equals(other.m6))
			return false;
		if (m7 == null) {
			if (other.m7 != null)
				return false;
		} else if (!m7.equals(other.m7))
			return false;
		if (m8 == null) {
			if (other.m8 != null)
				return false;
		} else if (!m8.equals(other.m8))
			return false;
		if (m9 == null) {
			if (other.m9 != null)
				return false;
		} else if (!m9.equals(other.m9))
			return false;
		return true;
	}

	
	public void setAllZeros() {
		m1 = BigDecimal.ZERO;
		m2 = BigDecimal.ZERO;
		m3 = BigDecimal.ZERO;
		m4 = BigDecimal.ZERO;
		m5 = BigDecimal.ZERO;
		m6 = BigDecimal.ZERO;
		m7 = BigDecimal.ZERO;
		m8 = BigDecimal.ZERO;
		m9 = BigDecimal.ZERO;
		m10 = BigDecimal.ZERO;
		m11 = BigDecimal.ZERO;
		m12 = BigDecimal.ZERO;
		m13 = BigDecimal.ZERO;
	}
	
	public void setAllZerosExceptM1() {
		m2 = BigDecimal.ZERO;
		m3 = BigDecimal.ZERO;
		m4 = BigDecimal.ZERO;
		m5 = BigDecimal.ZERO;
		m6 = BigDecimal.ZERO;
		m7 = BigDecimal.ZERO;
		m8 = BigDecimal.ZERO;
		m9 = BigDecimal.ZERO;
		m10 = BigDecimal.ZERO;
		m11 = BigDecimal.ZERO;
		m12 = BigDecimal.ZERO;
		m13 = BigDecimal.ZERO;
	}
}
