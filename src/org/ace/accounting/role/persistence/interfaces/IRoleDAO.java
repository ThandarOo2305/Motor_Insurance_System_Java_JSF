package org.ace.accounting.role.persistence.interfaces;

import java.util.List;

import org.ace.accounting.role.Role;
import org.ace.java.component.persistence.exception.DAOException;

public interface IRoleDAO {
	public List<Role> findAll() throws DAOException;
}
