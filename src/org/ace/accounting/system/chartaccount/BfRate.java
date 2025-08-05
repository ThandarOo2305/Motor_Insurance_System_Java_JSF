package org.ace.accounting.system.chartaccount;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Embeddable;

/*
 * Budget Figure Rate
 */
@Embeddable
public class BfRate implements Serializable{

	private static final long serialVersionUID = 1L;

	/*
	 * Budget Figure Rate for 1st Month According to Financial Year.
	 */
	private BigDecimal bf1;

	/*
	 * Budget Figure Rate for 2nd Month According to Financial Year.
	 */
	private BigDecimal bf2;

	/*
	 * Budget Figure Rate for 3rd Month According to Financial Year.
	 */
	private BigDecimal bf3;

	/*
	 * Budget Figure Rate for 4th Month According to Financial Year.
	 */
	private BigDecimal bf4;

	/*
	 * Budget Figure Rate for 5th Month According to Financial Year.
	 */
	private BigDecimal bf5;

	/*
	 * Budget Figure Rate for 6th Month According to Financial Year.
	 */
	private BigDecimal bf6;

	/*
	 * Budget Figure Rate for 7th Month According to Financial Year.
	 */
	private BigDecimal bf7;

	/*
	 * Budget Figure Rate for 8th Month According to Financial Year.
	 */
	private BigDecimal bf8;

	/*
	 * Budget Figure Rate for 9th Month According to Financial Year.
	 */
	private BigDecimal bf9;

	/*
	 * Budget Figure Rate for 10th Month According to Financial Year.
	 */
	private BigDecimal bf10;

	/*
	 * Budget Figure Rate for 11th Month According to Financial Year.
	 */
	private BigDecimal bf11;

	/*
	 * Budget Figure Rate for 12th Month According to Financial Year.
	 */
	private BigDecimal bf12;

	/*
	 * Budget Figure Rate for the month more than Financial Year date.
	 */
	private BigDecimal bf13;

	public BfRate() {
		setAllZero();
	}

	public BfRate(BfRate bfRate) {
		this.bf1 = bfRate.getBf1();
		this.bf2 = bfRate.getBf2();
		this.bf3 = bfRate.getBf3();
		this.bf4 = bfRate.getBf4();
		this.bf5 = bfRate.getBf5();
		this.bf6 = bfRate.getBf6();
		this.bf7 = bfRate.getBf7();
		this.bf8 = bfRate.getBf8();
		this.bf9 = bfRate.getBf9();
		this.bf10 = bfRate.getBf10();
		this.bf11 = bfRate.getBf11();
		this.bf12 = bfRate.getBf12();
		this.bf13 = bfRate.getBf13();
	}

	public BigDecimal getBf1() {
		return bf1 == null ? new BigDecimal(0) : bf1;
	}

	public void setBf1(BigDecimal bf1) {
		this.bf1 = bf1;
	}

	public BigDecimal getBf2() {
		return bf2 == null ? new BigDecimal(0) : bf2;
	}

	public void setBf2(BigDecimal bf2) {
		this.bf2 = bf2;
	}

	public BigDecimal getBf3() {
		return bf3 == null ? new BigDecimal(0) : bf3;
	}

	public void setBf3(BigDecimal bf3) {
		this.bf3 = bf3;
	}

	public BigDecimal getBf4() {
		return bf4 == null ? new BigDecimal(0) : bf4;
	}

	public void setBf4(BigDecimal bf4) {
		this.bf4 = bf4;
	}

	public BigDecimal getBf5() {
		return bf5 == null ? new BigDecimal(0) : bf5;
	}

	public void setBf5(BigDecimal bf5) {
		this.bf5 = bf5;
	}

	public BigDecimal getBf6() {
		return bf6 == null ? new BigDecimal(0) : bf6;
	}

	public void setBf6(BigDecimal bf6) {
		this.bf6 = bf6;
	}

	public BigDecimal getBf7() {
		return bf7 == null ? new BigDecimal(0) : bf7;
	}

	public void setBf7(BigDecimal bf7) {
		this.bf7 = bf7;
	}

	public BigDecimal getBf8() {
		return bf8 == null ? new BigDecimal(0) : bf8;
	}

	public void setBf8(BigDecimal bf8) {
		this.bf8 = bf8;
	}

	public BigDecimal getBf9() {
		return bf9 == null ? new BigDecimal(0) : bf9;
	}

	public void setBf9(BigDecimal bf9) {
		this.bf9 = bf9;
	}

	public BigDecimal getBf10() {
		return bf10 == null ? new BigDecimal(0) : bf10;
	}

	public void setBf10(BigDecimal bf10) {
		this.bf10 = bf10;
	}

	public BigDecimal getBf11() {
		return bf11 == null ? new BigDecimal(0) : bf11;
	}

	public void setBf11(BigDecimal bf11) {
		this.bf11 = bf11;
	}

	public BigDecimal getBf12() {
		return bf12 == null ? new BigDecimal(0) : bf12;
	}

	public void setBf12(BigDecimal bf12) {
		this.bf12 = bf12;
	}

	public BigDecimal getBf13() {
		return bf13 == null ? new BigDecimal(0) : bf13;
	}

	public void setBf13(BigDecimal bf13) {
		this.bf13 = bf13;
	}

	public void setAllZero() {
		setBf1(BigDecimal.ZERO);
		setBf2(BigDecimal.ZERO);
		setBf3(BigDecimal.ZERO);
		setBf4(BigDecimal.ZERO);
		setBf5(BigDecimal.ZERO);
		setBf6(BigDecimal.ZERO);
		setBf7(BigDecimal.ZERO);
		setBf8(BigDecimal.ZERO);
		setBf9(BigDecimal.ZERO);
		setBf10(BigDecimal.ZERO);
		setBf11(BigDecimal.ZERO);
		setBf12(BigDecimal.ZERO);
		setBf13(BigDecimal.ZERO);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bf1 == null) ? 0 : bf1.hashCode());
		result = prime * result + ((bf10 == null) ? 0 : bf10.hashCode());
		result = prime * result + ((bf11 == null) ? 0 : bf11.hashCode());
		result = prime * result + ((bf12 == null) ? 0 : bf12.hashCode());
		result = prime * result + ((bf13 == null) ? 0 : bf13.hashCode());
		result = prime * result + ((bf2 == null) ? 0 : bf2.hashCode());
		result = prime * result + ((bf3 == null) ? 0 : bf3.hashCode());
		result = prime * result + ((bf4 == null) ? 0 : bf4.hashCode());
		result = prime * result + ((bf5 == null) ? 0 : bf5.hashCode());
		result = prime * result + ((bf6 == null) ? 0 : bf6.hashCode());
		result = prime * result + ((bf7 == null) ? 0 : bf7.hashCode());
		result = prime * result + ((bf8 == null) ? 0 : bf8.hashCode());
		result = prime * result + ((bf9 == null) ? 0 : bf9.hashCode());
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
		BfRate other = (BfRate) obj;
		if (bf1 == null) {
			if (other.bf1 != null)
				return false;
		} else if (!bf1.equals(other.bf1) && bf1.doubleValue() != (other.bf1.doubleValue()))
			return false;
		if (bf10 == null) {
			if (other.bf10 != null)
				return false;
		} else if (!bf10.equals(other.bf10) && bf10.doubleValue() != (other.bf10.doubleValue()))
			return false;
		if (bf11 == null) {
			if (other.bf11 != null)
				return false;
		} else if (!bf11.equals(other.bf11) && bf11.doubleValue() != (other.bf11.doubleValue()))
			return false;
		if (bf12 == null) {
			if (other.bf12 != null)
				return false;
		} else if (!bf12.equals(other.bf12) && bf12.doubleValue() != (other.bf12.doubleValue()))
			return false;
		if (bf13 == null) {
			if (other.bf13 != null)
				return false;
		} else if (!bf13.equals(other.bf13) && bf13.doubleValue() != (other.bf13.doubleValue()))
			return false;
		if (bf2 == null) {
			if (other.bf2 != null)
				return false;
		} else if (!bf2.equals(other.bf2) && bf2.doubleValue() != (other.bf2.doubleValue()))
			return false;
		if (bf3 == null) {
			if (other.bf3 != null)
				return false;
		} else if (!bf3.equals(other.bf3) && bf3.doubleValue() != (other.bf3.doubleValue()))
			return false;
		if (bf4 == null) {
			if (other.bf4 != null)
				return false;
		} else if (!bf4.equals(other.bf4) && bf4.doubleValue() != (other.bf4.doubleValue()))
			return false;
		if (bf5 == null) {
			if (other.bf5 != null)
				return false;
		} else if (!bf5.equals(other.bf5) && bf5.doubleValue() != (other.bf5.doubleValue()))
			return false;
		if (bf6 == null) {
			if (other.bf6 != null)
				return false;
		} else if (!bf6.equals(other.bf6) && bf6.doubleValue() != (other.bf6.doubleValue()))
			return false;
		if (bf7 == null) {
			if (other.bf7 != null)
				return false;
		} else if (!bf7.equals(other.bf7) && bf7.doubleValue() != (other.bf7.doubleValue()))
			return false;
		if (bf8 == null) {
			if (other.bf8 != null)
				return false;
		} else if (!bf8.equals(other.bf8) && bf8.doubleValue() != (other.bf8.doubleValue()))
			return false;
		if (bf9 == null) {
			if (other.bf9 != null)
				return false;
		} else if (!bf9.equals(other.bf9) && bf9.doubleValue() != (other.bf9.doubleValue()))
			return false;
		return true;
	}

}
