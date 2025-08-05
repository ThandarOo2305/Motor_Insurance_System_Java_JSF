package org.ace.java.component.service;

import javax.annotation.Resource;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.persistence.interfaces.IDataRepository;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "DataRepService")
public class DataRepService<T> extends BaseService implements IDataRepService<T> {

	@Resource(name = "DataRepository")
	private IDataRepository<T> dataRepository;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void insert(Object object) {
		try {
			dataRepository.insert(object);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to add a new " + object.getClass().getName(), e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public T update(T param) {
		try {
			dataRepository.update(param);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to update a " + param.getClass().getName(), e);
		}
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(Object object) {
		try {
			dataRepository.delete(object);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to delete a " + object.getClass().getName(), e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public T findById(Class<T> paramClass, Object paramObject) {
		T result = null;
		try {
			result = dataRepository.findById(paramClass, paramObject);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find a " + paramClass.getName() + "(ID : " + paramObject.toString() + ")", e);
		}
		return result;
	}

}
