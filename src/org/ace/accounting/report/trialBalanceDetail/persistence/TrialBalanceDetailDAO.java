package org.ace.accounting.report.trialBalanceDetail.persistence;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.dto.TrialBalanceCriteriaDto;
import org.ace.accounting.dto.TrialBalanceReportDto;
import org.ace.accounting.report.trialBalanceDetail.persistence.interfaces.ITrialBalanceDetailDAO;
import org.ace.accounting.system.chartaccount.AccountType;
import org.ace.accounting.system.setup.persistence.interfaces.ISetupDAO;
import org.ace.accounting.system.systempost.SystemPost;
import org.ace.accounting.system.systempost.persistence.interfaces.ISystemPostDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Aung
 *
 */

@Repository("TrialBalanceDetailDAO")
public class TrialBalanceDetailDAO extends BasicDAO implements ITrialBalanceDetailDAO {

	@Resource(name = "SetupDAO")
	private ISetupDAO setupDAO;

	@Resource(name = "SystemPostDAO")
	private ISystemPostDAO systemPostDAO;

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<TrialBalanceReportDto> findTrialBalanceDetailList(TrialBalanceCriteriaDto dto) {
		List<TrialBalanceReportDto> dtoList = null;
		boolean isInterval;
		try {
			String budgetYear = dto.getBudgetYear();
			SystemPost sysPost = systemPostDAO.findbyPostingName("TRIALPOST");
			int previousMonthvalue = 0;
			String reportBudgetMonth;
			String thisMonth;
			String previousBudgetMonth;
			String budget;

			Calendar cal = Calendar.getInstance();
			cal.set(dto.getRequiredYear(), dto.getRequiredMonth(), 1);
			Date reportDate = cal.getTime();

			Calendar cal1 = Calendar.getInstance();
			int month = (dto.getRequiredMonth()) - 1;
			cal1.set(dto.getRequiredYear(), month, 1);
			Date previousDate = cal1.getTime();

			cal.set(dto.getRequiredYear(), dto.getRequiredMonth(), 1);

			String currentYear = BusinessUtil.getCurrentBudget();
			isInterval = DateUtils.isIntervalBugetYear(reportDate);
			if (isInterval && budgetYear.equals(currentYear)) {

				reportBudgetMonth = BusinessUtil.getBudgetInfo(reportDate, 3);
				thisMonth = BusinessUtil.getBudgetInfo(reportDate, 3);
				previousBudgetMonth = BusinessUtil.getBudgetInfo(previousDate, 3);
				budget = BusinessUtil.getBudgetInfo(reportDate, 2);
				if (!previousDate.before(BusinessUtil.getBudgetStartDate())) {
					previousMonthvalue = Integer.parseInt(previousBudgetMonth);
				}
			} else {
				reportBudgetMonth = BusinessUtil.getPrevBudgetInfo(reportDate, budgetYear, 3);
				thisMonth = BusinessUtil.getPrevBudgetInfo(reportDate, budgetYear, 3);
				previousBudgetMonth = BusinessUtil.getPrevBudgetInfo(previousDate, budgetYear, 3);
				budget = BusinessUtil.getPrevBudgetInfo(reportDate, budgetYear, 2);
				if (!previousDate.before(BusinessUtil.getPrevBudgetStartDate(budgetYear))) {
					previousMonthvalue = Integer.parseInt(previousBudgetMonth);
				}
			}

			String amountColumn = "";
			String obalColumn = "";

			if (dto.getCurrencyType().equals(CurrencyType.HOMECURRENCY) || dto.isHomeCurrencyConverted()) {
				reportBudgetMonth = "MSRC" + reportBudgetMonth;

				if (Integer.parseInt(previousBudgetMonth) > 0) {
					previousBudgetMonth = "MSRC" + previousBudgetMonth;
					thisMonth = "MSRC" + thisMonth;
				}

				amountColumn = "homeAmount";
				obalColumn = "hOBal";
			} else {
				reportBudgetMonth = "M" + reportBudgetMonth;
				if (Integer.parseInt(previousBudgetMonth) > 0) {
					previousBudgetMonth = "M" + previousBudgetMonth;
					thisMonth = "M" + thisMonth;
				}

				amountColumn = "localAmount";
				obalColumn = "oBal";
			}

			StringBuffer queryString = new StringBuffer("SELECT ACODE,ACNAME,ACTYPE,SUM(mDEBIT),SUM(mCREDIT),SUM(DEBIT),SUM(CREDIT) FROM (");

			// for TLF
			// queryString.append("SELECT C.ACTYPE AS ACTYPE, CC.DEPARTMENTID");
			// if (dto.isGroup()) {
			// if
			// (dto.getAccountType().equals(TrialBalanceAccountType.Gl_ACODE)) {
			// queryString.append(" , CASE WHEN ACTYPE IN('A','L') THEN C.ACCODE
			// ELSE ");
			// queryString.append(" (SELECT ACCODE FROM COA C2 WHERE
			// C2.ID=C.HEADID) END AS ACODE ");
			// } else {
			// queryString.append(" , C.IBSBACODE AS ACODE ");
			// }
			// queryString.append(" , CASE WHEN ACTYPE IN('A','L') THEN C.ACNAME
			// ");
			// queryString.append(" ELSE (SELECT ACNAME FROM COA C2 WHERE C2.ID
			// = C.HEADID ) END AS ACNAME ");
			// } else {
			// if
			// (dto.getAccountType().equals(TrialBalanceAccountType.Gl_ACODE)) {
			// queryString.append(" , C.ACCODE AS ACODE ");
			// } else {
			// queryString.append(" , C.IBSBACODE AS ACODE ");
			// }
			// if (dto.getBranch() == null) {
			// queryString.append(" , C.ACNAME AS ACNAME ");
			// } else {
			// queryString.append(" , CC.ACNAME AS ACNAME ");
			// }
			// }
			//
			// queryString.append(" , CASE WHEN ( T.TRANTYPEID ='2' OR
			// T.TRANTYPEID ='3' ) THEN ABS(T." + amountColumn + ") ELSE 0 END
			// AS mDEBIT ");
			// queryString.append(" ,CASE WHEN ( T.TRANTYPEID ='1' OR
			// T.TRANTYPEID ='4' ) THEN ABS(T." + amountColumn + ") ELSE 0 END
			// AS mCREDIT,0 AS DEBIT,0 AS CREDIT");
			// queryString.append(" FROM COA AS C ");
			// queryString.append(" LEFT JOIN VW_CCOA AS CC ON C.ID=CC.COAID ");
			// queryString.append(" LEFT JOIN TLF T ON CC.ID = T.CCOAID");
			// queryString.append(" WHERE CC.BUDGET=?budget");
			// queryString.append(" AND DATEPART(MONTH,T.SETTLEMENTDATE)
			// =?month");
			// if (dto.isGroup()) {
			// queryString.append(" AND C.ACCODETYPE='GROUP' ");
			// } else {
			// queryString.append(" AND C.ACCODETYPE='DETAIL' ");
			// }
			// if (dto.getCurrencyType().equals(CurrencyType.SOURCECURRENCY) &&
			// dto.getCurrency() != null) {
			// queryString.append(" AND CC.CURRENCYID=?currencyid ");
			// }
			//
			// if (dto.getBranch() != null) {
			// queryString.append(" AND CC.BRANCHID=?branchid ");
			// }

			if (previousMonthvalue < 1) {
				// for first month
				queryString.append("SELECT C.ACTYPE AS ACTYPE, CC.DEPARTMENTID  , C.ACCODE AS ACODE  , C.ACNAME AS ACNAME ");
				queryString.append(" ,CASE WHEN  ");
				// queryString.append(" (ACTYPE IN ('I','L') AND CC." +
				// reportBudgetMonth + " < 0 ) OR ");
				// queryString.append(" (ACTYPE IN ('A','E') AND CC." +
				// reportBudgetMonth + " > 0 ) ");
				queryString.append(" ACTYPE IN ('A','E') ");
				// queryString.append(" ) THEN ABS(CC." +reportBudgetMonth+" )
				// ELSE 0 END AS mDEBIT ");
				queryString.append("  THEN (CC." + reportBudgetMonth + " - " + "CC." + obalColumn + ") ELSE 0 END AS mDEBIT ");

				queryString.append(" ,CASE WHEN  ");
				// queryString.append(" (ACTYPE IN ('I','L') AND CC." +
				// reportBudgetMonth + " > 0 ) OR ");
				// queryString.append(" (ACTYPE IN ('A','E') AND CC." +
				// reportBudgetMonth + " < 0 ) ");
				// queryString.append(" ) THEN ABS(CC." + reportBudgetMonth + ")
				// ELSE 0 END AS mCREDIT ,0 AS DEBIT,0 AS CREDIT ");
				queryString.append(" ACTYPE IN ('I','L')");
				queryString.append("  THEN (CC." + reportBudgetMonth + " - " + "CC." + obalColumn + ") ELSE 0 END AS mCREDIT ,0 AS DEBIT,0 AS CREDIT ");
				queryString.append(" FROM COA AS C INNER JOIN VW_CCOA AS CC ON C.ID=CC.COAID WHERE CC.BUDGET=?budget ");

				if (dto.isGroup()) {
					queryString.append(" AND C.ACCODETYPE='GROUP' ");
				} else {
					queryString.append(" AND C.ACCODETYPE='DETAIL' ");
				}

				if (dto.getCurrencyType().equals(CurrencyType.SOURCECURRENCY) && dto.getCurrency() != null) {
					queryString.append(" AND CC.CURRENCYID=?currencyid ");
				}

				if (dto.getBranch() != null) {
					queryString.append(" AND CC.BRANCHID=?branchid ");
				}
			} else {
				// for second month to end
				queryString.append("SELECT C.ACTYPE AS ACTYPE, CC.DEPARTMENTID  , C.ACCODE AS ACODE  , C.ACNAME AS ACNAME ");
				queryString.append(" ,CASE WHEN  ");
				// queryString.append(" (ACTYPE IN ('I','L') AND CC." +
				// thisMonth + " < 0 ) OR ");
				queryString.append(" ACTYPE IN ('A','E')  ");
				queryString.append("  THEN (CC." + thisMonth + " - " + "CC." + previousBudgetMonth + ") ELSE 0 END AS mDEBIT ");
				queryString.append(" ,CASE WHEN ");
				queryString.append(" ACTYPE IN ('I','L')  ");
				// queryString.append(" (ACTYPE IN ('A','E') AND CC." +
				// thisMonth + " < 0 ) ");
				queryString.append("  THEN (CC." + thisMonth + " - " + "CC." + previousBudgetMonth + ") ELSE 0 END AS mCREDIT ,0 AS DEBIT,0 AS CREDIT ");
				queryString.append(" FROM COA AS C INNER JOIN VW_CCOA AS CC ON C.ID=CC.COAID WHERE CC.BUDGET=?budget ");
				/*
				 * queryString.
				 * append("SELECT C.ACTYPE AS ACTYPE, CC.DEPARTMENTID  , C.ACCODE AS ACODE  , C.ACNAME AS ACNAME "
				 * ); queryString.append(" ,CASE WHEN ( ");
				 * queryString.append(" (ACTYPE IN ('I','L')  AND CC." +
				 * previousBudgetMonth + " < 0 ) OR ");
				 * queryString.append(" (ACTYPE IN ('A','E')  AND CC." +
				 * previousBudgetMonth + " > 0 ) ");
				 * queryString.append(" ) THEN ABS(CC." + previousBudgetMonth +
				 * ") ELSE 0 END AS mDEBIT ");
				 * queryString.append(" ,CASE WHEN ( ");
				 * queryString.append(" (ACTYPE IN ('I','L')  AND CC." +
				 * previousBudgetMonth + " > 0 ) OR ");
				 * queryString.append(" (ACTYPE IN ('A','E')  AND CC." +
				 * previousBudgetMonth + " < 0 ) ");
				 * queryString.append(" ) THEN ABS(CC." + previousBudgetMonth +
				 * ") ELSE 0 END AS mCREDIT ,0 AS DEBIT,0 AS CREDIT ");
				 * queryString.
				 * append(" FROM COA AS C INNER JOIN VW_CCOA AS CC ON C.ID=CC.COAID WHERE CC.BUDGET=?budget "
				 * );
				 */

				if (dto.isGroup()) {
					queryString.append(" AND C.ACCODETYPE='GROUP' ");
				} else {
					queryString.append(" AND C.ACCODETYPE='DETAIL' ");
					// queryString.append(" AND C.ACCODETYPE='DETAIL' AND
					// C.ACCODE='01-001-004' ");
				}

				if (dto.getCurrencyType().equals(CurrencyType.SOURCECURRENCY) && dto.getCurrency() != null) {
					queryString.append(" AND CC.CURRENCYID=?currencyid ");
				}

				if (dto.getBranch() != null) {
					queryString.append(" AND CC.BRANCHID=?branchid ");
				}
			}

			queryString.append("UNION ALL ");

			// FOR CCOA

			queryString.append("SELECT C.ACTYPE AS ACTYPE, CC.DEPARTMENTID  , C.ACCODE AS ACODE  , C.ACNAME AS ACNAME  ,0 AS mDEBIT,0 AS mCREDIT ");
			queryString.append(" ,CASE WHEN  ");
			// queryString.append(" (ACTYPE IN ('I','L') AND CC." +
			// reportBudgetMonth + " < 0 ) OR ");
			queryString.append(" ACTYPE IN ('A','E')   ");
			queryString.append("  THEN (CC." + reportBudgetMonth + ") ELSE 0 END AS DEBIT ");
			queryString.append(" ,CASE WHEN ");
			queryString.append(" ACTYPE IN ('I','L')   ");
			// queryString.append(" (ACTYPE IN ('A','E') AND CC." +
			// reportBudgetMonth + " < 0 ) ");
			queryString.append("  THEN (CC." + reportBudgetMonth + ") ELSE 0 END AS CREDIT ");
			queryString.append(" FROM COA AS C INNER JOIN VW_CCOA AS CC ON C.ID=CC.COAID WHERE CC.BUDGET=?budget ");
			// queryString.append(" AND " + reportBudgetMonth + "<> 0 ");
			if (dto.isGroup()) {
				queryString.append(" AND C.ACCODETYPE='GROUP' ");
			} else {
				queryString.append(" AND C.ACCODETYPE='DETAIL' ");
				// queryString.append(" AND C.ACCODETYPE='DETAIL' AND
				// C.ACCODE='01-001-004' ");
			}

			if (dto.getCurrencyType().equals(CurrencyType.SOURCECURRENCY) && dto.getCurrency() != null) {
				queryString.append(" AND CC.CURRENCYID=?currencyid ");
			}

			if (dto.getBranch() != null) {
				queryString.append(" AND CC.BRANCHID=?branchid ");
			}
			/*
			 * 
			 * queryString.
			 * append(" SELECT C.ACTYPE AS ACTYPE, CC.DEPARTMENTID "); if
			 * (dto.isGroup()) { if
			 * (dto.getAccountType().equals(TrialBalanceAccountType.Gl_ACODE ))
			 * { queryString.
			 * append(" , CASE WHEN ACTYPE IN('A','L') THEN C.ACCODE ELSE " );
			 * queryString.
			 * append(" (SELECT ACCODE FROM COA C2 WHERE C2.ID=C.HEADID) END AS ACODE "
			 * ); } else { queryString.append(" , C.IBSBACODE AS ACODE "); }
			 * queryString.
			 * append(" , CASE WHEN ACTYPE IN('A','L') THEN C.ACNAME ");
			 * queryString.
			 * append(" ELSE (SELECT ACNAME FROM COA C2 WHERE C2.ID = C.HEADID ) END AS ACNAME "
			 * ); } else { if
			 * (dto.getAccountType().equals(TrialBalanceAccountType.Gl_ACODE ))
			 * { queryString.append(" , C.ACCODE AS ACODE "); } else {
			 * queryString.append(" , C.IBSBACODE AS ACODE "); } if
			 * (dto.getBranch() == null) {
			 * queryString.append(" , C.ACNAME AS ACNAME "); } else {
			 * queryString.append(" , CC.ACNAME AS ACNAME "); } } // FROM TLF
			 * queryString.append(" ,CASE WHEN ( "); queryString.
			 * append(" T.TRANTYPEID ='2' OR T.TRANTYPEID ='3' ) ");
			 * queryString.append(" THEN ABS(T." + amountColumn +
			 * ") ELSE 0 END AS mDEBIT "); queryString.append(" ,CASE WHEN ( ");
			 * queryString.
			 * append(" T.TRANTYPEID ='1' OR T.TRANTYPEID ='4' ) ");
			 * queryString.append(" THEN ABS(T." + amountColumn +
			 * ") ELSE 0 END AS mCREDIT ");
			 * 
			 * // FROM COA queryString.append(" ,CASE WHEN ( ");
			 * queryString.append(" (ACTYPE IN ('I','L')  AND CC." +
			 * reportBudgetMonth + " < 0 ) OR ");
			 * queryString.append(" (ACTYPE IN ('A','E')  AND CC." +
			 * reportBudgetMonth + " > 0 ) ");
			 * queryString.append(" ) THEN ABS(CC." + reportBudgetMonth +
			 * ") ELSE 0 END AS DEBIT "); queryString.append(" ,CASE WHEN ( ");
			 * queryString.append(" (ACTYPE IN ('I','L')  AND CC." +
			 * reportBudgetMonth + " > 0 ) OR ");
			 * queryString.append(" (ACTYPE IN ('A','E')  AND CC." +
			 * reportBudgetMonth + " < 0 ) ");
			 * queryString.append(" ) THEN ABS(CC." + reportBudgetMonth +
			 * ") ELSE 0 END AS CREDIT "); queryString.
			 * append(" FROM COA AS C INNER JOIN VW_CCOA AS CC ON C.ID=CC.COAID RIGHT JOIN TLF T ON CC.ID = T.CCOAID WHERE CC.BUDGET=?budget "
			 * ); queryString.
			 * append(" AND DATEPART(MONTH,T.SETTLEMENTDATE) =?month");
			 * queryString.append(" AND " + reportBudgetMonth + "<> 0 "); if
			 * (dto.isGroup()) {
			 * queryString.append(" AND C.ACCODETYPE='GROUP' "); } else {
			 * queryString.append(" AND C.ACCODETYPE='DETAIL' "); }
			 * 
			 * if (dto.getCurrencyType().equals(CurrencyType.SOURCECURRENCY) &&
			 * dto.getCurrency() != null) {
			 * queryString.append(" AND CC.CURRENCYID=?currencyid "); }
			 * 
			 * if (dto.getBranch() != null) {
			 * queryString.append(" AND CC.BRANCHID=?branchid "); }
			 */
			queryString.append(" ) T");
			queryString.append(" GROUP BY ACODE,ACNAME,ACTYPE ORDER BY ACODE");

			Query q = em.createNativeQuery(queryString.toString());

			q.setParameter("budget", budget);

			if (dto.getCurrencyType().equals(CurrencyType.SOURCECURRENCY) && dto.getCurrency() != null) {
				q.setParameter("currencyid", dto.getCurrency().getId());
			}
			if (dto.getBranch() != null) {
				q.setParameter("branchid", dto.getBranch().getId());
			}

			q.setParameter("month", (cal.get(Calendar.MONTH)) + 1);

			List<Object[]> objList = q.getResultList();
			if (objList != null) {
				dtoList = new ArrayList<>();
				for (Object[] obj : objList) {
					TrialBalanceReportDto reportDto = new TrialBalanceReportDto();
					reportDto.setAcode(obj[0].toString());
					reportDto.setAcname(obj[1].toString());
					reportDto.setAcType(AccountType.valueOf(obj[2].toString()));
					reportDto.setmDebit(new BigDecimal(obj[3].toString()));
					reportDto.setmCredit(new BigDecimal(obj[4].toString()));
					reportDto.setDebit(new BigDecimal(obj[5].toString()));
					reportDto.setCredit(new BigDecimal(obj[6].toString()));
					dtoList.add(reportDto);
				}
				for (TrialBalanceReportDto reportDto : dtoList) {
					if (reportDto.getAcType().equals(AccountType.A) || reportDto.getAcType().equals(AccountType.E)) {

						// reportDto.setDebit(reportDto.getDebit().subtract(reportDto.getCredit()).setScale(2,RoundingMode.HALF_UP));
						reportDto.setDebit(reportDto.getDebit().subtract(reportDto.getCredit()));
						if (reportDto.getDebit().doubleValue() < 0) {
							reportDto.setCredit(reportDto.getDebit().abs());
							reportDto.setDebit(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
						} else {
							reportDto.setCredit(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
						}

						// reportDto.setmDebit(reportDto.getmDebit().subtract(reportDto.getmCredit()).setScale(2,RoundingMode.HALF_UP));
						reportDto.setmDebit(reportDto.getmDebit().subtract(reportDto.getmCredit()));
						if (reportDto.getmDebit().doubleValue() < 0) {
							reportDto.setmCredit(reportDto.getmDebit().abs());
							reportDto.setmDebit(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
						} else {
							reportDto.setmCredit(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
						}
					} else if (reportDto.getAcType().equals(AccountType.I) || reportDto.getAcType().equals(AccountType.L)) {

						// reportDto.setCredit(reportDto.getCredit().subtract(reportDto.getDebit()).setScale(2,RoundingMode.HALF_UP));
						reportDto.setCredit(reportDto.getCredit().subtract(reportDto.getDebit()));
						if (reportDto.getCredit().doubleValue() < 0) {
							reportDto.setDebit(reportDto.getCredit().abs());
							reportDto.setCredit(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
						} else {
							reportDto.setDebit(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
						}

						// reportDto.setmCredit(reportDto.getmCredit().subtract(reportDto.getmDebit()).setScale(2,RoundingMode.HALF_UP));
						reportDto.setmCredit(reportDto.getmCredit().subtract(reportDto.getmDebit()));
						if (reportDto.getmCredit().doubleValue() < 0) {
							reportDto.setmDebit(reportDto.getmCredit().abs());
							reportDto.setmCredit(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
						} else {
							reportDto.setmDebit(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
						}

					}
				}
			}

		} catch (PersistenceException pe) {
			throw translate("Failed to findTrialBalanceDetailList", pe);
		}
		return dtoList;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<TrialBalanceReportDto> findTrialBalanceDetailListByClone(TrialBalanceCriteriaDto dto) {
		List<TrialBalanceReportDto> dtoList = null;
		boolean isInterval;
		try {
			String budgetYear = dto.getBudgetYear();
			SystemPost sysPost = systemPostDAO.findbyPostingName("TRIALPOST");
			int previousMonthvalue = 0;
			String reportBudgetMonth;
			String thisMonth;
			String previousBudgetMonth;
			String budget;

			Calendar cal = Calendar.getInstance();
			cal.set(dto.getRequiredYear(), dto.getRequiredMonth(), 1);
			Date reportDate = cal.getTime();

			Calendar cal1 = Calendar.getInstance();
			int month = (dto.getRequiredMonth()) - 1;
			cal1.set(dto.getRequiredYear(), month, 1);
			Date previousDate = cal1.getTime();

			cal.set(dto.getRequiredYear(), dto.getRequiredMonth(), 1);

			String currentYear = BusinessUtil.getCurrentBudget();
			isInterval = DateUtils.isIntervalBugetYear(reportDate);
			if (isInterval && budgetYear.equals(currentYear)) {

				reportBudgetMonth = BusinessUtil.getBudgetInfo(reportDate, 3);
				thisMonth = BusinessUtil.getBudgetInfo(reportDate, 3);
				previousBudgetMonth = BusinessUtil.getBudgetInfo(previousDate, 3);
				budget = BusinessUtil.getBudgetInfo(reportDate, 2);
				if (!previousDate.before(BusinessUtil.getBudgetStartDate())) {
					previousMonthvalue = Integer.parseInt(previousBudgetMonth);
				}
			} else {
				reportBudgetMonth = BusinessUtil.getPrevBudgetInfo(reportDate, budgetYear, 3);
				thisMonth = BusinessUtil.getPrevBudgetInfo(reportDate, budgetYear, 3);
				previousBudgetMonth = BusinessUtil.getPrevBudgetInfo(previousDate, budgetYear, 3);
				budget = BusinessUtil.getPrevBudgetInfo(reportDate, budgetYear, 2);
				if (!previousDate.before(BusinessUtil.getPrevBudgetStartDate(budgetYear))) {
					previousMonthvalue = Integer.parseInt(previousBudgetMonth);
				}
			}

			String amountColumn = "";
			String obalColumn = "";

			if (dto.getCurrencyType().equals(CurrencyType.HOMECURRENCY) || dto.isHomeCurrencyConverted()) {
				reportBudgetMonth = "MSRC" + reportBudgetMonth;

				if (Integer.parseInt(previousBudgetMonth) > 0) {
					previousBudgetMonth = "MSRC" + previousBudgetMonth;
					thisMonth = "MSRC" + thisMonth;
				}

				amountColumn = "homeAmount";
				obalColumn = "hOBal";
			} else {
				reportBudgetMonth = "M" + reportBudgetMonth;
				if (Integer.parseInt(previousBudgetMonth) > 0) {
					previousBudgetMonth = "M" + previousBudgetMonth;
					thisMonth = "M" + thisMonth;
				}

				amountColumn = "localAmount";
				obalColumn = "oBal";
			}

			StringBuffer queryString = new StringBuffer("SELECT ACODE,ACNAME,ACTYPE,SUM(mDEBIT),SUM(mCREDIT),SUM(DEBIT),SUM(CREDIT) FROM (");

			// for TLF
			// queryString.append("SELECT C.ACTYPE AS ACTYPE, CC.DEPARTMENTID");
			// if (dto.isGroup()) {
			// if
			// (dto.getAccountType().equals(TrialBalanceAccountType.Gl_ACODE)) {
			// queryString.append(" , CASE WHEN ACTYPE IN('A','L') THEN C.ACCODE
			// ELSE ");
			// queryString.append(" (SELECT ACCODE FROM COA C2 WHERE
			// C2.ID=C.HEADID) END AS ACODE ");
			// } else {
			// queryString.append(" , C.IBSBACODE AS ACODE ");
			// }
			// queryString.append(" , CASE WHEN ACTYPE IN('A','L') THEN C.ACNAME
			// ");
			// queryString.append(" ELSE (SELECT ACNAME FROM COA C2 WHERE C2.ID
			// = C.HEADID ) END AS ACNAME ");
			// } else {
			// if
			// (dto.getAccountType().equals(TrialBalanceAccountType.Gl_ACODE)) {
			// queryString.append(" , C.ACCODE AS ACODE ");
			// } else {
			// queryString.append(" , C.IBSBACODE AS ACODE ");
			// }
			// if (dto.getBranch() == null) {
			// queryString.append(" , C.ACNAME AS ACNAME ");
			// } else {
			// queryString.append(" , CC.ACNAME AS ACNAME ");
			// }
			// }
			//
			// queryString.append(" , CASE WHEN ( T.TRANTYPEID ='2' OR
			// T.TRANTYPEID ='3' ) THEN ABS(T." + amountColumn + ") ELSE 0 END
			// AS mDEBIT ");
			// queryString.append(" ,CASE WHEN ( T.TRANTYPEID ='1' OR
			// T.TRANTYPEID ='4' ) THEN ABS(T." + amountColumn + ") ELSE 0 END
			// AS mCREDIT,0 AS DEBIT,0 AS CREDIT");
			// queryString.append(" FROM COA AS C ");
			// queryString.append(" LEFT JOIN VW_CCOA AS CC ON C.ID=CC.COAID ");
			// queryString.append(" LEFT JOIN TLF T ON CC.ID = T.CCOAID");
			// queryString.append(" WHERE CC.BUDGET=?budget");
			// queryString.append(" AND DATEPART(MONTH,T.SETTLEMENTDATE)
			// =?month");
			// if (dto.isGroup()) {
			// queryString.append(" AND C.ACCODETYPE='GROUP' ");
			// } else {
			// queryString.append(" AND C.ACCODETYPE='DETAIL' ");
			// }
			// if (dto.getCurrencyType().equals(CurrencyType.SOURCECURRENCY) &&
			// dto.getCurrency() != null) {
			// queryString.append(" AND CC.CURRENCYID=?currencyid ");
			// }
			//
			// if (dto.getBranch() != null) {
			// queryString.append(" AND CC.BRANCHID=?branchid ");
			// }

			if (previousMonthvalue < 1) {
				// for first month
				queryString.append("SELECT C.ACTYPE AS ACTYPE, CC.DEPARTMENTID  , C.ACCODE AS ACODE  , C.ACNAME AS ACNAME ");
				queryString.append(" ,CASE WHEN  ");
				// queryString.append(" (ACTYPE IN ('I','L') AND CC." +
				// reportBudgetMonth + " < 0 ) OR ");
				// queryString.append(" (ACTYPE IN ('A','E') AND CC." +
				// reportBudgetMonth + " > 0 ) ");
				queryString.append(" ACTYPE IN ('A','E') ");
				// queryString.append(" ) THEN ABS(CC." +reportBudgetMonth+" )
				// ELSE 0 END AS mDEBIT ");
				queryString.append("  THEN (CC." + reportBudgetMonth + " - " + "CC." + obalColumn + ") ELSE 0 END AS mDEBIT ");

				queryString.append(" ,CASE WHEN  ");
				// queryString.append(" (ACTYPE IN ('I','L') AND CC." +
				// reportBudgetMonth + " > 0 ) OR ");
				// queryString.append(" (ACTYPE IN ('A','E') AND CC." +
				// reportBudgetMonth + " < 0 ) ");
				// queryString.append(" ) THEN ABS(CC." + reportBudgetMonth + ")
				// ELSE 0 END AS mCREDIT ,0 AS DEBIT,0 AS CREDIT ");
				queryString.append(" ACTYPE IN ('I','L')");
				queryString.append("  THEN (CC." + reportBudgetMonth + " - " + "CC." + obalColumn + ") ELSE 0 END AS mCREDIT ,0 AS DEBIT,0 AS CREDIT ");
				queryString.append(" FROM COA AS C INNER JOIN CLONE_CCOA AS CC ON C.ID=CC.COAID WHERE CC.BUDGET=?budget ");

				if (dto.isGroup()) {
					queryString.append(" AND C.ACCODETYPE='GROUP' ");
				} else {
					queryString.append(" AND C.ACCODETYPE='DETAIL' ");
				}

				if (dto.getCurrencyType().equals(CurrencyType.SOURCECURRENCY) && dto.getCurrency() != null) {
					queryString.append(" AND CC.CURRENCYID=?currencyid ");
				}

				if (dto.getBranch() != null) {
					queryString.append(" AND CC.BRANCHID=?branchid ");
				}
			} else {
				// for second month to end
				queryString.append("SELECT C.ACTYPE AS ACTYPE, CC.DEPARTMENTID  , C.ACCODE AS ACODE  , C.ACNAME AS ACNAME ");
				queryString.append(" ,CASE WHEN  ");
				// queryString.append(" (ACTYPE IN ('I','L') AND CC." +
				// thisMonth + " < 0 ) OR ");
				queryString.append(" ACTYPE IN ('A','E')  ");
				queryString.append("  THEN (CC." + thisMonth + " - " + "CC." + previousBudgetMonth + ") ELSE 0 END AS mDEBIT ");
				queryString.append(" ,CASE WHEN ");
				queryString.append(" ACTYPE IN ('I','L')  ");
				// queryString.append(" (ACTYPE IN ('A','E') AND CC." +
				// thisMonth + " < 0 ) ");
				queryString.append("  THEN (CC." + thisMonth + " - " + "CC." + previousBudgetMonth + ") ELSE 0 END AS mCREDIT ,0 AS DEBIT,0 AS CREDIT ");
				queryString.append(" FROM COA AS C INNER JOIN CLONE_CCOA AS CC ON C.ID=CC.COAID WHERE CC.BUDGET=?budget ");
				/*
				 * queryString.
				 * append("SELECT C.ACTYPE AS ACTYPE, CC.DEPARTMENTID  , C.ACCODE AS ACODE  , C.ACNAME AS ACNAME "
				 * ); queryString.append(" ,CASE WHEN ( ");
				 * queryString.append(" (ACTYPE IN ('I','L')  AND CC." +
				 * previousBudgetMonth + " < 0 ) OR ");
				 * queryString.append(" (ACTYPE IN ('A','E')  AND CC." +
				 * previousBudgetMonth + " > 0 ) ");
				 * queryString.append(" ) THEN ABS(CC." + previousBudgetMonth +
				 * ") ELSE 0 END AS mDEBIT ");
				 * queryString.append(" ,CASE WHEN ( ");
				 * queryString.append(" (ACTYPE IN ('I','L')  AND CC." +
				 * previousBudgetMonth + " > 0 ) OR ");
				 * queryString.append(" (ACTYPE IN ('A','E')  AND CC." +
				 * previousBudgetMonth + " < 0 ) ");
				 * queryString.append(" ) THEN ABS(CC." + previousBudgetMonth +
				 * ") ELSE 0 END AS mCREDIT ,0 AS DEBIT,0 AS CREDIT ");
				 * queryString.
				 * append(" FROM COA AS C INNER JOIN VW_CCOA AS CC ON C.ID=CC.COAID WHERE CC.BUDGET=?budget "
				 * );
				 */

				if (dto.isGroup()) {
					queryString.append(" AND C.ACCODETYPE='GROUP' ");
				} else {
					queryString.append(" AND C.ACCODETYPE='DETAIL' ");
					// queryString.append(" AND C.ACCODETYPE='DETAIL' AND
					// C.ACCODE='01-001-004' ");
				}

				if (dto.getCurrencyType().equals(CurrencyType.SOURCECURRENCY) && dto.getCurrency() != null) {
					queryString.append(" AND CC.CURRENCYID=?currencyid ");
				}

				if (dto.getBranch() != null) {
					queryString.append(" AND CC.BRANCHID=?branchid ");
				}
			}

			queryString.append("UNION ALL ");

			// FOR CCOA

			queryString.append("SELECT C.ACTYPE AS ACTYPE, CC.DEPARTMENTID  , C.ACCODE AS ACODE  , C.ACNAME AS ACNAME  ,0 AS mDEBIT,0 AS mCREDIT ");
			queryString.append(" ,CASE WHEN  ");
			// queryString.append(" (ACTYPE IN ('I','L') AND CC." +
			// reportBudgetMonth + " < 0 ) OR ");
			queryString.append(" ACTYPE IN ('A','E')   ");
			queryString.append("  THEN (CC." + reportBudgetMonth + ") ELSE 0 END AS DEBIT ");
			queryString.append(" ,CASE WHEN ");
			queryString.append(" ACTYPE IN ('I','L')   ");
			// queryString.append(" (ACTYPE IN ('A','E') AND CC." +
			// reportBudgetMonth + " < 0 ) ");
			queryString.append("  THEN (CC." + reportBudgetMonth + ") ELSE 0 END AS CREDIT ");
			queryString.append(" FROM COA AS C INNER JOIN CLONE_CCOA AS CC ON C.ID=CC.COAID WHERE CC.BUDGET=?budget ");
			// queryString.append(" AND " + reportBudgetMonth + "<> 0 ");
			if (dto.isGroup()) {
				queryString.append(" AND C.ACCODETYPE='GROUP' ");
			} else {
				queryString.append(" AND C.ACCODETYPE='DETAIL' ");
				// queryString.append(" AND C.ACCODETYPE='DETAIL' AND
				// C.ACCODE='01-001-004' ");
			}

			if (dto.getCurrencyType().equals(CurrencyType.SOURCECURRENCY) && dto.getCurrency() != null) {
				queryString.append(" AND CC.CURRENCYID=?currencyid ");
			}

			if (dto.getBranch() != null) {
				queryString.append(" AND CC.BRANCHID=?branchid ");
			}
			/*
			 * 
			 * queryString.
			 * append(" SELECT C.ACTYPE AS ACTYPE, CC.DEPARTMENTID "); if
			 * (dto.isGroup()) { if
			 * (dto.getAccountType().equals(TrialBalanceAccountType.Gl_ACODE ))
			 * { queryString.
			 * append(" , CASE WHEN ACTYPE IN('A','L') THEN C.ACCODE ELSE " );
			 * queryString.
			 * append(" (SELECT ACCODE FROM COA C2 WHERE C2.ID=C.HEADID) END AS ACODE "
			 * ); } else { queryString.append(" , C.IBSBACODE AS ACODE "); }
			 * queryString.
			 * append(" , CASE WHEN ACTYPE IN('A','L') THEN C.ACNAME ");
			 * queryString.
			 * append(" ELSE (SELECT ACNAME FROM COA C2 WHERE C2.ID = C.HEADID ) END AS ACNAME "
			 * ); } else { if
			 * (dto.getAccountType().equals(TrialBalanceAccountType.Gl_ACODE ))
			 * { queryString.append(" , C.ACCODE AS ACODE "); } else {
			 * queryString.append(" , C.IBSBACODE AS ACODE "); } if
			 * (dto.getBranch() == null) {
			 * queryString.append(" , C.ACNAME AS ACNAME "); } else {
			 * queryString.append(" , CC.ACNAME AS ACNAME "); } } // FROM TLF
			 * queryString.append(" ,CASE WHEN ( "); queryString.
			 * append(" T.TRANTYPEID ='2' OR T.TRANTYPEID ='3' ) ");
			 * queryString.append(" THEN ABS(T." + amountColumn +
			 * ") ELSE 0 END AS mDEBIT "); queryString.append(" ,CASE WHEN ( ");
			 * queryString.
			 * append(" T.TRANTYPEID ='1' OR T.TRANTYPEID ='4' ) ");
			 * queryString.append(" THEN ABS(T." + amountColumn +
			 * ") ELSE 0 END AS mCREDIT ");
			 * 
			 * // FROM COA queryString.append(" ,CASE WHEN ( ");
			 * queryString.append(" (ACTYPE IN ('I','L')  AND CC." +
			 * reportBudgetMonth + " < 0 ) OR ");
			 * queryString.append(" (ACTYPE IN ('A','E')  AND CC." +
			 * reportBudgetMonth + " > 0 ) ");
			 * queryString.append(" ) THEN ABS(CC." + reportBudgetMonth +
			 * ") ELSE 0 END AS DEBIT "); queryString.append(" ,CASE WHEN ( ");
			 * queryString.append(" (ACTYPE IN ('I','L')  AND CC." +
			 * reportBudgetMonth + " > 0 ) OR ");
			 * queryString.append(" (ACTYPE IN ('A','E')  AND CC." +
			 * reportBudgetMonth + " < 0 ) ");
			 * queryString.append(" ) THEN ABS(CC." + reportBudgetMonth +
			 * ") ELSE 0 END AS CREDIT "); queryString.
			 * append(" FROM COA AS C INNER JOIN VW_CCOA AS CC ON C.ID=CC.COAID RIGHT JOIN TLF T ON CC.ID = T.CCOAID WHERE CC.BUDGET=?budget "
			 * ); queryString.
			 * append(" AND DATEPART(MONTH,T.SETTLEMENTDATE) =?month");
			 * queryString.append(" AND " + reportBudgetMonth + "<> 0 "); if
			 * (dto.isGroup()) {
			 * queryString.append(" AND C.ACCODETYPE='GROUP' "); } else {
			 * queryString.append(" AND C.ACCODETYPE='DETAIL' "); }
			 * 
			 * if (dto.getCurrencyType().equals(CurrencyType.SOURCECURRENCY) &&
			 * dto.getCurrency() != null) {
			 * queryString.append(" AND CC.CURRENCYID=?currencyid "); }
			 * 
			 * if (dto.getBranch() != null) {
			 * queryString.append(" AND CC.BRANCHID=?branchid "); }
			 */
			queryString.append(" ) T");
			queryString.append(" GROUP BY ACODE,ACNAME,ACTYPE ORDER BY ACODE");

			Query q = em.createNativeQuery(queryString.toString());

			q.setParameter("budget", budget);

			if (dto.getCurrencyType().equals(CurrencyType.SOURCECURRENCY) && dto.getCurrency() != null) {
				q.setParameter("currencyid", dto.getCurrency().getId());
			}
			if (dto.getBranch() != null) {
				q.setParameter("branchid", dto.getBranch().getId());
			}

			q.setParameter("month", (cal.get(Calendar.MONTH)) + 1);

			List<Object[]> objList = q.getResultList();
			if (objList != null) {
				dtoList = new ArrayList<>();
				for (Object[] obj : objList) {
					TrialBalanceReportDto reportDto = new TrialBalanceReportDto();
					reportDto.setAcode(obj[0].toString());
					reportDto.setAcname(obj[1].toString());
					reportDto.setAcType(AccountType.valueOf(obj[2].toString()));
					reportDto.setmDebit(new BigDecimal(obj[3].toString()));
					reportDto.setmCredit(new BigDecimal(obj[4].toString()));
					reportDto.setDebit(new BigDecimal(obj[5].toString()));
					reportDto.setCredit(new BigDecimal(obj[6].toString()));
					dtoList.add(reportDto);
				}
				for (TrialBalanceReportDto reportDto : dtoList) {
					if (reportDto.getAcType().equals(AccountType.A) || reportDto.getAcType().equals(AccountType.E)) {

						// reportDto.setDebit(reportDto.getDebit().subtract(reportDto.getCredit()).setScale(2,RoundingMode.HALF_UP));
						reportDto.setDebit(reportDto.getDebit().subtract(reportDto.getCredit()));
						if (reportDto.getDebit().doubleValue() < 0) {
							reportDto.setCredit(reportDto.getDebit().abs());
							reportDto.setDebit(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
						} else {
							reportDto.setCredit(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
						}

						// reportDto.setmDebit(reportDto.getmDebit().subtract(reportDto.getmCredit()).setScale(2,RoundingMode.HALF_UP));
						reportDto.setmDebit(reportDto.getmDebit().subtract(reportDto.getmCredit()));
						if (reportDto.getmDebit().doubleValue() < 0) {
							reportDto.setmCredit(reportDto.getmDebit().abs());
							reportDto.setmDebit(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
						} else {
							reportDto.setmCredit(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
						}
					} else if (reportDto.getAcType().equals(AccountType.I) || reportDto.getAcType().equals(AccountType.L)) {

						// reportDto.setCredit(reportDto.getCredit().subtract(reportDto.getDebit()).setScale(2,RoundingMode.HALF_UP));
						reportDto.setCredit(reportDto.getCredit().subtract(reportDto.getDebit()));
						if (reportDto.getCredit().doubleValue() < 0) {
							reportDto.setDebit(reportDto.getCredit().abs());
							reportDto.setCredit(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
						} else {
							reportDto.setDebit(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
						}

						// reportDto.setmCredit(reportDto.getmCredit().subtract(reportDto.getmDebit()).setScale(2,RoundingMode.HALF_UP));
						reportDto.setmCredit(reportDto.getmCredit().subtract(reportDto.getmDebit()));
						if (reportDto.getmCredit().doubleValue() < 0) {
							reportDto.setmDebit(reportDto.getmCredit().abs());
							reportDto.setmCredit(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
						} else {
							reportDto.setmDebit(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
						}

					}
				}
			}

		} catch (PersistenceException pe) {
			throw translate("Failed to findTrialBalanceDetailList", pe);
		}
		return dtoList;
	}
}
