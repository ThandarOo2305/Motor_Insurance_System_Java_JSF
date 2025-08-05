package org.ace.accounting.web.system;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.CcoaDto;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.accounting.system.department.Department;
import org.ace.accounting.system.department.service.interfaces.IDepartmentService;
import org.ace.java.component.SystemException;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.ace.java.web.common.BaseBean;

@ManagedBean(name = "ManageBCOAActionBean")
@ViewScoped
public class ManageBCOAActionBean extends BaseBean {

	@ManagedProperty(value = "#{CcoaService}")
	private ICcoaService ccoaService;

	public void setCcoaService(ICcoaService ccoaService) {
		this.ccoaService = ccoaService;
	}

	@ManagedProperty(value = "#{DataRepService}")
	private IDataRepService<CurrencyChartOfAccount> dataRepService;

	public void setDataRepService(IDataRepService<CurrencyChartOfAccount> dataRepService) {
		this.dataRepService = dataRepService;
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

	private CurrencyChartOfAccount ccoa;
	private List<CcoaDto> ccoaDtoList;
	private List<Department> deptList;
	private List<Currency> currencyList;

	@PostConstruct
	public void init() {
		deptList = departmentService.findAllDepartment();
		currencyList = currencyService.findAllCurrency();
		createNewCcoa();
		loadData();
	}

	private void loadData() {
		ccoaDtoList = ccoaService.findAllCcoaDtos();
	}

	public void prepareUpdateCcoa(CcoaDto dto) {
		this.ccoa = dataRepService.findById(CurrencyChartOfAccount.class, dto.getId());
	}

	public void updateCcoa() {
		try {
			dataRepService.update(ccoa);
			addInfoMessage(null, MessageId.UPDATE_SUCCESS, ccoa.getCoa().getAcCode());
			createNewCcoa();
			loadData();
		} catch (SystemException ex) {
			handleSysException(ex);
		}
	}

	public void createNewCcoa() {
		ccoa = new CurrencyChartOfAccount();
	}

	public CurrencyChartOfAccount getCcoa() {
		return ccoa;
	}

	public List<Department> getDepartments() {
		return deptList;
	}

	public List<CcoaDto> getCcoaDtoList() {
		return ccoaDtoList;
	}

	public List<Currency> getCurrencyList() {
		return currencyList;

	}
}
