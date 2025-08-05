package org.ace.accounting.system.webPage.persistence.interfaces;

import java.util.List;

import org.ace.accounting.system.webPage.WebPage;
import org.ace.java.component.persistence.exception.DAOException;

public interface IWebPageDAO {
	public List<WebPage> findAll() throws DAOException;
}
