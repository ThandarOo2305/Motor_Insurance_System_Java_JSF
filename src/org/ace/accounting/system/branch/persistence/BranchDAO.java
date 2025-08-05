package org.ace.accounting.system.branch.persistence;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.persistence.interfaces.IBranchDAO;
import org.ace.accounting.system.chartaccount.CurrencyChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("BranchDAO")
public class BranchDAO extends BasicDAO implements IBranchDAO {
	@Resource(name = "DataRepService")
	private IDataRepService<Branch> branchDataRepService;

	@Resource(name = "DataRepService")
	private IDataRepService<CurrencyChartOfAccount> ccoaDataRepService;

	@Resource(name = "CcoaService")
	private ICcoaService ccoaService;

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Branch> findAll() throws DAOException {
		List<Branch> result = null;
		try {
			Query q = em.createNamedQuery("Branch.findAll");
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of Branch", pe);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Branch findByBranchCode(String branchCode) throws DAOException {
		Branch result = null;
		try {
			Query q = em.createNamedQuery("Branch.findByBranchCode");
			q.setParameter("branchCode", branchCode);
			result = (Branch) q.getSingleResult();
			em.flush();
		} catch (NoResultException pe) {
			return null;
		} catch (PersistenceException pe) {
			throw translate("Failed to find Branch with branchCode : " + branchCode, pe);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Branch branch) throws DAOException {
		Query query = null;
		StringBuffer buffer = null;
		try {
			buffer = new StringBuffer();
			buffer.append("DELETE FROM CurrencyChartOfAccount c WHERE c.branch.id = :branchId");
			query = em.createQuery(buffer.toString());
			query.setParameter("branchId", branch.getId());
			query.executeUpdate();
			em.flush();

			branch = em.merge(branch);
			em.remove(branch);
			em.flush();

		} catch (PersistenceException pe) {
			throw translate("Failed to delete Branch", pe);
		}
	}
}