package org.ace.accounting.system.gainloss.service.interfaces;

import java.util.List;

import org.ace.accounting.system.gainloss.ExchangeConfig;
import org.ace.java.component.SystemException;

public interface IExchangeConfigService {

	List<ExchangeConfig> findAllExchangeConfig() throws SystemException;

	void addNewExchangeConfig(ExchangeConfig exchangeConfig) throws SystemException;

	void updateBranch(ExchangeConfig exchangeConfig) throws SystemException;

	void deleteBranch(ExchangeConfig exchangeConfig) throws SystemException;

}