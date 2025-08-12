package org.ace.accounting.web.system;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.system.motor.MotorPolicy;
import org.ace.accounting.system.motor.MotorPolicyVehicleLink;

@ManagedBean(name = "ManagePolicyEnquiryActioBean")
@ViewScoped
public class ManagePolicyEnquiryActioBean {

	@ManagedProperty(value = "#{PolicyEnquiryService}")
	private PolicyEnquiryService policyEnquiryservice;

	public void setPolicyEnquiryService(IPolicyEnquiryService policyEnquiryService) {
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

	    public void setMotorPolicyVehicleValidator(IDataValidator<MotorPolicyVehicleLink> motorPolicyVehicleEnquiryValidator) {
	        this.motorPolicyVehicleEnquiryValidator = motorPolicyVehicleEnquiryValidator;
	    }

	    private Date policyStartDate;
	    private Date policyEndDate;
	    private String policyNo;
	    private String registrationNo;
	    private MotorPolicyVehicleLink motorPolicyVehicleLink;
	    private MotorPolicy motorPolicy;
	    // Search Result Lists
	    private List<MotorPolicyVehicleLink> results;

	    // Search Method
	    public void search() {
	        try {
	            results = policyEnquiryService.searchPolicies(
	                policyStartDate,
	                policyEndDate,
	                policyNo,
			        registrationNo
	            );

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

	}