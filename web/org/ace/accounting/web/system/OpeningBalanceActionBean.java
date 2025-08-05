package org.ace.accounting.web.system;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.CoaDialogCriteriaDto;
import org.ace.accounting.dto.ObalCriteriaDto;
import org.ace.accounting.dto.ObalDto;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.AccountCodeType;
import org.ace.accounting.system.chartaccount.AccountType;
import org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.accounting.system.department.Department;
import org.ace.accounting.user.User;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.ace.java.web.common.ParamId;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "OpeningBalanceActionBean")
@ViewScoped
public class OpeningBalanceActionBean extends BaseBean {
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

	private boolean branchDisable = true;

	private ObalCriteriaDto dto;
	private ObalDto dtoToDelete;
	private List<ObalDto> dtoList;

	private BigDecimal balDiff;

	@PostConstruct
	public void init() {
		createNew();
	}

	public void createNew() {
		balDiff = BigDecimal.ZERO;
		rebindData();
	}

	public void rebindData() {
		dtoList = new ArrayList<>();
		filteredList = new ArrayList<>();
		dto = new ObalCriteriaDto();
		User user = userProcessService.getLoginUser();
		if (user.isAdmin()) {
			branchDisable = false;
		} else {
			branchDisable = true;
			dto.setBranchId(user.getBranch().getId());
		}
		PrimeFaces.current().executeScript("PF('openingBalanceTable').clearFilters();");
		/*
		 * RequestContext context = RequestContext.getCurrentInstance();
		 * context.execute("PF('openingBalanceTable').clearFilters();");
		 */
	}

	public void delete(ObalDto dtoToDelete) {
		dtoToDelete.setoBal(new BigDecimal(0));
		dtoToDelete.setHoBal(new BigDecimal(0));
	}

	public void deleteAll() {
		for (ObalDto dto : dtoList) {
			dto.setoBal(new BigDecimal(0));
			dto.setHoBal(new BigDecimal(0));
		}
	}

	public void search() {
		if (dtoList.size() > 0 && (dtoList.stream().anyMatch(dto -> dto.isUpdated() == true))) {
			PrimeFaces.current().executeScript("PF('saveCheckDialog').show();");
			/*
			 * RequestContext context = RequestContext.getCurrentInstance();
			 * context.execute("PF('saveCheckDialog').show();");
			 */
		} else {
			dtoList = ccoaService.findOpeningBalance(dto);
		}
	}

	public void save() {
		try {
			calBalDiff();
			if (balDiff.compareTo(BigDecimal.ZERO) != 0) {
				addErrorMessage("openingBalanceForm:openingBalanceTable", MessageId.OUT_OF_BALANCE);
				PrimeFaces.current().ajax().update("openingBalanceForm");
			} else {
				ccoaService.updateObalByDtos(dtoList);
				addInfoMessage(null, MessageId.UPDATE_SUCCESS, "Opening balance");
				balDiff = BigDecimal.ZERO;
				rebindData();
				PrimeFaces.current().ajax().update("openingBalanceForm");

			}
		} catch (SystemException e) {
			handleSysException(e);
		}
	}

	public void calBalDiff() {
		BigDecimal assetTotal = BigDecimal.ZERO;
		BigDecimal liabilityTotal = BigDecimal.ZERO;
		for (ObalDto dto : dtoList) {
			if (dto.getAcType() == AccountType.A) {
				assetTotal = assetTotal.add(dto.getHoBal());
			} else if (dto.getAcType() == AccountType.L) {
				liabilityTotal = liabilityTotal.add(dto.getHoBal());
			}
		}
		balDiff = assetTotal.subtract(liabilityTotal);
	}

	public void setDtoList(List<ObalDto> dtoList) {
		this.dtoList = dtoList;
	}

	public List<ObalDto> getDtoList() {
		return dtoList;
	}

	public double getBalDiff() {
		return balDiff.doubleValue();
	}

	public void setBalDiff(BigDecimal balDiff) {
		this.balDiff = balDiff;
	}

	private List<ObalDto> filteredList;

	public List<ObalDto> getFilteredList() {
		return filteredList;
	}

	public void setFilteredList(List<ObalDto> filteredList) {
		this.filteredList = filteredList;
	}

	public void openCoaDialog() {
		CoaDialogCriteriaDto dto = new CoaDialogCriteriaDto();
		dto.setAccountCodeType(AccountCodeType.DETAIL);
		dto.addAccountTypes(AccountType.A);
		dto.addAccountTypes(AccountType.L);
		putParam(ParamId.COA_DIALOG_CRITERIA_DATA, dto);
		selectCoa();
	}

	public void returnDepartment(SelectEvent event) {
		Department dept = (Department) event.getObject();
		dto.setDepartmentId(dept.getId());
		dto.setDeptCode(dept.getdCode());
	}

	public void returnBranch(SelectEvent event) {
		Branch branch = (Branch) event.getObject();
		dto.setBranchId(branch.getId());
		dto.setBranchCode(branch.getBranchCode());
	}

	public void setDto(ObalCriteriaDto dto) {
		this.dto = dto;
	}

	public ObalCriteriaDto getDto() {
		return dto;
	}

	public void setDtoToDelete(ObalDto dtoToDelete) {
		this.dtoToDelete = dtoToDelete;
	}

	public ObalDto getDtoToDelete() {
		return dtoToDelete;
	}

	public List<Currency> getCurrencyList() {
		return currencyService.findAllCurrency();
	}

	@ManagedProperty(value = "#{CurrencyService}")
	private ICurrencyService currencyService;

	public void setCurrencyService(ICurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	public boolean isBranchDisable() {
		return branchDisable;
	}
}
