package org.ace.accounting.posting.persistence;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.dto.dailyPostingDto;
import org.ace.accounting.posting.persistence.interfaces.IDailyPostingDAO;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.currency.Currency;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("DailyPostingDAO")
public class DailyPostingDAO extends BasicDAO implements IDailyPostingDAO {

	@Resource(name = "DataRepService")
	private IDataRepService<CurrencyChartOfAccount> ccoadataRepService;

	@Resource(name = "DataRepService")
	private IDataRepService<ChartOfAccount> coaDataRepService;

	@Resource(name = "DataRepService")
	private IDataRepService<Currency> currencydataRepService;

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<dailyPostingDto> findTlfDetailAccountList(Date curStartDate, Date curEndDate, Branch branch) throws DAOException {
		List<dailyPostingDto> result = new ArrayList<dailyPostingDto>();
		List<Object[]> tempResults;
		CurrencyChartOfAccount ccoa;
		ChartOfAccount headCOA;
		ChartOfAccount groupCOA;
		Currency currency;
		BigDecimal localAmount;
		BigDecimal homeAmount;

		try {
			StringBuffer sf = new StringBuffer("SELECT TEMP.CCOAID, TEMP.CURRENCYID, TEMP.LOCAL , TEMP.HOME ");
			sf.append("FROM (SELECT T.CCOAID, T.CURRENCYID, SUM(T.LOCALAMOUNT) * -1  AS LOCAL , SUM(T.HOMEAMOUNT) * -1  AS HOME ");
			sf.append("FROM TLF T INNER JOIN CCOA CCOA ON T.CCOAID = CCOA.ID INNER JOIN COA COA ON COA.ID=CCOA.COAID ");
			sf.append("INNER JOIN TRANTYPE TP ON T.TRANTYPEID = TP.ID 	WHERE COA.ACTYPE IN('A','E') 	AND TP.TRANCODE IN ('CSCREDIT','TRCREDIT') ");
			sf.append("	AND CONVERT(DATE,T.SETTLEMENTDATE) BETWEEN ?1 AND ?2 AND T.BRANCHID=?3 AND T.REVERSE = 0 AND T.PAID = 1 GROUP BY T.CCOAID, T.CURRENCYID ");
			sf.append("UNION ALL SELECT T.CCOAID, T.CURRENCYID, SUM(T.LOCALAMOUNT) AS LOCAL , SUM(T.HOMEAMOUNT) AS HOME ");
			sf.append("FROM TLF T 	INNER JOIN  CCOA CCOA ON T.CCOAID = CCOA.ID " + "INNER JOIN COA COA ON COA.ID=CCOA.COAID 	INNER JOIN TRANTYPE TP ON T.TRANTYPEID = TP.ID 	");
			sf.append("WHERE COA.ACTYPE IN('A','E') AND TP.TRANCODE NOT IN ('CSCREDIT','TRCREDIT') 	AND CONVERT(DATE,T.SETTLEMENTDATE) BETWEEN ?1 AND ?2 AND T.BRANCHID=?3 AND T.REVERSE = 0 AND T.PAID = 1 ");
			sf.append("GROUP BY T.CCOAID, T.CURRENCYID 	UNION ALL SELECT T.CCOAID, T.CURRENCYID, SUM(T.LOCALAMOUNT) * -1  AS LOCAL , SUM(T.HOMEAMOUNT) * -1  AS HOME ");
			sf.append("FROM TLF T INNER JOIN CCOA CCOA ON T.CCOAID = CCOA.ID INNER JOIN COA COA ON COA.ID=CCOA.COAID INNER JOIN TRANTYPE TP ON T.TRANTYPEID = TP.ID ");
			sf.append("WHERE COA.ACTYPE IN('I','L') AND TP.TRANCODE IN ('CSDEBIT','TRDEBIT') AND CONVERT(DATE,T.SETTLEMENTDATE) BETWEEN ?1 AND ?2 AND T.BRANCHID=?3 AND T.REVERSE = 0 AND T.PAID = 1 ");
			sf.append("GROUP BY T.CCOAID, T.CURRENCYID 	" + "UNION ALL SELECT T.CCOAID, T.CURRENCYID, SUM(T.LOCALAMOUNT) AS LOCAL , SUM(T.HOMEAMOUNT) AS HOME ");
			sf.append("FROM TLF T INNER JOIN  CCOA CCOA ON T.CCOAID = CCOA.ID INNER JOIN COA COA ON COA.ID=CCOA.COAID ");
			sf.append("INNER JOIN TRANTYPE TP ON T.TRANTYPEID = TP.ID 	WHERE COA.ACTYPE IN('I','L') 	AND TP.TRANCODE NOT IN ('CSDEBIT','TRDEBIT') ");
			sf.append(
					"AND CONVERT(DATE,T.SETTLEMENTDATE) BETWEEN ?1 AND ?2 AND T.BRANCHID= ?3 AND T.REVERSE = 0 AND T.PAID = 1 GROUP BY T.CCOAID, T.CURRENCYID 	)AS TEMP GROUP BY TEMP.CCOAID, TEMP.CURRENCYID,TEMP.LOCAL , TEMP.HOME");
//			
//			StringBuffer sf = new StringBuffer("SELECT TEMP.CCOAID, TEMP.CURRENCYID, TEMP.LOCAL , TEMP.HOME ");
//			sf.append("FROM (SELECT T.CCOAID, T.CURRENCYID, SUM(CAST(T.LOCALAMOUNT AS DECIMAL(18,2))) * -1  AS LOCAL , SUM(CAST(T.HOMEAMOUNT AS DECIMAL(18,2))) * -1  AS HOME ");
//			sf.append("FROM TLF T INNER JOIN CCOA CCOA ON T.CCOAID = CCOA.ID INNER JOIN COA COA ON COA.ID=CCOA.COAID ");
//			sf.append("INNER JOIN TRANTYPE TP ON T.TRANTYPEID = TP.ID 	WHERE COA.ACTYPE IN('A','E') 	AND TP.TRANCODE IN ('CSCREDIT','TRCREDIT') ");
//			sf.append("	AND CONVERT(DATE,T.SETTLEMENTDATE) BETWEEN ?1 AND ?2 AND T.BRANCHID=?3 AND T.REVERSE = 0 AND T.PAID = 1 GROUP BY T.CCOAID, T.CURRENCYID ");
//			sf.append("UNION ALL SELECT T.CCOAID, T.CURRENCYID, SUM(CAST(T.LOCALAMOUNT AS DECIMAL(18,2))) AS LOCAL , SUM(CAST(T.HOMEAMOUNT AS DECIMAL(18,2))) AS HOME ");
//			sf.append("FROM TLF T 	INNER JOIN  CCOA CCOA ON T.CCOAID = CCOA.ID " + "INNER JOIN COA COA ON COA.ID=CCOA.COAID 	INNER JOIN TRANTYPE TP ON T.TRANTYPEID = TP.ID 	");
//			sf.append("WHERE COA.ACTYPE IN('A','E') AND TP.TRANCODE NOT IN ('CSCREDIT','TRCREDIT') 	AND CONVERT(DATE,T.SETTLEMENTDATE) BETWEEN ?1 AND ?2 AND T.BRANCHID=?3 AND T.REVERSE = 0 AND T.PAID = 1 ");
//			sf.append("GROUP BY T.CCOAID, T.CURRENCYID 	UNION ALL SELECT T.CCOAID, T.CURRENCYID, SUM(CAST(T.LOCALAMOUNT AS DECIMAL(18,2))) * -1  AS LOCAL , SUM(CAST(T.HOMEAMOUNT AS DECIMAL(18,2))) * -1  AS HOME ");
//			sf.append("FROM TLF T INNER JOIN CCOA CCOA ON T.CCOAID = CCOA.ID INNER JOIN COA COA ON COA.ID=CCOA.COAID INNER JOIN TRANTYPE TP ON T.TRANTYPEID = TP.ID ");
//			sf.append("WHERE COA.ACTYPE IN('I','L') AND TP.TRANCODE IN ('CSDEBIT','TRDEBIT') AND CONVERT(DATE,T.SETTLEMENTDATE) BETWEEN ?1 AND ?2 AND T.BRANCHID=?3 AND T.REVERSE = 0 AND T.PAID = 1 ");
//			sf.append("GROUP BY T.CCOAID, T.CURRENCYID 	" + "UNION ALL SELECT T.CCOAID, T.CURRENCYID, SUM(CAST(T.LOCALAMOUNT AS DECIMAL(18,2))) AS LOCAL , SUM(CAST(T.HOMEAMOUNT AS DECIMAL(18,2))) AS HOME ");
//			sf.append("FROM TLF T INNER JOIN  CCOA CCOA ON T.CCOAID = CCOA.ID INNER JOIN COA COA ON COA.ID=CCOA.COAID ");
//			sf.append("INNER JOIN TRANTYPE TP ON T.TRANTYPEID = TP.ID 	WHERE COA.ACTYPE IN('I','L') 	AND TP.TRANCODE NOT IN ('CSDEBIT','TRDEBIT') ");
//			sf.append(
//					"AND CONVERT(DATE,T.SETTLEMENTDATE) BETWEEN ?1 AND ?2 AND T.BRANCHID= ?3 AND T.REVERSE = 0 AND T.PAID = 1 GROUP BY T.CCOAID, T.CURRENCYID 	)AS TEMP GROUP BY TEMP.CCOAID, TEMP.CURRENCYID,TEMP.LOCAL , TEMP.HOME");
//			
//			
			Query query = em.createNativeQuery((sf.toString()));

			query.setParameter("1", DateUtils.resetStartDate(curStartDate));
			query.setParameter("2", DateUtils.resetEndDate(curEndDate));
			query.setParameter("3", branch.getId());
			tempResults = query.getResultList();
			em.flush();
			for (Object o[] : tempResults) {
				ccoa = ccoadataRepService.findById(CurrencyChartOfAccount.class, o[0].toString());
				currency = currencydataRepService.findById(Currency.class, o[1].toString());

				headCOA = (ccoa.getCoa().getHeadId() == null || ccoa.getCoa().getHeadId().isEmpty()) ? null
						: coaDataRepService.findById(ChartOfAccount.class, ccoa.getCoa().getHeadId());

				// headCOA = coaDataRepService.findById(ChartOfAccount.class,
				// ccoa.getCoa().getHeadId());

				groupCOA = (ccoa.getCoa().getGroupId() == null || ccoa.getCoa().getGroupId().isEmpty()) ? null
						: coaDataRepService.findById(ChartOfAccount.class, ccoa.getCoa().getGroupId());
				localAmount = new BigDecimal(o[2].toString());
				homeAmount = new BigDecimal(o[3].toString());
				result.add(new dailyPostingDto(ccoa, currency, headCOA, groupCOA, localAmount, homeAmount));
			}

		} catch (PersistenceException pe) {
			throw translate("Failed to find TLF daily Post", pe);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateMFieldLocalAmount(StringBuffer sf, BigDecimal localAmount, ChartOfAccount coa, Currency currency, Branch branch) throws DAOException {
		try {
			Query q = em.createQuery(sf.toString());
			q.setParameter("localAmount", localAmount);
			q.setParameter("coa", coa);
			q.setParameter("branch", branch);
			q.setParameter("currency", currency);
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to update MField LocalAmount", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateMSRCFieldHomeAmount(StringBuffer sf, BigDecimal homeAmount, ChartOfAccount coa, Currency currency, Branch branch) throws DAOException {
		try {
			Query q = em.createQuery(sf.toString());
			q.setParameter("homeAmount", homeAmount);
			q.setParameter("coa", coa);
			q.setParameter("branch", branch);
			q.setParameter("currency", currency);
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to update MField LocalAmount", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateMFiledOpeningAmount(StringBuffer sf, Branch branch) throws DAOException {
		try {
			Query q = em.createQuery(sf.toString());
			q.setParameter("branch", branch);
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to update MField LocalAmount", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateMSRCFiledOpeningAmount(StringBuffer sf, Branch branch) throws DAOException {
		try {
			Query q = em.createQuery(sf.toString());
			q.setParameter("branch", branch);
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to update MField LocalAmount", pe);
		}
	}

}
