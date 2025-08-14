package org.ace.accounting.web.system;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.validation.ErrorMessage;
import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.common.validation.ValidationResult;
import org.ace.accounting.system.motor.MotorEnquiryDTO;
import org.ace.accounting.system.motor.MotorPolicy;
import org.ace.accounting.system.motor.MotorPolicyVehicleLink;
import org.ace.accounting.system.motor.service.interfaces.IMotorEnquiryService;
import org.ace.java.web.common.BaseBean;

@ManagedBean(name = "ManagePolicyEnquiryActionBean")
@ViewScoped
public class ManagePolicyEnquiryActionBean extends BaseBean {

	@ManagedProperty(value = "#{MotorEnquiryService}")
	private IMotorEnquiryService policyEnquiryService;

	public void setPolicyEnquiryService(IMotorEnquiryService policyEnquiryService) {
		this.policyEnquiryService = policyEnquiryService;
	}

	// Policy No Validator
	@ManagedProperty(value = "#{MotorPolicyEnquiryValidator}")
	private IDataValidator<MotorPolicy> motorPolicyValidator;

	public void setMotorPolicyValidator(IDataValidator<MotorPolicy> motorPolicyNoValidator) {
		this.motorPolicyValidator = motorPolicyNoValidator;
	}

	// Registation No Validator
	@ManagedProperty(value = "#{MotorPolicyVehicleLinkEnquiryValidator}")
	private IDataValidator<MotorPolicyVehicleLink> motorPolicyVehicleEnquiryValidator;

	public void setMotorPolicyVehicleEnquiryValidator(
			IDataValidator<MotorPolicyVehicleLink> motorPolicyVehicleEnquiryValidator) {
		this.motorPolicyVehicleEnquiryValidator = motorPolicyVehicleEnquiryValidator;
	}

	private Date policyStartDate;
	private Date policyEndDate;
	private String policyNo;
	private String registrationNo;
	private MotorPolicy mp;
	private MotorPolicyVehicleLink mpv;
	// Search Result Lists
	private List<MotorEnquiryDTO> results;
	private boolean valid = true;
	ValidationResult policyResult;
	ValidationResult vehicleResult;

	// init
	@PostConstruct
	public void init() {
		setDefaultDates();
		createNewMotorPolicyVehicleLink();
		createNewPolicyNo();
	}

	public void createNewPolicyNo() {
		mp = new MotorPolicy();
		results = new ArrayList<>();
	}

	public void createNewMotorPolicyVehicleLink() {
		mpv = new MotorPolicyVehicleLink();
	}

	private void setDefaultDates() {
		Calendar endCal = Calendar.getInstance();
		endCal.set(Calendar.HOUR_OF_DAY, 0);// for end date
		endCal.set(Calendar.MINUTE, 0);
		endCal.set(Calendar.SECOND, 0);
		endCal.set(Calendar.MILLISECOND, 0);
		this.policyEndDate = endCal.getTime();

		Calendar startCal = (Calendar) endCal.clone();// for start date
		startCal.add(Calendar.DAY_OF_MONTH, -7);
		this.policyStartDate = startCal.getTime();
	}

	// Search Method
	public void search() {
		try {
			setMotorPolicyEnquiry();
//			ValidationResult result = motorPolicyValidator.validate(mp, true);
//			ValidationResult result1 = motorPolicyVehicleEnquiryValidator.validate(mpv, true);
			System.out.println("startdate: " + policyStartDate + ", enddate: " + policyEndDate + ", policyNo: "
					+ policyNo + ", registrationNo: " + registrationNo);
			// Validate only if input is provided
			if (policyNo != null && !policyNo.trim().isEmpty()) {
				policyResult = motorPolicyValidator.validate(mp, true);
				if (!policyResult.isVerified()) {
					valid = false;
				}
			}

			if (registrationNo != null && !registrationNo.trim().isEmpty()) {
				vehicleResult = motorPolicyVehicleEnquiryValidator.validate(mpv, true);
				if (!vehicleResult.isVerified()) {
					valid = false;
				}
			}

			if (valid) {
				results = policyEnquiryService.search(policyStartDate, policyEndDate, policyNo, registrationNo);
				System.out.println("success__________________________");
			} else {
				System.out.println("Validation failed for vehicle.");
				for (ErrorMessage e : policyResult.getErrorMeesages()) {
					addErrorMessage(null, e.getErrorcode(), e.getParams());
					System.out.println("Validation failed for vehicle1.");
				}
				for (ErrorMessage e : vehicleResult.getErrorMeesages()) {
					addErrorMessage(null, e.getErrorcode(), e.getParams());
					System.out.println("Validation failed for vehicle2.");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reset() {
		this.policyStartDate = null;
		this.policyEndDate = null;
		this.policyNo = null;
		this.registrationNo = null;
		this.results = new ArrayList<>();
	}

	public void setMotorPolicyEnquiry() {
		mp.setPolicyStartDate(policyStartDate);
		mp.setPolicyEndDate(policyEndDate);
		mp.setPolicyNo(policyNo);
		mpv.setRegistrationNo(registrationNo);
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
//
//	public List<MotorPolicyVehicleLink> getVehicleLinkResults() {
//		return results;
//	}
//
//	public void setVehicleLinkResults(List<MotorPolicyVehicleLink> vehicleLinkResults) {
//		this.results = vehicleLinkResults;
//	}

	public List<MotorEnquiryDTO> getResults() {
		return results;
	}

	public void setResults(List<MotorEnquiryDTO> results) {
		this.results = results;
	}

	public MotorPolicy getMp() {
		return mp;
	}

	public void setMp(MotorPolicy mp) {
		this.mp = mp;
	}

	public MotorPolicyVehicleLink getMpv() {
		return mpv;
	}

	public void setMpv(MotorPolicyVehicleLink mpv) {
		this.mpv = mpv;
	}

}