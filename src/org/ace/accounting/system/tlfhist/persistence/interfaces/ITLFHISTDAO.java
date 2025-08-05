package org.ace.accounting.system.tlfhist.persistence.interfaces;

import java.util.Date;
import java.util.List;

import org.ace.accounting.dto.AccountLedgerDto;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.tlf.TLF;
import org.ace.accounting.system.tlfhist.TLFHIST;
import org.ace.java.component.persistence.exception.DAOException;

public interface ITLFHISTDAO {
	public List<TLFHIST> findAll() throws DAOException;
	public List<TLF> findVoucherListByReverseZero(String voucherNo)throws DAOException;
	/***
	 * Account Ledger Listing and General Ledger Listing
	 */
	public List<AccountLedgerDto> findDebitCreditBy(StringBuffer sf,ChartOfAccount coa, Date sDate)throws DAOException;
	public List<AccountLedgerDto> findDebitCreditBy(StringBuffer sf,ChartOfAccount coa, Date startDate, Date endDate)throws DAOException;
}
