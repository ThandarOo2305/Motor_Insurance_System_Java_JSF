package org.ace.accounting.system.setup.persistence.interfaces;

import java.util.List;

import org.ace.accounting.system.setup.Setup;
import org.ace.java.component.persistence.exception.DAOException;

public interface ISetupDAO {
	public List<Setup> findAll() throws DAOException;

	public String findSetupValueByVariable(String variable) throws DAOException;

	public String findSetupBudgetByVariable(String variable) throws DAOException;

	public void updateSetupValueByVariable(String variable, String value) throws DAOException;

	public void updateSetupBudget(String budget) throws DAOException;

	public void insert(String variable) throws DAOException;
}
