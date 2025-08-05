package org.ace.accounting.posting.service;

import java.util.Date;

import javax.annotation.Resource;

import org.ace.accounting.common.SystemConstants;
import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.dto.YPDto;
import org.ace.accounting.posting.persistence.interfaces.IYearlyPostingDAO;
import org.ace.accounting.posting.service.interfaces.IYearlyPostingService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.CcoaHistory;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.chartaccount.persistence.interfaces.ICcoaDAO;
import org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
import org.ace.accounting.system.chartaccount.service.interfaces.ICoaService;
import org.ace.accounting.system.rateinfo.persistence.interfaces.IRateInfoDAO;
import org.ace.accounting.system.systempost.SystemPost;
import org.ace.accounting.system.systempost.service.interfaces.ISystemPostService;
import org.ace.accounting.system.tlf.TLF;
import org.ace.accounting.system.tlf.service.interfaces.ITLFService;
import org.ace.accounting.system.tlfhist.TLFHIST;
import org.ace.accounting.system.trantype.persistence.interfaces.ITranTypeDAO;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "YearlyPostingService")
public class YearlyPostingService extends BaseService implements IYearlyPostingService {
	@Resource(name = "DataRepService")
	private IDataRepService<SystemPost> systemPostDataService;

	@Resource(name = "SystemPostService")
	private ISystemPostService systemPostService;

	@Resource(name = "TLFService")
	private ITLFService tlfService;

	@Resource(name = "DataRepService")
	private IDataRepService<TLF> tlfDataService;

	@Resource(name = "DataRepService")
	private IDataRepService<TLFHIST> tlfHistDataService;

	@Resource(name = "CcoaService")
	private ICcoaService ccoaService;

	@Resource(name = "DataRepService")
	private IDataRepService<CcoaHistory> ccoaHistDataService;

	@Resource(name = "DataRepService")
	private IDataRepService<CurrencyChartOfAccount> ccoaDataService;

	@Resource(name = "CoaService")
	private ICoaService coaService;

	@Resource(name = "YearlyPostingDAO")
	private IYearlyPostingDAO yearlyPostingDAO;

	@Resource(name = "CcoaDAO")
	private ICcoaDAO ccoaDAO;

	@Resource(name = "RateInfoDAO")
	private IRateInfoDAO rateInfoDAO;

	@Resource(name = "TranTypeDAO")
	private ITranTypeDAO tranTypeDAO;

	@Resource(name = "TLFService")
	private ITLFService tLFService;

	@Transactional(propagation = Propagation.REQUIRED)
	public void handleYearlyPosting(Date postingDate, YPDto plAc, Branch branch) {
		try {
			String budgetYear = BusinessUtil.getBudgetInfo(DateUtils.plusYears(postingDate, 1), 2);
			SystemPost sysPost = systemPostService.findbyPostingName(SystemConstants.YEARPOST);

			sysPost.setPostingDate(BusinessUtil.getBudgetEndDate());
			systemPostDataService.update(sysPost);

			yearlyPostingDAO.moveTlfToHistory(postingDate);
			yearlyPostingDAO.moveCcoaToHistory(budgetYear, plAc, branch);
			yearlyPostingDAO.moveSetupToHistory();
			// yearlyPostingDAO.increaseBudgetYear();

			/* For setup table update after yearly posting */
			yearlyPostingDAO.increaseSetupBudgetYear();

		} catch (DAOException de) {
			throw new SystemException(de.getErrorCode(), "Failed to handle yearly posting.", de);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void handleYearlyClosing(YPDto plAc, Branch branch) {
		try {

			yearlyPostingDAO.createHOClosingForManulProcess(branch.getId(), branch, plAc);

		} catch (DAOException de) {
			throw new SystemException(de.getErrorCode(), "Failed to handle yearly Closing.", de);
		}
	}

}