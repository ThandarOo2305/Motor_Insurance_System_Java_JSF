package org.ace.accounting.system.motor;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.TableName;
import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.ADDON)
@TableGenerator(name = "ADDON_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "ADDON_GEN", allocationSize = 10)
@EntityListeners(IDInterceptor.class)
public class AddOn implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ADDON_GEN")
	private String id;
	
	private String name;
	
	@Version
	private int version;
	
	@Embedded
	private BasicEntity basicEntity;
	
	@ManyToMany(mappedBy = "addons")
	private List<MotorPolicyVehicleLink> vehicles;

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

	public List<MotorPolicyVehicleLink> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<MotorPolicyVehicleLink> vehicles) {
		this.vehicles = vehicles;
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
