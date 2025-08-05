// package org.ace.accounting.system.migrate;
//
// import java.util.ArrayList;
// import java.util.List;
//
// import org.ace.accounting.system.branch.Branch;
// import org.ace.accounting.system.branch.service.interfaces.IBranchService;
// import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
// import
// org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
// import org.ace.accounting.system.chartaccount.service.interfaces.ICoaService;
// import org.ace.accounting.system.coasetup.COASetup;
// import org.ace.accounting.system.currency.Currency;
// import
// org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
// import org.ace.java.component.service.interfaces.IDataRepService;
// import org.springframework.beans.factory.BeanFactory;
// import org.springframework.context.ApplicationContext;
// import org.springframework.context.support.ClassPathXmlApplicationContext;
//
////
//// NOT FOR PRACTICAL USE ,, USE THIS ONLY ONE TIME FOR MIGRATION
//// block idinterceptor class in coasetup to make this class work
////
// public class COASetupMigrate {
//
// public static void main(String[] args) {
// COASetupMigrate setUp = new COASetupMigrate();
// setUp.add();
// }
//
// @SuppressWarnings("unchecked")
// private void add() {
// List<DataBean> list = new ArrayList<COASetupMigrate.DataBean>();
// createList(list);
//
// @SuppressWarnings("resource")
// ApplicationContext context = new
// ClassPathXmlApplicationContext("spring-beans.xml");
// BeanFactory factory = context;
// IDataRepService<COASetup> dataRepService = (IDataRepService<COASetup>)
// factory.getBean("DataRepService");
// IBranchService branchService = (IBranchService)
// factory.getBean("BranchService");
// ICcoaService ccoaService = (ICcoaService) factory.getBean("CcoaService");
// ICoaService coaService = (ICoaService) factory.getBean("CoaService");
// ICurrencyService currencyService = (ICurrencyService)
// factory.getBean("CurrencyService");
//
// for (DataBean b : list) {
// COASetup coaSetup = new COASetup();
// coaSetup.setAcName(b.getAcName());
//
// Branch branch = branchService.findBranchByBranchCode(b.getBranchCode());
// coaSetup.setBranch(branch);
// CurrencyChartOfAccount ccoa;
// if (!b.getAcCode().equals(" ")) {
// String coaId = coaService.findCoaByAcCode(b.getAcCode()).getId();
// String currencyId =
// currencyService.findCurrencyByCurrencyCode(b.getCurrencyCode()).getId();
// String branchId =
// branchService.findBranchByBranchCode(b.getBranchCode()).getId();
//
// ccoa = ccoaService.findSpecificCCOA(coaId, currencyId, branchId, null);
// } else {
// ccoa = null;
// }
// coaSetup.setCcoa(ccoa);
//
// Currency cur =
// currencyService.findCurrencyByCurrencyCode(b.getCurrencyCode());
// coaSetup.setCurrency(cur);
// dataRepService.insert(coaSetup);
//
// }
//
// }
//
// private void createList(List<DataBean> list) {
// list.add(new DataBean("CARGO_STAMP_FEES", "LMD015", "001", "MMK"));
// list.add(new DataBean("CARGO_STAMP_FEES", "LMD015", "001", "USD"));
// list.add(new DataBean("CARGO_STAMP_FEES", "LMD015", "002", "MMK"));
// list.add(new DataBean("CARGO_STAMP_FEES", "LMD015", "002", "USD"));
// list.add(new DataBean("CARGO_STAMP_FEES", "LMD015", "003", "MMK"));
// list.add(new DataBean("CARGO_STAMP_FEES", "LMD015", "003", "USD"));
// list.add(new DataBean("CARGO_STAMP_FEES", "LMD015", "004", "MMK"));
// list.add(new DataBean("CARGO_STAMP_FEES", "LMD015", "004", "USD"));
// list.add(new DataBean("CASH", "AAC001", "001", "MMK"));
// list.add(new DataBean("CASH", "AAC003", "001", "SGD"));
// list.add(new DataBean("CASH", "AAC002", "001", "USD"));
// list.add(new DataBean("CASH", "AAC001", "002", "MMK"));
// list.add(new DataBean("CASH", "AAC003", "002", "SGD"));
// list.add(new DataBean("CASH", "AAC002", "002", "USD"));
// list.add(new DataBean("CASH", "AAC001", "003", "MMK"));
// list.add(new DataBean("CASH", "AAC001", "003", "USD"));
// list.add(new DataBean("CASH", "AAC001", "004", "MMK"));
// list.add(new DataBean("CASH", "AAC003", "004", "SGD"));
// list.add(new DataBean("CASH", "AAC002", "004", "USD"));
// list.add(new DataBean("CASH_IN_SAFE_AGENT_COMMISSION", "EEF003", "001",
// "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_AGENT_COMMISSION", "EEF003", "001",
// "USD"));
// list.add(new DataBean("CASH_IN_SAFE_AGENT_COMMISSION", "EEF003", "002",
// "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_AGENT_COMMISSION", "EEF003", "002",
// "USD"));
// list.add(new DataBean("CASH_IN_SAFE_AGENT_COMMISSION", "EEF003", "003",
// "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_AGENT_COMMISSION", "EEF003", "003",
// "USD"));
// list.add(new DataBean("CASH_IN_SAFE_AGENT_COMMISSION", "EEF003", "004",
// "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_AGENT_COMMISSION", "EEF003", "004",
// "USD"));
// list.add(new DataBean("CASH_IN_SAFE_AGENT_PAYABLE", "LPB002", "001", "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_AGENT_PAYABLE", "LPB002", "001", "USD"));
// list.add(new DataBean("CASH_IN_SAFE_AGENT_PAYABLE", "LPB002", "002", "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_AGENT_PAYABLE", "LPB002", "002", "USD"));
// list.add(new DataBean("CASH_IN_SAFE_AGENT_PAYABLE", "LPB002", "003", "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_AGENT_PAYABLE", "LPB002", "003", "USD"));
// list.add(new DataBean("CASH_IN_SAFE_AGENT_PAYABLE", "LPB002", "004", "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_AGENT_PAYABLE", "LPB002", "004", "USD"));
// list.add(new DataBean("CASH_IN_SAFE_PAYMENT_ORDER", "AUK002", "001", "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_PAYMENT_ORDER", "AUK002", "001", "USD"));
// list.add(new DataBean("CASH_IN_SAFE_PAYMENT_ORDER", "AUK002", "002", "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_PAYMENT_ORDER", "AUK002", "002", "USD"));
// list.add(new DataBean("CASH_IN_SAFE_PAYMENT_ORDER", "AUK002", "003", "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_PAYMENT_ORDER", "AUK002", "003", "USD"));
// list.add(new DataBean("CASH_IN_SAFE_PAYMENT_ORDER", "AUK002", "004", "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_PAYMENT_ORDER", "AUK002", "004", "USD"));
// list.add(new DataBean("CASH_IN_SAFE_PREMIUM", "IAG003", "001", "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_PREMIUM", "IAG003", "001", "USD"));
// list.add(new DataBean("CASH_IN_SAFE_PREMIUM", "IAG003", "002", "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_PREMIUM", "IAG003", "002", "USD"));
// list.add(new DataBean("CASH_IN_SAFE_PREMIUM", "IAG003", "003", "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_PREMIUM", "IAG003", "003", "USD"));
// list.add(new DataBean("CASH_IN_SAFE_PREMIUM", "IAG003", "004", "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_PREMIUM", "IAG003", "004", "USD"));
// list.add(new DataBean("CASH_IN_SAFE_SERVICE_CHARGES", "IAC002", "001",
// "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_SERVICE_CHARGES", "IAC002", "001",
// "USD"));
// list.add(new DataBean("CASH_IN_SAFE_SERVICE_CHARGES", "IAC002", "002",
// "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_SERVICE_CHARGES", "IAC002", "002",
// "USD"));
// list.add(new DataBean("CASH_IN_SAFE_SERVICE_CHARGES", "IAC002", "003",
// "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_SERVICE_CHARGES", "IAC002", "003",
// "USD"));
// list.add(new DataBean("CASH_IN_SAFE_SERVICE_CHARGES", "IAC002", "004",
// "MMK"));
// list.add(new DataBean("CASH_IN_SAFE_SERVICE_CHARGES", "IAC002", "004",
// "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_AGENT_COMMISSION", "EEF002", "001",
// "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_AGENT_COMMISSION", "EEF002", "001",
// "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_AGENT_COMMISSION", "EEF002", "002",
// "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_AGENT_COMMISSION", "EEF002", "002",
// "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_AGENT_COMMISSION", "EEF002", "003",
// "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_AGENT_COMMISSION", "EEF002", "003",
// "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_AGENT_COMMISSION", "EEF002", "004",
// "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_AGENT_COMMISSION", "EEF002", "004",
// "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_AGENT_PAYABLE", "LPB002", "001",
// "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_AGENT_PAYABLE", "LPB002", "001",
// "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_AGENT_PAYABLE", "LPB002", "002",
// "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_AGENT_PAYABLE", "LPB002", "002",
// "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_AGENT_PAYABLE", "LPB002", "003",
// "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_AGENT_PAYABLE", "LPB002", "003",
// "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_AGENT_PAYABLE", "LPB002", "004",
// "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_AGENT_PAYABLE", "LPB002", "004",
// "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_PAYMENT_ORDER", "AUK002", "001",
// "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_PAYMENT_ORDER", "AUK002", "001",
// "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_PAYMENT_ORDER", "AUK002", "002",
// "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_PAYMENT_ORDER", "AUK002", "002",
// "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_PAYMENT_ORDER", "AUK002", "003",
// "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_PAYMENT_ORDER", "AUK002", "003",
// "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_PAYMENT_ORDER", "AUK002", "004",
// "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_PAYMENT_ORDER", "AUK002", "004",
// "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_PREMIUM", "IAG005", "001", "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_PREMIUM", "IAG005", "001", "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_PREMIUM", "IAG005", "002", "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_PREMIUM", "IAG005", "002", "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_PREMIUM", "IAG005", "003", "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_PREMIUM", "IAG005", "003", "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_PREMIUM", "IAG005", "004", "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_PREMIUM", "IAG005", "004", "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_SERVICE_CHARGES", "IAC002", "001",
// "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_SERVICE_CHARGES", "IAC002", "001",
// "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_SERVICE_CHARGES", "IAC002", "002",
// "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_SERVICE_CHARGES", "IAC002", "002",
// "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_SERVICE_CHARGES", "IAC002", "003",
// "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_SERVICE_CHARGES", "IAC002", "003",
// "USD"));
// list.add(new DataBean("CASH_IN_TRANSIT_SERVICE_CHARGES", "IAC002", "004",
// "MMK"));
// list.add(new DataBean("CASH_IN_TRANSIT_SERVICE_CHARGES", "IAC002", "004",
// "USD"));
// list.add(new DataBean("CHEQUE", " ", "001", "MMK"));
// list.add(new DataBean("CHEQUE", " ", "001", "USD"));
// list.add(new DataBean("CHEQUE", "AEM003", "002", "MMK"));
// list.add(new DataBean("CHEQUE", "AEM003", "002", "USD"));
// list.add(new DataBean("CHEQUE", " ", "003", "MMK"));
// list.add(new DataBean("CHEQUE", " ", "003", "USD"));
// list.add(new DataBean("CHEQUE", " ", "004", "MMK"));
// list.add(new DataBean("CHEQUE", " ", "004", "USD"));
// list.add(new DataBean("CHIT_SUNDRY", "IAG005", "001", "MMK"));
// list.add(new DataBean("CHIT_SUNDRY", "IAG005", "001", "USD"));
// list.add(new DataBean("CHIT_SUNDRY", "IAG005", "002", "MMK"));
// list.add(new DataBean("CHIT_SUNDRY", "IAG005", "002", "USD"));
// list.add(new DataBean("CHIT_SUNDRY", "IAG005", "003", "MMK"));
// list.add(new DataBean("CHIT_SUNDRY", "IAG005", "003", "USD"));
// list.add(new DataBean("CHIT_SUNDRY", "IAG005", "004", "MMK"));
// list.add(new DataBean("CHIT_SUNDRY", "IAG005", "004", "USD"));
// list.add(new DataBean("FIDELITY_AGENT_COMMISSION", "EEF004", "001", "MMK"));
// list.add(new DataBean("FIDELITY_AGENT_COMMISSION", "EEF004", "001", "USD"));
// list.add(new DataBean("FIDELITY_AGENT_COMMISSION", "EEF004", "002", "MMK"));
// list.add(new DataBean("FIDELITY_AGENT_COMMISSION", "EEF004", "002", "USD"));
// list.add(new DataBean("FIDELITY_AGENT_COMMISSION", "EEF004", "003", "MMK"));
// list.add(new DataBean("FIDELITY_AGENT_COMMISSION", "EEF004", "003", "USD"));
// list.add(new DataBean("FIDELITY_AGENT_COMMISSION", "EEF004", "004", "MMK"));
// list.add(new DataBean("FIDELITY_AGENT_COMMISSION", "EEF004", "004", "USD"));
// list.add(new DataBean("FIDELITY_AGENT_PAYABLE", "LPB002", "001", "MMK"));
// list.add(new DataBean("FIDELITY_AGENT_PAYABLE", "LPB002", "001", "USD"));
// list.add(new DataBean("FIDELITY_AGENT_PAYABLE", "LPB002", "002", "MMK"));
// list.add(new DataBean("FIDELITY_AGENT_PAYABLE", "LPB002", "002", "USD"));
// list.add(new DataBean("FIDELITY_AGENT_PAYABLE", "LPB002", "003", "MMK"));
// list.add(new DataBean("FIDELITY_AGENT_PAYABLE", "LPB002", "003", "USD"));
// list.add(new DataBean("FIDELITY_AGENT_PAYABLE", "LPB002", "004", "MMK"));
// list.add(new DataBean("FIDELITY_AGENT_PAYABLE", "LPB002", "004", "USD"));
// list.add(new DataBean("FIDELITY_PAYMENT_ORDER", "AUK002", "001", "MMK"));
// list.add(new DataBean("FIDELITY_PAYMENT_ORDER", "AUK002", "001", "USD"));
// list.add(new DataBean("FIDELITY_PAYMENT_ORDER", "AUK002", "002", "MMK"));
// list.add(new DataBean("FIDELITY_PAYMENT_ORDER", "AUK002", "002", "USD"));
// list.add(new DataBean("FIDELITY_PAYMENT_ORDER", "AUK002", "003", "MMK"));
// list.add(new DataBean("FIDELITY_PAYMENT_ORDER", "AUK002", "003", "USD"));
// list.add(new DataBean("FIDELITY_PAYMENT_ORDER", "AUK002", "004", "MMK"));
// list.add(new DataBean("FIDELITY_PAYMENT_ORDER", "AUK002", "004", "USD"));
// list.add(new DataBean("FIDELITY_PREMIUM", "IAG004", "001", "MMK"));
// list.add(new DataBean("FIDELITY_PREMIUM", "IAG004", "001", "USD"));
// list.add(new DataBean("FIDELITY_PREMIUM", "IAG004", "002", "MMK"));
// list.add(new DataBean("FIDELITY_PREMIUM", "IAG004", "002", "USD"));
// list.add(new DataBean("FIDELITY_PREMIUM", "IAG004", "003", "MMK"));
// list.add(new DataBean("FIDELITY_PREMIUM", "IAG004", "003", "USD"));
// list.add(new DataBean("FIDELITY_PREMIUM", "IAG004", "004", "MMK"));
// list.add(new DataBean("FIDELITY_PREMIUM", "IAG004", "004", "USD"));
// list.add(new DataBean("FIDELITY_SERVICE_CHARGES", "IAC002", "001", "MMK"));
// list.add(new DataBean("FIDELITY_SERVICE_CHARGES", "IAC002", "001", "USD"));
// list.add(new DataBean("FIDELITY_SERVICE_CHARGES", "IAC002", "002", "MMK"));
// list.add(new DataBean("FIDELITY_SERVICE_CHARGES", "IAC002", "002", "USD"));
// list.add(new DataBean("FIDELITY_SERVICE_CHARGES", "IAC002", "003", "MMK"));
// list.add(new DataBean("FIDELITY_SERVICE_CHARGES", "IAC002", "003", "USD"));
// list.add(new DataBean("FIDELITY_SERVICE_CHARGES", "IAC002", "004", "MMK"));
// list.add(new DataBean("FIDELITY_SERVICE_CHARGES", "IAC002", "004", "USD"));
// list.add(new DataBean("FIRE_AGENT_COMMISSION", "EEF001", "001", "MMK"));
// list.add(new DataBean("FIRE_AGENT_COMMISSION", "EEF001", "001", "USD"));
// list.add(new DataBean("FIRE_AGENT_COMMISSION", "EEF001", "002", "MMK"));
// list.add(new DataBean("FIRE_AGENT_COMMISSION", "EEF001", "002", "USD"));
// list.add(new DataBean("FIRE_AGENT_COMMISSION", "EEF001", "003", "MMK"));
// list.add(new DataBean("FIRE_AGENT_COMMISSION", "EEF001", "003", "USD"));
// list.add(new DataBean("FIRE_AGENT_COMMISSION", "EEF001", "004", "MMK"));
// list.add(new DataBean("FIRE_AGENT_COMMISSION", "EEF001", "004", "USD"));
// list.add(new DataBean("FIRE_AGENT_PAYABLE", "LPB002", "001", "MMK"));
// list.add(new DataBean("FIRE_AGENT_PAYABLE", "LPB002", "001", "USD"));
// list.add(new DataBean("FIRE_AGENT_PAYABLE", "LPB002", "002", "MMK"));
// list.add(new DataBean("FIRE_AGENT_PAYABLE", "LPB002", "002", "USD"));
// list.add(new DataBean("FIRE_AGENT_PAYABLE", "LPB002", "003", "MMK"));
// list.add(new DataBean("FIRE_AGENT_PAYABLE", "LPB002", "003", "USD"));
// list.add(new DataBean("FIRE_AGENT_PAYABLE", "LPB002", "004", "MMK"));
// list.add(new DataBean("FIRE_AGENT_PAYABLE", "LPB002", "004", "USD"));
// list.add(new DataBean("FIRE_CLAIM", "EEB001", "001", "MMK"));
// list.add(new DataBean("FIRE_CLAIM", "EEB001", "001", "USD"));
// list.add(new DataBean("FIRE_CLAIM", "EEB001", "002", "MMK"));
// list.add(new DataBean("FIRE_CLAIM", "EEB001", "002", "USD"));
// list.add(new DataBean("FIRE_CLAIM", "EEB001", "003", "MMK"));
// list.add(new DataBean("FIRE_CLAIM", "EEB001", "003", "USD"));
// list.add(new DataBean("FIRE_CLAIM", "EEB001", "004", "MMK"));
// list.add(new DataBean("FIRE_CLAIM", "EEB001", "004", "USD"));
// list.add(new DataBean("FIRE_CO_AGENT_PAYABLE", "LPB004", "001", "MMK"));
// list.add(new DataBean("FIRE_CO_AGENT_PAYABLE", "LPB004", "001", "USD"));
// list.add(new DataBean("FIRE_CO_AGENT_PAYABLE", "LPB004", "002", "MMK"));
// list.add(new DataBean("FIRE_CO_AGENT_PAYABLE", "LPB004", "002", "USD"));
// list.add(new DataBean("FIRE_CO_AGENT_PAYABLE", "LPB004", "003", "MMK"));
// list.add(new DataBean("FIRE_CO_AGENT_PAYABLE", "LPB004", "003", "USD"));
// list.add(new DataBean("FIRE_CO_AGENT_PAYABLE", "LPB004", "004", "MMK"));
// list.add(new DataBean("FIRE_CO_AGENT_PAYABLE", "LPB004", "004", "USD"));
// list.add(new DataBean("FIRE_COINSURANCE_COMMISSION", "IAR002", "001",
// "MMK"));
// list.add(new DataBean("FIRE_COINSURANCE_COMMISSION", "IAR002", "001",
// "USD"));
// list.add(new DataBean("FIRE_COINSURANCE_COMMISSION", "IAR002", "002",
// "MMK"));
// list.add(new DataBean("FIRE_COINSURANCE_COMMISSION", "IAR002", "002",
// "USD"));
// list.add(new DataBean("FIRE_COINSURANCE_COMMISSION", "IAR002", "003",
// "MMK"));
// list.add(new DataBean("FIRE_COINSURANCE_COMMISSION", "IAR002", "003",
// "USD"));
// list.add(new DataBean("FIRE_COINSURANCE_COMMISSION", "IAR002", "004",
// "MMK"));
// list.add(new DataBean("FIRE_COINSURANCE_COMMISSION", "IAR002", "004",
// "USD"));
// list.add(new DataBean("FIRE_COINSURANCE_PREMIUM", "IAG002", "001", "MMK"));
// list.add(new DataBean("FIRE_COINSURANCE_PREMIUM", "IAG002", "001", "USD"));
// list.add(new DataBean("FIRE_COINSURANCE_PREMIUM", "IAG002", "002", "MMK"));
// list.add(new DataBean("FIRE_COINSURANCE_PREMIUM", "IAG002", "002", "USD"));
// list.add(new DataBean("FIRE_COINSURANCE_PREMIUM", "IAG002", "003", "MMK"));
// list.add(new DataBean("FIRE_COINSURANCE_PREMIUM", "IAG002", "003", "USD"));
// list.add(new DataBean("FIRE_COINSURANCE_PREMIUM", "IAG002", "004", "MMK"));
// list.add(new DataBean("FIRE_COINSURANCE_PREMIUM", "IAG002", "004", "USD"));
// list.add(new DataBean("FIRE_PAYMENT_ORDER", "AUK002", "001", "MMK"));
// list.add(new DataBean("FIRE_PAYMENT_ORDER", "AUK002", "001", "USD"));
// list.add(new DataBean("FIRE_PAYMENT_ORDER", "AUK002", "002", "MMK"));
// list.add(new DataBean("FIRE_PAYMENT_ORDER", "AUK002", "002", "USD"));
// list.add(new DataBean("FIRE_PAYMENT_ORDER", "AUK002", "003", "MMK"));
// list.add(new DataBean("FIRE_PAYMENT_ORDER", "AUK002", "003", "USD"));
// list.add(new DataBean("FIRE_PAYMENT_ORDER", "AUK002", "004", "MMK"));
// list.add(new DataBean("FIRE_PAYMENT_ORDER", "AUK002", "004", "USD"));
// list.add(new DataBean("FIRE_PREMIUM", "IAG001", "001", "MMK"));
// list.add(new DataBean("FIRE_PREMIUM", "IAG001", "001", "USD"));
// list.add(new DataBean("FIRE_PREMIUM", "IAG001", "002", "MMK"));
// list.add(new DataBean("FIRE_PREMIUM", "IAG001", "002", "USD"));
// list.add(new DataBean("FIRE_PREMIUM", "IAG001", "003", "MMK"));
// list.add(new DataBean("FIRE_PREMIUM", "IAG001", "003", "USD"));
// list.add(new DataBean("FIRE_PREMIUM", "IAG001", "004", "MMK"));
// list.add(new DataBean("FIRE_PREMIUM", "IAG001", "004", "USD"));
// list.add(new DataBean("FIRE_SERVICE_CHARGES", "IAC002", "001", "MMK"));
// list.add(new DataBean("FIRE_SERVICE_CHARGES", "IAC002", "001", "USD"));
// list.add(new DataBean("FIRE_SERVICE_CHARGES", "IAC002", "002", "MMK"));
// list.add(new DataBean("FIRE_SERVICE_CHARGES", "IAC002", "002", "USD"));
// list.add(new DataBean("FIRE_SERVICE_CHARGES", "IAC002", "003", "MMK"));
// list.add(new DataBean("FIRE_SERVICE_CHARGES", "IAC002", "003", "USD"));
// list.add(new DataBean("FIRE_SERVICE_CHARGES", "IAC002", "004", "MMK"));
// list.add(new DataBean("FIRE_SERVICE_CHARGES", "IAC002", "004", "USD"));
// list.add(new DataBean("FIRE_SUNDRY", "LMD002", "001", "MMK"));
// list.add(new DataBean("FIRE_SUNDRY", "LMD002", "001", "USD"));
// list.add(new DataBean("FIRE_SUNDRY", "LMD002", "002", "MMK"));
// list.add(new DataBean("FIRE_SUNDRY", "LMD002", "002", "USD"));
// list.add(new DataBean("FIRE_SUNDRY", "LMD002", "003", "MMK"));
// list.add(new DataBean("FIRE_SUNDRY", "LMD002", "003", "USD"));
// list.add(new DataBean("FIRE_SUNDRY", "LMD002", "004", "MMK"));
// list.add(new DataBean("FIRE_SUNDRY", "LMD002", "004", "USD"));
// list.add(new DataBean("GROUP_LIFE_PREMIUM", "IAL002", "001", "MMK"));
// list.add(new DataBean("GROUP_LIFE_PREMIUM", "IAL002", "001", "USD"));
// list.add(new DataBean("GROUP_LIFE_PREMIUM", "IAL002", "002", "MMK"));
// list.add(new DataBean("GROUP_LIFE_PREMIUM", "IAL002", "002", "USD"));
// list.add(new DataBean("GROUP_LIFE_PREMIUM", "IAL002", "003", "MMK"));
// list.add(new DataBean("GROUP_LIFE_PREMIUM", "IAL002", "003", "USD"));
// list.add(new DataBean("GROUP_LIFE_PREMIUM", "IAL002", "004", "MMK"));
// list.add(new DataBean("GROUP_LIFE_PREMIUM", "IAL002", "004", "USD"));
// list.add(new DataBean("HEALTH_AGENTCOMMISSION", "EEE003", "001", "MMK"));
// list.add(new DataBean("HEALTH_AGENTCOMMISSION", "EEE003", "001", "SGD"));
// list.add(new DataBean("HEALTH_AGENTCOMMISSION", "EEE003", "001", "USD"));
// list.add(new DataBean("HEALTH_AGENTCOMMISSION", "EEE003", "002", "MMK"));
// list.add(new DataBean("HEALTH_AGENTCOMMISSION", "EEE003", "002", "SGD"));
// list.add(new DataBean("HEALTH_AGENTCOMMISSION", "EEE003", "002", "USD"));
// list.add(new DataBean("HEALTH_AGENTCOMMISSION", "EEE003", "003", "MMK"));
// list.add(new DataBean("HEALTH_AGENTCOMMISSION", "EEE003", "003", "SGD"));
// list.add(new DataBean("HEALTH_AGENTCOMMISSION", "EEE003", "003", "USD"));
// list.add(new DataBean("HEALTH_AGENTCOMMISSION", "EEE003", "004", "MMK"));
// list.add(new DataBean("HEALTH_AGENTCOMMISSION", "EEE003", "004", "SGD"));
// list.add(new DataBean("HEALTH_AGENTCOMMISSION", "EEE003", "004", "USD"));
// list.add(new DataBean("HEALTH_CLAIM", "EEA008", "001", "MMK"));
// list.add(new DataBean("HEALTH_CLAIM", "EEA008", "001", "SGD"));
// list.add(new DataBean("HEALTH_CLAIM", "EEA008", "001", "USD"));
// list.add(new DataBean("HEALTH_CLAIM", "EEA008", "002", "MMK"));
// list.add(new DataBean("HEALTH_CLAIM", "EEA008", "002", "SGD"));
// list.add(new DataBean("HEALTH_CLAIM", "EEA008", "002", "USD"));
// list.add(new DataBean("HEALTH_CLAIM", "EEA008", "003", "MMK"));
// list.add(new DataBean("HEALTH_CLAIM", "EEA008", "003", "SGD"));
// list.add(new DataBean("HEALTH_CLAIM", "EEA008", "003", "USD"));
// list.add(new DataBean("HEALTH_CLAIM", "EEA008", "004", "MMK"));
// list.add(new DataBean("HEALTH_CLAIM", "EEA008", "004", "SGD"));
// list.add(new DataBean("HEALTH_CLAIM", "EEA008", "004", "USD"));
// list.add(new DataBean("HEALTH_PREMIUM_INCOME", "IAL005", "001", "MMK"));
// list.add(new DataBean("HEALTH_PREMIUM_INCOME", "IAL005", "001", "SGD"));
// list.add(new DataBean("HEALTH_PREMIUM_INCOME", "IAL005", "001", "USD"));
// list.add(new DataBean("HEALTH_PREMIUM_INCOME", "IAL005", "002", "MMK"));
// list.add(new DataBean("HEALTH_PREMIUM_INCOME", "IAL005", "002", "SGD"));
// list.add(new DataBean("HEALTH_PREMIUM_INCOME", "IAL005", "002", "USD"));
// list.add(new DataBean("HEALTH_PREMIUM_INCOME", "IAL005", "003", "MMK"));
// list.add(new DataBean("HEALTH_PREMIUM_INCOME", "IAL005", "003", "SGD"));
// list.add(new DataBean("HEALTH_PREMIUM_INCOME", "IAL005", "003", "USD"));
// list.add(new DataBean("HEALTH_PREMIUM_INCOME", "IAL005", "004", "MMK"));
// list.add(new DataBean("HEALTH_PREMIUM_INCOME", "IAL005", "004", "SGD"));
// list.add(new DataBean("HEALTH_PREMIUM_INCOME", "IAL005", "004", "USD"));
// list.add(new DataBean("INLAND_AGENT_COMMISSION", "EEF009", "001", "MMK"));
// list.add(new DataBean("INLAND_AGENT_COMMISSION", "EEF009", "002", "MMK"));
// list.add(new DataBean("INLAND_AGENT_COMMISSION", "EEF009", "003", "MMK"));
// list.add(new DataBean("INLAND_AGENT_COMMISSION", "EEF009", "004", "MMK"));
// list.add(new DataBean("INLAND_AGENT_PAYABLE", "LPB008", "001", "MMK"));
// list.add(new DataBean("INLAND_AGENT_PAYABLE", "LPB008", "002", "MMK"));
// list.add(new DataBean("INLAND_AGENT_PAYABLE", "LPB008", "003", "MMK"));
// list.add(new DataBean("INLAND_AGENT_PAYABLE", "LPB008", "004", "MMK"));
// list.add(new DataBean("INLAND_CLAIM_OUTSTANDING", "LPA008", "002", "MMK"));
// list.add(new DataBean("INLAND_CLAIM_OUTSTANDING", "LPA008", "003", "MMK"));
// list.add(new DataBean("INLAND_CLAIM_OUTSTANDING", "LPA008", "004", "MMK"));
// list.add(new DataBean("INLAND_PAYMENT_ORDER", "AUK008", "001", "MMK"));
// list.add(new DataBean("INLAND_PAYMENT_ORDER", "AUK008", "002", "MMK"));
// list.add(new DataBean("INLAND_PAYMENT_ORDER", "AUK008", "003", "MMK"));
// list.add(new DataBean("INLAND_PAYMENT_ORDER", "AUK008", "004", "MMK"));
// list.add(new DataBean("INLAND_PREMIUM", "IAH002", "001", "MMK"));
// list.add(new DataBean("INLAND_PREMIUM", "IAH002", "002", "MMK"));
// list.add(new DataBean("INLAND_PREMIUM", "IAH002", "003", "MMK"));
// list.add(new DataBean("INLAND_PREMIUM", "IAH002", "004", "MMK"));
// list.add(new DataBean("INLAND_SERVICE_CHARGES", "IAC009", "001", "MMK"));
// list.add(new DataBean("INLAND_SERVICE_CHARGES", "IAC009", "002", "MMK"));
// list.add(new DataBean("INLAND_SERVICE_CHARGES", "IAC009", "003", "MMK"));
// list.add(new DataBean("INLAND_SERVICE_CHARGES", "IAC009", "004", "MMK"));
// list.add(new DataBean("INTER_BRANCH_HO", "ASH001", "001", "MMK"));
// list.add(new DataBean("INTER_BRANCH_HO", "ASH001", "001", "USD"));
// list.add(new DataBean("INTER_BRANCH_HO", "ASH001", "002", "MMK"));
// list.add(new DataBean("INTER_BRANCH_HO", "ASH001", "002", "USD"));
// list.add(new DataBean("INTER_BRANCH_HO", "ASH001", "003", "MMK"));
// list.add(new DataBean("INTER_BRANCH_HO", "ASH001", "003", "USD"));
// list.add(new DataBean("INTER_BRANCH_HO", "ASH001", "004", "MMK"));
// list.add(new DataBean("INTER_BRANCH_HO", "ASH001", "004", "USD"));
// list.add(new DataBean("LIFE_AGENT_COMMISSION", "EEE001", "001", "MMK"));
// list.add(new DataBean("LIFE_AGENT_COMMISSION", "EEE001", "001", "USD"));
// list.add(new DataBean("LIFE_AGENT_COMMISSION", "EEE001", "002", "MMK"));
// list.add(new DataBean("LIFE_AGENT_COMMISSION", "EEE001", "002", "USD"));
// list.add(new DataBean("LIFE_AGENT_COMMISSION", "EEE001", "003", "MMK"));
// list.add(new DataBean("LIFE_AGENT_COMMISSION", "EEE001", "003", "USD"));
// list.add(new DataBean("LIFE_AGENT_COMMISSION", "EEE001", "004", "MMK"));
// list.add(new DataBean("LIFE_AGENT_COMMISSION", "EEE001", "004", "USD"));
// list.add(new DataBean("LIFE_AGENT_PAYABLE", "LPB001", "001", "MMK"));
// list.add(new DataBean("LIFE_AGENT_PAYABLE", "LPB001", "001", "USD"));
// list.add(new DataBean("LIFE_AGENT_PAYABLE", "LPB001", "002", "MMK"));
// list.add(new DataBean("LIFE_AGENT_PAYABLE", "LPB001", "002", "USD"));
// list.add(new DataBean("LIFE_AGENT_PAYABLE", "LPB001", "003", "MMK"));
// list.add(new DataBean("LIFE_AGENT_PAYABLE", "LPB001", "003", "USD"));
// list.add(new DataBean("LIFE_AGENT_PAYABLE", "LPB001", "004", "MMK"));
// list.add(new DataBean("LIFE_AGENT_PAYABLE", "LPB001", "004", "USD"));
// list.add(new DataBean("LIFE_CLAIM", "EEL001", "001", "MMK"));
// list.add(new DataBean("LIFE_CLAIM", "EEL001", "001", "USD"));
// list.add(new DataBean("LIFE_CLAIM", "EEL001", "002", "MMK"));
// list.add(new DataBean("LIFE_CLAIM", "EEL001", "002", "USD"));
// list.add(new DataBean("LIFE_CLAIM", "EEL001", "003", "MMK"));
// list.add(new DataBean("LIFE_CLAIM", "EEL001", "003", "USD"));
// list.add(new DataBean("LIFE_CLAIM", "EEL001", "004", "MMK"));
// list.add(new DataBean("LIFE_CLAIM", "EEL001", "004", "USD"));
// list.add(new DataBean("LIFE_COINSURANCE_COMMISSION", "IAR001", "001",
// "MMK"));
// list.add(new DataBean("LIFE_COINSURANCE_COMMISSION", "IAR001", "001",
// "USD"));
// list.add(new DataBean("LIFE_COINSURANCE_COMMISSION", "IAR001", "002",
// "MMK"));
// list.add(new DataBean("LIFE_COINSURANCE_COMMISSION", "IAR001", "002",
// "USD"));
// list.add(new DataBean("LIFE_COINSURANCE_COMMISSION", "IAR001", "003",
// "MMK"));
// list.add(new DataBean("LIFE_COINSURANCE_COMMISSION", "IAR001", "003",
// "USD"));
// list.add(new DataBean("LIFE_COINSURANCE_COMMISSION", "IAR001", "004",
// "MMK"));
// list.add(new DataBean("LIFE_COINSURANCE_COMMISSION", "IAR001", "004",
// "USD"));
// list.add(new DataBean("LIFE_COINSURANCE_PREMIUM", "IAL002", "001", "MMK"));
// list.add(new DataBean("LIFE_COINSURANCE_PREMIUM", "IAL002", "001", "USD"));
// list.add(new DataBean("LIFE_COINSURANCE_PREMIUM", "IAL002", "002", "MMK"));
// list.add(new DataBean("LIFE_COINSURANCE_PREMIUM", "IAL002", "002", "USD"));
// list.add(new DataBean("LIFE_COINSURANCE_PREMIUM", "IAL002", "003", "MMK"));
// list.add(new DataBean("LIFE_COINSURANCE_PREMIUM", "IAL002", "003", "USD"));
// list.add(new DataBean("LIFE_COINSURANCE_PREMIUM", "IAL002", "004", "MMK"));
// list.add(new DataBean("LIFE_COINSURANCE_PREMIUM", "IAL002", "004", "USD"));
// list.add(new DataBean("LIFE_DISABILITY_CLAIM", "EEA004", "001", "MMK"));
// list.add(new DataBean("LIFE_DISABILITY_CLAIM", "EEA004", "001", "USD"));
// list.add(new DataBean("LIFE_DISABILITY_CLAIM", "EEA004", "002", "MMK"));
// list.add(new DataBean("LIFE_DISABILITY_CLAIM", "EEA004", "002", "USD"));
// list.add(new DataBean("LIFE_DISABILITY_CLAIM", "EEA004", "003", "MMK"));
// list.add(new DataBean("LIFE_DISABILITY_CLAIM", "EEA004", "003", "USD"));
// list.add(new DataBean("LIFE_DISABILITY_CLAIM", "EEA004", "004", "MMK"));
// list.add(new DataBean("LIFE_DISABILITY_CLAIM", "EEA004", "004", "USD"));
// list.add(new DataBean("LIFE_PAYMENT_ORDER", "AUK001", "001", "MMK"));
// list.add(new DataBean("LIFE_PAYMENT_ORDER", "AUK001", "001", "USD"));
// list.add(new DataBean("LIFE_PAYMENT_ORDER", "AUK001", "002", "MMK"));
// list.add(new DataBean("LIFE_PAYMENT_ORDER", "AUK001", "002", "USD"));
// list.add(new DataBean("LIFE_PAYMENT_ORDER", "AUK001", "003", "MMK"));
// list.add(new DataBean("LIFE_PAYMENT_ORDER", "AUK001", "003", "USD"));
// list.add(new DataBean("LIFE_PAYMENT_ORDER", "AUK001", "004", "MMK"));
// list.add(new DataBean("LIFE_PAYMENT_ORDER", "AUK001", "004", "USD"));
// list.add(new DataBean("LIFE_SERVICE_CHARGES", "IAC001", "001", "MMK"));
// list.add(new DataBean("LIFE_SERVICE_CHARGES", "IAC001", "001", "USD"));
// list.add(new DataBean("LIFE_SERVICE_CHARGES", "IAC001", "002", "MMK"));
// list.add(new DataBean("LIFE_SERVICE_CHARGES", "IAC001", "002", "USD"));
// list.add(new DataBean("LIFE_SERVICE_CHARGES", "IAC001", "003", "MMK"));
// list.add(new DataBean("LIFE_SERVICE_CHARGES", "IAC001", "003", "USD"));
// list.add(new DataBean("LIFE_SERVICE_CHARGES", "IAC001", "004", "MMK"));
// list.add(new DataBean("LIFE_SERVICE_CHARGES", "IAC001", "004", "USD"));
// list.add(new DataBean("LIFE_SUNDRY", "LMD001", "001", "MMK"));
// list.add(new DataBean("LIFE_SUNDRY", "LMD001", "001", "USD"));
// list.add(new DataBean("LIFE_SUNDRY", "LMD001", "002", "MMK"));
// list.add(new DataBean("LIFE_SUNDRY", "LMD001", "002", "USD"));
// list.add(new DataBean("LIFE_SUNDRY", "LMD001", "003", "MMK"));
// list.add(new DataBean("LIFE_SUNDRY", "LMD001", "003", "USD"));
// list.add(new DataBean("LIFE_SUNDRY", "LMD001", "004", "MMK"));
// list.add(new DataBean("LIFE_SUNDRY", "LMD001", "004", "USD"));
// list.add(new DataBean("MANDALAY_INTERBRANCH", "ASH002", "001", "MMK"));
// list.add(new DataBean("MANDALAY_INTERBRANCH", "ASH002", "001", "USD"));
// list.add(new DataBean("MANDALAY_INTERBRANCH", "ASH002", "002", "MMK"));
// list.add(new DataBean("MANDALAY_INTERBRANCH", "ASH002", "002", "USD"));
// list.add(new DataBean("MANDALAY_INTERBRANCH", "ASH002", "003", "MMK"));
// list.add(new DataBean("MANDALAY_INTERBRANCH", "ASH002", "003", "USD"));
// list.add(new DataBean("MANDALAY_INTERBRANCH", "ASH002", "004", "MMK"));
// list.add(new DataBean("MANDALAY_INTERBRANCH", "ASH002", "004", "USD"));
// list.add(new DataBean("MARINE_AGENT_COMMISSION", "EEF008", "001", "MMK"));
// list.add(new DataBean("MARINE_AGENT_COMMISSION", "EEF008", "002", "MMK"));
// list.add(new DataBean("MARINE_AGENT_COMMISSION", "EEF008", "003", "MMK"));
// list.add(new DataBean("MARINE_AGENT_COMMISSION", "EEF008", "004", "MMK"));
// list.add(new DataBean("MARINE_AGENT_PAYABLE", "LPB007", "001", "MMK"));
// list.add(new DataBean("MARINE_AGENT_PAYABLE", "LPB007", "002", "MMK"));
// list.add(new DataBean("MARINE_AGENT_PAYABLE", "LPB007", "003", "MMK"));
// list.add(new DataBean("MARINE_AGENT_PAYABLE", "LPB007", "004", "MMK"));
// list.add(new DataBean("MARINE_CLAIM_OUTSTANDING", "LPA007", "002", "MMK"));
// list.add(new DataBean("MARINE_CLAIM_OUTSTANDING", "LPA007", "003", "MMK"));
// list.add(new DataBean("MARINE_CLAIM_OUTSTANDING", "LPA007", "004", "MMK"));
// list.add(new DataBean("MARINE_PAYMENT_ORDER", "AUK007", "001", "MMK"));
// list.add(new DataBean("MARINE_PAYMENT_ORDER", "AUK007", "002", "MMK"));
// list.add(new DataBean("MARINE_PAYMENT_ORDER", "AUK007", "003", "MMK"));
// list.add(new DataBean("MARINE_PAYMENT_ORDER", "AUK007", "004", "MMK"));
// list.add(new DataBean("MARINE_PREMIUM", "IAH001", "001", "MMK"));
// list.add(new DataBean("MARINE_PREMIUM", "IAH001", "002", "MMK"));
// list.add(new DataBean("MARINE_PREMIUM", "IAH001", "003", "MMK"));
// list.add(new DataBean("MARINE_PREMIUM", "IAH001", "004", "MMK"));
// list.add(new DataBean("MARINE_SERVICE_CHARGES", "IAC008", "001", "MMK"));
// list.add(new DataBean("MARINE_SERVICE_CHARGES", "IAC008", "002", "MMK"));
// list.add(new DataBean("MARINE_SERVICE_CHARGES", "IAC008", "003", "MMK"));
// list.add(new DataBean("MARINE_SERVICE_CHARGES", "IAC008", "004", "MMK"));
// list.add(new DataBean("MOTOR_AGENT_COMMISSION", "EEF005", "001", "MMK"));
// list.add(new DataBean("MOTOR_AGENT_COMMISSION", "EEF005", "001", "USD"));
// list.add(new DataBean("MOTOR_AGENT_COMMISSION", "EEF005", "002", "MMK"));
// list.add(new DataBean("MOTOR_AGENT_COMMISSION", "EEF005", "002", "USD"));
// list.add(new DataBean("MOTOR_AGENT_COMMISSION", "EEF005", "003", "MMK"));
// list.add(new DataBean("MOTOR_AGENT_COMMISSION", "EEF005", "003", "USD"));
// list.add(new DataBean("MOTOR_AGENT_COMMISSION", "EEF005", "004", "MMK"));
// list.add(new DataBean("MOTOR_AGENT_COMMISSION", "EEF005", "004", "USD"));
// list.add(new DataBean("MOTOR_AGENT_PAYABLE", "LPB003", "001", "MMK"));
// list.add(new DataBean("MOTOR_AGENT_PAYABLE", "LPB003", "001", "USD"));
// list.add(new DataBean("MOTOR_AGENT_PAYABLE", "LPB003", "002", "MMK"));
// list.add(new DataBean("MOTOR_AGENT_PAYABLE", "LPB003", "002", "USD"));
// list.add(new DataBean("MOTOR_AGENT_PAYABLE", "LPB003", "003", "MMK"));
// list.add(new DataBean("MOTOR_AGENT_PAYABLE", "LPB003", "003", "USD"));
// list.add(new DataBean("MOTOR_AGENT_PAYABLE", "LPB003", "004", "MMK"));
// list.add(new DataBean("MOTOR_AGENT_PAYABLE", "LPB003", "004", "USD"));
// list.add(new DataBean("MOTOR_CLAIM", "EED001", "001", "MMK"));
// list.add(new DataBean("MOTOR_CLAIM", "EED001", "001", "USD"));
// list.add(new DataBean("MOTOR_CLAIM", "EED001", "002", "MMK"));
// list.add(new DataBean("MOTOR_CLAIM", "EED001", "002", "USD"));
// list.add(new DataBean("MOTOR_CLAIM", "EED001", "003", "MMK"));
// list.add(new DataBean("MOTOR_CLAIM", "EED001", "003", "USD"));
// list.add(new DataBean("MOTOR_CLAIM", "EED001", "004", "MMK"));
// list.add(new DataBean("MOTOR_CLAIM", "EED001", "004", "USD"));
// list.add(new DataBean("MOTOR_CLAIM_DEATH", "EED002", "001", "MMK"));
// list.add(new DataBean("MOTOR_CLAIM_DEATH", "EED002", "001", "USD"));
// list.add(new DataBean("MOTOR_CLAIM_DEATH", "EED002", "002", "MMK"));
// list.add(new DataBean("MOTOR_CLAIM_DEATH", "EED002", "002", "USD"));
// list.add(new DataBean("MOTOR_CLAIM_DEATH", "EED002", "003", "MMK"));
// list.add(new DataBean("MOTOR_CLAIM_DEATH", "EED002", "003", "USD"));
// list.add(new DataBean("MOTOR_CLAIM_DEATH", "EED002", "004", "MMK"));
// list.add(new DataBean("MOTOR_CLAIM_DEATH", "EED002", "004", "USD"));
// list.add(new DataBean("MOTOR_CLAIM_OUTSTANDING", "LPA003", "001", "MMK"));
// list.add(new DataBean("MOTOR_CLAIM_OUTSTANDING", "LPA003", "001", "USD"));
// list.add(new DataBean("MOTOR_CLAIM_OUTSTANDING", "LPA003", "002", "MMK"));
// list.add(new DataBean("MOTOR_CLAIM_OUTSTANDING", "LPA003", "002", "USD"));
// list.add(new DataBean("MOTOR_CLAIM_OUTSTANDING", "LPA003", "003", "MMK"));
// list.add(new DataBean("MOTOR_CLAIM_OUTSTANDING", "LPA003", "003", "USD"));
// list.add(new DataBean("MOTOR_CLAIM_OUTSTANDING", "LPA003", "004", "MMK"));
// list.add(new DataBean("MOTOR_CLAIM_OUTSTANDING", "LPA003", "004", "USD"));
// list.add(new DataBean("MOTOR_CO_AGENT_PAYABLE", "LPB005", "001", "MMK"));
// list.add(new DataBean("MOTOR_CO_AGENT_PAYABLE", "LPB005", "001", "USD"));
// list.add(new DataBean("MOTOR_CO_AGENT_PAYABLE", "LPB005", "002", "MMK"));
// list.add(new DataBean("MOTOR_CO_AGENT_PAYABLE", "LPB005", "002", "USD"));
// list.add(new DataBean("MOTOR_CO_AGENT_PAYABLE", "LPB005", "003", "MMK"));
// list.add(new DataBean("MOTOR_CO_AGENT_PAYABLE", "LPB005", "003", "USD"));
// list.add(new DataBean("MOTOR_CO_AGENT_PAYABLE", "LPB005", "004", "MMK"));
// list.add(new DataBean("MOTOR_CO_AGENT_PAYABLE", "LPB005", "004", "USD"));
// list.add(new DataBean("MOTOR_COINSURANCE_COMMISSION", "IAR003", "001",
// "MMK"));
// list.add(new DataBean("MOTOR_COINSURANCE_COMMISSION", "IAR003", "001",
// "USD"));
// list.add(new DataBean("MOTOR_COINSURANCE_COMMISSION", "IAR003", "002",
// "MMK"));
// list.add(new DataBean("MOTOR_COINSURANCE_COMMISSION", "IAR003", "002",
// "USD"));
// list.add(new DataBean("MOTOR_COINSURANCE_COMMISSION", "IAR003", "003",
// "MMK"));
// list.add(new DataBean("MOTOR_COINSURANCE_COMMISSION", "IAR003", "003",
// "USD"));
// list.add(new DataBean("MOTOR_COINSURANCE_COMMISSION", "IAR003", "004",
// "MMK"));
// list.add(new DataBean("MOTOR_COINSURANCE_COMMISSION", "IAR003", "004",
// "USD"));
// list.add(new DataBean("MOTOR_COINSURANCE_PREMIUM", "IAN002", "001", "MMK"));
// list.add(new DataBean("MOTOR_COINSURANCE_PREMIUM", "IAN002", "001", "USD"));
// list.add(new DataBean("MOTOR_COINSURANCE_PREMIUM", "IAN002", "002", "MMK"));
// list.add(new DataBean("MOTOR_COINSURANCE_PREMIUM", "IAN002", "002", "USD"));
// list.add(new DataBean("MOTOR_COINSURANCE_PREMIUM", "IAN002", "003", "MMK"));
// list.add(new DataBean("MOTOR_COINSURANCE_PREMIUM", "IAN002", "003", "USD"));
// list.add(new DataBean("MOTOR_COINSURANCE_PREMIUM", "IAN002", "004", "MMK"));
// list.add(new DataBean("MOTOR_COINSURANCE_PREMIUM", "IAN002", "004", "USD"));
// list.add(new DataBean("MOTOR_PAYMENT_ORDER", "AUK003", "001", "MMK"));
// list.add(new DataBean("MOTOR_PAYMENT_ORDER", "AUK003", "001", "USD"));
// list.add(new DataBean("MOTOR_PAYMENT_ORDER", "AUK003", "002", "MMK"));
// list.add(new DataBean("MOTOR_PAYMENT_ORDER", "AUK003", "002", "USD"));
// list.add(new DataBean("MOTOR_PAYMENT_ORDER", "AUK003", "003", "MMK"));
// list.add(new DataBean("MOTOR_PAYMENT_ORDER", "AUK003", "003", "USD"));
// list.add(new DataBean("MOTOR_PAYMENT_ORDER", "AUK003", "004", "MMK"));
// list.add(new DataBean("MOTOR_PAYMENT_ORDER", "AUK003", "004", "USD"));
// list.add(new DataBean("MOTOR_PREMIUM", "IAN001", "001", "MMK"));
// list.add(new DataBean("MOTOR_PREMIUM", "IAN001", "001", "USD"));
// list.add(new DataBean("MOTOR_PREMIUM", "IAN001", "002", "MMK"));
// list.add(new DataBean("MOTOR_PREMIUM", "IAN001", "002", "USD"));
// list.add(new DataBean("MOTOR_PREMIUM", "IAN001", "003", "MMK"));
// list.add(new DataBean("MOTOR_PREMIUM", "IAN001", "003", "USD"));
// list.add(new DataBean("MOTOR_PREMIUM", "IAN001", "004", "MMK"));
// list.add(new DataBean("MOTOR_PREMIUM", "IAN001", "004", "USD"));
// list.add(new DataBean("MOTOR_REINSTATE_PREMIUM", "IRS002", "001", "MMK"));
// list.add(new DataBean("MOTOR_REINSTATE_PREMIUM", "IRS002", "001", "USD"));
// list.add(new DataBean("MOTOR_REINSTATE_PREMIUM", "IRS002", "002", "MMK"));
// list.add(new DataBean("MOTOR_REINSTATE_PREMIUM", "IRS002", "002", "USD"));
// list.add(new DataBean("MOTOR_REINSTATE_PREMIUM", "IRS002", "003", "MMK"));
// list.add(new DataBean("MOTOR_REINSTATE_PREMIUM", "IRS002", "003", "USD"));
// list.add(new DataBean("MOTOR_REINSTATE_PREMIUM", "IRS002", "004", "MMK"));
// list.add(new DataBean("MOTOR_REINSTATE_PREMIUM", "IRS002", "004", "USD"));
// list.add(new DataBean("MOTOR_SERVICE_CHARGES", "IAC003", "001", "MMK"));
// list.add(new DataBean("MOTOR_SERVICE_CHARGES", "IAC003", "001", "USD"));
// list.add(new DataBean("MOTOR_SERVICE_CHARGES", "IAC003", "002", "MMK"));
// list.add(new DataBean("MOTOR_SERVICE_CHARGES", "IAC003", "002", "USD"));
// list.add(new DataBean("MOTOR_SERVICE_CHARGES", "IAC003", "003", "MMK"));
// list.add(new DataBean("MOTOR_SERVICE_CHARGES", "IAC003", "003", "USD"));
// list.add(new DataBean("MOTOR_SERVICE_CHARGES", "IAC003", "004", "MMK"));
// list.add(new DataBean("MOTOR_SERVICE_CHARGES", "IAC003", "004", "USD"));
// list.add(new DataBean("MOTOR_SUNDRY", "LMD003", "001", "MMK"));
// list.add(new DataBean("MOTOR_SUNDRY", "LMD003", "001", "USD"));
// list.add(new DataBean("MOTOR_SUNDRY", "LMD003", "002", "MMK"));
// list.add(new DataBean("MOTOR_SUNDRY", "LMD003", "002", "USD"));
// list.add(new DataBean("MOTOR_SUNDRY", "LMD003", "003", "MMK"));
// list.add(new DataBean("MOTOR_SUNDRY", "LMD003", "003", "USD"));
// list.add(new DataBean("MOTOR_SUNDRY", "LMD003", "004", "MMK"));
// list.add(new DataBean("MOTOR_SUNDRY", "LMD003", "004", "USD"));
// list.add(new DataBean("NAYPYITAW_INTERBRANCH", "ASH006", "001", "MMK"));
// list.add(new DataBean("NAYPYITAW_INTERBRANCH", "ASH006", "001", "USD"));
// list.add(new DataBean("PUBLIC_LIFE_PREMIUM", "IAL001", "001", "MMK"));
// list.add(new DataBean("PUBLIC_LIFE_PREMIUM", "IAL001", "001", "USD"));
// list.add(new DataBean("PUBLIC_LIFE_PREMIUM", "IAL001", "002", "MMK"));
// list.add(new DataBean("PUBLIC_LIFE_PREMIUM", "IAL001", "002", "USD"));
// list.add(new DataBean("PUBLIC_LIFE_PREMIUM", "IAL001", "003", "MMK"));
// list.add(new DataBean("PUBLIC_LIFE_PREMIUM", "IAL001", "003", "USD"));
// list.add(new DataBean("PUBLIC_LIFE_PREMIUM", "IAL001", "004", "MMK"));
// list.add(new DataBean("PUBLIC_LIFE_PREMIUM", "IAL001", "004", "USD"));
// list.add(new DataBean("SNAKE_BITE_PREMIUM", "IAL007", "001", "MMK"));
// list.add(new DataBean("SNAKE_BITE_PREMIUM", "IAL007", "001", "USD"));
// list.add(new DataBean("SNAKE_BITE_PREMIUM", "IAL007", "002", "MMK"));
// list.add(new DataBean("SNAKE_BITE_PREMIUM", "IAL007", "002", "USD"));
// list.add(new DataBean("SNAKE_BITE_PREMIUM", "IAL007", "003", "MMK"));
// list.add(new DataBean("SNAKE_BITE_PREMIUM", "IAL007", "003", "USD"));
// list.add(new DataBean("SNAKE_BITE_PREMIUM", "IAL007", "004", "MMK"));
// list.add(new DataBean("SNAKE_BITE_PREMIUM", "IAL007", "004", "USD"));
// list.add(new DataBean("SPORT_MAN_PREMIUM", "IAL003", "001", "MMK"));
// list.add(new DataBean("SPORT_MAN_PREMIUM", "IAL003", "001", "USD"));
// list.add(new DataBean("SPORT_MAN_PREMIUM", "IAL003", "002", "MMK"));
// list.add(new DataBean("SPORT_MAN_PREMIUM", "IAL003", "002", "USD"));
// list.add(new DataBean("SPORT_MAN_PREMIUM", "IAL003", "003", "MMK"));
// list.add(new DataBean("SPORT_MAN_PREMIUM", "IAL003", "003", "USD"));
// list.add(new DataBean("SPORT_MAN_PREMIUM", "IAL003", "004", "MMK"));
// list.add(new DataBean("SPORT_MAN_PREMIUM", "IAL003", "004", "USD"));
// list.add(new DataBean("STAMP_FEES", "LBT001", "001", "MMK"));
// list.add(new DataBean("STAMP_FEES", "LBT001", "001", "USD"));
// list.add(new DataBean("STAMP_FEES", "LBT001", "002", "MMK"));
// list.add(new DataBean("STAMP_FEES", "LBT001", "002", "USD"));
// list.add(new DataBean("STAMP_FEES", "LBT001", "003", "MMK"));
// list.add(new DataBean("STAMP_FEES", "LBT001", "003", "USD"));
// list.add(new DataBean("STAMP_FEES", "LBT001", "004", "MMK"));
// list.add(new DataBean("STAMP_FEES", "LBT001", "004", "USD"));
// list.add(new DataBean("Sundry Deposit Stamp Duty Recovered A/C", "LMD015",
// "001", "MMK"));
// list.add(new DataBean("TRAVEL_PREMIUM", "IAL004", "001", "MMK"));
// list.add(new DataBean("TRAVEL_PREMIUM", "IAL004", "001", "USD"));
// list.add(new DataBean("TRAVEL_PREMIUM", "IAL004", "002", "MMK"));
// list.add(new DataBean("TRAVEL_PREMIUM", "IAL004", "002", "USD"));
// list.add(new DataBean("TRAVEL_PREMIUM", "IAL004", "003", "MMK"));
// list.add(new DataBean("TRAVEL_PREMIUM", "IAL004", "003", "USD"));
// list.add(new DataBean("TRAVEL_PREMIUM", "IAL004", "004", "MMK"));
// list.add(new DataBean("TRAVEL_PREMIUM", "IAL004", "004", "USD"));
// list.add(new DataBean("YANGON_INTERBRANCH", "ASH001", "001", "MMK"));
// list.add(new DataBean("YANGON_INTERBRANCH", "ASH001", "001", "USD"));
// list.add(new DataBean("YANGON_INTERBRANCH", "ASH001", "002", "MMK"));
// list.add(new DataBean("YANGON_INTERBRANCH", "ASH001", "002", "USD"));
// list.add(new DataBean("YANGON_INTERBRANCH", "ASH001", "003", "MMK"));
// list.add(new DataBean("YANGON_INTERBRANCH", "ASH001", "003", "USD"));
// list.add(new DataBean("YANGON_INTERBRANCH", "ASH001", "004", "MMK"));
// list.add(new DataBean("YANGON_INTERBRANCH", "ASH001", "004", "USD"));
// }
//
// private class DataBean {
// private String acName;
// private String acCode;
// private String branchCode;
// private String currencyCode;
//
// public DataBean(String acName, String acCode, String branchCode, String
// currencyCode) {
// this.acName = acName;
// this.acCode = acCode;
// this.branchCode = branchCode;
// this.currencyCode = currencyCode;
// }
//
// public String getAcName() {
// return acName;
// }
//
// public String getAcCode() {
// return acCode;
// }
//
// public String getBranchCode() {
// return branchCode;
// }
//
// public String getCurrencyCode() {
// return currencyCode;
// }
// }
// }
