// package org.ace.accounting.system.migrate;
//
// import java.text.DateFormat;
// import java.text.ParseException;
// import java.text.SimpleDateFormat;
// import java.util.ArrayList;
// import java.util.List;
//
// import org.ace.accounting.system.branch.service.interfaces.IBranchService;
// import org.ace.accounting.system.chartaccount.AccountCodeType;
// import org.ace.accounting.system.chartaccount.AccountType;
// import org.ace.accounting.system.chartaccount.ChartOfAccount;
// import org.ace.accounting.system.chartaccount.service.interfaces.ICoaService;
// import
// org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
// import org.ace.accounting.system.department.Department;
// import org.ace.java.component.service.interfaces.IDataRepService;
// import org.springframework.beans.factory.BeanFactory;
// import org.springframework.context.ApplicationContext;
// import org.springframework.context.support.ClassPathXmlApplicationContext;
//
/// ***
// *
// * update ccoa table manually update CCOA set CREATEDDATE=' >> DATE HERE <<
// * ',CREATEDUSERID='Migrated';
// *
// */
//
// public class CoaMigrate {
// public static void main(String[] args) {
// CoaMigrate mig = new CoaMigrate();
// mig.test();
// mig.add();
// System.out.println("DONE");
// }
//
// private void test() {
// List<DataBean> list = new ArrayList<DataBean>();
// createList(list);
// System.out.println("SMT" + list.size());
// }
//
// @SuppressWarnings("unchecked")
// private void add() {
//
// List<DataBean> list = new ArrayList<DataBean>();
// createList(list);
//
// ApplicationContext context = new
// ClassPathXmlApplicationContext("spring-beans.xml");
// BeanFactory factory = context;
// IDataRepService<ChartOfAccount> dataRepService =
// (IDataRepService<ChartOfAccount>) factory.getBean("DataRepService");
// IDataRepService<Department> depserv = (IDataRepService<Department>)
// factory.getBean("DataRepService");
//
// IBranchService branchService = (IBranchService)
// factory.getBean("BranchService");
// ICoaService coaService = (ICoaService) factory.getBean("CoaService");
// ICurrencyService currencyService = (ICurrencyService)
// factory.getBean("CurrencyService");
//
// DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//
// for (DataBean b : list) {
// ChartOfAccount coa = new ChartOfAccount();
// coa.setAcCode(b.getAcCode());
//
// if (b.getAcCode().substring(1).equals("00000")) {
// coa.setAcCodeType(AccountCodeType.HEAD);
// } else if (b.getAcCode().substring(3).equals("000")) {
// coa.setAcCodeType(AccountCodeType.GROUP);
// String id =
// coaService.findCoaByAcCode(String.valueOf(b.getAcCode().charAt(0)) +
// "00000").getId();
// coa.setHeadId(id);
// } else {
// coa.setAcCodeType(AccountCodeType.DETAIL);
// String id =
// coaService.findCoaByAcCode(String.valueOf(b.getAcCode().charAt(0)) +
// "00000").getId();
// coa.setHeadId(id);
// String groupId = coaService.findCoaByAcCode(b.getAcCode().substring(0, 3) +
// "000").getId();
// coa.setGroupId(groupId);
// }
//
// coa.setAcName(b.getAcName());
//
// AccountType acType = AccountType.valueOf(b.getAcType());
// coa.setAcType(acType);
//
// try {
// coa.setpDate(format.parse(b.getpDate()));
// } catch (ParseException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }
//
// coaService.addNewCoa(coa);
//
// }
//
// }
//
// private void createList(List<DataBean> list) {
// list.add(new DataBean("A00000", "null", "Assets", "E", "2013-05-02
// 01:44:17.257"));
// list.add(new DataBean("AAC000", "null", "General Account of Cash", "L",
// "2013-05-02 01:44:31.777"));
// list.add(new DataBean("AAC001", "null", "General Account of Cash (MMK)", "A",
// "2013-05-02 01:44:46.563"));
// list.add(new DataBean("AAC002", "null", "General Account of Cash (USD)", "A",
// "2013-05-19 21:49:58.240"));
// list.add(new DataBean("AAC003", "null", "General Account of Cash (S$)", "A",
// "2013-05-19 21:50:11.897"));
// list.add(new DataBean("ADF000", "null", "Account with Central Bank", "A",
// "2013-05-02 01:46:15.900"));
// list.add(new DataBean("ADF001", "null", "Account with Central Bank ( III )",
// "A", "2013-05-02 01:46:28.000"));
// list.add(new DataBean("AEG000", "null", "Account with Government Current
// Account", "A", "2013-05-02 01:46:44.767"));
// list.add(new DataBean("AEG001", "null", "Account with MEB(4)", "A",
// "2013-05-02 01:46:56.110"));
// list.add(new DataBean("AEG002", "null", "Account with MEB (4)", "A",
// "2013-05-02 22:23:53.750"));
// list.add(new DataBean("AEH000", "null", "A/C with (Govt.)Bank Current
// (Foreign Currency)", "A", "2014-08-21 13:29:43.240"));
// list.add(new DataBean("AEH001", "null", "A/C with MICB Bank Current (Foreign
// Currency) No.FDG 10.0857", "A", "2014-08-21 13:31:56.310"));
// list.add(new DataBean("AEM000", "null", "Account with Other Bank Current",
// "A", "2013-05-10 12:05:03.500"));
// list.add(new DataBean("AEM001", "null", "Account with KBZ(KT)", "A",
// "2013-05-10 12:05:31.343"));
// list.add(new DataBean("AEM002", "null", "Account with UAB", "A", "2013-09-23
// 09:08:53.067"));
// list.add(new DataBean("AEM003", "null", "Account with APEX Bank", "A",
// "2013-10-17 15:38:23.440"));
// list.add(new DataBean("AEM004", "null", "Account with SMIDB", "A",
// "2013-10-17 15:39:54.573"));
// list.add(new DataBean("AEM005", "null", "Account with Si Bin Bank (NPT)",
// "A", "2013-10-17 15:41:09.610"));
// list.add(new DataBean("AEM006", "null", "Account with CB Bank", "A",
// "2013-10-17 15:42:12.213"));
// list.add(new DataBean("AEM007", "null", "Account with MCB Bank", "A",
// "2013-11-15 09:27:13.843"));
// list.add(new DataBean("AEM008", "null", "Account with GTB", "A", "2013-11-22
// 15:32:43.940"));
// list.add(new DataBean("AEM009", "null", "Account with AYA Bank", "A",
// "2014-10-01 12:41:43.217"));
// list.add(new DataBean("AFA000", "null", "Immovable Property", "A",
// "2013-05-02 01:52:44.867"));
// list.add(new DataBean("AFA001", "null", "Land", "A", "2013-05-02
// 01:52:53.863"));
// list.add(new DataBean("AFA002", "null", "Land and Building", "A", "2013-05-02
// 22:25:10.440"));
// list.add(new DataBean("AFA003", "null", "Land and Building ( MDY )", "A",
// "2013-07-29 10:44:48.480"));
// list.add(new DataBean("AFB000", "null", "Office Furniture", "A", "2013-05-02
// 01:53:06.817"));
// list.add(new DataBean("AFB001", "null", "Office Furniture & Other Fitting at
// Cost", "A", "2013-05-02 01:53:20.893"));
// list.add(new DataBean("AFB002", "null", "Office Furniture and Other Fitting
// at Cost (MDY)", "A", "2013-07-29 10:48:10.300"));
// list.add(new DataBean("AFC000", "null", "Office Machinery & Other Equipment",
// "A", "2013-05-02 01:53:36.207"));
// list.add(new DataBean("AFC001", "null", "Office Machinery & Other Equipment
// at Cost", "A", "2013-05-02 01:53:50.177"));
// list.add(new DataBean("AFC002", "null", "Office Machinery and Other Equipment
// at Cost (MDY)", "A", "2013-07-29 10:46:53.983"));
// list.add(new DataBean("AFD000", "null", "Computer & Component", "A",
// "2013-05-02 01:54:04.427"));
// list.add(new DataBean("AFD001", "null", "Computer & Component at Cost", "A",
// "2013-05-02 01:54:16.497"));
// list.add(new DataBean("AFD002", "null", "Computer & Component at Cost (MDY)",
// "A", "2013-07-29 10:49:22.903"));
// list.add(new DataBean("AFE000", "null", "Office Vehicle", "A", "2013-05-02
// 01:54:28.243"));
// list.add(new DataBean("AFE001", "null", "Office Vehicle (Car) at Cost", "A",
// "2013-05-02 01:54:40.823"));
// list.add(new DataBean("AFE002", "null", "Office Vehicle (Car) at Cost (MDY)",
// "A", "2013-05-02 01:54:49.490"));
// list.add(new DataBean("AFE003", "null", "Motorcycle & Accessories at Cost",
// "A", "2013-07-29 10:52:10.633"));
// list.add(new DataBean("AFE004", "null", "Motorcycle and Accessories at Cost
// (MDY)", "A", "2013-07-29 10:52:53.893"));
// list.add(new DataBean("AFG000", "null", "Account with ( Private Bank) Smart
// Saving", "A", "2014-09-16 10:49:34.123"));
// list.add(new DataBean("AFG001", "null", "Account with MAB Smart Savings",
// "A", "2014-09-16 10:50:13.437"));
// list.add(new DataBean("AFH000", "null", "Account with (Other Bank) Saving
// Account", "A", "2013-05-02 01:47:29.840"));
// list.add(new DataBean("AFH001", "null", "Savings Account with (CB Bank)",
// "A", "2013-05-02 01:47:54.730"));
// list.add(new DataBean("AFH002", "null", "Savings Account with (APEX Bank)",
// "A", "2013-05-02 01:48:08.167"));
// list.add(new DataBean("AFH003", "null", "Savings Account with GTB", "A",
// "2013-05-02 01:48:22.440"));
// list.add(new DataBean("AFH004", "null", "Savings Account with (FPB Bank)",
// "A", "2013-05-02 01:48:37.217"));
// list.add(new DataBean("AFH005", "null", "Savings Account with (KBZ Bank)",
// "A", "2013-05-02 01:48:49.730"));
// list.add(new DataBean("AFH006", "null", "Savings Account with (AYA Bank)",
// "A", "2013-06-27 14:44:09.970"));
// list.add(new DataBean("AFH007", "null", "Savings Account with (UAB)", "A",
// "2013-09-23 09:06:41.387"));
// list.add(new DataBean("AFH008", "null", "Savings Account with Si Bin Bank (
// NPT )", "A", "2013-10-17 15:47:51.110"));
// list.add(new DataBean("AFH009", "null", "Savings A/C with MCB Bank", "A",
// "2013-10-17 15:48:39.720"));
// list.add(new DataBean("AFH010", "null", "Savings Account with MIDB Bank
// (HO)", "A", "2014-09-24 13:16:15.270"));
// list.add(new DataBean("AGK000", "null", "Account with (Other Bank) Fixed
// Deposit Account", "A", "2013-05-02 01:49:07.020"));
// list.add(new DataBean("AGK001", "null", "Fixed Deposit Account with (CB Bank
// )", "A", "2013-05-02 01:49:21.967"));
// list.add(new DataBean("AGK002", "null", "Fixed Deposit Account with (AYA
// Bank)", "A", "2013-05-02 01:49:36.233"));
// list.add(new DataBean("AGK003", "null", "Fixed Deposit Account with (CB
// Bank)", "A", "2013-05-02 01:49:50.470"));
// list.add(new DataBean("AGK004", "null", "Fixed Deposit Account with (CB
// Bank)", "A", "2013-05-02 01:50:07.380"));
// list.add(new DataBean("AGK005", "null", "Fixed Deposit Account with (APEX
// Bank )", "A", "2013-05-02 01:50:28.380"));
// list.add(new DataBean("AGK006", "null", "Account with UAB Fixed Deposit",
// "A", "2013-05-02 22:30:25.987"));
// list.add(new DataBean("AGK007", "null", "Account with (APEX Bank)", "A",
// "2013-05-02 22:40:48.973"));
// list.add(new DataBean("AGK008", "null", "Account with (APEX Bank)", "A",
// "2013-05-02 22:41:09.613"));
// list.add(new DataBean("AGK009", "null", "Account with (KBZ Bank)", "A",
// "2013-05-02 22:41:46.290"));
// list.add(new DataBean("AGK010", "null", "Account with Fixed Deposit Myanmar
// Citizens Bank", "A", "2013-05-02 22:42:26.193"));
// list.add(new DataBean("AGK011", "null", "Account with (FPB Bank)", "A",
// "2013-05-02 22:42:55.193"));
// list.add(new DataBean("AGK012", "null", "Account with (FPB Bank)", "A",
// "2013-05-02 22:43:19.250"));
// list.add(new DataBean("AGK013", "null", "Account with (GTB)", "A",
// "2013-05-02 22:44:11.650"));
// list.add(new DataBean("AGK014", "null", "Account with (SMIDB)", "A",
// "2013-05-02 22:44:30.680"));
// list.add(new DataBean("AGK015", "null", "Account with (GTB)", "A",
// "2013-05-02 22:44:49.213"));
// list.add(new DataBean("AGK016", "null", "Account with (GTB)", "A",
// "2013-05-02 22:45:05.627"));
// list.add(new DataBean("AGK017", "null", "Account with (KBZ Bank )", "A",
// "2013-09-30 14:36:52.517"));
// list.add(new DataBean("ALM000", "null", "Equity Investment", "A", "2013-05-02
// 01:50:46.880"));
// list.add(new DataBean("ALM001", "null", "Treasury Bond Purchase Account
// (MSEC)", "A", "2013-05-02 01:50:59.903"));
// list.add(new DataBean("ALM002", "null", "Loan and Advance (Policyholder)",
// "A", "2013-05-02 01:51:13.667"));
// list.add(new DataBean("ALM003", "null", "Treasury Bill Purchase Account
// (CBM)", "A", "2013-05-02 01:52:32.150"));
// list.add(new DataBean("ALM004", "null", "Share Purchase", "A", "2013-05-02
// 22:46:40.677"));
// list.add(new DataBean("ALM005", "null", "", "A", "2013-06-28 10:14:05.430"));
// list.add(new DataBean("ARH000", "null", "Advance Drawn to open branches",
// "A", "2013-05-02 01:55:16.697"));
// list.add(new DataBean("ARH001", "null", "Advance Drawn on Establishment of
// (HO)", "A", "2013-05-02 01:55:50.900"));
// list.add(new DataBean("ARH002", "null", "Advance drawn to Open MDY-Branch",
// "A", "2013-07-29 10:54:00.070"));
// list.add(new DataBean("ARH003", "null", "Advance drawn to Open Mawlamyaing",
// "A", "2013-10-09 17:30:33.900"));
// list.add(new DataBean("ARH004", "null", "Advance drawn to Open Monywa", "A",
// "2013-10-10 10:16:38.200"));
// list.add(new DataBean("ARH005", "null", "Advance drawn to Open Lashio -
// Branch", "A", "2013-10-10 10:34:25.557"));
// list.add(new DataBean("ARH006", "null", "Advance drawn to Open Nay Pyi Taw -
// Branch", "A", "2013-10-10 10:35:23.013"));
// list.add(new DataBean("ARR000", "null", "Suspense Account (Foreign)", "A",
// "2013-05-02 02:05:19.773"));
// list.add(new DataBean("ARR001", "null", "Re-insurance Claim Recoverable
// Account", "A", "2013-05-02 02:05:34.147"));
// list.add(new DataBean("ARS000", "null", "Suspense Account (Domestic)", "A",
// "2013-05-02 02:05:58.403"));
// list.add(new DataBean("ARS001", "null", "Petty Cash", "A", "2013-05-02
// 02:06:08.880"));
// list.add(new DataBean("ARS002", "null", "Prepaid Life Insurance Premium
// Account", "A", "2013-05-02 02:06:22.157"));
// list.add(new DataBean("ARS003", "null", "Advance to Admin", "A", "2013-05-02
// 02:06:31.610"));
// list.add(new DataBean("ARS004", "null", "Cash Shortaged Account", "A",
// "2013-04-30 13:19:39.890"));
// list.add(new DataBean("ARS005", "null", "Unused Policy Stamp", "A",
// "2013-04-30 13:19:58.327"));
// list.add(new DataBean("ARS006", "null", "Suspense Account (Miscellenous)",
// "A", "2013-07-01 15:47:41.320"));
// list.add(new DataBean("ARS007", "null", "Prepaid Rental fee Office", "A",
// "2013-07-29 17:07:53.940"));
// list.add(new DataBean("ARS008", "null", "Prepaid-House Rental Fees ( Staff
// )", "A", "2013-09-17 12:12:14.710"));
// list.add(new DataBean("ARS009", "null", "Prepaid Revenue Tax", "A",
// "2013-12-13 14:10:42.060"));
// list.add(new DataBean("ARS010", "null", "Suspense of Exchange Revaluation
// Account", "A", "2014-12-26 15:54:48.330"));
// list.add(new DataBean("ARS011", "null", "Cash Shortaged A/C(USD)", "A",
// "2015-02-25 10:18:59.120"));
// list.add(new DataBean("ART000", "null", "Advance to Purchase", "A",
// "2013-05-02 02:07:36.580"));
// list.add(new DataBean("ART001", "null", "Advance to Purchase of Office Flat
// (or)Building", "A", "2013-05-02 02:07:53.933"));
// list.add(new DataBean("ART002", "null", "Advance to Purchase of Govt Stamp
// Duty to Fixed on purchase contrac", "A", "2013-05-02 22:49:12.760"));
// list.add(new DataBean("ASC000", "null", "Co-insurance Claims Account
// Receivable (Government)", "A", "2013-05-02 02:08:15.730"));
// list.add(new DataBean("ASC001", "null", "Co-insurance Claims Account
// Government (Life)", "A", "2013-05-02 02:08:49.133"));
// list.add(new DataBean("ASC002", "null", "Co-insurance Claims Account
// Government(Fire + General)", "A", "2013-05-02 02:09:01.657"));
// list.add(new DataBean("ASC003", "null", "Co-insurance Claims Account
// Government (Motor)", "A", "2013-04-30 13:22:31.640"));
// list.add(new DataBean("ASD000", "null", "Co-insurance Claims Account
// Receivable(Private)", "A", "2013-05-02 02:09:14.140"));
// list.add(new DataBean("ASD001", "null", "Co-insurance Claims Account Private
// (Life)", "A", "2013-05-02 02:09:27.593"));
// list.add(new DataBean("ASD002", "null", "Co-insurance Claims Account Private
// (Fire + General)", "A", "2013-05-02 22:51:59.323"));
// list.add(new DataBean("ASD003", "null", "Co-insurance Claims Account Private
// (Motor)", "A", "2013-04-30 13:23:57.827"));
// list.add(new DataBean("ASE000", "null", "Adjusting Account", "A", "2013-05-02
// 02:09:36.513"));
// list.add(new DataBean("ASE001", "null", "Adjusting Account (Insurance)", "A",
// "2013-05-02 02:09:54.740"));
// list.add(new DataBean("ASF000", "null", "Contra Account", "A", "2013-05-02
// 22:53:35.350"));
// list.add(new DataBean("ASF001", "null", "A.E.G (Internal) Contra Account",
// "A", "2013-05-02 22:53:51.280"));
// list.add(new DataBean("ASF002", "null", "A.E.G (External) Contra Account",
// "A", "2013-05-02 22:54:06.503"));
// list.add(new DataBean("ASH000", "null", "Inter Branch Settlement Account",
// "A", "2013-05-02 22:54:22.743"));
// list.add(new DataBean("ASH001", "null", "Inter Branch Settlement Account
// (HO)", "A", "2013-05-02 22:54:35.287"));
// list.add(new DataBean("ASH002", "null", "Inter Branch Settlement Account
// (MDY)", "A", "2013-05-02 22:54:48.920"));
// list.add(new DataBean("ASH004", "null", "Inter Branch Settlement Account
// (Monywa)", "A", "2015-05-05 16:29:50.617"));
// list.add(new DataBean("ASH006", "null", "Inter Branch Settlement
// Account(NPT)", "A", "2015-07-03 22:54:22.743"));
// list.add(new DataBean("ASR000", "null", "Currency Remittance Account", "A",
// "2013-05-02 22:55:05.410"));
// list.add(new DataBean("ASR001", "null", "Currency Remittance Account (HO)",
// "A", "2013-05-02 22:55:18.780"));
// list.add(new DataBean("ASR002", "null", "Currency Remittance Account (MDY)",
// "A", "2013-05-02 22:55:32.773"));
// list.add(new DataBean("ASR003", "null", "Currency Remittance Account
// (Mawlamyaing)", "A", "2015-04-22 15:27:14.860"));
// list.add(new DataBean("ASR004", "null", "Currency Remittance Account
// (Monywa)", "A", "2015-04-22 15:27:30.803"));
// list.add(new DataBean("ASR005", "null", "Currency Remittance Account
// (Lashio)", "A", "2015-04-22 15:27:51.630"));
// list.add(new DataBean("ASR006", "null", "Currency Remittance Account (NPT)",
// "A", "2015-04-22 15:28:08.337"));
// list.add(new DataBean("AUI000", "null", "Accrued Income Account", "A",
// "2013-05-02 22:55:53.863"));
// list.add(new DataBean("AUI001", "null", "Item due and unreceived A/C", "A",
// "2013-05-02 22:56:08.713"));
// list.add(new DataBean("AUI002", "null", "Accrued Interest Receivable -
// Government", "A", "2013-05-02 22:56:28.340"));
// list.add(new DataBean("AUI003", "null", "Accrued Interest Receivable -
// Private", "A", "2013-05-02 22:56:45.127"));
// list.add(new DataBean("AUK000", "null", "Accrued Premium Income Account",
// "A", "2013-05-02 22:57:01.147"));
// list.add(new DataBean("AUK001", "null", "Accrued Pre; Income Receivable
// (Life)", "A", "2013-05-02 22:57:16.637"));
// list.add(new DataBean("AUK002", "null", "Accrued Pre; Income Receivable
// (Fire)", "A", "2013-05-02 22:57:33.017"));
// list.add(new DataBean("AUK003", "null", "Accrued Premium Income Receivable
// (Motor)", "A", "2013-05-02 22:57:48.960"));
// list.add(new DataBean("AUK004", "null", "Accrued Co-Insurance Premium Income
// Receivable (Fire)", "A", "2014-03-13 13:28:00.360"));
// list.add(new DataBean("AUK005", "null", "Accrued Premi; Income Recei;
// (Fire)(USD)", "A", "2015-02-25 10:21:29.833"));
// list.add(new DataBean("AUK006", "null", "Accrued Premi; Income Recei;
// (Motor)(USD)", "A", "2015-02-25 10:23:11.793"));
// list.add(new DataBean("AUK007", "null", "Accrued Premium Income Receivable
// (Marine Cargo)", "A", "2015-09-03 17:27:42.970"));
// list.add(new DataBean("AUK008", "null", "Accrued Premium Income Receivable
// (Inland Cargo)", "A", "2015-09-03 17:27:42.970"));
// list.add(new DataBean("AUL000", "null", "Accrued Co(or)Re Insurance
// Commission Income Account", "A", "2013-06-20 14:59:02.180"));
// list.add(new DataBean("AUL001", "null", "Co(or)Re Insur; Commi; Recei; ( Life
// )", "A", "2013-06-20 14:59:27.360"));
// list.add(new DataBean("AUL002", "null", "Co(or)Re Insur; Commi; Recei;( Fire
// )", "A", "2013-06-20 14:59:41.617"));
// list.add(new DataBean("AUL003", "null", "Co(or)Re Insur; Commi; Recei;( Motor
// )", "A", "2013-06-20 14:59:52.787"));
// list.add(new DataBean("AUL004", "null", "Co(or)Re Insur; Commi;
// Recei;(Motor)(USD)", "A", "2015-02-25 10:30:00.593"));
// list.add(new DataBean("AUL005", "null", "Co(or)Re Insur; Commi;
// Recei;(Fire)(USD)", "A", "2015-02-25 10:31:10.900"));
// list.add(new DataBean("AUM000", "null", "Accured Co-Insurance Premium
// Receiveable Account", "A", "2014-03-10 10:34:55.430"));
// list.add(new DataBean("AUM001", "null", "Accured Co-Insurance Premium
// Receiveable Account (Motor)", "A", "2014-03-10 10:35:41.653"));
// list.add(new DataBean("AUM002", "null", "Accured Co-Insurance Premium
// Receiveable Account (Fire)", "A", "2014-03-10 10:36:02.153"));
// list.add(new DataBean("E00000", "null", "Charges", "E", "2013-05-02
// 02:10:22.347"));
// list.add(new DataBean("EE0000", "null", "Charges", "E", "2013-05-02
// 02:10:30.410"));
// list.add(new DataBean("EEA000", "null", "Public Life Insurance Claims", "E",
// "2013-05-02 02:10:52.023"));
// list.add(new DataBean("EEA001", "null", "Public Life Insurance Claims
// (Death)", "E", "2013-05-02 02:11:03.947"));
// list.add(new DataBean("EEA002", "null", "Public Life Insurance Claims
// (Surrender)", "E", "2013-05-02 02:11:23.250"));
// list.add(new DataBean("EEA003", "null", "Public Life Insurance Claims
// (Maturity)", "E", "2013-05-02 02:11:32.023"));
// list.add(new DataBean("EEA004", "null", "Public Life Insurance Claims
// (Disability)", "E", "2013-05-02 02:11:40.117"));
// list.add(new DataBean("EEA005", "null", "Public Life Insurance Claims
// (Group)", "E", "2013-05-02 23:01:37.297"));
// list.add(new DataBean("EEA006", "null", "Public Life Insurance Claims
// (Sport-Men)", "E", "2013-05-02 23:01:48.717"));
// list.add(new DataBean("EEA007", "null", "Public Special Life Insurance Claims
// on Highway Travelling", "E", "2013-05-02 23:01:58.403"));
// list.add(new DataBean("EEA008", "null", "Public Life Insurance Claims
// (Shorejob)", "E", "2013-05-02 23:02:08.170"));
// list.add(new DataBean("EEA009", "null", "Public Life Insurance Claims
// (Travelling)", "E", "2013-05-02 23:02:19.417"));
// list.add(new DataBean("EEA010", "null", "Public Life Insurance Claims
// (Snake-Bite)", "E", "2013-05-02 23:02:31.400"));
// list.add(new DataBean("EEA011", "null", "Public Life Insurance(Medical
// Charges)Claims A/C", "E", "2013-08-07 12:22:55.673"));
// list.add(new DataBean("EEB000", "null", "General Insurance Claims", "E",
// "2013-05-02 02:11:59.650"));
// list.add(new DataBean("EEB001", "null", "Insurance Claims (Fire)", "E",
// "2013-05-02 02:12:08.893"));
// list.add(new DataBean("EEB002", "null", "Premium Refund (Fire)", "E",
// "2013-05-02 23:05:02.810"));
// list.add(new DataBean("EEB003", "null", "Insurance Claims (Cash in Transit)",
// "E", "2013-05-02 23:05:14.717"));
// list.add(new DataBean("EEB004", "null", "Insurance Claims (Cash in Safe)",
// "E", "2013-05-02 23:05:26.867"));
// list.add(new DataBean("EEB005", "null", "Insurance Claims (Fidelity Bond)",
// "E", "2013-04-30 13:29:10.590"));
// list.add(new DataBean("EEB006", "null", "Co-Insurance Claims (Fire &
// General)", "E", "2014-03-19 14:54:32.307"));
// list.add(new DataBean("EEB007", "null", "Insurance Claims (Fire)(USD)", "E",
// "2015-02-25 10:32:44.703"));
// list.add(new DataBean("EEB008", "null", "Premium Refund (Fire)(USD)", "E",
// "2015-02-25 10:33:41.240"));
// list.add(new DataBean("EEC000", "null", "Marine and Aviation Insurance
// Claims", "E", "2013-05-02 02:12:31.040"));
// list.add(new DataBean("EEC001", "null", "Marine and Aviation Insurance
// Claims", "E", "2013-05-02 02:12:41.520"));
// list.add(new DataBean("EED000", "null", "Comprehensive (Motor) Insurance
// Claims (Accident and Misc.)", "E", "2013-05-02 02:13:12.630"));
// list.add(new DataBean("EED001", "null", "Accident and Miscellaneous Claims",
// "E", "2013-05-02 02:13:22.590"));
// list.add(new DataBean("EED002", "null", "Death Claims", "E", "2013-05-02
// 02:13:33.093"));
// list.add(new DataBean("EED003", "null", "Premium Refund (Motor)", "E",
// "2013-04-30 13:30:37.873"));
// list.add(new DataBean("EED004", "null", "Accident & Miscell; Claims (USD)",
// "E", "2015-02-25 10:35:12.017"));
// list.add(new DataBean("EED005", "null", "Death Claims (USD)", "E",
// "2015-02-25 10:35:56.647"));
// list.add(new DataBean("EED006", "null", "Premium Refund(Motor)(USD)", "E",
// "2015-02-25 10:37:30.793"));
// list.add(new DataBean("EEE000", "null", "Commission Payment (Life) (To
// Agent)", "E", "2013-05-02 02:13:57.030"));
// list.add(new DataBean("EEE001", "null", "Agent Commission Payment (Life)",
// "E", "2013-05-02 02:14:05.750"));
// list.add(new DataBean("EEE002", "null", "Agent Commission Payment (Highway
// Travelling)", "E", "2014-05-09 10:06:37.547"));
// list.add(new DataBean("EEE003", "null", "Agent Comission Payment (Health)",
// "E", "2015-06-08 13:36:06.577"));
// list.add(new DataBean("EEF000", "null", "General Commission Payment (To
// Agent)", "E", "2013-05-02 02:14:24.687"));
// list.add(new DataBean("EEF001", "null", "Agent Com; Payment (Fire)", "E",
// "2013-05-02 02:14:39.620"));
// list.add(new DataBean("EEF002", "null", "Agent Com; Payment (Cash in
// Transit)", "E", "2013-05-02 02:14:47.480"));
// list.add(new DataBean("EEF003", "null", "Agent Com; Payment (Cash in Safe)",
// "E", "2013-05-02 23:08:59.790"));
// list.add(new DataBean("EEF004", "null", "Agent Com; Payment (Fidelity Bond)",
// "E", "2013-05-02 23:09:16.543"));
// list.add(new DataBean("EEF005", "null", "Agent Commission Payment (Motor)",
// "E", "2013-05-02 23:09:32.893"));
// list.add(new DataBean("EEF006", "null", "Agent Commission Payment
// (Fire)(USD)", "E", "2015-02-25 10:39:23.707"));
// list.add(new DataBean("EEF007", "null", "Agent Commission Payment
// (Motor)(USD)", "E", "2015-02-25 10:40:53.563"));
// list.add(new DataBean("EEF008", "null", "Agent Commission Payment (Marine
// Cargo)", "E", "2015-09-03 17:27:42.970"));
// list.add(new DataBean("EEF009", "null", "Agent Commission Payment (Inland
// Cargo)", "E", "2015-09-03 17:27:42.970"));
// list.add(new DataBean("EEG000", "null", "Assessor & Surveyor Charges", "E",
// "2013-06-20 15:01:31.397"));
// list.add(new DataBean("EEG001", "null", "Assessor & Surveyor Charges on
// Fire", "E", "2013-06-20 15:02:05.933"));
// list.add(new DataBean("EEG002", "null", "Assessor & Surveyor Charges on
// Motor", "E", "2013-06-20 15:02:22.063"));
// list.add(new DataBean("EEG003", "null", "Assessor & Surveyor Charges on
// Marine Cargo", "E", "2015-09-03 17:27:42.970"));
// list.add(new DataBean("EEG004", "null", "Assessor & Surveyor Charges on
// Inland Cargo", "E", "2015-09-03 17:27:42.970"));
// list.add(new DataBean("EEH000", "null", "Interest on Deposits, Borrowing
// etc", "E", "2013-05-02 02:16:35.620"));
// list.add(new DataBean("EEH001", "null", "Interest on Borrowings", "E",
// "2013-05-02 02:16:44.770"));
// list.add(new DataBean("EEH001", "null", "Sue & Labour Charges(Marine & Inland
// Cargo)", "E", "2015-09-03 17:27:42.970"));
// list.add(new DataBean("EEH002", "null", "Interest on Staff Funds", "E",
// "2013-05-02 02:16:53.653"));
// list.add(new DataBean("EEH003", "null", "Interest on Premium", "E",
// "2013-07-29 10:58:57.000"));
// list.add(new DataBean("EEI000", "null", "Re-Insurance Premium Expenses
// (Foreign)", "E", "2013-05-02 02:17:07.100"));
// list.add(new DataBean("EEI001", "null", "Re-Insurance Premium Expenses
// Account", "E", "2013-05-02 02:17:20.397"));
// list.add(new DataBean("EEJ000", "null", "Medical Examination Charges", "E",
// "2013-09-05 12:20:10.180"));
// list.add(new DataBean("EEJ001", "null", "Medical Examination Charges(Life)",
// "E", "2013-09-05 12:20:36.543"));
// list.add(new DataBean("EEK000", "null", "Marine Cargo Insurance Claims
// (INJECTED)", "E", "2015-09-03 17:27:42.970"));
// list.add(new DataBean("EEK001", "null", "Marine Cargo Insurance Claims", "E",
// "2015-09-03 17:27:42.970"));
// list.add(new DataBean("EEK002", "null", "Inland Cargo Insurance Claims", "E",
// "2015-09-03 17:27:42.970"));
// list.add(new DataBean("EEK003", "null", "Premium Refund (Marine Cargo)", "E",
// "2015-09-03 17:27:42.970"));
// list.add(new DataBean("EEK004", "null", "Premium Refund (Inland Cargo)", "E",
// "2015-09-03 17:27:42.970"));
// list.add(new DataBean("EEL000", "null", "External Audit Fees", "E",
// "2013-05-02 02:17:53.057"));
// list.add(new DataBean("EEL001", "null", "External Audit Fees", "E",
// "2013-05-02 02:18:12.890"));
// list.add(new DataBean("EEL101", "FIRE", "External Audit Fees(Fire)", "E",
// "2014-03-14 14:12:12.333"));
// list.add(new DataBean("EEL102", "LIFE", "External Audit Fees(Life)", "E",
// "2014-03-14 14:12:57.103"));
// list.add(new DataBean("EEL103", "MOTOR", "External Audit Fees(Motor)", "E",
// "2014-03-14 14:13:10.380"));
// list.add(new DataBean("EEM000", "null", "Maintenance and Repairs", "E",
// "2013-05-02 02:20:17.853"));
// list.add(new DataBean("EEM001", "null", "Land and Building Maintenance", "E",
// "2013-05-02 02:20:27.387"));
// list.add(new DataBean("EEM002", "null", "Furniture and Other Equipment
// Maintenance", "E", "2013-05-02 02:20:44.490"));
// list.add(new DataBean("EEM003", "null", "Office Equipment Maintenance", "E",
// "2013-05-02 02:20:56.583"));
// list.add(new DataBean("EEM004", "null", "Maintenance and Repairs (Office
// Vehicle/Motor Cycle)", "E", "2013-05-02 02:21:12.237"));
// list.add(new DataBean("EEM005", "null", "Maintenance and Repairs (Computer &
// Component)", "E", "2013-05-02 02:21:28.757"));
// list.add(new DataBean("EEM006", "null", "Repair To Building Lease By
// Company", "E", "2013-05-02 02:21:48.180"));
// list.add(new DataBean("EEM101", "FIRE", "Maintenence and Repairs(Fire)", "E",
// "2014-03-14 14:13:33.827"));
// list.add(new DataBean("EEM102", "LIFE", "Maintenance and Repairs(Life)", "E",
// "2014-03-14 14:14:02.313"));
// list.add(new DataBean("EEM103", "MOTOR", "Maintenance and Repairs(Motor)",
// "E", "2014-03-14 14:14:22.670"));
// list.add(new DataBean("EEN000", "null", "Depreciation", "E", "2013-05-02
// 02:21:55.497"));
// list.add(new DataBean("EEN001", "null", "Depreciation on Land and Building",
// "E", "2013-05-02 02:22:06.343"));
// list.add(new DataBean("EEN002", "null", "Depreciation on Furniture and Other
// Equipment", "E", "2013-05-02 02:22:19.357"));
// list.add(new DataBean("EEN003", "null", "Depreciation on Office Equipment",
// "E", "2013-05-02 02:22:34.520"));
// list.add(new DataBean("EEN004", "null", "Depreciation on Office Vehicle/Motor
// Cycle", "E", "2013-05-02 02:22:45.450"));
// list.add(new DataBean("EEN005", "null", "Depreciation on Computer &
// Component", "E", "2013-05-02 02:22:59.327"));
// list.add(new DataBean("EEN101", "FIRE", "Depreciation(Fire)", "E",
// "2014-03-14 14:18:16.093"));
// list.add(new DataBean("EEN102", "LIFE", "Depreciation (Life)", "E",
// "2014-03-14 14:18:30.680"));
// list.add(new DataBean("EEN103", "MOTOR", "Depreciation(Motor)", "E",
// "2014-03-14 14:18:44.143"));
// list.add(new DataBean("EEO000", "null", "Directors Fees and Expenses", "E",
// "2013-05-02 02:23:12.927"));
// list.add(new DataBean("EEO001", "null", "Directors Fees and Expenses", "E",
// "2013-05-02 02:23:31.187"));
// list.add(new DataBean("EEO002", "null", "Directors Meeting Allowance", "E",
// "2013-05-02 02:23:42.630"));
// list.add(new DataBean("EEO003", "null", "Chairman Expense", "E", "2013-05-02
// 02:23:53.390"));
// list.add(new DataBean("EEO101", "FIRE", "Directors Fees and Expenses (Fire)",
// "E", "2014-03-14 14:19:39.180"));
// list.add(new DataBean("EEO102", "LIFE", "Directors Fees and Expenses(Life)",
// "E", "2014-03-14 14:19:54.467"));
// list.add(new DataBean("EEO103", "MOTOR", "Directors Fees and
// Expenses(Motor)", "E", "2014-03-14 14:20:09.833"));
// list.add(new DataBean("EEP000", "null", "Establishment Salaries", "E",
// "2013-05-02 02:24:10.847"));
// list.add(new DataBean("EEP001", "null", "Wages & Salaries", "E", "2013-05-02
// 02:24:22.090"));
// list.add(new DataBean("EEP002", "null", "Provident Fund Contribution", "E",
// "2013-05-02 02:25:24.610"));
// list.add(new DataBean("EEP003", "null", "Fixed Travelling Allowance", "E",
// "2013-05-02 02:26:17.750"));
// list.add(new DataBean("EEP004", "null", "Service Allowance", "E", "2013-05-02
// 02:25:43.253"));
// list.add(new DataBean("EEP005", "null", "Technical Allowance", "E",
// "2013-05-02 02:25:55.037"));
// list.add(new DataBean("EEP006", "null", "Income Tax Contribution", "E",
// "2013-05-02 02:26:03.503"));
// list.add(new DataBean("EEP007", "null", "Staff Allowance", "E", "2013-05-02
// 02:26:27.600"));
// list.add(new DataBean("EEP008", "null", "Executive Allowance", "E",
// "2013-05-02 02:26:36.700"));
// list.add(new DataBean("EEP009", "null", "Performance Allowance", "E",
// "2013-05-02 02:26:47.417"));
// list.add(new DataBean("EEP010", "null", "Meal Allowance", "E", "2013-05-02
// 02:26:59.250"));
// list.add(new DataBean("EEP011", "null", "Staff Uniform", "E", "2013-05-02
// 02:27:09.777"));
// list.add(new DataBean("EEP012", "null", "Overtime - Staff", "E", "2013-05-02
// 02:27:18.833"));
// list.add(new DataBean("EEP013", "null", "Overtime - Motor Car", "E",
// "2013-05-02 02:27:29.347"));
// list.add(new DataBean("EEP014", "null", "Medical and Medicine Expenses", "E",
// "2013-05-02 02:27:39.213"));
// list.add(new DataBean("EEP015", "null", "Staff Welfare Expenses", "E",
// "2013-05-02 02:27:48.617"));
// list.add(new DataBean("EEP016", "null", "Bonus", "E", "2013-05-02
// 02:28:02.180"));
// list.add(new DataBean("EEP017", "null", "Social Security Board (SSB)", "E",
// "2013-09-27 13:23:06.950"));
// list.add(new DataBean("EEP018", "null", "Cost of Living Allowance", "E",
// "2013-10-30 12:01:18.770"));
// list.add(new DataBean("EEP019", "null", "Management Fees", "E", "2014-02-28
// 10:25:29.790"));
// list.add(new DataBean("EEP020", "null", "Life Premium Contribution for
// (Sub-Staff)", "E", "2015-04-23 15:56:04.960"));
// list.add(new DataBean("EEP101", "FIRE", "Establishment Salaries(Fire)", "E",
// "2014-03-14 14:21:20.393"));
// list.add(new DataBean("EEP102", "LIFE", "Establishment Salaries(Life)", "E",
// "2014-03-14 14:21:31.187"));
// list.add(new DataBean("EEP103", "MOTOR", "Establishment Salaries(Motor)",
// "E", "2014-03-14 14:21:43.263"));
// list.add(new DataBean("EEQ000", "null", "Foreign Exchange Adjustment (Loss)",
// "E", "2014-06-30 14:26:16.277"));
// list.add(new DataBean("EEQ001", "null", "Foreign Exchange Adjustment (Loss)",
// "E", "2014-06-30 14:26:27.540"));
// list.add(new DataBean("EEQ101", "FIRE", "Foreign Exchange Adjustment(Loss)
// Fire", "E", "2014-12-26 15:56:54.787"));
// list.add(new DataBean("EEQ102", "LIFE", "Foreign Exchange
// Adjustment(Loss)Life", "E", "2014-12-26 15:57:13.677"));
// list.add(new DataBean("EEQ103", "MOTOR", "Foreign Exchange
// Adjustment(Loss)Motor", "E", "2014-12-26 15:57:30.463"));
// list.add(new DataBean("EER000", "null", "Travelling Expenses", "E",
// "2013-05-02 02:28:36.580"));
// list.add(new DataBean("EER001", "null", "Passage & Travelling
// Exp;(Domestic)", "E", "2013-05-02 02:28:49.703"));
// list.add(new DataBean("EER002", "null", "Passing & Travelling Exp;
// (Foreign)", "E", "2013-05-02 02:29:01.887"));
// list.add(new DataBean("EER003", "null", "Hotel & Accomodation (Domestic)",
// "E", "2013-05-02 02:29:10.397"));
// list.add(new DataBean("EER004", "null", "Hotel and Accomodation (Foreign)",
// "E", "2013-05-02 02:29:27.880"));
// list.add(new DataBean("EER005", "null", "Hire Charges (Vehicle / Taxi)", "E",
// "2013-06-20 15:03:10.843"));
// list.add(new DataBean("EER101", "FIRE", "Travelling Expenses(fire)", "E",
// "2014-03-14 14:22:11.903"));
// list.add(new DataBean("EER102", "LIFE", "Travelling Expenses(Life)", "E",
// "2014-03-14 14:22:22.950"));
// list.add(new DataBean("EER103", "MOTOR", "Travelling Expenses(Motor)", "E",
// "2014-03-14 14:22:35.773"));
// list.add(new DataBean("EES000", "null", "Operation Expenses", "E",
// "2013-05-02 02:29:44.857"));
// list.add(new DataBean("EES001", "null", "Lighting & Power", "E", "2013-05-02
// 02:29:57.370"));
// list.add(new DataBean("EES002", "null", "Book Newspaper Periodical Exp;",
// "E", "2013-05-02 02:30:07.540"));
// list.add(new DataBean("EES003", "null", "Petrol & Diesel for Office Car/Motor
// Cycle", "E", "2013-05-02 02:30:22.397"));
// list.add(new DataBean("EES004", "null", "Petrol & Diesel for Outside Car",
// "E", "2013-05-02 02:30:38.470"));
// list.add(new DataBean("EES005", "null", "Petrol & Diesel for Machine", "E",
// "2013-05-02 02:30:48.560"));
// list.add(new DataBean("EES006", "null", "Software/Network Installation Cost",
// "E", "2013-05-02 02:31:01.190"));
// list.add(new DataBean("EES007", "null", "Rental Fees - Office", "E",
// "2013-05-02 02:31:13.237"));
// list.add(new DataBean("EES008", "null", "Rental Fees - Motor Cars", "E",
// "2013-05-02 02:31:20.780"));
// list.add(new DataBean("EES009", "null", "Rental Fees - Residence Foreigner",
// "E", "2013-05-02 02:31:34.363"));
// list.add(new DataBean("EES010", "null", "House Rental Fees - Staff", "E",
// "2013-05-02 02:31:46.143"));
// list.add(new DataBean("EES011", "null", "Transportation and Handling
// Charges", "E", "2013-05-02 02:31:55.373"));
// list.add(new DataBean("EES012", "null", "Printing, Stationery and Office
// Supplies", "E", "2013-05-02 02:32:04.793"));
// list.add(new DataBean("EES013", "null", "Internet, Email & TV Channel
// Expenses", "E", "2013-05-02 02:32:13.310"));
// list.add(new DataBean("EES014", "null", "Postage, Fax, Telegraph and
// Telephones", "E", "2013-05-02 02:32:31.590"));
// list.add(new DataBean("EES015", "null", "Insurance Premium on Land &
// Building", "E", "2013-07-17 13:09:53.340"));
// list.add(new DataBean("EES016", "null", "Insurance Premium on Motor Car",
// "E", "2013-07-17 13:10:18.143"));
// list.add(new DataBean("EES101", "FIRE", "Operation Expenses (Fire)", "E",
// "2014-03-14 14:23:04.350"));
// list.add(new DataBean("EES102", "LIFE", "Operation Expenses (life)", "E",
// "2014-03-14 14:23:17.517"));
// list.add(new DataBean("EES103", "MOTOR", "Operation Expenses (Motor)", "E",
// "2014-03-14 14:23:31.247"));
// list.add(new DataBean("EET000", "null", "Miscellaneous Expenses", "E",
// "2013-05-02 02:32:40.730"));
// list.add(new DataBean("EET001", "null", "Annual Party Expenses", "E",
// "2013-05-02 02:32:58.120"));
// list.add(new DataBean("EET002", "null", "Gift of Honor (Staff)", "E",
// "2013-05-02 02:33:19.787"));
// list.add(new DataBean("EET003", "null", "Entertainment Expenses
// (Shareholder)", "E", "2013-05-02 02:33:30.090"));
// list.add(new DataBean("EET004", "null", "Entertainment Expenses", "E",
// "2013-05-02 02:33:42.353"));
// list.add(new DataBean("EET005", "null", "Special Entertainment and Gift",
// "E", "2013-05-02 02:33:58.173"));
// list.add(new DataBean("EET006", "null", "Donation", "E", "2013-05-02
// 02:34:11.303"));
// list.add(new DataBean("EET007", "null", "HR Training Expenses", "E",
// "2013-05-02 02:34:23.220"));
// list.add(new DataBean("EET008", "null", "Advertising Expenses", "E",
// "2013-05-02 02:34:35.990"));
// list.add(new DataBean("EET009", "null", "Bank Charges", "E", "2013-05-02
// 02:34:53.273"));
// list.add(new DataBean("EET010", "null", "Seminar Expenses", "E", "2013-05-02
// 02:35:03.253"));
// list.add(new DataBean("EET011", "null", "Natural Disaster Aid Expenses", "E",
// "2013-05-02 02:35:12.923"));
// list.add(new DataBean("EET012", "null", "Generator Expenses", "E",
// "2013-05-02 02:35:23.410"));
// list.add(new DataBean("EET013", "null", "Office Utilities", "E", "2013-05-02
// 02:35:40.127"));
// list.add(new DataBean("EET014", "null", "Consulting Fees + Expenses", "E",
// "2013-05-02 02:35:51.463"));
// list.add(new DataBean("EET015", "null", "Advisory Fees (Domestic)", "E",
// "2013-05-02 02:36:04.243"));
// list.add(new DataBean("EET016", "null", "Legal Expenses", "E", "2013-05-02
// 02:36:13.560"));
// list.add(new DataBean("EET017", "null", "Discount Allowed on Treasury Bill",
// "E", "2013-05-02 02:36:25.947"));
// list.add(new DataBean("EET018", "null", "Office Property Write Off", "E",
// "2013-05-02 02:36:34.797"));
// list.add(new DataBean("EET019", "null", "Honoraria for Lectures and
// Examinations", "E", "2013-05-02 02:36:49.990"));
// list.add(new DataBean("EET020", "null", "Miscellaneous - (General) Expense",
// "E", "2013-05-02 02:37:04.010"));
// list.add(new DataBean("EET021", "null", "General Meeting & Other Ceremonies
// Expenses", "E", "2013-05-02 02:37:17.210"));
// list.add(new DataBean("EET022", "null", "Business Relation Expenses", "E",
// "2013-09-05 09:21:07.820"));
// list.add(new DataBean("EET101", "FIRE", "Miscellaneous Expenses(Fire)", "E",
// "2014-03-14 14:23:59.730"));
// list.add(new DataBean("EET102", "LIFE", "Miscellaneous Expenses(Life)", "E",
// "2014-03-14 14:24:11.137"));
// list.add(new DataBean("EET103", "MOTOR", "Miscellaneous Expenses(Motor)",
// "E", "2014-03-14 14:24:21.587"));
// list.add(new DataBean("EEU000", "null", "Rate & Taxes", "E", "2013-05-02
// 02:37:25.390"));
// list.add(new DataBean("EEU001", "null", "Vehicle Registration & Licenses
// Fees", "E", "2013-05-02 02:37:32.820"));
// list.add(new DataBean("EEU002", "null", "Rate & Taxes", "E", "2013-05-02
// 02:37:52.723"));
// list.add(new DataBean("EEU003", "null", "Co; Registration Fees + Secretarial
// Service", "E", "2013-05-02 02:38:14.807"));
// list.add(new DataBean("EEU004", "null", "Co Licences Renewal Annual Fees",
// "E", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("EEU101", "FIRE", "Rate & Taxes (Fire)", "E",
// "2014-03-14 14:24:56.610"));
// list.add(new DataBean("EEU102", "LIFE", "Rate & Taxes (Life)", "E",
// "2014-03-14 14:25:07.920"));
// list.add(new DataBean("EEU103", "MOTOR", "Rate & Taxes (Motor)", "E",
// "2014-03-14 14:25:18.840"));
// list.add(new DataBean("EEV000", "null", "Income Tax ( Revenue )", "E",
// "2013-05-02 00:00:00.000"));
// list.add(new DataBean("EEV001", "null", "Income Tax", "E", "2013-05-02
// 00:00:00.000"));
// list.add(new DataBean("EEV101", "FIRE", "Income Tax (Revenue) (Fire)", "E",
// "2014-03-14 14:25:44.690"));
// list.add(new DataBean("EEV102", "LIFE", "Income Tax (Revenue) (Life)", "E",
// "2014-03-14 14:25:56.403"));
// list.add(new DataBean("EEV103", "MOTOR", "Income Tax (Revenue) (Motor)", "E",
// "2014-03-14 14:26:25.747"));
// list.add(new DataBean("EEW000", "null", "Commercial Tax", "E", "2013-05-02
// 00:00:00.000"));
// list.add(new DataBean("EEW001", "null", "Commercial Tax (General Premium
// Income)", "E", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("EEW002", "null", "Commercial Tax (Operation)", "E",
// "2015-06-04 10:58:46.210"));
// list.add(new DataBean("EEW101", "FIRE", "Commercial Tax (Fire", "E",
// "2014-03-31 20:34:55.187"));
// list.add(new DataBean("EEW103", "MOTOR", "Commercial Tax (Motor)", "E",
// "2014-03-31 20:35:10.693"));
// list.add(new DataBean("EEX000", "null", "Government Stamps", "E", "2013-05-02
// 00:00:00.000"));
// list.add(new DataBean("EEX001", "null", "Insurance Stamp (On Policy)", "E",
// "2013-05-02 00:00:00.000"));
// list.add(new DataBean("EEX002", "null", "Revenue Stamp", "E", "2013-05-02
// 00:00:00.000"));
// list.add(new DataBean("EEX003", "null", "Share Transfer Stamp & Expenses",
// "E", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("EEX101", "FIRE", "Government Stamps (Fire)", "E",
// "2014-03-14 14:27:55.340"));
// list.add(new DataBean("EEX102", "LIFE", "Government Stamps(Life)", "E",
// "2014-03-14 14:28:07.163"));
// list.add(new DataBean("EEX103", "MOTOR", "Government Stamps (Motor)", "E",
// "2014-03-14 14:29:22.120"));
// list.add(new DataBean("EEY000", "null", "Opening Office Expenses", "E",
// "2013-05-02 00:00:00.000"));
// list.add(new DataBean("EEY001", "null", "Opening Office Expenses", "E",
// "2013-05-02 00:00:00.000"));
// list.add(new DataBean("EEY101", "FIRE", "Opening Office Expenses(Fire)", "E",
// "2014-03-14 14:29:47.970"));
// list.add(new DataBean("EEY102", "LIFE", "Opening Office Expenses(Life)", "E",
// "2014-03-14 14:29:59.170"));
// list.add(new DataBean("EEY103", "MOTOR", "Opening Office Expenses (Motor)",
// "E", "2014-03-14 14:30:10.467"));
// list.add(new DataBean("EEZ000", "null", "Loss on Sale of Fixed Assets", "E",
// "2013-05-02 00:00:00.000"));
// list.add(new DataBean("EEZ001", "null", "Loss on Sale of Fixed Assets", "E",
// "2013-05-02 00:00:00.000"));
// list.add(new DataBean("I00000", "null", "Income", "I", "2013-05-02
// 01:33:23.260"));
// list.add(new DataBean("IA0000", "null", "Income Account", "I", "2013-05-02
// 01:33:48.517"));
// list.add(new DataBean("IAB000", "null", "Interest on Investment", "I",
// "2013-05-02 01:34:02.980"));
// list.add(new DataBean("IAB001", "null", "Interest on Saving Account", "I",
// "2013-05-02 01:34:15.357"));
// list.add(new DataBean("IAB002", "null", "Interest on Fixed Deposit Account",
// "I", "2013-05-02 01:34:28.567"));
// list.add(new DataBean("IAB003", "null", "", "I", "2013-05-02 01:34:41.850"));
// list.add(new DataBean("IAB004", "null", "Divident on Share Purchase", "I",
// "2013-05-02 01:34:57.450"));
// list.add(new DataBean("IAB005", "null", "Interest on Treasury Bill (CBM)",
// "I", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("IAB006", "null", "Interest on Treasury Bond (MSEC)",
// "I", "2013-05-02 01:35:14.713"));
// list.add(new DataBean("IAB007", "null", "Interest on Capital Investment
// (Motor)", "I", "2013-05-02 01:35:30.080"));
// list.add(new DataBean("IAB008", "null", "Interest on Capital Investment
// (Life)", "I", "2013-05-02 01:35:47.267"));
// list.add(new DataBean("IAB009", "null", "Interest on Capital Investment
// (Fire)", "I", "2013-06-28 10:19:27.447"));
// list.add(new DataBean("IAC000", "null", "Commission & services
// (Others)Income", "I", "2013-05-02 01:36:07.190"));
// list.add(new DataBean("IAC001", "null", "Commission & Service Income (Life)",
// "I", "2013-05-02 01:36:20.467"));
// list.add(new DataBean("IAC002", "null", "Commission & Service Income (Fire &
// General)", "I", "2013-04-30 13:40:39.420"));
// list.add(new DataBean("IAC003", "null", "Commission & Service Income
// (Motor)", "I", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("IAC004", "null", "Commis; & Service
// Income(Motor)(USD)", "I", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("IAC005", "null", "Other Commis; & Service Income",
// "I", "2015-02-25 10:05:21.273"));
// list.add(new DataBean("IAC006", "null", "Commis; & Service
// Income(Fire)(USD)", "I", "2015-02-25 10:06:46.433"));
// list.add(new DataBean("IAC007", "null", "Commis; & Service
// Inco;(Reinsurance)(USD)", "I", "2015-02-25 10:07:58.740"));
// list.add(new DataBean("IAC008", "null", "Commission & Service Income (Marine
// Cargo)", "I", "2015-09-03 17:27:42.970"));
// list.add(new DataBean("IAC009", "null", "Commission & Service Income (Inland
// Cargo)", "I", "2015-09-03 17:27:42.970"));
// list.add(new DataBean("IAD000", "null", "Commission refund by Agent", "I",
// "2013-04-30 13:41:15.530"));
// list.add(new DataBean("IAD001", "null", "Commission refund by Agent ( Life
// )", "I", "2013-04-30 13:41:40.640"));
// list.add(new DataBean("IAD002", "null", "Commission refund by Agent ( Fire +
// General )", "I", "2013-04-30 13:41:57.090"));
// list.add(new DataBean("IAD003", "null", "Commission refund by Agent ( Motor
// )", "I", "2013-04-30 13:42:11.903"));
// list.add(new DataBean("IAD004", "null", "Commissi; refund by
// Agent(Motor)(USD)", "I", "2015-02-25 10:10:10.933"));
// list.add(new DataBean("IAD005", "null", "Commissi; refund by
// Agent(Fire)(USD)", "I", "2015-02-25 10:10:58.530"));
// list.add(new DataBean("IAD006", "null", "Commission refund by Agent ( Marine
// cargo )", "I", "2015-09-03 17:27:42.970"));
// list.add(new DataBean("IAD007", "null", "Commission refund by Agent ( Inland
// Cargo )", "I", "2015-09-03 17:27:42.970"));
// list.add(new DataBean("IAG000", "null", "General Premium Income", "I",
// "2013-05-02 01:38:26.230"));
// list.add(new DataBean("IAG001", "null", "Fire Insurance Premium", "I",
// "2013-05-02 01:38:37.400"));
// list.add(new DataBean("IAG002", "null", "(Fire) Co. Insurance Premium
// Income", "I", "2013-05-02 01:38:50.223"));
// list.add(new DataBean("IAG003", "null", "Cash in Safe Insurance Premium",
// "I", "2013-05-02 01:39:03.077"));
// list.add(new DataBean("IAG004", "null", "Fidelity Bond Insurance Premium",
// "I", "2013-05-02 01:39:18.233"));
// list.add(new DataBean("IAG005", "null", "Cash in Transit Insurance Premium",
// "I", "2013-07-02 15:24:24.787"));
// list.add(new DataBean("IAG006", "null", "(Fire) Insur; Premium Income(USD)",
// "I", "2015-02-25 10:15:04.340"));
// list.add(new DataBean("IAH000", "null", "Marine and Aviation Insurance
// Income", "I", "2013-05-02 01:39:50.670"));
// list.add(new DataBean("IAH001", "null", "Marine Cargo Insurance premium
// Income", "I", "2015-09-03 17:27:42.970"));
// list.add(new DataBean("IAH002", "null", "Inland Cargo Insurance premium
// Income", "I", "2015-09-03 17:27:42.970"));
// list.add(new DataBean("IAL000", "null", "Life Insurance Premium Income", "I",
// "2013-05-02 01:36:53.647"));
// list.add(new DataBean("IAL001", "null", "Life Insurance Premium", "I",
// "2013-05-02 01:37:11.407"));
// list.add(new DataBean("IAL002", "null", "Group Insurance Premium", "I",
// "2013-05-02 01:37:22.753"));
// list.add(new DataBean("IAL003", "null", "Sportmen Insurance Premium", "I",
// "2013-05-02 01:37:34.723"));
// list.add(new DataBean("IAL004", "null", "Public Special Life Insurance
// Premium Income on High Way Travelling", "I", "2013-05-02 01:37:48.837"));
// list.add(new DataBean("IAL005", "null", "Health Insurance Premium Income",
// "I", "2013-05-02 01:38:00.927"));
// list.add(new DataBean("IAL006", "null", "(Seaman) Insurance Premium Income",
// "I", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("IAL007", "null", "Snake-Bite Premium Income", "I",
// "2013-05-02 00:00:00.000"));
// list.add(new DataBean("IAL008", "null", "Premium Suspense", "I", "2013-05-02
// 01:38:11.833"));
// list.add(new DataBean("IAN000", "null", "Motor Comprehensive Insurance
// Premium Income (Accident and Misc)", "I", "2013-05-02 01:40:50.097"));
// list.add(new DataBean("IAN001", "null", "Accident and Misc Insurance
// Premium", "I", "2013-05-02 01:41:05.103"));
// list.add(new DataBean("IAN002", "null", "Accident and Misc Insur; Premium
// (USD)", "I", "2013-07-02 15:50:11.483"));
// list.add(new DataBean("IAN003", "null", "(Motor) Co Insurance Premium
// Income", "I", "2015-07-08 16:38:51.593"));
// list.add(new DataBean("IAR000", "null", "Co-insurance Commission", "I",
// "2013-05-02 01:42:35.370"));
// list.add(new DataBean("IAR001", "null", "Co-insurance Commission (Life)",
// "I", "2013-05-02 01:42:49.187"));
// list.add(new DataBean("IAR002", "null", "Co-insurance Commission (Fire)",
// "I", "2013-05-02 01:43:04.087"));
// list.add(new DataBean("IAR003", "null", "Co-insurance Commission (Motor)",
// "I", "2013-05-02 01:43:55.000"));
// list.add(new DataBean("IRP000", "null", "Service Charges on Share Transfer",
// "I", "2014-08-25 11:35:54.687"));
// list.add(new DataBean("IRR000", "null", "Share Certificate Fee", "I",
// "2014-08-22 15:29:16.510"));
// list.add(new DataBean("IRR001", "null", "Share Certificate Fee", "I",
// "2014-08-22 15:24:18.410"));
// list.add(new DataBean("IRR101", "FIRE", "Share Certificate Fee (Fire)", "I",
// "2015-03-31 20:28:41.553"));
// list.add(new DataBean("IRR102", "LIFE", "Share Certificate Fee (Life)", "I",
// "2015-03-31 20:28:51.817"));
// list.add(new DataBean("IRR103", "MOTOR", "Share Certificate Fee (Motor)",
// "I", "2015-03-31 20:29:01.710"));
// list.add(new DataBean("IRS000", "null", "Other Income", "I", "2013-05-02
// 01:43:13.983"));
// list.add(new DataBean("IRS001", "null", "Disposal of Assets Proceed", "I",
// "2013-05-02 01:43:31.747"));
// list.add(new DataBean("IRS002", "null", "Income From Motor Parts Scrap Sales/
// Reinstatement Income/ Miscellaneous Income", "I", "2013-05-02
// 01:43:55.380"));
// list.add(new DataBean("IRS003", "null", "Other Income", "I", "2013-05-02
// 01:43:55.000"));
// list.add(new DataBean("IRS004", "null", "Other Income USD", "I", "2013-05-02
// 01:43:55.000"));
// list.add(new DataBean("IRS005", "null", "Penalty Income ( Staff )", "I",
// "2013-09-11 14:59:23.107"));
// list.add(new DataBean("IRS006", "null", "Income from Scrap Sales (Fire)",
// "I", "2014-06-30 14:17:09.607"));
// list.add(new DataBean("IRS007", "null", "Income From Scrap
// Sales(Marine/Inland Cargo)", "I", "2015-09-03 17:27:42.970"));
// list.add(new DataBean("IRS101", "FIRE", "Other Income (Fire)", "I",
// "2014-03-14 14:31:37.530"));
// list.add(new DataBean("IRS102", "LIFE", "Other Income (Life)", "I",
// "2014-03-14 14:33:26.607"));
// list.add(new DataBean("IRS103", "MOTOR", "Other Income(Motor)", "I",
// "2014-03-14 14:32:06.080"));
// list.add(new DataBean("IRT000", "null", "Foreign Exchange Adjustment (Gain)",
// "I", "2014-06-30 14:19:53.200"));
// list.add(new DataBean("IRT001", "null", "Foreign Exchange Adjustment (Gain)",
// "I", "2014-06-30 14:20:10.300"));
// list.add(new DataBean("IRT101", "FIRE", "Foreign Exchange
// Adjustment(Gain)Fire", "I", "2014-12-26 15:55:47.877"));
// list.add(new DataBean("IRT102", "LIFE", "Foreign Exchange
// Adjustment(Gain)Life", "I", "2014-12-26 15:56:04.710"));
// list.add(new DataBean("IRT103", "MOTOR", "Foreign Exchange
// Adjustment(Gain)Motor", "I", "2014-12-26 15:56:19.077"));
// list.add(new DataBean("L00000", "null", "Liabilities", "L", "2013-05-02
// 01:23:30.483"));
// list.add(new DataBean("LAC000", "null", "Paid Up Share Capital", "L",
// "2013-05-02 01:23:53.737"));
// list.add(new DataBean("LAC001", "null", "Share Capital Account", "L",
// "2013-05-02 01:24:09.473"));
// list.add(new DataBean("LAF000", "null", "Share Premium Account", "L",
// "2014-06-30 14:05:00.537"));
// list.add(new DataBean("LAF001", "null", "Share Premium Account", "L",
// "2014-06-30 14:06:00.910"));
// list.add(new DataBean("LBG000", "null", "General Reserve Fund", "L",
// "2013-05-02 01:24:45.613"));
// list.add(new DataBean("LBG001", "null", "General Reserve Fund (10%)", "L",
// "2013-05-02 01:24:56.593"));
// list.add(new DataBean("LBH000", "null", "Policy Holder Protection Fund", "L",
// "2014-03-14 14:37:50.510"));
// list.add(new DataBean("LBH001", "null", "Policy Holder Protection Fund (5%)",
// "L", "2014-03-14 14:37:59.933"));
// list.add(new DataBean("LBK000", "null", "Life Insurance Fund", "L",
// "2013-07-23 12:09:28.193"));
// list.add(new DataBean("LBK001", "null", "Life Insurance fund", "L",
// "2013-07-23 12:09:54.667"));
// list.add(new DataBean("LBM000", "null", "Reserve For Revenue Taxation A/C",
// "L", "2014-03-14 14:36:16.333"));
// list.add(new DataBean("LBM001", "null", "Reserve For Revenue Taxation A/C",
// "L", "2014-03-14 14:36:48.110"));
// list.add(new DataBean("LBR000", "null", "General Insurance fund", "L",
// "2013-05-02 01:25:07.230"));
// list.add(new DataBean("LBR001", "null", "(Motor) General Insurance Fund",
// "L", "2013-07-29 11:04:01.250"));
// list.add(new DataBean("LBR002", "null", "(Fire) General Insurance Fund", "L",
// "2013-05-02 01:25:40.517"));
// list.add(new DataBean("LBR003", "null", "Marine And Aviation Insurance Fund",
// "L", "2013-05-02 01:25:54.427"));
// list.add(new DataBean("LBT000", "null", "Staff Fund Account", "L",
// "2013-05-02 01:26:21.890"));
// list.add(new DataBean("LBT001", "null", "Staff Provident Fund Account", "L",
// "2013-05-02 01:26:38.057"));
// list.add(new DataBean("LBW000", "null", "Provision For Divident Payment",
// "L", "2013-05-02 01:26:57.757"));
// list.add(new DataBean("LBW001", "null", "Provision For Divident Payment",
// "L", "2013-05-02 01:27:11.500"));
// list.add(new DataBean("LKP000", "null", "Payment Order Account", "L",
// "2013-05-02 01:27:23.963"));
// list.add(new DataBean("LKP001", "null", "Payment Order (Government)", "L",
// "2013-05-02 01:27:41.317"));
// list.add(new DataBean("LKP002", "null", "Payment Order (Private)", "L",
// "2013-05-02 01:27:54.813"));
// list.add(new DataBean("LMD000", "null", "Sundry Deposit Account (Domestic)",
// "L", "2013-05-02 01:28:15.120"));
// list.add(new DataBean("LMD001", "null", "Sundry Deposit Co Insurance Premium
// Income ( Life on High Way Travelling )", "L", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("LMD002", "null", "Sundry Deposit Co Ins; Premium
// (Fire)", "L", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("LMD003", "null", "Sundry Deposit Co Insurance Premium
// Income (Motor)", "L", "2013-05-02 01:28:31.143"));
// list.add(new DataBean("LMD004", "null", "Sundry Deposit Item Awaiting
// Classifi;", "L", "2013-05-02 01:28:44.057"));
// list.add(new DataBean("LMD005", "null", "Sundry Deposit Item Due and Unpaid
// Account", "L", "2013-05-02 01:29:01.040"));
// list.add(new DataBean("LMD006", "null", "Sundry Deposit Reserve for
// Commercial Tax", "L", "2013-05-02 01:29:11.053"));
// list.add(new DataBean("LMD007", "null", "Sundry Deposit Surplus Cash
// Account", "L", "2013-05-02 01:29:27.333"));
// list.add(new DataBean("LMD008", "null", "Sundry Deposit Share Capital in
// foreign Currency Account", "L", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("LMD009", "null", "Sundry Deposit Income Tax Payable
// (Staff)", "L", "2013-05-15 11:15:18.777"));
// list.add(new DataBean("LMD010", "null", "Sundry Deposit Pre Insurance Premium
// Receive", "L", "2014-05-09 10:04:26.740"));
// list.add(new DataBean("LMD011", "null", "Sundry Deposit A/C-Miscellaneous",
// "L", "2013-11-12 14:59:37.217"));
// list.add(new DataBean("LMD012", "null", "Sundry Deposit A/C (Exchange
// Revaluation)", "L", "2014-11-28 13:02:25.683"));
// list.add(new DataBean("LMD013", "null", "Sundry Deposit
// A/C(Miscellaneous)(USD)", "L", "2015-02-25 09:50:21.667"));
// list.add(new DataBean("LMD014", "null", "Surplus Cash Account (USD)", "L",
// "2015-02-25 09:51:46.560"));
// list.add(new DataBean("LMD015", "null", "Sundry Deposit Stamp Duty
// Recovered", "L", "2015-09-03 16:13:05.823"));
// list.add(new DataBean("LND000", "null", "Sundry Deposit Account (Foreign)",
// "L", "2013-05-02 01:30:05.840"));
// list.add(new DataBean("LND001", "null", "Due To Foreign Re-Insurance Company
// And Insurance Fire and General Account", "L", "2013-05-02 01:31:10.260"));
// list.add(new DataBean("LNF000", "null", "Sundry Creditor (Domestic)", "L",
// "2013-05-02 00:00:00.000"));
// list.add(new DataBean("LNF001", "null", "Borrowing from STG", "L",
// "2013-05-02 00:00:00.000"));
// list.add(new DataBean("LPA000", "null", "Outstanding Claims", "L",
// "2013-05-02 01:31:53.547"));
// list.add(new DataBean("LPA001", "null", "Outstanding Insurance Claim (Life)",
// "L", "2013-05-02 01:32:08.550"));
// list.add(new DataBean("LPA002", "null", "Outstanding Insurance Claim Account
// (Fire & General)", "L", "2013-05-02 01:32:26.893"));
// list.add(new DataBean("LPA003", "null", "Outstanding Insurance Claim
// (Motor)", "L", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("LPA004", "null", "Outstanding Insurance Claim Account
// ()", "L", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("LPA005", "null", "Outstanding Insurance Claim
// A/C(Fiire)(USD)", "L", "2015-02-25 09:54:56.133"));
// list.add(new DataBean("LPA006", "null", "Outstanding Insurance Claim
// A/C(Motor)(USD)", "L", "2015-02-25 09:56:24.943"));
// list.add(new DataBean("LPA007", "null", "Oustanding Insurance Claim
// Account(Marine Cargo)", "L", "2015-09-03 17:27:42.923"));
// list.add(new DataBean("LPA008", "null", "Oustanding Insurance Claim
// Account(Inland Cargo)", "L", "2015-09-03 17:27:42.953"));
// list.add(new DataBean("LPB000", "null", "Outstanding Insurance Commission
// Payable", "L", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("LPB001", "null", "Ins; Com; Payable (Life Agent)",
// "L", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("LPB002", "null", "Ins; Com; Payable (Fire & General
// Agent)", "L", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("LPB003", "null", "Ins; Com; Payable (Motor Agent)",
// "L", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("LPB004", "null", "Co.Insurance Commission Payable
// (Fire)", "L", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("LPB005", "null", "Insur; Commiss; Payable(Fire)(USD)",
// "L", "2013-07-03 13:38:23.973"));
// list.add(new DataBean("LPB006", "null", "Insur; Commis; Payable(Motor)(USD)",
// "L", "2015-02-25 10:00:43.780"));
// list.add(new DataBean("LPB007", "null", "Ins; Commission Payable(Marine
// Cargo)", "L", "2015-09-03 17:27:42.953"));
// list.add(new DataBean("LPB008", "null", "Insurance Commission Payable(Inland
// Cargo)", "L", "2015-09-03 17:27:42.970"));
// list.add(new DataBean("LPL000", "null", "Foreign Currency Control Account",
// "L", "2013-05-10 12:15:03.943"));
// list.add(new DataBean("LPL001", "null", "Foreign Currency Control Account",
// "L", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("LPM000", "null", "Technical Account", "L", "2013-05-02
// 00:00:00.000"));
// list.add(new DataBean("LPM001", "null", "Technical Account (Life)", "L",
// "2013-05-02 00:00:00.000"));
// list.add(new DataBean("LPM002", "null", "Technical Account (Fire & General)",
// "L", "2013-05-02 00:00:00.000"));
// list.add(new DataBean("LPM003", "null", "Technical Account (Motor)", "L",
// "2013-05-02 00:00:00.000"));
// list.add(new DataBean("LPM004", "null", "Technical Account(Marine & Inland
// Cargo", "L", "2015-09-03 17:27:42.970"));
// list.add(new DataBean("LPM004", "null", "Technical Account ( )", "L",
// "2013-05-10 12:17:00.640"));
// list.add(new DataBean("LPN000", "null", "Profit & Loss Appropriation", "L",
// "2013-04-24 10:51:26.190"));
// list.add(new DataBean("LPN001", "null", "Profit & Loss Account (Life)", "L",
// "2013-04-24 10:51:59.990"));
// list.add(new DataBean("LPN002", "null", "Profit & Loss Account (Fire &
// General)", "L", "2013-05-10 12:18:05.523"));
// list.add(new DataBean("LPN003", "null", "Profit & Loss Account", "L",
// "2013-05-10 12:20:28.997"));
// list.add(new DataBean("LPN004", "null", "Profit & Loss Account", "L",
// "2013-05-10 13:08:51.070"));
// }
//
// private class DataBean {
// private String acCode;
// private String dCode;
// private String acName;
// private String acType;
// private String pDate;
//
// public DataBean(String acCode, String dCode, String acName, String acType,
// String pDate) {
// this.acCode = acCode;
// this.dCode = dCode;
// this.acName = acName;
// this.acType = acType;
// this.pDate = pDate;
// }
//
// public String getAcCode() {
// return acCode;
// }
//
// public void setAcCode(String acCode) {
// this.acCode = acCode;
// }
//
// public String getAcName() {
// return acName;
// }
//
// public void setAcName(String acName) {
// this.acName = acName;
// }
//
// public String getAcType() {
// return acType;
// }
//
// public void setAcType(String acType) {
// this.acType = acType;
// }
//
// public String getpDate() {
// return pDate;
// }
//
// public void setpDate(String pDate) {
// this.pDate = pDate;
// }
//
// }
// }
