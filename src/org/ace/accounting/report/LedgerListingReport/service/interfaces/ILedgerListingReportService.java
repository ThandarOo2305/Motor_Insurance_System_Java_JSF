package org.ace.accounting.report.LedgerListingReport.service.interfaces;

import java.util.List;

import org.ace.accounting.dto.AccountLedgerDto;
import org.ace.accounting.dto.GenLedgerDto;
import org.ace.accounting.dto.LedgerDto;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.java.component.SystemException;

public interface ILedgerListingReportService {
	/***
	 * Account Ledger Listing and General Ledger Listing
	 * 
	 * @param coaList
	 */
	public List<AccountLedgerDto> findAccLedgerListing(LedgerDto ledgerDto, List<ChartOfAccount> coaList) throws SystemException;

	public List<GenLedgerDto> findGenLedgerListing(LedgerDto ledgerDto, ChartOfAccount coa) throws SystemException;

	public List<GenLedgerDto> findGenLedgerListingByClone(LedgerDto ledgerDto, ChartOfAccount coa) throws SystemException;

	public List<AccountLedgerDto> findAccLedgerListingByClone(LedgerDto ledgerDto, List<ChartOfAccount> coaList) throws SystemException;
}
