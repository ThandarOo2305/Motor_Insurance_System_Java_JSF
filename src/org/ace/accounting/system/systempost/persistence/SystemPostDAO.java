package org.ace.accounting.system.systempost.persistence;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.system.systempost.SystemPost;
import org.ace.accounting.system.systempost.persistence.interfaces.ISystemPostDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("SystemPostDAO")
public class SystemPostDAO extends BasicDAO implements ISystemPostDAO{
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<SystemPost> findAll() throws DAOException {
		List<SystemPost> result = null;
		try {
			Query q = em.createNamedQuery("SystemPost.findAll");
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of SystemPost", pe);
		}
		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public SystemPost findbyPostingName(String postingName) throws DAOException {
		SystemPost result = null;
		try {
				Query q = em.createNamedQuery("SystemPost.findbyPostingName");
				q.setParameter("postingName", postingName);
				result = (SystemPost)q.getSingleResult();
				em.flush();
			}catch (NoResultException pe) {
				return null;
			}catch (PersistenceException pe) {
				throw translate("Failed to find by Posting Name" + postingName, pe);
			}
		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updatePostingDateByName(Date postingDate, String postingName)throws DAOException{
		try {
				Query q = em.createNamedQuery("SystemPost.updatePostingDateByName");
				q.setParameter("postingDate", postingDate);
				q.setParameter("postingName", postingName);
				q.executeUpdate();
				em.flush();
			}catch (PersistenceException pe) {
				throw translate("Failed to Update PostingDate by Posting Name" + postingName, pe);
			}
	}
}
