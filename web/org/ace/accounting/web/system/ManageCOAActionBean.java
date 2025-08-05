package org.ace.accounting.web.system;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.validation.ErrorMessage;
import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.common.validation.ValidationResult;
import org.ace.accounting.system.chartaccount.AccountCodeType;
import org.ace.accounting.system.chartaccount.AccountType;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICoaService;
import org.ace.java.component.SystemException;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.ace.java.web.common.BaseBean;

@ManagedBean(name = "ManageCOAActionBean")
@ViewScoped
public class ManageCOAActionBean extends BaseBean {

	@ManagedProperty(value = "#{AccountCodeValidator}")
	private IDataValidator<ChartOfAccount> coaValidator;

	public void setCoaValidator(IDataValidator<ChartOfAccount> coaValidator) {
		this.coaValidator = coaValidator;
	}

	@ManagedProperty(value = "#{DataRepService}")
	private IDataRepService<ChartOfAccount> coaDataRepService;

	public void setCoaDataRepService(IDataRepService<ChartOfAccount> coaDataRepService) {
		this.coaDataRepService = coaDataRepService;
	}

	@ManagedProperty(value = "#{CoaService}")
	private ICoaService coaService;

	public void setCoaService(ICoaService coaService) {
		this.coaService = coaService;
	}

	private boolean createNew;
	private ChartOfAccount coa;
	private List<ChartOfAccount> coaList;
	private List<ChartOfAccount> groupList;
	private List<ChartOfAccount> headList;
	private boolean headCodeDisable = false;
	private boolean groupCodeDisable = true;
	private boolean acCodeDisabled = false;

	@PostConstruct
	public void init() {
		loadData();
		createNewCoa();
	}

	public void createNewCoa() {
		acCodeDisabled = false;
		createNew = true;
		coa = new ChartOfAccount();
		coa.setAcType(AccountType.A);
		coa.setAcCodeType(AccountCodeType.DETAIL);
		loadHeadList();
		eventAcCodeType();
	}

	private void loadData() {
		coaList = coaService.findAllCoa();
		//sort();
	}

	public void prepareUpdateCoa(ChartOfAccount coa) {
		acCodeDisabled = true;
		createNew = false;
		this.coa = coa;
		loadHeadList();
		eventAcCodeType();
		loadGroupList();
	}

	public void loadHeadList() {
		headList = coaList.stream().filter(temp -> temp.getAcCodeType().equals(AccountCodeType.HEAD) && temp.getAcType().equals(coa.getAcType())).collect(Collectors.toList());
	}

	public void eventAcCodeType() {
		switch (coa.getAcCodeType()) {
			case DETAIL:
				headCodeDisable = false;
				groupCodeDisable = false;
				break;
			case GROUP:
				headCodeDisable = false;
				groupCodeDisable = true;
				coa.setGroupId(null);
				break;
			case HEAD:
				headCodeDisable = true;
				groupCodeDisable = true;
				coa.setGroupId(null);
				coa.setHeadId(null);
				break;
		}

	}

	public void loadGroupList() {
		if (coa.getAcCodeType().equals(AccountCodeType.DETAIL)) {
			groupList = coaList.stream().filter(temp -> (temp.getAcCodeType().equals(AccountCodeType.GROUP) && temp.getHeadId().equals(coa.getHeadId())))
					.collect(Collectors.toList());
			groupCodeDisable = false;
		} else {
			groupCodeDisable = true;
		}
	}

	public void addNewCoa() {
		ValidationResult result = coaValidator.validate(coa, false);
		if (result.isVerified()) {
			try {
				coaService.addNewCoa(coa);
				coaList.add(coa);
				addInfoMessage(null, MessageId.INSERT_SUCCESS, coa.getAcCode());
				createNewCoa();
			} catch (SystemException ex) {
				handleSysException(ex);
			}
		} else {
			for (ErrorMessage message : result.getErrorMeesages()) {
				addErrorMessage(message.getId(), message.getErrorcode(), message.getParams());
			}
		}
	}

	public void updateCoa() {
		ValidationResult result = coaValidator.validate(coa, false);
		if (result.isVerified()) {
			try {
				coaService.updateChartOfAccount(coa);
				updateList(coa);
				addInfoMessage(null, MessageId.UPDATE_SUCCESS, coa.getAcCode());
				createNewCoa();
			} catch (SystemException ex) {
				handleSysException(ex);
			}
		} else {

			for (ErrorMessage message : result.getErrorMeesages()) {
				addErrorMessage(message.getId(), message.getErrorcode(), message.getParams());
			}
		}
	}

	public String deleteCoa(ChartOfAccount coa) {
		this.coa = coa;
		ValidationResult result = coaValidator.validate(coa, true);
		if (result.isVerified()) {
			try {
				coaService.deleteChartOfAccount(coa);
				coaList.remove(coa);
				addInfoMessage(null, MessageId.DELETE_SUCCESS, coa.getAcCode());
				createNewCoa();
			} catch (SystemException ex) {
				handleSysException(ex);
			}

		} else {
			for (ErrorMessage message : result.getErrorMeesages()) {
				addErrorMessage(message.getId(), message.getErrorcode(), message.getParams());
			}
		}

		return null;
	}

	private void updateList(ChartOfAccount coa) {
		ChartOfAccount tempCoa = ((List<ChartOfAccount>) coaList.stream().filter(p -> p.getId().equals(coa.getId())).collect(Collectors.toList())).get(0);
		coaList.remove(tempCoa);
		ChartOfAccount newCoa = coaDataRepService.findById(ChartOfAccount.class, coa.getId());
		coaList.add(newCoa);
		sort();
	}

	private void sort() {
		Comparator<ChartOfAccount> c = Comparator.comparing(ChartOfAccount::getAcType).thenComparing(ChartOfAccount::getAcCodeType).thenComparing(ChartOfAccount::getAcCode);
		coaList.sort(c);
	}

	public void setCreateNew(boolean createNew) {
		this.createNew = createNew;
	}

	public List<ChartOfAccount> getCoaList() {
		return coaList;
	}

	public boolean isCreateNew() {
		return createNew;
	}

	public ChartOfAccount getCoa() {
		return coa;
	}

	public void setCoa(ChartOfAccount coa) {
		this.coa = coa;
	}

	public AccountType[] getAcTypes() {
		return AccountType.values();
	}

	public AccountCodeType[] getAccountCodeTypes() {
		return AccountCodeType.values();
	}

	public List<ChartOfAccount> getGroupList() {
		return groupList;
	}

	public List<ChartOfAccount> getHeadList() {
		return headList;
	}

	public boolean isHeadCodeDisable() {
		return headCodeDisable;
	}

	public boolean isGroupCodeDisable() {
		return groupCodeDisable;
	}

	public boolean isAcCodeDisabled() {
		return acCodeDisabled;
	}
}
