package org.ace.accounting.system.tlfhist;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.TableName;

import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.department.Department;
import org.ace.accounting.system.tlf.TLF;
import org.ace.accounting.system.trantype.TranType;
import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.TLFHIST)
@TableGenerator(name = "TLFHIST_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "TLFHIST_GEN", allocationSize = 10)
@NamedQueries(value = { @NamedQuery(name = "TLFHIST.findAll", query = "SELECT t FROM TLFHIST t ORDER BY t.ccoa.coa.acCode ASC"),
		@NamedQuery(name = "TLFHIST.findVoucherListByVoucherNo", query = "SELECT DISTINCT NEW org.ace.accounting.system.tlf.TLF(t.id, t.customerId, "
				+ "t.ccoa,t.homeAmount, t.localAmount, t.currency,t.chequeNo, t.bankId, " + "t.purchaseOrderId, t.loanId, t.narration, t.tranType,t.orgnTLFID,"
				+ "t.branch, t.rate,t.settlementDate, t.reverse," + "t.referenceNo,t.referenceType, t.eNo, t.paid, t.tlfNo,t.clearing,"
				+ "t.isRenewal) FROM TLF t, CurrencyChartOfAccount c WHERE " + "t.eNo=:voucherNo AND t.ccoa=c AND t.currency=c.currency AND "
				+ "t.branch=c.branch AND t.reverse=0"), })
@EntityListeners(IDInterceptor.class)
public class TLFHIST implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 253892774779027266L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TLF_GEN")
	private String id;

	private String customerId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CCOAID", referencedColumnName = "ID")
	private CurrencyChartOfAccount ccoa;

	private BigDecimal homeAmount;

	private BigDecimal localAmount;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CURRENCYID", referencedColumnName = "ID")
	private Currency currency;

	private String chequeNo;

	private String bankId;

	private String purchaseOrderId;

	private String loanId;

	private String narration;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRANTYPEID", referencedColumnName = "ID")
	private TranType tranType;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPARTMENTID", referencedColumnName = "ID")
	private Department department;

	private String orgnTLFID;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BRANCHID", referencedColumnName = "ID")
	private Branch branch;

	private BigDecimal rate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date settlementDate;

	private boolean reverse;

	private String referenceNo;

	private String referenceType;

	private String eNo;

	private boolean paid;

	private String tlfNo;

	private boolean clearing;

	private boolean isRenewal;

	@Version
	private int version;

	@Embedded
	private BasicEntity basicEntity;

	public TLFHIST() {
	}

	public TLFHIST(String id, String customerId, CurrencyChartOfAccount ccoa, BigDecimal homeAmount, BigDecimal localAmount, Currency currency, String chequeNo, String bankId,
			String purchaseOrderId, String loanId, String narration, TranType tranType, Department department, String orgnTLFID, Branch branch, BigDecimal rate,
			Date settlementDate, boolean reverse, String referenceNo, String referenceType, String eNo, boolean paid, String tlfNo, boolean clearing, boolean isRenewal,
			int version, BasicEntity basicEntity) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.ccoa = ccoa;
		this.homeAmount = homeAmount;
		this.localAmount = localAmount;
		this.currency = currency;
		this.chequeNo = chequeNo;
		this.bankId = bankId;
		this.purchaseOrderId = purchaseOrderId;
		this.loanId = loanId;
		this.narration = narration;
		this.tranType = tranType;
		this.department = department;
		this.orgnTLFID = orgnTLFID;
		this.branch = branch;
		this.rate = rate;
		this.settlementDate = settlementDate;
		this.reverse = reverse;
		this.referenceNo = referenceNo;
		this.referenceType = referenceType;
		this.eNo = eNo;
		this.paid = paid;
		this.tlfNo = tlfNo;
		this.clearing = clearing;
		this.isRenewal = isRenewal;
		this.version = version;
		this.basicEntity = basicEntity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public CurrencyChartOfAccount getCcoa() {
		return ccoa;
	}

	public void setCcoa(CurrencyChartOfAccount ccoa) {
		this.ccoa = ccoa;
	}

	public BigDecimal getHomeAmount() {
		return homeAmount;
	}

	public void setHomeAmount(BigDecimal homeAmount) {
		this.homeAmount = homeAmount;
	}

	public BigDecimal getLocalAmount() {
		return localAmount;
	}

	public void setLocalAmount(BigDecimal localAmount) {
		this.localAmount = localAmount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(String purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public TranType getTranType() {
		return tranType;
	}

	public void setTranType(TranType tranType) {
		this.tranType = tranType;
	}

	public String getOrgnTLFID() {
		return orgnTLFID;
	}

	public void setOrgnTLFID(String orgnTLFID) {
		this.orgnTLFID = orgnTLFID;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}

	public String geteNo() {
		return eNo;
	}

	public void seteNo(String eNo) {
		this.eNo = eNo;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public String getTlfNo() {
		return tlfNo;
	}

	public void setTlfNo(String tlfNo) {
		this.tlfNo = tlfNo;
	}

	public boolean isClearing() {
		return clearing;
	}

	public void setClearing(boolean clearing) {
		this.clearing = clearing;
	}

	public boolean isRenewal() {
		return isRenewal;
	}

	public void setRenewal(boolean isRenewal) {
		this.isRenewal = isRenewal;
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
		result = prime * result + ((bankId == null) ? 0 : bankId.hashCode());
		result = prime * result + ((basicEntity == null) ? 0 : basicEntity.hashCode());
		result = prime * result + ((branch == null) ? 0 : branch.hashCode());
		result = prime * result + ((ccoa == null) ? 0 : ccoa.hashCode());
		result = prime * result + ((chequeNo == null) ? 0 : chequeNo.hashCode());
		result = prime * result + (clearing ? 1231 : 1237);
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((eNo == null) ? 0 : eNo.hashCode());
		result = prime * result + ((homeAmount == null) ? 0 : homeAmount.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (isRenewal ? 1231 : 1237);
		result = prime * result + ((loanId == null) ? 0 : loanId.hashCode());
		result = prime * result + ((localAmount == null) ? 0 : localAmount.hashCode());
		result = prime * result + ((narration == null) ? 0 : narration.hashCode());
		result = prime * result + ((orgnTLFID == null) ? 0 : orgnTLFID.hashCode());
		result = prime * result + (paid ? 1231 : 1237);
		result = prime * result + ((purchaseOrderId == null) ? 0 : purchaseOrderId.hashCode());
		result = prime * result + ((rate == null) ? 0 : rate.hashCode());
		result = prime * result + ((referenceNo == null) ? 0 : referenceNo.hashCode());
		result = prime * result + ((referenceType == null) ? 0 : referenceType.hashCode());
		result = prime * result + (reverse ? 1231 : 1237);
		result = prime * result + ((settlementDate == null) ? 0 : settlementDate.hashCode());
		result = prime * result + ((tlfNo == null) ? 0 : tlfNo.hashCode());
		result = prime * result + ((tranType == null) ? 0 : tranType.hashCode());
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
		TLFHIST other = (TLFHIST) obj;
		if (bankId == null) {
			if (other.bankId != null)
				return false;
		} else if (!bankId.equals(other.bankId))
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
		if (chequeNo == null) {
			if (other.chequeNo != null)
				return false;
		} else if (!chequeNo.equals(other.chequeNo))
			return false;
		if (clearing != other.clearing)
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (eNo == null) {
			if (other.eNo != null)
				return false;
		} else if (!eNo.equals(other.eNo))
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
		if (isRenewal != other.isRenewal)
			return false;
		if (loanId == null) {
			if (other.loanId != null)
				return false;
		} else if (!loanId.equals(other.loanId))
			return false;
		if (localAmount == null) {
			if (other.localAmount != null)
				return false;
		} else if (!localAmount.equals(other.localAmount))
			return false;
		if (narration == null) {
			if (other.narration != null)
				return false;
		} else if (!narration.equals(other.narration))
			return false;
		if (orgnTLFID == null) {
			if (other.orgnTLFID != null)
				return false;
		} else if (!orgnTLFID.equals(other.orgnTLFID))
			return false;
		if (paid != other.paid)
			return false;
		if (purchaseOrderId == null) {
			if (other.purchaseOrderId != null)
				return false;
		} else if (!purchaseOrderId.equals(other.purchaseOrderId))
			return false;
		if (rate == null) {
			if (other.rate != null)
				return false;
		} else if (!rate.equals(other.rate))
			return false;
		if (referenceNo == null) {
			if (other.referenceNo != null)
				return false;
		} else if (!referenceNo.equals(other.referenceNo))
			return false;
		if (referenceType == null) {
			if (other.referenceType != null)
				return false;
		} else if (!referenceType.equals(other.referenceType))
			return false;
		if (reverse != other.reverse)
			return false;
		if (settlementDate == null) {
			if (other.settlementDate != null)
				return false;
		} else if (!settlementDate.equals(other.settlementDate))
			return false;
		if (tlfNo == null) {
			if (other.tlfNo != null)
				return false;
		} else if (!tlfNo.equals(other.tlfNo))
			return false;
		if (tranType == null) {
			if (other.tranType != null)
				return false;
		} else if (!tranType.equals(other.tranType))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

	public TLFHIST(TLF tlf) {
		this.id = tlf.getId();
		this.customerId = tlf.getCustomerId();
		this.ccoa = tlf.getCcoa();
		this.homeAmount = tlf.getHomeAmount();
		this.localAmount = tlf.getLocalAmount();
		this.currency = tlf.getCurrency();
		this.chequeNo = tlf.getChequeNo();
		this.bankId = tlf.getBankId();
		this.purchaseOrderId = tlf.getPurchaseOrderId();
		this.loanId = tlf.getLoanId();
		this.narration = tlf.getNarration();
		this.tranType = tlf.getTranType();
		this.orgnTLFID = tlf.getOrgnTLFID();
		this.branch = tlf.getBranch();
		this.rate = tlf.getRate();
		this.settlementDate = tlf.getSettlementDate();
		this.reverse = tlf.isReverse();
		this.referenceNo = tlf.getReferenceNo();
		this.referenceType = tlf.getReferenceType();
		this.eNo = tlf.geteNo();
		this.paid = tlf.isPaid();
		this.tlfNo = tlf.getTlfNo();
		this.clearing = tlf.isClearing();
		this.isRenewal = tlf.isRenewal();
		this.version = tlf.getVersion();
		this.basicEntity = tlf.getBasicEntity();
	}
}
