package org.ace.accounting.system.motor.persistence.interfaces;

import java.util.List;

import org.ace.accounting.system.motor.MotorPolicyVehicleLink;
import org.ace.java.component.persistence.exception.DAOException;

public interface IMotorPolicyVehicleLinkDAO {
	
	public List<MotorPolicyVehicleLink> findAll() throws DAOException;
	
	public MotorPolicyVehicleLink findByRegistrationNo(String registrationNo) throws DAOException;
	
	public boolean existsByRegistrationNo(String registrationNO) throws DAOException;

}
