package org.ace.accounting.system.view.bankcash.persistence.interfaces;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.dto.BankCashDto;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;
import org.ace.java.component.persistence.exception.DAOException;

/**************************************************************************
 * $21-04-2016$ $Thaw Oo Khaing$ $1$ ACEPLUS SOLUTIONS CO., Ltd.
 *************************************************************************/

public interface IBankCashDAO {

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
	 *             DAOException]
	 */
	public BigDecimal findOpeningBalance(CurrencyType type, Currency currency, Date date, boolean curConverted, Branch branch) throws DAOException;

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
	 *             DAOException]
	 */
	public BigDecimal findTotalTransfer(CurrencyType type, Currency currency, Date date, boolean curConverted, Branch branch) throws DAOException;

	/**
	 * [ Currently not used , for later use if needed. ]
	 */
	public BigDecimal findTotalReceipt(CurrencyType type, Currency currency, Date date, boolean curConverted, Branch branch) throws DAOException;

	/**
	 * [ Currently not used , for later use if needed. ]
	 */
	public BigDecimal findTotalPayment(CurrencyType type, Currency currency, Date date, boolean curConverted, Branch branch) throws DAOException;

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
	 *             DAOException]
	 */
	public List<BankCashDto> findDailyTransaction(CurrencyType type, Currency currency, Date date, boolean curConverted, Branch branch) throws DAOException;

}
