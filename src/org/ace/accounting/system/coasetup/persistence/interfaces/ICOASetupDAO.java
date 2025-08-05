package org.ace.accounting.system.coasetup.persistence.interfaces;

import java.util.List;

import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.coasetup.COASetup;
import org.ace.accounting.system.currency.Currency;
import org.ace.java.component.persistence.exception.DAOException;

public interface ICOASetupDAO {
	public COASetup findCOAByACNameAndCur(String acName, Currency currency, Branch branch) throws DAOException;

	public List<COASetup> findCOAListByACNameAndCur(String acName, Currency currency, Branch branch) throws DAOException;

	public List<COASetup> findAllCashAccount() throws DAOException;
}
