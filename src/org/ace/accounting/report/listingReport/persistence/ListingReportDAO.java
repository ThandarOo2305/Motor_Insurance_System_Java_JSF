package org.ace.accounting.report.listingReport.persistence;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.dto.CashBookDTO;
import org.ace.accounting.dto.CashBookDetail;
import org.ace.accounting.dto.CoaListingDto;
import org.ace.accounting.dto.VLdto;
import org.ace.accounting.dto.VPFTdto;
import org.ace.accounting.dto.VPdto;
import org.ace.accounting.report.CashBookCriteria;
import org.ace.accounting.report.listingReport.persistence.interfaces.IListingReportDAO;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.AccountType;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.coasetup.COASetup;
import org.ace.accounting.system.coasetup.persistence.interfaces.ICOASetupDAO;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.persistence.interfaces.ICurrencyDAO;
import org.ace.accounting.system.setup.persistence.interfaces.ISetup_HistoryDAO;
import org.ace.accounting.system.setup.service.ISetupService;
import org.ace.accounting.system.trantype.TranCode;
import org.ace.accounting.web.listing.TransactionType;
import org.ace.accounting.web.listing.VoucherListingType;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("ListingReportDAO")
public class ListingReportDAO extends BasicDAO implements IListingReportDAO {

	@Resource(name = "COASetupDAO")
	private ICOASetupDAO coaSetupDAO;

	@Resource(name = "CurrencyDAO")
	private ICurrencyDAO currencyDao;

	@Resource(name = "SetupService")
	private ISetupService setupService;

	@Resource(name = "Setup_HistoryDAO")
	private ISetup_HistoryDAO setupHistoryDAO;

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CoaListingDto> findAllCoaList() throws DAOException {
		List<CoaListingDto> result = null;
		try {
			StringBuffer queryString = new StringBuffer();
			queryString.append("SELECT new org.ace.accounting.dto.CoaListingDto(coa.acCode,coa.acName,coa.acType) FROM ChartOfAccount coa order by coa.acCode");
			Query q = em.createQuery(queryString.toString());
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find Chart Of Account List", pe);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<VLdto> findVoucherListingReport(VoucherListingType type, Date startDate, Date endDate, Branch branch, Currency currency, Boolean isOnlyVoucher, boolean isReverse) {
		List<VLdto> result = null;
		String localAmount = null;
		StringBuffer sf;
		String tableName = "TLF";
		Date budgetStartDate = DateUtils.formatStringToDate(setupService.findSetupValueByVariable(BusinessUtil.BUDGETSDATE));
		/*
		 * if (currency == null) { localAmount = "homeAmount"; } else {
		 * localAmount = "localAmount"; }
		 */

		try {

			StringBuffer queryString = null;

			if (startDate.before(budgetStartDate)) {
				tableName = "TLFHIST";
			}

			queryString = new StringBuffer(
					" SELECT NEW org.ace.accounting.dto.VLdto(tlf.settlementDate, tlf.eNo, tlf.ccoa.coa.acCode, tlf.ccoa.coa.acName, tlf.narration,tlf.currency.currencyCode, tlf.rate, "
							+ " CASE WHEN (tlf.tranType.tranCode IN (org.ace.accounting.system.trantype.TranCode.CSDEBIT,org.ace.accounting.system.trantype.TranCode.TRDEBIT)) THEN tlf.localAmount ELSE  0  END ,"
							+ " CASE WHEN (tlf.tranType.tranCode IN (org.ace.accounting.system.trantype.TranCode.CSCREDIT,org.ace.accounting.system.trantype.TranCode.TRCREDIT)) THEN tlf.localAmount ELSE  0  END ) FROM "
							+ tableName + " tlf WHERE tlf.settlementDate >= :startDate AND tlf.settlementDate <= :endDate AND tlf.reverse= :isReverse AND tlf.paid=true ");

			if (branch != null) {
				queryString.append(" AND tlf.branch.id = :branchId ");
			}
			if (currency != null) {
				queryString.append(" AND tlf.currency.id = :currencyId ");
			}
			if (null != isOnlyVoucher) {
				if (isOnlyVoucher.booleanValue()) {
					queryString.append(" AND tlf.eNo like 'VOC%' ");
				} else {
					queryString.append(" AND tlf.eNo not like 'VOC%' ");
				}

			}

			if (type.equals(VoucherListingType.CC)) {
				queryString.append(" AND ((tlf.tranType.status='CCV' AND  tlf.ccoa.coa NOT IN :coaList) OR (tlf.tranType.status='CDV' AND  tlf.ccoa.coa IN :coaList))  ");
			} else if (type.equals(VoucherListingType.T)) {
				queryString.append(" AND tlf.tranType.status IN ('TCV','TDV') ");
			} else if (type.equals(VoucherListingType.CD)) {
				queryString.append(" AND ((tlf.tranType.status='CDV' AND  tlf.ccoa.coa NOT IN :coaList) OR (tlf.tranType.status='CCV' AND  tlf.ccoa.coa IN :coaList))  ");
			}

			queryString.append(" ORDER BY tlf.currency.currencyCode, tlf.eNo ASC ");

			Query q = em.createQuery(queryString.toString());

			startDate = DateUtils.resetStartDate(startDate);
			q.setParameter("startDate", startDate);

			endDate = DateUtils.resetEndDate(endDate);

			q.setParameter("endDate", endDate);

			if (branch != null) {
				q.setParameter("branchId", branch.getId());
			}
			if (currency != null) {
				q.setParameter("currencyId", currency.getId());
			}

			if (type.equals(VoucherListingType.CC) || type.equals(VoucherListingType.CD)) {
				List<ChartOfAccount> coaList = getCashCoaList(currency, branch);
				q.setParameter("coaList", coaList);
			}

			q.setParameter("isReverse", isReverse);

			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find voucher listing report", pe);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private String getOpeningBalanceField(CurrencyType type, Date reportDate, boolean curConverted) {
		String openingBalanceField = null;
		int previousBudgetMonth;
		String budgetYear;

		// String budgetYear = BusinessUtil.getBudgetInfo(reportDate, 2);
		String currentYear = BusinessUtil.getCurrentBudget();

		if (DateUtils.isIntervalBugetYear(reportDate))
			budgetYear = BusinessUtil.getBudgetInfo(reportDate, 2);
		else
			budgetYear = setupHistoryDAO.findbudgetyear(reportDate);

		if (budgetYear.equals(currentYear)) {
			previousBudgetMonth = Integer.parseInt(BusinessUtil.getBudgetInfo(reportDate, 3)) - 1;
		} else {
			previousBudgetMonth = Integer.parseInt(BusinessUtil.getPrevBudgetInfo(reportDate, budgetYear, 3)) - 1;
		}

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

	/**
	 * @see org.ace.accounting.report.listingReport.persistence.interfaces.IListingReportDAO#findCashbookListingReport(
	 *      org.ace.accounting.report.CashBookCriteria)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<CashBookDTO> findCashbookListingReport(CashBookCriteria criteria) {
		Map<String, CashBookDTO> cashBookMap = new HashMap<String, CashBookDTO>();
		StringBuffer buffer = null;
		Query query = null;
		boolean isConvert = criteria.isHomeCurrencyConverted();
		boolean isHomeCurrency = criteria.isHomeCurrency();

		if (isHomeCurrency) {
			isConvert = true;
			criteria.setCurrency(null);
		}
		try {

			/**
			 * find cash account code from COASetup table in database. According
			 * to currency type.
			 */
			buffer = new StringBuffer();
			buffer.append(" SELECT DISTINCT c.acCode FROM COASetup cs JOIN cs.ccoa cc JOIN cc.coa c ");
			// buffer.append(" WHERE cc.currency.id = :currenyId AND cs.acName =
			// 'CASH'");
			buffer.append(" WHERE cs.acName = 'CASH'");
			if (!isHomeCurrency) {
				buffer.append(" AND cc.currency.id = :currenyId ");
			}
			query = em.createQuery(buffer.toString());
			if (!isHomeCurrency) {
				query.setParameter("currenyId", criteria.getCurrency().getId());
			}
			List<String> acCodeList = query.getResultList();
			em.flush();

			CurrencyType type = criteria.getCurrencyType();
			Currency currency = criteria.getCurrency();
			Branch branch = criteria.getBranch();
			Date reportDate = DateUtils.resetStartDate(criteria.getFromDate());
			BigDecimal result = BigDecimal.ZERO;

			String openingBalanceField = getOpeningBalanceField(type, criteria.getFromDate(), isConvert);
			StringBuffer queryString = new StringBuffer("SELECT SUM(c." + openingBalanceField + ") FROM VwCcoa c WHERE c.coa in :cashCoaList AND c.budget=:budget ");
			if (criteria.getBranch() != null) {
				queryString.append(" AND c.branch=:branch ");
			}

			if (type.equals(CurrencyType.SOURCECURRENCY) && criteria.getCurrency() != null) {
				queryString.append(" AND c.currency=:currency ");
			}

			Query q = em.createQuery(queryString.toString());
			List<ChartOfAccount> cashCoaList = getCashCoaList(currency, branch);
			q.setParameter("cashCoaList", cashCoaList);
			// q.setParameter("budget", BusinessUtil.getBudgetInfo(reportDate,
			// 2));

			if (DateUtils.isIntervalBugetYear(reportDate))
				q.setParameter("budget", BusinessUtil.getBudgetInfo(reportDate, 2));
			else {
				String budgetYear = setupHistoryDAO.findbudgetyear(reportDate);
				q.setParameter("budget", budgetYear);
			}

			if (branch != null) {
				q.setParameter("branch", branch);
			}
			if (type.equals(CurrencyType.SOURCECURRENCY) && currency != null) {
				q.setParameter("currency", currency);
			}
			result = (BigDecimal) q.getSingleResult();

			String field;
			if (isConvert || type.equals(CurrencyType.HOMECURRENCY)) {
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
				cashBookMap.put(cashCoaList.get(0).getAcCode(), new CashBookDTO(criteria.getFromDate(), cashCoaList.get(0).getAcCode(), cashCoaList.get(0).getAcName(), result));
			}

			// String acodes = "";
			// acodes += "(";
			// for (String acode : acCodeList) {
			// acodes += " '" + acode + "',";
			// }
			// acodes = acodes.substring(0, acodes.length() - 1);
			// acodes += ")";
			// System.out.println(acodes);

			/**
			 * find opening balance for respective acCode. According to fromDate
			 * of criteria.
			 */
			buffer = new StringBuffer();
			// String budgetInfo =
			// BusinessUtil.getBudgetInfo(criteria.getFromDate(), 3);
			String budgetInfo;
			String cashBookYear;
			String currentYear = BusinessUtil.getCurrentBudget();

			if (DateUtils.isIntervalBugetYear(criteria.getFromDate()))
				cashBookYear = BusinessUtil.getBudgetInfo(criteria.getFromDate(), 2);
			else
				cashBookYear = setupHistoryDAO.findbudgetyear(criteria.getFromDate());

			if (cashBookYear.equals(currentYear)) {
				budgetInfo = BusinessUtil.getBudgetInfo(criteria.getFromDate(), 3);
			} else {
				budgetInfo = BusinessUtil.getPrevBudgetInfo(criteria.getFromDate(), cashBookYear, 3);
			}

			Date budgetStartDate = BusinessUtil.getBudgetStartDate();
			int cashBookMonth = Integer.parseInt(budgetInfo);
			String ccoaColumn = null;
			boolean isHistory = criteria.getFromDate().before(budgetStartDate);

			// if currency type of criteria is Home currency,
			String tlfColumn = isConvert ? "t.homeAmount" : "t.localAmount";

			String ccoaTable = isHistory ? "CcoaHistory" : "CurrencyChartOfAccount";

			String tlfTable = isHistory ? "TLFHIST" : "TLF";
			switch (cashBookMonth) {
				case 1:
					ccoaColumn = isConvert ? "cc.hOBal" : "cc.oBal";
					break;
				default:
					ccoaColumn = isConvert ? "cc.msrcRate.msrc" + (cashBookMonth - 1) : "cc.monthlyRate.m" + (cashBookMonth - 1);
					break;
			}

			String filter = "";
			boolean haveBranch = criteria.getBranch() == null ? false : true;
			boolean haveCurrency = criteria.getCurrency() == null ? false : true;
			if (haveBranch) {
				filter = filter + " AND cc.branch.id = :branchId ";
			}

			if (haveCurrency) {
				filter = filter + " AND cc.currency.id = :currencyId ";
			}

			buffer.append(" SELECT c.acCode, cc.acName, SUM(" + ccoaColumn + ") FROM " + ccoaTable + " cc JOIN cc.coa c ");
			buffer.append(" WHERE cc.budget = :budgetYear AND c.acCode IN :acCodeList ");
			buffer.append(filter);
			buffer.append(" GROUP BY c.acCode, cc.acName ");
			buffer.append(" UNION ALL ");
			buffer.append(" SELECT c.acCode, cc.acName, SUM(" + tlfColumn + ") FROM " + tlfTable + " t JOIN t.ccoa cc JOIN cc.coa c ");
			buffer.append(" WHERE cc.budget = :budgetYear");
			buffer.append(filter);
			buffer.append(" AND c.acCode IN :acCodeList AND t.reverse = 0 AND t.paid = 1");
			buffer.append(" AND t.tranType.tranCode IN :tranCode_Param_1 AND c.acType IN :accountType_Param_1");
			buffer.append(" GROUP BY c.acCode, cc.acName ");
			buffer.append(" UNION ALL ");
			buffer.append(" SELECT c.acCode, cc.acName, SUM(-1 * " + tlfColumn + ") FROM " + tlfTable + " t JOIN t.ccoa cc JOIN cc.coa c ");
			buffer.append(" WHERE c.acCode IN :acCodeList AND cc.budget = :budgetYear ");
			buffer.append(filter);
			buffer.append(" AND t.reverse = 0 AND t.paid = 1");
			buffer.append(" AND t.tranType.tranCode IN :tranCode_Param_1 AND c.acType IN :accountType_Param_2");
			buffer.append(" GROUP BY c.acCode, cc.acName ");
			buffer.append(" UNION ALL ");
			buffer.append(" SELECT c.acCode, cc.acName, SUM(" + tlfColumn + ") FROM " + tlfTable + " t JOIN t.ccoa cc JOIN cc.coa c ");
			buffer.append(" WHERE c.acCode IN :acCodeList AND cc.budget = :budgetYear ");
			buffer.append(filter);
			buffer.append(" AND t.reverse = 0 AND t.paid = 1");
			buffer.append(" AND t.tranType.tranCode IN :tranCode_Param_2 AND c.acType IN :accountType_Param_2");
			buffer.append(" GROUP BY c.acCode, cc.acName ");
			buffer.append(" UNION ALL ");
			buffer.append(" SELECT c.acCode, cc.acName, SUM(-1 * " + tlfColumn + ") FROM " + tlfTable + " t JOIN t.ccoa cc JOIN cc.coa c ");
			buffer.append(" WHERE c.acCode IN :acCodeList AND cc.budget = :budgetYear ");
			buffer.append(filter);
			buffer.append(" AND t.reverse = 0 AND t.paid = 1");
			buffer.append(" AND t.tranType.tranCode IN :tranCode_Param_2 AND c.acType IN :accountType_Param_1");
			buffer.append(" GROUP BY c.acCode, cc.acName ");

			query = em.createQuery(buffer.toString());

			query.setParameter("acCodeList", Arrays.asList(acCodeList));
			query.setParameter("budgetYear", cashBookYear);
			if (haveBranch) {
				query.setParameter("branchId", criteria.getBranch().getId());
			}

			if (haveCurrency) {
				query.setParameter("currencyId", criteria.getCurrency().getId());
			}

			query.setParameter("tranCode_Param_1", Arrays.asList(TranCode.CSDEBIT, TranCode.TRDEBIT));
			query.setParameter("tranCode_Param_2", Arrays.asList(TranCode.CSCREDIT, TranCode.TRCREDIT));
			query.setParameter("accountType_Param_1", Arrays.asList(AccountType.A, AccountType.E));
			query.setParameter("accountType_Param_2", Arrays.asList(AccountType.L, AccountType.I));
			List<Object[]> objArr = query.getResultList();
			String acCode = null;
			String acName = null;
			BigDecimal openingBal = null;
			for (Object[] obj : objArr) {
				acCode = (String) obj[0];
				acName = (String) obj[1];
				openingBal = (BigDecimal) obj[2];
				// TODO FIXME PSH --FOR CLEAN AND Create Common method for
				// oppeining balance
				if (cashBookMap.containsKey(acCode)) {
					// cashBookMap.get(acCode).addOpeningBalance(openingBal);
				} else {
					// cashBookMap.put(acCode, new
					// CashBookDTO(criteria.getFromDate(), acCode, acName,
					// openingBal));
				}

			}
			em.flush();

			buffer = new StringBuffer();
			buffer.append(" SELECT new org.ace.accounting.dto.CashBookDetail(w.settlementDate, w.contraCode, w.contraName, w.desp, w.acType, ");
			buffer.append(isConvert ? " w.homeCredit, w.homeDebit)" : " w.credit, w.debit)");
			buffer.append(" FROM VwCashbook w ");
			buffer.append(" WHERE w.settlementDate >= :fromDate AND w.settlementDate <= :toDate ");
			buffer.append(" AND w.acode = :acode ");
			if (haveBranch) {
				buffer.append(" AND w.branchID = :branchID ");
			}

			if (haveCurrency) {
				buffer.append(" AND w.currencyID = :currencyID ");
			}

			query = em.createQuery(buffer.toString());
			for (CashBookDTO cashBook : cashBookMap.values()) {
				if (haveBranch) {
					query.setParameter("branchID", criteria.getBranch().getId());
				}

				if (haveCurrency) {
					query.setParameter("currencyID", criteria.getCurrency().getId());
				}

				query.setParameter("fromDate", DateUtils.resetStartDate(criteria.getFromDate()));
				query.setParameter("toDate", DateUtils.resetEndDate(criteria.getToDate()));
				query.setParameter("acode", cashBook.getAcCode());
				List<CashBookDetail> details = query.getResultList();
				cashBook.setDetails(details);
			}

			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find cashbook listing report", pe);
		}

		return new ArrayList<CashBookDTO>(cashBookMap.values());
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private List<ChartOfAccount> getCashCoaList(Currency currency, Branch branch) {
		List<COASetup> coaSetupList = coaSetupDAO.findCOAListByACNameAndCur("CASH", currency, branch);
		List<ChartOfAccount> coaList = new ArrayList<ChartOfAccount>();
		for (COASetup coaSetup : coaSetupList) {
			coaList.add(coaSetup.getCcoa().getCoa());
		}
		return coaList;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<VPFTdto> findFromVoucherNo(Date startDate, Date endDate, Branch branch) {
		List<VPFTdto> result = null;
		try {
			StringBuffer queryString = new StringBuffer();
			queryString.append(
					"SELECT DISTINCT new org.ace.accounting.dto.VPFTdto(t.eNo) FROM TLF t WHERE t.settlementDate >= :startDate AND t.settlementDate <= :endDate AND t.reverse=false AND t.paid=true ");
			
			  if (branch != null) 
			  {  
				  queryString.append(" AND t.branch.id=:branch ");
			  }
			  queryString.append(" ORDER BY t.eNo ");
			  
			 
			Query q = em.createQuery(queryString.toString());
			startDate = DateUtils.resetStartDate(startDate);
			q.setParameter("startDate", startDate);
			endDate = DateUtils.resetEndDate(endDate);
			q.setParameter("endDate", endDate);
			if (branch != null) {
				q.setParameter("branch", branch.getId());
			}
			result = q.getResultList();
			em.flush();
		} catch (NoResultException pe) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find VoucherNo", pe);
		}
		return result;

	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<VPFTdto> findFromVoucherNoForGenJournal(VoucherListingType type, Date startDate, Date endDate, Branch branch, Currency currency, TransactionType tType) {
		List<VPFTdto> result = null;
		String tableName = "TLF";
		Date budgetStartDate = DateUtils.formatStringToDate(setupService.findSetupValueByVariable(BusinessUtil.BUDGETSDATE));

		try {
			StringBuffer queryString = new StringBuffer();
			if (startDate.before(budgetStartDate)) {
				tableName = "TLFHIST";
			}

			queryString.append("SELECT DISTINCT new org.ace.accounting.dto.VPFTdto(t.eNo) FROM " + tableName
					+ " t WHERE t.settlementDate >= :startDate AND t.settlementDate <= :endDate AND t.reverse=false AND t.paid=true ");
			if (branch != null) {
				queryString.append(" AND t.branch=:branch ");
			}

			if (currency != null) {
				queryString.append(" AND t.currency.id = :currencyId ");
			}

			if (tType.equals(TransactionType.VOC)) {
				queryString.append(" AND t.eNo like 'VOC%' ");
			} else if (tType.equals(TransactionType.T)) {
				queryString.append(" AND t.eNo not like 'VOC%' ");
			}

			if (type.equals(VoucherListingType.CC)) {
				queryString.append(" AND ((t.tranType.status='CCV' AND  t.ccoa.coa NOT IN :coaList) OR (t.tranType.status='CDV' AND  t.ccoa.coa IN :coaList))  ");
			} else if (type.equals(VoucherListingType.T)) {
				queryString.append(" AND t.tranType.status IN ('TCV','TDV') ");
			} else if (type.equals(VoucherListingType.CD)) {
				queryString.append(" AND ((t.tranType.status='CDV' AND  t.ccoa.coa NOT IN :coaList) OR (t.tranType.status='CCV' AND  t.ccoa.coa IN :coaList))  ");
			}

			queryString.append(" ORDER BY t.settlementDate,t.eNo ");

			Query q = em.createQuery(queryString.toString());
			startDate = DateUtils.resetStartDate(startDate);
			q.setParameter("startDate", startDate);
			endDate = DateUtils.resetEndDate(endDate);
			q.setParameter("endDate", endDate);
			if (branch != null) {
				q.setParameter("branch", branch);
			}

			if (currency != null) {
				q.setParameter("currencyId", currency.getId());
			}

			if (type.equals(VoucherListingType.CC) || type.equals(VoucherListingType.CD)) {
				List<ChartOfAccount> coaList = getCashCoaList(currency, branch);
				q.setParameter("coaList", coaList);
			}
			result = q.getResultList();
			em.flush();
		} catch (NoResultException pe) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find VoucherNo", pe);
		}
		return result;

	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<VPdto> findVoucherPrintingByVoucherNo(Date startDate, Date endDate, String fromEno, String toEno, Branch branch) {
		List<VPdto> result = null;
		try {
			StringBuffer queryString = new StringBuffer();
			queryString.append("SELECT NEW org.ace.accounting.dto.VPdto(t.settlementDate,t.eNo,t.ccoa.coa.acCode,t.ccoa.coa.acName,t.narration,t.currency.currencyCode,"
					+ " CASE WHEN (t.tranType.tranCode IN (org.ace.accounting.system.trantype.TranCode.CSDEBIT,org.ace.accounting.system.trantype.TranCode.TRDEBIT)) THEN t.localAmount ELSE 0 END ,"
					+ " CASE WHEN (t.tranType.tranCode IN (org.ace.accounting.system.trantype.TranCode.CSCREDIT,org.ace.accounting.system.trantype.TranCode.TRCREDIT)) THEN t.localAmount ELSE 0 END) FROM TLF t"
					+ " WHERE t.settlementDate >= :startDate AND t.settlementDate <= :endDate AND t.reverse=false AND t.eNo BETWEEN :fromEno AND :toEno AND t.paid=true");
			if (branch != null) {
				queryString.append(" AND t.branch.id= :branchid ");
			}
			queryString.append(" ORDER BY t.eNo ");

			Query q = em.createQuery(queryString.toString());
			startDate = DateUtils.resetStartDate(startDate);
			q.setParameter("startDate", startDate);
			endDate = DateUtils.resetEndDate(endDate);

			q.setParameter("endDate", endDate);
			q.setParameter("fromEno", fromEno);
			q.setParameter("toEno", toEno);
			if (branch != null) {
			q.setParameter("branchid", branch.getId());
			}
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find VoucherNo", pe);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<VPdto> findVoucherPrinting(Date startDate, Date endDate) {
		List<VPdto> result = null;
		try {
			StringBuffer queryString = new StringBuffer();
			queryString.append("SELECT NEW org.ace.accounting.dto.VPdto(t.settlementDate,t.eNo,t.ccoa.coa.acCode,t.ccoa.coa.acName,t.narration,t.currency.currencyCode,"
					+ " CASE WHEN (t.tranType.tranCode IN (org.ace.accounting.system.trantype.TranCode.CSDEBIT,org.ace.accounting.system.trantype.TranCode.TRDEBIT)) THEN t.localAmount ELSE  0  END ,"
					+ " CASE WHEN (t.tranType.tranCode IN (org.ace.accounting.system.trantype.TranCode.CSCREDIT,org.ace.accounting.system.trantype.TranCode.TRCREDIT)) THEN t.localAmount ELSE  0  END) FROM TLF t"
					+ " WHERE t.settlementDate >= :startDate AND t.settlementDate <= :endDate AND t.reverse=false AND t.paid=true ORDER BY t.settlementDate,t.eNo");

			Query q = em.createQuery(queryString.toString());
			startDate = DateUtils.resetStartDate(startDate);
			q.setParameter("startDate", startDate);

			endDate = DateUtils.resetEndDate(endDate);
			q.setParameter("endDate", endDate);

			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find VoucherNo", pe);
		}
		return result;

	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CashBookDTO> findCashBookList(Date startDate, Date endDate) {
		List<CashBookDTO> result = null;
		try {
			StringBuffer queryString = new StringBuffer();
			queryString.append("SELECT NEW org.ace.accounting.dto.VPdto(t.settlementDate,t.eNo,t.ccoa.coa.acCode,t.ccoa.coa.acName,t.narration,t.currency.currencyCode,"
					+ " CASE WHEN (t.tranType.tranCode IN (org.ace.accounting.system.trantype.TranCode.CSDEBIT,org.ace.accounting.system.trantype.TranCode.TRDEBIT)) THEN t.localAmount ELSE  0  END ,"
					+ " CASE WHEN (t.tranType.tranCode IN (org.ace.accounting.system.trantype.TranCode.CSCREDIT,org.ace.accounting.system.trantype.TranCode.TRCREDIT)) THEN t.localAmount ELSE  0  END) FROM TLF t"
					+ " WHERE t.settlementDate >= :startDate AND t.settlementDate <= :endDate AND t.reverse=false AND t.paid=true");

			Query q = em.createQuery(queryString.toString());
			startDate = DateUtils.resetStartDate(startDate);
			q.setParameter("startDate", startDate);

			endDate = DateUtils.resetEndDate(endDate);

			q.setParameter("endDate", endDate);
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find VoucherNo", pe);
		}
		return result;

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<VLdto> findGenLournalListingReport(VoucherListingType type, Date startDate, Date endDate, Branch branch, Currency currency, Boolean homeCurrencyConverted,
			TransactionType tType) {
		List<VLdto> result = null;
		String tableName = "TLF";
		String localAmount = null;
		Date budgetStartDate = DateUtils.formatStringToDate(setupService.findSetupValueByVariable(BusinessUtil.BUDGETSDATE));

		try {

			StringBuffer queryString = null;

			if (startDate.before(budgetStartDate)) {
				tableName = "TLFHIST";
			}

			if (homeCurrencyConverted) {
				localAmount = "homeAmount";
			} else {
				localAmount = "localAmount";
			}

			queryString = new StringBuffer(
					" SELECT NEW org.ace.accounting.dto.VLdto(tlf.settlementDate, tlf.eNo, tlf.ccoa.coa.acCode, tlf.ccoa.coa.acName, tlf.narration,tlf.currency.currencyCode, tlf.rate, "
							+ " CASE WHEN (tlf.tranType.tranCode IN (org.ace.accounting.system.trantype.TranCode.CSDEBIT,org.ace.accounting.system.trantype.TranCode.TRDEBIT)) THEN tlf."
							+ localAmount + " ELSE  0  END ,"
							+ " CASE WHEN (tlf.tranType.tranCode IN (org.ace.accounting.system.trantype.TranCode.CSCREDIT,org.ace.accounting.system.trantype.TranCode.TRCREDIT)) THEN tlf."
							+ localAmount + " ELSE  0  END ) FROM " + tableName
							+ " tlf WHERE tlf.settlementDate >= :startDate AND tlf.settlementDate <= :endDate AND tlf.reverse= false AND tlf.paid=true ");

			if (branch != null) {
				queryString.append(" AND tlf.branch.id = :branchId ");
			}

			if (currency != null) {
				queryString.append(" AND tlf.currency.id = :currencyId ");
			}

			if (tType.equals(TransactionType.VOC)) {
				queryString.append(" AND tlf.eNo like 'VOC%' ");
			} else if (tType.equals(TransactionType.T)) {
				queryString.append(" AND tlf.eNo not like 'VOC%' ");
			}

			if (type.equals(VoucherListingType.CC)) {
				queryString.append(" AND ((tlf.tranType.status='CCV' AND  tlf.ccoa.coa NOT IN :coaList) OR (tlf.tranType.status='CDV' AND  tlf.ccoa.coa IN :coaList))  ");
			} else if (type.equals(VoucherListingType.T)) {
				queryString.append(" AND tlf.tranType.status IN ('TCV','TDV') ");
			} else if (type.equals(VoucherListingType.CD)) {
				queryString.append(" AND ((tlf.tranType.status='CDV' AND  tlf.ccoa.coa NOT IN :coaList) OR (tlf.tranType.status='CCV' AND  tlf.ccoa.coa IN :coaList))  ");
			}

			queryString.append(" ORDER BY tlf.currency.currencyCode, tlf.eNo  ASC ");

			Query q = em.createQuery(queryString.toString());

			startDate = DateUtils.resetStartDate(startDate);
			q.setParameter("startDate", startDate);

			endDate = DateUtils.resetEndDate(endDate);

			q.setParameter("endDate", endDate);

			if (branch != null) {
				q.setParameter("branchId", branch.getId());
			}

			if (currency != null) {
				q.setParameter("currencyId", currency.getId());
			}

			if (type.equals(VoucherListingType.CC) || type.equals(VoucherListingType.CD)) {
				List<ChartOfAccount> coaList = getCashCoaList(currency, branch);
				q.setParameter("coaList", coaList);
			}

			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find General Journal listing report", pe);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<VLdto> findGenJournalListingReportByVoucherNo(VoucherListingType type, Date startDate, Date endDate, String fromEno, String toEno, Branch branch, Currency currency,
			Boolean homeCurrencyConverted, TransactionType tType) {
		List<VLdto> result = null;
		String tableName = "TLF";
		String localAmount = null;
		Date budgetStartDate = DateUtils.formatStringToDate(setupService.findSetupValueByVariable(BusinessUtil.BUDGETSDATE));

		try {

			StringBuffer queryString = null;

			if (startDate.before(budgetStartDate)) {
				tableName = "TLFHIST";
			}

			if (homeCurrencyConverted) {
				localAmount = "homeAmount";
			} else {
				localAmount = "localAmount";
			}

			queryString = new StringBuffer(
					" SELECT NEW org.ace.accounting.dto.VLdto(tlf.settlementDate, tlf.eNo, tlf.ccoa.coa.acCode, tlf.ccoa.coa.acName, tlf.narration,tlf.currency.currencyCode, tlf.rate, "
							+ " CASE WHEN (tlf.tranType.tranCode IN (org.ace.accounting.system.trantype.TranCode.CSDEBIT,org.ace.accounting.system.trantype.TranCode.TRDEBIT)) THEN tlf."
							+ localAmount + " ELSE  0  END ,"
							+ " CASE WHEN (tlf.tranType.tranCode IN (org.ace.accounting.system.trantype.TranCode.CSCREDIT,org.ace.accounting.system.trantype.TranCode.TRCREDIT)) THEN tlf."
							+ localAmount + " ELSE  0  END ) FROM " + tableName
							+ " tlf WHERE tlf.settlementDate >= :startDate AND tlf.settlementDate <= :endDate AND tlf.reverse= false AND tlf.paid=true ");

			if (branch != null) {
				queryString.append(" AND tlf.branch.id = :branchId ");
			}

			if (currency != null) {
				queryString.append(" AND tlf.currency.id = :currencyId ");
			}

			if (tType.equals(TransactionType.VOC)) {
				queryString.append(" AND tlf.eNo like 'VOC%' ");
			} else if (tType.equals(TransactionType.T)) {
				queryString.append(" AND tlf.eNo not like 'VOC%' ");
			}

			if (type.equals(VoucherListingType.CC)) {
				queryString.append(" AND ((tlf.tranType.status='CCV' AND  tlf.ccoa.coa NOT IN :coaList) OR (tlf.tranType.status='CDV' AND  tlf.ccoa.coa IN :coaList))  ");
			} else if (type.equals(VoucherListingType.T)) {
				queryString.append(" AND tlf.tranType.status IN ('TCV','TDV') ");
			} else if (type.equals(VoucherListingType.CD)) {
				queryString.append(" AND ((tlf.tranType.status='CDV' AND  tlf.ccoa.coa NOT IN :coaList) OR (tlf.tranType.status='CCV' AND  tlf.ccoa.coa IN :coaList))  ");
			}

			queryString.append(" AND tlf.eNo BETWEEN :fromEno AND :toEno ORDER BY tlf.currency.currencyCode, tlf.eNo  ASC ");

			Query q = em.createQuery(queryString.toString());

			startDate = DateUtils.resetStartDate(startDate);
			q.setParameter("startDate", startDate);

			endDate = DateUtils.resetEndDate(endDate);

			q.setParameter("endDate", endDate);

			if (branch != null) {
				q.setParameter("branchId", branch.getId());
			}

			if (currency != null) {
				q.setParameter("currencyId", currency.getId());
			}

			if (type.equals(VoucherListingType.CC) || type.equals(VoucherListingType.CD)) {
				List<ChartOfAccount> coaList = getCashCoaList(currency, branch);
				q.setParameter("coaList", coaList);
			}

			q.setParameter("fromEno", fromEno);
			q.setParameter("toEno", toEno);
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find General Journal listing report", pe);
		}
		return result;
	}

}
