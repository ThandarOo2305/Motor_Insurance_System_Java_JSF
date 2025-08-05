package org.ace.accounting.web.posting;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.posting.service.interfaces.IDailyPostingService;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
import org.ace.accounting.system.systempost.SystemPost;
import org.ace.accounting.system.systempost.service.interfaces.ISystemPostService;
import org.ace.accounting.system.tlf.service.interfaces.ITLFService;
import org.ace.accounting.user.User;
import org.ace.java.component.SystemException;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.ace.java.web.common.BaseBean;
import org.ace.java.web.common.ParamId;

@ManagedBean(name = "DailyPostingActionBean")
@ViewScoped
public class DailyPostingActionBean extends BaseBean {

	@ManagedProperty(value = "#{BranchService}")
	private IBranchService branchService;

	public void setBranchService(IBranchService branchService) {
		this.branchService = branchService;
	}

	@ManagedProperty(value = "#{SystemPostService}")
	private ISystemPostService sysPostService;

	public void setSysPostService(ISystemPostService sysPostService) {
		this.sysPostService = sysPostService;
	}

	@ManagedProperty(value = "#{DataRepService}")
	private IDataRepService<ChartOfAccount> coaDataRepService;

	public void setCoaDataRepService(IDataRepService<ChartOfAccount> coaDataRepService) {
		this.coaDataRepService = coaDataRepService;
	}

	@ManagedProperty(value = "#{TLFService}")
	private ITLFService tlfService;

	public void setTlfService(ITLFService tlfService) {
		this.tlfService = tlfService;
	}

	@ManagedProperty(value = "#{CcoaService}")
	private ICcoaService ccoaService;

	public void setCcoaService(ICcoaService ccoaService) {
		this.ccoaService = ccoaService;
	}

	@ManagedProperty(value = "#{UserProcessService}")
	private IUserProcessService userProcessService;

	public void setUserProcessService(IUserProcessService userProcessService) {
		this.userProcessService = userProcessService;
	}

	@ManagedProperty(value = "#{DailyPostingService}")
	private IDailyPostingService dailyPostingService;

	public void setDailyPostingService(IDailyPostingService dailyPostingService) {
		this.dailyPostingService = dailyPostingService;
	}

	public static String formId = "dailyPostingForm";
	private Branch branch;
	private Date postingDate;
	private SystemPost sysPost;
	private boolean isBranchDisabled;
	private Date lastYear;
	private Date nextYear;
	private boolean admin;

	@PostConstruct
	public void init() {
		createNewPosting();
		User user = (User) getParam(ParamId.LOGIN_USER);
		if (user.isAdmin()) {
			isBranchDisabled = false;
		} else {
			branch = user.getBranch();
			isBranchDisabled = true;
		}

	}

	public void createNewPosting() {
		branch = new Branch();
		postingDate = new Date();
		sysPost = new SystemPost();
		lastYear = new Date();
		nextYear = new Date();

	}

	public void confirmPosting() {
		if (validate()) {
			try {
				dailyPostingService.processDailyPosting(branch, postingDate);
				addInfoMessage(null, MessageId.POSTING_SUCCESS);
			} catch (SystemException e) {
				handleSysException(e);
			}
		}
	}

	public boolean validate() {
		boolean valid = true;
		sysPost = sysPostService.findbyPostingName("TRIALPOST");
		lastYear = sysPost.getPostingDate();
		nextYear = DateUtils.plusYears(lastYear, 1);
		Date budgetEndDate = BusinessUtil.getBudgetEndDate();
		Date budgetStartDate = BusinessUtil.getBudgetStartDate();
		if (!isValidPostingDate()) {
			valid = false;
			addInfoMessage(null, MessageId.VALID_POSTING, budgetStartDate, budgetEndDate);
		} else if (isPostingExcedded()) {
			valid = false;
			addInfoMessage(null, MessageId.POSTING_EXCEDDED_ERROR, nextYear);
		} else if (!isPostingDone()) {
			valid = false;
			addInfoMessage(null, MessageId.POSTING_DONE_ERROR);
		}
		return valid;
	}

	public boolean isValidPostingDate() {
		boolean validate = false;
		Date budgetEndDate = BusinessUtil.getBudgetEndDate();
		Date budgetStartDate = BusinessUtil.getBudgetStartDate();
		if ((postingDate.after(budgetStartDate) || postingDate.equals(budgetStartDate)) && (postingDate.before(budgetEndDate) || postingDate.equals(budgetEndDate))) {
			validate = true;
		}
		return validate;
	}

	public boolean isPostingExcedded() {
		boolean valid = false;
		if (DateUtils.getYearFromDate(postingDate) > DateUtils.getYearFromDate(nextYear)) {
			valid = true;
		} else {
			if (DateUtils.getYearFromDate(postingDate) == DateUtils.getYearFromDate(nextYear)) {
				if (DateUtils.getMonthFromDate(postingDate) > DateUtils.getMonthFromDate(nextYear)) {
					valid = true;
				}
			}
		}
		return valid;
	}

	public boolean isPostingDone() {
		boolean valid = false;
		if (postingDate.equals(lastYear)) {
			valid = true;
		} else if (postingDate.before(lastYear) || postingDate.after(lastYear)) {
			valid = true;
		} else if (postingDate.after(nextYear)) {
			valid = true;
		}
		return valid;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public Date getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}

	public List<Branch> getBranches() {
		return branchService.findAllBranch();
	}

	public boolean isAdmin() {
		return admin;
	}

	public boolean isBranchDisabled() {
		return isBranchDisabled;
	}

}
