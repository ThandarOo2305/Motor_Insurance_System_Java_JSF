package org.ace.accounting.system.allocatecode.service;

import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.system.allocatecode.AllocateCode;
import org.ace.accounting.system.allocatecode.persistence.interfaces.IAllocateCodeDAO;
import org.ace.accounting.system.allocatecode.service.interfaces.IAllocateCodeService;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.persistence.interfaces.ICoaDAO;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.primefaces.model.TreeNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "AllocateCodeService")
public class AllocateCodeService extends BaseService implements IAllocateCodeService {

	@Resource(name = "AllocateCodeDAO")
	private IAllocateCodeDAO allocateCodeDAO;

	@Resource(name = "CoaDAO")
	private ICoaDAO coaDAO;

	@Resource(name = "DataRepService")
	private IDataRepService<AllocateCode> dataRepService;

	@Transactional(propagation = Propagation.REQUIRED)
	public List<AllocateCode> findAllAllocateCode() {
		List<AllocateCode> result = null;
		try {
			result = allocateCodeDAO.findAll();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of AllocateCode)", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<AllocateCode> findAllocateCodeBy(String budgetYear) {
		List<AllocateCode> result = null;
		try {
			result = allocateCodeDAO.findAllocateCodeBy(budgetYear);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find AllocateCode by Year)", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<String> findCoaIDBy(String budgetYear) {
		List<String> result = null;
		try {
			result = allocateCodeDAO.findCoaIDBy(budgetYear);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find AllocateCode by Year)", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<AllocateCode> findAllocateCodeByNative(String budgetYear) {
		List<AllocateCode> result = null;
		try {
			result = allocateCodeDAO.findAllocateCodeByNative(budgetYear);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find AllocateCode by Year)", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void addAllocateCodeBy(AllocateCode allocateCode, String coaID) {
		try {
			allocateCodeDAO.addAllocateCodeBy(allocateCode, coaID);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to add AllocateCode by AllocateCode and coaID)", e);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void addAllocateCodeBy(String budgetYear, TreeNode[] selectedNodes, List<AllocateCode> allocateCodeList) {
		try {
			AllocateCode tempAllocateCode;
			allocateCodeDAO.deleteAllocateCodeBy(budgetYear);
			for (TreeNode node : selectedNodes) {
				
				String coaID = ((ChartOfAccount)node.getData()).getAcCode();

				if (!coaID.equals("Account Code")) {
//					String[] tt = coaID.split("-");
//					coaID = tt[0];
					for (AllocateCode allocateCode : allocateCodeList) {
						tempAllocateCode = new AllocateCode(budgetYear, allocateCode.getDepartment(), coaDAO.findByAcCode(coaID), allocateCode.getAmtPercent(),
								allocateCode.getBasedOn());
						dataRepService.insert(tempAllocateCode);
					}
				}
			}
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to add AllocateCode by AccountList)", e);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAllocateCodeBy(String budgetYear) {
		try {
			allocateCodeDAO.deleteAllocateCodeBy(budgetYear);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find AllocateCode by Year)", e);
		}
	}
}
