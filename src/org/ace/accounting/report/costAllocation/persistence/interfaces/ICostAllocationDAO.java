package org.ace.accounting.report.costAllocation.persistence.interfaces;

import java.util.List;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.dto.AllocateByDeptDto;
import org.ace.accounting.dto.AllocateProcessByDeptDto;
import org.ace.accounting.dto.AllocateProcessDto;
import org.ace.accounting.dto.CostAllocationReportDto;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;

public interface ICostAllocationDAO {
	public List<CostAllocationReportDto> findCostAllocationReport(CurrencyType currencyType, Branch branch, Currency currency, String allocateYear, String allocateMonth,
			boolean currencyConverted);

	public List<AllocateProcessDto> findAllocateProcess(CurrencyType currencyType, Branch branch, Currency currency, String allocateYear, String allocateMonth,
			boolean currencyConverted);

	public List<AllocateProcessByDeptDto> findAllocateProcessByDept(CurrencyType currencyType, Branch branch, Currency currency, String allocateYear, String allocateMonth,
			boolean currencyConverted);

	public List<AllocateByDeptDto> getAcCodeByDept(String hacCode);
}
