package org.ace.accounting.system.tlf.service.interfaces;

import java.util.Date;
import java.util.List;

import org.ace.accounting.common.VoucherType;
import org.ace.accounting.dto.EditVoucherDto;
import org.ace.accounting.dto.GainAndLossDTO;
import org.ace.accounting.dto.JVdto;
import org.ace.accounting.dto.RateDTO;
import org.ace.accounting.dto.VoucherDTO;
import org.ace.accounting.report.balancesheet.BalanceSheetDTO;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.coasetup.COASetup;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.rateinfo.RateType;
import org.ace.accounting.system.tlf.TLF;
import org.ace.accounting.system.trantype.TranCode;
import org.ace.accounting.system.trantype.TranType;
import org.ace.java.component.SystemException;

public interface ITLFService {
	public List<TLF> findAllTLF();

	public List<RateDTO> findRateTLF(RateDTO ratedto);

	public String getVoucherNo();

	public List<String> findVoucherNoByBranchIdAndVoucherType(String branchId, VoucherType voucherType);

	public List<TLF> findVoucherListByReverseZero(String voucherNo);

	public TranType findCashAccountByVoucherNo(String voucherNo);

	public double getExchangeRate(Currency reqCur, RateType reqRateType, Date rDate) throws SystemException;

	public COASetup findCOABy(String acName, Currency currency, Branch branch);

	public void addVoucher(List<TLF> dtoList) throws SystemException;

	public String addVoucher(VoucherDTO voucherDto, TranCode transCode) throws SystemException;

	public void updateVoucher(List<TLF> oldVoucherList, List<EditVoucherDto> voucherList, VoucherType voucherType);

	public void reverseVoucher(List<TLF> oldList, boolean reverse, VoucherType voucherType);

	public String addNewTlfByDto(List<JVdto> dtoList);

	String getVoucherNoForVocherTypes(TranCode tranCode, Date settlementDate);

	String getVoucherNoForVocherTypes(TranCode tranCode);

	List<GainAndLossDTO> findGainAndLosttList(GainAndLossDTO dto);

	List<BalanceSheetDTO> generateBalanceSheetByGroup() throws SystemException;

	List<BalanceSheetDTO> generateBalanceSheet(String branchId, String currencyId, boolean isHomeCurrency, String budgetYear) throws SystemException;

	List<BalanceSheetDTO> generateBalanceSheetByClone(String branchId, String currencyId, boolean isHomeCurrency, String budgetYear) throws SystemException;

	List<BalanceSheetDTO> generateBalanceSheetByDate(String branchId, String currencyId, Date fromDate, Date toDate, boolean isHomeCurrency) throws SystemException;

	List<BalanceSheetDTO> generateCloneBalanceSheetByDate(String branchId, String currencyId, Date fromDate, Date toDate, boolean isHomeCurrency) throws SystemException;

}
