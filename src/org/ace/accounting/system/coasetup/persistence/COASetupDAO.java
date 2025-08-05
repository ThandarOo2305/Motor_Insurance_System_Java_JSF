package org.ace.accounting.system.coasetup.persistence;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.coasetup.COASetup;
import org.ace.accounting.system.coasetup.persistence.interfaces.ICOASetupDAO;
import org.ace.accounting.system.currency.Currency;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("COASetupDAO")
public class COASetupDAO extends BasicDAO implements ICOASetupDAO {

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public COASetup findCOAByACNameAndCur(String acName, Currency currency, Branch branch) throws DAOException {

		List<COASetup> result = null;

		try {
			StringBuffer sF = new StringBuffer("SELECT c FROM COASetup c WHERE c.acName=:acName ");
			if (currency != null) {
				sF.append("AND c.currency=:currency");
			}
			if (branch != null) {
				sF.append(" AND c.branch=:branch ");
			}
			Query q = em.createQuery(sF.toString());
			q.setParameter("acName", acName);

			if (currency != null) {
				q.setParameter("currency", currency);
			}
			if (branch != null) {
				q.setParameter("branch", branch);
			}

			q.setFirstResult(0);
			q.setMaxResults(1);
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find COASetup  by" + acName + branch.getName() + currency.getCurrencyCode(), pe);
		}

		return result.size() >= 1 ? result.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<COASetup> findCOAListByACNameAndCur(String acName, Currency currency, Branch branch) throws DAOException {

		List<COASetup> result = null;

		try {
			StringBuffer sF = new StringBuffer("SELECT c FROM COASetup c WHERE c.acName=:acName  ");
			if (branch != null) {
				sF.append(" AND c.branch=:branch ");
			}

			if (currency != null) {
				sF.append("AND c.currency=:currency");
			}
			Query q = em.createQuery(sF.toString());
			q.setParameter("acName", acName);
			if (branch != null) {
				q.setParameter("branch", branch);
			}
			if (currency != null) {
				q.setParameter("currency", currency);
			}
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find COASetup  by" + acName + branch.getName() + currency.getCurrencyCode(), pe);
		}

		return result.size() >= 1 ? result : null;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<COASetup> findAllCashAccount() throws DAOException {
		List<COASetup> result = null;

		try {
			Query q = em.createNamedQuery("COASetup.findAllCashAccount");
			result = q.getResultList();
		} catch (PersistenceException pe) {
			throw translate("Failed to find All CashAccount", pe);
		}
		return result;
	}
}
