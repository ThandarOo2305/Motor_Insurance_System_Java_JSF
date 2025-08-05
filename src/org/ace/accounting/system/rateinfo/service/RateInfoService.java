package org.ace.accounting.system.rateinfo.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.rateinfo.RateInfo;
import org.ace.accounting.system.rateinfo.RateType;
import org.ace.accounting.system.rateinfo.persistence.interfaces.IRateInfoDAO;
import org.ace.accounting.system.rateinfo.service.interfaces.IRateInfoService;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "RateInfoService")
public class RateInfoService extends BaseService implements IRateInfoService {

	@Resource(name = "RateInfoDAO")
	private IRateInfoDAO rateInfoDAO;

	@Resource(name = "DataRepService")
	private IDataRepService<RateInfo> dataRepService;

	@Transactional(propagation = Propagation.REQUIRED)
	public List<RateInfo> findAllRateInfo() {
		List<RateInfo> result = null;
		try {
			result = rateInfoDAO.findAll();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of RateInfo)", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void addNewDailyRateInfo(RateInfo rateInfo) {
		try {
			rateInfoDAO.updateLastModifyBy(rateInfo.getCurrency(), rateInfo.getRateType());
			dataRepService.insert(rateInfo);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to Insert New Daily Currency Rate Info)", e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public double findCurrentRateInfo(Currency cur, Date voucherDate) {
		double exchangeRate;
		try {
			exchangeRate = rateInfoDAO.findExchangeRateBy(cur, RateType.TT, voucherDate);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find Currency Rate Info)", e);
		}
		return exchangeRate;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<RateInfo> findRateInfoByCurrencyID(String currencyID) {
		List<RateInfo> list = null;
		try {
			list = rateInfoDAO.findRateInfoByCurrencyID(currencyID);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of RateInfo", e);
		}

		return list;
	}
}