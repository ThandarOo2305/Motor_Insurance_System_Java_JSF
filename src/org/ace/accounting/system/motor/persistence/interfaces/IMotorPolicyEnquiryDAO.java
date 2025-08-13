package org.ace.accounting.system.motor.persistence.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ace.accounting.system.motor.MotorEnquiryDTO;
import org.ace.accounting.system.motor.MotorPolicyVehicleLink;

public interface IMotorPolicyEnquiryDAO {
	public List<MotorEnquiryDTO> search(String sqlquery, Map<String, Object> params);
}
