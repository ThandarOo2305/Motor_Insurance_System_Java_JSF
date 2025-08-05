package org.ace.accounting.common.service;

import javax.annotation.Resource;

import org.ace.accounting.common.interfaces.ITranValiDAO;
import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.common.validation.ValidationResult;
import org.ace.accounting.system.department.Department;
import org.springframework.stereotype.Service;

@Service(value="DepartmentValidator")
public class DepartmentValidator implements IDataValidator<Department> {
	
	@Resource(name = "TranValiDAO")
	private ITranValiDAO tranValiDAO;

	@Override
	public ValidationResult validate(Department department, boolean transaction) {
		
		ValidationResult result = new ValidationResult();
		
		String formId = "departmentEntryForm";
		
		if(transaction){
			
			if(tranValiDAO.isDepartmentUsed(department)){
				result.addErrorMessage(formId+":panelDept", MessageId.DEPT_USED_TRANS);
			}
			
		}
		
		return result;
	}

}
