package org.ace.accounting.system.motor.validator;

import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.common.validation.ValidationResult;
import org.ace.accounting.system.motor.MotorPolicyVehicleLink;
import org.springframework.stereotype.Service;

@Service(value = "MotorPolicyVehicleLinkEnquiryValidator")
public class MotorPolicyVehicleLinkEnquiryValidator implements IDataValidator<MotorPolicyVehicleLink>{
	@Override
	public ValidationResult validate(MotorPolicyVehicleLink motorPolicyVehicleLink, boolean transaction) {
	        ValidationResult result = new ValidationResult();
	        String formId = "motorEntryForm";
	        String registraionnumber= motorPolicyVehicleLink.getRegistrationNo();
	        
	        String regPattern = "^[A-Z]{3} [0-9][A-Z]/[0-9]{4}$";
	        if (!registraionnumber.matches(regPattern)) {
	            result.addErrorMessage(formId + ":registrationNo", "Invalid Registration No. Format must be like: YGN 1A/1111");
	        }
	        return result;
	}
}
