package org.ace.accounting.system.rateinfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.ace.accounting.system.currency.Currency;
import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.RATEINFO)
@TableGenerator(name = "RATEINFO_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "RATEINFO_GEN", allocationSize = 10)
@NamedQueries(value = { @NamedQuery(name = "RateInfo.findAll", query = "SELECT r FROM RateInfo r WHERE r.lastModify = 1 ORDER BY r.rDate, r.currency DESC"),
		@NamedQuery(name = "RateInfo.findRateInfoByCurRTypeRDate", query = "SELECT r FROM RateInfo r WHERE r.currency = :currency AND r.rateType= :rateType AND r.rDate= :rDate AND r.lastModify = 1"),
		@NamedQuery(name = "RateInfo.updateLastModifyBy", query = "UPDATE RateInfo r set r.lastModify=0  WHERE r.currency = :currency AND r.rateType= :rateType"),
		@NamedQuery(name = "RateInfo.findRateInfoByCurrencyID", query = "SELECT r FROM RateInfo r WHERE r.currency.id=:currency") })
@EntityListeners(IDInterceptor.class)
public class RateInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6273089696398684674L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "RATEINFO_GEN")
	private String id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUR", referencedColumnName = "ID")
	private Currency currency;

	@Enumerated(value = EnumType.STRING)
	private RateType rateType;

	private BigDecimal exchangeRate;

	private String denoRate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date rDate;

	private boolean lastModify = true;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TOCUR", referencedColumnName = "ID")
	private Currency toCur;

	@Version
	private int version;

	@Embedded
	private BasicEntity basicEntity;

	public RateInfo() {
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public RateType getRateType() {
		return rateType;
	}

	public void setRateType(RateType rateType) {
		this.rateType = rateType;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getDenoRate() {
		return denoRate;
	}

	public void setDenoRate(String denoRate) {
		this.denoRate = denoRate;
	}

	public Date getrDate() {
		return rDate;
	}

	public void setrDate(Date rDate) {
		this.rDate = rDate;
	}

	public boolean isLastModify() {
		return lastModify;
	}

	public void setLastModify(boolean lastModify) {
		this.lastModify = lastModify;
	}

	public Currency getToCur() {
		return toCur;
	}

	public void setToCur(Currency toCur) {
		this.toCur = toCur;
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
		result = prime * result + ((basicEntity == null) ? 0 : basicEntity.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((denoRate == null) ? 0 : denoRate.hashCode());
		result = prime * result + ((exchangeRate == null) ? 0 : exchangeRate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (lastModify ? 1231 : 1237);
		result = prime * result + ((rDate == null) ? 0 : rDate.hashCode());
		result = prime * result + ((rateType == null) ? 0 : rateType.hashCode());
		result = prime * result + ((toCur == null) ? 0 : toCur.hashCode());
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
		RateInfo other = (RateInfo) obj;
		if (basicEntity == null) {
			if (other.basicEntity != null)
				return false;
		} else if (!basicEntity.equals(other.basicEntity))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (denoRate == null) {
			if (other.denoRate != null)
				return false;
		} else if (!denoRate.equals(other.denoRate))
			return false;
		if (exchangeRate == null) {
			if (other.exchangeRate != null)
				return false;
		} else if (!exchangeRate.equals(other.exchangeRate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastModify != other.lastModify)
			return false;
		if (rDate == null) {
			if (other.rDate != null)
				return false;
		} else if (!rDate.equals(other.rDate))
			return false;
		if (rateType != other.rateType)
			return false;
		if (toCur == null) {
			if (other.toCur != null)
				return false;
		} else if (!toCur.equals(other.toCur))
			return false;
		return true;
	}

}
