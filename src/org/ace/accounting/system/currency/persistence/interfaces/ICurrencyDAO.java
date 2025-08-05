/***************************************************************************************
 * @author <<Your Name>>
 * @Date 2013-02-11
 * @Version 1.0
 * @Purpose <<You have to write the comment the main purpose of this class>>
 * 
 *    
 ***************************************************************************************/
package org.ace.accounting.system.currency.persistence.interfaces;

import java.util.List;

import org.ace.accounting.dto.MonthlyRateDto;
import org.ace.accounting.system.currency.Currency;
import org.ace.java.component.persistence.exception.DAOException;

public interface ICurrencyDAO {

	public Currency findCurrencyByCurrencyCode(String currencyCode) throws DAOException;

	public List<Currency> findAll() throws DAOException;

	public List<Currency> findForeignCurrency() throws DAOException;

	public void updateMonthlyRate(MonthlyRateDto cur) throws DAOException;

	public List<MonthlyRateDto> findForeignCurrencyDto() throws DAOException;

	public Currency findHomeCurrency() throws DAOException;

	public void delete(Currency cur) throws DAOException;

}