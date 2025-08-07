package org.ace.accounting.system.motor.service;

import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.system.motor.MotorPolicy;
import org.ace.accounting.system.motor.persistence.interfaces.IMotorPolicyDAO;
import org.ace.accounting.system.motor.service.interfaces.IMotorPolicyService;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "MotorPolicyService")
public class MotorPolicyService extends BaseService implements IMotorPolicyService {

	@Resource(name = "MotorPolicyDAO")
	private IMotorPolicyDAO motorPolicyDAO;

	// Read ( Find all motor )
	@Transactional(propagation = Propagation.REQUIRED)
	public List<MotorPolicy> findAllMotorPolicies() throws SystemException {
		List<MotorPolicy> result = null;
		try {
			result = motorPolicyDAO.findAll();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of Motor Policy)", e);
		}
		return result;
	}

	// Read ( find by customer name )
	@Transactional(propagation = Propagation.REQUIRED)
	public MotorPolicy findMotorPolicyByCustomerName(String customerName) throws SystemException {
		try {
			return motorPolicyDAO.findByCustomerName(customerName);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find MotorPolicy by customer name", e);
		}
	}
	
	// Read ( find by policy number )
	@Transactional(propagation = Propagation.REQUIRED)
	public MotorPolicy findMotorPolicyByPolicyNo(String policyNo) throws SystemException {
	    try {
	        return motorPolicyDAO.findByPolicyNo(policyNo);
	    } catch (DAOException e) {
	        throw new SystemException(e.getErrorCode(), "Failed to find MotorPolicy by policy number", e);
	    }
	}

	// Create
	@Transactional(propagation = Propagation.REQUIRED)
	public void addNewMotorPolicy(MotorPolicy motorPolicy) throws SystemException {
		try {
			motorPolicyDAO.insert(motorPolicy);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to insert Motor Policy", e);
		}
	}

	// Update
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateMotorPolicy(MotorPolicy motorPolicy) throws SystemException {
		try {
			motorPolicyDAO.update(motorPolicy);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to update Motor Policy", e);
		}
	}

	// Delete
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteMotorPolicy(MotorPolicy motorPolicy) throws SystemException {
		try {
			motorPolicyDAO.delete(motorPolicy);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to delete Motor Policy", e);
		}
	}
}