package org.ace.accounting.system.motor.service.interfaces;

import java.util.Date;
import java.util.List;

import org.ace.accounting.system.motor.MotorPolicyVehicleLink;

public interface IMotorEnquiryService {
    List<MotorPolicyVehicleLink> search(Date policyStartDateFrom, Date policyStartDateTo, String policyNo, String registrationNo);
	
}
