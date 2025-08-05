package org.ace.accounting.system.tlf.persistence.interfaces;

import java.util.Date;
import java.util.List;

import org.ace.accounting.common.VoucherType;
import org.ace.accounting.dto.GainAndLossDTO;
import org.ace.accounting.dto.RateDTO;
import org.ace.accounting.report.balancesheet.BalanceSheetDTO;
import org.ace.accounting.system.tlf.TLF;
import org.ace.accounting.system.trantype.TranType;
import org.ace.java.component.persistence.exception.DAOException;

public interface ITLFDAO {
	public List<TLF> findAll() throws DAOException;

	public List<RateDTO> findRateList(RateDTO ratedto) throws DAOException;

	public List<String> findVoucherNoByBranchIdAndVoucherType(String branchId, VoucherType voucherType) throws DAOException;

	public List<TLF> findVoucherListByReverseZero(String voucherNo) throws DAOException;

	public TranType findCashAccountByVoucherNo(String voucherNo) throws DAOException;

	public void updateReverseByID(String id, boolean reverse) throws DAOException;

	List<GainAndLossDTO> findGainAndLossList(GainAndLossDTO ratedto) throws DAOException;

	List<BalanceSheetDTO> generateBalanceSheetByGroup() throws DAOException;

	List<BalanceSheetDTO> generateBalanceSheet(String branchId, String currencyId, boolean isHomeCurrency, String budgetYear) throws DAOException;

	List<BalanceSheetDTO> generateBalanceSheetByClone(String branchId, String currencyId, boolean isHomeCurrency, String budgetYear) throws DAOException;

	List<BalanceSheetDTO> generateBalanceSheetByDate(String branchId, String currencyId, Date fromDate, Date toDate, boolean isHomeCurrency) throws DAOException;

	List<BalanceSheetDTO> generateCloneBalanceSheetByDate(String branchId, String currencyId, Date fromDate, Date toDate, boolean isHomeCurrency) throws DAOException;

	List<TLF> findTLFListForBranchConso(String branchId) throws DAOException;

}
