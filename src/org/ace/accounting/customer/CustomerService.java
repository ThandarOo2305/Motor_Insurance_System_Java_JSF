package org.ace.accounting.customer;

import java.util.List;

import javax.annotation.Resource;

import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "CustomerService")
public class CustomerService extends BaseService implements ICustomerService {
	
	@Resource(name = "DataRepService")
	private IDataRepService<Customer> dataRepService;
	
	@Resource(name = "CustomerDAO")
	private ICustomerDAO customerDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Customer> findAll() throws SystemException {
		List<Customer> customerList = null;
		try {
			customerList = customerDAO.findAllCustomer();
			
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to get customer list", e);
		}
		return customerList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Customer addNewCustomer(Customer customer) throws SystemException {
		try {
			dataRepService.insert(customer);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to create a Customer", e);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Customer updateCustomer(Customer customer) throws SystemException {
		try {
			dataRepService.update(customer);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to update a Customer", e);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteCustomer(Customer customer) throws SystemException {
		try {
			dataRepService.delete(customer);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to delete a Customer", e);
		}
		
	}

	@Override
	public Customer changeDTOToCustomer(CustomerDTO customerDTO) {
		Customer customer = new Customer();
		customer.setId(customerDTO.getId());
		customer.setSalutation(customerDTO.getSalutation());
		customer.setFirstName(customerDTO.getFirstName());
		customer.setMiddleName(customerDTO.getMiddleName());
		customer.setLastName(customerDTO.getLastName());
		customer.setFatherName(customerDTO.getFatherName());
		customer.setGender(customerDTO.getGender());
		customer.setDateOfBirth(customerDTO.getDateOfBirth());
		customer.setBirthMark(customerDTO.getBirthMark());
		customer.setMaritalStatus(customerDTO.getMaritalStatus());
		customer.setOccupation(customerDTO.getOccupation());
		customer.setResidentAddress(customerDTO.getResidentAddress());
		customer.setResidentTownship(customerDTO.getResidentTownship());
		customer.setPermanentAddress(customerDTO.getPermanentAddress());
		customer.setPermanentTownship(customerDTO.getPermanentTownship());
		customer.setOfficeAddress(customerDTO.getOfficeTownship());
		customer.setOfficeTownship(customerDTO.getOfficeAddress());
		customer.setPhone(customerDTO.getPhone());
		customer.setFax(customerDTO.getFax());
		customer.setMobile(customerDTO.getMobile());
		customer.setEmail(customerDTO.getEmail());
		customer.setRelativeSalutation(customerDTO.getRelativeSalutation());
		customer.setRelativeFirstName(customerDTO.getRelativeFirstName());
		customer.setRelativeMiddleName(customerDTO.getRelativeMiddleName());
		customer.setRelativeLastName(customerDTO.getRelativeLastName());
		customer.setRelationship(customerDTO.getRelationship());
		customer.setRelativeDOB(customerDTO.getRelativeDOB());
		customer.setVersion(customerDTO.getVersion());
		customer.setBasicEntity(customerDTO.getBasicEntity());
		return customer;
	}

	@Override
	public CustomerDTO changeCustomerToDTO(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setId(customer.getId());
		customerDTO.setSalutation(customer.getSalutation());
		customerDTO.setFirstName(customer.getFirstName());
		customerDTO.setMiddleName(customer.getMiddleName());
		customerDTO.setLastName(customer.getLastName());
		customerDTO.setFatherName(customer.getFatherName());
		customerDTO.setGender(customer.getGender());
		customerDTO.setDateOfBirth(customer.getDateOfBirth());
		customerDTO.setBirthMark(customer.getBirthMark());
		customerDTO.setMaritalStatus(customer.getMaritalStatus());
		customerDTO.setOccupation(customer.getOccupation());
		customerDTO.setResidentAddress(customer.getResidentAddress());
		customerDTO.setResidentTownship(customer.getResidentTownship());
		customerDTO.setPermanentAddress(customer.getPermanentAddress());
		customerDTO.setPermanentTownship(customer.getPermanentTownship());
		customerDTO.setOfficeAddress(customer.getOfficeAddress());
		customerDTO.setOfficeTownship(customer.getOfficeTownship());
		customerDTO.setPhone(customer.getPhone());
		customerDTO.setFax(customer.getFax());
		customerDTO.setMobile(customer.getMobile());
		customerDTO.setEmail(customer.getEmail());
		customerDTO.setRelativeSalutation(customer.getRelativeSalutation());
		customerDTO.setRelativeFirstName(customer.getRelativeFirstName());
		customerDTO.setRelativeMiddleName(customer.getRelativeMiddleName());
		customerDTO.setRelativeLastName(customer.getRelativeLastName());
		customerDTO.setRelationship(customer.getRelationship());
		customerDTO.setRelativeDOB(customer.getRelativeDOB());
		customerDTO.setVersion(customer.getVersion());
		customerDTO.setBasicEntity(customer.getBasicEntity());
		return customerDTO;
	}
	
	

	
}
