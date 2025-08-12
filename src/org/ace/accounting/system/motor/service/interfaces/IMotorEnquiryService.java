package org.ace.accounting.system.motor.service.interfaces;

import java.util.Date;
import java.util.List;


public interface IMotorEnquiryService {
	List<Object[]> searchPolicies(Date policyStartDateFrom, Date policyStartDateTo,
            String policyNo, String registrationNo);	
}
