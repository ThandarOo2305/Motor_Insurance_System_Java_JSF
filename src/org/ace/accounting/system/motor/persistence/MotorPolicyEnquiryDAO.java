package org.ace.accounting.system.motor.persistence;

import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.ace.accounting.system.motor.MotorEnquiryDTO;
import org.ace.accounting.system.motor.MotorPolicy;
import org.ace.accounting.system.motor.persistence.interfaces.IMotorPolicyEnquiryDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("MotorPolicyEnquiryDAO")
public class MotorPolicyEnquiryDAO extends BasicDAO implements IMotorPolicyEnquiryDAO {

	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<MotorEnquiryDTO> search(String sqlquery, Map<String, Object> params) throws DAOException {
//		List<MotorPolicyVehicleLink> result = null;
		try {
//			Query q = em.createQuery(sqlquery);
			
			TypedQuery<MotorEnquiryDTO> q= em.createQuery(sqlquery, MotorEnquiryDTO.class);
			// set parameters for query
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				q.setParameter(entry.getKey(), entry.getValue());
			}

			return q.getResultList();
		} catch (PersistenceException pe) {
			throw translate("Failed to find", pe);
		}
//		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public MotorPolicy findByPolicyNo(String policyNo) throws DAOException {
	    try {
	        String jpql = "SELECT DISTINCT m FROM MotorPolicy m " +
	                      "LEFT JOIN FETCH m.vehicleLinks mv " +
	                      "WHERE m.policyNo = :policyNo";

	        TypedQuery<MotorPolicy> query = em.createQuery(jpql, MotorPolicy.class);
	        query.setParameter("policyNo", policyNo);

	        return query.getSingleResult();
	    } catch (NoResultException e) {
	        return null;
	    } catch (PersistenceException pe) {
	        throw translate("Failed to find MotorPolicy with policyNo: " + policyNo, pe);
	    }
	}


}
