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
import org.ace.accounting.posting.service.interfaces.IDataCloningService;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.setup.service.ISetupService;
import org.ace.accounting.system.systempost.service.interfaces.ISystemPostService;
import org.ace.accounting.system.tlf.service.interfaces.ITLFService;
import org.ace.accounting.user.User;
import org.ace.java.component.SystemException;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.ace.java.web.common.BaseBean;
import org.ace.java.web.common.ParamId;

@ManagedBean(name = "DataCloningActionBean")
@ViewScoped
public class DataCloningActionBean extends BaseBean {

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

	@ManagedProperty(value = "#{SetupService}")
	private ISetupService setupService;

	public void setSetupService(ISetupService setupService) {
		this.setupService = setupService;
	}

	@ManagedProperty(value = "#{DataCloningService}")
	private IDataCloningService dataCloningService;

	public void setDataCloningService(IDataCloningService dataCloningService) {
		this.dataCloningService = dataCloningService;
	}

	public static String formId = "dataCloningForm";
	private Branch branch;
	private Date postingDate;
	private boolean isBranchDisabled;
	private boolean isDataCloning;
	private boolean admin;
	private Currency currency;

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
		postingDate = DateUtils.formatStringToDate(setupService.findSetupValueByVariable(BusinessUtil.BUDGETEDATE));
		isDataCloning = true;
		isBranchDisabled = true;

	}

	public void confirmCloning() {
		if (validate()) {
			try {
				dataCloningService.handleDataCloning(postingDate);
				addInfoMessage(null, MessageId.CLONING_SUCCESS);
			} catch (SystemException e) {
				handleSysException(e);
			}
		}
	}

	public boolean validate() {
		boolean valid = true;
		// Date budgetEndDate =
		// DateUtils.formatStringToDate(setupService.findSetupValueByVariable(BusinessUtil.BUDGETEDATE));
		Date budgetEndDate = BusinessUtil.getBudgetEndDate();
		Date budgetStartDate = BusinessUtil.getBudgetStartDate();
		Date todayDate = new Date();
		if (budgetEndDate.after(todayDate)) {
			valid = false;
			addInfoMessage(null, MessageId.VALID_CLONING, budgetStartDate, budgetEndDate);
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
		return isDataCloning ? true : false;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Currency getCurrency() {
		return currency;
	}

	public boolean isDataCloning() {
		return isDataCloning;
	}

	public void setDataCloning(boolean isDataCloning) {
		this.isDataCloning = isDataCloning;
	}

}
