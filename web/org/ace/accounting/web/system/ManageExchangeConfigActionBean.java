package org.ace.accounting.web.system;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.CoaDialogCriteriaDto;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.gainloss.ExchangeConfig;
import org.ace.accounting.system.gainloss.service.interfaces.IExchangeConfigService;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.ace.java.web.common.ParamId;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "ManageExchangeConfigActionBean")
@ViewScoped
public class ManageExchangeConfigActionBean extends BaseBean {

	@ManagedProperty(value = "#{ExchangeConfigService}")
	private IExchangeConfigService exchangeService;

	public void setExchangeService(IExchangeConfigService exchangeService) {
		this.exchangeService = exchangeService;
	}

	private ExchangeConfig exchangeConfig;
	private List<ExchangeConfig> exchangeList;
	private boolean isEdit;

	@PostConstruct
	public void init() {
		exchangeConfig = new ExchangeConfig();
		exchangeList = exchangeService.findAllExchangeConfig();
	}

	public void createNewExchange() {
		exchangeConfig = new ExchangeConfig();
	}

	public void createExchange() {
		try {
			if (isEdit) {
				exchangeService.updateBranch(exchangeConfig);
				exchangeList.add(exchangeConfig);
				addInfoMessage(null, MessageId.UPDATE_SUCCESS, exchangeConfig.getAcName());
				exchangeConfig = new ExchangeConfig();
			} else {
				exchangeService.addNewExchangeConfig(exchangeConfig);
				exchangeList.add(exchangeConfig);
				addInfoMessage(null, MessageId.INSERT_SUCCESS, exchangeConfig.getAcName());
				exchangeConfig = new ExchangeConfig();
			}
		} catch (SystemException e) {
			// TODO: handle exception
		}
	}

	public void prepareEdit(ExchangeConfig exchangeConfig) {
		this.isEdit = true;
		this.exchangeConfig = exchangeConfig;
		exchangeList.remove(exchangeConfig);
	}

	public void deleteExchangeConfig(ExchangeConfig exchangeConfig) {
		try {
			exchangeService.deleteBranch(exchangeConfig);
			exchangeList.remove(exchangeConfig);
			addInfoMessage(null, MessageId.DELETE_SUCCESS, exchangeConfig.getAcName());
		} catch (SystemException e) {
			// TODO: handle exception
		}
	}

	public void selectCOACode() {
		CoaDialogCriteriaDto dto = new CoaDialogCriteriaDto();
		putParam(ParamId.COA_DIALOG_CRITERIA_DATA, dto);
		selectCoa();
	}

	public void returnCoaCode(SelectEvent event) {
		ChartOfAccount coa = (ChartOfAccount) event.getObject();
		exchangeConfig.setCoaCode(coa.getAcCode());
		exchangeConfig.setAcName(coa.getAcName());
	}

	public void returnExchangeCode(SelectEvent event) {
		ChartOfAccount coa = (ChartOfAccount) event.getObject();
		exchangeConfig.setExchangeCode(coa.getAcCode());
	}

	public ExchangeConfig getExchangeConfig() {
		return exchangeConfig;
	}

	public void setExchangeConfig(ExchangeConfig exchangeConfig) {
		this.exchangeConfig = exchangeConfig;
	}

	public List<ExchangeConfig> getExchangeList() {
		return exchangeList;
	}

	public boolean isEdit() {
		return isEdit;
	}

}
