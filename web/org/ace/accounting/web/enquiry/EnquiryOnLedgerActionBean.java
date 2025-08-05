package org.ace.accounting.web.enquiry;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.ace.accounting.dto.CoaDialogCriteriaDto;
import org.ace.accounting.dto.EnquiryLedgerDto;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.accounting.user.User;
import org.ace.java.web.common.BaseBean;
import org.ace.java.web.common.ParamId;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "EnquiryOnLedgerActionBean")
@ViewScoped
public class EnquiryOnLedgerActionBean extends BaseBean {
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

  private EnquiryLedgerDto dto;
  private List<EnquiryLedgerDto> dtoList;
  private List<Branch> branchList;
  private List<Currency> currencyList;
  private boolean isBranchDisable;

  @PostConstruct
  public void init() {
    dto = new EnquiryLedgerDto();
    User user = userProcessService.getLoginUser();
    if (user.isAdmin()) {
      isBranchDisable = false;
    } else {
      dto.setBranchid(user.getBranch().getId());
      isBranchDisable = true;
    }

    branchList = branchService.findAllBranch();
    currencyList = currencyService.findAllCurrency();

  }

  public void search() {
    dtoList = ccoaService.findAllEnquiryLedger(dto);
  }

  public EnquiryLedgerDto getDto() {
    return dto;
  }

  public List<EnquiryLedgerDto> getDtoList() {
    return dtoList;
  }

  public void returnCoa(SelectEvent event) {
    ChartOfAccount coa = (ChartOfAccount) event.getObject();
    dto.setCoa(coa);
    dto.setCoaid(coa.getId());
  }

  public List<Currency> getCurrencyList() {
    return currencyList;
  }

  public List<Branch> getBranchList() {
    return branchList;

  }

  public boolean isBranchDisable() {
    return isBranchDisable;
  }

  public void openCoaDialog() {
    CoaDialogCriteriaDto dto = new CoaDialogCriteriaDto();
    putParam(ParamId.COA_DIALOG_CRITERIA_DATA, dto);
    selectCoa();
  }
}
