package org.ace.accounting.web.transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.JVdto;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.role.Role;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.accounting.system.rateinfo.service.interfaces.IRateInfoService;
import org.ace.accounting.system.tlf.service.interfaces.ITLFService;
import org.ace.accounting.system.trantype.TranCode;
import org.ace.accounting.user.User;
import org.ace.java.component.SystemException;
import org.ace.java.component.service.PasswordCodecHandler;
import org.ace.java.web.common.BaseBean;
import org.ace.java.web.common.ParamId;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "JournalVoucherActionBean")
@ViewScoped
public class JournalVoucherActionBean extends BaseBean {

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

	@ManagedProperty(value = "#{RateInfoService}")
	private IRateInfoService rateInfoService;

	public void setRateInfoService(IRateInfoService rateInfoService) {
		this.rateInfoService = rateInfoService;
	}

	@ManagedProperty(value = "#{UserProcessService}")
	private IUserProcessService userProcessService;

	public void setUserProcessService(IUserProcessService userProcessService) {
		this.userProcessService = userProcessService;
	}

	@ManagedProperty(value = "#{PasswordCodecHandler}")
	private PasswordCodecHandler codecHandler;

	public void setCodecHandler(PasswordCodecHandler codecHandler) {
		this.codecHandler = codecHandler;
	}

	private boolean createNew;
	private JVdto dto;
	private List<JVdto> dtoList;
	private Currency cur;
	private double exchangeRate;
	private int dtoIndex;
	private boolean admin;
	private Date todayDate;
	private Date minDate;
	private String confirmEdit;

	private BigDecimal totalDebitAmount = BigDecimal.ZERO;
	private BigDecimal totalDebitHomeAmount = BigDecimal.ZERO;
	private BigDecimal totalCreditAmount = BigDecimal.ZERO;
	private BigDecimal totalCreditHomeAmount = BigDecimal.ZERO;

	private BigDecimal differentAmount = BigDecimal.ZERO;
	private BigDecimal differentHomeAmount = BigDecimal.ZERO;
	private BigDecimal homeAmt = BigDecimal.ZERO;
	private Date beforeBudgetSDate;
	private String editPassword;
	private boolean isEdit;

	@PostConstruct
	public void init() {
		rebindData();
		createNewDto();
		setConfirmEdit("New");
		minDate = BusinessUtil.getBackDate();
		beforeBudgetSDate = BusinessUtil.getBudgetStartDate();
		isEdit = false;
	}

	@PreDestroy
	public void destroy() {
		removeParam(ParamId.COA_DATA);
	}

	private void rebindData() {
		dtoList = new ArrayList<JVdto>();
	}

	public void prepareUpdateDto(JVdto dto) {
		createNew = false;
		dtoIndex = dtoList.indexOf(dto);
		this.dto = dto;
		setConfirmEdit("Edit");
	}

	public void createNewDto() {
		createNew = true;
		dto = new JVdto();
		if (dtoList.size() > 0) {
			dto.setCur(cur);
			// dto.setVoucherNo(dtoList.get(0).getVoucherNo());
			dto.setExchangeRate(BigDecimal.valueOf(exchangeRate));
			dto.setSettlementDate(dtoList.get(0).getSettlementDate());
			if (confirmEdit.equals("New") && dtoList.size() == 0) {
				dto.setNarration(null);
			} else {
				dto.setNarration(dtoList.get(0).getNarration());
			}
		} else {
			dto.setCur(currencyService.findHomeCurrency());
			cur = dto.getCur();
			dto.setExchangeRate(BigDecimal.ONE);
			// dto.setVoucherNo(dto.getVoucherNo());
		}
		todayDate = new Date();

		admin = userProcessService.getLoginUser().isAdmin();
		if (null == dto.getSettlementDate()) {
			dto.setSettlementDate(new Date());
		}

	}

	public void changeRate() {
		if (cur.getIsHomeCur()) {
			dto.setExchangeRate(BigDecimal.ONE);
		} else {
			exchangeRate = rateInfoService.findCurrentRateInfo(cur, dto.getSettlementDate());
			dto.setExchangeRate(BigDecimal.valueOf(exchangeRate));
		}
		dto.setCur(cur);
		// dto.setCcoa(null);
		PrimeFaces.current().resetInputs("journalVoucherEntryForm:exchangeRate");
	}

	public void changeDate() {
		changeRate();
	}

	public void changeCurrency() {
		dto.setCcoa(null);
		changeRate();
	}

	public void saveDtos() {
		if (validateDtoList()) {
		try {
			if (!isEdit) {
				Date budgetStartDate = BusinessUtil.getBudgetStartDate();
				minDate = BusinessUtil.getBackDate();
				boolean pwdControl = BusinessUtil.getPwdControl(budgetStartDate, minDate, dto.getSettlementDate());
				if (pwdControl) {
					PrimeFaces.current().executeScript("PF('passwordDialog').show();");
					isEdit = false;
				} else {
					isEdit = true;
				}

				if (isEdit) {
					adjustedAmount();
					String voucherNo = tlfService.addNewTlfByDto(dtoList);
					addInfoMessage(null, MessageId.INSERT_SUCCESS, voucherNo);
					rebindData();
					createNewDto();

					}
				}
				isEdit = false;
			} catch (SystemException ex) {
				handleSysException(ex);
			}
		}
	  }
	//}

	public void addNewDto() {
		if (validate()) {
			try {
				exchangeRate = dto.getExchangeRate().doubleValue();
				homeAmt = dto.getAmount().multiply(dto.getExchangeRate()).setScale(2, RoundingMode.HALF_UP);
				dto.setHomeAmount(homeAmt);
				dtoList.add(dto);
				calculatTotalAmount();
				calculateDifferentAmount();
				createNewDto();
			} catch (SystemException ex) {
				handleSysException(ex);
			}
		}
	}

	private void calculatTotalAmount() {
		this.totalDebitAmount = BigDecimal.ZERO;
		this.totalDebitHomeAmount = BigDecimal.ZERO;

		this.totalCreditAmount = BigDecimal.ZERO;
		this.totalCreditHomeAmount = BigDecimal.ZERO;

		for (JVdto dto : dtoList) {
			if (dto.getTranCode().equals(TranCode.CSCREDIT) || dto.getTranCode().equals(TranCode.TRCREDIT)) {
				this.totalCreditHomeAmount = totalCreditHomeAmount.add(dto.getHomeAmount()).setScale(2, RoundingMode.HALF_UP);
				this.totalCreditAmount = totalCreditAmount.add(dto.getAmount()).setScale(2, RoundingMode.HALF_UP);
			} else {
				this.totalDebitHomeAmount = totalDebitHomeAmount.add(dto.getHomeAmount()).setScale(2, RoundingMode.HALF_UP);
				this.totalDebitAmount = totalDebitAmount.add(dto.getAmount()).setScale(2, RoundingMode.HALF_UP);
			}
		}
	}

	private void calculateDifferentAmount() {
		this.differentAmount = BigDecimal.ZERO;
		this.differentHomeAmount = BigDecimal.ZERO;

		this.differentAmount = totalDebitAmount.subtract(totalCreditAmount).setScale(2, RoundingMode.HALF_UP);
		this.differentHomeAmount = totalDebitHomeAmount.subtract(totalCreditHomeAmount).setScale(2, RoundingMode.HALF_UP);
	}

	private void adjustedAmount() {

		if (!this.differentHomeAmount.equals(BigDecimal.ZERO) && totalDebitHomeAmount.compareTo(totalCreditHomeAmount) == 1) {
			for (JVdto dto : dtoList) {
				if (dto.getTranCode().equals(TranCode.CSCREDIT) || dto.getTranCode().equals(TranCode.TRCREDIT)) {
					dto.setHomeAmount(dto.getHomeAmount().add(this.differentHomeAmount).setScale(2, RoundingMode.HALF_UP));
					break;
				}
			}

		}
		if (!this.differentHomeAmount.equals(BigDecimal.ZERO) && totalCreditHomeAmount.compareTo(totalDebitHomeAmount) == 1) {
			for (JVdto dto : dtoList) {
				if (dto.getTranCode().equals(TranCode.CSDEBIT) || dto.getTranCode().equals(TranCode.TRDEBIT)) {
					dto.setHomeAmount(dto.getHomeAmount().subtract(this.differentHomeAmount).setScale(2, RoundingMode.HALF_UP));
					break;
				}
			}

		}
	}

	public void updateDto() {
		try {
			// calculatTotalAmount();
			int comp;
			exchangeRate = dto.getExchangeRate().doubleValue();
			homeAmt = (dto.getAmount().multiply(dto.getExchangeRate())).setScale(2, RoundingMode.HALF_UP);
			dto.setHomeAmount(homeAmt);
			/*
			 * dto.setHomeAmount(homeAmt); calculateDifferentAmount();
			 * calculatTotalAmount(); // calculateDifferentAmount(); // comp =
			 * differentHomeAmount.compareTo(differentAmount); if
			 * (totalCreditHomeAmount.equals(totalDebitHomeAmount)) {
			 * dto.setHomeAmount(homeAmt);
			 * 
			 * } else {
			 * 
			 * comp = differentHomeAmount.compareTo(differentAmount); if (comp
			 * == 0) { dto.setHomeAmount(homeAmt); } else if (comp == 1) {
			 * dto.setHomeAmount(homeAmt.subtract(differentHomeAmount)); } else
			 * { dto.setHomeAmount(homeAmt.add(differentHomeAmount)); } }
			 */
			// adjustedAmount();
			dtoList.set(dtoIndex, dto);

			calculatTotalAmount();
			calculateDifferentAmount();

			setConfirmEdit("New");
			createNewDto();
		} catch (SystemException ex) {
			handleSysException(ex);
		}
	}

	public String deleteDto(JVdto dto) {
		try {
			dtoList.remove(dto);
			calculatTotalAmount();
			calculateDifferentAmount();
			createNewDto();
		} catch (SystemException ex) {
			handleSysException(ex);
		}
		return null;
	}

	private boolean validateDtoList() {
		BigDecimal totalDebit = BigDecimal.ZERO;
		BigDecimal totalCredit = BigDecimal.ZERO;
		if (cur != null) {
			for (JVdto dto : dtoList) {
				if (dto.getTranCode().equals(TranCode.TRCREDIT)) {
					totalCredit = totalCredit.add(dto.getAmount());
				} else if (dto.getTranCode().equals(TranCode.TRDEBIT)) {
					totalDebit = totalDebit.add(dto.getAmount());
				}
			}
			totalCredit.doubleValue();
			totalCredit.setScale(4);
			totalDebit.setScale(4);
			if (totalCredit.compareTo(totalDebit) == 0
					&& dtoList.stream().allMatch(dto -> DateUtils.resetStartDate(dtoList.get(0).getSettlementDate()).equals(DateUtils.resetStartDate(dto.getSettlementDate())))) {
				return true;
			}
		}
		addErrorMessage(null, MessageId.AMOUNT_DATE_INBALANCE);
		return false;
	}

	public void authenticatePage() {
		if (isEditUser()) {

			if (authenticate(this.editPassword)) {
				PrimeFaces.current().executeScript("PF('passwordDialog').hide();");
				if (!isEdit) {
					adjustedAmount();
					String voucherNo = tlfService.addNewTlfByDto(dtoList);
					addInfoMessage(null, MessageId.INSERT_SUCCESS, voucherNo);
					rebindData();
					createNewDto();
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

	public boolean validate() {
		boolean valid = true;

		Date budgetEndDate = BusinessUtil.getBudgetEndDate();
		Date budgetStartDate = BusinessUtil.getBudgetStartDate();
		// nextYear = DateUtils.plusYears(budgetEndDate, 1);
		if (!isValidVoucherDate()) {
			valid = false;
			addInfoMessage(null, MessageId.VALID_VOUCHER_DATE, DateUtils.formatDateToString(budgetStartDate), DateUtils.formatDateToString(budgetEndDate));
		}
		return valid;
	}

	public boolean isValidVoucherDate() {
		boolean validate = true;
		if (dto.getSettlementDate().before(minDate) && dto.getSettlementDate().equals(minDate)) {
			validate = false;
		}
		return validate;
	}

	public void setCreateNew(boolean createNew) {
		this.createNew = createNew;
	}

	public boolean isCreateNew() {
		return createNew;
	}

	public List<Currency> getCurs() {
		return currencyService.findAllCurrency();
	}

	public void returnCcoa(SelectEvent event) {
		CurrencyChartOfAccount ccoa = (CurrencyChartOfAccount) event.getObject();
		dto.setCcoa(ccoa);
	}

	public JVdto getDto() {
		return dto;
	}

	public void setDto(JVdto dto) {
		this.dto = dto;
	}

	public List<JVdto> getDtoList() {
		return dtoList;
	}

	public void setDtoList(List<JVdto> dtoList) {
		this.dtoList = dtoList;
	}

	public List<TranCode> getCodes() {
		List<TranCode> list = new ArrayList<TranCode>();
		list.add(TranCode.TRCREDIT);
		list.add(TranCode.TRDEBIT);
		return list;
	}

	public Currency getCur() {
		return cur;
	}

	public void setCur(Currency cur) {
		this.cur = cur;
	}

	public List<JVdto> filteredList;

	public List<JVdto> getFilteredList() {
		return filteredList;
	}

	public void setFilteredList(List<JVdto> filteredList) {
		this.filteredList = filteredList;
	}

	public void selectCCOAAccountCode() {
		selectCCOAAccountCode(dto.getCur());
	}

	public boolean isCurDisabled() {
		return dtoList.size() != 0 ? true : false;
	}

	public boolean isNarrationDisabled() {
		if (confirmEdit.equals("Edit")) {
			return false;
		}
		return dtoList.size() != 0 ? true : false;
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

	public BigDecimal getTotalDebitAmount() {
		return totalDebitAmount;
	}

	public void setTotalDebitAmount(BigDecimal totalDebitAmount) {
		this.totalDebitAmount = totalDebitAmount;
	}

	public BigDecimal getTotalDebitHomeAmount() {
		return totalDebitHomeAmount;
	}

	public void setTotalDebitHomeAmount(BigDecimal totalDebitHomeAmount) {
		this.totalDebitHomeAmount = totalDebitHomeAmount;
	}

	public BigDecimal getTotalCreditAmount() {
		return totalCreditAmount;
	}

	public void setTotalCreditAmount(BigDecimal totalCreditAmount) {
		this.totalCreditAmount = totalCreditAmount;
	}

	public BigDecimal getTotalCreditHomeAmount() {
		return totalCreditHomeAmount;
	}

	public void setTotalCreditHomeAmount(BigDecimal totalCreditHomeAmount) {
		this.totalCreditHomeAmount = totalCreditHomeAmount;
	}

	public BigDecimal getDifferentAmount() {
		return differentAmount;
	}

	public void setDifferentAmount(BigDecimal differentAmount) {
		this.differentAmount = differentAmount;
	}

	public BigDecimal getDifferentHomeAmount() {
		return differentHomeAmount;
	}

	public void setDifferentHomeAmount(BigDecimal differentHomeAmount) {
		this.differentHomeAmount = differentHomeAmount;
	}

	public String getConfirmEdit() {
		return confirmEdit;
	}

	public void setConfirmEdit(String confirmEdit) {
		this.confirmEdit = confirmEdit;
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
