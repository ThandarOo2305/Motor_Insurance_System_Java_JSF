package org.ace.accounting.report.reportStatement.service.interfaces;

import java.util.Date;
import java.util.List;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.dto.LiabilitiesACDto;
import org.ace.accounting.dto.ReportStatementDto;
import org.ace.accounting.report.ReportType;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;
import org.ace.java.component.SystemException;

/**
 * ...
 * 
 * @author TOK
 * @since 1.0.0
 * @date 2016/05/19
 */
public interface IReportStatementService {
	public List<ReportStatementDto> previewProcedure(boolean isObal, ReportType reportType, CurrencyType currencyType, Currency currency, Branch branch, Date reportDate,
			String formatType) throws SystemException, Exception;

	public List<ReportStatementDto> prevPreviewProcedure(boolean isObal, ReportType reportType, CurrencyType currencyType, Currency currency, Branch branch, Date reportDate,
			String budgetYear, String formatType) throws SystemException, Exception;

	public List<ReportStatementDto> previewProcedureForCloneData(boolean isObal, ReportType reportType, CurrencyType currencyType, Currency currency, Branch branch,
			Date reportDate, String budgetYear, String formatType) throws SystemException, Exception;

	public List<ReportStatementDto> liabilitiesAcodeCheck(LiabilitiesACDto plDto, LiabilitiesACDto taxDto, List<ReportStatementDto> dtoList) throws SystemException;

}
