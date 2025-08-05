package org.ace.accounting.web.system;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.CoaDialogCriteriaDto;
import org.ace.accounting.dto.YearlyBudgetDto;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.chartaccount.AccountCodeType;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
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

@ManagedBean(name = "YearlyBudgetActionBean")
@ViewScoped
public class YearlyBudgetActionBean extends BaseBean {
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

	@ManagedProperty(value = "#{DepartmentService}")
	private IDepartmentService departmentService;

	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@ManagedProperty(value = "#{CurrencyService}")
	private ICurrencyService currencyService;

	public void setCurrencyService(ICurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	@ManagedProperty(value = "#{BranchService}")
	public IBranchService branchService;

	public void setBranchService(IBranchService branchService) {
		this.branchService = branchService;
	}

	private YearlyBudgetDto dto;
	private List<YearlyBudgetDto> dtoList;
	private List<Branch> branchList;
	private List<Currency> currencyList;
	private List<Department> deptList;
	private boolean admin;

	@PostConstruct
	public void init() {
		createNewYearlyBudget();
		rebindData();
	}

	public void createNewYearlyBudget() {
		dto = new YearlyBudgetDto();
		dtoList = new ArrayList<YearlyBudgetDto>();
		branchList = new ArrayList<Branch>();
		currencyList = new ArrayList<Currency>();
		deptList = new ArrayList<Department>();
		rebindData();

	}

	public void rebindData() {
		User user = userProcessService.getLoginUser();
		admin = user.isAdmin();
		deptList = departmentService.findAllDepartment();
		currencyList = currencyService.findAllCurrency();
		branchList = branchService.findAllBranch();
		if (admin) {
			dto.setBranchId(null);
			dto.setBranchCode(null);
		} else {
			dto.setBranchId(user.getBranch().getId());
			dto.setBranchCode(user.getBranch().getBranchCode());
		}
	}

	public void search() {
		dtoList = ccoaService.findAllYearlyBudget(dto);
	}

	public void saveBudgetYear() {
		try {
			List<YearlyBudgetDto> editedList = dtoList.stream().filter(p -> p.isEdit()).map(p -> new YearlyBudgetDto(p.getCcoaId(), p.getbF())).collect(Collectors.toList());
			if (editedList.size() != 0) {
				ccoaService.updateYearlyBudget(editedList);
				addInfoMessage(null, MessageId.INSERT_SUCCESS, "Budget Year");
				createNewYearlyBudget();
			} else {
				addErrorMessage(null, MessageId.NO_DATA_TOSAVE);
			}
		} catch (SystemException e) {
			handleSysException(e);
		}
	}

	public void deleteCurrentBudget(YearlyBudgetDto dto) {
		dto.setbF(BigDecimal.ZERO);
		dto.setEdit(true);
	}

	public void deleteAllBudgetYear() {
		for (YearlyBudgetDto dto : dtoList) {
			dto.setEdit(true);
			dto.setbF(BigDecimal.ZERO);
		}
	}

	public List<Branch> getBranchList() {
		return branchList;
	}

	public List<Currency> getCurrencyList() {
		return currencyList;
	}

	public List<Department> getDeptList() {
		return deptList;
	}

	public YearlyBudgetDto getDto() {
		return dto;
	}

	public void setDto(YearlyBudgetDto dto) {
		this.dto = dto;
	}

	public List<YearlyBudgetDto> getDtoList() {
		return dtoList;
	}

	public boolean isAdmin() {
		return admin;
	}

	private List<YearlyBudgetDto> filterList;

	public List<YearlyBudgetDto> getFilterList() {
		return filterList;
	}

	public void setFilterList(List<YearlyBudgetDto> filterList) {
		this.filterList = filterList;
	}

	public void returnCoa(SelectEvent event) {
		ChartOfAccount coa = (ChartOfAccount) event.getObject();
		dto.setCoaId(coa.getId());
		dto.setAcCode(coa.getAcCode());
		dto.setAcName(coa.getAcName());
	}

	public void openCoaDialog() {
		CoaDialogCriteriaDto dto = new CoaDialogCriteriaDto();
		dto.setAccountCodeType(AccountCodeType.DETAIL);
		putParam(ParamId.COA_DIALOG_CRITERIA_DATA, dto);
		selectCoa();
	}
}
