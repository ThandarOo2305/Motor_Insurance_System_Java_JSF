package org.ace.accounting.system.motor.validator;

import javax.annotation.Resource;

import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.common.validation.ValidationResult;
import org.ace.accounting.system.motor.MotorPolicy;
import org.ace.accounting.system.motor.service.interfaces.IMotorPolicyService;
import org.springframework.stereotype.Service;

@Service(value = "MotorPolicyValidator")
public class MotorPolicyValidator implements IDataValidator<MotorPolicy>{

	@Resource(name = "MotorPolicyService")
	private IMotorPolicyService motorPolicyService;
	
	@Override
	public ValidationResult validate(MotorPolicy motorPolicy, boolean transaction) {
        ValidationResult result = new ValidationResult();
        String formId = "MotorEntryForm";
        String policyNo = motorPolicy.getPolicyNo();
        int period = motorPolicy.getPeriod();
        
        System.out.println("in policy validator");
        String regPattern = "^MTR-\\d{2}-\\d{6}$"; // Format: MTR-25-000123
        if (!policyNo.isEmpty() && !policyNo.matches(regPattern)) {
			result.addErrorMessage(formId + ":policyNo", "Invalid Policy No. Format. Expected: MTR-25-000123");
		}
        
        if (period != 3 && period != 6 && period != 9 && period != 12) {
            result.addErrorMessage(formId + ":Period", "Period can't be except 3,6,9 or 12");
        }
        Boolean motorfind = motorPolicyService.existsMotorPolicyByPolicyNo(policyNo);
        if(motorfind) {
        	result.addErrorMessage(formId + ":PolicyNo", "Policy Number is already exits");
        }
        return result;
	}

}
