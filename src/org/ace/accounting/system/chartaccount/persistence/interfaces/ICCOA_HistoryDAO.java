package org.ace.accounting.system.chartaccount.persistence.interfaces;

import java.math.BigDecimal;
import java.util.List;

import org.ace.accounting.dto.AccountLedgerDto;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.currency.Currency;
import org.ace.java.component.persistence.exception.DAOException;

public interface ICCOA_HistoryDAO {
	public List<CurrencyChartOfAccount> findAll() throws DAOException;
	public BigDecimal finddblBalance(StringBuffer sf)throws DAOException;
	public List<AccountLedgerDto> findDebitCreditBy(StringBuffer sf, Branch branch,Currency currency)throws DAOException;
	public BigDecimal finddblBalance(StringBuffer sf, ChartOfAccount coa, String budgetYear,Currency currency, Branch branch)throws DAOException;
}
