package org.ace.accounting.report.LedgerListingReport.persistence;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.dto.AccountLedgerDto;
import org.ace.accounting.dto.GLDBDto;
import org.ace.accounting.dto.LedgerDto;
import org.ace.accounting.report.LedgerListingReport.persistence.interfaces.ILedgerListingReportDAO;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.currency.Currency;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("LedgerListingReportDAO")
public class LedgerListingReportDAO extends BasicDAO implements ILedgerListingReportDAO {

	@Transactional(propagation = Propagation.REQUIRED)
	public BigDecimal finddblBalance(StringBuffer sf, ChartOfAccount coa, String budgetYear, Currency currency, Branch branch) throws DAOException {
		BigDecimal result = null;
		try {
			Query query = em.createQuery(sf.toString());
			query.setParameter("budgetYear", budgetYear);

			if (coa != null) {
				query.setParameter("coa", coa);
			}
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

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AccountLedgerDto> findDebitCreditByStartDate(StringBuffer sf, ChartOfAccount coa, Date startDate, Date endDate) throws DAOException {
		List<AccountLedgerDto> result = null;
		try {
			Query query = em.createQuery(sf.toString());
			if (coa != null) {
				query.setParameter("coa", coa);
			}
			startDate = DateUtils.resetStartDate(startDate);
			query.setParameter("startDate", startDate);
			endDate = DateUtils.resetEndDate(endDate);
			query.setParameter("endDate", endDate);
			result = query.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find DebitCredit By ChartOfAccount", pe);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AccountLedgerDto> findDebitCreditBySDateAndEDate(StringBuffer sf, ChartOfAccount coa, Date startDate, Date endDate) throws DAOException {
		List<AccountLedgerDto> result = null;
		try {
			Query query = em.createQuery(sf.toString());
			if (coa != null) {
				query.setParameter("coa", coa);
			}
			startDate = DateUtils.resetStartDate(startDate);
			query.setParameter("startDate", startDate);
			endDate = DateUtils.resetEndDate(endDate);
			query.setParameter("endDate", endDate);
			result = query.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find DebitCredit By ChartOfAccount", pe);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GLDBDto> findGeneralLedgerList(LedgerDto ledgerDto, ChartOfAccount coa, Date startDate, Date endDate) throws DAOException {
		List<GLDBDto> result = null;
		try {
			String amount = "";
			if (ledgerDto.isHomeCurrencyConverted() || CurrencyType.HOMECURRENCY.equals(ledgerDto.getCurrencyType())) {
				amount = "homeAmount";
			} else if (CurrencyType.SOURCECURRENCY.equals(ledgerDto.getCurrencyType())) {
				amount = "localAmount";
			}

			StringBuffer sf = new StringBuffer();
			// sf.append("SELECT NEW
			// org.ace.accounting.dto.GLDBDto(cast(t.settlementDate as date),");
			sf.append("SELECT  NEW org.ace.accounting.dto.GLDBDto(t.settlementDate,");
			sf.append("	SUM ( CASE WHEN t.status IN ('TDV','CDV') THEN  ");
			sf.append("		t." + amount + " ELSE 0 END ");
			sf.append("	), ");
			sf.append("	SUM ( CASE WHEN t.status IN ('TCV','CCV') THEN  ");
			sf.append("		t." + amount + " ELSE 0 END ");
			sf.append("	) )");
			sf.append(" FROM VwTLF t INNER JOIN ");
			/* sf.append("	 VwCcoa cc ON t.ccoa.id = cc.id LEFT JOIN "); */
			sf.append("	 CurrencyChartOfAccount c ON t.ccoa.id = c.id ");
			if (ledgerDto.isGroupAccount()) {
				sf.append(" INNER JOIN ChartOfAccount cg on cg.id = c.coa.groupId ");
			}
			// sf.append(" WHERE (t.reverse = 0) AND t.settlementDate >=
			// :startDate AND t.settlementDate <= :endDate ");
			sf.append(" WHERE (t.reverse = 0) AND t.settlementDate BETWEEN :startDate AND :endDate ");

			if (CurrencyType.SOURCECURRENCY.equals(ledgerDto.getCurrencyType()) || ledgerDto.getCurrency() != null) {
				sf.append(" AND t.currency.id = :currencyId ");
			}
			if (!ledgerDto.isAllBranch() || ledgerDto.getBranch() != null) {
				sf.append(" AND t.branch.id = :branchId ");
			}

			if (ledgerDto.isGroupAccount()) {
				sf.append(" AND cg.id=:groupid ");
				// sf.append(" GROUP BY cg.acCode, cast(t.settlementDate as
				// date)");
				sf.append(" GROUP BY cg.acCode, t.settlementDate");
			} else {
				sf.append(" AND c.coa.id=:coaid ");
				// sf.append(" GROUP BY c.coa.acCode, cast(t.settlementDate as
				// date)");
				sf.append(" GROUP BY c.coa.acCode, t.settlementDate");
			}

			Query query = em.createQuery(sf.toString());
			if (ledgerDto.isGroupAccount()) {
				query.setParameter("groupid", coa.getGroupId());
			} else {
				query.setParameter("coaid", coa.getId());
			}
			// query.setParameter("startDate",
			// DateUtils.resetStartDate(startDate));
			// query.setParameter("endDate", DateUtils.resetEndDate(endDate));
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);

			if (CurrencyType.SOURCECURRENCY.equals(ledgerDto.getCurrencyType()) || ledgerDto.getCurrency() != null) {
				query.setParameter("currencyId", ledgerDto.getCurrency().getId());
			}
			if (!ledgerDto.isAllBranch() || ledgerDto.getBranch() != null) {
				query.setParameter("branchId", ledgerDto.getBranch().getId());
			}

			result = query.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find unposted list By ChartOfAccount", pe);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GLDBDto> findGeneralLedgerListByClone(LedgerDto ledgerDto, ChartOfAccount coa, Date startDate, Date endDate) throws DAOException {
		List<GLDBDto> result = null;
		try {
			String amount = "";
			if (ledgerDto.isHomeCurrencyConverted() || CurrencyType.HOMECURRENCY.equals(ledgerDto.getCurrencyType())) {
				amount = "homeAmount";
			} else if (CurrencyType.SOURCECURRENCY.equals(ledgerDto.getCurrencyType())) {
				amount = "localAmount";
			}

			StringBuffer sf = new StringBuffer();
			// sf.append("SELECT NEW
			// org.ace.accounting.dto.GLDBDto(cast(t.settlementDate as date),");
			sf.append("SELECT  NEW org.ace.accounting.dto.GLDBDto(t.settlementDate,");
			sf.append("	SUM ( CASE WHEN t.status IN ('TDV','CDV') THEN  ");
			sf.append("		t." + amount + " ELSE 0 END ");
			sf.append("	), ");
			sf.append("	SUM ( CASE WHEN t.status IN ('TCV','CCV') THEN  ");
			sf.append("		t." + amount + " ELSE 0 END ");
			sf.append("	) )");
			sf.append(" FROM VwTLFClone t INNER JOIN ");
			/* sf.append("	 VwCcoa cc ON t.ccoa.id = cc.id LEFT JOIN "); */
			sf.append("	 CcoaClone c ON t.ccoa.id = c.id ");
			if (ledgerDto.isGroupAccount()) {
				sf.append(" INNER JOIN ChartOfAccount cg on cg.id = c.coa.groupId ");
			}
			// sf.append(" WHERE (t.reverse = 0) AND t.settlementDate >=
			// :startDate AND t.settlementDate <= :endDate ");
			sf.append(" WHERE (t.reverse = 0) AND t.settlementDate BETWEEN :startDate AND :endDate ");

			if (CurrencyType.SOURCECURRENCY.equals(ledgerDto.getCurrencyType()) || ledgerDto.getCurrency() != null) {
				sf.append(" AND t.currency.id = :currencyId ");
			}
			if (!ledgerDto.isAllBranch() || ledgerDto.getBranch() != null) {
				sf.append(" AND t.branch.id = :branchId ");
			}

			if (ledgerDto.isGroupAccount()) {
				sf.append(" AND cg.id=:groupid ");
				// sf.append(" GROUP BY cg.acCode, cast(t.settlementDate as
				// date)");
				sf.append(" GROUP BY cg.acCode, t.settlementDate");
			} else {
				sf.append(" AND c.coa.id=:coaid ");
				// sf.append(" GROUP BY c.coa.acCode, cast(t.settlementDate as
				// date)");
				sf.append(" GROUP BY c.coa.acCode, t.settlementDate");
			}

			Query query = em.createQuery(sf.toString());
			if (ledgerDto.isGroupAccount()) {
				query.setParameter("groupid", coa.getGroupId());
			} else {
				query.setParameter("coaid", coa.getId());
			}
			// query.setParameter("startDate",
			// DateUtils.resetStartDate(startDate));
			// query.setParameter("endDate", DateUtils.resetEndDate(endDate));
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);

			if (CurrencyType.SOURCECURRENCY.equals(ledgerDto.getCurrencyType()) || ledgerDto.getCurrency() != null) {
				query.setParameter("currencyId", ledgerDto.getCurrency().getId());
			}
			if (!ledgerDto.isAllBranch() || ledgerDto.getBranch() != null) {
				query.setParameter("branchId", ledgerDto.getBranch().getId());
			}

			result = query.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find unposted list By ChartOfAccount", pe);
		}
		return result;
	}
}
