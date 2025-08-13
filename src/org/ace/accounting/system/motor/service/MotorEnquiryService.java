package org.ace.accounting.system.motor.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.ace.accounting.system.motor.MotorEnquiryDTO;
import org.ace.accounting.system.motor.persistence.interfaces.IMotorPolicyEnquiryDAO;
import org.ace.accounting.system.motor.service.interfaces.IMotorEnquiryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "MotorEnquiryService")
public class MotorEnquiryService implements IMotorEnquiryService {

	@Resource(name = "MotorPolicyEnquiryDAO")
	private IMotorPolicyEnquiryDAO motorPolicyEnquiryDAO;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<MotorEnquiryDTO> search(Date policyStartDateFrom, Date policyStartDateTo, String policyNo,
			String registrationNo) {
//		StringBuilder hql = new StringBuilder("SELECT mv FROM MotorPolicyVehicleLink mv JOIN mv.motorPolicy mp WHERE 1=1 ");
//		Map<String, Object> paramMap = new HashMap<>();
//
//		if (policyStartDateFrom != null) {
//			hql.append(" AND mp.policyStartDate >= :startDateFrom");
//			paramMap.put("startDateFrom", policyStartDateFrom);
//		}
//
//		if (policyStartDateTo != null) {
//			hql.append(" AND mp.policyStartDate <= :startDateTo");
//			paramMap.put("startDateTo", policyStartDateTo);
//		}
//
//		if (policyNo != null && !policyNo.isEmpty()) {
//			hql.append(" AND mp.policyNo = :policyNo");
//			paramMap.put("policyNo", policyNo);
//		}
//
//		if (registrationNo != null && !registrationNo.isEmpty()) {
//			hql.append(" AND mv.registrationNo = :registrationNo");
//			paramMap.put("registrationNo", registrationNo);
//		}
//		System.out.println("query:" + hql.toString());
//		return motorPolicyEnquiryDAO.search(hql.toString(), paramMap);
//	}
		
		Map<String, Object> paramMap = new HashMap<>();
		StringBuffer hql = new StringBuffer("SELECT new org.ace.accounting.system.motor.MotorEnquiryDTO(");
		hql.append("mp.policyNo, ")
		   .append("mv.registrationNo, ")
		   .append("mp.saleChannel, ")
		   .append("mp.customerName, ")
		   .append("mp.branch, ")
		   .append("mv.claimCount, ")
		   .append("mv.sumInsured, ")
		   .append("mv.basicTermPremium, ")
		   .append("mp.submittedDate) ")
		   .append("FROM MotorPolicyVehicleLink mv ")
		   .append("JOIN mv.motorPolicy mp ")
		   .append("WHERE 1=1 ");

        if (policyStartDateFrom != null) {
            hql.append(" AND mp.policyStartDate >= :policyStartDateFrom");
            paramMap.put("policyStartDateFrom", policyStartDateFrom);
        }
        if (policyStartDateTo != null) {
            hql.append(" AND mp.policyStartDate <= :policyStartDateTo");
            paramMap.put("policyStartDateTo", policyStartDateTo);
        }
        if (policyNo != null && !policyNo.isEmpty()) {
            hql.append(" AND mp.policyNo = :policyNo");
            paramMap.put("policyNo", policyNo);
            
        }
        if (registrationNo != null && !registrationNo.isEmpty()) {
            hql.append(" AND mv.registrationNo = :registrationNo");
            paramMap.put("registrationNo", registrationNo);
        }

        
        return motorPolicyEnquiryDAO.search(hql.toString(), paramMap);
	}
}
