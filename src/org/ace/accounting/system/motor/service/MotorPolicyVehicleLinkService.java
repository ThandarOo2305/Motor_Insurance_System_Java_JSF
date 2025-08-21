package org.ace.accounting.system.motor.service;

import java.util.List;

import javax.annotation.Resource;

import org.ace.accounting.system.motor.MotorPolicyVehicleLink;
import org.ace.accounting.system.motor.persistence.interfaces.IMotorPolicyVehicleLinkDAO;
import org.ace.accounting.system.motor.service.interfaces.IMotorPolicyVehicleLinkService;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "MotorPolicyVehicleLinkService")
public class MotorPolicyVehicleLinkService extends BaseService implements IMotorPolicyVehicleLinkService{
	
	@Resource(name="MotorPolicyVehicleLinkDAO")
	private IMotorPolicyVehicleLinkDAO vehicleDao;
	
	@Transactional(propagation= Propagation.REQUIRED, readOnly=true)
	public List<MotorPolicyVehicleLink> findAllMotorPolicyVehicleLinks() throws SystemException {
		List<MotorPolicyVehicleLink> result=null;
		try {
			result=vehicleDao.findAll();
			
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to fin all vehicleLink", e);
		}
		return result;
	}
	
	@Transactional(propagation= Propagation.REQUIRED, readOnly=true)
	public MotorPolicyVehicleLink findMotorPolicyVehicleLinkByRegistrationNo(String registrationNo)
			throws SystemException {
		  MotorPolicyVehicleLink result = null;
		    try {
		        result = vehicleDao.findByRegistrationNo(registrationNo);
		    } catch (DAOException e) {
		        throw new SystemException(e.getErrorCode(), "Failed to find vehicleLink by registration number: " + registrationNo, e);
		    }
		    return result;
	}

	@Transactional(propagation= Propagation.REQUIRED)
	public boolean existsByRegistrationNo(String registrationNo) throws SystemException {
		try {
			return vehicleDao.existsByRegistrationNo(registrationNo);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "RegistrationNo already exists!", e);
		}
		
	}
	

}
