package org.ace.accounting.system.gainloss.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.ace.accounting.system.gainloss.ExchangeConfig;
import org.ace.accounting.system.gainloss.persistence.interfaces.IExchangeConfigDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("ExchangeConfigDAO")
public class ExchangeConfigDAO extends BasicDAO implements IExchangeConfigDAO {
	
	@Transactional(propagation = Propagation.REQUIRED,readOnly = true)
	@Override
	public List<ExchangeConfig> findAllExchangeConfig() throws DAOException{
		try {
			TypedQuery<ExchangeConfig> query = em.createNamedQuery("ExchangeConfig.findAll", ExchangeConfig.class);
			return query.getResultList();
		} catch (NoResultException ne) {
			return new ArrayList<>();
		} catch (PersistenceException pe) {
			throw translate("Fail to find all ExchangeConfig Data", pe);
		}
	}

}
