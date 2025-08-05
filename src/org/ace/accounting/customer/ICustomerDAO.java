package org.ace.accounting.customer;

import java.util.List;

import org.ace.java.component.persistence.exception.DAOException;

public interface ICustomerDAO {
	
	public List<Customer> findAllCustomer()throws DAOException;

}
