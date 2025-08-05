package org.ace.accounting.occupation;

import java.util.List;


import javax.annotation.Resource;

import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "OccupationService")
public class OccupationService extends BaseService implements IOccupationService{

	@Resource(name = "DataRepService")
	private IDataRepService<Occupation> dataRepService;
	
	@Resource(name = "OccupationDAO")
	private IOccupationDAO occupationDAO;
	

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Occupation> findAll() throws SystemException {
		List<Occupation> result = null;
		try {
			result =occupationDAO.findAll();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to get occupation list", e);
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Occupation updateOccupation(Occupation occupation) throws SystemException {
		try {
			dataRepService.update(occupation);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to update a Occupation", e);
		}
		return occupation;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteOccupation(Occupation occupation) throws SystemException {
		try {
			 dataRepService.delete(occupation);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to delete a Occupation", e);
		}
		
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Occupation addNewOccupation(Occupation occupation) throws SystemException{
		try {
			dataRepService.insert(occupation);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to add a Occupation", e);
		}
		return occupation;
	}
	

}
