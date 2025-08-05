/***************************************************************************************
 * @author <<Your Name>>
 * @Date 2013-02-11
 * @Version 1.0
 * @Purpose <<You have to write the comment the main purpose of this class>>
 * 
 *    
 ***************************************************************************************/
package org.ace.accounting.system.currency;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.TableName;

import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.CUR)
@TableGenerator(name = "CURRENCY_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "CURRENCY_GEN", allocationSize = 10)
@NamedQueries(value = { @NamedQuery(name = "Currency.findAll", query = "SELECT c FROM Currency c  ORDER BY c.currencyCode ASC"),
		@NamedQuery(name = "Currency.findForeignCurrency", query = "SELECT c FROM Currency c WHERE c.isHomeCur=false ORDER BY c.currencyCode ASC"),
		@NamedQuery(name = "Currency.findByCurrencyCode", query = "SELECT c FROM Currency c WHERE c.currencyCode = :currencyCode"),
		@NamedQuery(name = "Currency.findById", query = "SELECT c FROM Currency c WHERE c.id = :id"),
		@NamedQuery(name = "Currency.findHomeCurrency", query = "SELECT c FROM Currency c WHERE c.isHomeCur = true") })
@EntityListeners(IDInterceptor.class)
public class Currency implements Serializable {

	private static final long serialVersionUID = -5785537862626257490L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CURRENCY_GEN")
	private String id;

	@Column(name = "CUR", unique = true)
	private String currencyCode;
	private String description;
	private String symbol;
	private String inwordDesp1;
	private String inwordDesp2;
	private String homeInwordDesp1;
	private String homeInwordDesp2;
	private Boolean isHomeCur;
	private BigDecimal m1;
	private BigDecimal m2;
	private BigDecimal m3;
	private BigDecimal m4;
	private BigDecimal m5;
	private BigDecimal m6;
	private BigDecimal m7;
	private BigDecimal m8;
	private BigDecimal m9;
	private BigDecimal m10;
	private BigDecimal m11;
	private BigDecimal m12;
	private BigDecimal m13;

	@Version
	private int version;

	@Embedded
	private BasicEntity basicEntity;

	public Currency() {
		setAllZero();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getInwordDesp1() {
		return inwordDesp1;
	}

	public void setInwordDesp1(String inwordDesp1) {
		this.inwordDesp1 = inwordDesp1;
	}

	public String getInwordDesp2() {
		return inwordDesp2;
	}

	public void setInwordDesp2(String inwordDesp2) {
		this.inwordDesp2 = inwordDesp2;
	}

	public Boolean getIsHomeCur() {
		return isHomeCur;
	}

	public void setIsHomeCur(Boolean isHomeCur) {
		this.isHomeCur = isHomeCur;
	}

	public BigDecimal getM1() {
		return m1;
	}

	public void setM1(BigDecimal m1) {
		this.m1 = m1;
	}

	public BigDecimal getM2() {
		return m2;
	}

	public void setM2(BigDecimal m2) {
		this.m2 = m2;
	}

	public BigDecimal getM3() {
		return m3;
	}

	public void setM3(BigDecimal m3) {
		this.m3 = m3;
	}

	public BigDecimal getM4() {
		return m4;
	}

	public void setM4(BigDecimal m4) {
		this.m4 = m4;
	}

	public BigDecimal getM5() {
		return m5;
	}

	public void setM5(BigDecimal m5) {
		this.m5 = m5;
	}

	public BigDecimal getM6() {
		return m6;
	}

	public void setM6(BigDecimal m6) {
		this.m6 = m6;
	}

	public BigDecimal getM7() {
		return m7;
	}

	public void setM7(BigDecimal m7) {
		this.m7 = m7;
	}

	public BigDecimal getM8() {
		return m8;
	}

	public void setM8(BigDecimal m8) {
		this.m8 = m8;
	}

	public BigDecimal getM9() {
		return m9;
	}

	public void setM9(BigDecimal m9) {
		this.m9 = m9;
	}

	public BigDecimal getM10() {
		return m10;
	}

	public void setM10(BigDecimal m10) {
		this.m10 = m10;
	}

	public BigDecimal getM11() {
		return m11;
	}

	public void setM11(BigDecimal m11) {
		this.m11 = m11;
	}

	public BigDecimal getM12() {
		return m12;
	}

	public void setM12(BigDecimal m12) {
		this.m12 = m12;
	}

	public BigDecimal getM13() {
		return m13;
	}

	public void setM13(BigDecimal m13) {
		this.m13 = m13;
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

	public String getHomeInwordDesp1() {
		return homeInwordDesp1;
	}

	public void setHomeInwordDesp1(String homeInwordDesp1) {
		this.homeInwordDesp1 = homeInwordDesp1;
	}

	public String getHomeInwordDesp2() {
		return homeInwordDesp2;
	}

	public void setHomeInwordDesp2(String homeInwordDesp2) {
		this.homeInwordDesp2 = homeInwordDesp2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((basicEntity == null) ? 0 : basicEntity.hashCode());
		result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((homeInwordDesp1 == null) ? 0 : homeInwordDesp1.hashCode());
		result = prime * result + ((homeInwordDesp2 == null) ? 0 : homeInwordDesp2.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inwordDesp1 == null) ? 0 : inwordDesp1.hashCode());
		result = prime * result + ((inwordDesp2 == null) ? 0 : inwordDesp2.hashCode());
		result = prime * result + ((isHomeCur == null) ? 0 : isHomeCur.hashCode());
		result = prime * result + ((m1 == null) ? 0 : m1.hashCode());
		result = prime * result + ((m10 == null) ? 0 : m10.hashCode());
		result = prime * result + ((m11 == null) ? 0 : m11.hashCode());
		result = prime * result + ((m12 == null) ? 0 : m12.hashCode());
		result = prime * result + ((m13 == null) ? 0 : m13.hashCode());
		result = prime * result + ((m2 == null) ? 0 : m2.hashCode());
		result = prime * result + ((m3 == null) ? 0 : m3.hashCode());
		result = prime * result + ((m4 == null) ? 0 : m4.hashCode());
		result = prime * result + ((m5 == null) ? 0 : m5.hashCode());
		result = prime * result + ((m6 == null) ? 0 : m6.hashCode());
		result = prime * result + ((m7 == null) ? 0 : m7.hashCode());
		result = prime * result + ((m8 == null) ? 0 : m8.hashCode());
		result = prime * result + ((m9 == null) ? 0 : m9.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
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
		Currency other = (Currency) obj;
		if (basicEntity == null) {
			if (other.basicEntity != null)
				return false;
		} else if (!basicEntity.equals(other.basicEntity))
			return false;
		if (currencyCode == null) {
			if (other.currencyCode != null)
				return false;
		} else if (!currencyCode.equals(other.currencyCode))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (homeInwordDesp1 == null) {
			if (other.homeInwordDesp1 != null)
				return false;
		} else if (!homeInwordDesp1.equals(other.homeInwordDesp1))
			return false;
		if (homeInwordDesp2 == null) {
			if (other.homeInwordDesp2 != null)
				return false;
		} else if (!homeInwordDesp2.equals(other.homeInwordDesp2))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inwordDesp1 == null) {
			if (other.inwordDesp1 != null)
				return false;
		} else if (!inwordDesp1.equals(other.inwordDesp1))
			return false;
		if (inwordDesp2 == null) {
			if (other.inwordDesp2 != null)
				return false;
		} else if (!inwordDesp2.equals(other.inwordDesp2))
			return false;
		if (isHomeCur == null) {
			if (other.isHomeCur != null)
				return false;
		} else if (!isHomeCur.equals(other.isHomeCur))
			return false;
		if (m1 == null) {
			if (other.m1 != null)
				return false;
		} else if (!m1.equals(other.m1))
			return false;
		if (m10 == null) {
			if (other.m10 != null)
				return false;
		} else if (!m10.equals(other.m10))
			return false;
		if (m11 == null) {
			if (other.m11 != null)
				return false;
		} else if (!m11.equals(other.m11))
			return false;
		if (m12 == null) {
			if (other.m12 != null)
				return false;
		} else if (!m12.equals(other.m12))
			return false;
		if (m13 == null) {
			if (other.m13 != null)
				return false;
		} else if (!m13.equals(other.m13))
			return false;
		if (m2 == null) {
			if (other.m2 != null)
				return false;
		} else if (!m2.equals(other.m2))
			return false;
		if (m3 == null) {
			if (other.m3 != null)
				return false;
		} else if (!m3.equals(other.m3))
			return false;
		if (m4 == null) {
			if (other.m4 != null)
				return false;
		} else if (!m4.equals(other.m4))
			return false;
		if (m5 == null) {
			if (other.m5 != null)
				return false;
		} else if (!m5.equals(other.m5))
			return false;
		if (m6 == null) {
			if (other.m6 != null)
				return false;
		} else if (!m6.equals(other.m6))
			return false;
		if (m7 == null) {
			if (other.m7 != null)
				return false;
		} else if (!m7.equals(other.m7))
			return false;
		if (m8 == null) {
			if (other.m8 != null)
				return false;
		} else if (!m8.equals(other.m8))
			return false;
		if (m9 == null) {
			if (other.m9 != null)
				return false;
		} else if (!m9.equals(other.m9))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

	public void setAllZero() {
		setM1(BigDecimal.ZERO);
		setM2(BigDecimal.ZERO);
		setM3(BigDecimal.ZERO);
		setM4(BigDecimal.ZERO);
		setM5(BigDecimal.ZERO);
		setM6(BigDecimal.ZERO);
		setM7(BigDecimal.ZERO);
		setM8(BigDecimal.ZERO);
		setM9(BigDecimal.ZERO);
		setM10(BigDecimal.ZERO);
		setM11(BigDecimal.ZERO);
		setM12(BigDecimal.ZERO);
		setM13(BigDecimal.ZERO);
	}
}
