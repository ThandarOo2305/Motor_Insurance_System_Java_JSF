package org.ace.accounting.web.system;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.validation.ErrorMessage;
import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.common.validation.ValidationResult;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.java.component.SystemException;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.ace.java.web.common.BaseBean;

@ManagedBean(name = "ManageBranchActionBean")
@ViewScoped
public class ManageBranchActionBean extends BaseBean {

	@ManagedProperty(value = "#{DataRepService}")
	private IDataRepService<Branch> dataRepService;

	public void setDataRepService(IDataRepService<Branch> dataRepService) {
		this.dataRepService = dataRepService;
	}

	@ManagedProperty(value = "#{BranchService}")
	private IBranchService branchService;

	public void setBranchService(IBranchService branchService) {
		this.branchService = branchService;
	}

	@ManagedProperty(value = "#{BranchCodeValidator}")
	protected IDataValidator<Branch> branchCodeValidator;

	public void setBranchCodeValidator(IDataValidator<Branch> branchCodeValidator) {
		this.branchCodeValidator = branchCodeValidator;
	}

	private boolean createNew;
	private Branch branch;
	private List<Branch> branchList;

	@PostConstruct
	public void init() {
		createNewBranch();
		rebindData();
	}

	public void createNewBranch() {
		createNew = true;
		branch = new Branch();
	}

	public void rebindData() {
		branchList = branchService.findAllBranch();
	}

	public void prepareUpdateBranch(Branch branch) {
		createNew = false;
		this.branch = branch;
	}

	public void addNewBranch() {
		try {
			branchService.addNewBranch(branch);
			addInfoMessage(null, MessageId.INSERT_SUCCESS, branch.getName());
			createNewBranch();
			rebindData();
		} catch (SystemException ex) {
			handleSysException(ex);
		}
	}

	public void updateBranch() {
		try {
			branchService.updateBranch(branch);
			addInfoMessage(null, MessageId.UPDATE_SUCCESS, branch.getName());
			createNewBranch();
			rebindData();
		} catch (SystemException ex) {
			handleSysException(ex);
		}
	}

	public String deleteBranch(Branch branch) {
		ValidationResult result = branchCodeValidator.validate(branch, true);
		if (result.isVerified()) {
			try {
				branchService.deleteBranch(branch);
				addInfoMessage(null, MessageId.DELETE_SUCCESS, branch.getName());
			} catch (SystemException ex) {
				handleSysException(ex);
			}
		} else {
			for (ErrorMessage message : result.getErrorMeesages()) {
				addErrorMessage(null, message.getErrorcode(), message.getParams());
			}
		}

		createNewBranch();
		rebindData();
		return null;
	}

	public boolean isCreateNew() {
		return createNew;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public List<Branch> getBranchList() {
		return branchList;
	}
}
