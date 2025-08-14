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
import org.ace.accounting.system.motor.MotorEnquiry;
import org.ace.accounting.system.motor.MotorEnquiryDTO;
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

	@ManagedProperty(value = "#{MotorEnquiryValidator}")
	private IDataValidator<MotorEnquiry> enquiryValidator;
	
	public void setEnquiryValidator(IDataValidator<MotorEnquiry> enquiryValidator) {
		this.enquiryValidator = enquiryValidator;
	}

	private Date policyStartDate;
	private Date policyEndDate;
	private String policyNo;
	private String registrationNo;
	private MotorEnquiry mpq;
	// Search Result Lists
	private List<MotorEnquiryDTO> results;
	// init
	@PostConstruct
	public void init() {
		setDefaultDates();
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
			setMotorEnquiry();
			
			ValidationResult result = enquiryValidator.validate(mpq, true);
			
			System.out.println("startdate: " + policyStartDate + ", enddate: " + policyEndDate + ", policyNo: "
					+ policyNo + ", registrationNo: " + registrationNo);
			if (result.isVerified()) {
				
				results = policyEnquiryService.search(policyStartDate, policyEndDate, policyNo, registrationNo);
				
				System.out.println("success__________________________");
			} else {
				System.out.println("Validation failed for vehicle.");
//				for(ErrorMessage e : result.getErrorMeesages()) {
//					addErrorMessage(null, e.getErrorcode(), e.getParams());
//					System.out.println(e.getErrorcode().toString() + "::::" + e.getParams().toString());
//				}	
		        for(ErrorMessage e: result.getErrorMeesages()) {
		        	addErrorMessage(null , e.getErrorcode(), e.getParams());
		        }
		}		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reset() {
		this.policyStartDate = null;
		this.policyEndDate = null;
		this.policyNo = null;
		this.registrationNo = null;
		this.results = new ArrayList<>();
		this.mpq = new MotorEnquiry();
	}

	public void setMotorEnquiry() {
		mpq = new MotorEnquiry();
		mpq.setPolicyStartDate(policyStartDate);
		mpq.setPolicyEndDate(policyEndDate);
		mpq.setPolicyNo(policyNo);
		mpq.setRegistrationNo(registrationNo);
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

	public List<MotorEnquiryDTO> getResults() {
		return results;
	}

	public void setResults(List<MotorEnquiryDTO> results) {
		this.results = results;
	}

	public MotorEnquiry getMpq() {
		return mpq;
	}

	public void setMpq(MotorEnquiry mpq) {
		this.mpq = mpq;
	}

	

}