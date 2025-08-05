package org.ace.accounting.system.coasetup;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.TableName;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.department.Department;
import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.COASETUP)
@TableGenerator(name = "COASETUP_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "COASETUP_GEN", allocationSize = 10)
@NamedQueries(value = { @NamedQuery(name = "COASetup.findAcnameByAcNameBranchCode", query = "SELECT c FROM COASetup c") })
@NamedQuery(name = "COASetup.findAllCashAccount", query = "SELECT c from COASetup c  WHERE c.acName=org.ace.accounting.common.VoucherType.CASH")
@EntityListeners(IDInterceptor.class)
public class COASetup implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -157733738940063961L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "COASETUP_GEN")
	private String id;

	private String acName;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CCOAID", referencedColumnName = "ID")
	private CurrencyChartOfAccount ccoa;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BRANCHID", referencedColumnName = "ID")
	private Branch branch;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPARTMENTID", referencedColumnName = "ID")
	private Department department;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CURRENCYID", referencedColumnName = "ID")
	private Currency currency;

	@Version
	private int version;

	@Embedded
	private BasicEntity basicEntity;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CurrencyChartOfAccount getCcoa() {
		return ccoa;
	}

	public void setCcoa(CurrencyChartOfAccount ccoa) {
		this.ccoa = ccoa;
	}

	public String getAcName() {
		return acName;
	}

	public void setAcName(String acName) {
		this.acName = acName;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public BasicEntity getBasicEntity() {
		return basicEntity;
	}

	public void setBasicEntity(BasicEntity basicEntity) {
		this.basicEntity = basicEntity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acName == null) ? 0 : acName.hashCode());
		result = prime * result + ((basicEntity == null) ? 0 : basicEntity.hashCode());
		result = prime * result + ((branch == null) ? 0 : branch.hashCode());
		result = prime * result + ((ccoa == null) ? 0 : ccoa.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + version;
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
		COASetup other = (COASetup) obj;
		if (acName == null) {
			if (other.acName != null)
				return false;
		} else if (!acName.equals(other.acName))
			return false;
		if (basicEntity == null) {
			if (other.basicEntity != null)
				return false;
		} else if (!basicEntity.equals(other.basicEntity))
			return false;
		if (branch == null) {
			if (other.branch != null)
				return false;
		} else if (!branch.equals(other.branch))
			return false;
		if (ccoa == null) {
			if (other.ccoa != null)
				return false;
		} else if (!ccoa.equals(other.ccoa))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

}
