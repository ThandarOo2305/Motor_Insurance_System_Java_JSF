package org.ace.accounting.role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.TableName;
import org.ace.accounting.system.webPage.WebPage;
import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.ROLE)
@TableGenerator(name = "ACCROLE_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "ACCROLE_GEN", allocationSize = 10)
@NamedQueries(value = { @NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r ORDER BY r.name ASC"),
		@NamedQuery(name = "Role.findRoleId", query = "SELECT r.id FROM Role r ORDER BY r.name ASC") })
@EntityListeners(IDInterceptor.class)
public class Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ACCROLE_GEN")
	private String id;

	private String name;

	private String description;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = TableName.ROLE_WEBPAGE, joinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
			@JoinColumn(name = "WEBPAGES_ID", referencedColumnName = "ID") })
	private List<WebPage> webpages;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public List<WebPage> getWebpages() {
		return webpages;
	}

	public void setWebpages(List<WebPage> webpages) {
		this.webpages = webpages;
	}

	public void addWebpages(List<WebPage> webPageList) {
		if (webpages == null) {
			webpages = new ArrayList<WebPage>();
		}
		Set<WebPage> set = new HashSet<WebPage>(webpages);
		set.addAll(webPageList);
		webpages = new ArrayList<WebPage>(set);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((basicEntity == null) ? 0 : basicEntity.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Role other = (Role) obj;
		if (basicEntity == null) {
			if (other.basicEntity != null)
				return false;
		} else if (!basicEntity.equals(other.basicEntity))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

}