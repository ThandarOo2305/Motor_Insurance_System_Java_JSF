package org.ace.accounting.system.motor.service.interfaces;

import java.util.Date;
import java.util.List;

import org.ace.accounting.system.motor.MotorEnquiryDTO;
import org.ace.accounting.system.motor.MotorPolicy;

public interface IMotorEnquiryService {
    List<MotorEnquiryDTO> search(Date policyStartDateFrom, Date policyStartDateTo, String policyNo, String registrationNo);
    
    public MotorPolicy findByPolicyNo(String policyNo);
	
}
