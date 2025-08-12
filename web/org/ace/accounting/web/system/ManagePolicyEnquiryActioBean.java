package org.ace.accounting.web.system;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.system.motor.MotorPolicy;
import org.ace.accounting.system.motor.MotorPolicyVehicleLink;
import org.ace.accounting.system.motor.service.MotorEnquiryService;
import org.ace.accounting.system.motor.service.interfaces.IMotorEnquiryService;

@ManagedBean(name = "ManagePolicyEnquiryActioBean")
@ViewScoped
public class ManagePolicyEnquiryActioBean {

	@ManagedProperty(value = "#{MotorEnquiryService}")
	private IMotorEnquiryService policyEnquiryservice;

	public void setPolicyEnquiryService(IMotorEnquiryService policyEnquiryService) {
		this.policyEnquiryservice = policyEnquiryService;
	}

	// Policy No Validator
	@ManagedProperty(value = "#{MotorPolicyValidator}")
	private IDataValidator<MotorPolicy> motorPolicyValidator;

	public void setMotorPolicyValidator(IDataValidator<MotorPolicy> motorPolicyValidator) {
		this.motorPolicyValidator = motorPolicyValidator;
	}

	// Registation No Validator
	@ManagedProperty(value = "#{MotorPolicyVehicleValidator}")
	private IDataValidator<MotorPolicyVehicleLink> motorPolicyVehicleValidator;

	public void setMotorPolicyVehicleValidator(IDataValidator<MotorPolicyVehicleLink> motorPolicyVehicleValidator) {
		this.motorPolicyVehicleValidator = motorPolicyVehicleValidator;
	}

	private Date policyStartDate;
	private Date policyEndDate;
	private String policyNo;
	private String registrationNo;

	// Search Result Lists
	private List<MotorPolicyVehicleLink> results;

	// Search Method
	public void search() {
		try {
			results = policyEnquiryservice.search(policyStartDate, policyEndDate, policyNo, registrationNo);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Getters and setters for all fields
	public Date getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(Date policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public Date getPolicyEndDate() {
		return policyEndDate;
	}

	public void setPolicyEndDate(Date policyEndDate) {
		this.policyEndDate = policyEndDate;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public List<MotorPolicyVehicleLink> getVehicleLinkResults() {
		return results;
	}

	public void setVehicleLinkResults(List<MotorPolicyVehicleLink> vehicleLinkResults) {
		this.results = vehicleLinkResults;
	}

}