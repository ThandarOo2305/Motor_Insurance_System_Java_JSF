package org.ace.accounting.system.motor.persistence;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.system.motor.MotorPolicy;
import org.ace.accounting.system.motor.persistence.interfaces.IMotorPolicyDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("MotorPolicyDAO")
public class MotorPolicyDAO extends BasicDAO implements IMotorPolicyDAO {

	@SuppressWarnings("unchecked")

	// Read ( Find all motor )
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<MotorPolicy> findAll() throws DAOException {
		List<MotorPolicy> result = null;
		try {
			Query q = em.createNamedQuery("MotorPolicy.findAll");
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of Motor Policy", pe);
		}
		return result;
	}

	// Read ( find by policy number )
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public MotorPolicy findByPolicyNo(String policyNo) throws DAOException {
		MotorPolicy result = null;
		try {
			Query q = em.createNamedQuery("MotorPolicy.findByPolicyNo");
			q.setParameter("policyNo", policyNo);
			result = (MotorPolicy) q.getSingleResult();
			em.flush();
		} catch (NoResultException pe) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find policyNo(Policy Number = " + policyNo + ")", pe);
		}
		return result;
	}

	// Read ( return type Boolean for policyNo )
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public boolean existsByPolicyNo(String policyNo) throws DAOException {
		try {
			Query q = em.createNamedQuery("MotorPolicy.findByPolicyNo");
			q.setParameter("policyNo", policyNo);
			q.getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		} catch (PersistenceException pe) {
			throw translate("Failed to check existence of policyNo (Policy Number = " + policyNo + ")", pe);
		}
	}

	// Create
	@Transactional(propagation = Propagation.REQUIRED)
	public void insert(MotorPolicy motorPolicy) throws DAOException {
		try {
			em.persist(motorPolicy);
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to insert Motor Policy", pe);
		}
	}

	// Update
	@Transactional(propagation = Propagation.REQUIRED)
	public MotorPolicy update(MotorPolicy motorPolicy) throws DAOException {
		try {
			MotorPolicy updated = em.merge(motorPolicy);
			em.flush();
			return updated;
		} catch (PersistenceException pe) {
			throw translate("Failed to update Motor Policy", pe);
		}
	}

	// Delete
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(MotorPolicy motorPolicy) throws DAOException {
		try {
			MotorPolicy managedMotorPolicy = em.merge(motorPolicy);
			em.remove(managedMotorPolicy);
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to delete Motor Policy", pe);
		}
	}
}