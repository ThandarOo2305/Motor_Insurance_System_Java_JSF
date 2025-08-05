package org.ace.accounting.system.coasetup.service;

import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.coasetup.COASetup;
import org.ace.accounting.system.coasetup.persistence.interfaces.ICOASetupDAO;
import org.ace.accounting.system.coasetup.service.interfaces.ICOASetupService;
import org.ace.accounting.system.currency.Currency;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "COASetupService")
public class COASetupService extends BaseService implements ICOASetupService {
	@Resource(name = "COASetupDAO")
	private ICOASetupDAO coaSetupDAO;

	@Transactional(propagation = Propagation.REQUIRED)
	public COASetup findCOASetup(String acType, Currency currency, Branch branch) {
		COASetup result = null;
		try {
			result = coaSetupDAO.findCOAByACNameAndCur(acType, currency, branch);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find coasetup  )", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<COASetup> findCOASetupList(String acType, Currency currency, Branch branch) {
		List<COASetup> result = null;
		try {
			result = coaSetupDAO.findCOAListByACNameAndCur(acType, currency, branch);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find coasetup list )", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<COASetup> findAllCashAccount() {
		List<COASetup> result = null;
		try {
			result = coaSetupDAO.findAllCashAccount();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find coasetup list )", e);
		}
		return result;
	}

}
