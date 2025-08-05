package org.ace.accounting.system.setup.service;

import java.util.Date;

import org.ace.java.component.persistence.exception.DAOException;

public interface ISetupService {
	public String findSetupValueByVariable(String variable) throws DAOException;

	public void insert(String variable) throws DAOException;
	
	public void insert(String variable, Date backDate) throws DAOException;
}
