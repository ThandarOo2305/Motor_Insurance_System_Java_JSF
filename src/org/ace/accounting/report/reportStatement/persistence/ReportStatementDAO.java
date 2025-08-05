package org.ace.accounting.report.reportStatement.persistence;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.dto.ReportStatementDto;
import org.ace.accounting.report.ReportType;
import org.ace.accounting.report.reportStatement.persistence.interfaces.IReportStatementDAO;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.AccountType;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.formatfile.ColType;
import org.ace.accounting.system.formatfile.persistence.interfaces.IFormatFileDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("ReportStatementDAO")
public class ReportStatementDAO extends BasicDAO implements IReportStatementDAO {

	@Resource(name = "FormatFileDAO")
	private IFormatFileDAO formatFileDAO;

	/**
	 * @throws Exception
	 * @see org.ace.accounting.report.reportStatement.persistence.interfaces.IReportStatementDAO#previewProcedure(
	 *      org.ace.accounting.report.CashBookCriteria)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ReportStatementDto> previewProcedure(boolean isObal, ReportType reportType, CurrencyType currencyType, Currency currency, Branch branch, Date reportDate,
			String formatType) throws Exception {
		List<ReportStatementDto> result = null;
		int lCount = 0;
		int rCount = 0;

		String budgetYear = BusinessUtil.getBudgetInfo(reportDate, 2);
		Calendar cal = Calendar.getInstance();
		cal.setTime(reportDate);
		String reportMonth = BusinessUtil.getBudgetInfo(reportDate, 3);
		String ccoaMonth = null;
		String ytdMonth = null;
		String obalMonth = null;
		if (currencyType.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
			ccoaMonth = "M" + reportMonth;
			obalMonth = "OBALM" + reportMonth;
			ytdMonth = "YTDM" + reportMonth;
		} else {
			ccoaMonth = "MSRC" + reportMonth;
			obalMonth = "OBALMSRC" + reportMonth;
			ytdMonth = "YTDMSRC" + reportMonth;
		}
		try {
			StringBuffer buffer = new StringBuffer(" SELECT * FROM (SELECT PL.COLTYPE, PL.LNO, CC.ACODE, D.DCODE, PL.DESP, PL.SHOWHIDE, PL.AMOUNTTOTAL, PL.STATUS, CC.ACTYPE, ");
			buffer.append(" SUM(ISNULL(CC." + ccoaMonth + ",0)) AS AMT, ISNULL((ISNULL(SUM(CC." + ytdMonth + "),0) - ISNULL(SUM(CC." + obalMonth + "),0)) ,0) AS TYRS, ");
			buffer.append(" ISNULL(SUM(CC." + obalMonth + "),0) AS OBAL, ISNULL(SUM(CC." + ytdMonth + "),0) as YTD ");
			buffer.append(" FROM FORMATFILE AS PL LEFT JOIN COA C ON PL.COAID = C.ID ");
			buffer.append(" LEFT JOIN DEPTCODE D ON PL.DEPTID = D.ID ");
			buffer.append(" INNER JOIN VW_REPORT_FORMAT AS CC ON C.ACCODE = CC.ACODE ");
			// Budget clause is moved here
			buffer.append(" AND ISNULL(BUDGET,?1) = ?1 ");

			// MOVED HERE
			// TODO -- according to vb code , currency clause is here
			if (currencyType.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
				buffer.append(" AND CC.CUR = ?4");
			}

			if (branch != null) {
				buffer.append(" AND ISNULL(CC.BRANCHID,?5) = ?5 ");
			}

			// TODO - need consultation
			// queryString.append(" WHERE BUDGET = ?1 AND FORMATTYPE =?2 AND
			// FORMATSTATUS =?3 ");
			// according to vb code , budget clause is here -- ISNULL(BUDGET,?1)
			// = ?1 AND
			buffer.append(" WHERE FORMATTYPE = ?2 AND FORMATSTATUS = ?3 ");

			// TODO -- according to vb code , branch clause is here

			buffer.append(" GROUP BY PL.LNO, CC.ACODE, D.DCODE, PL.DESP, PL.SHOWHIDE, PL.AMOUNTTOTAL, PL.STATUS, CC.ACTYPE, PL.COLTYPE");

			buffer.append(" UNION ALL ");

			StringBuffer nullCOAQuery = new StringBuffer();
			nullCOAQuery.append(
					" SELECT PL.COLTYPE, PL.LNO, null, null, PL.DESP, PL.SHOWHIDE, PL.AMOUNTTOTAL, PL.STATUS, null, 0.0000 AS AMT,0.0000 AS TYRS, 0.0000 AS OBAL, 0.0000 AS YTD");
			nullCOAQuery.append(" FROM FORMATFILE AS PL");
			nullCOAQuery.append(" WHERE FORMATTYPE = ?2 AND FORMATSTATUS = ?3 ");
			nullCOAQuery.append(" AND PL.COAID IS NULL");
			nullCOAQuery.append(" ) AS T");
			nullCOAQuery.append(" ORDER BY T.LNO");

			buffer.append(nullCOAQuery);

			Query q = em.createNativeQuery(buffer.toString());
			q.setParameter("1", budgetYear);
			q.setParameter("2", formatType);
			q.setParameter("3", reportType.name());

			if (currencyType.equals(CurrencyType.SOURCECURRENCY) && currency != null)
				q.setParameter("4", currency.getCurrencyCode());

			if (branch != null)
				q.setParameter("5", branch.getId());

			List<Object[]> tempResults = q.getResultList();
			if (tempResults != null && tempResults.size() > 0) {
				result = new ArrayList<>();
				for (Object[] obj : tempResults) {
					if ((obj[4] == null ? "" : obj[4].toString()).equalsIgnoreCase("LINE")) {
						ReportStatementDto dto = new ReportStatementDto();
						dto.setLno(Integer.parseInt(obj[1].toString()));
						dto.setAcCode(obj[2] == null ? null : obj[2].toString());
						dto.setdCode(obj[3] == null ? null : obj[3].toString());
						dto.setDesp("");
						dto.setShowHide((boolean) obj[5]);
						dto.setAmountTotal(String.valueOf(Boolean.parseBoolean(obj[6].toString())));
						dto.setStatus(obj[7] == null ? null : String.valueOf(Boolean.parseBoolean(obj[7].toString())));
						dto.setAmt(new BigDecimal(obj[10].toString()));
						dto.setcBal(isObal ? new BigDecimal(obj[12].toString()) : new BigDecimal(obj[10].toString()));
						dto.setAcType(obj[8] == null ? null : AccountType.valueOf(obj[8].toString()));
						result.add(dto);
					} else if ((obj[4] == null ? "" : obj[4].toString()).equalsIgnoreCase("AMOUNTLINE")) {
						ReportStatementDto dto = new ReportStatementDto();
						dto.setLno(Integer.parseInt(obj[1].toString()));
						dto.setAcCode("_________________");
						dto.setdCode(obj[3] == null ? null : obj[3].toString());
						// was 50 previously
						dto.setDesp(StringUtils.repeat("_", 31));
						dto.setShowHide((boolean) obj[5]);
						dto.setAmountTotal("L");
						dto.setStatus(obj[7] == null ? null : String.valueOf(Boolean.parseBoolean(obj[7].toString())));
						dto.setAmt(new BigDecimal(obj[10].toString()));
						dto.setcBal(isObal ? new BigDecimal(obj[12].toString()) : new BigDecimal(obj[10].toString()));
						dto.setAcType(obj[8] == null ? null : AccountType.valueOf(obj[8].toString()));
						result.add(dto);
					} else {
						if (obj[0] != null && !obj[0].toString().isEmpty() && (ColType.valueOf(obj[0].toString())).equals(ColType.L)) {
							lCount++;
							ReportStatementDto dto = new ReportStatementDto();
							dto.setLno(Integer.parseInt(obj[1].toString()));
							dto.setAcCode(obj[2] == null ? null : obj[2].toString());
							dto.setdCode(obj[3] == null ? null : obj[3].toString());
							dto.setDesp(obj[4] == null ? null : obj[4].toString());
							dto.setShowHide((boolean) obj[5]);
							dto.setAmountTotal(String.valueOf(Boolean.parseBoolean(obj[6].toString())));
							dto.setStatus(obj[8] == null ? null : obj[8].toString());
							dto.setoBal(new BigDecimal(obj[11].toString()));
							dto.setAmt(new BigDecimal(obj[10].toString()));
							dto.setcBal(isObal ? new BigDecimal(obj[12].toString()) : new BigDecimal(obj[10].toString()));
							dto.setColType(ColType.valueOf(obj[0].toString()));
							dto.setAcType(obj[8] == null ? null : AccountType.valueOf(obj[8].toString()));
							result.add(dto);
						} else if (obj[0] != null && !obj[0].toString().isEmpty() && (ColType.valueOf(obj[0].toString())).equals(ColType.R)) {
							rCount++;
							// if (Boolean.parseBoolean(obj[5].toString()) ||
							// rCount > lCount)
							// if (Boolean.parseBoolean(obj[5].toString()) ) {
							ReportStatementDto dto = new ReportStatementDto();
							dto.setLno(Integer.parseInt(obj[1].toString()));
							dto.setRlno(Integer.parseInt(obj[1].toString()));
							dto.setrAcCode(obj[2] == null ? null : obj[2].toString());
							dto.setdCode(obj[3] == null ? null : obj[3].toString());
							dto.setrDesp(obj[4] == null ? null : obj[4].toString());
							dto.setShowHide((boolean) obj[5]);
							dto.setAmountTotal(String.valueOf(Boolean.parseBoolean(obj[1].toString())));
							dto.setStatus(obj[8] == null ? null : obj[8].toString());
							dto.setoBal(new BigDecimal(obj[11].toString()));
							dto.setAmt(new BigDecimal(obj[10].toString()));
							dto.setrAmt(isObal ? new BigDecimal(obj[12].toString()) : new BigDecimal(obj[10].toString()));
							dto.setcBal(isObal ? new BigDecimal(obj[12].toString()) : new BigDecimal(obj[10].toString()));
							dto.setColType(ColType.valueOf(obj[0].toString()));
							dto.setRacType(obj[8] == null ? null : AccountType.valueOf(obj[8].toString()));
							result.add(dto);
						}
						// else {
						// for (ReportStatementDto dto : result) {
						// if (dto.getLno() == rCount) {
						// dto.setRlno(Integer.parseInt(obj[1].toString()));
						// dto.setShowHide(false);
						// dto.setrAcCode(obj[2] == null ? null :
						// obj[2].toString());
						// dto.setrDesp(obj[4] == null ? null :
						// obj[4].toString());
						// dto.setrAmt(new BigDecimal(obj[12].toString()));
						// dto.setRacType(obj[8] == null ? null :
						// AccountType.valueOf(obj[8].toString()));
						// }
						// }
						// }
						// }
						else {
							ReportStatementDto dto = new ReportStatementDto();
							dto.setLno(Integer.parseInt(obj[1].toString()));
							dto.setRlno(Integer.parseInt(obj[1].toString()));
							dto.setrAcCode(obj[2] == null ? null : obj[2].toString());
							dto.setdCode(obj[3] == null ? null : obj[3].toString());
							dto.setrDesp(obj[4] == null ? null : obj[4].toString());
							dto.setShowHide((boolean) obj[5]);
							dto.setAmountTotal(String.valueOf(Boolean.parseBoolean(obj[1].toString())));
							dto.setStatus(obj[8] == null ? null : obj[8].toString());
							dto.setoBal(new BigDecimal(obj[11].toString()));
							dto.setAmt(new BigDecimal(obj[10].toString()));
							dto.setrAmt(isObal ? new BigDecimal(obj[12].toString()) : new BigDecimal(obj[10].toString()));
							dto.setColType((obj[0] == null || obj[0].toString().isEmpty()) ? null : ColType.valueOf(obj[0].toString()));
							dto.setRacType(obj[8] == null ? null : AccountType.valueOf(obj[8].toString()));
							result.add(dto);
						}
					}
				}
			}
		} catch (PersistenceException pe) {
			throw translate("Failed to find report statement", pe);
		}
		if (result == null) {
			throw new Exception("No data found.");
		}
		return result;
	}

	/**
	 * for previous budgetYear
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ReportStatementDto> prevPreviewProcedure(boolean isObal, ReportType reportType, CurrencyType currencyType, Currency currency, Branch branch, Date reportDate,
			String budgetYear, String formatType) throws Exception {
		List<ReportStatementDto> result = null;
		int lCount = 0;
		int rCount = 0;

		String pBudgetYear = BusinessUtil.getPrevBudgetInfo(reportDate, budgetYear, 2);
		Calendar cal = Calendar.getInstance();
		cal.setTime(reportDate);
		String reportMonth = BusinessUtil.getPrevBudgetInfo(reportDate, budgetYear, 3);
		String ccoaMonth = null;
		String ytdMonth = null;
		String obalMonth = null;
		if (currencyType.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
			ccoaMonth = "M" + reportMonth;
			obalMonth = "OBALM" + reportMonth;
			ytdMonth = "YTDM" + reportMonth;
		} else {
			ccoaMonth = "MSRC" + reportMonth;
			obalMonth = "OBALMSRC" + reportMonth;
			ytdMonth = "YTDMSRC" + reportMonth;
		}
		try {
			StringBuffer buffer = new StringBuffer(" SELECT * FROM (SELECT PL.COLTYPE, PL.LNO, CC.ACODE, D.DCODE, PL.DESP, PL.SHOWHIDE, PL.AMOUNTTOTAL, PL.STATUS, CC.ACTYPE, ");
			buffer.append(" SUM(ISNULL(CC." + ccoaMonth + ",0)) AS AMT, ISNULL((ISNULL(SUM(CC." + ytdMonth + "),0) - ISNULL(SUM(CC." + obalMonth + "),0)) ,0) AS TYRS, ");
			buffer.append(" ISNULL(SUM(CC." + obalMonth + "),0) AS OBAL, ISNULL(SUM(CC." + ytdMonth + "),0) as YTD ");
			buffer.append(" FROM FORMATFILE AS PL LEFT JOIN COA C ON PL.COAID = C.ID ");
			buffer.append(" LEFT JOIN DEPTCODE D ON PL.DEPTID = D.ID ");
			buffer.append(" INNER JOIN VW_REPORT_FORMAT AS CC ON C.ACCODE = CC.ACODE ");
			// Budget clause is moved here
			// buffer.append(" AND ISNULL(BUDGET,?1) = ?1 ");

			// MOVED HERE
			// TODO -- according to vb code , currency clause is here
			if (currencyType.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
				buffer.append(" AND CC.CUR = ?4");
			}

			if (branch != null) {
				buffer.append(" AND ISNULL(CC.BRANCHID,?5) = ?5 ");
			}

			// TODO - need consultation
			// queryString.append(" WHERE BUDGET = ?1 AND FORMATTYPE =?2 AND
			// FORMATSTATUS =?3 ");
			// according to vb code , budget clause is here -- ISNULL(BUDGET,?1)
			// = ?1 AND
			buffer.append(" WHERE FORMATTYPE = ?2 AND FORMATSTATUS = ?3 ");

			buffer.append(" AND BUDGET = ?1 ");

			buffer.append(" OR FORMATTYPE = ?2 AND FORMATSTATUS = ?3 AND BUDGET = '' ");

			// TODO -- according to vb code , branch clause is here

			buffer.append(" GROUP BY PL.LNO, CC.ACODE, D.DCODE, PL.DESP, PL.SHOWHIDE, PL.AMOUNTTOTAL, PL.STATUS, CC.ACTYPE, PL.COLTYPE");

			buffer.append(" UNION ALL ");

			StringBuffer nullCOAQuery = new StringBuffer();
			nullCOAQuery.append(
					" SELECT PL.COLTYPE, PL.LNO, null, null, PL.DESP, PL.SHOWHIDE, PL.AMOUNTTOTAL, PL.STATUS, null, 0.0000 AS AMT,0.0000 AS TYRS, 0.0000 AS OBAL, 0.0000 AS YTD");
			nullCOAQuery.append(" FROM FORMATFILE AS PL");
			nullCOAQuery.append(" WHERE FORMATTYPE = ?2 AND FORMATSTATUS = ?3 ");
			nullCOAQuery.append(" AND PL.COAID IS NULL");
			nullCOAQuery.append(" ) AS T");
			nullCOAQuery.append(" ORDER BY T.LNO");

			buffer.append(nullCOAQuery);

			Query q = em.createNativeQuery(buffer.toString());
			q.setParameter("1", pBudgetYear);
			q.setParameter("2", formatType);
			q.setParameter("3", reportType.name());

			if (currencyType.equals(CurrencyType.SOURCECURRENCY) && currency != null)
				q.setParameter("4", currency.getCurrencyCode());

			if (branch != null)
				q.setParameter("5", branch.getId());

			List<Object[]> tempResults = q.getResultList();
			if (tempResults != null && tempResults.size() > 0) {
				result = new ArrayList<>();
				for (Object[] obj : tempResults) {
					if ((obj[4] == null ? "" : obj[4].toString()).equalsIgnoreCase("LINE")) {
						ReportStatementDto dto = new ReportStatementDto();
						dto.setLno(Integer.parseInt(obj[1].toString()));
						dto.setAcCode(obj[2] == null ? null : obj[2].toString());
						dto.setdCode(obj[3] == null ? null : obj[3].toString());
						dto.setDesp("");
						dto.setShowHide((boolean) obj[5]);
						dto.setAmountTotal(String.valueOf(Boolean.parseBoolean(obj[6].toString())));
						dto.setStatus(obj[7] == null ? null : String.valueOf(Boolean.parseBoolean(obj[7].toString())));
						dto.setAmt(new BigDecimal(obj[10].toString()));
						dto.setcBal(isObal ? new BigDecimal(obj[12].toString()) : new BigDecimal(obj[10].toString()));
						dto.setAcType(obj[8] == null ? null : AccountType.valueOf(obj[8].toString()));
						result.add(dto);
					} else if ((obj[4] == null ? "" : obj[4].toString()).equalsIgnoreCase("AMOUNTLINE")) {
						ReportStatementDto dto = new ReportStatementDto();
						dto.setLno(Integer.parseInt(obj[1].toString()));
						dto.setAcCode("_________________");
						dto.setdCode(obj[3] == null ? null : obj[3].toString());
						// was 50 previously
						dto.setDesp(StringUtils.repeat("_", 31));
						dto.setShowHide((boolean) obj[5]);
						dto.setAmountTotal("L");
						dto.setStatus(obj[7] == null ? null : String.valueOf(Boolean.parseBoolean(obj[7].toString())));
						dto.setAmt(new BigDecimal(obj[10].toString()));
						dto.setcBal(isObal ? new BigDecimal(obj[12].toString()) : new BigDecimal(obj[10].toString()));
						dto.setAcType(obj[8] == null ? null : AccountType.valueOf(obj[8].toString()));
						result.add(dto);
					} else {
						if (obj[0] != null && !obj[0].toString().isEmpty() && (ColType.valueOf(obj[0].toString())).equals(ColType.L)) {
							lCount++;
							ReportStatementDto dto = new ReportStatementDto();
							dto.setLno(Integer.parseInt(obj[1].toString()));
							dto.setAcCode(obj[2] == null ? null : obj[2].toString());
							dto.setdCode(obj[3] == null ? null : obj[3].toString());
							dto.setDesp(obj[4] == null ? null : obj[4].toString());
							dto.setShowHide((boolean) obj[5]);
							dto.setAmountTotal(String.valueOf(Boolean.parseBoolean(obj[6].toString())));
							dto.setStatus(obj[8] == null ? null : obj[8].toString());
							dto.setoBal(new BigDecimal(obj[11].toString()));
							dto.setAmt(new BigDecimal(obj[10].toString()));
							dto.setcBal(isObal ? new BigDecimal(obj[12].toString()) : new BigDecimal(obj[10].toString()));
							dto.setColType(ColType.valueOf(obj[0].toString()));
							dto.setAcType(obj[8] == null ? null : AccountType.valueOf(obj[8].toString()));
							result.add(dto);
						} else if (obj[0] != null && !obj[0].toString().isEmpty() && (ColType.valueOf(obj[0].toString())).equals(ColType.R)) {
							rCount++;
							// if (Boolean.parseBoolean(obj[5].toString()) ||
							// rCount > lCount)
							// if (Boolean.parseBoolean(obj[5].toString()) ) {
							ReportStatementDto dto = new ReportStatementDto();
							dto.setLno(Integer.parseInt(obj[1].toString()));
							dto.setRlno(Integer.parseInt(obj[1].toString()));
							dto.setrAcCode(obj[2] == null ? null : obj[2].toString());
							dto.setdCode(obj[3] == null ? null : obj[3].toString());
							dto.setrDesp(obj[4] == null ? null : obj[4].toString());
							dto.setShowHide((boolean) obj[5]);
							dto.setAmountTotal(String.valueOf(Boolean.parseBoolean(obj[1].toString())));
							dto.setStatus(obj[8] == null ? null : obj[8].toString());
							dto.setoBal(new BigDecimal(obj[11].toString()));
							dto.setAmt(new BigDecimal(obj[10].toString()));
							dto.setrAmt(isObal ? new BigDecimal(obj[12].toString()) : new BigDecimal(obj[10].toString()));
							dto.setcBal(isObal ? new BigDecimal(obj[12].toString()) : new BigDecimal(obj[10].toString()));
							dto.setColType(ColType.valueOf(obj[0].toString()));
							dto.setRacType(obj[8] == null ? null : AccountType.valueOf(obj[8].toString()));
							result.add(dto);
						}
						// else {
						// for (ReportStatementDto dto : result) {
						// if (dto.getLno() == rCount) {
						// dto.setRlno(Integer.parseInt(obj[1].toString()));
						// dto.setShowHide(false);
						// dto.setrAcCode(obj[2] == null ? null :
						// obj[2].toString());
						// dto.setrDesp(obj[4] == null ? null :
						// obj[4].toString());
						// dto.setrAmt(new BigDecimal(obj[12].toString()));
						// dto.setRacType(obj[8] == null ? null :
						// AccountType.valueOf(obj[8].toString()));
						// }
						// }
						// }
						// }
						else {
							ReportStatementDto dto = new ReportStatementDto();
							dto.setLno(Integer.parseInt(obj[1].toString()));
							dto.setRlno(Integer.parseInt(obj[1].toString()));
							dto.setrAcCode(obj[2] == null ? null : obj[2].toString());
							dto.setdCode(obj[3] == null ? null : obj[3].toString());
							dto.setrDesp(obj[4] == null ? null : obj[4].toString());
							dto.setShowHide((boolean) obj[5]);
							dto.setAmountTotal(String.valueOf(Boolean.parseBoolean(obj[1].toString())));
							dto.setStatus(obj[8] == null ? null : obj[8].toString());
							dto.setoBal(new BigDecimal(obj[11].toString()));
							dto.setAmt(new BigDecimal(obj[10].toString()));
							dto.setrAmt(isObal ? new BigDecimal(obj[12].toString()) : new BigDecimal(obj[10].toString()));
							dto.setColType((obj[0] == null || obj[0].toString().isEmpty()) ? null : ColType.valueOf(obj[0].toString()));
							dto.setRacType(obj[8] == null ? null : AccountType.valueOf(obj[8].toString()));
							result.add(dto);
						}
					}
				}
			}
		} catch (PersistenceException pe) {
			throw translate("Failed to find report statement", pe);
		}
		if (result == null) {
			throw new Exception("No data found.");
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ReportStatementDto> previewProcedureForCloneData(boolean isObal, ReportType reportType, CurrencyType currencyType, Currency currency, Branch branch,
			Date reportDate, String budgetYear, String formatType) throws Exception {
		List<ReportStatementDto> result = null;
		int lCount = 0;
		int rCount = 0;

		String pBudgetYear = BusinessUtil.getPrevBudgetInfo(reportDate, budgetYear, 2);
		Calendar cal = Calendar.getInstance();
		cal.setTime(reportDate);
		String reportMonth = BusinessUtil.getPrevBudgetInfo(reportDate, budgetYear, 3);
		String ccoaMonth = null;
		String ytdMonth = null;
		String obalMonth = null;
		if (currencyType.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
			ccoaMonth = "M" + reportMonth;
			obalMonth = "OBALM" + reportMonth;
			ytdMonth = "YTDM" + reportMonth;
		} else {
			ccoaMonth = "MSRC" + reportMonth;
			obalMonth = "OBALMSRC" + reportMonth;
			ytdMonth = "YTDMSRC" + reportMonth;
		}
		try {
			StringBuffer buffer = new StringBuffer(" SELECT * FROM (SELECT PL.COLTYPE, PL.LNO, CC.ACODE, D.DCODE, PL.DESP, PL.SHOWHIDE, PL.AMOUNTTOTAL, PL.STATUS, CC.ACTYPE, ");
			buffer.append(" SUM(ISNULL(CC." + ccoaMonth + ",0)) AS AMT, ISNULL((ISNULL(SUM(CC." + ytdMonth + "),0) - ISNULL(SUM(CC." + obalMonth + "),0)) ,0) AS TYRS, ");
			buffer.append(" ISNULL(SUM(CC." + obalMonth + "),0) AS OBAL, ISNULL(SUM(CC." + ytdMonth + "),0) as YTD ");
			buffer.append(" FROM FORMATFILE AS PL LEFT JOIN COA C ON PL.COAID = C.ID ");
			buffer.append(" LEFT JOIN DEPTCODE D ON PL.DEPTID = D.ID ");
			buffer.append(" INNER JOIN VW_REPORT_FORMAT_CLONE AS CC ON C.ACCODE = CC.ACODE ");
			// Budget clause is moved here
			// buffer.append(" AND ISNULL(BUDGET,?1) = ?1 ");

			// MOVED HERE
			// TODO -- according to vb code , currency clause is here
			if (currencyType.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
				buffer.append(" AND CC.CUR = ?4");
			}

			if (branch != null) {
				buffer.append(" AND ISNULL(CC.BRANCHID,?5) = ?5 ");
			}

			// TODO - need consultation
			// queryString.append(" WHERE BUDGET = ?1 AND FORMATTYPE =?2 AND
			// FORMATSTATUS =?3 ");
			// according to vb code , budget clause is here -- ISNULL(BUDGET,?1)
			// = ?1 AND
			buffer.append(" WHERE FORMATTYPE = ?2 AND FORMATSTATUS = ?3 ");

			buffer.append(" AND BUDGET = ?1 ");

			buffer.append(" OR FORMATTYPE = ?2 AND FORMATSTATUS = ?3 AND BUDGET = '' ");

			// TODO -- according to vb code , branch clause is here

			buffer.append(" GROUP BY PL.LNO, CC.ACODE, D.DCODE, PL.DESP, PL.SHOWHIDE, PL.AMOUNTTOTAL, PL.STATUS, CC.ACTYPE, PL.COLTYPE");

			buffer.append(" UNION ALL ");

			StringBuffer nullCOAQuery = new StringBuffer();
			nullCOAQuery.append(
					" SELECT PL.COLTYPE, PL.LNO, null, null, PL.DESP, PL.SHOWHIDE, PL.AMOUNTTOTAL, PL.STATUS, null, 0.0000 AS AMT,0.0000 AS TYRS, 0.0000 AS OBAL, 0.0000 AS YTD");
			nullCOAQuery.append(" FROM FORMATFILE AS PL");
			nullCOAQuery.append(" WHERE FORMATTYPE = ?2 AND FORMATSTATUS = ?3 ");
			nullCOAQuery.append(" AND PL.COAID IS NULL");
			nullCOAQuery.append(" ) AS T");
			nullCOAQuery.append(" ORDER BY T.LNO");

			buffer.append(nullCOAQuery);

			Query q = em.createNativeQuery(buffer.toString());
			q.setParameter("1", pBudgetYear);
			q.setParameter("2", formatType);
			q.setParameter("3", reportType.name());

			if (currencyType.equals(CurrencyType.SOURCECURRENCY) && currency != null)
				q.setParameter("4", currency.getCurrencyCode());

			if (branch != null)
				q.setParameter("5", branch.getId());

			List<Object[]> tempResults = q.getResultList();
			if (tempResults != null && tempResults.size() > 0) {
				result = new ArrayList<>();
				for (Object[] obj : tempResults) {
					if ((obj[4] == null ? "" : obj[4].toString()).equalsIgnoreCase("LINE")) {
						ReportStatementDto dto = new ReportStatementDto();
						dto.setLno(Integer.parseInt(obj[1].toString()));
						dto.setAcCode(obj[2] == null ? null : obj[2].toString());
						dto.setdCode(obj[3] == null ? null : obj[3].toString());
						dto.setDesp("");
						dto.setShowHide((boolean) obj[5]);
						dto.setAmountTotal(String.valueOf(Boolean.parseBoolean(obj[6].toString())));
						dto.setStatus(obj[7] == null ? null : String.valueOf(Boolean.parseBoolean(obj[7].toString())));
						dto.setAmt(new BigDecimal(obj[10].toString()));
						dto.setcBal(isObal ? new BigDecimal(obj[12].toString()) : new BigDecimal(obj[10].toString()));
						dto.setAcType(obj[8] == null ? null : AccountType.valueOf(obj[8].toString()));
						result.add(dto);
					} else if ((obj[4] == null ? "" : obj[4].toString()).equalsIgnoreCase("AMOUNTLINE")) {
						ReportStatementDto dto = new ReportStatementDto();
						dto.setLno(Integer.parseInt(obj[1].toString()));
						dto.setAcCode("_________________");
						dto.setdCode(obj[3] == null ? null : obj[3].toString());
						// was 50 previously
						dto.setDesp(StringUtils.repeat("_", 31));
						dto.setShowHide((boolean) obj[5]);
						dto.setAmountTotal("L");
						dto.setStatus(obj[7] == null ? null : String.valueOf(Boolean.parseBoolean(obj[7].toString())));
						dto.setAmt(new BigDecimal(obj[10].toString()));
						dto.setcBal(isObal ? new BigDecimal(obj[12].toString()) : new BigDecimal(obj[10].toString()));
						dto.setAcType(obj[8] == null ? null : AccountType.valueOf(obj[8].toString()));
						result.add(dto);
					} else {
						if (obj[0] != null && !obj[0].toString().isEmpty() && (ColType.valueOf(obj[0].toString())).equals(ColType.L)) {
							lCount++;
							ReportStatementDto dto = new ReportStatementDto();
							dto.setLno(Integer.parseInt(obj[1].toString()));
							dto.setAcCode(obj[2] == null ? null : obj[2].toString());
							dto.setdCode(obj[3] == null ? null : obj[3].toString());
							dto.setDesp(obj[4] == null ? null : obj[4].toString());
							dto.setShowHide((boolean) obj[5]);
							dto.setAmountTotal(String.valueOf(Boolean.parseBoolean(obj[6].toString())));
							dto.setStatus(obj[8] == null ? null : obj[8].toString());
							dto.setoBal(new BigDecimal(obj[11].toString()));
							dto.setAmt(new BigDecimal(obj[10].toString()));
							dto.setcBal(isObal ? new BigDecimal(obj[12].toString()) : new BigDecimal(obj[10].toString()));
							dto.setColType(ColType.valueOf(obj[0].toString()));
							dto.setAcType(obj[8] == null ? null : AccountType.valueOf(obj[8].toString()));
							result.add(dto);
						} else if (obj[0] != null && !obj[0].toString().isEmpty() && (ColType.valueOf(obj[0].toString())).equals(ColType.R)) {
							rCount++;
							// if (Boolean.parseBoolean(obj[5].toString()) ||
							// rCount > lCount)
							// if (Boolean.parseBoolean(obj[5].toString()) ) {
							ReportStatementDto dto = new ReportStatementDto();
							dto.setLno(Integer.parseInt(obj[1].toString()));
							dto.setRlno(Integer.parseInt(obj[1].toString()));
							dto.setrAcCode(obj[2] == null ? null : obj[2].toString());
							dto.setdCode(obj[3] == null ? null : obj[3].toString());
							dto.setrDesp(obj[4] == null ? null : obj[4].toString());
							dto.setShowHide((boolean) obj[5]);
							dto.setAmountTotal(String.valueOf(Boolean.parseBoolean(obj[1].toString())));
							dto.setStatus(obj[8] == null ? null : obj[8].toString());
							dto.setoBal(new BigDecimal(obj[11].toString()));
							dto.setAmt(new BigDecimal(obj[10].toString()));
							dto.setrAmt(isObal ? new BigDecimal(obj[12].toString()) : new BigDecimal(obj[10].toString()));
							dto.setcBal(isObal ? new BigDecimal(obj[12].toString()) : new BigDecimal(obj[10].toString()));
							dto.setColType(ColType.valueOf(obj[0].toString()));
							dto.setRacType(obj[8] == null ? null : AccountType.valueOf(obj[8].toString()));
							result.add(dto);
						}
						// else {
						// for (ReportStatementDto dto : result) {
						// if (dto.getLno() == rCount) {
						// dto.setRlno(Integer.parseInt(obj[1].toString()));
						// dto.setShowHide(false);
						// dto.setrAcCode(obj[2] == null ? null :
						// obj[2].toString());
						// dto.setrDesp(obj[4] == null ? null :
						// obj[4].toString());
						// dto.setrAmt(new BigDecimal(obj[12].toString()));
						// dto.setRacType(obj[8] == null ? null :
						// AccountType.valueOf(obj[8].toString()));
						// }
						// }
						// }
						// }
						else {
							ReportStatementDto dto = new ReportStatementDto();
							dto.setLno(Integer.parseInt(obj[1].toString()));
							dto.setRlno(Integer.parseInt(obj[1].toString()));
							dto.setrAcCode(obj[2] == null ? null : obj[2].toString());
							dto.setdCode(obj[3] == null ? null : obj[3].toString());
							dto.setrDesp(obj[4] == null ? null : obj[4].toString());
							dto.setShowHide((boolean) obj[5]);
							dto.setAmountTotal(String.valueOf(Boolean.parseBoolean(obj[1].toString())));
							dto.setStatus(obj[8] == null ? null : obj[8].toString());
							dto.setoBal(new BigDecimal(obj[11].toString()));
							dto.setAmt(new BigDecimal(obj[10].toString()));
							dto.setrAmt(isObal ? new BigDecimal(obj[12].toString()) : new BigDecimal(obj[10].toString()));
							dto.setColType((obj[0] == null || obj[0].toString().isEmpty()) ? null : ColType.valueOf(obj[0].toString()));
							dto.setRacType(obj[8] == null ? null : AccountType.valueOf(obj[8].toString()));
							result.add(dto);
						}
					}
				}
			}
		} catch (PersistenceException pe) {
			throw translate("Failed to find report statement", pe);
		}
		if (result == null) {
			throw new Exception("No data found.");
		}
		return result;
	}

	/**
	 * for translating and calculating the formula
	 */

}
