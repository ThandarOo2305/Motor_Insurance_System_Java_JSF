package org.ace.accounting.system.gainloss.persistence.interfaces;

import java.util.List;

import org.ace.accounting.system.gainloss.ExchangeConfig;
import org.ace.java.component.persistence.exception.DAOException;

public interface IExchangeConfigDAO {

	List<ExchangeConfig> findAllExchangeConfig() throws DAOException;

}