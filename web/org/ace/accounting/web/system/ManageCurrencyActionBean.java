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
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.java.component.SystemException;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.ace.java.web.common.BaseBean;

@ManagedBean(name = "ManageCurrencyActionBean")
@ViewScoped
public class ManageCurrencyActionBean extends BaseBean {

	@ManagedProperty(value = "#{DataRepService}")
	private IDataRepService<Currency> dataRepService;

	public void setDataRepService(IDataRepService<Currency> dataRepService) {
		this.dataRepService = dataRepService;
	}

	@ManagedProperty(value = "#{CurrencyCodeValidator}")
	protected IDataValidator<Currency> curCodeValidator;

	public void setCurCodeValidator(IDataValidator<Currency> curCodeValidator) {
		this.curCodeValidator = curCodeValidator;
	}

	@ManagedProperty(value = "#{CurrencyService}")
	protected ICurrencyService currencyService;

	public void setCurrencyService(ICurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	private boolean createNew;
	private Currency currency;
	private boolean homeCurDisable;

	private List<Currency> currencyList;

	@PostConstruct
	public void init() {
		createNewCurrency();
		loadCurrency();
	}

	public void createNewCurrency() {
		createNew = true;
		currency = new Currency();
		currency.setIsHomeCur(false);
	}

	public void loadCurrency() {
		currencyList = currencyService.findAllCurrency();
		Currency currency = currencyService.findHomeCurrency();
		homeCurDisable = currency == null ? false : true;
	}

	public void prepareUpdateCurrency(Currency currency) {
		createNew = false;
		this.currency = currency;
	}

	public void addNewCurrency() {
		ValidationResult result = curCodeValidator.validate(currency, false);
		if (result.isVerified()) {
			try {
				currencyService.addNewCurrency(currency);
				addInfoMessage(null, MessageId.INSERT_SUCCESS, currency.getCurrencyCode());
				createNewCurrency();
				loadCurrency();
			} catch (SystemException ex) {
				handleSysException(ex);
			}
		} else {
			for (ErrorMessage message : result.getErrorMeesages()) {
				addErrorMessage(message.getId(), message.getErrorcode(), message.getParams());
			}
		}
	}

	public void updateCurrency() {
		ValidationResult result = curCodeValidator.validate(currency, false);
		if (result.isVerified()) {
			try {
				currencyService.updateCurrency(currency);
				addInfoMessage(null, MessageId.UPDATE_SUCCESS, currency.getCurrencyCode());
				createNewCurrency();
				loadCurrency();
			} catch (SystemException ex) {
				handleSysException(ex);
			}
		} else {
			for (ErrorMessage message : result.getErrorMeesages()) {
				addErrorMessage(message.getId(), message.getErrorcode(), message.getParams());
			}
			loadCurrency();
		}
	}

	public String deleteCurrency(Currency currency) {
		ValidationResult result = curCodeValidator.validate(currency, true);
		if (result.isVerified()) {
			try {
				currencyService.deleteCurrency(currency);
				addInfoMessage(null, MessageId.DELETE_SUCCESS, currency.getCurrencyCode());
				createNewCurrency();
				loadCurrency();
			} catch (SystemException ex) {
				handleSysException(ex);
			}
		} else {
			for (ErrorMessage message : result.getErrorMeesages()) {
				addErrorMessage(null, message.getErrorcode(), message.getParams());
			}
		}
		return null;
	}

	public Currency getCurrency() {
		return currency;
	}

	public boolean isCreateNew() {
		return createNew;
	}

	public List<Currency> getCurrencyList() {
		return currencyList;
	}

	public boolean isHomeCurDisable() {
		return homeCurDisable;
	}
}
