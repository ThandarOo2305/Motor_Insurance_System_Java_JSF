/***************************************************************************************
 * @author <<Your Name>>
 * @Date 2013-02-11
 * @Version 1.0
 * @Purpose <<You have to write the comment the main purpose of this class>>
 * 
 *    
 ***************************************************************************************/
package org.ace.accounting.system.currency.service.interfaces;

import java.util.List;

import org.ace.accounting.dto.MonthlyRateDto;
import org.ace.accounting.system.currency.Currency;

public interface ICurrencyService {
	public Currency findCurrencyByCurrencyCode(String currencyCode);

	public List<Currency> findAllCurrency();

	public List<Currency> findForeignCurrency();

	public List<MonthlyRateDto> findForeignCurrencyDto();

	public void updateAllMonthlyRate(List<MonthlyRateDto> currencyList);

	public Currency findHomeCurrency();

	public void addNewCurrency(Currency currency);

	public void updateCurrency(Currency currency);

	public void updateAllCurrency(List<Currency> curList);

	public void deleteCurrency(Currency currency);
}
