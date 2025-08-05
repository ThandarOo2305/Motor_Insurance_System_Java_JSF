package org.ace.accounting.system.rateinfo.persistence.interfaces;

import java.util.Date;
import java.util.List;

import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.rateinfo.RateInfo;
import org.ace.accounting.system.rateinfo.RateType;
import org.ace.java.component.persistence.exception.DAOException;

public interface IRateInfoDAO {

	public List<RateInfo> findAll() throws DAOException;

	public double findExchangeRateBy(Currency reqCur, RateType reqRateType, Date rDate) throws DAOException;

	public void updateLastModifyBy(Currency cur, RateType rateType) throws DAOException;

	public List<RateInfo> findRateInfoByCurrencyID(String currencyID) throws DAOException;

}
