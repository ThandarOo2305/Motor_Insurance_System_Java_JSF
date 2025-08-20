package org.ace.accounting.system.motor.persistence.interfaces;

import java.util.List;
import java.util.Map;

import org.ace.accounting.system.motor.MotorEnquiryDTO;
import org.ace.accounting.system.motor.MotorPolicy;

public interface IMotorPolicyEnquiryDAO {
	public List<MotorEnquiryDTO> search(String sqlquery, Map<String, Object> params);

	public MotorPolicy findByPolicyNo(String policyNo);
}
