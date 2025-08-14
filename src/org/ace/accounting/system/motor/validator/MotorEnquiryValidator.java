package org.ace.accounting.system.motor.validator;

import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.common.validation.ValidationResult;
import org.ace.accounting.system.motor.MotorEnquiry;
import org.springframework.stereotype.Service;

@Service(value = "MotorEnquiryValidator")
public class MotorEnquiryValidator implements IDataValidator<MotorEnquiry> {

	@Override
	public ValidationResult validate(MotorEnquiry obj, boolean transaction) {
		ValidationResult result = new ValidationResult();
		String formId = "enquiryForm";

		// Null-safe values
		String policyNo = obj.getPolicyNo() != null ? obj.getPolicyNo().trim() : "";
		String registrationNo = obj.getRegistrationNo() != null ? obj.getRegistrationNo().trim() : "";

		String regPatternPolicy = "^MTR-\\d{2}-\\d{6}$";        // e.g., MTR-25-000123
		String regPatternRegNo = "^[A-Z]{3} [0-9][A-Z]/[0-9]{4}$"; // e.g., YGN 1A/1111

		// Ensure at least one search criteria is provided
		if (obj.getPolicyStartDate() == null &&
		    obj.getPolicyEndDate() == null &&
		    policyNo.isEmpty() &&
		    registrationNo.isEmpty()) {

			result.addErrorMessage(formId + ":policyNo", "There must be at least one condition to search");
		} else {
			// Validate policy number format if provided
			if (!policyNo.isEmpty() && !policyNo.matches(regPatternPolicy)) {
				result.addErrorMessage(formId + ":policyNo", "Invalid Policy No. Format. Expected: MTR-25-000123");
			}

			// Validate registration number format if provided
			if (!registrationNo.isEmpty() && !registrationNo.matches(regPatternRegNo)) {
				result.addErrorMessage(formId + ":registrationNo",
						"Invalid Registration No. Format must be like: YGN 1A/1111");
			}
		}

		return result;
	}
}