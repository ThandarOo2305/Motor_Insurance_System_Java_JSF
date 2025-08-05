package org.ace.accounting.system.department.persistence;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.system.department.Department;
import org.ace.accounting.system.department.persistence.interfaces.IDepartmentDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("DepartmentDAO")
public class DepartmentDAO extends BasicDAO implements IDepartmentDAO {

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Department> findAll() throws DAOException {
		List<Department> result = null;
		try {
			Query q = em.createNamedQuery("Department.findAll");
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of Department", pe);
		}
		return result;
	}
}
