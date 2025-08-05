package org.ace.accounting.web.system;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIInput;

import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.system.allocatecode.AllocateCode;
import org.ace.accounting.system.allocatecode.service.interfaces.IAllocateCodeService;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
import org.ace.accounting.system.chartaccount.service.interfaces.ICoaService;
import org.ace.accounting.system.department.Department;
import org.ace.accounting.system.department.service.interfaces.IDepartmentService;
import org.ace.java.component.SystemException;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.ace.java.web.common.BaseBean;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean(name = "AllocateCodeActionBean")
@ViewScoped
public class AllocateCodeActionBean extends BaseBean {
	@ManagedProperty(value = "#{DepartmentService}")
	private IDepartmentService departmentService;

	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@ManagedProperty(value = "#{CcoaService}")
	private ICcoaService ccoaService;

	public void setCcoaService(ICcoaService ccoaService) {
		this.ccoaService = ccoaService;
	}

	@ManagedProperty(value = "#{CoaService}")
	private ICoaService coaService;

	public void setCoaService(ICoaService coaService) {
		this.coaService = coaService;
	}

	@ManagedProperty(value = "#{AllocateCodeService}")
	private IAllocateCodeService allocateCodeService;

	public void setAllocateCodeService(IAllocateCodeService allocateCodeService) {
		this.allocateCodeService = allocateCodeService;
	}

	@ManagedProperty(value = "#{DataRepService}")
	private IDataRepService<AllocateCode> dataRepService;

	public void setDataRepService(IDataRepService<AllocateCode> dataRepService) {
		this.dataRepService = dataRepService;
	}

	private List<String> budgetYearList;
	private String budgetYear;
	private List<AllocateCode> allocateCodeList;
	private boolean editData;
	private BigDecimal baseOn;
	private List<String> existingAccountList;
	private List<ChartOfAccount> accountList;
	private List<Department> departmentList;
	private TreeNode root;
	private TreeNode[] selectedNodes;

	@PostConstruct
	public void init() {
		createNewAllocateCode();
		bindBudgetYear();
		changeBudgetYear();
	}

	public void createNewAllocateCode() {
		editData = false;
		allocateCodeList = new ArrayList<AllocateCode>();
		baseOn = new BigDecimal("100");
		budgetYearList = new ArrayList<String>();
		budgetYear = "";
		existingAccountList = new ArrayList<String>();
		accountList = new ArrayList<ChartOfAccount>();
		selectedNodes = null;
		root = null;
	}

	public void bindBudgetYear() {
		budgetYearList = ccoaService.findAllBudgetYear();
		departmentList = departmentService.findAllDepartment();
	}

	public void changeBudgetYear() {
		allocateCodeList = allocateCodeService.findAllocateCodeBy(budgetYear);
		if (allocateCodeList == null || allocateCodeList.isEmpty()) {
			allocateCodeList = new ArrayList<AllocateCode>();
			for (Department dept : departmentList) {
				AllocateCode allocateCode = new AllocateCode();
				allocateCode.setBudget(budgetYear);
				allocateCode.setDepartment(dept);
				allocateCode.setAmtPercent(BigDecimal.ZERO);
				allocateCode.setBasedOn(new BigDecimal("100"));
				allocateCodeList.add(allocateCode);
			}

		} else {
			baseOn = allocateCodeList.get(0).getBasedOn();
		}
		editData = false;
	}

	public void saveAllocateCode() {
		if (validateBudgetYear()) {
			bindAccountTreeView();
		}
	}

	public void bindAccountTreeView() {
		root = null;
		accountList = coaService.findCoaByIE();
		existingAccountList = allocateCodeService.findCoaIDBy(budgetYear);

		root = new DefaultTreeNode("Root", null);
		TreeNode parentNode = new DefaultTreeNode("Account Code", root);
		

		boolean found = false;
		for (ChartOfAccount account : accountList) {
//			TreeNode childNode = new DefaultTreeNode(account.getAcCode() + "-" + account.getAcName(), parentNode);
			TreeNode childNode = new DefaultTreeNode(account, parentNode);
			found = false;
			for (String allocateAccount : existingAccountList) {
				if (allocateAccount.equals(account.getId())) {
					found = true;
				}
			}
			childNode.setSelected(found);
		}
		selectedNodes = null;
		PrimeFaces.current().executeScript("PF('accountListDialog').show()");
		/*
		 * RequestContext.getCurrentInstance().execute(
		 * "PF('accountListDialog').show()") ;
		 */
	}

	public void addAccountType() {
		if (selectedNodes != null && selectedNodes.length > 0) {
			try {
				allocateCodeService.addAllocateCodeBy(budgetYear, selectedNodes, allocateCodeList);
				addInfoMessage(null, MessageId.INSERT_SUCCESS, "Cost Allocation");
				createNewAllocateCode();
				bindBudgetYear();
				changeBudgetYear();
			} catch (SystemException e) {
				handleSysException(e);
			}
		} else {
			addErrorMessage(null, MessageId.REQUIRED_ACCOUNTTYPE);
		}
	}

	private boolean validateBudgetYear() {
		boolean valid = true;

		if (budgetYear == null || budgetYear.isEmpty()) {
			addErrorMessage("allocateCodeForm:budgetYear", UIInput.REQUIRED_MESSAGE_ID);
			valid = false;
		}

		if (!isFullPercentage()) {
			addErrorMessage(null, MessageId.PERCENTAGE_ERROR);
			valid = false;
		}
		return valid;
	}

	private boolean isFullPercentage() {
		BigDecimal sum = BigDecimal.ZERO;
		for (AllocateCode allocateCode : allocateCodeList) {
			sum = sum.add(allocateCode.getAmtPercent());
		}
		return sum.compareTo(new BigDecimal(100)) == 0;
	}

	public void deleteAllocateCode() {
		try {
			if (budgetYear == null || budgetYear.isEmpty()) {
				addErrorMessage(null, MessageId.NO_DATA_TODELETE);
			} else {
				allocateCodeService.deleteAllocateCodeBy(budgetYear);
				addInfoMessage(null, MessageId.DELETE_SUCCESS, "Cost Allocation");
				createNewAllocateCode();
				bindBudgetYear();
				changeBudgetYear();
			}

		} catch (SystemException e) {
			handleSysException(e);
		}
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;

	}

	public TreeNode getRoot() {
		return root;
	}

	public String getBudgetYear() {
		return budgetYear;
	}

	public void setBudgetYear(String budgetYear) {
		this.budgetYear = budgetYear;
	}

	public boolean isEditData() {
		return editData;
	}

	public void setEditData(boolean editData) {
		this.editData = editData;
	}

	public BigDecimal getBaseOn() {
		return baseOn;
	}

	public void setBaseOn(BigDecimal baseOn) {
		this.baseOn = baseOn;
	}

	public List<String> getBudgetYearList() {
		return budgetYearList;
	}

	public List<AllocateCode> getAllocateCodeList() {
		return allocateCodeList;
	}
}
