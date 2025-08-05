package org.ace.accounting.system.branch.persistence.interfaces;

import java.util.List;

import org.ace.accounting.system.branch.Branch;
import org.ace.java.component.persistence.exception.DAOException;

public interface IBranchDAO {
	public List<Branch> findAll() throws DAOException;

	public Branch findByBranchCode(String branchCode) throws DAOException;

	public void delete(Branch branch) throws DAOException;

}
