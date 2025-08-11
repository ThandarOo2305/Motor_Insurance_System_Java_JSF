package org.ace.accounting.system.motor.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.ace.accounting.system.motor.MotorPolicyVehicleLink;
import org.ace.accounting.system.motor.persistence.interfaces.IMotorPolicyEnquiryDAO;
import org.ace.accounting.system.motor.service.interfaces.IMotorEnquiryService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class MotorEnquiryService implements IMotorEnquiryService{
	
	@Resource(name = "MotorPolicyEnquiryDAO")
	private IMotorPolicyEnquiryDAO motorPolicyEnquiryDAO;


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<MotorPolicyVehicleLink> search(Date policyStartDateFrom, Date policyStartDateTo, String policyNo,
			String registrationNo) {
			StringBuilder hql = new StringBuilder("SELECT m FROM MotorPolicyVehicleLink m WHERE 1=1 ");
	        Map<String, Object> paramMap = new HashMap<>();
	
	        if (policyStartDateFrom != null) {
	            hql.append(" AND m.policyStartDate >= :startDateFrom");
	            paramMap.put("startDateFrom", policyStartDateFrom);
	        }
	
	        if (policyStartDateTo != null) {
	            hql.append(" AND m.policyStartDate <= :startDateTo");
	            paramMap.put("startDateTo", policyStartDateTo);
	        }
	
	        if (policyNo != null && !policyNo.isEmpty()) {
	            hql.append(" AND m.policyNo = :policyNo");
	            paramMap.put("policyNo", policyNo);
	        }
	
	        if (registrationNo != null && !registrationNo.isEmpty()) {
	            hql.append(" AND m.registrationNo = :registrationNo");
	            paramMap.put("registrationNo", registrationNo);
	        }
	
	        return motorPolicyEnquiryDAO.search(hql.toString(), paramMap);

	}

}
