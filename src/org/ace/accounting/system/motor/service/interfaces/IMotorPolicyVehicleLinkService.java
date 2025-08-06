package org.ace.accounting.system.motor.service.interfaces;

import java.util.List;

import org.ace.accounting.system.motor.MotorPolicyVehicleLink;
import org.ace.java.component.SystemException;

public interface IMotorPolicyVehicleLinkService {
	
	public List<MotorPolicyVehicleLink> findAllMotorPolicyVehicleLinks() throws SystemException;
	
	public MotorPolicyVehicleLink findMotorPolicyVehicleLinkByRegistrationNo(String registrationNo) throws SystemException;
	
	public void addNewMotorPolicyVehicleLink(MotorPolicyVehicleLink link) throws SystemException;

	public void updateMotorPolicyVehicleLink(MotorPolicyVehicleLink link) throws SystemException;

	public void deleteMotorPolicyVehicleLink(MotorPolicyVehicleLink link) throws SystemException;
}
