package org.ace.accounting.system.chartaccount.persistence;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.dto.AccountLedgerDto;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.chartaccount.persistence.interfaces.ICCOA_HistoryDAO;
import org.ace.accounting.system.currency.Currency;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("CCOA_HistoryDAO")
public class CCOA_HistoryDAO extends BasicDAO implements ICCOA_HistoryDAO {
	@Transactional(propagation = Propagation.REQUIRED)
	public BigDecimal finddblBalance(StringBuffer sf) throws DAOException {
		BigDecimal result = null;
		try {
			Query query = em.createNativeQuery(sf.toString());
			result = (BigDecimal) query.getSingleResult();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find CurrencyChartOfAccount History", pe);
		}
		return (result == null) ? BigDecimal.ZERO : result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<AccountLedgerDto> findDebitCreditBy(StringBuffer sf, Branch branch, Currency currency) throws DAOException {
		List<AccountLedgerDto> dtoList = null;
		try {

		} catch (PersistenceException pe) {
			throw translate("Failed to find Debit Credit By ", pe);
		}
		return dtoList;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public BigDecimal finddblBalance(StringBuffer sf, ChartOfAccount coa, String budgetYear, Currency currency, Branch branch) throws DAOException {
		BigDecimal result = null;
		try {
			Query query = em.createQuery(sf.toString());
			query.setParameter("coa", coa);
			query.setParameter("budgetYear", budgetYear);
			if (currency != null) {
				query.setParameter("currency", currency);
			}
			if (branch != null) {
				query.setParameter("branch", branch);
			}
			result = (BigDecimal) query.getSingleResult();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find CurrencyChartOfAccount", pe);
		}
		return (result == null) ? BigDecimal.ZERO : result;
	}

	@Override
	public List<CurrencyChartOfAccount> findAll() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}
}
