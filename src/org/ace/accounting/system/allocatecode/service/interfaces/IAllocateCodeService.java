package org.ace.accounting.system.allocatecode.service.interfaces;

import java.util.List;

import org.ace.accounting.system.allocatecode.AllocateCode;
import org.primefaces.model.TreeNode;

public interface IAllocateCodeService {
	public List<AllocateCode> findAllAllocateCode();

	public List<AllocateCode> findAllocateCodeBy(String budgetYear);

	public List<String> findCoaIDBy(String budgetYear);

	public List<AllocateCode> findAllocateCodeByNative(String budgetYear);

	public void deleteAllocateCodeBy(String budgetYear);

	public void addAllocateCodeBy(AllocateCode allocateCode, String coaID);

	public void addAllocateCodeBy(String budgetYear, TreeNode[] selectedNodes, List<AllocateCode> allocateCodeList);
}
