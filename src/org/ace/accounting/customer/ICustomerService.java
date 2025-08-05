package org.ace.accounting.customer;

import java.util.List;

import org.ace.java.component.SystemException;

public interface ICustomerService {
	
	public List<Customer> findAll()throws SystemException;
	
	public Customer addNewCustomer(Customer customer)throws SystemException;
	
	public Customer updateCustomer(Customer customer)throws SystemException;
	
	public void deleteCustomer(Customer customer)throws SystemException;
	
	public Customer changeDTOToCustomer(CustomerDTO customerDTO) ;
	
	public CustomerDTO changeCustomerToDTO(Customer customer) ;

}
