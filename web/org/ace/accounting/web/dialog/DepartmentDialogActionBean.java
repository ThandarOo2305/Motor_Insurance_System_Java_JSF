package org.ace.accounting.web.dialog;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.system.department.Department;
import org.ace.accounting.system.department.service.interfaces.IDepartmentService;
import org.ace.java.web.common.BaseBean;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "DepartmentDialogActionBean")
@ViewScoped
public class DepartmentDialogActionBean extends BaseBean {

	@ManagedProperty(value = "#{DepartmentService}")
	private IDepartmentService departmentService;

	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	
	private List<Department> DepartmentList;

	@PostConstruct
	public void init() {
		DepartmentList = departmentService.findAllDepartment();
	}

	public List<Department> getDepartmentList() {
		return DepartmentList;
	}

	public void selectDepartment(Department department) {
		PrimeFaces.current().dialog().closeDynamic(department);
		/* RequestContext.getCurrentInstance().closeDialog(department); */
	}

}