package org.ace.accounting.system.motor.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.system.motor.persistence.interfaces.IMotorPolicyEnquiryDAO;
import org.ace.accounting.system.motor.service.interfaces.IMotorEnquiryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "MotorEnquiryService")
public class MotorEnquiryService implements IMotorEnquiryService{
	
	@Resource(name = "MotorPolicyEnquiryDAO")
	private IMotorPolicyEnquiryDAO motorPolicyEnquiryDAO;


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	@Override
	public List<Object[]> searchPolicies(Date policyStartDateFrom, Date policyStartDateTo, String policyNo,
			String registrationNo) {
		
		return motorPolicyEnquiryDAO.searchPolicies(policyStartDateFrom, policyStartDateTo, policyNo, registrationNo);

	}

}
