package org.ace.accounting.system.systempost.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.system.systempost.SystemPost;
import org.ace.accounting.system.systempost.persistence.interfaces.ISystemPostDAO;
import org.ace.accounting.system.systempost.service.interfaces.ISystemPostService;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "SystemPostService")
public class SystemPostService extends BaseService implements ISystemPostService {

	@Resource(name = "SystemPostDAO")
	private ISystemPostDAO sysPostDAO;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<SystemPost> findAll() {
		List<SystemPost> result = null;
		try {
			result = sysPostDAO.findAll();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of SystemPost)", e);
		}
		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public SystemPost findbyPostingName(String postingName) {
		SystemPost result = null;
		try {
			result = sysPostDAO.findbyPostingName(postingName);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find SystemPost by Posting Name" + postingName, e);
		}
		return result;
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public void updatePostingDateByName(Date postingDate, String postingName) {
		try {
				sysPostDAO.updatePostingDateByName(postingDate, postingName);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to Update PostingDate By Posting Name" + postingName, e);
		}
	}
	

}
