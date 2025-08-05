package org.ace.accounting.system.allocatecode.persistence;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.system.allocatecode.AllocateCode;
import org.ace.accounting.system.allocatecode.persistence.interfaces.IAllocateCodeDAO;
import org.ace.accounting.system.department.Department;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("AllocateCodeDAO")
public class AllocateCodeDAO extends BasicDAO implements IAllocateCodeDAO {

	@Resource(name = "DataRepService")
	private IDataRepService<Department> dataRepService;
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AllocateCode> findAll() throws DAOException {
		List<AllocateCode> result = null;
		try {
			Query q = em.createNamedQuery("AllocateCode.findAll");
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of AllocateCode", pe);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AllocateCode> findAllocateCodeBy(String budgetYear)throws DAOException{
		List<AllocateCode> result = null;
		try {
			Query q = em.createNamedQuery("AllocateCode.findAllocateCodeByBudgetYear");			
			q.setParameter("budget", budgetYear);
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of AllocateCode", pe);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<String> findCoaIDBy(String budgetYear)throws DAOException{
		List<String> result = null;
		try {
			Query q = em.createNamedQuery("AllocateCode.findCoaIDBy");
			
			q.setParameter("budget", budgetYear);
			result = q.getResultList();
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of AllocateCode", pe);
		}
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AllocateCode> findAllocateCodeByNative(String budgetYear)throws DAOException{
		List<AllocateCode> result = new ArrayList<AllocateCode>();
		try {
			//Query q = em.createNamedQuery("AllocateCode.findAllocateCodeByBudgetYear");
			StringBuffer bf = new StringBuffer("SELECT distinct DEPARTMENTID,AMTPERCENT,BUDGET,BASEDON FROM Allocate_Code WHERE BUDGET=? ");
			Query q = em.createNativeQuery(bf.toString());
			
			q.setParameter(1, budgetYear);
			List<Object[]> tempResults = q.getResultList();
			
			String budget= "";
			BigDecimal basedon;
			Department department=null;
			BigDecimal amtPercent;
			
			
			for(Object o[]:tempResults){
				department = dataRepService.findById(Department.class, o[0].toString());
				amtPercent = new BigDecimal( o[1].toString() );
				budget = o[2].toString();
				basedon = new BigDecimal( o[3].toString());
				result.add(new AllocateCode(budget, department, amtPercent, basedon));
			}			
			em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of AllocateCode", pe);
		}
		return result;
	}
	
	public void addAllocateCodeBy(AllocateCode allocateCode,String coaID){
		try{
			 Query q  = em.createNamedQuery("AllocateCode.addAllocateCodeBydfdfdtest");
			 q.setParameter("budget", coaID);
			 q.executeUpdate();
			 em.flush();
		}catch(PersistenceException pe){
			throw translate("Failed to delete Allocate by budgetYear" , pe);
		}
	}
	
	public void deleteAllocateCodeBy(String budgetYear){
		try{
			 Query q  = em.createNamedQuery("AllocateCode.deleteAllocateCodeByBudgetYear");
			 q.setParameter("budget", budgetYear);
			 q.executeUpdate();
			 em.flush();
		}catch(PersistenceException pe){
			throw translate("Failed to delete Allocate by budgetYear" , pe);
		}
	}
}
