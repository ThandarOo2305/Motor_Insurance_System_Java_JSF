package org.ace.accounting.system.chartaccount;

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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.TableName;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.department.Department;
import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.CCOA)
@TableGenerator(name = "CCOA_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "CCOA_GEN", allocationSize = 10)
@NamedQueries(value = { @NamedQuery(name = "CurrencyChartOfAccount.findAll", query = "SELECT c FROM CurrencyChartOfAccount c ORDER BY c.coa.acCode,c.currency.currencyCode ASC"),
		@NamedQuery(name = "CurrencyChartOfAccount.findCCOAByCOAid", query = "SELECT c FROM CurrencyChartOfAccount c where c.coa.id=:id"),
		@NamedQuery(name = "CurrencyChartOfAccount.findCOAByBranchID", query = "SELECT DISTINCT NEW org.ace.accounting.system.chartaccount.CurrencyChartOfAccount(c.coa) FROM CurrencyChartOfAccount c WHERE c.branch.id=:branchId and c.coa.acCodeType != org.ace.accounting.system.chartaccount.AccountCodeType.HEAD AND c.coa.acCodeType != org.ace.accounting.system.chartaccount.AccountCodeType.GROUP "),
		@NamedQuery(name = "CurrencyChartOfAccount.findByAcCodeAndBranchId", query = "SELECT c FROM CurrencyChartOfAccount c WHERE c.coa.acCode=:acCode AND c.branch.id=:branchId ORDER BY c.currency.currencyCode ASC"),
		@NamedQuery(name = "CurrencyChartOfAccount.updateBudgetYearByID", query = "UPDATE CurrencyChartOfAccount c SET c.bF=:bF WHERE c.id=:id"),
		@NamedQuery(name = "CurrencyChartOfAccount.findCCOAByIdAndBranch", query = "Select ccoa from CurrencyChartOfAccount ccoa WHERE ccoa.coa.id=:coaId AND ccoa.branch=:branch AND ccoa.currency=:currency "),
		@NamedQuery(name = "CurrencyChartOfAccount.findAllBudget", query = "Select distinct ccoa.budget from CurrencyChartOfAccount ccoa WHERE ccoa.budget IS NOT NULL UNION ALL Select distinct ccoahist.budget from CcoaHistory ccoahist WHERE ccoahist.budget IS NOT NULL"),
		@NamedQuery(name = "CurrencyChartOfAccount.findCCOAByCurrencyID", query = "SELECT c FROM CurrencyChartOfAccount c where c.currency.id=:curid"),
		@NamedQuery(name = "CurrencyChartOfAccount.findCCOAByBranchID", query = "SELECT c FROM CurrencyChartOfAccount c where c.branch.id=:branchid") ,
		@NamedQuery(name = "CurrencyChartOfAccount.updateByBranchId", query = "UPDATE CurrencyChartOfAccount c SET c.acName=:acName, c.department=:departmentId WHERE c.coa.acCode = :acCode AND c.branch.branchCode = :branchCode") })

@EntityListeners(IDInterceptor.class)
public class CurrencyChartOfAccount implements Serializable {
	private static final long serialVersionUID = -5230388877783166738L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CCOA_GEN")
	private String id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COAID", referencedColumnName = "ID")
	private ChartOfAccount coa;

	private String acName;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BRANCHID", referencedColumnName = "ID")
	private Branch branch;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPARTMENTID", referencedColumnName = "ID")
	private Department department;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CURRENCYID", referencedColumnName = "ID")
	private Currency currency;

	/*
	 * Budget Figure
	 */
	private BigDecimal bF;

	/*
	 * Opening Balance
	 */
	private BigDecimal oBal;

	/*
	 * Home Opening Balance
	 */
	private BigDecimal hOBal;

	/*
	 * Closing Balance
	 */
	private BigDecimal cBal;

	@Embedded
	private MonthlyRate monthlyRate;

	@Embedded
	private MSrcRate msrcRate;

	@Embedded
	private BfRate bfRate;

	@Embedded
	private BfSrcRate bfsrcRate;

	@Embedded
	private MrevRate mrevRate;

	@Embedded
	private LymSrcRate lymsrcRate;

	private BigDecimal sccrBal;

	private BigDecimal accrued;

	private String budget;

	@Version
	private int version;

	@Embedded
	private BasicEntity basicEntity;

	public CurrencyChartOfAccount() {
		bF = BigDecimal.ZERO;
		oBal = BigDecimal.ZERO;
		hOBal = BigDecimal.ZERO;
		cBal = BigDecimal.ZERO;
		sccrBal = BigDecimal.ZERO;
		accrued = BigDecimal.ZERO;
		monthlyRate = new MonthlyRate();
		msrcRate = new MSrcRate();
		bfRate = new BfRate();
		bfsrcRate = new BfSrcRate();
		mrevRate = new MrevRate();
		lymsrcRate = new LymSrcRate();
	}

	public CurrencyChartOfAccount(ChartOfAccount coa) {
		super();
		this.coa = coa;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ChartOfAccount getCoa() {
		return coa;
	}

	public void setCoa(ChartOfAccount coa) {
		this.coa = coa;
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

	public BigDecimal getbF() {
		return bF;
	}

	public void setbF(BigDecimal bF) {
		this.bF = bF;
	}

	public BigDecimal getoBal() {
		return oBal;
	}

	public void setoBal(BigDecimal oBal) {
		this.oBal = oBal;
	}

	public BigDecimal gethOBal() {
		return hOBal;
	}

	public void sethOBal(BigDecimal hOBal) {
		this.hOBal = hOBal;
	}

	public BigDecimal getcBal() {
		return cBal;
	}

	public void setcBal(BigDecimal cBal) {
		this.cBal = cBal;
	}

	public MonthlyRate getMonthlyRate() {
		return monthlyRate;
	}

	public void setMonthlyRate(MonthlyRate monthlyRate) {
		this.monthlyRate = monthlyRate;
	}

	public MSrcRate getMsrcRate() {
		return msrcRate;
	}

	public void setMsrcRate(MSrcRate msrcRate) {
		this.msrcRate = msrcRate;
	}

	public BfRate getBfRate() {
		return bfRate;
	}

	public void setBfRate(BfRate bfRate) {
		this.bfRate = bfRate;
	}

	public BfSrcRate getBfsrcRate() {
		return bfsrcRate;
	}

	public void setBfsrcRate(BfSrcRate bfsrcRate) {
		this.bfsrcRate = bfsrcRate;
	}

	public MrevRate getMrevRate() {
		return mrevRate;
	}

	public void setMrevRate(MrevRate mrevRate) {
		this.mrevRate = mrevRate;
	}

	public LymSrcRate getLymsrcRate() {
		return lymsrcRate;
	}

	public void setLymsrcRate(LymSrcRate lymsrcRate) {
		this.lymsrcRate = lymsrcRate;
	}

	public BigDecimal getSccrBal() {
		return sccrBal;
	}

	public void setSccrBal(BigDecimal sccrBal) {
		this.sccrBal = sccrBal;
	}

	public BigDecimal getAccrued() {
		return accrued;
	}

	public void setAccrued(BigDecimal accrued) {
		this.accrued = accrued;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
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
		result = prime * result + ((accrued == null) ? 0 : accrued.hashCode());
		result = prime * result + ((bF == null) ? 0 : bF.hashCode());
		result = prime * result + ((basicEntity == null) ? 0 : basicEntity.hashCode());
		result = prime * result + ((bfRate == null) ? 0 : bfRate.hashCode());
		result = prime * result + ((bfsrcRate == null) ? 0 : bfsrcRate.hashCode());
		result = prime * result + ((branch == null) ? 0 : branch.hashCode());
		result = prime * result + ((budget == null) ? 0 : budget.hashCode());
		result = prime * result + ((cBal == null) ? 0 : cBal.hashCode());
		result = prime * result + ((coa == null) ? 0 : coa.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((hOBal == null) ? 0 : hOBal.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lymsrcRate == null) ? 0 : lymsrcRate.hashCode());
		result = prime * result + ((monthlyRate == null) ? 0 : monthlyRate.hashCode());
		result = prime * result + ((mrevRate == null) ? 0 : mrevRate.hashCode());
		result = prime * result + ((msrcRate == null) ? 0 : msrcRate.hashCode());
		result = prime * result + ((oBal == null) ? 0 : oBal.hashCode());
		result = prime * result + ((sccrBal == null) ? 0 : sccrBal.hashCode());
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
		CurrencyChartOfAccount other = (CurrencyChartOfAccount) obj;
		if (acName == null) {
			if (other.acName != null)
				return false;
		} else if (!acName.equals(other.acName))
			return false;
		if (accrued == null) {
			if (other.accrued != null)
				return false;
		} else if (!accrued.equals(other.accrued))
			return false;
		if (bF == null) {
			if (other.bF != null)
				return false;
		} else if (!bF.equals(other.bF))
			return false;
		if (basicEntity == null) {
			if (other.basicEntity != null)
				return false;
		} else if (!basicEntity.equals(other.basicEntity))
			return false;
		if (bfRate == null) {
			if (other.bfRate != null)
				return false;
		} else if (!bfRate.equals(other.bfRate))
			return false;
		if (bfsrcRate == null) {
			if (other.bfsrcRate != null)
				return false;
		} else if (!bfsrcRate.equals(other.bfsrcRate))
			return false;
		if (branch == null) {
			if (other.branch != null)
				return false;
		} else if (!branch.equals(other.branch))
			return false;
		if (budget == null) {
			if (other.budget != null)
				return false;
		} else if (!budget.equals(other.budget))
			return false;
		if (cBal == null) {
			if (other.cBal != null)
				return false;
		} else if (!cBal.equals(other.cBal))
			return false;
		if (coa == null) {
			if (other.coa != null)
				return false;
		} else if (!coa.equals(other.coa))
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
		if (hOBal == null) {
			if (other.hOBal != null)
				return false;
		} else if (!hOBal.equals(other.hOBal))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lymsrcRate == null) {
			if (other.lymsrcRate != null)
				return false;
		} else if (!lymsrcRate.equals(other.lymsrcRate))
			return false;
		if (monthlyRate == null) {
			if (other.monthlyRate != null)
				return false;
		} else if (!monthlyRate.equals(other.monthlyRate))
			return false;
		if (mrevRate == null) {
			if (other.mrevRate != null)
				return false;
		} else if (!mrevRate.equals(other.mrevRate))
			return false;
		if (msrcRate == null) {
			if (other.msrcRate != null)
				return false;
		} else if (!msrcRate.equals(other.msrcRate))
			return false;
		if (oBal == null) {
			if (other.oBal != null)
				return false;
		} else if (!oBal.equals(other.oBal))
			return false;
		if (sccrBal == null) {
			if (other.sccrBal != null)
				return false;
		} else if (!sccrBal.equals(other.sccrBal))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

}