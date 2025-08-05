package org.ace.accounting.system.view.bankcash;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.ace.accounting.common.TableName;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;
import org.eclipse.persistence.annotations.ReadOnly;

@Entity
@Table(name = TableName.VW_BANKCASH)
@ReadOnly
public class VwBankCash {
	@Id
	private String id;

	private String acode;

	private String status;

	private BigDecimal localAmount;

	private BigDecimal homeAmount;

	@Temporal(TemporalType.TIMESTAMP)
	private Date settlementDate;

	@JoinColumn(name = "CURID", referencedColumnName = "ID")
	private Currency cur;

	@JoinColumn(name = "BRANCHID", referencedColumnName = "ID")
	private Branch branch;

	public VwBankCash() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAcode() {
		return acode;
	}

	public void setAcode(String acode) {
		this.acode = acode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getLocalAmount() {
		return localAmount;
	}

	public void setLocalAmount(BigDecimal localAmount) {
		this.localAmount = localAmount;
	}

	public BigDecimal getHomeAmount() {
		return homeAmount;
	}

	public void setHomeAmount(BigDecimal homeAmount) {
		this.homeAmount = homeAmount;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public Currency getCur() {
		return cur;
	}

	public void setCur(Currency cur) {
		this.cur = cur;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acode == null) ? 0 : acode.hashCode());
		result = prime * result + ((branch == null) ? 0 : branch.hashCode());
		result = prime * result + ((cur == null) ? 0 : cur.hashCode());
		result = prime * result + ((homeAmount == null) ? 0 : homeAmount.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((localAmount == null) ? 0 : localAmount.hashCode());
		result = prime * result + ((settlementDate == null) ? 0 : settlementDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		VwBankCash other = (VwBankCash) obj;
		if (acode == null) {
			if (other.acode != null)
				return false;
		} else if (!acode.equals(other.acode))
			return false;
		if (branch == null) {
			if (other.branch != null)
				return false;
		} else if (!branch.equals(other.branch))
			return false;
		if (cur == null) {
			if (other.cur != null)
				return false;
		} else if (!cur.equals(other.cur))
			return false;
		if (homeAmount == null) {
			if (other.homeAmount != null)
				return false;
		} else if (!homeAmount.equals(other.homeAmount))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (localAmount == null) {
			if (other.localAmount != null)
				return false;
		} else if (!localAmount.equals(other.localAmount))
			return false;
		if (settlementDate == null) {
			if (other.settlementDate != null)
				return false;
		} else if (!settlementDate.equals(other.settlementDate))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

}
