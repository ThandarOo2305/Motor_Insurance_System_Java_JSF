package org.ace.accounting.system.systempost;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.TableName;

import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.SYSTEMPOST)
@TableGenerator(name = "SYSTEMPOST_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "SYSTEMPOST_GEN", allocationSize = 10)
@NamedQueries(value = { @NamedQuery(name = "SystemPost.findAll", query = "SELECT s FROM SystemPost s ORDER BY s.postingName"),
		@NamedQuery(name = "SystemPost.findbyPostingName", query = "SELECT s FROM SystemPost s WHERE s.postingName=:postingName"),
		@NamedQuery(name = "SystemPost.updatePostingDateByName", query = "UPDATE SystemPost s SET s.postingDate=:postingDate WHERE s.postingName=:postingName") })
@EntityListeners(IDInterceptor.class)
public class SystemPost implements Serializable {
	private static final long serialVersionUID = 3050597136794243842L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "DEPARTMENT_GEN")
	private String id;
	private String postingName;

	@Temporal(TemporalType.TIMESTAMP)
	private Date postingDate;

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

	public String getPostingName() {
		return postingName;
	}

	public void setPostingName(String postingName) {
		this.postingName = postingName;
	}

	public Date getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
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

}
