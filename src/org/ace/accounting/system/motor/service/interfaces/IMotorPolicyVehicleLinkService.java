package org.ace.accounting.system.motor.service.interfaces;

import java.util.List;

import org.ace.accounting.system.motor.MotorPolicyVehicleLink;
import org.ace.java.component.SystemException;

public interface IMotorPolicyVehicleLinkService {
	
	public List<MotorPolicyVehicleLink> findAllMotorPolicyVehicleLinks() throws SystemException;
	
	public MotorPolicyVehicleLink findMotorPolicyVehicleLinkByRegistrationNo(String registrationNo) throws SystemException;
	
	public boolean existsByRegistrationNo(String registrationNo) throws SystemException;
}
