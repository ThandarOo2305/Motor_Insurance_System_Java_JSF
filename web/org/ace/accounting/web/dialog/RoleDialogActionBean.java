package org.ace.accounting.web.dialog;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.role.Role;
import org.ace.accounting.role.service.interfaces.IRoleService;
import org.ace.java.web.common.BaseBean;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "RoleDialogActionBean")
@ViewScoped
public class RoleDialogActionBean extends BaseBean {

	@ManagedProperty(value = "#{RoleService}")
	private IRoleService roleService;

	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}
	
	private List<Role> roleList;

	@PostConstruct
	public void init() {
		roleList = roleService.findAllRole();
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void selectRole(Role role) {
		PrimeFaces.current().executeScript("PF('accLedgerListingPdfDialog').hide();");
		/* RequestContext.getCurrentInstance().closeDialog(role); */
	}
}