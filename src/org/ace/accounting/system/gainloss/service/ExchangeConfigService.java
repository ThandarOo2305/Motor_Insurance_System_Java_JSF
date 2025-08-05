package org.ace.accounting.system.gainloss.service;

import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.system.gainloss.ExchangeConfig;
import org.ace.accounting.system.gainloss.persistence.interfaces.IExchangeConfigDAO;
import org.ace.accounting.system.gainloss.service.interfaces.IExchangeConfigService;
import org.ace.java.component.ErrorCode;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "ExchangeConfigService")
public class ExchangeConfigService implements IExchangeConfigService {

	@Autowired
	private IExchangeConfigDAO exchangeConfigDAO;

	@Resource(name = "DataRepService")
	private IDataRepService<ExchangeConfig> exchangeDataRepService;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	@Override
	public List<ExchangeConfig> findAllExchangeConfig() throws SystemException {
		try {
			return exchangeConfigDAO.findAllExchangeConfig();
		} catch (DAOException de) {
			throw new SystemException(de.getErrorCode(), "Failed to Find All ExchangeConfig Data ", de);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void addNewExchangeConfig(ExchangeConfig exchangeConfig) throws SystemException {
		try {
			exchangeDataRepService.insert(exchangeConfig);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to add new ExchangeConfig", e);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateBranch(ExchangeConfig exchangeConfig) throws SystemException {
		try {
			exchangeDataRepService.update(exchangeConfig);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to update Exchange Config", e);
		} 
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void deleteBranch(ExchangeConfig exchangeConfig) throws SystemException {
		try {
			exchangeDataRepService.delete(exchangeConfig);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to Exchange Config", e);
		}
	}
}
