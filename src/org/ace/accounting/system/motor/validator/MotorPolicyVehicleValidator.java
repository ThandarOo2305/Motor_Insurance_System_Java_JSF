package org.ace.accounting.system.motor.validator;

import javax.annotation.Resource;

import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.common.validation.ValidationResult;
import org.ace.accounting.system.motor.MotorPolicyVehicleLink;
import org.ace.accounting.system.motor.service.interfaces.IMotorPolicyVehicleLinkService;
import org.springframework.stereotype.Service;

@Service(value = "MotorPolicyVehicleValidator")
public class MotorPolicyVehicleValidator implements IDataValidator<MotorPolicyVehicleLink>{

	@Resource(name = "MotorPolicyVehicleLinkService")
    private IMotorPolicyVehicleLinkService motorPolicyVehicleLinkService;
	
	public void setMotorPolicyVehicleLinkService(IMotorPolicyVehicleLinkService motorPolicyVehicleLinkService) {
		this.motorPolicyVehicleLinkService = motorPolicyVehicleLinkService;
	}

	@Override
	public ValidationResult validate(MotorPolicyVehicleLink motorPolicyVehicleLink, boolean transaction) {
        ValidationResult result = new ValidationResult();
        String formId = "MotorEntryForm";
        String registraionnumber= motorPolicyVehicleLink.getRegistrationNo();
        
        String regPattern = "^[A-Z]{3} [0-9][A-Z]/[0-9]{4}$";
        if (registraionnumber == null || !registraionnumber.matches(regPattern)) {
            result.addErrorMessage(formId + ":registrationNo", "Invalid Registration No. Format must be like: YGN 1A/1111");
        }else {
        	Boolean registrationresult = motorPolicyVehicleLinkService.existsByRegistrationNo(registraionnumber);
        	if(registrationresult) {
        		result.addErrorMessage(formId + ":registrationNo", "Registration No is already exist in database ");
        	}
        }
        
        return result;
	}

}
