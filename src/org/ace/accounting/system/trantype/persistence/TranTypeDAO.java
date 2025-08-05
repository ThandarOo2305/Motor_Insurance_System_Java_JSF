package org.ace.accounting.system.trantype.persistence;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.system.trantype.TranCode;
import org.ace.accounting.system.trantype.TranType;
import org.ace.accounting.system.trantype.persistence.interfaces.ITranTypeDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("TranTypeDAO")
public class TranTypeDAO extends BasicDAO implements ITranTypeDAO {

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<TranType> findAll() throws DAOException {
		List<TranType> result = null;
		try {
			Query q = em.createNamedQuery("TranType.findAll");
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of TranType", pe);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public TranType findByTransCode(TranCode tranCode) throws DAOException{
		List<TranType> result = null;
		try {
			Query q = em.createNamedQuery("TranType.findByTransCode");
			q.setParameter("tranCode", tranCode);
			result =  q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of TranType", pe);
		}
		return result.size() >= 1 ? result.get(0) : null;
	}
	
}
