// package org.ace.accounting.system.tlf;
//
// import java.math.BigDecimal;
// import java.util.ArrayList;
// import java.util.Calendar;
// import java.util.Date;
// im

// ing.common.BasicEntity;
// import org.ace.accounting.process.interfaces.IUserProcessService;
// import org.ace.accounting.system.branch.Branch;
// import org.ace.accounting.system.branch.service.interfaces.IBranchService;
// import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
// import org.ace.accounting.system.coasetup.COASetup;
// import org.ace.accounting.system.currency.Currency;
// import
// org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
// import org.ace.accounting.system.tlf.service.interfaces.ITLFService;
// import org.ace.accounting.system.trantype.TranCode;
// import org.ace.accounting.system.trantype.TranType;
// import
// org.ace.accounting.system.trantype.service.interfaces.ITranTypeService;
// import org.ace.accounting.user.User;
// import org.ace.java.component.service.interfaces.IDataRepService;
// import org.springframework.beans.factory.BeanFactory;
// import org.springframework.context.support.ClassPathXmlApplicationContext;
//
// public class TLFGenerator {
//
// @SuppressWarnings({ "unchecked", "resource" })
// public static void main(String[] args) {
//
// BeanFactory factory = new ClassPathXmlApplicationContext("spring-beans.xml");
// IUserProcessService userProcessService = (IUserProcessService)
// factory.getBean("UserProcessService");
// // IDataRepService<TLF> dataRepService = (IDataRepService<TLF>)
// // factory.getBean("DataRepService");
// ITLFService tlfService = (ITLFService) factory.getBean("TLFService");
// IBranchService branchService = (IBranchService)
// factory.getBean("BranchService");
// ITranTypeService tranTypeService = (ITranTypeService)
// factory.getBean("TranTypeService");
// IDataRepService<CurrencyChartOfAccount> ccoaService =
// (IDataRepService<CurrencyChartOfAccount>) factory.getBean("DataRepService");
// IDataRepService<User> userService = (IDataRepService<User>)
// factory.getBean("DataRepService");
// ICurrencyService currencyService = (ICurrencyService)
// factory.getBean("CurrencyService");
//
// userProcessService.registerUser(userService.findById(User.class,
// "ISSYS001000000000104122015"));
//
// BigDecimal homeExchangeRate = new BigDecimal(1);
//
// Currency currency = currencyService.findCurrencyByCurrencyCode("MMK");
// Branch branch = (Branch) branchService.findBranchByBranchCode("001");
//
// String referenceType = null;
// // BasicEntity basicEntity = new BasicEntity();
// // Calendar cal = Calendar.getInstance();
// // cal.set(Calendar.DAY_OF_MONTH, 3);
// // basicEntity.setCreatedDate(cal.getTime());
// // basicEntity.setCreatedUserId(createdUserId);
//
// boolean reverse = false;
// boolean paid = true;
//
// Calendar cal = Calendar.getInstance();
// cal.setTime(new Date());
// // cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
//
// Date settlementDate = cal.getTime();
//
// for (int x = 0; x < 50; x++) {
// String narration = "generated : " + x;
// String voucherNo = tlfService.getVoucherNo();
// BigDecimal debitAmount = new BigDecimal(5000);
// BigDecimal homeAmount = debitAmount.multiply(homeExchangeRate);
// BigDecimal localAmount = debitAmount;
// // if voucherNo is null show error msg
// List<TLF> tlfList = new ArrayList<TLF>();
//
// // TranCode tranCode = TranCode.TRCREDIT;
// TranCode tranCode = TranCode.CSDEBIT;
// CurrencyChartOfAccount ccoa =
// ccoaService.findById(CurrencyChartOfAccount.class, "1051");
// TranType tranType = tranTypeService.findByTransCode(tranCode);
//
// TLF tlf = new TLF(ccoa, voucherNo, homeAmount, localAmount, narration,
// tranType, currency, homeExchangeRate, branch, reverse, paid, referenceType,
// settlementDate);
// tlfList.add(tlf);
//
// COASetup coaSetup = tlfService.findCOABy("CASH", currency, branch);
// tranCode = TranCode.CSCREDIT;
//
// // only from journal voucher
// // ccoa = ccoaService.findById(CurrencyChartOfAccount.class,
// // "1026");
// // tranCode = TranCode.TRDEBIT;
// tranType = tranTypeService.findByTransCode(tranCode);
// tlf = new TLF(coaSetup.getCcoa()// ccoa
// , voucherNo, homeAmount, localAmount, narration, tranType, currency,
// homeExchangeRate, branch, reverse, paid, referenceType, settlementDate);
// tlfList.add(tlf);
//
// tlfService.addVoucher(tlfList);
// }
// System.out.println("DONE");
// }
//
// }
