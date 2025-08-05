package org.ace.accounting.web.system;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.validation.ErrorMessage;
import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.common.validation.ValidationResult;
import org.ace.accounting.system.department.Department;
import org.ace.accounting.system.department.service.interfaces.IDepartmentService;
import org.ace.java.component.SystemException;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.ace.java.web.common.BaseBean;

@ManagedBean(name = "ManageDepartmentActionBean")
@ViewScoped
public class ManageDepartmentActionBean extends BaseBean {

	@ManagedProperty(value = "#{DepartmentService}")
	private IDepartmentService departmentService;

	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@ManagedProperty(value = "#{DataRepService}")
	private IDataRepService<Department> dataRepService;

	public void setDataRepService(IDataRepService<Department> dataRepService) {
		this.dataRepService = dataRepService;
	}

	@ManagedProperty(value = "#{DepartmentValidator}")
	protected IDataValidator<Department> deptValidator;

	public void setDeptValidator(IDataValidator<Department> deptValidator) {
		this.deptValidator = deptValidator;
	}

	private boolean createNew;
	private Department department;

	private List<Department> departmentList;

	@PostConstruct
	public void init() {
		createNewDepartment();
		rebindData();
	}

	public void createNewDepartment() {
		createNew = true;
		department = new Department();
	}

	private void rebindData() {
		departmentList = departmentService.findAllDepartment();
	}

	public void prepareUpdateDepartment(Department department) {
		createNew = false;
		this.department = department;
	}

	public void addNewDepartment() {
		try {
			dataRepService.insert(department);
			addInfoMessage(null, MessageId.INSERT_SUCCESS, department.getdCode());
			departmentList.add(department);
			createNewDepartment();
		} catch (SystemException ex) {

			handleSysException(ex);
		}
	}

	public void updateDepartment() {
		try {
			dataRepService.update(department);
			addInfoMessage(null, MessageId.UPDATE_SUCCESS, department.getdCode());
			createNewDepartment();
			rebindData();
		} catch (SystemException ex) {

			handleSysException(ex);
		}
	}

	public String deleteDepartment(Department department) {
		ValidationResult result = deptValidator.validate(department, true);
		if (result.isVerified()) {
			try {
				dataRepService.delete(department);
				addInfoMessage(null, MessageId.DELETE_SUCCESS, department.getdCode());
				departmentList.remove(department);
				createNewDepartment();
			} catch (SystemException ex) {
				handleSysException(ex);
			}

		} else {
			for (ErrorMessage message : result.getErrorMeesages()) {
				addErrorMessage(message.getId(), message.getErrorcode(), message.getParams());
			}
		}

		return null;
	}

	public Department getDepartment() {
		return department;
	}

	public boolean isCreateNew() {
		return createNew;
	}

	public List<Department> getDepartmentList() {
		return departmentList;
	}

}
