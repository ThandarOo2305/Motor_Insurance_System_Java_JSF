package org.ace.accounting.system.motor.validator;

import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.common.validation.ValidationResult;
import org.ace.accounting.system.motor.MotorPolicy;
import org.springframework.stereotype.Service;

@Service(value = "MotorPolicyValidator")
public class MotorPolicyValidator implements IDataValidator<MotorPolicy>{

	@Override
	public ValidationResult validate(MotorPolicy motorPolicy, boolean transaction) {
        ValidationResult result = new ValidationResult();
        String formId = "MotorEntryForm";
        int period = motorPolicy.getPeriod();
        
        if (period != 3 && period != 6 && period != 9 && period != 12) {
            result.addErrorMessage(formId + ":Period", "Period can't be except 3,6,9 or 12");
        }

        return result;
	}

}
