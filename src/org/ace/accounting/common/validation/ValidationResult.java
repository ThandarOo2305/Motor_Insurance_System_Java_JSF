package org.ace.accounting.common.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
	
	private List<ErrorMessage> errormessages;

	public ValidationResult() {
		errormessages = new ArrayList<ErrorMessage>();
	}

	public boolean isVerified() {
		return errormessages.isEmpty();
	}

	public void addErrorMessage(String id, String errorcode, Object... params) {
		errormessages.add(new ErrorMessage(id, errorcode, params));
	}

	public List<ErrorMessage> getErrorMeesages() {
		return errormessages;
	}	
}
