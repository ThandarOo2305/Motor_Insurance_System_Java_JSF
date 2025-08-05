package org.ace.accounting.system.chartaccount;

import java.math.BigDecimal;

import javax.persistence.Embeddable;

@Embeddable
public class MrevRate {

	private BigDecimal mrev1;

	private BigDecimal mrev2;

	private BigDecimal mrev3;

	private BigDecimal mrev4;

	private BigDecimal mrev5;

	private BigDecimal mrev6;

	private BigDecimal mrev7;

	private BigDecimal mrev8;

	private BigDecimal mrev9;

	private BigDecimal mrev10;

	private BigDecimal mrev11;

	private BigDecimal mrev12;

	private BigDecimal mrev13;

	public MrevRate() {
		mrev1 = BigDecimal.ZERO;
		mrev2 = BigDecimal.ZERO;
		mrev3 = BigDecimal.ZERO;
		mrev4 = BigDecimal.ZERO;
		mrev5 = BigDecimal.ZERO;
		mrev6 = BigDecimal.ZERO;
		mrev7 = BigDecimal.ZERO;
		mrev8 = BigDecimal.ZERO;
		mrev9 = BigDecimal.ZERO;
		mrev10 = BigDecimal.ZERO;
		mrev11 = BigDecimal.ZERO;
		mrev12 = BigDecimal.ZERO;
		mrev13 = BigDecimal.ZERO;
	}

	public BigDecimal getMrev1() {
		return mrev1;
	}

	public void setMrev1(BigDecimal mrev1) {
		this.mrev1 = mrev1;
	}

	public BigDecimal getMrev2() {
		return mrev2;
	}

	public void setMrev2(BigDecimal mrev2) {
		this.mrev2 = mrev2;
	}

	public BigDecimal getMrev3() {
		return mrev3;
	}

	public void setMrev3(BigDecimal mrev3) {
		this.mrev3 = mrev3;
	}

	public BigDecimal getMrev4() {
		return mrev4;
	}

	public void setMrev4(BigDecimal mrev4) {
		this.mrev4 = mrev4;
	}

	public BigDecimal getMrev5() {
		return mrev5;
	}

	public void setMrev5(BigDecimal mrev5) {
		this.mrev5 = mrev5;
	}

	public BigDecimal getMrev6() {
		return mrev6;
	}

	public void setMrev6(BigDecimal mrev6) {
		this.mrev6 = mrev6;
	}

	public BigDecimal getMrev7() {
		return mrev7;
	}

	public void setMrev7(BigDecimal mrev7) {
		this.mrev7 = mrev7;
	}

	public BigDecimal getMrev8() {
		return mrev8;
	}

	public void setMrev8(BigDecimal mrev8) {
		this.mrev8 = mrev8;
	}

	public BigDecimal getMrev9() {
		return mrev9;
	}

	public void setMrev9(BigDecimal mrev9) {
		this.mrev9 = mrev9;
	}

	public BigDecimal getMrev10() {
		return mrev10;
	}

	public void setMrev10(BigDecimal mrev10) {
		this.mrev10 = mrev10;
	}

	public BigDecimal getMrev11() {
		return mrev11;
	}

	public void setMrev11(BigDecimal mrev11) {
		this.mrev11 = mrev11;
	}

	public BigDecimal getMrev12() {
		return mrev12;
	}

	public void setMrev12(BigDecimal mrev12) {
		this.mrev12 = mrev12;
	}

	public BigDecimal getMrev13() {
		return mrev13;
	}

	public void setMrev13(BigDecimal mrev13) {
		this.mrev13 = mrev13;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mrev1 == null) ? 0 : mrev1.hashCode());
		result = prime * result + ((mrev10 == null) ? 0 : mrev10.hashCode());
		result = prime * result + ((mrev11 == null) ? 0 : mrev11.hashCode());
		result = prime * result + ((mrev12 == null) ? 0 : mrev12.hashCode());
		result = prime * result + ((mrev13 == null) ? 0 : mrev13.hashCode());
		result = prime * result + ((mrev2 == null) ? 0 : mrev2.hashCode());
		result = prime * result + ((mrev3 == null) ? 0 : mrev3.hashCode());
		result = prime * result + ((mrev4 == null) ? 0 : mrev4.hashCode());
		result = prime * result + ((mrev5 == null) ? 0 : mrev5.hashCode());
		result = prime * result + ((mrev6 == null) ? 0 : mrev6.hashCode());
		result = prime * result + ((mrev7 == null) ? 0 : mrev7.hashCode());
		result = prime * result + ((mrev8 == null) ? 0 : mrev8.hashCode());
		result = prime * result + ((mrev9 == null) ? 0 : mrev9.hashCode());
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
		MrevRate other = (MrevRate) obj;
		if (mrev1 == null) {
			if (other.mrev1 != null)
				return false;
		} else if (!mrev1.equals(other.mrev1))
			return false;
		if (mrev10 == null) {
			if (other.mrev10 != null)
				return false;
		} else if (!mrev10.equals(other.mrev10))
			return false;
		if (mrev11 == null) {
			if (other.mrev11 != null)
				return false;
		} else if (!mrev11.equals(other.mrev11))
			return false;
		if (mrev12 == null) {
			if (other.mrev12 != null)
				return false;
		} else if (!mrev12.equals(other.mrev12))
			return false;
		if (mrev13 == null) {
			if (other.mrev13 != null)
				return false;
		} else if (!mrev13.equals(other.mrev13))
			return false;
		if (mrev2 == null) {
			if (other.mrev2 != null)
				return false;
		} else if (!mrev2.equals(other.mrev2))
			return false;
		if (mrev3 == null) {
			if (other.mrev3 != null)
				return false;
		} else if (!mrev3.equals(other.mrev3))
			return false;
		if (mrev4 == null) {
			if (other.mrev4 != null)
				return false;
		} else if (!mrev4.equals(other.mrev4))
			return false;
		if (mrev5 == null) {
			if (other.mrev5 != null)
				return false;
		} else if (!mrev5.equals(other.mrev5))
			return false;
		if (mrev6 == null) {
			if (other.mrev6 != null)
				return false;
		} else if (!mrev6.equals(other.mrev6))
			return false;
		if (mrev7 == null) {
			if (other.mrev7 != null)
				return false;
		} else if (!mrev7.equals(other.mrev7))
			return false;
		if (mrev8 == null) {
			if (other.mrev8 != null)
				return false;
		} else if (!mrev8.equals(other.mrev8))
			return false;
		if (mrev9 == null) {
			if (other.mrev9 != null)
				return false;
		} else if (!mrev9.equals(other.mrev9))
			return false;
		return true;
	}

	public void setAllZeros() {
		mrev1 = BigDecimal.ZERO;
		mrev2 = BigDecimal.ZERO;
		mrev3 = BigDecimal.ZERO;
		mrev4 = BigDecimal.ZERO;
		mrev5 = BigDecimal.ZERO;
		mrev6 = BigDecimal.ZERO;
		mrev7 = BigDecimal.ZERO;
		mrev8 = BigDecimal.ZERO;
		mrev9 = BigDecimal.ZERO;
		mrev10 = BigDecimal.ZERO;
		mrev11 = BigDecimal.ZERO;
		mrev12 = BigDecimal.ZERO;
		mrev13 = BigDecimal.ZERO;
	}
}
