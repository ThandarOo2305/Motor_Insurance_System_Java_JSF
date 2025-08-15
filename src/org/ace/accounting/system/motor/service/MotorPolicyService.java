package org.ace.accounting.system.motor.service;

import java.time.LocalDate;
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
	
	// Read ( find by policy number )
	@Transactional(propagation = Propagation.REQUIRED)
	public MotorPolicy findMotorPolicyByPolicyNo(String policyNo) throws SystemException {
	    try {
	        return motorPolicyDAO.findByPolicyNo(policyNo);
	    } catch (DAOException e) {
	        throw new SystemException(e.getErrorCode(), "Failed to find MotorPolicy by policy number", e);
	    }
	}
	 
	// Read ( return type Boolean for policyNo )
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public boolean existsMotorPolicyByPolicyNo(String policyNo) throws SystemException {
	    try {
	        return motorPolicyDAO.existsByPolicyNo(policyNo);
	    } catch (DAOException e) {
	        throw new SystemException(e.getErrorCode(), "Failed to check existence of MotorPolicy by policy number", e);
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
	
	@Override
	public String generateProposalNo() throws SystemException {
	    LocalDate now = LocalDate.now();
	    String monthYear = String.format("%02d-%d", now.getMonthValue(), now.getYear());
	    String prefix = "MRT/PO/";

	    String lastProposalNo = motorPolicyDAO.findLastProposalNoByMonthYear(monthYear);

	    int nextNumber = 1;
	    if (lastProposalNo != null && lastProposalNo.trim().startsWith(prefix)) {
	        String[] parts = lastProposalNo.trim().split("/");
	        if (parts.length >= 4) {
	            try {
	                nextNumber = Integer.parseInt(parts[2].trim()) + 1;
	            } catch (NumberFormatException ignored) {
	                nextNumber = 1;
	            }
	        }
	    }

	    return String.format("%s%06d/%s", prefix, nextNumber, monthYear);
	}

}