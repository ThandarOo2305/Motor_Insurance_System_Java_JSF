package org.ace.accounting.system.setup;

import java.io.Serializable;

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
@Table(name = TableName.SETUP)
@TableGenerator(name = "SETUP_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "SETUP_GEN", allocationSize = 10)
@NamedQueries(value = { @NamedQuery(name = "Setup.findAll", query = "SELECT s FROM Setup s ORDER BY s.variable"),
		@NamedQuery(name = "Setup.findSetupValueByVariable", query = "SELECT s.value FROM Setup s WHERE s.variable=:variable"),
		@NamedQuery(name = "Setup.findSetupBudgetByVariable", query = "SELECT s.budget FROM Setup s WHERE s.variable=:variable"),
		@NamedQuery(name = "Setup.updateSetupBudget", query = "Update Setup s Set s.budget=:budget"),
		@NamedQuery(name = "Setup.updateSetupValueByVariable", query = "Update Setup s Set s.value=:value WHERE s.variable=:variable") })
@EntityListeners(IDInterceptor.class)
public class Setup implements Serializable {
	private static final long serialVersionUID = 3050597136794243842L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SETUP_GEN")
	private String id;

	private String variable;

	private String value;

	private String budget;

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

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((basicEntity == null) ? 0 : basicEntity.hashCode());
		result = prime * result + ((budget == null) ? 0 : budget.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((variable == null) ? 0 : variable.hashCode());
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
		Setup other = (Setup) obj;
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		if (variable == null) {
			if (other.variable != null)
				return false;
		} else if (!variable.equals(other.variable))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

}