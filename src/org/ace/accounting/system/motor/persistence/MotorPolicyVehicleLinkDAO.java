package org.ace.accounting.system.motor.persistence;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.system.motor.MotorPolicyVehicleLink;
import org.ace.accounting.system.motor.persistence.interfaces.IMotorPolicyVehicleLinkDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("MotorPolicyVehicleLinkDAO")
public class MotorPolicyVehicleLinkDAO extends BasicDAO implements IMotorPolicyVehicleLinkDAO{
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation= Propagation.REQUIRED, readOnly=true)
	public List<MotorPolicyVehicleLink> findAll() throws DAOException {
		List<MotorPolicyVehicleLink> result=null;
		try {
			Query q = em.createNamedQuery("MotorPolicyVehicleLink.findAll");
			result=q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Vehicle not found!", pe);
		}
		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public MotorPolicyVehicleLink findByRegistrationNo(String registrationNo) throws DAOException {
		MotorPolicyVehicleLink result = null;
		try {
			Query q = em.createNamedQuery("MotorPolicyVehicleLink.findByRegistrationNo");
			result = (MotorPolicyVehicleLink) q.getSingleResult();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Vehicle not Found with RegistrationNo!", pe);
		}
		return result;
	   
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean existsByRegistrationNo(String registrationNo) throws DAOException {
	    boolean exists = false;
	    try {
	        String queryStr = "SELECT COUNT(mv) FROM MotorPolicyVehicleLink mv WHERE mv.registrationNo = :registrationNo";
	        Query query = em.createQuery(queryStr);
	        query.setParameter("registrationNo", registrationNo);

	        Long count = (Long) query.getSingleResult();
	        exists = count != null && count > 0;
	    } catch (PersistenceException pe) {
	        throw translate("Failed to validate registration number: " + registrationNo, pe);
	    }
	    return exists;
	}
	

}
