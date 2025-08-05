package org.ace.accounting.system.chartaccount.service;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

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
import org.ace.accounting.system.chartaccount.persistence.interfaces.ICCOA_HistoryDAO;
import org.ace.accounting.system.chartaccount.persistence.interfaces.ICcoaDAO;
import org.ace.accounting.system.chartaccount.persistence.interfaces.ICoaDAO;
import org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.department.Department;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "CcoaService")
public class CcoaService extends BaseService implements ICcoaService {
	@Resource(name = "CcoaDAO")
	private ICcoaDAO ccoaDAO;

	@Resource(name = "CoaDAO")
	private ICoaDAO coaDAO;

	@Resource(name = "CCOA_HistoryDAO")
	private ICCOA_HistoryDAO ccoaHistoryDAO;

	@Resource(name = "DataRepService")
	private IDataRepService<CurrencyChartOfAccount> ccoaDataRepService;

	@Transactional(propagation = Propagation.REQUIRED)
	public List<CurrencyChartOfAccount> findAll() {
		List<CurrencyChartOfAccount> list = null;
		try {
			list = ccoaDAO.findAll();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of CCOA.", e);
		}

		return list;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<CurrencyChartOfAccount> findCCOAByCOAid(String coaid) {
		List<CurrencyChartOfAccount> list = null;
		try {
			list = ccoaDAO.findCCOAByCOAid(coaid);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of CCOA.", e);
		}

		return list;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public CurrencyChartOfAccount findSpecificCCOA(String coaId, String currencyId, String branchId, String optionalDepartmentId) {
		CurrencyChartOfAccount result = null;
		try {
			result = ccoaDAO.findSpecificCCOA(coaId, currencyId, branchId, optionalDepartmentId);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find specific CCOA.", e);
		}

		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<String> findAllBudgetYear() {
		List<String> list = null;
		try {
			list = ccoaDAO.findAllBudgetYear();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all Budget Year.", e);
		}
		return list;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<CcoaDto> findAllCcoaDtos() {
		List<CcoaDto> result = null;
		try {
			result = ccoaDAO.findAllCcoaDtos();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of CcoaDtos)", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<ObalDto> findOpeningBalance(ObalCriteriaDto dto) {
		List<ObalDto> dtos = null;
		try {
			dtos = ccoaDAO.findObal(dto);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find opening balance", e);
		}
		return dtos;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<CurrencyChartOfAccount> findBudgetFigure(String branchId) {
		List<CurrencyChartOfAccount> ccoa = null;
		try {
			ccoa = ccoaDAO.findBudgetFigure(branchId);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find Yearly Budget Figure.", e);
		}
		return ccoa;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateObalByDtos(List<ObalDto> dtoList) {
		try {
			ccoaDAO.updateCcoaByObalDtos(dtoList);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to update opening balance", e);
		}
	}

	/*
	 * Enquiry Account Ledger Information
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<EnquiryLedgerDto> findAllEnquiryLedger(EnquiryLedgerDto dto) {
		List<EnquiryLedgerDto> list = null;
		try {
			list = ccoaDAO.findAllEnquiryLedger(dto);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find Enquiry Ledger.)", e);
		}

		return list;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<YearlyBudgetDto> findAllYearlyBudget(YearlyBudgetDto dto) {
		List<YearlyBudgetDto> result = null;
		try {
			result = ccoaDAO.findAllYearlyBudget(dto);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find All Year Budget ", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateYearlyBudget(List<YearlyBudgetDto> dtoList) {
		try {
			for (YearlyBudgetDto dto : dtoList) {
				ccoaDAO.updateYearlyBudget(dto);
			}
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to update Yearly Budget.", e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<MonthlyBudgetDto> findAllMonthlyBudget(MonthlyBudgetDto dto) {
		List<MonthlyBudgetDto> list = null;
		try {
			list = ccoaDAO.findAllMonthlyBudget(dto);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all Monthly Budget.", e);
		}
		return list;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateMonthlyBudget(List<MonthlyBudgetDto> dtoList) {
		try {
			ccoaDAO.updateMonthlyBudget(dtoList);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to update Monthly Budget.", e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<MonthlyBudgetHomeCurDto> findAllMonthlyBudgetHomeCur(MonthlyBudgetHomeCurDto dto) {
		List<MonthlyBudgetHomeCurDto> list = null;
		try {
			list = ccoaDAO.findAllMonthlyBudgetHomeCur(dto);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all Monthly Budget Home Currency.", e);
		}
		return list;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateMonthlyBudgetHomeCur(List<MonthlyBudgetHomeCurDto> dtoList) {
		try {
			ccoaDAO.updateMonthlyBudgetHomeCur(dtoList);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to update Monthly Budget Home Currency.", e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<CurrencyChartOfAccount> findCOAByBranchID(String branchID) {
		List<CurrencyChartOfAccount> list = null;
		try {
			list = ccoaDAO.findCOAByBranchID(branchID);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find COA By Branch ID.", e);
		}
		return list;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public BigDecimal finddblBalance(StringBuffer sf, ChartOfAccount coa, String budgetYear, Currency currency, Branch branch) {
		BigDecimal result = BigDecimal.ZERO;
		try {
			result = ccoaDAO.finddblBalance(sf, coa, budgetYear, currency, branch);
			if (result == null || result.equals(BigDecimal.ZERO)) {
				sf = new StringBuffer(sf.toString().replace("FROM CurrencyChartOfAccount", "FROM CcoaHistory"));
				result = ccoaHistoryDAO.finddblBalance(sf, coa, budgetYear, currency, branch);
			}
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find COA By Branch ID.", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<CCOADialogDTO> findAllCCOADialogDTO(Currency currency, Branch branch) {
		List<CCOADialogDTO> results = null;
		try {
			results = ccoaDAO.findAllCCOADialogDTO(currency, branch);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find COA By Branch ID.", e);
		}
		return results;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<CurrencyChartOfAccount> findCCOAByCurrencyID(String currencyID) {
		List<CurrencyChartOfAccount> list = null;
		try {
			list = ccoaDAO.findCOAByCurrencyID(currencyID);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of CCOA", e);
		}

		return list;
	}

	public List<CurrencyChartOfAccount> findCCOAByBranchID(String branchID) {
		List<CurrencyChartOfAccount> list = null;
		try {
			list = ccoaDAO.findCCOAByBranchID(branchID);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of CCOA", e);
		}

		return list;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateCCOAByAllBranch(CurrencyChartOfAccount ccoa) {
		try {
			Department departmentId = null;
			if (ccoa.getDepartment() != null)
				departmentId = ccoa.getDepartment();
			ccoaDAO.updateByBranch(ccoa.getAcName(),ccoa.getCoa().getAcCode(), ccoa.getBranch().getBranchCode(),departmentId);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to update a ", e);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CurrencyChartOfAccount> findCCOAforBranchClosing(String branchId)throws SystemException{
		try {
			return ccoaDAO.findCCOAforBranchClosing(branchId);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Fail to find CCOA");
		}
	}
	

}