package org.ace.accounting.system.department.persistence.interfaces;

import java.util.List;

import org.ace.accounting.system.department.Department;
import org.ace.java.component.persistence.exception.DAOException;

public interface IDepartmentDAO {
	public List<Department> findAll() throws DAOException;
}
