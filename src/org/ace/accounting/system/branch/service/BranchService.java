package org.ace.accounting.system.branch.service;

import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.persistence.interfaces.IBranchDAO;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICoaService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.java.component.ErrorCode;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "BranchService")
public class BranchService extends BaseService implements IBranchService {

	@Resource(name = "BranchDAO")
	private IBranchDAO branchDAO;

	@Resource(name = "DataRepService")
	private IDataRepService<ChartOfAccount> coaDataRepService;

	@Resource(name = "DataRepService")
	private IDataRepService<CurrencyChartOfAccount> ccoaDataRepService;

	@Resource(name = "DataRepService")
	private IDataRepService<Branch> branchDataRepService;

	@Resource(name = "BranchService")
	private IBranchService branchService;

	@Resource(name = "CoaService")
	private ICoaService coaService;

	@Resource(name = "CurrencyService")
	private ICurrencyService currencyService;

	public void setBranchDataRepService(IDataRepService<Branch> branchDataRepService) {
		this.branchDataRepService = branchDataRepService;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Branch> findAllBranch() throws SystemException {
		List<Branch> result = null;
		try {
			result = branchDAO.findAll();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of Branch)", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Branch findBranchByBranchCode(String branchCode) throws SystemException {
		Branch result = null;
		try {
			result = branchDAO.findByBranchCode(branchCode);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find a Branch (branchCode : " + branchCode + ")", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void addNewBranch(Branch branch) throws SystemException {
		try {
			branchDataRepService.insert(branch);
			List<ChartOfAccount> coaList = coaService.findAllCoa();
			List<Currency> currencyList = currencyService.findAllCurrency();
			for (ChartOfAccount chartOfAccount : coaList) {
				for (Currency currency : currencyList) {
					CurrencyChartOfAccount ccoa = new CurrencyChartOfAccount();
					ccoa.setCoa(chartOfAccount);
					ccoa.setAcName(chartOfAccount.getAcName());
					ccoa.setCurrency(currency);
					ccoa.setBranch(branch);
					ccoaDataRepService.insert(ccoa);
				}
				coaDataRepService.update(chartOfAccount);
			}
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to add new branch", e);
		} catch (SystemException e) {
			if (e.getErrorCode().equals(ErrorCode.DUPLICATE_KEY_FOUND)) {
				throw new SystemException(ErrorCode.DUPLICATE_BRANCH_CODE, "Branch code must be unique.", e);
			}
			throw e;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateBranch(Branch branch) throws SystemException {
		try {
			branchDataRepService.update(branch);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to update branch", e);
		} catch (SystemException e) {
			if (e.getErrorCode().equals(ErrorCode.DUPLICATE_KEY_FOUND)) {
				throw new SystemException(ErrorCode.DUPLICATE_BRANCH_CODE, "Branch code must be unique.", e);
			}
			throw e;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteBranch(Branch branch) throws SystemException {
		try {
			branchDAO.delete(branch);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to delete branch", e);
		}
	}
}
