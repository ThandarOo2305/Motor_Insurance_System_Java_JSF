package org.ace.accounting.report.listingReport.persistence.interfaces;

import java.util.Date;
import java.util.List;

import org.ace.accounting.dto.CashBookDTO;
import org.ace.accounting.dto.CoaListingDto;
import org.ace.accounting.dto.VLdto;
import org.ace.accounting.dto.VPFTdto;
import org.ace.accounting.dto.VPdto;
import org.ace.accounting.report.CashBookCriteria;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.web.listing.TransactionType;
import org.ace.accounting.web.listing.VoucherListingType;
import org.ace.java.component.persistence.exception.DAOException;

/**
 * This interface serves as the DAO to manipulate the <code>CashBookDTO</code>
 * object.
 * 
 * @author HS
 * @since 1.0.0
 * @date 2015/04/08
 */

public interface IListingReportDAO {
	public List<CoaListingDto> findAllCoaList() throws DAOException;

	/**
	 * 
	 * @param criteria
	 * @return {@link List} of {@link CashBookDTO} instance
	 * @throws DAOException
	 *             An exception occurs during the DB operation
	 */
	public List<CashBookDTO> findCashbookListingReport(CashBookCriteria criteria);

	public List<VPFTdto> findFromVoucherNo(Date startDate, Date endDate, Branch branch);

	public List<VPFTdto> findFromVoucherNoForGenJournal(VoucherListingType type, Date startDate, Date endDate, Branch branch, Currency currency, TransactionType tType);

	public List<VPdto> findVoucherPrintingByVoucherNo(Date startDate, Date endDate, String fromId, String toId,Branch branchid);

	public List<VPdto> findVoucherPrinting(Date startDate, Date endDate);

	List<VLdto> findVoucherListingReport(VoucherListingType type, Date startDate, Date endDate, Branch branch, Currency currency, Boolean isOnlyVoucher, boolean reverse);

	List<VLdto> findGenLournalListingReport(VoucherListingType type, Date startDate, Date endDate, Branch branch, Currency currency, Boolean homeCurrencyConverted,
			TransactionType tType);

	List<VLdto> findGenJournalListingReportByVoucherNo(VoucherListingType type, Date startDate, Date endDate, String fromEno, String toEno, Branch branch, Currency currency,
			Boolean homeCurrencyConverted, TransactionType tType);

}
