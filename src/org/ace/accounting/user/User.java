
package org.ace.accounting.user;

import java.io.Serializable;


import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.ContactInfo;
import org.ace.accounting.common.TableName;
import org.ace.accounting.role.Role;
import org.ace.accounting.system.branch.Branch;
import org.ace.java.component.idgen.service.IDInterceptor;

@Entity
@Table(name = TableName.USER)
@TableGenerator(name = "ACCUSERS_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "ACCUSERS_GEN", allocationSize = 10)
@NamedQueries(value = { @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u ORDER BY u.name ASC"),
		@NamedQuery(name = "User.findByUsercode", query = "SELECT u FROM User u WHERE u.userCode = :userCode"),
		@NamedQuery(name = "User.changePassword", query = "UPDATE User u SET u.password = :newPassword WHERE u.userCode = :usercode"),
		@NamedQuery(name = "User.resetPassword", query = "UPDATE User u SET u.password = :defaultPassowrd WHERE u.userCode = :usercode"),
		@NamedQuery(name = "User.findUserId", query = "SELECT u.id FROM User u ORDER BY u.name ASC") })
@EntityListeners(IDInterceptor.class)
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_PASSWORD = "password";
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ACCUSERS_GEN")
	private String id;

	private String userCode;

	private String password;

	private String name;

	private boolean isAdmin;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = TableName.USER_ROLE, joinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
			@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") })
	private List<Role> roles;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BRANCHID", referencedColumnName = "ID")
	private Branch branch;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOfBirth;

	@Embedded
	private ContactInfo contactInfo;

	@Version
	private int version;

	@Embedded
	private BasicEntity basicEntity;

	public User() {
		this.contactInfo = new ContactInfo();
	}

	public User(String id, String usercode, String password, String name) {
		this.id = id;
		this.userCode = usercode;
		this.password = password;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void addRoles(List<Role> roleList) {
		if (roles == null) {
			roles = new ArrayList<Role>();
		}
		Set<Role> set = new HashSet<Role>(roles);
		set.addAll(roleList);
		roles = new ArrayList<Role>(set);
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public Branch getBranch() {
		return branch;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((basicEntity == null) ? 0 : basicEntity.hashCode());
		result = prime * result + ((branch == null) ? 0 : branch.hashCode());
		result = prime * result + ((contactInfo == null) ? 0 : contactInfo.hashCode());
		result = prime * result + ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (isAdmin ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((userCode == null) ? 0 : userCode.hashCode());
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
		User other = (User) obj;
		if (basicEntity == null) {
			if (other.basicEntity != null)
				return false;
		} else if (!basicEntity.equals(other.basicEntity))
			return false;
		if (branch == null) {
			if (other.branch != null)
				return false;
		} else if (!branch.equals(other.branch))
			return false;
		if (contactInfo == null) {
			if (other.contactInfo != null)
				return false;
		} else if (!contactInfo.equals(other.contactInfo))
			return false;
		if (dateOfBirth == null) {
			if (other.dateOfBirth != null)
				return false;
		} else if (!dateOfBirth.equals(other.dateOfBirth))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isAdmin != other.isAdmin)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (userCode == null) {
			if (other.userCode != null)
				return false;
		} else if (!userCode.equals(other.userCode))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

}
