package org.ace.accounting.role.service;

import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.role.Role;
import org.ace.accounting.role.persistence.interfaces.IRoleDAO;
import org.ace.accounting.role.service.interfaces.IRoleService;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "RoleService")
public class RoleService extends BaseService implements IRoleService {

	@Resource(name = "RoleDAO")
	private IRoleDAO roleDAO;

	@Transactional(propagation = Propagation.REQUIRED)
	public List<Role> findAllRole() {
		List<Role> result = null;
		try {
			result = roleDAO.findAll();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of Role)", e);
		}
		return result;
	}

}
