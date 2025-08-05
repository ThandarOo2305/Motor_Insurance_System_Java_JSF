package org.ace.accounting.report.reportStatement.persistence.interfaces;

import java.util.Date;
import java.util.List;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.dto.ReportStatementDto;
import org.ace.accounting.report.ReportType;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;
import org.ace.java.component.persistence.exception.DAOException;

/**
 * ... for report statement? don't ask me the purpose...
 * 
 * @author TOK
 * @since 1.0.0
 * @date 2016/05/19
 */

public interface IReportStatementDAO {

	/**
	 * 
	 * @param boolean
	 *            isObal
	 * @param ReportType
	 *            reportType
	 * @param CurrencyType
	 *            currencyType
	 * @param Currency
	 *            currency
	 * @param Branch
	 *            branch
	 * @param Date
	 *            reportDate
	 * @param String
	 *            formatType
	 * @return {@link List} of {@link ReportStatementDto} instance
	 * @throws DAOException
	 *             An exception occurs during the DB operation
	 */
	public List<ReportStatementDto> previewProcedure(boolean isObal, ReportType reportType, CurrencyType currencyType, Currency currency, Branch branch, Date reportDate,
			String formatType) throws DAOException, Exception;

	public List<ReportStatementDto> prevPreviewProcedure(boolean isObal, ReportType reportType, CurrencyType currencyType, Currency currency, Branch branch, Date reportDate,
			String budgetYear, String formatType) throws DAOException, Exception;

	public List<ReportStatementDto> previewProcedureForCloneData(boolean isObal, ReportType reportType, CurrencyType currencyType, Currency currency, Branch branch,
			Date reportDate, String budgetYear, String formatType) throws DAOException, Exception;
}
