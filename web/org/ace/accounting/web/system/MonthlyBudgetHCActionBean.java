package org.ace.accounting.web.system;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.CoaDialogCriteriaDto;
import org.ace.accounting.dto.MonthlyBudgetHomeCurDto;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.chartaccount.AccountCodeType;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.accounting.system.department.Department;
import org.ace.accounting.system.department.service.interfaces.IDepartmentService;
import org.ace.accounting.user.User;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.ace.java.web.common.ParamId;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "MonthlyBudgetHCActionBean")
@ViewScoped
public class MonthlyBudgetHCActionBean extends BaseBean {

	@ManagedProperty(value = "#{UserProcessService}")
	private IUserProcessService userProcessService;

	public void setUserProcessService(IUserProcessService userProcessService) {
		this.userProcessService = userProcessService;
	}

	@ManagedProperty(value = "#{CcoaService}")
	private ICcoaService ccoaService;

	public void setCcoaService(ICcoaService ccoaService) {
		this.ccoaService = ccoaService;
	}

	@ManagedProperty(value = "#{CurrencyService}")
	private ICurrencyService currencyService;

	public void setCurrencyService(ICurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	@ManagedProperty(value = "#{BranchService}")
	private IBranchService branchService;

	public void setBranchService(IBranchService branchService) {
		this.branchService = branchService;
	}

	@ManagedProperty(value = "#{DepartmentService}")
	private IDepartmentService departmentService;

	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	private MonthlyBudgetHomeCurDto dto;
	private List<MonthlyBudgetHomeCurDto> dtoList;
	private boolean isBranchDisable;

	@PostConstruct
	public void init() {
		rebindData();
	}

	public void rebindData() {
		dtoList = new ArrayList<>();
		filterList = new ArrayList<>();
		dto = new MonthlyBudgetHomeCurDto();
		User user = userProcessService.getLoginUser();
		if (user.isAdmin()) {
			isBranchDisable = false;
		} else {
			dto.setBranchCode(user.getBranch().getBranchCode());
			dto.setBranchid(user.getBranch().getId());
			isBranchDisable = true;
		}
	}

	public void search() {
		dtoList = ccoaService.findAllMonthlyBudgetHomeCur(dto);
	}

	public void saveMonthlyBudgetHomeCur() {
		try {
			ccoaService.updateMonthlyBudgetHomeCur(dtoList);
			addInfoMessage(null, MessageId.UPDATE_SUCCESS, "Budget Month Home Currency");
			rebindData();
		} catch (SystemException e) {
			handleSysException(e);
		}
	}

	public void deleteCurrentBudget(MonthlyBudgetHomeCurDto dto) {
		dto.setAllZero();
	}

	public void deleteAllBudget() {

		for (MonthlyBudgetHomeCurDto dto : dtoList) {
			dto.setAllZero();
		}

	}

	public boolean isBranchDisable() {
		return isBranchDisable;
	}

	public MonthlyBudgetHomeCurDto getDto() {
		return dto;
	}

	public void setDto(MonthlyBudgetHomeCurDto dto) {
		this.dto = dto;
	}

	public List<MonthlyBudgetHomeCurDto> getDtoList() {
		return dtoList;
	}

	public void setDtoList(List<MonthlyBudgetHomeCurDto> dtoList) {
		this.dtoList = dtoList;
	}

	private List<CurrencyChartOfAccount> filterList;

	public List<CurrencyChartOfAccount> getFilterList() {
		return filterList;
	}

	public void setFilterList(List<CurrencyChartOfAccount> filterList) {
		this.filterList = filterList;
	}

	public void returnCoa(SelectEvent event) {
		ChartOfAccount coa = (ChartOfAccount) event.getObject();
		dto.setCoaid(coa.getId());
		dto.setAcCode(coa.getAcCode());
	}

	public List<Currency> getCurrencyList() {
		return currencyService.findAllCurrency();
	}

	public void returnDepartment(SelectEvent event) {
		Department dept = (Department) event.getObject();
		dto.setDepartmentid(dept.getId());
		dto.setdCode(dept.getdCode());
	}

	public void returnBranch(SelectEvent event) {
		Branch branch = (Branch) event.getObject();
		dto.setBranchid(branch.getId());
		dto.setBranchCode(branch.getBranchCode());
	}

	public void openCoaDialog() {
		CoaDialogCriteriaDto dto = new CoaDialogCriteriaDto();
		dto.setAccountCodeType(AccountCodeType.DETAIL);
		putParam(ParamId.COA_DIALOG_CRITERIA_DATA, dto);
		selectCoa();
	}
}
