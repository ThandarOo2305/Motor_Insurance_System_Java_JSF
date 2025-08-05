package org.ace.accounting.user.persistence;

import java.util.List;


import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.user.User;
import org.ace.accounting.user.persistence.interfaces.IUserDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("UserDAO")
public class UserDAO extends BasicDAO implements IUserDAO {

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<User> findAll() throws DAOException {
		List<User> result = null;
		try {
			Query q = em.createNamedQuery("User.findAll");
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of User", pe);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public User find(String usercode) throws DAOException {
		User result = null;
		try {
			Query q = em.createNamedQuery("User.findByUsercode");
			q.setParameter("userCode", usercode);
			result = (User) q.getSingleResult();
			em.flush();
		} catch (NoResultException pe) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find User(Username = " + usercode + ")", pe);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void changePassword(String usercode, String newPassword) throws DAOException {
		try {
			Query q = em.createNamedQuery("User.changePassword");
			q.setParameter("usercode", usercode);
			q.setParameter("newPassword", newPassword);
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to change Password (Username = " + usercode + ")", pe);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void resetPassword(String usercode, String defaultPassword) throws DAOException {
		try {
			Query q = em.createNamedQuery("User.resetPassword");
			q.setParameter("usercode", usercode);
			q.setParameter("defaultPassowrd", defaultPassword);
			q.executeUpdate();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to reset Password (Username = " + usercode + ")", pe);
		}
	}

}
