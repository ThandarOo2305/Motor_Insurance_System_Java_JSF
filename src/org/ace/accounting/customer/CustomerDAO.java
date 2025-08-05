package org.ace.accounting.customer;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;


@Repository(value = "CustomerDAO")
public class CustomerDAO extends BasicDAO implements ICustomerDAO {
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Customer> findAllCustomer() throws DAOException {
		List<Customer> customerList = null;
		try {
			 Query query = em.createNamedQuery("Customer.findAll");
			 customerList = query.getResultList();
			 em.flush();
		} catch (PersistenceException pe) {
			throw translate("Failed to find all of Customer", pe);
		}
		return customerList;
	}
	

}
