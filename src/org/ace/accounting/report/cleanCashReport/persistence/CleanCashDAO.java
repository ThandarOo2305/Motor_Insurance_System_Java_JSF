package org.ace.accounting.report.cleanCashReport.persistence;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.report.ReportCriteria;
import org.ace.accounting.report.cleanCashReport.CleanCashReport;
import org.ace.accounting.report.cleanCashReport.persistence.interfaces.ICleanCashDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/***************************************************************************************
 * @Date 2016-04-05
 * @author PPA
 * @Version 1.0
 * @Purpose This class serves as Data Manipulation to retrieve
 *          <code>CleanCashReport</code> Clean Cash Report process
 * 
 ***************************************************************************************/

@Repository("CleanCashDAO")
public class CleanCashDAO extends BasicDAO implements ICleanCashDAO {

	// To generate log
	private Logger logger = Logger.getLogger(getClass());

	/**
	 * Retrieve CleanCashReport from database for the given criteria parameter.
	 * If user choose by home amount, retrieve data from home amount column and
	 * not choose it, retrieve from local amount column. If status is ("CC" or
	 * "TC"), sum to debit amount of same account code. If status is ("CD" or
	 * "TD"), sum to credit amount of same account code.
	 * 
	 * @param ReportCriteria[Criteria
	 *            of user selection].
	 * 
	 * @return Map<String, CleanCashReport>[Map of cleanCashReport with acode
	 *         key and CleanCashReport value].
	 * 
	 * @throws DAOException.
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public Map<String, CleanCashReport> find(ReportCriteria criteria) throws DAOException {
		Map<String, CleanCashReport> resultMap = new TreeMap<>();
		BigDecimal debit = BigDecimal.ZERO;
		BigDecimal credit = BigDecimal.ZERO;
		Date startDate = criteria.getDate();

		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.add(Calendar.DATE, 1);
		Date endDate = c.getTime();
		try {
			logger.debug("find() method for CleanCash Report has been started.");
			StringBuffer query = new StringBuffer();
			query.append("SELECT c.ACODE,c.ACNAME,c.STATUS");
			if (criteria.isByHome()) {
				query.append(",SUM(c.HOMEAMOUNT) ");
			} else {
				query.append(",SUM(c.LOCALAMOUNT) ");
			}
			query.append(" FROM VW_CLEANCASH c WHERE c.ACODE IS NOT NULL ");
			query.append(" AND  c.SETTLEMENTDATE >= ?startDate ");
			query.append(" AND  c.SETTLEMENTDATE < ?endDate ");

			if (criteria.getBranch() != null) {
				query.append(" AND c.BRANCHID = ?branchId ");
			}
			if (criteria.getCurrency() != null) {
				query.append(" AND  c.CURID = ?cur ");
			}
			query.append(" GROUP BY c.ACODE,c.ACNAME,c.STATUS");
			Query q = em.createNativeQuery(query.toString());

			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);

			if (criteria.getBranch() != null) {
				q.setParameter("branchId", criteria.getBranch().getId());
			}
			if (criteria.getCurrency() != null) {
				q.setParameter("cur", criteria.getCurrency().getId());
			}

			List<Object[]> objList = q.getResultList();

			for (Object[] obj : objList) {
				String aCode = (String) obj[0];
				String status = (String) obj[2];
				if (status.equalsIgnoreCase("CC") || status.equalsIgnoreCase("TC")) {
					debit = (BigDecimal) obj[3];
					credit = BigDecimal.ZERO;
				} else if (status.equalsIgnoreCase("TD") || status.equalsIgnoreCase("CD")) {
					credit = (BigDecimal) obj[3];
					debit = BigDecimal.ZERO;
				}
				CleanCashReport cleanCash;
				if (resultMap.containsKey(aCode)) {
					cleanCash = resultMap.get(aCode);
					debit = debit.add(cleanCash.getDebit());
					credit = credit.add(cleanCash.getCredit());
					cleanCash.setDebit(debit);
					cleanCash.setCredit(credit);
				} else {
					cleanCash = new CleanCashReport(aCode, (String) obj[1], debit, credit);
				}
				resultMap.put(aCode, cleanCash);
			}
			em.flush();
			logger.debug("find() method for CleanCash Report has been successfully finished.");
		} catch (PersistenceException pe) {
			throw translate("Failed to find CleanCash Report Data by criteria.", pe);
		}
		return resultMap;
	}
}
