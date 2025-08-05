package org.ace.accounting.system.setup.service;

import java.util.Date;

import javax.annotation.Resource;

import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.system.setup.persistence.interfaces.ISetupDAO;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "SetupService")
public class SetupService extends BaseService implements ISetupService {

	@Resource(name = "SetupDAO")
	private ISetupDAO setupDAO;

	@Transactional(propagation = Propagation.REQUIRED)
	public String findSetupValueByVariable(String variable) throws DAOException {
		String result = null;
		try {
			result = setupDAO.findSetupValueByVariable(variable);
		} catch (DAOException de) {
			throw new SystemException(de.getErrorCode(), "Failed to handle yearly posting.", de);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void insert(String variable) throws DAOException {
		try {
			setupDAO.insert(variable);
		} catch (DAOException de) {
			throw new SystemException(de.getErrorCode(), "Failed to handle yearly posting.", de);
		}
	}

	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insert(String editPassword, Date backDate) throws DAOException {
		try {
			setupDAO.updateSetupValueByVariable(BusinessUtil.BACKDATE, DateUtils.formatDateToString(backDate));
			setupDAO.updateSetupValueByVariable(BusinessUtil.EDIT_PASSWORD,editPassword);
		} catch (DAOException de) {
			throw new SystemException(de.getErrorCode(), "Failed to handle yearly posting.", de);
		}
	}

}
