/***************************************************************************************
 * @author <<Your Name>>
 * @Date 2013-02-11
 * @Version 1.0
 * @Purpose <<You have to write the comment the main purpose of this class>>
 * 
 *    
 ***************************************************************************************/
package org.ace.accounting.system.currency.service;

import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.common.interfaces.ITranValiDAO;
import org.ace.accounting.dto.MonthlyRateDto;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICoaService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.persistence.interfaces.ICurrencyDAO;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "CurrencyService")
public class CurrencyService extends BaseService implements ICurrencyService {

	@Resource(name = "CurrencyDAO")
	private ICurrencyDAO currencyDAO;

	@Resource(name = "DataRepService")
	private IDataRepService<ChartOfAccount> coaDataRepService;

	@Resource(name = "DataRepService")
	private IDataRepService<CurrencyChartOfAccount> ccoaDataRepService;

	@Resource(name = "DataRepService")
	private IDataRepService<Currency> currencyDataRepService;

	@Resource(name = "BranchService")
	private IBranchService branchService;

	@Resource(name = "CoaService")
	private ICoaService coaService;

	@Resource(name = "TranValiDAO")
	ITranValiDAO tranValiDAO;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Currency> findAllCurrency() {
		List<Currency> result = null;
		try {
			result = currencyDAO.findAll();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of Currency)", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Currency findHomeCurrency() {
		Currency result = null;
		try {
			result = currencyDAO.findHomeCurrency();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find home Currency)", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<Currency> findForeignCurrency() {
		List<Currency> result = null;
		try {
			result = currencyDAO.findForeignCurrency();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of Currency)", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<MonthlyRateDto> findForeignCurrencyDto() {
		List<MonthlyRateDto> result = null;
		try {
			result = currencyDAO.findForeignCurrencyDto();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of Currency)", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Currency findCurrencyByCurrencyCode(String currrencyCode) {
		Currency result = null;
		try {
			result = currencyDAO.findCurrencyByCurrencyCode(currrencyCode);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find a Currency (currrencyCode : " + currrencyCode + ")", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void addNewCurrency(Currency currency) {
		try {
			currencyDataRepService.insert(currency);
			List<ChartOfAccount> coaList = coaService.findAllCoa();
			List<Branch> branchList = branchService.findAllBranch();
			for (ChartOfAccount chartOfAccount : coaList) {
				for (Branch branch : branchList) {
					CurrencyChartOfAccount ccoa = new CurrencyChartOfAccount();
					ccoa.setCoa(chartOfAccount);
					ccoa.setAcName(chartOfAccount.getAcName());
					ccoa.setBranch(branch);
					ccoa.setCurrency(currency);
					ccoaDataRepService.insert(ccoa);
				}
			}

		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to add a new currency", e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateCurrency(Currency currency) {
		try {
			currencyDataRepService.update(currency);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to update currency", e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateAllCurrency(List<Currency> currencyList) {
		try {
			for (Currency currency : currencyList) {
				currencyDataRepService.update(currency);
			}
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to update multiple currency", e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateAllMonthlyRate(List<MonthlyRateDto> currencyList) {
		try {
			for (MonthlyRateDto currency : currencyList) {
				currencyDAO.updateMonthlyRate(currency);
			}
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to update multiple currency", e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteCurrency(Currency currency) {
		try {
			currencyDAO.delete(currency);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to delete currency", e);
		}
	}
}
