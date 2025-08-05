package org.ace.accounting.system.chartaccount;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Embeddable;

@Embeddable
public class MSrcRate implements Serializable{

	private static final long serialVersionUID = 1L;

	private BigDecimal msrc1;

	private BigDecimal msrc2;

	private BigDecimal msrc3;

	private BigDecimal msrc4;

	private BigDecimal msrc5;

	private BigDecimal msrc6;

	private BigDecimal msrc7;

	private BigDecimal msrc8;

	private BigDecimal msrc9;

	private BigDecimal msrc10;

	private BigDecimal msrc11;

	private BigDecimal msrc12;

	private BigDecimal msrc13;

	public MSrcRate() {
		msrc1 = BigDecimal.ZERO;
		msrc2 = BigDecimal.ZERO;
		msrc3 = BigDecimal.ZERO;
		msrc4 = BigDecimal.ZERO;
		msrc5 = BigDecimal.ZERO;
		msrc6 = BigDecimal.ZERO;
		msrc7 = BigDecimal.ZERO;
		msrc8 = BigDecimal.ZERO;
		msrc9 = BigDecimal.ZERO;
		msrc10 = BigDecimal.ZERO;
		msrc11 = BigDecimal.ZERO;
		msrc12 = BigDecimal.ZERO;
		msrc13 = BigDecimal.ZERO;
	}

	public BigDecimal getMsrc1() {
		return msrc1;
	}

	public void setMsrc1(BigDecimal msrc1) {
		this.msrc1 = msrc1;
	}

	public BigDecimal getMsrc2() {
		return msrc2;
	}

	public void setMsrc2(BigDecimal msrc2) {
		this.msrc2 = msrc2;
	}

	public BigDecimal getMsrc3() {
		return msrc3;
	}

	public void setMsrc3(BigDecimal msrc3) {
		this.msrc3 = msrc3;
	}

	public BigDecimal getMsrc4() {
		return msrc4;
	}

	public void setMsrc4(BigDecimal msrc4) {
		this.msrc4 = msrc4;
	}

	public BigDecimal getMsrc5() {
		return msrc5;
	}

	public void setMsrc5(BigDecimal msrc5) {
		this.msrc5 = msrc5;
	}

	public BigDecimal getMsrc6() {
		return msrc6;
	}

	public void setMsrc6(BigDecimal msrc6) {
		this.msrc6 = msrc6;
	}

	public BigDecimal getMsrc7() {
		return msrc7;
	}

	public void setMsrc7(BigDecimal msrc7) {
		this.msrc7 = msrc7;
	}

	public BigDecimal getMsrc8() {
		return msrc8;
	}

	public void setMsrc8(BigDecimal msrc8) {
		this.msrc8 = msrc8;
	}

	public BigDecimal getMsrc9() {
		return msrc9;
	}

	public void setMsrc9(BigDecimal msrc9) {
		this.msrc9 = msrc9;
	}

	public BigDecimal getMsrc10() {
		return msrc10;
	}

	public void setMsrc10(BigDecimal msrc10) {
		this.msrc10 = msrc10;
	}

	public BigDecimal getMsrc11() {
		return msrc11;
	}

	public void setMsrc11(BigDecimal msrc11) {
		this.msrc11 = msrc11;
	}

	public BigDecimal getMsrc12() {
		return msrc12;
	}

	public void setMsrc12(BigDecimal msrc12) {
		this.msrc12 = msrc12;
	}

	public BigDecimal getMsrc13() {
		return msrc13;
	}

	public void setMsrc13(BigDecimal msrc13) {
		this.msrc13 = msrc13;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((msrc1 == null) ? 0 : msrc1.hashCode());
		result = prime * result + ((msrc10 == null) ? 0 : msrc10.hashCode());
		result = prime * result + ((msrc11 == null) ? 0 : msrc11.hashCode());
		result = prime * result + ((msrc12 == null) ? 0 : msrc12.hashCode());
		result = prime * result + ((msrc13 == null) ? 0 : msrc13.hashCode());
		result = prime * result + ((msrc2 == null) ? 0 : msrc2.hashCode());
		result = prime * result + ((msrc3 == null) ? 0 : msrc3.hashCode());
		result = prime * result + ((msrc4 == null) ? 0 : msrc4.hashCode());
		result = prime * result + ((msrc5 == null) ? 0 : msrc5.hashCode());
		result = prime * result + ((msrc6 == null) ? 0 : msrc6.hashCode());
		result = prime * result + ((msrc7 == null) ? 0 : msrc7.hashCode());
		result = prime * result + ((msrc8 == null) ? 0 : msrc8.hashCode());
		result = prime * result + ((msrc9 == null) ? 0 : msrc9.hashCode());
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
		MSrcRate other = (MSrcRate) obj;
		if (msrc1 == null) {
			if (other.msrc1 != null)
				return false;
		} else if (!msrc1.equals(other.msrc1))
			return false;
		if (msrc10 == null) {
			if (other.msrc10 != null)
				return false;
		} else if (!msrc10.equals(other.msrc10))
			return false;
		if (msrc11 == null) {
			if (other.msrc11 != null)
				return false;
		} else if (!msrc11.equals(other.msrc11))
			return false;
		if (msrc12 == null) {
			if (other.msrc12 != null)
				return false;
		} else if (!msrc12.equals(other.msrc12))
			return false;
		if (msrc13 == null) {
			if (other.msrc13 != null)
				return false;
		} else if (!msrc13.equals(other.msrc13))
			return false;
		if (msrc2 == null) {
			if (other.msrc2 != null)
				return false;
		} else if (!msrc2.equals(other.msrc2))
			return false;
		if (msrc3 == null) {
			if (other.msrc3 != null)
				return false;
		} else if (!msrc3.equals(other.msrc3))
			return false;
		if (msrc4 == null) {
			if (other.msrc4 != null)
				return false;
		} else if (!msrc4.equals(other.msrc4))
			return false;
		if (msrc5 == null) {
			if (other.msrc5 != null)
				return false;
		} else if (!msrc5.equals(other.msrc5))
			return false;
		if (msrc6 == null) {
			if (other.msrc6 != null)
				return false;
		} else if (!msrc6.equals(other.msrc6))
			return false;
		if (msrc7 == null) {
			if (other.msrc7 != null)
				return false;
		} else if (!msrc7.equals(other.msrc7))
			return false;
		if (msrc8 == null) {
			if (other.msrc8 != null)
				return false;
		} else if (!msrc8.equals(other.msrc8))
			return false;
		if (msrc9 == null) {
			if (other.msrc9 != null)
				return false;
		} else if (!msrc9.equals(other.msrc9))
			return false;
		return true;
	}

	public void setAllZeros() {
		msrc1 = BigDecimal.ZERO;
		msrc2 = BigDecimal.ZERO;
		msrc3 = BigDecimal.ZERO;
		msrc4 = BigDecimal.ZERO;
		msrc5 = BigDecimal.ZERO;
		msrc6 = BigDecimal.ZERO;
		msrc7 = BigDecimal.ZERO;
		msrc8 = BigDecimal.ZERO;
		msrc9 = BigDecimal.ZERO;
		msrc10 = BigDecimal.ZERO;
		msrc11 = BigDecimal.ZERO;
		msrc12 = BigDecimal.ZERO;
		msrc13 = BigDecimal.ZERO;
	}

	public void setAllZerosExceptMsrc1() {
		msrc2 = BigDecimal.ZERO;
		msrc3 = BigDecimal.ZERO;
		msrc4 = BigDecimal.ZERO;
		msrc5 = BigDecimal.ZERO;
		msrc6 = BigDecimal.ZERO;
		msrc7 = BigDecimal.ZERO;
		msrc8 = BigDecimal.ZERO;
		msrc9 = BigDecimal.ZERO;
		msrc10 = BigDecimal.ZERO;
		msrc11 = BigDecimal.ZERO;
		msrc12 = BigDecimal.ZERO;
		msrc13 = BigDecimal.ZERO;
	}
}
