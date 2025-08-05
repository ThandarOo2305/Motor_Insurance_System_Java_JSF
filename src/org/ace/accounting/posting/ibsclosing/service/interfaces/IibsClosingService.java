package org.ace.accounting.posting.ibsclosing.service.interfaces;

import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.java.component.SystemException;

public interface IibsClosingService {

	void createIbsClosing(Branch closingBranch, Branch ibsBranch, ChartOfAccount iCOA) throws SystemException;

	void createIbsConsolidation(Branch closingBranch, Branch ibsBranch, ChartOfAccount iCOA) throws SystemException;

}
