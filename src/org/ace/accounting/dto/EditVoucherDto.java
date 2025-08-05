package org.ace.accounting.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.trantype.TranType;

public class EditVoucherDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String customerId;

	private CurrencyChartOfAccount ccoa;

	private BigDecimal homeAmount;

	private BigDecimal localAmount;

	private Currency currency;

	private String chequeNo;

	private String bankId;

	private String purchaseOrderId;

	private String loanId;

	private String narration;

	private TranType tranType;

	private String orgnTLFID;

	private Branch branch;

	private BigDecimal rate;

	private Date settlementDate;

	private boolean reverse;

	private String referenceNo;

	private String referenceType;

	private String eNo;

	private boolean paid;

	private String tlfNo;

	private boolean clearing;

	private boolean isRenewal;

	public EditVoucherDto() {
		super();
	}

	// Used in TLF.findCashAccountByVoucherNo
	public EditVoucherDto(TranType tranType) {
		super();
		this.tranType = tranType;
	}

	public EditVoucherDto(CurrencyChartOfAccount ccoa, String eNo, BigDecimal homeAmount, BigDecimal localAmount, String narration, TranType tranType, Currency currency,
			BigDecimal rate, Branch branch, boolean reverse, Date settlementDate, String chequeNo) {
		super();
		this.ccoa = ccoa;
		this.homeAmount = homeAmount.setScale(2, RoundingMode.HALF_UP);
		this.localAmount = localAmount.setScale(2, RoundingMode.HALF_UP);
		this.currency = currency;
		this.chequeNo = chequeNo;
		this.narration = narration;
		this.tranType = tranType;
		this.branch = branch;
		this.rate = rate;
		this.settlementDate = settlementDate;
		this.reverse = reverse;
		this.eNo = eNo;
	}

	public EditVoucherDto(String id, String customerId, CurrencyChartOfAccount ccoa, BigDecimal homeAmount, BigDecimal localAmount, Currency currency, String chequeNo,
			String bankId, String purchaseOrderId, String loanId, String narration, TranType tranType, String orgnTLFID, Branch branch, BigDecimal rate, Date settlementDate,
			boolean reverse, String referenceNo, String referenceType, String eNo, boolean paid, String tlfNo, boolean clearing, boolean isRenewal) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bankId == null) ? 0 : bankId.hashCode());
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
		EditVoucherDto other = (EditVoucherDto) obj;
		if (bankId == null) {
			if (other.bankId != null)
				return false;
		} else if (!bankId.equals(other.bankId))
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
		return true;
	}

}
