package org.ace.accounting.role.persistence;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.role.Role;
import org.ace.accounting.role.persistence.interfaces.IRoleDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("RoleDAO")
public class RoleDAO extends BasicDAO implements IRoleDAO {

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Role> findAll() throws DAOException {
		List<Role> result = null;
		try {
			Query q = em.createNamedQuery("Role.findAll");
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of Role", pe);
		}
		return result;
	}

}
