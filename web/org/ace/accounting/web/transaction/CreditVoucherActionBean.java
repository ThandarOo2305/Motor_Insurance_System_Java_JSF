package org.ace.accounting.web.transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.VoucherDTO;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.role.Role;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.accounting.system.rateinfo.RateType;
import org.ace.accounting.system.tlf.service.interfaces.ITLFService;
import org.ace.accounting.system.trantype.TranCode;
import org.ace.accounting.system.trantype.service.interfaces.ITranTypeService;
import org.ace.accounting.user.User;
import org.ace.java.component.SystemException;
import org.ace.java.component.service.PasswordCodecHandler;
import org.ace.java.web.common.BaseBean;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "CreditVoucherActionBean")
@ViewScoped
public class CreditVoucherActionBean extends BaseBean {

	@ManagedProperty(value = "#{CurrencyService}")
	private ICurrencyService currencyService;

	public void setCurrencyService(ICurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	@ManagedProperty(value = "#{TLFService}")
	private ITLFService tlfService;

	public void setTlfService(ITLFService tlfService) {
		this.tlfService = tlfService;
	}

	@ManagedProperty(value = "#{UserProcessService}")
	private IUserProcessService userProcessService;

	public void setUserProcessService(IUserProcessService userProcessService) {
		this.userProcessService = userProcessService;
	}

	@ManagedProperty(value = "#{TranTypeService}")
	private ITranTypeService tranTypeService;

	public void setTranTypeService(ITranTypeService tranTypeService) {
		this.tranTypeService = tranTypeService;
	}

	@ManagedProperty(value = "#{PasswordCodecHandler}")
	private PasswordCodecHandler codecHandler;

	public void setCodecHandler(PasswordCodecHandler codecHandler) {
		this.codecHandler = codecHandler;
	}

	private List<Currency> currencyList;
	private VoucherDTO voucherDto;
	private boolean admin;
	private Date todayDate;
	private Date minDate;
	private Date beforeBudgetSDate;
	private String editPassword;
	private boolean isEdit;

	@PostConstruct
	public void init() {
		createNewCreditVoucher();
		rebindData();
		minDate = BusinessUtil.getBackDate();
		beforeBudgetSDate = BusinessUtil.getBudgetStartDate();
		isEdit = false;
	}

	public void rebindData() {
		currencyList = currencyService.findAllCurrency();
	}

	public void createNewCreditVoucher() {
		voucherDto = new VoucherDTO();
		Currency homeCur = currencyService.findHomeCurrency();
		voucherDto.setCurrency(homeCur);
		voucherDto.setHomeExchangeRate(BigDecimal.ONE);
		todayDate = new Date();
		admin = userProcessService.getLoginUser().isAdmin();
		voucherDto.setCreatedUserId(userProcessService.getLoginUser().getId());
		voucherDto.setSettlementDate(new Date());
	}

	public void addVoucher() {
		// if (validate()) {
		try {

			if (!isEdit) {
				Date budgetStartDate = BusinessUtil.getBudgetStartDate();
				minDate = BusinessUtil.getBackDate();
				boolean pwdControl = BusinessUtil.getPwdControl(budgetStartDate, minDate, voucherDto.getSettlementDate());
				if (pwdControl) {
					PrimeFaces.current().executeScript("PF('passwordDialog').show();");
					isEdit = false;
				} else {
					isEdit = true;
				}

				if (isEdit) {

					if (voucherDto.getHomeExchangeRate().doubleValue() > 0) {
						String voucherNo = tlfService.addVoucher(voucherDto, TranCode.CSCREDIT);
						addInfoMessage(null, MessageId.INSERT_SUCCESS, voucherNo);
						createNewCreditVoucher();
					} else {
						addErrorMessage(null, MessageId.NO_EXCHANGE_RATE,
								DateUtils.formatDateToString(null != voucherDto.getSettlementDate() ? voucherDto.getSettlementDate() : new Date()));
					}
				}
				// }
			}
			isEdit = false;
		} catch (SystemException e) {
			handleSysException(e);
		}
	}

	public void changeRate() {
		if (voucherDto.getCurrency() != null) {
			double exchangeRate = 0.0;
			if (null != voucherDto.getSettlementDate()) {
				exchangeRate = tlfService.getExchangeRate(voucherDto.getCurrency(), RateType.CS, voucherDto.getSettlementDate());
			} else {
				exchangeRate = tlfService.getExchangeRate(voucherDto.getCurrency(), RateType.CS, new Date());
			}
			voucherDto.setHomeExchangeRate(BigDecimal.valueOf(exchangeRate));
		}
		PrimeFaces.current().resetInputs("voucherForm:exchangeRate");
	}

	public boolean validate() {
		boolean valid = true;

		Date budgetEndDate = BusinessUtil.getBudgetEndDate();
		Date budgetStartDate = BusinessUtil.getBudgetStartDate();
		if (isValidVoucherDate()) {
			valid = false;
			addInfoMessage(null, MessageId.VALID_VOUCHER_DATE, DateUtils.formatDateToString(budgetStartDate), DateUtils.formatDateToString(budgetEndDate));
		}
		return valid;
	}

	public boolean isValidVoucherDate() {
		boolean validate = false;
		/*
		 * if ((voucherDto.getSettlementDate().after(budgetStartDate) ||
		 * voucherDto.getSettlementDate().equals(budgetStartDate)) &&
		 * (voucherDto.getSettlementDate().before(budgetEndDate) ||
		 * voucherDto.getSettlementDate().equals(budgetEndDate))) {
		 * 
		 */
		if (voucherDto.getSettlementDate().before(minDate) || voucherDto.getSettlementDate().equals(minDate)) {

			validate = true;
		}
		return validate;
	}

	public void authenticatePage() {
		if (isEditUser()) {

			if (authenticate(this.editPassword)) {
				PrimeFaces.current().executeScript("PF('passwordDialog').hide();");
				if (!isEdit) {
					if (voucherDto.getHomeExchangeRate().doubleValue() > 0) {
						String voucherNo = tlfService.addVoucher(voucherDto, TranCode.CSCREDIT);
						addInfoMessage(null, MessageId.INSERT_SUCCESS, voucherNo);
						createNewCreditVoucher();
					} else {
						addErrorMessage(null, MessageId.NO_EXCHANGE_RATE,
								DateUtils.formatDateToString(null != voucherDto.getSettlementDate() ? voucherDto.getSettlementDate() : new Date()));
					}
				}
			} else {
				addErrorMessage("passwordForm:password", MessageId.WRONG_PASSWORD);
			}

		} else {
			addErrorMessage("passwordForm:password", MessageId.INVALID_USER);
		}

	}

	public boolean isEditUser() {
		User user = userProcessService.getLoginUser();

		if (null != user && user.getBranch().getBranchCode().equals("001")) {
			for (Role userRole : user.getRoles()) {
				if (userRole.getName().equalsIgnoreCase("admin")) {
					return true;
				}
			}
		} else {
			return false;
		}

		return false;

	}

	public String returnDashBoard() {
		return "home.xhtml?faces-redirect=true";
	}

	private boolean authenticate(String password) {
		String decodedPassword = BusinessUtil.getVoucherEditPassword();
		String encodedPassword = codecHandler.encode(password);
		return decodedPassword.equals(encodedPassword);

	}

	public void changeDate() {
		changeRate();
	}

	public void changeCurrency() {
		voucherDto.setCcoa(null);
		changeRate();
	}

	public void selectCCOAAccountCode() {
		selectCCOAAccountCode(voucherDto.getCurrency());
	}

	public void returnCCOAAccountCode(SelectEvent event) {
		CurrencyChartOfAccount ccoa = (CurrencyChartOfAccount) event.getObject();
		voucherDto.setCcoa(ccoa);
	}

	public List<Currency> getCurrencyList() {
		return currencyList;
	}

	public VoucherDTO getVoucherDto() {
		return voucherDto;
	}

	public boolean isAdmin() {
		return admin;
	}

	public Date getTodayDate() {
		return todayDate;
	}

	public Date getMinDate() {
		return minDate;
	}

	public Date getBeforeBudgetSDate() {
		return beforeBudgetSDate;
	}

	public boolean isEdit() {
		return isEdit;
	}

	public String getEditPassword() {
		return editPassword;
	}

	public void setEditPassword(String editPassword) {
		this.editPassword = editPassword;
	}

}
