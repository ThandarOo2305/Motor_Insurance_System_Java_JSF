package org.ace.accounting.system.coasetup.service.interfaces;

import java.util.List;

import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.coasetup.COASetup;
import org.ace.accounting.system.currency.Currency;

public interface ICOASetupService {
	public COASetup findCOASetup(String acType, Currency currency, Branch branch);

	public List<COASetup> findCOASetupList(String acType, Currency currency, Branch branch);

	public List<COASetup> findAllCashAccount();
}
