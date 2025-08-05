package org.ace.accounting.customer;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.occupation.Occupation;

public class CustomerDTO {

	private String id;
	private String salutation;
	private String firstName;
	private String middleName;
	private String lastName;
	private String fatherName;
	private String gender;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOfBirth;
	
	private String birthMark;
	private String maritalStatus;
	private Occupation occupation;
	
	//-----CONTACT INFO---------
	private String residentAddress;
	private String residentTownship;
	private String permanentAddress;
	private String permanentTownship;
	private String officeAddress;
	private String officeTownship;
	private String phone;
	private String fax;
	private String mobile;
	private String email;

	//------ FAMILY INFO ----------
	private String relativeSalutation;
	private String relativeFirstName;
	private String relativeMiddleName;
	private String relativeLastName;
	private String relationship;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date relativeDOB;
	private int version;
	private BasicEntity basicEntity;


	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getBirthMark() {
		return birthMark;
	}

	public void setBirthMark(String birthMark) {
		this.birthMark = birthMark;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public Occupation getOccupation() {
		return occupation;
	}

	public void setOccupation(Occupation occupation) {
		this.occupation = occupation;
	}

	public String getResidentAddress() {
		return residentAddress;
	}

	public void setResidentAddress(String residentAddress) {
		this.residentAddress = residentAddress;
	}

	public String getResidentTownship() {
		return residentTownship;
	}

	public void setResidentTownship(String residentTownship) {
		this.residentTownship = residentTownship;
	}

	public String getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public String getPermanentTownship() {
		return permanentTownship;
	}

	public void setPermanentTownship(String permanentTownship) {
		this.permanentTownship = permanentTownship;
	}

	public String getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public String getOfficeTownship() {
		return officeTownship;
	}

	public void setOfficeTownship(String officeTownship) {
		this.officeTownship = officeTownship;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRelativeSalutation() {
		return relativeSalutation;
	}

	public void setRelativeSalutation(String relativeSalutation) {
		this.relativeSalutation = relativeSalutation;
	}

	public String getRelativeFirstName() {
		return relativeFirstName;
	}

	public void setRelativeFirstName(String relativeFirstName) {
		this.relativeFirstName = relativeFirstName;
	}

	public String getRelativeMiddleName() {
		return relativeMiddleName;
	}

	public void setRelativeMiddleName(String relativeMiddleName) {
		this.relativeMiddleName = relativeMiddleName;
	}

	public String getRelativeLastName() {
		return relativeLastName;
	}

	public void setRelativeLastName(String relativeLastName) {
		this.relativeLastName = relativeLastName;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public Date getRelativeDOB() {
		return relativeDOB;
	}

	public void setRelativeDOB(Date relativeDOB) {
		this.relativeDOB = relativeDOB;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
