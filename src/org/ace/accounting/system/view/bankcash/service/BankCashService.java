package org.ace.accounting.system.view.bankcash.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.dto.BankCashDto;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.view.bankcash.persistence.interfaces.IBankCashDAO;
import org.ace.accounting.system.view.bankcash.service.interfaces.IBankCashService;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**************************************************************************
 * $21-04-2016$ $Thaw Oo Khaing$ $1$ ACEPLUS SOLUTIONS CO., Ltd.
 *************************************************************************/

@Service(value = "BankCashService")
public class BankCashService extends BaseService implements IBankCashService {

	@Resource(name = "BankCashDAO")
	private IBankCashDAO bankCashDAO;

	/**
	 * [ To find the sum of opening balance on the report date , get closing
	 * balance of the previous month first and find the cash TLF between the
	 * start of the month and one day before report date. ]
	 * 
	 * @param CurrencyType
	 *            type [To decide home amount or source amount]
	 * @param Currency
	 *            currency [To filter the result by currency]
	 * @param Date
	 *            reportDate [Report Date]
	 * @param boolean
	 *            curConverted [To decide the request amount is converted amount
	 *            for local currency]
	 * @param Branch
	 *            branch [To filter the result by branch]
	 * 
	 * 
	 * @return [Return A BigDecimal amount]
	 * 
	 * @throws [Exception
	 *             SystemException]
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public BigDecimal findOpeningBalance(CurrencyType type, Currency currency, Date date, boolean curConverted, Branch branch) {
		BigDecimal result;
		try {
			result = bankCashDAO.findOpeningBalance(type, currency, date, curConverted, branch);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find opening balance for bank cash.", e);
		}
		return result;
	}

	/**
	 * [ To find the sum of total transfer TLF on the report date. ]
	 * 
	 * @param CurrencyType
	 *            type [To decide home amount or source amount]
	 * @param Currency
	 *            currency [To filter the result by currency]
	 * @param Date
	 *            reportDate [Report Date]
	 * @param boolean
	 *            curConverted [To decide the request amount is converted amount
	 *            for local currency]
	 * @param Branch
	 *            branch [To filter the result by branch]
	 * 
	 * 
	 * @return [Return A BigDecimal amount]
	 * 
	 * @throws [Exception
	 *             SystemException]
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public BigDecimal findTotalTransfer(CurrencyType type, Currency currency, Date date, boolean curConverted, Branch branch) {
		BigDecimal result;
		try {
			result = bankCashDAO.findTotalTransfer(type, currency, date, curConverted, branch);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find total transfer for bank cash.", e);
		}
		return result;
	}

	/**
	 * [ Currently not used , for later use if needed. ]
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public BigDecimal findTotalReceipt(CurrencyType type, Currency currency, Date date, boolean curConverted, Branch branch) {
		BigDecimal result;
		try {
			result = bankCashDAO.findTotalReceipt(type, currency, date, curConverted, branch);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find total receipt for bank cash.", e);
		}
		return result;
	}

	/**
	 * [ Currently not used , for later use if needed. ]
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public BigDecimal findTotalPayment(CurrencyType type, Currency currency, Date date, boolean curConverted, Branch branch) {
		BigDecimal result;
		try {
			result = bankCashDAO.findTotalPayment(type, currency, date, curConverted, branch);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find total payment for bank cash.", e);
		}
		return result;
	}

	/**
	 * [ To find the daily tlf by the report date. ]
	 * 
	 * @param CurrencyType
	 *            type [To decide home amount or source amount]
	 * @param Currency
	 *            currency [To filter the result by currency]
	 * @param Date
	 *            date [Report Date]
	 * @param boolean
	 *            curConverted [To decide the request amount is converted amount
	 *            for local currency]
	 * @param Branch
	 *            branch [To filter the result by branch]
	 * 
	 * 
	 * @return [Return A List of BankCashDto]
	 * 
	 * @throws [Exception
	 *             SystemException]
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public List<BankCashDto> findDailyTransaction(CurrencyType type, Currency currency, Date date, boolean curConverted, Branch branch) {
		List<BankCashDto> result;
		try {
			result = bankCashDAO.findDailyTransaction(type, currency, date, curConverted, branch);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find daily transactions for bank cash.", e);
		}
		return result;
	}

}
