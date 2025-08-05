package org.ace.accounting.system.tlf;

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
import org.ace.accounting.dto.EditVoucherDto;
import org.ace.accounting.dto.JVdto;
import org.ace.accounting.dto.VoucherDTO;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.department.Department;
import org.ace.accounting.system.trantype.TranType;
import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.TLF)
@TableGenerator(name = "TLF_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "TLF_GEN", allocationSize = 10)
@NamedQueries(value = { @NamedQuery(name = "TLF.findAll", query = "SELECT t FROM TLF t"),
        @NamedQuery(name = "TLF.findVoucherNoByBranchId", query = "SELECT DISTINCT NEW java.lang.String(t.eNo)  FROM TLF t WHERE t.branch.id=:branchId AND t.tranType.status LIKE :status AND t.reverse=0  AND t.eNo LIKE :eNo"),
        @NamedQuery(name = "TLF.findVoucherListByVoucherNo", query = "SELECT DISTINCT t FROM TLF t, CurrencyChartOfAccount c WHERE "
                + "t.eNo=:voucherNo AND t.ccoa=c AND t.currency=c.currency AND " + "t.branch=c.branch AND t.reverse=0"),
        @NamedQuery(name = "TLF.updateReverseByID", query = "UPDATE TLF t SET t.reverse=:reverse WHERE t.id=:id"),
        @NamedQuery(name = "TLF.findCashAccountByVoucherNo", query = "SELECT NEW org.ace.accounting.dto.EditVoucherDto(t.tranType) from TLF t,COASetup c  WHERE t.eNo=:voucherNo AND t.ccoa = c.ccoa and c.acName=org.ace.accounting.common.VoucherType.CASH"), })

@EntityListeners(IDInterceptor.class)
public class TLF implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5685055807452264053L;

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

    @OneToOne(fetch = FetchType.LAZY, optional = true)
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

    public TLF() {
    }

    public TLF(TranType tranType) {
        super();
        this.tranType = tranType;
    }

    public TLF(String id, String customerId, CurrencyChartOfAccount ccoa, BigDecimal homeAmount, BigDecimal localAmount, Currency currency, String chequeNo, String bankId,
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

    public TLF(EditVoucherDto dto) {
        this.id = dto.getId();
        this.customerId = dto.getCustomerId();
        this.ccoa = dto.getCcoa();
        this.homeAmount = dto.getHomeAmount();
        this.localAmount = dto.getLocalAmount();
        this.currency = dto.getCurrency();
        this.chequeNo = dto.getChequeNo();
        this.bankId = dto.getBankId();
        this.purchaseOrderId = dto.getPurchaseOrderId();
        this.loanId = dto.getLoanId();
        this.narration = dto.getNarration();
        this.tranType = dto.getTranType();
        this.orgnTLFID = dto.getOrgnTLFID();
        this.branch = dto.getBranch();
        this.rate = dto.getRate();
        this.settlementDate = dto.getSettlementDate();
        this.reverse = dto.isReverse();
        this.referenceNo = dto.getReferenceNo();
        this.referenceType = dto.getReferenceType();
        this.eNo = dto.geteNo();
        this.paid = dto.isPaid();
        this.tlfNo = dto.getTlfNo();
        this.clearing = dto.isClearing();
        this.isRenewal = dto.isRenewal();

    }

    public TLF(VoucherDTO dto, String voucherNo, TranType tranType, Branch branch, boolean reverse, boolean paid, String referenceType, Date settlementDate) {
        this.ccoa = dto.getCcoa();
        this.eNo = voucherNo;
        this.homeAmount = dto.getHomeAmount();
        this.localAmount = dto.getLocalAmount();
        this.narration = dto.getNarration();
        this.tranType = tranType;
        this.currency = dto.getCurrency();
        this.rate = dto.getHomeExchangeRate();
        this.branch = branch;
        this.reverse = reverse;
        this.paid = paid;
        this.referenceType = referenceType;
        this.settlementDate = settlementDate;
    }

    public TLF(VoucherDTO dto, String voucherNo, TranType tranType, Branch branch, boolean reverse, boolean paid, String referenceType, Date settlementDate, Date createdDate,
            String createdUserId) {
        this.ccoa = dto.getCcoa();
        this.eNo = voucherNo;
        this.homeAmount = dto.getHomeAmount();
        this.localAmount = dto.getLocalAmount();
        this.narration = dto.getNarration();
        this.tranType = tranType;
        this.currency = dto.getCurrency();
        this.rate = dto.getHomeExchangeRate();
        this.branch = branch;
        this.reverse = reverse;
        this.paid = paid;
        this.referenceType = referenceType;
        this.settlementDate = settlementDate;
        if (null != createdDate) {
            this.basicEntity = new BasicEntity();
            this.basicEntity.setCreatedDate(createdDate);
            this.basicEntity.setCreatedUserId(createdUserId);
        }

    }

    public TLF(CurrencyChartOfAccount ccoa, String voucherNo, BigDecimal homeAmount, BigDecimal localAmount, String narration, TranType tranType, Currency currency,
            BigDecimal homeExchangeRate, Branch branch, boolean reverse, boolean paid, String referenceType, Date settlementDate) {
        this.ccoa = ccoa;
        this.eNo = voucherNo;
        this.homeAmount = homeAmount;
        this.localAmount = localAmount;
        this.narration = narration;
        this.tranType = tranType;
        this.currency = currency;
        this.rate = homeExchangeRate;
        this.branch = branch;
        this.reverse = reverse;
        this.paid = paid;
        this.referenceType = referenceType;
        this.settlementDate = settlementDate;
    }

    public TLF(CurrencyChartOfAccount ccoa, String voucherNo, BigDecimal homeAmount, BigDecimal localAmount, String narration, TranType tranType, Currency currency,
            BigDecimal homeExchangeRate, Branch branch, boolean reverse, Date settlementDate, String chequeNo) {
        this.ccoa = ccoa;
        this.eNo = voucherNo;
        this.homeAmount = homeAmount;
        this.localAmount = localAmount;
        this.narration = narration;
        this.tranType = tranType;
        this.currency = currency;
        this.rate = homeExchangeRate;
        this.branch = branch;
        this.reverse = reverse;
        this.settlementDate = settlementDate;
        this.chequeNo = chequeNo;
    }

    public TLF(String id, String customerId, CurrencyChartOfAccount ccoa, BigDecimal homeAmount, BigDecimal localAmount, Currency currency, String chequeNo, String bankId,
            String purchaseOrderId, String loanId, String narration, TranType tranType, String orgnTLFID, Branch branch, BigDecimal rate, Date settlementDate, boolean reverse,
            String referenceNo, String referenceType, String eNo, boolean paid, String tlfNo, boolean clearing, boolean isRenewal) {
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
    }

    public TLF(TLF tlf) {
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
        this.basicEntity = tlf.getBasicEntity();
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public CurrencyChartOfAccount getCcoa() {
        return ccoa;
    }

    public void setCcoa(CurrencyChartOfAccount ccoa) {
        this.ccoa = ccoa;
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
        result = prime * result + ((department == null) ? 0 : department.hashCode());
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
        TLF other = (TLF) obj;
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
        if (department == null) {
            if (other.department != null)
                return false;
        } else if (!department.equals(other.department))
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

    public TLF(JVdto dto) {
        this.localAmount = dto.getAmount();
        this.homeAmount = dto.getHomeAmount();
        if (dto.getChequeNo() != null && !dto.getChequeNo().isEmpty()) {
            this.chequeNo = dto.getChequeNo();
        }
        this.ccoa = dto.getCcoa();
        this.currency = dto.getCur();
        this.narration = dto.getNarration();
        this.tranType = new TranType();
        this.tranType.setTranCode(dto.getTranCode());
        this.settlementDate = dto.getSettlementDate();
        this.rate = dto.getExchangeRate();
    }

    public TLF(String eNo, CurrencyChartOfAccount ccoa, BigDecimal homeAmount, BigDecimal localAmount, Currency currency, String narration, TranType tranType, Branch branch,
            BigDecimal rate, Date settlementDate, boolean reverse, String referenceNo, String referenceType, boolean paid) {
        this.eNo = eNo;
        this.ccoa = ccoa;
        this.homeAmount = homeAmount;
        this.localAmount = localAmount;
        this.currency = currency;
        this.narration = narration;
        this.tranType = tranType;
        this.branch = branch;
        this.rate = rate;
        this.settlementDate = settlementDate;
        this.reverse = reverse;
        this.referenceNo = referenceNo;
        this.referenceType = referenceType;
        this.paid = paid;

    }
}
