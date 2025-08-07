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
	public void insert(MotorPolicyVehicleLink link) throws DAOException {
		try {
			em.persist(link);
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to insert VehicleLink", pe);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public MotorPolicyVehicleLink update(MotorPolicyVehicleLink link) throws DAOException {
		try {
			em.merge(link);
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to update Vehicle", pe);
		}
		return link;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(MotorPolicyVehicleLink link) throws DAOException {
		try {
			MotorPolicyVehicleLink updatedMotorPolicyVehicleLink= em.merge(link);
			em.remove(updatedMotorPolicyVehicleLink);
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to delete VehicleLink", pe);
		}
	}
	

}
