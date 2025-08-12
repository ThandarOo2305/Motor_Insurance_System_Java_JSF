package org.ace.accounting.system.motor.persistence.interfaces;

import java.util.Date;
import java.util.List;


public interface IMotorPolicyEnquiryDAO {
	List<Object[]> searchPolicies(Date policyStartDateFrom, Date policyStartDateTo,
	            String policyNo, String registrationNo);
}
