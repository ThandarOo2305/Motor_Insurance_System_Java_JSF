package org.ace.accounting.system.setup.persistence;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.system.setup.Setup;
import org.ace.accounting.system.setup.persistence.interfaces.ISetupDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("SetupDAO")
public class SetupDAO extends BasicDAO implements ISetupDAO {

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Setup> findAll() throws DAOException {
		List<Setup> result = null;
		try {
			Query q = em.createNamedQuery("Setup.findAll");
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of User", pe);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public String findSetupValueByVariable(String variable) throws DAOException {
		String result = null;
		try {
			Query q = em.createNamedQuery("Setup.findSetupValueByVariable");
			q.setParameter("variable", variable);
			result = (String) q.getSingleResult();
			em.flush();
		} catch (NoResultException e) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of User", pe);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public String findSetupBudgetByVariable(String variable) throws DAOException {
		String result = null;
		try {
			Query q = em.createNamedQuery("Setup.findSetupBudgetByVariable");
			q.setParameter("variable", variable);
			result = (String) q.getSingleResult();
			em.flush();
		} catch (NoResultException e) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of User", pe);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateSetupValueByVariable(String variable, String value) {
		try {
			Query q = em.createNamedQuery("Setup.updateSetupValueByVariable");
			q.setParameter("variable", variable);
			q.setParameter("value", value);
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of User", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateSetupBudget(String budget) {
		try {
			Query q = em.createNamedQuery("Setup.updateSetupBudget");
			q.setParameter("budget", budget);
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to update", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void insert(String variable) throws DAOException {
		try {
			Query q = em.createNativeQuery("update Setup set value=?1 where variable=?2 and version= 1");
			q.setParameter(1, variable);
			q.setParameter(2, "EDITPASSWORD");
			q.executeUpdate();
			em.flush();

		} catch (PersistenceException pe) {
			throw translate("Failed to insert all of Password", pe);
		}

	}

}
