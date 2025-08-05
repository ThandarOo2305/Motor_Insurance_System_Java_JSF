package org.ace.accounting.system.webPage.persistence;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.system.webPage.WebPage;
import org.ace.accounting.system.webPage.persistence.interfaces.IWebPageDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("WebPageDAO")
public class WebPageDAO extends BasicDAO implements IWebPageDAO{
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<WebPage> findAll() throws DAOException {
		List<WebPage> result = null;
		try {
			Query q = em.createNamedQuery("WebPage.findAll");
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of WebPage", pe);
		}
		return result;
	}
}
