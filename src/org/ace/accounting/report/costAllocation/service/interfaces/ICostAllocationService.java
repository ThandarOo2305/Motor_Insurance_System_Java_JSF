package org.ace.accounting.report.costAllocation.service.interfaces;

import java.util.List;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.dto.CostAllocationReportDto;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;

public interface ICostAllocationService {
	public List<CostAllocationReportDto> findCostAllocationReport(CurrencyType currencyType, Branch branch, Currency currency, String allocateYear, String allocateMonth,
			boolean currencyConverted);

	public void allocateProcess(CurrencyType currencyType, Branch branch, Currency currency, String allocateYear, String allocateMonth, boolean currencyConverted);
}
