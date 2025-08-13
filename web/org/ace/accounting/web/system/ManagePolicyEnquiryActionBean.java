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

@ManagedBean(name = "ManagePolicyEnquiryActionBean")
@ViewScoped
public class ManagePolicyEnquiryActionBean {

	@ManagedProperty(value = "#{MotorEnquiryService}")
	private IMotorEnquiryService policyEnquiryService;

	public void setPolicyEnquiryService(IMotorEnquiryService policyEnquiryService) {
		this.policyEnquiryService = policyEnquiryService;
	}
	//Policy No Validator
	@ManagedProperty(value = "#{MotorPolicyEnquiryValidator}")
	    private IDataValidator<MotorPolicy> motorPolicyNoValidator;

	    public void setMotorPolicyNoValidator(IDataValidator<MotorPolicy> motorPolicyNoValidator) {
	        this.motorPolicyNoValidator = motorPolicyNoValidator;
	    }

	//Registation No Validator
	    
	    @ManagedProperty(value = "#{MotorPolicyVehicleEnquiryValidator}")
	    private IDataValidator<MotorPolicyVehicleLink> motorPolicyVehicleEnquiryValidator;

	    public void setMotorPolicyVehicleEnquiryValidator(
				IDataValidator<MotorPolicyVehicleLink> motorPolicyVehicleEnquiryValidator) {
			this.motorPolicyVehicleEnquiryValidator = motorPolicyVehicleEnquiryValidator;
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
	            results = policyEnquiryService.search(
	                policyStartDate,
	                policyEndDate,
	                policyNo,
			        registrationNo
	            );
	            System.out.println("success_____________________");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public void reset() {
	        this.policyStartDate = null;
	        this.policyEndDate = null;
	        this.policyNo = null;
	        this.registrationNo = null;
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

	public List<MotorPolicyVehicleLink> getResults() {
		return results;
	}

	public void setResults(List<MotorPolicyVehicleLink> results) {
		this.results = results;
	}
	
}