package org.ace.accounting.posting.persistence;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.dto.YPDto;
import org.ace.accounting.posting.persistence.interfaces.IYearlyPostingDAO;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.AccountType;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.chartaccount.persistence.interfaces.ICcoaDAO;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.rateinfo.RateInfo;
import org.ace.accounting.system.rateinfo.persistence.interfaces.IRateInfoDAO;
import org.ace.accounting.system.setup.persistence.interfaces.ISetupDAO;
import org.ace.accounting.system.tlf.TLF;
import org.ace.accounting.system.tlf.service.interfaces.ITLFService;
import org.ace.accounting.system.trantype.TranCode;
import org.ace.accounting.system.trantype.TranType;
import org.ace.accounting.system.trantype.persistence.interfaces.ITranTypeDAO;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("YearlyPostingDAO")
public class YearlyPostingDAO extends BasicDAO implements IYearlyPostingDAO {

	@Resource(name = "SetupDAO")
	private ISetupDAO setupDAO;

	@Resource(name = "TLFService")
	private ITLFService tLFService;

	@Resource(name = "CcoaDAO")
	private ICcoaDAO ccoaDAO;

	@Resource(name = "RateInfoDAO")
	private IRateInfoDAO rateInfoDAO;

	@Resource(name = "TranTypeDAO")
	private ITranTypeDAO tranTypeDAO;

	@Transactional(propagation = Propagation.REQUIRED)
	public void moveTlfToHistory(Date postingDate) throws DAOException {
		try {
			postingDate = DateUtils.resetEndDate(postingDate);
			insertTLFHISTByPostingDate(postingDate);
			deleteTLFByPostingDate(postingDate);
		} catch (PersistenceException pe) {
			throw translate("Failed to move Tlf To History By PostingDate", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void insertTLFHISTByPostingDate(Date postingDate) throws DAOException {
		StringBuffer sf = new StringBuffer("INSERT INTO TLF_HIST(ID, CUSTOMERID, ccoaid, HOMEAMOUNT, LOCALAMOUNT, CURRENCYID, ");
		sf.append(" CHEQUENO, BANKID, PURCHASEORDERID, LOANID, NARRATION, TRANTYPEID, DEPARTMENTID, ORGNTLFID, BRANCHID, RATE,");
		sf.append(" SETTLEMENTDATE, REVERSE, REFERENCENO, REFERENCETYPE, ENO, PAID, TLFNO, CLEARING, ISRENEWAL, PAYABLETRAN, ");
		sf.append(" VERSION,CREATEDDATE,CREATEDUSERID,UPDATEDDATE,UPDATEDUSERID)  (SELECT ID, CUSTOMERID, CCOAID, HOMEAMOUNT, LOCALAMOUNT,");
		sf.append(" CURRENCYID, CHEQUENO, BANKID, PURCHASEORDERID, LOANID, NARRATION, TRANTYPEID, DEPARTMENTID, ORGNTLFID, BRANCHID, RATE,");
		sf.append(" SETTLEMENTDATE, REVERSE, REFERENCENO, REFERENCETYPE, ENO, PAID, TLFNO, CLEARING, ISRENEWAL, PAYABLETRAN, ");
		sf.append(" VERSION,CREATEDDATE,CREATEDUSERID,UPDATEDDATE,UPDATEDUSERID FROM TLF t WHERE t.SETTLEMENTDATE <= ?1 )");
		try {
			Query q = em.createNativeQuery(sf.toString());
			q.setParameter(1, postingDate);
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to insertTLFHIST By PostingDate", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteTLFByPostingDate(Date postingDate) throws DAOException {
		StringBuffer sf = new StringBuffer("DELETE FROM TLF T WHERE t.settlementDate <= :postingDate");
		try {
			Query q = em.createQuery(sf.toString());
			q.setParameter("postingDate", postingDate);
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to deleteTLF By PostingDate", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void moveCcoaToHistory(String budgetYear, YPDto plAc, Branch branch) throws DAOException {
		try {
			insertCcoaHistory();
			updateCcoaBal();
			// updateCcoaBalForYearlyPosting(plAc, branch);
			// updateCcoaBalByTacAndPlAcAndACTYPE(plAc);
			updateCcoaAllZeroAndBudgetYear(budgetYear);
		} catch (PersistenceException pe) {
			throw translate("Failed to moveCcoaToHistory by Yearly Posting", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void insertCcoaHistory() throws DAOException {
		StringBuffer sf = new StringBuffer("INSERT INTO PREV_CCOA(ID, ACNAME, ACCRUED, BF, BUDGET, CBAL, HOBAL, OBAL, SCCRBAL, VERSION, CREATEDDATE,");
		sf.append(" CREATEDUSERID,  UPDATEDDATE, UPDATEDUSERID, BF1, BF10, BF11, BF12, BF13, BF2, BF3 ,BF4 ,BF5 ,BF6 ,BF7 ,BF8 ,BF9 , ");
		sf.append(" BFSRC1 ,BFSRC10 , BFSRC11 , BFSRC12 , BFSRC13 , BFSRC2 , BFSRC3 , BFSRC4 , BFSRC5 , BFSRC6 , BFSRC7 ,  BFSRC8 , BFSRC9 , ");
		sf.append("LYMSRC1 , LYMSRC10 , LYMSRC11 , LYMSRC12 , LYMSRC13 , LYMSRC2 , LYMSRC3 , LYMSRC4 ,  LYMSRC5 , LYMSRC6 , LYMSRC7 , LYMSRC8 ,");
		sf.append(" LYMSRC9 , M1 , M10 , M11 , M12 , M13 , M2 , M3 , M4 , M5 , M6 , M7 ,  M8 , M9 , MREV1 , MREV10 , MREV11 , MREV12 , MREV13 , MREV2");
		sf.append(" , MREV3 , MREV4 , MREV5 , MREV6 , MREV7 , MREV8 , MREV9 ,  MSRC1 , MSRC10 , MSRC11 , MSRC12 , MSRC13 , MSRC2 , MSRC3 , MSRC4 , ");
		sf.append("MSRC5 , MSRC6 , MSRC7 , MSRC8 , MSRC9 , COAID , BRANCHID ,  CURRENCYID , DEPARTMENTID)   (SELECT  ID, ACNAME, ACCRUED, BF, BUDGET, ");
		sf.append("CBAL, HOBAL, OBAL, SCCRBAL, VERSION, CREATEDDATE, CREATEDUSERID,  UPDATEDDATE, UPDATEDUSERID, BF1, BF10, BF11, BF12, BF13, BF2, BF3 ,");
		sf.append("BF4 ,BF5 ,BF6 ,BF7 ,BF8 ,BF9 ,  BFSRC1 ,BFSRC10 , BFSRC11 , BFSRC12 , BFSRC13 , BFSRC2 , BFSRC3 , BFSRC4 , BFSRC5 , BFSRC6 , BFSRC7 ,");
		sf.append("  BFSRC8 , BFSRC9 , LYMSRC1 , LYMSRC10 , LYMSRC11 , LYMSRC12 , LYMSRC13 , LYMSRC2 , LYMSRC3 , LYMSRC4 ,  LYMSRC5 , LYMSRC6 , LYMSRC7 , ");
		sf.append("LYMSRC8 , LYMSRC9 , M1 , M10 , M11 , M12 , 0 , M2 , M3 , M4 , M5 , M6 , M7 ,  M8 , M9 , MREV1 , MREV10 , MREV11 , MREV12 , MREV13 , MREV2 , ");
		sf.append("MREV3 , MREV4 , MREV5 , MREV6 , MREV7 , MREV8 , MREV9 ,  MSRC1 , MSRC10 , MSRC11 , MSRC12 , 0 , MSRC2 , MSRC3 , MSRC4 , MSRC5 , MSRC6 , MSRC7 , ");
		sf.append("MSRC8 , MSRC9 , COAID , BRANCHID ,  CURRENCYID , DEPARTMENTID FROM CCOA  )");
		try {
			Query q = em.createNativeQuery(sf.toString());
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to insertCcoaHistory by Yearly Posting", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateCcoaBal() throws DAOException {
		Date budgetStartDate = BusinessUtil.getBudgetStartDate();
		Date budgetEndDate = BusinessUtil.getBudgetEndDate();
		int month = (DateUtils.monthsBetween(budgetStartDate, budgetEndDate, false, false)) + 1;
		StringBuffer sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c.monthlyRate.m1= c.monthlyRate.m" + month + ", c.msrcRate.msrc1=c.msrcRate.msrc" + month
				+ ", c.cBal= c.monthlyRate.m" + month + ", c.oBal = c.monthlyRate.m" + month + ", c.hOBal = c.msrcRate.msrc" + month);
		try {
			Query q = em.createQuery(sf.toString());
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to updateCcoaBal by Yearly Posting", pe);
		}
	}

	public BigDecimal findByPLC(YPDto plAc, Branch branch) {
		Date budgetStartDate = BusinessUtil.getBudgetStartDate();
		Date budgetEndDate = BusinessUtil.getBudgetEndDate();
		int month = (DateUtils.monthsBetween(budgetStartDate, budgetEndDate, false, false)) + 1;
		BigDecimal result = null;
		StringBuffer bf = new StringBuffer(
				"SELECT c.msrcRate.msrc" + month + " FROM CurrencyChartOfAccount c WHERE c.branch.id =:branchId AND c.currency.id =:currencyId AND c.coa.id IN :plcId");
		try {
			Query q = em.createQuery(bf.toString());
			q.setParameter("plcId", Arrays.asList(plAc.getId()));
			q.setParameter("branchId", branch.getId());
			q.setParameter("currencyId", "ISSYS0210001000000000129032013");
			result = (BigDecimal) q.getSingleResult();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to updateCcoaBal by Yearly Posting", pe);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateCcoaBalForYearlyPosting(YPDto plAc, Branch branch) throws DAOException {
		Date budgetStartDate = BusinessUtil.getBudgetStartDate();
		Date budgetEndDate = BusinessUtil.getBudgetEndDate();
		int month = (DateUtils.monthsBetween(budgetStartDate, budgetEndDate, false, false)) + 1;
		BigDecimal result = findByPLC(plAc, branch);
		double result2;

		/*
		 * StringBuffer sf = new StringBuffer("SELECT c.msrcRate.msrc" + month +
		 * " FROM CurrencyChartOfAccount c"); sf.
		 * append(" WHERE c.coa.id IN :headId AND c.branchId :branchId AND c.currencyId :currencyId "
		 * );
		 */

		/*
		 * StringBuffer bf = new StringBuffer("SELECT c.msrcRate.msrc" + month +
		 * " - " + result +
		 * " FROM CurrencyChartOfAccount c WHERE c.coa.id IN :headId AND c.branch.id =:branchId AND c.currency.id =:currencyId "
		 * );
		 */

		StringBuffer sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c.monthlyRate.m1= c.monthlyRate.m1" + " - " + result + ", c.msrcRate.msrc1= c.msrcRate.msrc1"
				+ " - " + result + ", c.cBal= c.cBal" + " - " + result + ", c.oBal = c.oBal" + " - " + result + ", c.hOBal = c.hOBal" + " - " + result
				+ " WHERE c.coa.id IN :headId AND c.branch.id =:branchId AND c.currency.id =:currencyId ");

		try {
			Query q = em.createQuery(sf.toString());
			q.setParameter("headId", Arrays.asList(plAc.getHeadId()));
			q.setParameter("branchId", branch.getId());
			q.setParameter("currencyId", "ISSYS0210001000000000129032013");
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to updateCcoaBal by Yearly Posting", pe);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateCcoaBalByTacAndPlAcAndACTYPE(YPDto plAc) throws DAOException {
		StringBuffer sf = new StringBuffer("UPDATE CurrencyChartOfAccount c SET c.monthlyRate.m1= 0, c.msrcRate.msrc1=0 ,c.cBal=0, c.oBal=0,");
		sf.append(" c.hOBal=0 WHERE (c.coa.id IN :detail OR ");
		sf.append("c.coa.id IN :groupIDList OR c.coa.acType in :acTypeList)");
		try {
			Query q = em.createQuery(sf.toString());
			q.setParameter("detail", Arrays.asList(plAc.getId()));
			q.setParameter("groupIDList", Arrays.asList(plAc.getGroupId()));
			q.setParameter("acTypeList", Arrays.asList(AccountType.E, AccountType.I));
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to updateCcoaBalByTacAndPlAcAndACTYPE by Yearly Posting", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateCcoaAllZeroAndBudgetYear(String budgetYear) throws DAOException {
		StringBuffer sf = new StringBuffer("UPDATE CurrencyChartOfAccount c set c.bF=0, c.monthlyRate.m2=0, c.monthlyRate.m3=0, c.monthlyRate.m4=0,");
		sf.append(" c.monthlyRate.m5=0, c.monthlyRate.m6=0, c.monthlyRate.m7=0, c.monthlyRate.m8=0,   c.monthlyRate.m9=0, c.monthlyRate.m10=0, ");
		sf.append("c.monthlyRate.m11=0, c.monthlyRate.m12=0, c.monthlyRate.m13=0,  c.msrcRate.msrc2=0 , c.msrcRate.msrc3=0, c.msrcRate.msrc4=0, ");
		sf.append("c.msrcRate.msrc5=0, c.msrcRate.msrc6=0, c.msrcRate.msrc7=0, c.msrcRate.msrc8=0,  c.msrcRate.msrc9=0, c.msrcRate.msrc10=0,");
		sf.append(" c.msrcRate.msrc11=0, c.msrcRate.msrc12=0, c.msrcRate.msrc13=0,  c.bfRate.bf1=0,c.bfRate.bf2=0,c.bfRate.bf3=0,c.bfRate.bf4=0,");
		sf.append("c.bfRate.bf5=0,c.bfRate.bf6=0,c.bfRate.bf7=0,c.bfRate.bf8=0,c.bfRate.bf9=0,  c.bfRate.bf10=0,c.bfRate.bf11=0,");
		sf.append("c.bfRate.bf12=0,c.bfRate.bf13=0, c.bfsrcRate.bfsrc1=0, c.bfsrcRate.bfsrc2=0, c.bfsrcRate.bfsrc3=0, c.bfsrcRate.bfsrc4=0,");
		sf.append(" c.bfsrcRate.bfsrc5=0, c.bfsrcRate.bfsrc6=0, c.bfsrcRate.bfsrc7=0,  c.bfsrcRate.bfsrc8=0, c.bfsrcRate.bfsrc9=0, ");
		sf.append("c.bfsrcRate.bfsrc10=0, c.bfsrcRate.bfsrc11=0, c.bfsrcRate.bfsrc12=0, c.bfsrcRate.bfsrc13=0,  c.mrevRate.mrev1=0, ");
		sf.append("c.mrevRate.mrev2=0, c.mrevRate.mrev3=0, c.mrevRate.mrev4=0, c.mrevRate.mrev5=0, c.mrevRate.mrev6=0, c.mrevRate.mrev7=0, ");
		sf.append("c.mrevRate.mrev8=0,   c.mrevRate.mrev9=0, c.mrevRate.mrev10=0, c.mrevRate.mrev11=0, c.mrevRate.mrev12=0, c.mrevRate.mrev13=0, ");
		sf.append("  c.lymsrcRate.lymsrc1=0, c.lymsrcRate.lymsrc2=0, c.lymsrcRate.lymsrc3=0, c.lymsrcRate.lymsrc4=0, c.lymsrcRate.lymsrc5=0, ");
		sf.append("c.lymsrcRate.lymsrc6=0,   c.lymsrcRate.lymsrc7=0, c.lymsrcRate.lymsrc8=0, c.lymsrcRate.lymsrc9=0, c.lymsrcRate.lymsrc10=0,");
		sf.append(" c.lymsrcRate.lymsrc11=0, c.lymsrcRate.lymsrc12=0,    c.lymsrcRate.lymsrc13=0, c.sccrBal=0, c.accrued=0,c.budget=:budgetYear");
		try {
			Query q = em.createQuery(sf.toString());
			q.setParameter("budgetYear", budgetYear);
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to updateCcoaAllZeroAndBudgetYear by Yearly Posting", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void increaseBudgetYear() throws DAOException {
		try {
			Date budgetStartDate = DateUtils.formatStringToDate(setupDAO.findSetupValueByVariable(BusinessUtil.BUDGETSDATE));
			Date budgetEndDate = DateUtils.formatStringToDate(setupDAO.findSetupValueByVariable(BusinessUtil.BUDGETEDATE));
			budgetStartDate = DateUtils.plusYears(budgetStartDate, 1);
			budgetEndDate = DateUtils.plusYears(budgetEndDate, 1);

			setupDAO.updateSetupValueByVariable(BusinessUtil.BUDGETSDATE, DateUtils.formatDateToString(budgetStartDate));
			setupDAO.updateSetupValueByVariable(BusinessUtil.BUDGETEDATE, DateUtils.formatDateToString(budgetEndDate));
		} catch (PersistenceException pe) {
			throw translate("Failed to increaseBudgetYear by Yearly Posting", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void increaseSetupBudgetYear() throws DAOException {
		try {
			Date budgetStartDate = DateUtils.formatStringToDate(setupDAO.findSetupValueByVariable(BusinessUtil.BUDGETSDATE));
			Date budgetEndDate = DateUtils.formatStringToDate(setupDAO.findSetupValueByVariable(BusinessUtil.BUDGETEDATE));
			String budget = BusinessUtil.getBudgetInfo(DateUtils.plusYears(budgetStartDate, 1), 2);
			budgetStartDate = DateUtils.plusDays(budgetEndDate, 1);
			budgetEndDate = DateUtils.plusYears(budgetEndDate, 1);
			String budsmth = String.valueOf(DateUtils.getMonthFromDate(DateUtils.plusMonths(budgetStartDate, 1)));

			setupDAO.updateSetupValueByVariable(BusinessUtil.BUDGETSDATE, DateUtils.formatDateToString(budgetStartDate));
			setupDAO.updateSetupValueByVariable(BusinessUtil.BUDGETEDATE, DateUtils.formatDateToString(budgetEndDate));
			setupDAO.updateSetupValueByVariable(BusinessUtil.BACKDATE, DateUtils.formatDateToString(budgetStartDate));
			setupDAO.updateSetupValueByVariable(BusinessUtil.BUDSMTH, budsmth);
			setupDAO.updateSetupBudget(budget);

		} catch (PersistenceException pe) {
			throw translate("Failed to increaseSetupBudgetYear by Yearly Posting", pe);
		}
	}

	/* move data from setup table to prev_setup */
	@Transactional(propagation = Propagation.REQUIRED)
	public void moveSetupToHistory() throws DAOException {
		try {
			insertSetUpHIST();
		} catch (PersistenceException pe) {
			throw translate("Failed to move Setup To History", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void insertSetUpHIST() throws DAOException {
		StringBuffer sf = new StringBuffer("INSERT INTO PREV_SETUP(VARIABLE, VALUE, BUDGET, VERSION)  (SELECT  VARIABLE, VALUE, BUDGET, VERSION ");
		sf.append(" FROM SETUP )");
		try {
			Query q = em.createNativeQuery(sf.toString());
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to insertSetupHistory by Yearly Posting", pe);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void createHOClosingForManulProcess(String branchID, Branch ibsBranch, YPDto plAc) throws SystemException {
		try {

			List<CurrencyChartOfAccount> ccoaList = new ArrayList<>();
			List<TLF> tlfList = new ArrayList<>();

			ccoaList = ccoaDAO.findCCOAforHOClosing(branchID);

			List<CurrencyChartOfAccount> iCcoaList = ccoaDAO.findCCOAByCOAIDAndBranchIdForHOClosing(plAc.getId(), ibsBranch.getId());
			// create tlf for closing branch and HO branch and add to new list
			if (!ccoaList.isEmpty()) {
				tlfList = createTLFListForHOClosing(ccoaList, iCcoaList, ibsBranch);
				tLFService.addVoucher(tlfList);
			}

		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Fail to create IbsClosing");
		}

		// inter to db
	}

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

	private List<TLF> createTLFListForHOClosing(List<CurrencyChartOfAccount> ccoaList, List<CurrencyChartOfAccount> interbranchCCOAList, Branch closingBranch) {
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
		narration = "HO for Final closing";
		branch = closingBranch;
		reverse = false;
		paid = true;
		referenceType = "HO_CLOSING";
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
