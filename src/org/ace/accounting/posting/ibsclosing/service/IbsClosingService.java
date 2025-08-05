package org.ace.accounting.posting.ibsclosing.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.posting.ibsclosing.service.interfaces.IibsClosingService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.AccountType;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.chartaccount.persistence.interfaces.ICcoaDAO;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.rateinfo.RateInfo;
import org.ace.accounting.system.rateinfo.RateType;
import org.ace.accounting.system.rateinfo.persistence.interfaces.IRateInfoDAO;
import org.ace.accounting.system.tlf.TLF;
import org.ace.accounting.system.tlf.persistence.interfaces.ITLFDAO;
import org.ace.accounting.system.tlf.service.interfaces.ITLFService;
import org.ace.accounting.system.trantype.TranCode;
import org.ace.accounting.system.trantype.TranType;
import org.ace.accounting.system.trantype.persistence.interfaces.ITranTypeDAO;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "IbsClosingService")
public class IbsClosingService implements IibsClosingService {
	@Resource(name = "TLFDAO")
	private ITLFDAO tlfDAO;

	@Resource(name = "CcoaDAO")
	private ICcoaDAO ccoaDAO;

	@Resource(name = "RateInfoDAO")
	private IRateInfoDAO rateInfoDAO;

	@Resource(name = "TranTypeDAO")
	private ITranTypeDAO tranTypeDAO;

	@Resource(name = "TLFService")
	private ITLFService tLFService;

	private List<TLF> createTLFListForClosing(List<CurrencyChartOfAccount> ccoaList, List<CurrencyChartOfAccount> interbranchCCOAList, Branch closingBranch) {
		List<TLF> tlfList = new ArrayList<>();

		BigDecimal homeAmount;
		BigDecimal localAmount;
		String narration;
		TranType DTranType;
		TranType CTranType;
		Currency currency;
		BigDecimal homeExchangeRate;
		Branch branch;
		List<RateInfo> rateList;
		boolean reverse;
		boolean paid;
		String referenceType;
		Date settlementDate;

		rateList = rateInfoDAO.findAll();
		DTranType = tranTypeDAO.findByTransCode(TranCode.TRDEBIT);
		CTranType = tranTypeDAO.findByTransCode(TranCode.TRCREDIT);
		homeExchangeRate = BigDecimal.ONE;
		narration = "T/R to HO for Final closing";
		branch = closingBranch;
		reverse = false;
		paid = true;
		referenceType = "IBS_CLOSING";
		settlementDate = new Date();
		Date budgetStartDate = BusinessUtil.getBudgetStartDate();
		Date budgetEndDate = BusinessUtil.getBudgetEndDate();
		int month = (DateUtils.monthsBetween(budgetStartDate, budgetEndDate, false, false)) + 1;

		String cVoucherNo = tLFService.getVoucherNoForVocherTypes(TranCode.TRCREDIT);
		String dVoucherNo = tLFService.getVoucherNoForVocherTypes(TranCode.TRDEBIT);

		for (CurrencyChartOfAccount ccoa : ccoaList) {
			String voucherNo = ccoa.getCoa().getAcType().equals(AccountType.I) ? cVoucherNo : dVoucherNo;
			TranType tranType = ccoa.getCoa().getAcType().equals(AccountType.I) ? CTranType : DTranType;
			TranType iTranType = ccoa.getCoa().getAcType().equals(AccountType.I) ? DTranType : CTranType;

			/*
			 * if (ccoa.getCurrency().getIsHomeCur()) { homeAmount =
			 * ccoa.getMonthlyRate().getM12(); } else { homeExchangeRate =
			 * rateList.stream().filter(rateInfo ->
			 * rateInfo.getCurrency().equals(ccoa.getCurrency()))
			 * .filter(rateInfo ->
			 * rateInfo.getRateType().equals(RateType.TT)).findAny().get().
			 * getExchangeRate(); homeAmount =
			 * ccoa.getMonthlyRate().getM12().multiply(homeExchangeRate); }
			 * 
			 * localAmount = ccoa.getMonthlyRate().getM12();
			 */
			/*
			 * localAmount = ccoa.getMsrcRate().getMsrc12(); homeAmount =
			 * ccoa.getMsrcRate().getMsrc12(); currency = ccoa.getCurrency();
			 */
			localAmount = changeMonth(month, ccoa);
			homeAmount = localAmount;
			currency = ccoa.getCurrency();

			TLF tlf = new TLF(ccoa, voucherNo, homeAmount, localAmount, narration, iTranType, currency, homeExchangeRate, branch, reverse, paid, referenceType, settlementDate);
			CurrencyChartOfAccount iCCOA = null;

			for (CurrencyChartOfAccount c : interbranchCCOAList) {
				if (c.getCurrency().equals(currency)) {
					iCCOA = c;
					break;
				}
			}

			TLF iTlf = new TLF(iCCOA, voucherNo, homeAmount, localAmount, narration, tranType, currency, homeExchangeRate, branch, reverse, paid, referenceType, settlementDate);

			tlfList.add(tlf);
			tlfList.add(iTlf);

		}

		return tlfList;
	}

	private List<TLF> createTLFListForConso(List<TLF> ieList, List<CurrencyChartOfAccount> interbranchCCOAList, Branch hoBranch) {
		List<TLF> tlfList = new ArrayList<>();

		List<TLF> iList = new ArrayList<>();
		List<TLF> eList = new ArrayList<>();

		String narration;
		TranType DTranType;
		TranType CTranType;
		Currency currency;
		BigDecimal homeExchangeRate;
		String closingBranchName;
		boolean reverse;
		boolean paid;
		String referenceType;
		Date settlementDate;

		DTranType = tranTypeDAO.findByTransCode(TranCode.TRDEBIT);
		CTranType = tranTypeDAO.findByTransCode(TranCode.TRCREDIT);
		homeExchangeRate = BigDecimal.ONE;
		closingBranchName = ieList.get(0).getBranch().getName();
		narration = "T/R from " + closingBranchName + " for Final closing";

		reverse = false;
		paid = true;
		referenceType = "HO_IBS_CLOSING";
		settlementDate = new Date();

		String iVoucherNo = tLFService.getVoucherNoForVocherTypes(TranCode.TRCREDIT);
		String eVoucherNo = tLFService.getVoucherNoForVocherTypes(TranCode.TRDEBIT);

		currency = ieList.get(0).getCurrency();

		// add value to reverseTlfList
		Function<TLF, TLF> tlfMapper = (tlf -> {
			tlf.setNarration(narration);
			tlf.setReferenceType(referenceType);
			tlf.setSettlementDate(settlementDate);
			tlf.setBasicEntity(null);
			tlf.setTranType(tlf.getCcoa().getCoa().getAcType().equals(AccountType.I) ? CTranType : DTranType);
			tlf.seteNo(tlf.getCcoa().getCoa().getAcType().equals(AccountType.I) ? iVoucherNo : eVoucherNo);
			tlf.setBranch(hoBranch);
			return tlf;
		});

		tlfList = ieList.stream().map(TLF::new).map(tlfMapper).collect(Collectors.toList());

		Predicate<TLF> isIncomeAccount = (tlf -> tlf.getCcoa().getCoa().getAcType().equals(AccountType.I));
		iList = ieList.stream().filter(isIncomeAccount).collect(Collectors.toList());
		eList = ieList.stream().filter(isIncomeAccount.negate()).collect(Collectors.toList());
		BigDecimal iTotalAmount = BigDecimal.ZERO;
		BigDecimal eTotalAmount = BigDecimal.ZERO;

		iTotalAmount = iList.stream().map(i -> i.getHomeAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
		eTotalAmount = eList.stream().map(e -> e.getHomeAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);

		CurrencyChartOfAccount iCCOA = null;
		for (CurrencyChartOfAccount c : interbranchCCOAList) {
			if (c.getCurrency().equals(currency)) {
				iCCOA = c;
				break;
			}
		}

		TLF iTlf = new TLF(iCCOA, iVoucherNo, iTotalAmount, iTotalAmount, narration, DTranType, currency, homeExchangeRate, hoBranch, reverse, paid, referenceType, settlementDate);
		tlfList.add(iTlf);
		TLF eTlf = new TLF(iCCOA, eVoucherNo, eTotalAmount, eTotalAmount, narration, CTranType, currency, homeExchangeRate, hoBranch, reverse, paid, referenceType, settlementDate);
		tlfList.add(eTlf);

		return tlfList;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public double getExchangeRate(Currency currency, RateType rateType, Date date) throws SystemException {
		double exchangeRate = 0.0;
		try {
			// RateInfo result = null;
			if (currency.getIsHomeCur()) {
				exchangeRate = 1;
			} else {
				exchangeRate = rateInfoDAO.findExchangeRateBy(currency, rateType, DateUtils.formatDate(date));
				// exchangeRate = result == null ? 0 :
				// result.getExchangeRate().doubleValue();
			}
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to Find ExchangeRate", e);
		}
		return exchangeRate;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void createIbsClosing(Branch closingBranch, Branch ibsBranch, ChartOfAccount iCOA) throws SystemException {
		try {

			List<CurrencyChartOfAccount> ccoaList = new ArrayList<>();
			List<TLF> tlfList = new ArrayList<>();

			// Delete all tlf which reference type is 'IBS_CLOSING'
			// tlfDAO.deleteByReferenceType("IBS_CLOSING");

			// SELECT CC.CUR, C.ACTYPE, CC.ACODE, CC.M12 AS AMOUNT FROM CCOA CC
			// INNER JOIN
			// COA C ON CC.ACODE = C.ACODE AND CC.DCODE = C.DCODE
			// WHERE CC.BRANCHID = '" & cboBranch.Text & "' AND CC.M12 <> 0 AND
			// SUBSTRING(CC.ACODE, 4, 3) <> '000' AND (C.ACTYPE = 'I' OR
			// C.ACTYPE = 'E')
			// ORDER BY CC.CUR, CC.ACODE"
			ccoaList = ccoaDAO.findCCOAforBranchClosing(closingBranch.getId());

			List<CurrencyChartOfAccount> iCcoaList = ccoaDAO.findCCOAByCOAIDAndBranchId(iCOA.getId(), ibsBranch.getId());
			// create tlf for closing branch and HO branch and add to new list
			if (!ccoaList.isEmpty()) {
				tlfList = createTLFListForClosing(ccoaList, iCcoaList, ibsBranch);
				tLFService.addVoucher(tlfList);
			}

		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Fail to create IbsClosing");
		}

		// inter to db
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void createIbsConsolidation(Branch closingBranch, Branch ibsBranch, ChartOfAccount iCOA) throws SystemException {
		try {
			List<CurrencyChartOfAccount> ccoaList = new ArrayList<>();
			List<TLF> oldTLFList = new ArrayList<>();
			List<TLF> tlfList = new ArrayList<>();

			// chage ho branch
			List<CurrencyChartOfAccount> iCcoaList = ccoaDAO.findCCOAByCOAIDAndBranchId(iCOA.getId(), ibsBranch.getId());
			// Delete all tlf by reference type (HO_IBS_CLOSING)
			// tlfDAO.deleteByReferenceType("HO_IBS_CLOSING");

			// SELECT B.NAME AS BRANCH, T.CURRENCYID AS CUR, C.ACTYPE, T.COAID
			// AS ACODE, T.LOCALAMOUNT AS AMOUNT FROM TLF T INNER JOIN BRANCH B
			// ON T.BRANCHID = B.BRANCHCODE INNER JOIN COA C ON T.COAID =
			// C.ACODE WHERE T.BRANCHID = '" & cboBranch.Text & "' AND NARRATION
			// = 'T/R to HO for Final closing' AND C.ACTYPE = 'I' ORDER BY
			// T.CURRENCYID, T.COAID

			oldTLFList = tlfDAO.findTLFListForBranchConso(closingBranch.getId());
			tlfList = createTLFListForConso(oldTLFList, iCcoaList, ibsBranch);
			tLFService.addVoucher(tlfList);

		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Fail to create Branch Consolidation");
		}

	}

	public BigDecimal changeMonth(int month, CurrencyChartOfAccount ccoa) {
		BigDecimal amt = null;
		switch (month) {
			case 1:
				amt = ccoa.getMsrcRate().getMsrc1();
				break;
			case 2:
				amt = ccoa.getMsrcRate().getMsrc2();
				break;
			case 3:
				amt = ccoa.getMsrcRate().getMsrc3();
				break;
			case 4:
				amt = ccoa.getMsrcRate().getMsrc4();
				break;
			case 5:
				amt = ccoa.getMsrcRate().getMsrc5();
				break;
			case 6:
				amt = ccoa.getMsrcRate().getMsrc6();
				break;
			case 7:
				amt = ccoa.getMsrcRate().getMsrc7();
				break;
			case 8:
				amt = ccoa.getMsrcRate().getMsrc8();
				break;
			case 9:
				amt = ccoa.getMsrcRate().getMsrc9();
				break;
			case 10:
				amt = ccoa.getMsrcRate().getMsrc10();
				break;
			case 11:
				amt = ccoa.getMsrcRate().getMsrc11();
				break;
			case 12:
				amt = ccoa.getMsrcRate().getMsrc12();
				break;
			default:
				break;

		}
		return amt;
	}

}
