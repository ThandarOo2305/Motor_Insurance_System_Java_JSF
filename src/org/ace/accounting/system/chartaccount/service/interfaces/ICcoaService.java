package org.ace.accounting.system.chartaccount.service.interfaces;

import java.math.BigDecimal;
import java.util.List;

import org.ace.accounting.dto.CCOADialogDTO;
import org.ace.accounting.dto.CcoaDto;
import org.ace.accounting.dto.EnquiryLedgerDto;
import org.ace.accounting.dto.MonthlyBudgetDto;
import org.ace.accounting.dto.MonthlyBudgetHomeCurDto;
import org.ace.accounting.dto.ObalCriteriaDto;
import org.ace.accounting.dto.ObalDto;
import org.ace.accounting.dto.YearlyBudgetDto;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.currency.Currency;

public interface ICcoaService {
	public List<CurrencyChartOfAccount> findAll();

	public List<CurrencyChartOfAccount> findCCOAByCOAid(String coaid);

	public CurrencyChartOfAccount findSpecificCCOA(String coaId, String currencyId, String branchId, String optionalDepartmentId);

	public List<String> findAllBudgetYear();

	public void updateYearlyBudget(List<YearlyBudgetDto> yearlyBudgetList);

	public List<CcoaDto> findAllCcoaDtos();

	public List<ObalDto> findOpeningBalance(ObalCriteriaDto dto);

	public List<CurrencyChartOfAccount> findBudgetFigure(String branchId);

	public void updateObalByDtos(List<ObalDto> dtoList);

	/*
	 * Enquiry Account Ledger Information
	 */
	public List<EnquiryLedgerDto> findAllEnquiryLedger(EnquiryLedgerDto dto);

	public List<MonthlyBudgetDto> findAllMonthlyBudget(MonthlyBudgetDto dto);

	public void updateMonthlyBudget(List<MonthlyBudgetDto> dtoList);

	public List<MonthlyBudgetHomeCurDto> findAllMonthlyBudgetHomeCur(MonthlyBudgetHomeCurDto dto);

	public void updateMonthlyBudgetHomeCur(List<MonthlyBudgetHomeCurDto> dtoList);

	public List<CurrencyChartOfAccount> findCOAByBranchID(String branchID);

	/***
	 * Account Ledger Listing and General Ledger Listing
	 */
	public BigDecimal finddblBalance(StringBuffer sf, ChartOfAccount coa, String budgetYear, Currency currency, Branch branch);

	/**
	 * The method to retrieve the list of CurrencyChartOfAccount.
	 * 
	 * @param String
	 *            branchId
	 * @return {@link List} of {@link CCOADialogDTO} instances
	 */
	public List<CCOADialogDTO> findAllCCOADialogDTO(Currency currency, Branch branch);

	public List<CurrencyChartOfAccount> findCCOAByCurrencyID(String currencyID);

	public List<CurrencyChartOfAccount> findCCOAByBranchID(String branchID);

	public List<YearlyBudgetDto> findAllYearlyBudget(YearlyBudgetDto dto);
	
	public List<CurrencyChartOfAccount> findCCOAforBranchClosing(String branchId);
	
	void updateCCOAByAllBranch(CurrencyChartOfAccount ccoa);
}
