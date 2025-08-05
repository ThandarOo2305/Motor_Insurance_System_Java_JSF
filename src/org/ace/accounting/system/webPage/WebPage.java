package org.ace.accounting.system.webPage;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
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

@Entity
@Table(name = TableName.WEBPAGE)
@TableGenerator(name = "ACCWEBPAGE_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "ACCWEBPAGE_GEN", allocationSize = 10)
@NamedQueries(value = { @NamedQuery(name = "WebPage.findAll", query = "SELECT pl FROM WebPage pl ORDER BY pl.customOrder ASC"),
		@NamedQuery(name = "WebPage.findWebPageID", query = "SELECT pl.id FROM WebPage pl ORDER BY pl.name ASC") })
// @EntityListeners(IDInterceptor.class)
public class WebPage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ACCWEBPAGE_GEN")
	private String id;

	private int customOrder;

	private String name;

	private String description;

	private String url;

	@Enumerated(EnumType.STRING)
	private WebpageGroup webpageGroup;

	@Version
	private int version;

	@Embedded
	private BasicEntity basicEntity;

	public WebPage() {
	}

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getCustomOrder() {
		return customOrder;
	}

	public void setCustomOrder(int customOrder) {
		this.customOrder = customOrder;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((basicEntity == null) ? 0 : basicEntity.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		WebPage other = (WebPage) obj;
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
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

	public WebpageGroup getWebpageGroup() {
		return webpageGroup;
	}

	public void setWebpageGroup(WebpageGroup webpageGroup) {
		this.webpageGroup = webpageGroup;
	}
}
