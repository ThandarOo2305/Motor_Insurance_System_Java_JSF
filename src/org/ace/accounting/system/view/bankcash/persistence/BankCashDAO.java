package org.ace.accounting.system.view.bankcash.persistence;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.dto.BankCashDto;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.coasetup.COASetup;
import org.ace.accounting.system.coasetup.persistence.interfaces.ICOASetupDAO;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.view.bankcash.persistence.interfaces.IBankCashDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**************************************************************************
 * $21-04-2016$ $Thaw Oo Khaing$ $1$ ACEPLUS SOLUTIONS CO., Ltd.
 *************************************************************************/

@Repository("BankCashDAO")
public class BankCashDAO extends BasicDAO implements IBankCashDAO {

	@Resource(name = "COASetupDAO")
	private ICOASetupDAO coaSetupDAO;

	/**
	 * [ To find the sum of opening balance on the report date , get closing
	 * balance of the previous month first and find the cash TLF between the
	 * start of the month and one day before report date. ]
	 * 
	 * @param CurrencyType
	 *            type [To decide home amount or source amount]
	 * @param Currency
	 *            currency [To filter the result by currency]
	 * @param Date
	 *            reportDate [Report Date]
	 * @param boolean
	 *            curConverted [To decide the request amount is converted amount
	 *            for local currency]
	 * @param Branch
	 *            branch [To filter the result by branch]
	 * 
	 * 
	 * @return [Return A BigDecimal amount]
	 * 
	 * @throws [Exception
	 *             DAOException]
	 */
	@Override
	@SuppressWarnings("unchecked")
	public BigDecimal findOpeningBalance(CurrencyType type, Currency currency, Date reportDate, boolean curConverted, Branch branch) {
		BigDecimal result = BigDecimal.ZERO;
		try {
			String openingBalanceField = getOpeningBalanceField(type, reportDate, curConverted);
			StringBuffer queryString = new StringBuffer("SELECT SUM(c." + openingBalanceField + ") FROM VwCcoa c WHERE c.coa in :cashCoaList AND c.budget=:budget ");
			if (branch != null) {
				queryString.append(" AND c.branch=:branch ");
			}

			if (type.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
				queryString.append(" AND c.currency=:currency ");
			}

			Query q = em.createQuery(queryString.toString());
			List<ChartOfAccount> cashCoaList = getCashCoaList(currency, branch);
			q.setParameter("cashCoaList", cashCoaList);
			q.setParameter("budget", BusinessUtil.getBudgetInfo(reportDate, 2));
			if (branch != null) {
				q.setParameter("branch", branch);
			}
			if (type.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
				q.setParameter("currency", currency);
			}
			result = (BigDecimal) q.getSingleResult();

			String field;
			if (curConverted || type.equals(CurrencyType.HOMECURRENCY)) {
				field = "homeAmount";
			} else {
				field = "localAmount";
			}

			queryString = new StringBuffer("SELECT CASE WHEN v.status IN ('CDV','TDV') THEN v." + field + " ELSE -1* v." + field
					+ " END AS OBAL FROM VwTLF v WHERE v.ccoa.coa IN :cashCoaList AND " + " CAST(v.settlementDate AS DATE) BETWEEN :startDate AND :endDate ");
			if (branch != null) {
				queryString.append(" AND v.branch=:branch ");
			}

			if (type.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
				queryString.append(" AND v.currency=:currency ");
			}
			q = em.createQuery(queryString.toString());
			q.setParameter("cashCoaList", cashCoaList);
			Calendar cal = Calendar.getInstance();
			cal.setTime(reportDate);
			Date endDate = DateUtils.minusDays(cal.getTime(), 1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			Date startDate = cal.getTime();
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
			if (branch != null) {
				q.setParameter("branch", branch);
			}
			if (type.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
				q.setParameter("currency", currency);
			}
			List<BigDecimal> tlfBalance = q.getResultList().equals(null) ? new ArrayList<>() : q.getResultList();
			BigDecimal tlfResult = BigDecimal.ZERO;
			for (BigDecimal b : tlfBalance) {
				tlfResult = tlfResult.add(b);
			}
			if (null != result) {
				result = result.add(tlfResult);
			}
			em.flush();
		} catch (NoResultException e) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find opening balance for bank cash.", pe);
		}
		return result;
	}

	/**
	 * [ To find the sum of total transfer TLF on the report date. ]
	 * 
	 * @param CurrencyType
	 *            type [To decide home amount or source amount]
	 * @param Currency
	 *            currency [To filter the result by currency]
	 * @param Date
	 *            reportDate [Report Date]
	 * @param boolean
	 *            curConverted [To decide the request amount is converted amount
	 *            for local currency]
	 * @param Branch
	 *            branch [To filter the result by branch]
	 * 
	 * 
	 * @return [Return A BigDecimal amount]
	 * 
	 * @throws [Exception
	 *             DAOException]
	 */
	@Override
	public BigDecimal findTotalTransfer(CurrencyType type, Currency currency, Date reportDate, boolean curConverted, Branch branch) {
		BigDecimal result = BigDecimal.ZERO;
		try {
			String field;
			if (curConverted || type.equals(CurrencyType.HOMECURRENCY)) {
				field = "homeAmount";
			} else {
				field = "localAmount";
			}

			StringBuffer queryString = new StringBuffer(" SELECT SUM(v." + field + ") FROM VwTLF v WHERE v.status='TCV' " + " AND CAST(v.settlementDate AS DATE)=:date ");

			if (branch != null) {
				queryString.append(" AND v.branch=:branch ");
			}

			if (type.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
				queryString.append(" AND v.currency=:currency ");
			}

			Query q = em.createQuery(queryString.toString());
			q.setParameter("date", reportDate);
			if (branch != null) {
				q.setParameter("branch", branch);
			}

			if (type.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
				q.setParameter("currency", currency);
			}

			result = (BigDecimal) q.getSingleResult() == null ? BigDecimal.ZERO : (BigDecimal) q.getSingleResult();
		} catch (NoResultException e) {
			return result;
		} catch (PersistenceException pe) {
			throw translate("Failed to find total transfer for bank cash.", pe);
		}

		return result;
	}

	/**
	 * [ Currently not used , for later use if needed. ]
	 */
	@Override
	public BigDecimal findTotalReceipt(CurrencyType type, Currency currency, Date date, boolean curConverted, Branch branch) {
		BigDecimal result = BigDecimal.ZERO;
		try {
			String field;
			if (curConverted || type.equals(CurrencyType.HOMECURRENCY)) {
				field = "homeAmount";
			} else {
				field = "localAmount";
			}

			// CC NOT CCV , CC in bank cash view
			StringBuffer queryString = new StringBuffer(" SELECT SUM(v." + field + ") FROM VwBankCash v WHERE v.status='CC' " + " AND CAST(v.settlementDate AS DATE)=:date ");
			if (branch != null) {
				queryString.append(" AND v.branch=:branch ");
			}

			if (type.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
				queryString.append(" AND v.cur=:currency ");
			}

			Query q = em.createQuery(queryString.toString());
			q.setParameter("date", date);
			if (branch != null) {
				q.setParameter("branch", branch);
			}

			if (type.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
				q.setParameter("currency", currency);
			}

			result = (BigDecimal) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find total receipt for bank cash.", pe);
		}

		return result;
	}

	/**
	 * [ Currently not used , for later use if needed. ]
	 */
	@Override
	public BigDecimal findTotalPayment(CurrencyType type, Currency currency, Date date, boolean curConverted, Branch branch) {
		BigDecimal result = BigDecimal.ZERO;
		try {
			String field;
			if (curConverted || type.equals(CurrencyType.HOMECURRENCY)) {
				field = "homeAmount";
			} else {
				field = "localAmount";
			}

			// CD NOT CDV , CD in bank cash view
			StringBuffer queryString = new StringBuffer(" SELECT SUM(v." + field + ") FROM VwBankCash v WHERE v.status='CD' " + " AND CAST(v.settlementDate AS DATE)=:date ");

			if (branch != null) {
				queryString.append(" AND v.branch=:branch ");
			}

			if (type.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
				queryString.append(" AND v.cur=:currency ");
			}

			Query q = em.createQuery(queryString.toString());
			q.setParameter("date", date);
			if (branch != null) {
				q.setParameter("branch", branch);
			}

			if (type.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
				q.setParameter("currency", currency);
			}

			result = (BigDecimal) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find total payment for bank cash.", pe);
		}

		return result;
	}

	/**
	 * [ To find the daily TLF by the report date. ]
	 * 
	 * @param CurrencyType
	 *            type [To decide home amount or source amount]
	 * @param Currency
	 *            currency [To filter the result by currency]
	 * @param Date
	 *            reportDate [Report Date]
	 * @param boolean
	 *            curConverted [To decide the request amount is converted amount
	 *            for local currency]
	 * @param Branch
	 *            branch [To filter the result by branch]
	 * 
	 * 
	 * @return [Return A List of BankCashDto]
	 * 
	 * @throws [Exception
	 *             DAOException]
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<BankCashDto> findDailyTransaction(CurrencyType type, Currency currency, Date reportDate, boolean curConverted, Branch branch) {
		List<BankCashDto> result = new ArrayList<>();
		try {
			String field;
			if (curConverted || type.equals(CurrencyType.HOMECURRENCY)) {
				field = "homeAmount";
			} else {
				field = "localAmount";
			}

			StringBuffer queryString = new StringBuffer("SELECT T.ACODE, T.ACNAME, SUM(T.DEBIT), SUM(T.CREDIT), T.ACCOUNTCODE FROM( ");
			queryString.append(" SELECT v.acode as ACODE,c.acName as ACNAME,v.ACCOUNTCODE AS ACCOUNTCODE, ");
			queryString.append(" CASE WHEN v.STATUS='CC' THEN SUM(v." + field + ") ELSE 0 END as DEBIT, ");
			queryString.append(" CASE WHEN v.STATUS='CD' THEN SUM(v." + field + ") ELSE 0 END as CREDIT ");
			queryString.append(" FROM VW_BANKCASH v INNER JOIN COA c ON v.ACCOUNTCODE=c.ACCODE ");
			queryString.append(" WHERE CONVERT(DATE,v.SETTLEMENTDATE)=?1 ");

			int index = 1;
			if (branch != null) {
				index++;
				queryString.append(" AND v.BRANCHID=?" + String.valueOf(index));
			}

			if (type.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
				index++;
				queryString.append(" AND v.CURID=?" + String.valueOf(index));
			}

			queryString.append(" GROUP BY v.ACODE,c.ACNAME,v.STATUS,v.ACCOUNTCODE ");
			queryString.append(" ) T GROUP BY T.ACODE,T.ACNAME,T.ACCOUNTCODE ");

			Query q = em.createNativeQuery(queryString.toString());
			index = 1;
			q.setParameter(String.valueOf(index), reportDate);
			if (branch != null) {
				index++;
				q.setParameter(String.valueOf(index), branch.getId());
			}

			if (type.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
				index++;
				q.setParameter(String.valueOf(index), currency.getId());
			}

			List<Object[]> resultList = q.getResultList();
			for (Object[] obj : resultList) {
				result.add(new BankCashDto(obj[0].toString(), obj[1].toString(), new BigDecimal(obj[2].toString()), new BigDecimal(obj[3].toString()), obj[4].toString()));
			}
		} catch (PersistenceException pe) {
			throw translate("Failed to find daily transactions for bank cash.", pe);
		}

		return result;
	}

	/**
	 * [Private method to find Cash COAs By Currency and Branch. ]
	 * 
	 * @param Currency
	 *            currency [To filter the result by currency]
	 * @param Branch
	 *            branch [To filter the result by branch]
	 * 
	 * @return [Return A List of COA]
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private List<ChartOfAccount> getCashCoaList(Currency currency, Branch branch) {
		List<COASetup> coaSetupList = coaSetupDAO.findCOAListByACNameAndCur("CASH", currency, branch);
		List<ChartOfAccount> coaList = new ArrayList<ChartOfAccount>();
		for (COASetup coaSetup : coaSetupList) {
			coaList.add(coaSetup.getCcoa().getCoa());
		}
		return coaList;
	}

	/**
	 * [ Private method to find the opening balance field , e.g. m12 or msrc12.
	 * ]
	 * 
	 * @param CurrencyType
	 *            type [To decide home amount or source amount]
	 * @param Date
	 *            reportDate [Report Date]
	 * @param boolean
	 *            curConverted [To decide the request amount is converted amount
	 *            for local currency]
	 * 
	 * @return [Return a string of opening balance field]
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private String getOpeningBalanceField(CurrencyType type, Date reportDate, boolean curConverted) {
		String openingBalanceField = null;
		int previousBudgetMonth = Integer.parseInt(BusinessUtil.getBudgetInfo(reportDate, 3)) - 1;
		if (previousBudgetMonth == 0) {
			if (type.equals(CurrencyType.HOMECURRENCY) || curConverted) {
				openingBalanceField = "hOBal";
			} else {
				openingBalanceField = "oBal";
			}
		} else {
			if (type.equals(CurrencyType.HOMECURRENCY) || curConverted) {
				openingBalanceField = BusinessUtil.getMonthSRCStatusJPQLField(previousBudgetMonth);
			} else {
				openingBalanceField = BusinessUtil.getMonthStatusJPQLField(previousBudgetMonth);
			}
		}
		return openingBalanceField;
	}
}
