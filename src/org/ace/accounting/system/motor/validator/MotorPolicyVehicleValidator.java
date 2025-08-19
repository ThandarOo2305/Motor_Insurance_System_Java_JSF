package org.ace.accounting.system.motor.validator;

import java.time.Year;

import javax.annotation.Resource;

import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.common.validation.ValidationResult;
import org.ace.accounting.system.motor.MotorPolicyVehicleLink;
import org.ace.accounting.system.motor.service.interfaces.IMotorPolicyVehicleLinkService;
import org.springframework.stereotype.Service;

@Service(value = "MotorPolicyVehicleValidator")
public class MotorPolicyVehicleValidator implements IDataValidator<MotorPolicyVehicleLink> {

	@Resource(name = "MotorPolicyVehicleLinkService")
	private IMotorPolicyVehicleLinkService motorPolicyVehicleLinkService;

	@SuppressWarnings("unused")
	@Override
	public ValidationResult validate(MotorPolicyVehicleLink motorPolicyVehicleLink, boolean transaction) {
		ValidationResult result = new ValidationResult();
		String formId = "motorEntryForm";
		String registraionnumber = motorPolicyVehicleLink.getRegistrationNo();

		String regPattern = "^[A-Z]{3} [0-9][A-Z]/[0-9]{4}$";
		if (registraionnumber == null || !registraionnumber.matches(regPattern)) {
			result.addErrorMessage(formId + ":registrationNo",
					"Invalid Registration No. Format must be like: YGN 1A/1111");
		} else {
			Boolean registrationresult = motorPolicyVehicleLinkService.existsByRegistrationNo(registraionnumber);
			if (registrationresult) {
				result.addErrorMessage(formId + ":registrationNo", "Registration No is already exist in database ");
			}
		}
		
		/* ---------------- Engine No ---------------- */
        String engineNo = motorPolicyVehicleLink.getEngineNo();
        if (engineNo == null || engineNo.trim().isEmpty()) {
            result.addErrorMessage(formId + ":engineNo", "Engine No is required.");
        } else if (!engineNo.matches("^[A-Z0-9-]{5,17}$")) {
            // Example: only allow alphanumeric & dashes, length 5–17
            result.addErrorMessage(formId + ":engineNo",
                    "Invalid Engine No. Must be 5–17 alphanumeric characters.");
        }

        /* ---------------- Chassis No ---------------- */
        String chassisNo = motorPolicyVehicleLink.getChassisNo();
        if (chassisNo == null || chassisNo.trim().isEmpty()) {
            result.addErrorMessage(formId + ":chassisNo", "Chassis No is required.");
        } else if (!chassisNo.matches("^[A-Z0-9-]{17}$")) {
            // Example: typical chassis numbers are alphanumeric, 17 chars
            result.addErrorMessage(formId + ":chassisNo",
                    "Invalid Chassis No. Must be 17 alphanumeric characters.");
        }

        /* ---------------- Year of Manufacture ---------------- */
        Integer yearOfManufacture = motorPolicyVehicleLink.getYearOfManufacture();
        int currentYear = Year.now().getValue();
        if (yearOfManufacture == null) {
            result.addErrorMessage(formId + ":yearOfManufacture", "Year of Manufacture is required.");
        } else if (yearOfManufacture < 1980 || yearOfManufacture > currentYear) {
            result.addErrorMessage(formId + ":yearOfManufacture",
                    "Invalid Year of Manufacture. Must be between 1900 and " + currentYear + ".");
        }

        /* ---------------- Cubic Capacity ---------------- */
        Integer cubicCapacity = motorPolicyVehicleLink.getCubicCapacity();
        if (cubicCapacity == null) {
            result.addErrorMessage(formId + ":cubicCapacity", "Cubic Capacity is required.");
        } else if (cubicCapacity <= 0) {
            result.addErrorMessage(formId + ":cubicCapacity", "Cubic Capacity must be greater than 0.");
        } else if (cubicCapacity > 10000) {
            // Optional: set an upper limit for sanity (e.g., 10,000 CC)
            result.addErrorMessage(formId + ":cubicCapacity",
                    "Cubic Capacity seems too large. Please enter a valid value.");
        }

		return result;
	}

}
