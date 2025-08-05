package org.ace.accounting.web.report;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.java.web.common.BaseBean;

@ManagedBean(name = "TrialBalanceGroupActionBean")
@ViewScoped
public class TrialBalanceGroupActionBean extends BaseBean {
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

	// To choose currency type HomeCurrency or SourceCurrency.
	private CurrencyType currencyType;
	// To check HomeCurrency or not.
	private boolean isHomeCurrency;
	private Branch branch;
	private Currency currency;
	// Convert SourceCurrency to HomeCurrency or not convert.
	private boolean isCurrencyConverted;
	// Allocated Year.
	private String allocateYear;
	// Allocated Month.
	private String allocateMonth;

	@PostConstruct
	public void init() {
		// dto = new CostAllocationReportDto();
		isHomeCurrency = true;
		isCurrencyConverted = false;
		currency = currencyService.findHomeCurrency();
		currencyType = CurrencyType.HOMECURRENCY;
	}

	/**
	 * Select HomeCurrency or SourceCurrency.
	 */

	public void currencySelect() {
		if (currencyType.equals(CurrencyType.HOMECURRENCY)) {
			currency = currencyService.findHomeCurrency();
			isHomeCurrency = true;
		} else if (currencyType.equals(CurrencyType.SOURCECURRENCY)) {
			isHomeCurrency = false;
		}
	}

	public CurrencyType getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(CurrencyType currencyType) {
		this.currencyType = currencyType;
	}

	public boolean isHomeCurrency() {
		return isHomeCurrency;
	}

	public void setHomeCurrency(boolean isHomeCurrency) {
		this.isHomeCurrency = isHomeCurrency;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public boolean isCurrencyConverted() {
		return isCurrencyConverted;
	}

	public void setCurrencyConverted(boolean isCurrencyConverted) {
		this.isCurrencyConverted = isCurrencyConverted;
	}

	public String getAllocateYear() {
		return allocateYear;
	}

	public void setAllocateYear(String allocateYear) {
		this.allocateYear = allocateYear;
	}

	public String getAllocateMonth() {
		return allocateMonth;
	}

	public void setAllocateMonth(String allocateMonth) {
		this.allocateMonth = allocateMonth;
	}

	/**
	 * To get all currency lists.
	 * 
	 * @return List[all currency]
	 */
	public List<Currency> getCurrencyList() {
		return currencyService.findAllCurrency();
	}

	/**
	 * To get all branch lists.
	 * 
	 * @return List[all branch]
	 */
	public List<Branch> getBranchList() {
		return branchService.findAllBranch();
	}

	public CurrencyType[] getCurrencyTypes() {
		return CurrencyType.values();
	}
}
