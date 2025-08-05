package org.ace.accounting.system.tlf;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.ace.accounting.system.chartaccount.AccountType;
import org.eclipse.persistence.annotations.ReadOnly;

@Entity
@Table(name = "VW_CASHBOOK")
@ReadOnly
public class VwCashbook {
	@Id
	private String contraCode;

	private String contraName;

	private String acode;

	private String acname;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date settlementDate;

	@Enumerated(EnumType.STRING)
	private AccountType acType;

	private String desp;

	private String currencyID;

	private BigDecimal credit;

	private BigDecimal debit;

	private BigDecimal homeCredit;

	private BigDecimal homeDebit;

	private String branchID;

	public VwCashbook() {
	}

	public String getContraCode() {
		return contraCode;
	}

	public String getContraName() {
		return contraName;
	}

	public String getAcode() {
		return acode;
	}

	public String getAcname() {
		return acname;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public AccountType getAcType() {
		return acType;
	}

	public String getDesp() {
		return desp;
	}

	public String getCurrencyID() {
		return currencyID;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public BigDecimal getHomeCredit() {
		return homeCredit;
	}

	public BigDecimal getHomeDebit() {
		return homeDebit;
	}

	public String getBranchID() {
		return branchID;
	}

	public void setBranchID(String branchID) {
		this.branchID = branchID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acType == null) ? 0 : acType.hashCode());
		result = prime * result + ((acname == null) ? 0 : acname.hashCode());
		result = prime * result + ((acode == null) ? 0 : acode.hashCode());
		result = prime * result + ((branchID == null) ? 0 : branchID.hashCode());
		result = prime * result + ((contraCode == null) ? 0 : contraCode.hashCode());
		result = prime * result + ((contraName == null) ? 0 : contraName.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((credit == null) ? 0 : credit.hashCode());
		result = prime * result + ((currencyID == null) ? 0 : currencyID.hashCode());
		result = prime * result + ((debit == null) ? 0 : debit.hashCode());
		result = prime * result + ((desp == null) ? 0 : desp.hashCode());
		result = prime * result + ((homeCredit == null) ? 0 : homeCredit.hashCode());
		result = prime * result + ((homeDebit == null) ? 0 : homeDebit.hashCode());
		result = prime * result + ((settlementDate == null) ? 0 : settlementDate.hashCode());
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
		VwCashbook other = (VwCashbook) obj;
		if (acType != other.acType)
			return false;
		if (acname == null) {
			if (other.acname != null)
				return false;
		} else if (!acname.equals(other.acname))
			return false;
		if (acode == null) {
			if (other.acode != null)
				return false;
		} else if (!acode.equals(other.acode))
			return false;
		if (branchID == null) {
			if (other.branchID != null)
				return false;
		} else if (!branchID.equals(other.branchID))
			return false;
		if (contraCode == null) {
			if (other.contraCode != null)
				return false;
		} else if (!contraCode.equals(other.contraCode))
			return false;
		if (contraName == null) {
			if (other.contraName != null)
				return false;
		} else if (!contraName.equals(other.contraName))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (credit == null) {
			if (other.credit != null)
				return false;
		} else if (!credit.equals(other.credit))
			return false;
		if (currencyID == null) {
			if (other.currencyID != null)
				return false;
		} else if (!currencyID.equals(other.currencyID))
			return false;
		if (debit == null) {
			if (other.debit != null)
				return false;
		} else if (!debit.equals(other.debit))
			return false;
		if (desp == null) {
			if (other.desp != null)
				return false;
		} else if (!desp.equals(other.desp))
			return false;
		if (homeCredit == null) {
			if (other.homeCredit != null)
				return false;
		} else if (!homeCredit.equals(other.homeCredit))
			return false;
		if (homeDebit == null) {
			if (other.homeDebit != null)
				return false;
		} else if (!homeDebit.equals(other.homeDebit))
			return false;
		if (settlementDate == null) {
			if (other.settlementDate != null)
				return false;
		} else if (!settlementDate.equals(other.settlementDate))
			return false;
		return true;
	}

	
}
