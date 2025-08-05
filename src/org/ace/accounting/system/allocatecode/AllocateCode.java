package org.ace.accounting.system.allocatecode;

import java.io.Serializable;
import java.math.BigDecimal;

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

import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.department.Department;
import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.ALLOCATECODE)
@TableGenerator(name = "ALLOCATECODE_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "ALLOCATECODE_GEN", allocationSize = 10)
@NamedQueries(value = { @NamedQuery(name = "AllocateCode.findAll", query = "SELECT a FROM AllocateCode a ORDER BY a.budget DESC"),
		@NamedQuery(name = "AllocateCode.findAllocateCodeByBudgetYear", query = "SELECT DISTINCT NEW org.ace.accounting.system.allocatecode.AllocateCode(a.budget,a.department,a.amtPercent,a.basedOn) FROM AllocateCode a WHERE a.budget=:budget ORDER BY a.budget DESC"),
		@NamedQuery(name = "AllocateCode.findCoaIDBy", query = "SELECT distinct(a.chartOfAccount.id) FROM AllocateCode a WHERE a.budget=:budget"),
		@NamedQuery(name = "AllocateCode.deleteAllocateCodeByBudgetYear", query = "DELETE FROM AllocateCode a WHERE a.budget=:budget") })
@EntityListeners(IDInterceptor.class)
public class AllocateCode implements Serializable {
	private static final long serialVersionUID = -38073014967635774L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ALLOCATECODE_GEN")
	private String id;

	private String budget;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPARTMENTID", referencedColumnName = "ID")
	private Department department;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COAID", referencedColumnName = "ID")
	private ChartOfAccount chartOfAccount;

	private BigDecimal amtPercent;

	private BigDecimal basedOn;

	@Version
	private int version;

	@Embedded
	private BasicEntity basicEntity;

	public AllocateCode() {
	}

	public AllocateCode(String budget, Department department, ChartOfAccount chartOfAccount, BigDecimal amtPercent, BigDecimal basedOn) {
		super();
		this.budget = budget;
		this.department = department;
		this.chartOfAccount = chartOfAccount;
		this.amtPercent = amtPercent;
		this.basedOn = basedOn;
	}

	public AllocateCode(String budget, Department department, BigDecimal amtPercent, BigDecimal basedOn) {
		super();
		this.budget = budget;
		this.department = department;
		this.amtPercent = amtPercent;
		this.basedOn = basedOn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public ChartOfAccount getChartOfAccount() {
		if (chartOfAccount == null) {
			chartOfAccount = new ChartOfAccount();
		}
		return chartOfAccount;
	}

	public void setChartOfAccount(ChartOfAccount chartOfAccount) {
		this.chartOfAccount = chartOfAccount;
	}

	public BigDecimal getAmtPercent() {
		return amtPercent;
	}

	public void setAmtPercent(BigDecimal amtPercent) {
		this.amtPercent = amtPercent;
	}

	public BigDecimal getBasedOn() {
		return basedOn;
	}

	public void setBasedOn(BigDecimal basedOn) {
		this.basedOn = basedOn;
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
		result = prime * result + ((amtPercent == null) ? 0 : amtPercent.hashCode());
		result = prime * result + ((basedOn == null) ? 0 : basedOn.hashCode());
		result = prime * result + ((basicEntity == null) ? 0 : basicEntity.hashCode());
		result = prime * result + ((budget == null) ? 0 : budget.hashCode());
		result = prime * result + ((chartOfAccount == null) ? 0 : chartOfAccount.hashCode());
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
		AllocateCode other = (AllocateCode) obj;
		if (amtPercent == null) {
			if (other.amtPercent != null)
				return false;
		} else if (!amtPercent.equals(other.amtPercent))
			return false;
		if (basedOn == null) {
			if (other.basedOn != null)
				return false;
		} else if (!basedOn.equals(other.basedOn))
			return false;
		if (basicEntity == null) {
			if (other.basicEntity != null)
				return false;
		} else if (!basicEntity.equals(other.basicEntity))
			return false;
		if (budget == null) {
			if (other.budget != null)
				return false;
		} else if (!budget.equals(other.budget))
			return false;
		if (chartOfAccount == null) {
			if (other.chartOfAccount != null)
				return false;
		} else if (!chartOfAccount.equals(other.chartOfAccount))
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