package org.ace.accounting.system.trantype;

import java.io.Serializable;

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
import javax.persistence.Version;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.TableName;

import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.TRANTYPE)
@TableGenerator(name = "TRANTYPE_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "TRANTYPE_GEN", allocationSize = 10)
@NamedQueries(value = { @NamedQuery(name = "TranType.findAll", query = "SELECT t FROM TranType t"),
		@NamedQuery(name = "TranType.findByTransCode", query = "SELECT t FROM TranType t WHERE t.tranCode=:tranCode")	})
@EntityListeners(IDInterceptor.class)
public class TranType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5733231881764097574L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TRANTYPE_GEN")
	private String id;

	@Enumerated(EnumType.STRING)
	private TranCode tranCode;

	private String desp;

	private String narration;

	private String status;

	private String pbReference;

	private String rvReference;

	@Version
	private int version;

	@Embedded
	private BasicEntity basicEntity;

	public TranType() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TranCode getTranCode() {
		return tranCode;
	}

	public void setTranCode(TranCode tranCode) {
		this.tranCode = tranCode;
	}

	public String getDesp() {
		return desp;
	}

	public void setDesp(String desp) {
		this.desp = desp;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPbReference() {
		return pbReference;
	}

	public void setPbReference(String pbReference) {
		this.pbReference = pbReference;
	}

	public String getRvReference() {
		return rvReference;
	}

	public void setRvReference(String rvReference) {
		this.rvReference = rvReference;
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
		result = prime * result + ((desp == null) ? 0 : desp.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((narration == null) ? 0 : narration.hashCode());
		result = prime * result + ((pbReference == null) ? 0 : pbReference.hashCode());
		result = prime * result + ((rvReference == null) ? 0 : rvReference.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tranCode == null) ? 0 : tranCode.hashCode());
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
		TranType other = (TranType) obj;
		if (basicEntity == null) {
			if (other.basicEntity != null)
				return false;
		} else if (!basicEntity.equals(other.basicEntity))
			return false;
		if (desp == null) {
			if (other.desp != null)
				return false;
		} else if (!desp.equals(other.desp))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (narration == null) {
			if (other.narration != null)
				return false;
		} else if (!narration.equals(other.narration))
			return false;
		if (pbReference == null) {
			if (other.pbReference != null)
				return false;
		} else if (!pbReference.equals(other.pbReference))
			return false;
		if (rvReference == null) {
			if (other.rvReference != null)
				return false;
		} else if (!rvReference.equals(other.rvReference))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (tranCode != other.tranCode)
			return false;
		if (version != other.version)
			return false;
		return true;
	}

}