package org.ace.accounting.posting.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.dto.dailyPostingDto;
import org.ace.accounting.posting.persistence.interfaces.IDailyPostingDAO;
import org.ace.accounting.posting.service.interfaces.IDailyPostingService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.persistence.interfaces.IBranchDAO;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.systempost.persistence.interfaces.ISystemPostDAO;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "DailyPostingService")
public class DailyPostingService extends BaseService implements IDailyPostingService {

	@Resource(name = "DailyPostingDAO")
	private IDailyPostingDAO dailyPostingDAO;

	@Resource(name = "DataRepService")
	private IDataRepService<ChartOfAccount> coaDataRepService;

	@Resource(name = "SystemPostDAO")
	private ISystemPostDAO sysPostDAO;

	@Resource(name = "BranchDAO")
	private IBranchDAO branchDAO;

	@Transactional(propagation = Propagation.REQUIRED)
	public void processDailyPosting(Branch branch, Date postingDate) throws SystemException {
		Date budgetEndDate = BusinessUtil.getBudgetEndDate();
		Date budgetStartDate = BusinessUtil.getBudgetStartDate();
		int month = (DateUtils.monthsBetween(budgetStartDate, budgetEndDate, false, false)) + 1;
		String mField, msrcField, previousMField, previousMsrcField;
		StringBuffer sf;

		try {
			if (postingDate.after(budgetEndDate)) {
				/*
				 * mField = "monthlyRate.m13"; msrcField = "msrcRate.msrc13";
				 * previousMField = "monthlyRate.m12"; previousMsrcField =
				 * "msrcRate.msrc12";
				 */
				mField = "monthlyRate.m" + month + 1;
				msrcField = "msrcRate.msrc" + month + 1;
				previousMField = "monthlyRate.m" + month;
				previousMsrcField = "msrcRate.msrc" + month;
			} else {
				mField = BusinessUtil.getMonthStatusJPQLField(Integer.valueOf(BusinessUtil.getBudgetInfo(postingDate, 3)));
				msrcField = BusinessUtil.getMonthSRCStatusJPQLField(Integer.valueOf(BusinessUtil.getBudgetInfo(postingDate, 3)));
				previousMField = (DateUtils.getMonthFromDate(postingDate) + 1 == 0) ? "monthlyRate.m" + month
						: BusinessUtil.getMonthStatusJPQLField(Integer.valueOf(BusinessUtil.getBudgetInfo(postingDate, 3)) - 1);
				previousMsrcField = (DateUtils.getMonthFromDate(postingDate) + 1 == 0) ? "msrcRate.msrc" + month
						: BusinessUtil.getMonthSRCStatusJPQLField(Integer.valueOf(BusinessUtil.getBudgetInfo(postingDate, 3)) - 1);
			}

			if (branch != null) {

				// '@ Posting Opening Amount of CCOA File .....
				if (mField.equals("monthlyRate.m1")) {
					sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + mField + "=" + "c.oBal" + " WHERE c.branch = :branch");
					dailyPostingDAO.updateMFiledOpeningAmount(sf, branch);
					sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + msrcField + "=" + "c.hOBal" + " WHERE c.branch = :branch");
					dailyPostingDAO.updateMSRCFiledOpeningAmount(sf, branch);
				} else {
					sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + mField + "=" + "c." + previousMField + " WHERE c.branch = :branch");
					dailyPostingDAO.updateMFiledOpeningAmount(sf, branch);
					sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + msrcField + "=" + "c." + previousMsrcField + " WHERE c.branch = :branch");
					dailyPostingDAO.updateMSRCFiledOpeningAmount(sf, branch);
				}

				// '@ Posting LocalAmt To CCOA File .....

				List<dailyPostingDto> detailAccountList = findDetailAccountList(postingDate, branch);

				for (dailyPostingDto dto : detailAccountList) {
					sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + mField + "=" + "c." + mField + "+"
							+ ":localAmount WHERE c.coa = :coa AND c.currency = :currency AND c.branch = :branch");
					dailyPostingDAO.updateMFieldLocalAmount(sf, dto.getLocalAmount(), dto.getCcoa().getCoa(), dto.getCurrency(), branch);

					// 'Head Code Posting Modify (14-07-2012)
					sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + mField + "=" + "c." + mField + "+"
							+ ":localAmount WHERE c.coa = :coa AND c.currency = :currency AND c.branch = :branch");
					dailyPostingDAO.updateMFieldLocalAmount(sf, dto.getLocalAmount(), dto.getHeadCOA(), dto.getCurrency(), branch);

					if (dto.getGroupCOA() != null) {
						sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + mField + "=" + "c." + mField + "+"
								+ ":localAmount WHERE c.coa = :coa AND c.currency = :currency AND c.branch = :branch");
						dailyPostingDAO.updateMFieldLocalAmount(sf, dto.getLocalAmount(), dto.getGroupCOA(), dto.getCurrency(), branch);
					}

					// Update MSRC Field by HomeAmount
					sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + msrcField + "=" + "c." + msrcField + "+"
							+ ":homeAmount WHERE c.coa = :coa AND c.currency = :currency AND c.branch = :branch");
					dailyPostingDAO.updateMSRCFieldHomeAmount(sf, dto.getHomeAmount(), dto.getCcoa().getCoa(), dto.getCurrency(), branch);

					// 'Head Code Posting Modify (14-07-2012)
					sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + msrcField + "=" + "c." + msrcField + "+"
							+ ":homeAmount WHERE c.coa = :coa AND c.currency = :currency AND c.branch = :branch");
					dailyPostingDAO.updateMSRCFieldHomeAmount(sf, dto.getHomeAmount(), dto.getHeadCOA(), dto.getCurrency(), branch);

					if (dto.getGroupCOA() != null) {
						sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + msrcField + "=" + "c." + msrcField + "+"
								+ ":homeAmount WHERE c.coa = :coa AND c.currency = :currency AND c.branch = :branch");
						dailyPostingDAO.updateMSRCFieldHomeAmount(sf, dto.getHomeAmount(), dto.getGroupCOA(), dto.getCurrency(), branch);
					}
				}
			} else {
				List<Branch> branchList = branchDAO.findAll();
				for (Branch branch1 : branchList) {

					// '@ Posting Opening Amount of CCOA File .....
					if (mField.equals("monthlyRate.m1")) {
						sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + mField + "=" + "c.oBal" + " WHERE c.branch = :branch");
						dailyPostingDAO.updateMFiledOpeningAmount(sf, branch1);
						sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + msrcField + "=" + "c.hOBal" + " WHERE c.branch = :branch");
						dailyPostingDAO.updateMSRCFiledOpeningAmount(sf, branch1);
					} else {
						sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + mField + "=" + "c." + previousMField + " WHERE c.branch = :branch");
						dailyPostingDAO.updateMFiledOpeningAmount(sf, branch1);
						sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + msrcField + "=" + "c." + previousMsrcField + " WHERE c.branch = :branch");
						dailyPostingDAO.updateMSRCFiledOpeningAmount(sf, branch1);
					}

					// '@ Posting LocalAmt To CCOA File .....

					List<dailyPostingDto> detailAccountList = findDetailAccountList(postingDate, branch1);

					for (dailyPostingDto dto : detailAccountList) {
						sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + mField + "=" + "c." + mField + "+"
								+ ":localAmount WHERE c.coa = :coa AND c.currency = :currency AND c.branch = :branch");
						dailyPostingDAO.updateMFieldLocalAmount(sf, dto.getLocalAmount(), dto.getCcoa().getCoa(), dto.getCurrency(), branch1);

						// 'Head Code Posting Modify (14-07-2012)
						sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + mField + "=" + "c." + mField + "+"
								+ ":localAmount WHERE c.coa = :coa AND c.currency = :currency AND c.branch = :branch");
						dailyPostingDAO.updateMFieldLocalAmount(sf, dto.getLocalAmount(), dto.getHeadCOA(), dto.getCurrency(), branch1);

						if (dto.getGroupCOA() != null) {
							sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + mField + "=" + "c." + mField + "+"
									+ ":localAmount WHERE c.coa = :coa AND c.currency = :currency AND c.branch = :branch");
							dailyPostingDAO.updateMFieldLocalAmount(sf, dto.getLocalAmount(), dto.getGroupCOA(), dto.getCurrency(), branch1);
						}

						// Update MSRC Field by HomeAmount
						sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + msrcField + "=" + "c." + msrcField + "+"
								+ ":homeAmount WHERE c.coa = :coa AND c.currency = :currency AND c.branch = :branch");
						dailyPostingDAO.updateMSRCFieldHomeAmount(sf, dto.getHomeAmount(), dto.getCcoa().getCoa(), dto.getCurrency(), branch1);

						// 'Head Code Posting Modify (14-07-2012)
						sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + msrcField + "=" + "c." + msrcField + "+"
								+ ":homeAmount WHERE c.coa = :coa AND c.currency = :currency AND c.branch = :branch");
						dailyPostingDAO.updateMSRCFieldHomeAmount(sf, dto.getHomeAmount(), dto.getHeadCOA(), dto.getCurrency(), branch1);

						if (dto.getGroupCOA() != null) {
							sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c." + msrcField + "=" + "c." + msrcField + "+"
									+ ":homeAmount WHERE c.coa = :coa AND c.currency = :currency AND c.branch = :branch");
							dailyPostingDAO.updateMSRCFieldHomeAmount(sf, dto.getHomeAmount(), dto.getGroupCOA(), dto.getCurrency(), branch1);
						}
					}

				}

			}
			sysPostDAO.updatePostingDateByName(postingDate, "TRIALPOST");
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to do daily posting.", e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private List<dailyPostingDto> findDetailAccountList(Date postingDate, Branch branch) {
		List<dailyPostingDto> tlfDetailAccountList = null;
		Date curStartDate = new Date();
		Date curEndDate = new Date();
		int month = (DateUtils.getMonthFromDate(postingDate)) + 1;
		curStartDate = DateUtils.formatStringToDate("01-" + month + "-" + DateUtils.getYearFromDate(postingDate));

		curEndDate = postingDate;
		tlfDetailAccountList = dailyPostingDAO.findTlfDetailAccountList(curStartDate, curEndDate, branch);
		return tlfDetailAccountList;
	}

}
