package org.ace.accounting.system.chartaccount.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.common.interfaces.ITranValiDAO;
import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.dto.CoaDialogCriteriaDto;
import org.ace.accounting.dto.GenLedgerCriteria;
import org.ace.accounting.dto.GenLedgerSummaryDTO;
import org.ace.accounting.dto.LiabilitiesACDto;
import org.ace.accounting.dto.YPDto;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.chartaccount.AccountType;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.chartaccount.persistence.interfaces.ICoaDAO;
import org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
import org.ace.accounting.system.chartaccount.service.interfaces.ICoaService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "CoaService")
public class CoaService extends BaseService implements ICoaService {

	@Resource(name = "CoaDAO")
	private ICoaDAO coaDAO;

	@Resource(name = "DataRepService")
	private IDataRepService<ChartOfAccount> coaDataRepService;

	@Resource(name = "DataRepService")
	private IDataRepService<CurrencyChartOfAccount> ccoaDataRepService;

	@Resource(name = "CurrencyService")
	private ICurrencyService currencyService;

	@Resource(name = "CcoaService")
	private ICcoaService ccoaService;

	@Resource(name = "BranchService")
	private IBranchService branchService;

	@Resource(name = "TranValiDAO")
	ITranValiDAO tranValiDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ChartOfAccount> findAllCoa() {
		List<ChartOfAccount> result = null;
		try {
			result = coaDAO.findAll();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of ChartOfAccount)", e);
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ChartOfAccount> findAllCoaByAccountCodeType() {
		List<ChartOfAccount> result = null;
		try {
			result = coaDAO.findAllCOAByAccountCodeType();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of ChartOfAccount)", e);
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<String> findAllAcCode() {
		List<String> result = null;
		try {
			result = coaDAO.findAllAcCode();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all acCodes of ChartOfAccount)", e);
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ChartOfAccount findCoaByAcCode(String acCode) {
		ChartOfAccount chartOfAccount = null;
		try {
			chartOfAccount = coaDAO.findByAcCode(acCode);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find COA By AcCode.)", e);
		}
		return chartOfAccount;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ChartOfAccount findCoaByAcCode(String acCode, AccountType acType) {
		ChartOfAccount chartOfAccount = null;
		try {
			chartOfAccount = coaDAO.findByAcCode(acCode, acType);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find COA By AcCode.)", e);
		}
		return chartOfAccount;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ChartOfAccount> findCoaByIE() {
		List<ChartOfAccount> result = null;
		try {
			result = coaDAO.findCoaByIE();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find COA By IE.)", e);
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ChartOfAccount findCoaByibsbAcCode(String ibsbACode) {
		ChartOfAccount chartOfAccount = null;
		try {
			chartOfAccount = coaDAO.findByIbsbACode(ibsbACode);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find COA By IBSBCode.)", e);
		}
		return chartOfAccount;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addNewCoa(ChartOfAccount chartOfAccount) {
		try {
			Calendar cal = Calendar.getInstance();
			chartOfAccount.setpDate(cal.getTime());
			String budInfo = BusinessUtil.getBudgetInfo(chartOfAccount.getpDate(), 2);
			List<Currency> curList = currencyService.findAllCurrency();
			List<Branch> branchList = branchService.findAllBranch();
			coaDataRepService.insert(chartOfAccount);

			CurrencyChartOfAccount ccoa = null;
			for (Currency currency : curList) {
				for (Branch branch : branchList) {
					ccoa = new CurrencyChartOfAccount();
					ccoa.setCoa(chartOfAccount);
					ccoa.setAcName(chartOfAccount.getAcName());
					ccoa.setBranch(branch);
					ccoa.setCurrency(currency);
					ccoa.setBudget(budInfo);
					ccoaDataRepService.insert(ccoa);
				}
			}

		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to add a Chart Of Account", e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ChartOfAccount updateChartOfAccount(ChartOfAccount chartOfAccount) {
		try {
			for (CurrencyChartOfAccount ccoa : ccoaService.findCCOAByCOAid(chartOfAccount.getId())) {
				ccoa.setAcName(chartOfAccount.getAcName());
				ccoaDataRepService.update(ccoa);
			}
			coaDataRepService.update(chartOfAccount);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to update ChartOfAccount", e);
		}
		return chartOfAccount;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteChartOfAccount(ChartOfAccount chartOfAccount) {
		try {
			for (CurrencyChartOfAccount ccoa : ccoaService.findCCOAByCOAid(chartOfAccount.getId())) {
				ccoaDataRepService.delete(ccoa);
			}
			coaDataRepService.delete(chartOfAccount);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to delete ChartOfAccount", e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ChartOfAccount> findAllHeadAccount() {
		List<ChartOfAccount> result = null;
		try {
			result = coaDAO.findAllHeadAccount();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all head accounts.", e);
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ChartOfAccount> findAllGroupAccount() {
		List<ChartOfAccount> result = null;
		try {
			result = coaDAO.findAllGroupAccount();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all group accounts.", e);
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ChartOfAccount findCoaByHeadId(String headId) {
		ChartOfAccount chartOfAccount = null;
		try {
			chartOfAccount = coaDAO.findCoaByHeadId(headId);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find COA By IBSBCode.)", e);
		}
		return chartOfAccount;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ChartOfAccount findCoaByGroupId(String groupId) {
		ChartOfAccount chartOfAccount = null;
		try {
			chartOfAccount = coaDAO.findCoaByGroupId(groupId);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find COA By IBSBCode.)", e);
		}
		return chartOfAccount;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateGroupAccount(ChartOfAccount chartOfAccount, String groupId) {
		try {

			coaDAO.updateGroupAccount(chartOfAccount, groupId);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to update ChartOfAccount", e);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<YPDto> findDtosForYearlyPosting() {
		List<YPDto> result = new ArrayList<>();
		try {
			result = coaDAO.findDtosForYearlyPosting();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to findDtosForYearlyPosting", e);
		}
		return result;
	}

	@Override
	public List<ChartOfAccount> findAllCoaByCriteria(CoaDialogCriteriaDto dto) {
		List<ChartOfAccount> result = new ArrayList<>();
		try {
			result = coaDAO.findAllCoaByCriteria(dto);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to findAllCoaByCriteria", e);
		}
		return result;
	}

	public List<LiabilitiesACDto> findLiabilitiesACDtos() {
		List<LiabilitiesACDto> result = new ArrayList<>();
		try {
			result = coaDAO.findLiabilitiesACDtos();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to findLiabilitiesACDtos", e);
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<GenLedgerSummaryDTO> findGenLedgerSummaryCOAList(GenLedgerCriteria criteria) throws SystemException {

		LocalDate localDate = criteria.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		localDate = localDate.withDayOfMonth(1);
		Date monthStartDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date endDate = criteria.getEndDate();
		String openingBalanceColumn = null;
		String amountColumn = null;
		int mValue = localDate.getMonthValue() - BusinessUtil.getBudSmth();
		mValue = mValue < 0 ? mValue + 12 : mValue;
		if (criteria.isHomeCurrency()) {
			String mSymbol = mValue == 0 ? "hOBal" : "monthlyRate.m".concat(String.valueOf(mValue));
			openingBalanceColumn = mSymbol;
			amountColumn = "homeAmount";
		} else {
			String mSymbol = mValue == 0 ? "oBal" : "msrcRate.msrc".concat(String.valueOf(mValue));
			openingBalanceColumn = mSymbol;
			amountColumn = "localAmount";
		}

		try {
			return coaDAO.findGenLedgerSummaryCOAList(monthStartDate, endDate, openingBalanceColumn, amountColumn);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to Find general ledger Data");
		}

	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ChartOfAccount> findForIbsCode(String ibsCode) throws SystemException {
		List<ChartOfAccount> result = new ArrayList<>();
		try {
			result = coaDAO.findForIbsCode(ibsCode);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to findLiabilitiesACDtos", e);
		}
		return result;
	}
}

