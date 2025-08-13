package org.ace.accounting.system.motor.validator;

import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.common.validation.ValidationResult;
import org.ace.accounting.system.motor.MotorPolicy;
import org.springframework.stereotype.Service;

@Service(value = "MotorPolicyEnquiryValidator")
public class MotorPolicyEnquiryValidator implements IDataValidator<MotorPolicy> {
	@Override
	public ValidationResult validate(MotorPolicy motorPolicy, boolean transaction) {
	    ValidationResult result = new ValidationResult();
	    String formId = "motorEntryForm";

	    String policyNo = motorPolicy.getPolicyNo();
	    String regPattern = "^MP-\\d{4}-\\d{6}$"; // Format: MP-2024-000123

	    if (policyNo == null || !policyNo.matches(regPattern)) {
	        result.addErrorMessage(
	            formId + ":policyNo",
	            "Invalid Policy No. Format. Expected: MP-2024-000123"
	        );
	    }

	    return result;
	}
}
