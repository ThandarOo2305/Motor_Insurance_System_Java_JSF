package org.ace.accounting.system.motor.persistence;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.ace.accounting.system.motor.MotorPolicyVehicleLink;
import org.ace.accounting.system.motor.persistence.interfaces.IMotorPolicyEnquiryDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("MotorPolicyEnquiryDAO")
public class MotorPolicyEnquiryDAO extends BasicDAO implements IMotorPolicyEnquiryDAO {

	

    @Override
    @SuppressWarnings("unchecked")
	@Transactional(propagation= Propagation.REQUIRED, readOnly=true)
    public List<Object[]> searchPolicies(Date policyStartDateFrom, Date policyStartDateTo,
                                         String policyNo, String registrationNo) {

        StringBuffer hql = new StringBuffer("SELECT mp.policyNo, mp.proposalNo, mp.registrationNo, mp.saleChannel, ");
        hql.append("mp.salePerson, mp.customer, mp.branch, mp.claimCount, mp.totalSumInsured, ");
        hql.append("mp.basicPremium, mp.submittedDate, mp.createdUser ");
        hql.append("FROM MotorPolicy mp WHERE 1=1");

        if (policyStartDateFrom != null) {
            hql.append(" AND mp.policyStartDate >= :policyStartDateFrom");
        }
        if (policyStartDateTo != null) {
            hql.append(" AND mp.policyStartDate <= :policyStartDateTo");
        }
        if (policyNo != null && !policyNo.isEmpty()) {
            hql.append(" AND mp.policyNo = :policyNo");
        }
        if (registrationNo != null && !registrationNo.isEmpty()) {
            hql.append(" AND mp.registrationNo = :registrationNo");
        }

        Query query = em.createQuery(hql.toString());

        if (policyStartDateFrom != null) {
            query.setParameter("policyStartDateFrom", policyStartDateFrom);
        }
        if (policyStartDateTo != null) {
            query.setParameter("policyStartDateTo", policyStartDateTo);
        }
        if (policyNo != null && !policyNo.isEmpty()) {
            query.setParameter("policyNo", policyNo);
        }
        if (registrationNo != null && !registrationNo.isEmpty()) {
            query.setParameter("registrationNo", registrationNo);
        }

        return query.getResultList();
    }

}
