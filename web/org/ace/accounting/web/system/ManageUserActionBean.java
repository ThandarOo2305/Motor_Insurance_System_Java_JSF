package org.ace.accounting.web.system;

import java.util.ArrayList;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIInput;
import javax.faces.event.AjaxBehaviorEvent;

import org.ace.accounting.common.ContactInfo;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.role.Role;
import org.ace.accounting.role.service.interfaces.IRoleService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.user.User;
import org.ace.accounting.user.service.interfaces.IUserService;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "ManageUserActionBean")
@ViewScoped
public class ManageUserActionBean extends BaseBean {

	@ManagedProperty(value = "#{UserService}")
	private IUserService userService;

	@ManagedProperty(value = "#{UserProcessService}")
	private IUserProcessService userProcessService;

	public void setUserProcessService(IUserProcessService userProcessService) {
		this.userProcessService = userProcessService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@ManagedProperty(value = "#{RoleService}")
	private IRoleService roleService;

	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}

	private boolean createNew;
	private User user;

	// master data form user table
	private List<User> userList;
	// for user table role detail
	private List<Role> roles;
	// roleList is master data for role dialog
	private List<Role> roleList;
	// selectedRoleList is , in prepare update , the roles that user to
	// be updated currently have
	private List<Role> selectedRoleList;
	// tempSelectedRoleList is to hold the updated user's new roles
	private List<Role> tempSelectedRoleList;

	@PostConstruct
	public void init() {
		createNewUser();
		roleList = roleService.findAllRole();
		rebindData();
	}

	public void createNewUser() {
		createNew = true;
		user = new User();
		selectedRoleList = new ArrayList<Role>();
		tempSelectedRoleList = new ArrayList<Role>();
	}

	private void rebindData() {
		userList = userService.findAllUser();
	}

	public void prepareUpdateUser(User user) {
		createNew = false;
		this.user = user;
		if (user.getContactInfo() == null) {
			user.setContactInfo(new ContactInfo());
		}
		this.user.setPassword(userService.getDecodedPassword(user));
		selectedRoleList = user.getRoles();
		tempSelectedRoleList = user.getRoles();
	}

	public void addNewUser() {
		try {
			if (!selectedRoleList.isEmpty()) {
				userService.addNewUser(user);
				addInfoMessage(null, MessageId.INSERT_SUCCESS, user.getUserCode());
				createNewUser();
				rebindData();
			} else {
				addErrorMessage("userEntryForm:selectRoleListLink", UIInput.REQUIRED_MESSAGE_ID);
			}
		} catch (SystemException ex) {
			handleSysException(ex);
		}
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setCreateNew(boolean createNew) {
		this.createNew = createNew;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public void updateUser() {
		try {
			userService.updateUser(user);
			addInfoMessage(null, MessageId.UPDATE_SUCCESS, user.getUserCode());
			createNewUser();
			rebindData();
		} catch (SystemException ex) {
			handleSysException(ex);
		}
	}

	public String deleteUser(User user) {
		try {
			userService.deleteUser(user);
			addInfoMessage(null, MessageId.DELETE_SUCCESS, user.getUserCode());
			createNewUser();
			rebindData();
		} catch (SystemException ex) {
			handleSysException(ex);
		}
		return null;
	}

	public boolean isCreateNew() {
		return createNew;
	}

	public List<User> getUserList() {
		return userList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void returnBranch(SelectEvent event) {
		Branch branch = (Branch) event.getObject();
		user.setBranch(branch);
	}

	public void removeWebPage(Role webPage) {
		selectedRoleList.remove(webPage);
	}

	public void handleDialogClose(AjaxBehaviorEvent event) {

		if (tempSelectedRoleList.size() > 0) {
			selectedRoleList = tempSelectedRoleList;
		} else {
			tempSelectedRoleList = new ArrayList<Role>();
		}
	}

	public void roleDetailClose(AjaxBehaviorEvent event) {
		roles = null;
	}

	public void addRoleWebPage() {
		this.selectedRoleList = tempSelectedRoleList;
		user.addRoles(selectedRoleList);
		tempSelectedRoleList = user.getRoles();
		selectedRoleList = user.getRoles();
	}

	public List<Role> getSelectedRoleList() {
		return selectedRoleList;
	}

	public void setSelectedRoleList(List<Role> selectedRoleList) {
		this.selectedRoleList = selectedRoleList;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public List<Role> getTempSelectedRoleList() {
		return tempSelectedRoleList;
	}

	public void setTempSelectedRoleList(List<Role> tempSelectedRoleList) {
		this.tempSelectedRoleList = tempSelectedRoleList;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Role> getRoles() {
		return roles;
	}

}
