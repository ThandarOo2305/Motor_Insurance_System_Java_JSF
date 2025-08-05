package org.ace.accounting.dto;

import java.math.BigDecimal;

/**************************************************************************
 * $21-04-2016$ $Thaw Oo Khaing$ $1$ ACEPLUS SOLUTIONS CO., Ltd.
 *************************************************************************/

public class BankCashDto {
	private String acCode;
	private String acName;
	private BigDecimal debit = BigDecimal.ZERO;
	private BigDecimal credit = BigDecimal.ZERO;
	private String accountCode;

	public BankCashDto(String acCode, String acName, BigDecimal debit, BigDecimal credit) {
		this.acCode = acCode;
		this.acName = acName;
		this.debit = debit;
		this.credit = credit;
	}

	public BankCashDto(String acCode, String acName, BigDecimal debit, BigDecimal credit, String accountCode) {
		this.acCode = acCode;
		this.acName = acName;
		this.debit = debit;
		this.credit = credit;
		this.accountCode = accountCode;
	}

	public String getAcCode() {
		return acCode;
	}

	public String getAcName() {
		return acName;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acCode == null) ? 0 : acCode.hashCode());
		result = prime * result + ((acName == null) ? 0 : acName.hashCode());
		result = prime * result + ((credit == null) ? 0 : credit.hashCode());
		result = prime * result + ((debit == null) ? 0 : debit.hashCode());
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
		BankCashDto other = (BankCashDto) obj;
		if (acCode == null) {
			if (other.acCode != null)
				return false;
		} else if (!acCode.equals(other.acCode))
			return false;
		if (acName == null) {
			if (other.acName != null)
				return false;
		} else if (!acName.equals(other.acName))
			return false;
		if (credit == null) {
			if (other.credit != null)
				return false;
		} else if (!credit.equals(other.credit))
			return false;
		if (debit == null) {
			if (other.debit != null)
				return false;
		} else if (!debit.equals(other.debit))
			return false;
		return true;
	}

}
