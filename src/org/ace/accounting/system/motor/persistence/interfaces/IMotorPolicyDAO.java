package org.ace.accounting.system.motor.persistence.interfaces;

import java.util.List;

import org.ace.accounting.system.motor.MotorPolicy;
import org.ace.java.component.persistence.exception.DAOException;

public interface IMotorPolicyDAO {
	
	public List<MotorPolicy> findAll() throws DAOException;
	
	public MotorPolicy findByPolicyNo(String policyNo) throws DAOException;
	
	public MotorPolicy findByCustomerName(String customerName) throws DAOException;
	
	public void insert(MotorPolicy motorPolicy) throws DAOException;
	
	public MotorPolicy update(MotorPolicy motorPolicy) throws DAOException;
	
	public void delete(MotorPolicy motorPolicy) throws DAOException;
}