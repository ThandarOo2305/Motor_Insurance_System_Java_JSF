package org.ace.accounting.system.branch.service.interfaces;

import java.util.List;

import org.ace.accounting.system.branch.Branch;
import org.ace.java.component.SystemException;

public interface IBranchService {

	public List<Branch> findAllBranch() throws SystemException;

	public Branch findBranchByBranchCode(String branchCode) throws SystemException;

	public void addNewBranch(Branch branch) throws SystemException;

	public void updateBranch(Branch branch) throws SystemException;

	public void deleteBranch(Branch branch) throws SystemException;

}
