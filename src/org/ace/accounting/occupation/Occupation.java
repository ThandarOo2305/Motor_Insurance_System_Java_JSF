package org.ace.accounting.occupation;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.TableName;
import org.ace.accounting.customer.Customer;
import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.OCCUPATION)
@TableGenerator(name = "OCCUPATIONS_GEN",table = "ID_GEN",pkColumnName = "GEN_NAME",valueColumnName = "GEN_VAL" ,pkColumnValue = "OCCUPATIONS_GEN", allocationSize = 10 )
@NamedQueries(value = { @NamedQuery(name = "Occupation.findAll", query = "SELECT o FROM Occupation o ORDER BY o.name ASC")})
@EntityListeners(IDInterceptor.class)
public class Occupation implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE,generator = "OCCUPATIONS_GEN")
	private String id;
	
	private String name;
	private String myanmarName;
	private String description;
	
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
	public String getMyanmarName() {
		return myanmarName;
	}
	public void setMyanmarName(String myanmarName) {
		this.myanmarName = myanmarName;
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
	
	

}
