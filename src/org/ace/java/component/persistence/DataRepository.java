package org.ace.java.component.persistence;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.persistence.interfaces.IDataRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("DataRepository")
public class DataRepository<T> extends BasicDAO implements IDataRepository<T> {

	/**
	 * Basic Insert Operation using EM
	 * 
	 * @param object
	 *            -> object to insert
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insert(Object object) throws DAOException {
		try {
			em.persist(object);
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to insert " + object.getClass().getName(), pe);
		}
	}

	/**
	 * Basic Update Operation using EM
	 * 
	 * @param param
	 *            -> Object to update
	 * @return updated object
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public T update(T param) throws DAOException {
		try {
			param = em.merge(param);
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to update " + param.getClass().getName(), pe);
		}
		return param;
	}

	/**
	 * Basic Delete Operation using EM
	 * 
	 * @param object
	 *            -> Object to delete
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Object object) throws DAOException {
		try {
			object = em.merge(object);
			em.remove(object);
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to delete " + object.getClass().getName(), pe);
		}
	}

	/**
	 * Basic Find Operation using EM
	 * 
	 * @param paramClass
	 *            -> class name
	 * @param paramObject
	 *            -> primary key value of enity object
	 * @return result value
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public T findById(Class<T> paramClass, Object paramObject) throws DAOException {
		T result = null;
		try {
			result = em.find(paramClass, paramObject);
			em.flush();
		} catch (NoResultException nre) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find By id " + paramObject.getClass().getName(), pe);
		}
		return result;
	}

}
