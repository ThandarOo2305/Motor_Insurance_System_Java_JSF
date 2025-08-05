package org.ace.accounting.system.setup.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.system.setup.SetupHistory;
import org.ace.accounting.system.setup.persistence.interfaces.ISetup_HistoryDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("Setup_HistoryDAO")
public class Setup_HistoryDAO extends BasicDAO implements ISetup_HistoryDAO {

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<SetupHistory> findAll() throws DAOException {
		List<SetupHistory> result = null;
		try {
			Query q = em.createNamedQuery("SetupHistory.findAll");
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of User", pe);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public String findSetupHistoryValueByVariable(String variable, String budget) throws DAOException {
		String result = null;
		try {
			Query q = em.createNamedQuery("SetupHistory.findSetupHistoryValueByVariable");
			q.setParameter("variable", variable);
			q.setParameter("budget", budget);
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
	public void updateSetupHistoryValueByVariable(String variable, String value) {
		try {
			Query q = em.createNamedQuery("SetupHistory.updateSetupValueByVariable");
			q.setParameter("variable", variable);
			q.setParameter("value", value);
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of User", pe);
		}
	}

	// Search for Budget info from prev_setup
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Object[]> findbudgetvalue(Date reportDate) throws DAOException {
		List<Object[]> result = null;
		try {
			StringBuffer queryString = new StringBuffer();
			queryString.append("Select value,BUDGET from prev_setup where VARIABLE ='BUDGETSDATE' or VARIABLE ='BUDGETEDATE' group by VALUE,BUDGET order by BUDGET");
			Query q = em.createNativeQuery(queryString.toString());
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find prev_setup", pe);
		}
		return result;
	}

	// To find for Budget year from prev_setup
	@Transactional(propagation = Propagation.REQUIRED)
	public String findbudgetyear(Date reportDate) throws DAOException {
		String budgetYear = null;
		List<Object[]> budgetvalue = new ArrayList<>();
		try {

			budgetvalue = findbudgetvalue(reportDate);
			for (int i = 0; i <= budgetvalue.size() - 1; i += 2) {
				// DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				Date startDate = null;
				Date endDate = null;
				startDate = DateUtils.formatStringToDate(budgetvalue.get(i)[0].toString());
				endDate = DateUtils.formatStringToDate(budgetvalue.get(i + 1)[0].toString());

				if (reportDate.compareTo(startDate) == 0 || reportDate.compareTo(startDate) == 1 && reportDate.compareTo(endDate) == -1 || reportDate.compareTo(endDate) == 0) {
					budgetYear = budgetvalue.get(i)[1].toString();
				}

			}
		} catch (DAOException de) {
			throw translate("Failed to find prev_setup", de);
		}
		return budgetYear;
	}

}
