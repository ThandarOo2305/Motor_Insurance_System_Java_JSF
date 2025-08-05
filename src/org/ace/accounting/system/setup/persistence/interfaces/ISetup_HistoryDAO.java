package org.ace.accounting.system.setup.persistence.interfaces;

import java.util.Date;
import java.util.List;

import org.ace.accounting.system.setup.SetupHistory;
import org.ace.java.component.persistence.exception.DAOException;

public interface ISetup_HistoryDAO {
	public List<SetupHistory> findAll() throws DAOException;

	public String findSetupHistoryValueByVariable(String variable, String budget) throws DAOException;

	public void updateSetupHistoryValueByVariable(String variable, String value) throws DAOException;

	public String findbudgetyear(Date reportDate) throws DAOException;
}
