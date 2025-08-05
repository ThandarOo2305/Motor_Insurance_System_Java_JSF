package org.ace.accounting.common.interfaces;

import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.department.Department;
import org.ace.java.component.persistence.exception.DAOException;

public interface ITranValiDAO {

	public boolean isCurUsed(Currency cur) throws DAOException;

	public boolean isCoaUsed(String coaId) throws DAOException;

	public boolean isDepartmentUsed(Department department) throws DAOException;

	public boolean isBranchUsed(Branch branch) throws DAOException;
}
