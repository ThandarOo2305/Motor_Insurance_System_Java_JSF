package org.ace.accounting.system.chartaccount;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Embeddable;

@Embeddable
public class LymSrcRate implements Serializable{

	private static final long serialVersionUID = 1L;

	private BigDecimal lymsrc1;

	private BigDecimal lymsrc2;

	private BigDecimal lymsrc3;

	private BigDecimal lymsrc4;

	private BigDecimal lymsrc5;

	private BigDecimal lymsrc6;

	private BigDecimal lymsrc7;

	private BigDecimal lymsrc8;

	private BigDecimal lymsrc9;

	private BigDecimal lymsrc10;

	private BigDecimal lymsrc11;

	private BigDecimal lymsrc12;

	private BigDecimal lymsrc13;

	public LymSrcRate() {
		lymsrc1 = BigDecimal.ZERO;
		lymsrc2 = BigDecimal.ZERO;
		lymsrc3 = BigDecimal.ZERO;
		lymsrc4 = BigDecimal.ZERO;
		lymsrc5 = BigDecimal.ZERO;
		lymsrc6 = BigDecimal.ZERO;
		lymsrc7 = BigDecimal.ZERO;
		lymsrc8 = BigDecimal.ZERO;
		lymsrc9 = BigDecimal.ZERO;
		lymsrc10 = BigDecimal.ZERO;
		lymsrc11 = BigDecimal.ZERO;
		lymsrc12 = BigDecimal.ZERO;
		lymsrc13 = BigDecimal.ZERO;
	}

	public BigDecimal getLymsrc1() {
		return lymsrc1;
	}

	public void setLymsrc1(BigDecimal lymsrc1) {
		this.lymsrc1 = lymsrc1;
	}

	public BigDecimal getLymsrc2() {
		return lymsrc2;
	}

	public void setLymsrc2(BigDecimal lymsrc2) {
		this.lymsrc2 = lymsrc2;
	}

	public BigDecimal getLymsrc3() {
		return lymsrc3;
	}

	public void setLymsrc3(BigDecimal lymsrc3) {
		this.lymsrc3 = lymsrc3;
	}

	public BigDecimal getLymsrc4() {
		return lymsrc4;
	}

	public void setLymsrc4(BigDecimal lymsrc4) {
		this.lymsrc4 = lymsrc4;
	}

	public BigDecimal getLymsrc5() {
		return lymsrc5;
	}

	public void setLymsrc5(BigDecimal lymsrc5) {
		this.lymsrc5 = lymsrc5;
	}

	public BigDecimal getLymsrc6() {
		return lymsrc6;
	}

	public void setLymsrc6(BigDecimal lymsrc6) {
		this.lymsrc6 = lymsrc6;
	}

	public BigDecimal getLymsrc7() {
		return lymsrc7;
	}

	public void setLymsrc7(BigDecimal lymsrc7) {
		this.lymsrc7 = lymsrc7;
	}

	public BigDecimal getLymsrc8() {
		return lymsrc8;
	}

	public void setLymsrc8(BigDecimal lymsrc8) {
		this.lymsrc8 = lymsrc8;
	}

	public BigDecimal getLymsrc9() {
		return lymsrc9;
	}

	public void setLymsrc9(BigDecimal lymsrc9) {
		this.lymsrc9 = lymsrc9;
	}

	public BigDecimal getLymsrc10() {
		return lymsrc10;
	}

	public void setLymsrc10(BigDecimal lymsrc10) {
		this.lymsrc10 = lymsrc10;
	}

	public BigDecimal getLymsrc11() {
		return lymsrc11;
	}

	public void setLymsrc11(BigDecimal lymsrc11) {
		this.lymsrc11 = lymsrc11;
	}

	public BigDecimal getLymsrc12() {
		return lymsrc12;
	}

	public void setLymsrc12(BigDecimal lymsrc12) {
		this.lymsrc12 = lymsrc12;
	}

	public BigDecimal getLymsrc13() {
		return lymsrc13;
	}

	public void setLymsrc13(BigDecimal lymsrc13) {
		this.lymsrc13 = lymsrc13;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lymsrc1 == null) ? 0 : lymsrc1.hashCode());
		result = prime * result + ((lymsrc10 == null) ? 0 : lymsrc10.hashCode());
		result = prime * result + ((lymsrc11 == null) ? 0 : lymsrc11.hashCode());
		result = prime * result + ((lymsrc12 == null) ? 0 : lymsrc12.hashCode());
		result = prime * result + ((lymsrc13 == null) ? 0 : lymsrc13.hashCode());
		result = prime * result + ((lymsrc2 == null) ? 0 : lymsrc2.hashCode());
		result = prime * result + ((lymsrc3 == null) ? 0 : lymsrc3.hashCode());
		result = prime * result + ((lymsrc4 == null) ? 0 : lymsrc4.hashCode());
		result = prime * result + ((lymsrc5 == null) ? 0 : lymsrc5.hashCode());
		result = prime * result + ((lymsrc6 == null) ? 0 : lymsrc6.hashCode());
		result = prime * result + ((lymsrc7 == null) ? 0 : lymsrc7.hashCode());
		result = prime * result + ((lymsrc8 == null) ? 0 : lymsrc8.hashCode());
		result = prime * result + ((lymsrc9 == null) ? 0 : lymsrc9.hashCode());
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
		LymSrcRate other = (LymSrcRate) obj;
		if (lymsrc1 == null) {
			if (other.lymsrc1 != null)
				return false;
		} else if (!lymsrc1.equals(other.lymsrc1))
			return false;
		if (lymsrc10 == null) {
			if (other.lymsrc10 != null)
				return false;
		} else if (!lymsrc10.equals(other.lymsrc10))
			return false;
		if (lymsrc11 == null) {
			if (other.lymsrc11 != null)
				return false;
		} else if (!lymsrc11.equals(other.lymsrc11))
			return false;
		if (lymsrc12 == null) {
			if (other.lymsrc12 != null)
				return false;
		} else if (!lymsrc12.equals(other.lymsrc12))
			return false;
		if (lymsrc13 == null) {
			if (other.lymsrc13 != null)
				return false;
		} else if (!lymsrc13.equals(other.lymsrc13))
			return false;
		if (lymsrc2 == null) {
			if (other.lymsrc2 != null)
				return false;
		} else if (!lymsrc2.equals(other.lymsrc2))
			return false;
		if (lymsrc3 == null) {
			if (other.lymsrc3 != null)
				return false;
		} else if (!lymsrc3.equals(other.lymsrc3))
			return false;
		if (lymsrc4 == null) {
			if (other.lymsrc4 != null)
				return false;
		} else if (!lymsrc4.equals(other.lymsrc4))
			return false;
		if (lymsrc5 == null) {
			if (other.lymsrc5 != null)
				return false;
		} else if (!lymsrc5.equals(other.lymsrc5))
			return false;
		if (lymsrc6 == null) {
			if (other.lymsrc6 != null)
				return false;
		} else if (!lymsrc6.equals(other.lymsrc6))
			return false;
		if (lymsrc7 == null) {
			if (other.lymsrc7 != null)
				return false;
		} else if (!lymsrc7.equals(other.lymsrc7))
			return false;
		if (lymsrc8 == null) {
			if (other.lymsrc8 != null)
				return false;
		} else if (!lymsrc8.equals(other.lymsrc8))
			return false;
		if (lymsrc9 == null) {
			if (other.lymsrc9 != null)
				return false;
		} else if (!lymsrc9.equals(other.lymsrc9))
			return false;
		return true;
	}

	public void setAllZeros() {
		lymsrc1 = BigDecimal.ZERO;
		lymsrc2 = BigDecimal.ZERO;
		lymsrc3 = BigDecimal.ZERO;
		lymsrc4 = BigDecimal.ZERO;
		lymsrc5 = BigDecimal.ZERO;
		lymsrc6 = BigDecimal.ZERO;
		lymsrc7 = BigDecimal.ZERO;
		lymsrc8 = BigDecimal.ZERO;
		lymsrc9 = BigDecimal.ZERO;
		lymsrc10 = BigDecimal.ZERO;
		lymsrc11 = BigDecimal.ZERO;
		lymsrc12 = BigDecimal.ZERO;
		lymsrc13 = BigDecimal.ZERO;
	}
}
