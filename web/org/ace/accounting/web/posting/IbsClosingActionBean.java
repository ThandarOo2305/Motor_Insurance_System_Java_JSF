package org.ace.accounting.web.posting;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.posting.ibsclosing.service.interfaces.IibsClosingService;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
import org.ace.accounting.system.chartaccount.service.interfaces.ICoaService;
import org.ace.accounting.system.setup.service.ISetupService;
import org.ace.accounting.user.User;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.ace.java.web.common.ParamId;

@ManagedBean(name = "IbsClosingActionBean")
@ViewScoped
public class IbsClosingActionBean extends BaseBean {

	@ManagedProperty(value = "#{BranchService}")
	private IBranchService branchService;

	public void setBranchService(IBranchService branchService) {
		this.branchService = branchService;
	}

	@ManagedProperty(value = "#{IbsClosingService}")
	private IibsClosingService ibsClosingService;

	public void setIbsClosingService(IibsClosingService ibsClosingService) {
		this.ibsClosingService = ibsClosingService;
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

	@ManagedProperty(value = "#{CoaService}")
	private ICoaService coaService;

	public void setCoaService(ICoaService coaService) {
		this.coaService = coaService;
	}

	@ManagedProperty(value = "#{SetupService}")
	private ISetupService setupService;

	public void setSetupService(ISetupService setupService) {
		this.setupService = setupService;
	}

	private User user;
	private ChartOfAccount coa;
	private List<ChartOfAccount> coaList;
	private boolean isIbsClosing;
	private boolean isBranchDisabled;
	private Branch branch;
	private String ibsCode;

	@PostConstruct
	public void init() {
		user = (User) getParam(ParamId.LOGIN_USER);
		branch = user.getBranch();
		isIbsClosing = true;
		isBranchDisabled = true;
		ibsCode = setupService.findSetupValueByVariable("IBS");
		coaList = coaService.findForIbsCode(ibsCode);
	}

	public void createIbsClosing() throws ParseException {
		Date budgetEndDate = BusinessUtil.getBudgetEndDate();
		/*
		 * String pattern = "yyyy-MM-dd"; SimpleDateFormat simpleDateFormat =
		 * new SimpleDateFormat(pattern); Date today =
		 * simpleDateFormat.parse("2021-09-30");
		 */
		Date today = DateUtils.resetStartDate(new Date());
		;
		if (today.compareTo(budgetEndDate) != 0) {
			addErrorMessage(null, "Current date must be budget end date!");
		} else {
			try {
				if (isIbsClosing) {
					ibsClosingService.createIbsClosing(user.getBranch(), branch, coa);
				} else {
					ibsClosingService.createIbsConsolidation(branch, user.getBranch(), coa);
				}

				addInfoMessage(null, MessageId.POSTING_SUCCESS);
			} catch (SystemException e) {
				handleException(e);
			}
		}

	}

	public ChartOfAccount getCoa() {
		return coa;
	}

	public void setCoa(ChartOfAccount coa) {
		this.coa = coa;
	}

	public List<ChartOfAccount> getCoaList() {
		return coaList;
	}

	public boolean isIbsClosing() {
		return isIbsClosing;
	}

	public void setIbsClosing(boolean isIbsClosing) {
		this.isIbsClosing = isIbsClosing;
	}

	public boolean isBranchDisabled() {
		return isIbsClosing ? true : false;
	}

	public void setBranchDisabled(boolean isBranchDisabled) {
		this.isBranchDisabled = isBranchDisabled;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public List<Branch> getBranches() {
		return branchService.findAllBranch();
	}

}
