/**
 * 
 */
package org.ace.accounting.report.daybook.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.dto.DayBookDto;
import org.ace.accounting.dto.DayBookReportDto;
import org.ace.accounting.dto.DayBookReportDto1;
import org.ace.accounting.report.daybook.persistence.interfaces.IDayBookDAO;
import org.ace.accounting.report.daybook.service.interfaces.IDayBookService;
import org.ace.accounting.system.chartaccount.persistence.interfaces.ICoaDAO;
import org.ace.accounting.system.coasetup.COASetup;
import org.ace.accounting.system.coasetup.persistence.interfaces.ICOASetupDAO;
import org.ace.accounting.system.setup.persistence.interfaces.ISetupDAO;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Aung
 *
 */

@Service(value = "DayBookService")
public class DayBookService extends BaseService implements IDayBookService {

	@Resource(name = "SetupDAO")
	private ISetupDAO setupDAO;

	@Resource(name = "DayBookDAO")
	private IDayBookDAO dayBookDAO;

	@Resource(name = "COASetupDAO")
	private ICOASetupDAO coaSetupDAO;

	@Resource(name = "CoaDAO")
	private ICoaDAO coaDao;

	/**
	 * 
	 * @param dto
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public List<DayBookReportDto> findDayBookList(DayBookDto dto) {
		List<DayBookReportDto> result = null;
		COASetup cashAccount;
		try {
			StringBuffer sf = new StringBuffer();
			sf = new StringBuffer(
					"SELECT NEW org.ace.accounting.dto.DayBookReportDto(t.ccoa.coa, t.eNo, t.currency, t.narration, t.branch, t.settlementDate, t.tranType, t.localAmount, t.homeAmount)");
			sf.append(" FROM TLF t WHERE t.reverse=0 AND t.paid=1 AND t.ccoa.coa.acCodeType!=org.ace.accounting.system.chartaccount.AccountCodeType.HEAD AND");
			sf.append(" t.ccoa.coa.acCodeType!=org.ace.accounting.system.chartaccount.AccountCodeType.GROUP ");

			cashAccount = coaSetupDAO.findCOAByACNameAndCur("CASH", dto.getCurrency(), null);
			if (dto.isAllBranch()) {
				if (dto.isHomeCurrency()) {
					sf.append("AND t.ccoa.coa != :cashAccount AND cast(t.settlementDate as date) = :requiredDate");
					dto.setCurrency(null);
					dto.setBranch(null);
				} else {
					sf.append("AND t.ccoa.coa != :cashAccount AND cast(t.settlementDate as date) = :requiredDate AND t.currency = :currency");
					dto.setBranch(null);
				}
			} else {// If not All Branch
				if (dto.isHomeCurrency()) {
					sf.append("AND t.ccoa.coa != :cashAccount AND cast(t.settlementDate as date) = :requiredDate AND t.branch = :branch");
					dto.setCurrency(null);
				} else {
					sf.append("AND t.ccoa.coa != :cashAccount AND cast(t.settlementDate as date) = :requiredDate AND t.currency = :currency AND t.branch = :branch");
				}
			}
			sf.append(" ORDER BY t.ccoa.coa, t.eNo");

			Date budgetStartDate = DateUtils.formatStringToDate(setupDAO.findSetupValueByVariable(BusinessUtil.BUDGETSDATE));
			// --- get data from tlf history if required Date is before budget
			// start date
			if (dto.getRequiredDate().before(budgetStartDate)) {
				sf = new StringBuffer(sf.toString().replace("FROM TLF", "FROM TLFHIST"));
			}
			result = dayBookDAO.findDayBookList(sf, dto, cashAccount.getCcoa().getCoa());

			for (DayBookReportDto dto1 : result) {

				dto1.setHomeCurrency(dto.isHomeCurrency());
				dto1.setHomeCurrencyConverted(dto.isHomeCurrencyConverted());
			}

		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find COA By Branch ID.", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<DayBookReportDto1> findDayBookListWithGrandTotal(DayBookDto dto) {
		List<DayBookReportDto> detailList = findDayBookList(dto);
		List<DayBookReportDto1> groupAccWithDetailAcc = new ArrayList<DayBookReportDto1>();
		DayBookReportDto1 groupAccWithDetailAccItem = new DayBookReportDto1(null, detailList);
		groupAccWithDetailAcc.add(groupAccWithDetailAccItem);
		DayBookReportDto grandTotal = getGrandTotal(detailList);
		groupAccWithDetailAcc.get(0).setGrandTotal(grandTotal);

		return groupAccWithDetailAcc;
	}

	public DayBookReportDto getGrandTotal(List<DayBookReportDto> detailList) {

		double crDebit = 0;
		double trDebit = 0;
		double crCredit = 0;
		double trCredit = 0;

		double crDebit_Home = 0;
		double trDebit_Home = 0;
		double crCredit_Home = 0;
		double trCredit_Home = 0;

		for (DayBookReportDto dto : detailList) {
			
			crDebit += dto.getCrDebit() == null ? 0 : dto.getCrDebit().doubleValue();
			trDebit += dto.getTrDebit() == null ? 0 : dto.getTrDebit().doubleValue();
			crCredit += dto.getCrCredit() == null ? 0 : dto.getCrCredit().doubleValue();
			trCredit += dto.getTrCredit() == null ? 0 : dto.getTrCredit().doubleValue();

			crDebit_Home += dto.getCrDebit_Home() == null ? 0 : dto.getCrDebit_Home().doubleValue();
			trDebit_Home += dto.getTrDebit_Home() == null ? 0 : dto.getTrDebit_Home().doubleValue();
			crCredit_Home += dto.getCrCredit_Home() == null ? 0 : dto.getCrCredit_Home().doubleValue();
			trCredit_Home += dto.getTrCredit_Home() == null ? 0 : dto.getTrCredit_Home().doubleValue();
		}
		DayBookReportDto grandTotal = new DayBookReportDto(new BigDecimal(crDebit), new BigDecimal(trDebit), new BigDecimal(crCredit), new BigDecimal(trCredit),
				new BigDecimal(crDebit_Home), new BigDecimal(trDebit_Home), new BigDecimal(crCredit_Home), new BigDecimal(trCredit_Home));
		if(detailList.size()>0) {
			grandTotal.setHomeCurrencyConverted(detailList.get(0).isHomeCurrencyConverted());			
		}

		return grandTotal;
	}

}
