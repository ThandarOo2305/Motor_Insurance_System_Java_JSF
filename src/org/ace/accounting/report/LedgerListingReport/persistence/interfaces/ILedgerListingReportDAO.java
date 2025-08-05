package org.ace.accounting.report.LedgerListingReport.persistence.interfaces;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.ace.accounting.dto.AccountLedgerDto;
import org.ace.accounting.dto.GLDBDto;
import org.ace.accounting.dto.LedgerDto;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.currency.Currency;
import org.ace.java.component.persistence.exception.DAOException;

public interface ILedgerListingReportDAO {
	/***
	 * Account Ledger Listing and General Ledger Listing
	 */

	public BigDecimal finddblBalance(StringBuffer sf, ChartOfAccount coa, String budgetYear, Currency currency, Branch branch) throws DAOException;

	public List<AccountLedgerDto> findDebitCreditByStartDate(StringBuffer sf, ChartOfAccount coa, Date startDate, Date endDate) throws DAOException;

	public List<AccountLedgerDto> findDebitCreditBySDateAndEDate(StringBuffer sf, ChartOfAccount coa, Date startDate, Date endDate) throws DAOException;

	List<GLDBDto> findGeneralLedgerList(LedgerDto ledgerDto, ChartOfAccount coa, Date startDate, Date endDate) throws DAOException;

	List<GLDBDto> findGeneralLedgerListByClone(LedgerDto ledgerDto, ChartOfAccount coa, Date startDate, Date endDate) throws DAOException;

}
