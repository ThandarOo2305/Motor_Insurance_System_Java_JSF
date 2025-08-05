package org.ace.accounting.common.persistence;

import java.math.BigDecimal;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.common.interfaces.ITranValiDAO;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.department.Department;
import org.ace.accounting.system.tlf.TLF;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("TranValiDAO")
public class TranValiDAO extends BasicDAO implements ITranValiDAO {

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean isCurUsed(Currency currency) throws DAOException {
		StringBuffer buffer = null;
		Number result = null;
		Query query = null;
		try {
			buffer = new StringBuffer();
			buffer.append("SELECT SUM(ccoa.oBal) FROM CurrencyChartOfAccount ccoa WHERE ccoa.currency.id = :currencyId");
			query = em.createQuery(buffer.toString());
			query.setParameter("currencyId", currency.getId());
			result = (Number) query.getSingleResult();
			result = (result == null) ? 0 : result;
			if (result.doubleValue() > 0) {
				return true;
			}

			buffer = new StringBuffer();
			buffer.append("SELECT COUNT(t.id) FROM TLF t WHERE t.currency.id = :currencyId AND t.reverse = false");
			query = em.createQuery(buffer.toString());
			query.setParameter("currencyId", currency.getId());
			result = (Number) query.getSingleResult();
			if (result.doubleValue() > 0) {
				return true;
			}

			buffer = new StringBuffer();
			buffer.append("SELECT COUNT(t.id) FROM TLFHIST t WHERE t.currency.id = :currencyId AND t.reverse = false");
			query = em.createQuery(buffer.toString());
			query.setParameter("currencyId", currency.getId());
			result = (Number) query.getSingleResult();
			if (result.doubleValue() > 0) {
				return true;
			}

			buffer = new StringBuffer();
			buffer.append("SELECT COUNT(c.id) FROM COASetup c WHERE c.currency.id = :currencyId");
			query = em.createQuery(buffer.toString());
			query.setParameter("currencyId", currency.getId());
			result = (Number) query.getSingleResult();
			if (result.doubleValue() > 0) {
				return true;
			}
		} catch (PersistenceException pe) {
			throw translate("Failed to find Currency Used Transaction", pe);
		}

		return false;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public boolean isCoaUsed(String coaId) throws DAOException {

		boolean used = false;

		try {
			Query q = em.createNamedQuery("ChartOfAccount.findOBALUsedCOA");
			q.setParameter("coaId", coaId);
			BigDecimal result = (BigDecimal) q.getSingleResult();
			if (result != null && result.compareTo(BigDecimal.ZERO) != 0) {
				used = true;
			} else {
				Query tlfQuery = em.createNamedQuery("ChartOfAccount.findTLFUsedCOA");
				tlfQuery.setParameter("coaId", coaId);
				tlfQuery.setMaxResults(1);
				TLF tlf = (TLF) tlfQuery.getSingleResult();

				if (tlf != null) {
					used = true;
				}
			}
		} catch (NoResultException re) {
			return false;
		} catch (PersistenceException pe) {
			throw translate("Failed to find ChartOfAccount Used Transaction", pe);
		}

		return used;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean isDepartmentUsed(Department department) throws DAOException {

		boolean used = false;

		try {

			Query q = em.createNamedQuery("Department.findCCOAUsedDept");
			q.setParameter("department", department);
			q.setMaxResults(1);

			CurrencyChartOfAccount ccoa = (CurrencyChartOfAccount) q.getSingleResult();

			if (ccoa != null) {
				used = true;
			}

		} catch (NoResultException re) {

			return false;
		} catch (PersistenceException pe) {

			throw translate("Failed to find Department Used Transaction", pe);
		}

		return used;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean isBranchUsed(Branch branch) throws DAOException {
		try {
			Number result = null;
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT COUNT(DISTINCT u) FROM User u WHERE u.branch.id = :branchId ");
			Query query = em.createQuery(buffer.toString());
			query.setParameter("branchId", branch.getId());
			result = (Number) query.getSingleResult();
			if (result.doubleValue() > 0) {
				return true;
			}

			buffer = new StringBuffer();
			buffer.append("SELECT SUM(ccoa.oBal) FROM CurrencyChartOfAccount ccoa WHERE ccoa.branch.id = :branchId");
			query = em.createQuery(buffer.toString());
			query.setParameter("branchId", branch.getId());
			result = (Number) query.getSingleResult();
			result = (result == null) ? 0 : result;
			if (result.doubleValue() > 0) {
				return true;
			}

			buffer = new StringBuffer();
			buffer.append(" SELECT COUNT(t.id) FROM TLF t WHERE t.branch.id = :branchId  AND t.reverse = false ");
			query = em.createQuery(buffer.toString());
			query.setParameter("branchId", branch.getId());
			result = (Number) query.getSingleResult();
			if (result.doubleValue() > 0) {
				return true;
			}

			buffer = new StringBuffer();
			buffer.append(" SELECT COUNT(t.id) FROM TLFHIST t WHERE t.branch.id = :branchId  AND t.reverse = false ");
			query = em.createQuery(buffer.toString());
			query.setParameter("branchId", branch.getId());
			result = (Number) query.getSingleResult();
			if (result.doubleValue() > 0) {
				return true;
			}

		} catch (PersistenceException pe) {
			throw translate("Failed to find Branch Used Transaction", pe);
		}

		return false;

	}
}
