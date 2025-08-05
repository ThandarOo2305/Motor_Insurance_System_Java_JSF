package org.ace.accounting.system.chartaccount;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.TableName;
import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.COA)
@TableGenerator(name = "COA_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "COA_GEN", allocationSize = 10)
@NamedQueries(value = { @NamedQuery(name = "ChartOfAccount.findAll", query = "SELECT c FROM ChartOfAccount c ORDER BY c.acCode"),
		@NamedQuery(name = "ChartOfAccount.findAllCOAByAccountCodeType", query = "SELECT c FROM ChartOfAccount c WHERE c.acCodeType!=org.ace.accounting.system.chartaccount.AccountCodeType.HEAD AND c.acCodeType!=org.ace.accounting.system.chartaccount.AccountCodeType.GROUP ORDER BY c.acCode ASC"),
		@NamedQuery(name = "ChartOfAccount.findAllAcCode", query = "SELECT c.acCode FROM ChartOfAccount c"),
		@NamedQuery(name = "ChartOfAccount.findAllHeadCode", query = "SELECT c FROM ChartOfAccount c WHERE c.acCodeType=org.ace.accounting.system.chartaccount.AccountCodeType.HEAD ORDER BY c.acCode ASC"),
		@NamedQuery(name = "ChartOfAccount.findAllGroupCode", query = "SELECT c FROM ChartOfAccount c WHERE c.acCodeType=org.ace.accounting.system.chartaccount.AccountCodeType.GROUP ORDER BY c.acCode ASC"),
		@NamedQuery(name = "ChartOfAccount.findByAcCode", query = "SELECT c FROM ChartOfAccount c WHERE c.acCode = :acCode"),
		@NamedQuery(name = "ChartOfAccount.findByHeadACId", query = "SELECT c FROM ChartOfAccount c WHERE c.id = :headId"),
		@NamedQuery(name = "ChartOfAccount.findByGroupACId", query = "SELECT c FROM ChartOfAccount c WHERE c.id = :groupId"),
		@NamedQuery(name = "ChartOfAccount.findByAcCodeAndType", query = "SELECT c FROM ChartOfAccount c WHERE c.acCode = :acCode and c.acType = :acType"),
		@NamedQuery(name = "ChartOfAccount.findCoaByIE", query = "SELECT c FROM ChartOfAccount c WHERE c.acType=org.ace.accounting.system.chartaccount.AccountType.I AND c.acCodeType=org.ace.accounting.system.chartaccount.AccountCodeType.DETAIL OR c.acType=org.ace.accounting.system.chartaccount.AccountType.E AND c.acCodeType=org.ace.accounting.system.chartaccount.AccountCodeType.DETAIL  ORDER BY c.acCode ASC"),
		@NamedQuery(name = "ChartOfAccount.findByIbsbACode", query = "SELECT c FROM ChartOfAccount c WHERE c.ibsbACode = :ibsbACode "),
		@NamedQuery(name = "ChartOfAccount.findOBALUsedCOA", query = "SELECT SUM(ccoa.oBal) FROM ChartOfAccount coa INNER JOIN CurrencyChartOfAccount ccoa WHERE ccoa.coa.id=coa.id AND coa.id=:coaId "),
		@NamedQuery(name = "ChartOfAccount.findTLFUsedCOA", query = "SELECT	t1 FROM TLF t1 INNER JOIN t1.ccoa ccoa1 "
				+ "WHERE ccoa1.currency = t1.currency AND ccoa1.branch = t1.branch AND t1.ccoa.coa.id=:coaId AND t1.reverse = false " + "UNION ALL "
				+ "SELECT t2 FROM TLFHIST t2 INNER JOIN t2.ccoa ccoa2 "
				+ "WHERE ccoa2.currency = t2.currency AND ccoa2.branch = t2.branch AND t2.ccoa.coa.id=:coaId AND t2.reverse = false "),
		@NamedQuery(name = "ChartOfAccount.findAllWithoutGroupAcc", query = "SELECT c FROM ChartOfAccount c WHERE SUBSTRING(c.acCode,3)!='000'  ORDER BY c.acCode ASC") })
@EntityListeners(IDInterceptor.class)
public class ChartOfAccount implements Serializable {

	private static final long serialVersionUID = -157733738940063961L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "COA_GEN")
	private String id;

	private String acName;

	private String acCode;

	@Enumerated(value = EnumType.STRING)
	private AccountType acType;

	@Enumerated(value = EnumType.STRING)
	private AccountCodeType acCodeType;

	@Temporal(TemporalType.TIMESTAMP)
	private Date pDate;

	private String ibsbACode;

	private String headId;

	private String groupId;

	@Version
	private int version;

	@Embedded
	private BasicEntity basicEntity;

	public ChartOfAccount() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAcName() {
		return acName;
	}

	public void setAcName(String acName) {
		this.acName = acName;
	}

	public String getAcCode() {
		return acCode;
	}

	public void setAcCode(String acCode) {
		this.acCode = acCode;
	}

	public AccountType getAcType() {
		return acType;
	}

	public void setAcType(AccountType acType) {
		this.acType = acType;
	}

	public Date getpDate() {
		return pDate;
	}

	public void setpDate(Date pDate) {
		this.pDate = pDate;
	}

	public String getIbsbACode() {
		return ibsbACode;
	}

	public void setIbsbACode(String ibsbACode) {
		this.ibsbACode = ibsbACode;
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

	public AccountCodeType getAcCodeType() {
		return acCodeType;
	}

	public void setAcCodeType(AccountCodeType acCodeType) {
		this.acCodeType = acCodeType;
	}

	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acCode == null) ? 0 : acCode.hashCode());
		result = prime * result + ((acCodeType == null) ? 0 : acCodeType.hashCode());
		result = prime * result + ((acName == null) ? 0 : acName.hashCode());
		result = prime * result + ((acType == null) ? 0 : acType.hashCode());
		result = prime * result + ((basicEntity == null) ? 0 : basicEntity.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((headId == null) ? 0 : headId.hashCode());
		result = prime * result + ((ibsbACode == null) ? 0 : ibsbACode.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((pDate == null) ? 0 : pDate.hashCode());
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
		ChartOfAccount other = (ChartOfAccount) obj;
		if (acCode == null) {
			if (other.acCode != null)
				return false;
		} else if (!acCode.equals(other.acCode))
			return false;
		if (acCodeType != other.acCodeType)
			return false;
		if (acName == null) {
			if (other.acName != null)
				return false;
		} else if (!acName.equals(other.acName))
			return false;
		if (acType != other.acType)
			return false;
		if (basicEntity == null) {
			if (other.basicEntity != null)
				return false;
		} else if (!basicEntity.equals(other.basicEntity))
			return false;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (headId == null) {
			if (other.headId != null)
				return false;
		} else if (!headId.equals(other.headId))
			return false;
		if (ibsbACode == null) {
			if (other.ibsbACode != null)
				return false;
		} else if (!ibsbACode.equals(other.ibsbACode))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (pDate == null) {
			if (other.pDate != null)
				return false;
		} else if (!pDate.equals(other.pDate))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return acCode + "-" + acName;
	}
}
