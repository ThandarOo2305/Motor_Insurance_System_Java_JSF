package org.ace.accounting.web.dialog;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.dto.CCOADialogDTO;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.user.User;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.ace.java.web.common.BaseBean;
import org.ace.java.web.common.ParamId;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "CCOAAccountDialogActionBean")
@ViewScoped
public class CCOAAccountDialogActionBean extends BaseBean {
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

	@ManagedProperty(value = "#{DataRepService}")
	private IDataRepService<CurrencyChartOfAccount> dataRepService;

	public void setDataRepService(IDataRepService<CurrencyChartOfAccount> dataRepService) {
		this.dataRepService = dataRepService;
	}

	private List<CCOADialogDTO> ccoaList;

	@PostConstruct
	public void init() {
		User user = userProcessService.getLoginUser();
		Currency currency = (Currency) getParam(ParamId.CURRENCY_DATA);
		ccoaList = ccoaService.findAllCCOADialogDTO(currency, user.getBranch());
	}

	public List<CCOADialogDTO> getCcoaList() {
		return ccoaList;
	}

	public void selectCCOAAccount(CCOADialogDTO ccoa) {
		CurrencyChartOfAccount account = dataRepService.findById(CurrencyChartOfAccount.class, ccoa.getId());
		PrimeFaces.current().dialog().closeDynamic(account);
	}

	@PreDestroy
	public void destory() {
		removeParam(ParamId.CURRENCY_DATA);
	}
}
