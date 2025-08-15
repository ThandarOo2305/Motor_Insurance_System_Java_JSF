package org.ace.accounting.system.motor.service.interfaces;

import java.util.List;

import org.ace.accounting.system.motor.MotorPolicy;
import org.ace.java.component.SystemException;

public interface IMotorPolicyService {
	
	public List<MotorPolicy> findAllMotorPolicies() throws SystemException;
	
	public MotorPolicy findMotorPolicyByPolicyNo(String policyNo) throws SystemException;
	
	public boolean existsMotorPolicyByPolicyNo(String policyNo) throws SystemException;
	
	public void addNewMotorPolicy(MotorPolicy motorPolicy) throws SystemException;

	public void updateMotorPolicy(MotorPolicy motorPolicy) throws SystemException;

	public void deleteMotorPolicy(MotorPolicy motorPolicy) throws SystemException;
	
	public String generateProposalNo() throws SystemException;
}