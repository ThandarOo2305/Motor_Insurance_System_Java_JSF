package org.ace.accounting.web.system;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.accounting.system.rateinfo.RateInfo;
import org.ace.accounting.system.rateinfo.RateType;
import org.ace.accounting.system.rateinfo.service.interfaces.IRateInfoService;
import org.ace.java.component.SystemException;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.ace.java.web.common.BaseBean;

@ManagedBean(name = "RateDailyCurActionBean")
@ViewScoped
public class RateDailyCurActionBean extends BaseBean {

	@ManagedProperty(value = "#{CurrencyService}")
	private ICurrencyService currencyService;

	public void setCurrencyService(ICurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	@ManagedProperty(value = "#{RateInfoService}")
	private IRateInfoService rateInfoService;

	public void setRateInfoService(IRateInfoService rateInfoService) {
		this.rateInfoService = rateInfoService;
	}

	@ManagedProperty(value = "#{DataRepService}")
	private IDataRepService<RateInfo> dataRepService;

	public void setDataRepService(IDataRepService<RateInfo> dataRepService) {
		this.dataRepService = dataRepService;
	}

	private RateInfo rateInfo;
	private List<RateInfo> rateInfoList;
	private List<Currency> currencyList;
	private boolean createNew;
	private List<RateInfo> filterList;

	@PostConstruct
	public void init() {
		createNewRateInfo();
		loadCurrency();
		rebindData();
	}

	public void createNewRateInfo() {
		createNew = true;
		rateInfo = new RateInfo();
	}

	public void loadCurrency() {
		currencyList = currencyService.findForeignCurrency();
	}

	public void rebindData() {
		rateInfoList = rateInfoService.findAllRateInfo();
	}

	public RateType[] getRateType() {
		return RateType.values();
	}

	public void addNewRateInfo() {
		if (ValidateRateInfo()) {
			try {
				rateInfoService.addNewDailyRateInfo(rateInfo);
				addInfoMessage(null, MessageId.INSERT_SUCCESS, rateInfo.getCurrency().getCurrencyCode());
				createNewRateInfo();
				rebindData();
			} catch (SystemException e) {
				handleSysException(e);
			}
		}
	}

	public void deleteRateInfo() {
		try {
			rateInfo.setLastModify(false);
			dataRepService.update(rateInfo);
			addInfoMessage(null, MessageId.DELETE_SUCCESS, rateInfo.getCurrency().getCurrencyCode());
			createNewRateInfo();
			rebindData();
		} catch (SystemException e) {
			handleSysException(e);
		}
	}

	public boolean ValidateRateInfo() {
		Date maxDate = new Date();
		Date rDate = rateInfo.getrDate();
		boolean validate = true;
		if (rateInfo.getExchangeRate().equals(BigDecimal.ZERO)) {
			addErrorMessage("dailyCurrencyRateForm" + ":exchangeRate", MessageId.REQUIRED_EXCHANGERATE);
			validate = false;
		}
		if (rDate.after(maxDate)) {
			validate = false;
			addErrorMessage(null, MessageId.DATE_EXCEEDED);

		}
		return validate;
	}

	public RateInfo getRateInfo() {
		return rateInfo;
	}

	public void setRateInfo(RateInfo rateInfo) {
		this.rateInfo = rateInfo;
	}

	public List<RateInfo> getRateInfoList() {
		return rateInfoList;
	}

	public List<Currency> getCurrencyList() {
		return currencyList;
	}

	public boolean isCreateNew() {
		return createNew;
	}

	public List<RateInfo> getFilterList() {
		return filterList;
	}

	public void setFilterList(List<RateInfo> filterList) {
		this.filterList = filterList;
	}

	public Date getMaxDate() {
		return new Date();
	}
}
