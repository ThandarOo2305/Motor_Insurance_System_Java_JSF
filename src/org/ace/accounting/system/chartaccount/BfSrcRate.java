package org.ace.accounting.system.chartaccount;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Embeddable;

/*
 * Budget Figure Rate for Home Currency Amount
 */
@Embeddable
public class BfSrcRate implements Serializable{
   
	private static final long serialVersionUID = 1L;

	/*
	 * Budget Figure Rate for Home Currency Amount of 1st Month According to
	 * Financial Year.
	 */
	private BigDecimal bfsrc1;

	/*
	 * Budget Figure Rate for Home Currency Amount of 2nd Month According to
	 * Financial Year.
	 */
	private BigDecimal bfsrc2;

	/*
	 * Budget Figure Rate for Home Currency Amount of 3rd Month According to
	 * Financial Year.
	 */
	private BigDecimal bfsrc3;

	/*
	 * Budget Figure Rate for Home Currency Amount of 4th Month According to
	 * Financial Year.
	 */
	private BigDecimal bfsrc4;

	/*
	 * Budget Figure Rate for Home Currency Amount of 5th Month According to
	 * Financial Year.
	 */
	private BigDecimal bfsrc5;

	/*
	 * Budget Figure Rate for Home Currency Amount of 6th Month According to
	 * Financial Year.
	 */
	private BigDecimal bfsrc6;

	/*
	 * Budget Figure Rate for Home Currency Amount of 7th Month According to
	 * Financial Year.
	 */
	private BigDecimal bfsrc7;

	/*
	 * Budget Figure Rate for Home Currency Amount of 8th Month According to
	 * Financial Year.
	 */
	private BigDecimal bfsrc8;

	/*
	 * Budget Figure Rate for Home Currency Amount of 9th Month According to
	 * Financial Year.
	 */
	private BigDecimal bfsrc9;

	/*
	 * Budget Figure Rate for Home Currency Amount of 10th Month According to
	 * Financial Year.
	 */
	private BigDecimal bfsrc10;

	/*
	 * Budget Figure Rate for Home Currency Amount of 11th Month According to
	 * Financial Year.
	 */
	private BigDecimal bfsrc11;

	/*
	 * Budget Figure Rate for Home Currency Amount of 12th Month According to
	 * Financial Year.
	 */
	private BigDecimal bfsrc12;

	/*
	 * Budget Figure Rate for Home Currency Amount of the month more than
	 * Financial Year date.
	 */
	private BigDecimal bfsrc13;

	public BfSrcRate() {
		setAllZero();
	}

	public BfSrcRate(BfSrcRate bfsrcRate) {
		super();
		this.bfsrc1 = bfsrcRate.getBfsrc1();
		this.bfsrc2 = bfsrcRate.getBfsrc2();
		this.bfsrc3 = bfsrcRate.getBfsrc3();
		this.bfsrc4 = bfsrcRate.getBfsrc4();
		this.bfsrc5 = bfsrcRate.getBfsrc5();
		this.bfsrc6 = bfsrcRate.getBfsrc6();
		this.bfsrc7 = bfsrcRate.getBfsrc7();
		this.bfsrc8 = bfsrcRate.getBfsrc8();
		this.bfsrc9 = bfsrcRate.getBfsrc9();
		this.bfsrc10 = bfsrcRate.getBfsrc10();
		this.bfsrc11 = bfsrcRate.getBfsrc11();
		this.bfsrc12 = bfsrcRate.getBfsrc12();
		this.bfsrc13 = bfsrcRate.getBfsrc13();
	}

	public BigDecimal getBfsrc1() {
		return bfsrc1 == null ? new BigDecimal(0) : bfsrc1;
	}

	public void setBfsrc1(BigDecimal bfsrc1) {
		this.bfsrc1 = bfsrc1;
	}

	public BigDecimal getBfsrc2() {
		return bfsrc2 == null ? new BigDecimal(0) : bfsrc2;
	}

	public void setBfsrc2(BigDecimal bfsrc2) {
		this.bfsrc2 = bfsrc2;
	}

	public BigDecimal getBfsrc3() {
		return bfsrc3 == null ? new BigDecimal(0) : bfsrc3;
	}

	public void setBfsrc3(BigDecimal bfsrc3) {
		this.bfsrc3 = bfsrc3;
	}

	public BigDecimal getBfsrc4() {
		return bfsrc4 == null ? new BigDecimal(0) : bfsrc4;
	}

	public void setBfsrc4(BigDecimal bfsrc4) {
		this.bfsrc4 = bfsrc4;
	}

	public BigDecimal getBfsrc5() {
		return bfsrc5 == null ? new BigDecimal(0) : bfsrc5;
	}

	public void setBfsrc5(BigDecimal bfsrc5) {
		this.bfsrc5 = bfsrc5;
	}

	public BigDecimal getBfsrc6() {
		return bfsrc6 == null ? new BigDecimal(0) : bfsrc6;
	}

	public void setBfsrc6(BigDecimal bfsrc6) {
		this.bfsrc6 = bfsrc6;
	}

	public BigDecimal getBfsrc7() {
		return bfsrc7 == null ? new BigDecimal(0) : bfsrc7;
	}

	public void setBfsrc7(BigDecimal bfsrc7) {
		this.bfsrc7 = bfsrc7;
	}

	public BigDecimal getBfsrc8() {
		return bfsrc8 == null ? new BigDecimal(0) : bfsrc8;
	}

	public void setBfsrc8(BigDecimal bfsrc8) {
		this.bfsrc8 = bfsrc8;
	}

	public BigDecimal getBfsrc9() {
		return bfsrc9 == null ? new BigDecimal(0) : bfsrc9;
	}

	public void setBfsrc9(BigDecimal bfsrc9) {
		this.bfsrc9 = bfsrc9;
	}

	public BigDecimal getBfsrc10() {
		return bfsrc10 == null ? new BigDecimal(0) : bfsrc10;
	}

	public void setBfsrc10(BigDecimal bfsrc10) {
		this.bfsrc10 = bfsrc10;
	}

	public BigDecimal getBfsrc11() {
		return bfsrc11 == null ? new BigDecimal(0) : bfsrc11;
	}

	public void setBfsrc11(BigDecimal bfsrc11) {
		this.bfsrc11 = bfsrc11;
	}

	public BigDecimal getBfsrc12() {
		return bfsrc12 == null ? new BigDecimal(0) : bfsrc12;
	}

	public void setBfsrc12(BigDecimal bfsrc12) {
		this.bfsrc12 = bfsrc12;
	}

	public BigDecimal getBfsrc13() {
		return bfsrc13 == null ? new BigDecimal(0) : bfsrc13;
	}

	public void setBfsrc13(BigDecimal bfsrc13) {
		this.bfsrc13 = bfsrc13;
	}

	public void setAllZero() {
		setBfsrc1(BigDecimal.ZERO);
		setBfsrc2(BigDecimal.ZERO);
		setBfsrc3(BigDecimal.ZERO);
		setBfsrc4(BigDecimal.ZERO);
		setBfsrc5(BigDecimal.ZERO);
		setBfsrc6(BigDecimal.ZERO);
		setBfsrc7(BigDecimal.ZERO);
		setBfsrc8(BigDecimal.ZERO);
		setBfsrc9(BigDecimal.ZERO);
		setBfsrc10(BigDecimal.ZERO);
		setBfsrc11(BigDecimal.ZERO);
		setBfsrc12(BigDecimal.ZERO);
		setBfsrc13(BigDecimal.ZERO);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bfsrc1 == null) ? 0 : bfsrc1.hashCode());
		result = prime * result + ((bfsrc10 == null) ? 0 : bfsrc10.hashCode());
		result = prime * result + ((bfsrc11 == null) ? 0 : bfsrc11.hashCode());
		result = prime * result + ((bfsrc12 == null) ? 0 : bfsrc12.hashCode());
		result = prime * result + ((bfsrc13 == null) ? 0 : bfsrc13.hashCode());
		result = prime * result + ((bfsrc2 == null) ? 0 : bfsrc2.hashCode());
		result = prime * result + ((bfsrc3 == null) ? 0 : bfsrc3.hashCode());
		result = prime * result + ((bfsrc4 == null) ? 0 : bfsrc4.hashCode());
		result = prime * result + ((bfsrc5 == null) ? 0 : bfsrc5.hashCode());
		result = prime * result + ((bfsrc6 == null) ? 0 : bfsrc6.hashCode());
		result = prime * result + ((bfsrc7 == null) ? 0 : bfsrc7.hashCode());
		result = prime * result + ((bfsrc8 == null) ? 0 : bfsrc8.hashCode());
		result = prime * result + ((bfsrc9 == null) ? 0 : bfsrc9.hashCode());
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
		BfSrcRate other = (BfSrcRate) obj;
		if (bfsrc1 == null) {
			if (other.bfsrc1 != null)
				return false;
		} else if (!bfsrc1.equals(other.bfsrc1) && bfsrc1.doubleValue() != (other.bfsrc1.doubleValue()))
			return false;
		if (bfsrc10 == null) {
			if (other.bfsrc10 != null)
				return false;
		} else if (!bfsrc10.equals(other.bfsrc10) && bfsrc10.doubleValue() != (other.bfsrc10.doubleValue()))
			return false;
		if (bfsrc11 == null) {
			if (other.bfsrc11 != null)
				return false;
		} else if (!bfsrc11.equals(other.bfsrc11) && bfsrc11.doubleValue() != (other.bfsrc11.doubleValue()))
			return false;
		if (bfsrc12 == null) {
			if (other.bfsrc12 != null)
				return false;
		} else if (!bfsrc12.equals(other.bfsrc12) && bfsrc12.doubleValue() != (other.bfsrc12.doubleValue()))
			return false;
		if (bfsrc13 == null) {
			if (other.bfsrc13 != null)
				return false;
		} else if (!bfsrc13.equals(other.bfsrc13) && bfsrc13.doubleValue() != (other.bfsrc13.doubleValue()))
			return false;
		if (bfsrc2 == null) {
			if (other.bfsrc2 != null)
				return false;
		} else if (!bfsrc2.equals(other.bfsrc2) && bfsrc2.doubleValue() != (other.bfsrc2.doubleValue()))
			return false;
		if (bfsrc3 == null) {
			if (other.bfsrc3 != null)
				return false;
		} else if (!bfsrc3.equals(other.bfsrc3) && bfsrc3.doubleValue() != (other.bfsrc3.doubleValue()))
			return false;
		if (bfsrc4 == null) {
			if (other.bfsrc4 != null)
				return false;
		} else if (!bfsrc4.equals(other.bfsrc4) && bfsrc4.doubleValue() != (other.bfsrc4.doubleValue()))
			return false;
		if (bfsrc5 == null) {
			if (other.bfsrc5 != null)
				return false;
		} else if (!bfsrc5.equals(other.bfsrc5) && bfsrc5.doubleValue() != (other.bfsrc5.doubleValue()))
			return false;
		if (bfsrc6 == null) {
			if (other.bfsrc6 != null)
				return false;
		} else if (!bfsrc6.equals(other.bfsrc6) && bfsrc6.doubleValue() != (other.bfsrc6.doubleValue()))
			return false;
		if (bfsrc7 == null) {
			if (other.bfsrc7 != null)
				return false;
		} else if (!bfsrc7.equals(other.bfsrc7) && bfsrc7.doubleValue() != (other.bfsrc7.doubleValue()))
			return false;
		if (bfsrc8 == null) {
			if (other.bfsrc8 != null)
				return false;
		} else if (!bfsrc8.equals(other.bfsrc8) && bfsrc8.doubleValue() != (other.bfsrc8.doubleValue()))
			return false;
		if (bfsrc9 == null) {
			if (other.bfsrc9 != null)
				return false;
		} else if (!bfsrc9.equals(other.bfsrc9) && bfsrc9.doubleValue() != (other.bfsrc9.doubleValue()))
			return false;
		return true;
	}

}
