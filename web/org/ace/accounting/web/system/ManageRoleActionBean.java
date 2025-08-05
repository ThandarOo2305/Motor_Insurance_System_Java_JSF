package org.ace.accounting.web.system;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIInput;
import javax.faces.event.AjaxBehaviorEvent;

import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.role.Role;
import org.ace.accounting.role.service.interfaces.IRoleService;
import org.ace.accounting.system.webPage.WebPage;
import org.ace.accounting.system.webPage.service.interfaces.IWebPageService;
import org.ace.java.component.SystemException;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.ace.java.web.common.BaseBean;

@ManagedBean(name = "ManageRoleActionBean")
@ViewScoped
public class ManageRoleActionBean extends BaseBean {

	@ManagedProperty(value = "#{RoleService}")
	private IRoleService roleService;

	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}

	@ManagedProperty(value = "#{WebPageService}")
	private IWebPageService webPageService;

	public void setWebPageService(IWebPageService webPageService) {
		this.webPageService = webPageService;
	}

	@ManagedProperty(value = "#{DataRepService}")
	private IDataRepService<Role> dataRepService;

	public void setDataRepService(IDataRepService<Role> dataRepService) {
		this.dataRepService = dataRepService;
	}

	private boolean createNew;
	private Role role;
	private boolean isAdmin;

	// roleList for role table
	private List<Role> roleList;
	// for role table detail webpages
	private List<WebPage> webpages;
	// webPageList is master data for web page dialog
	private List<WebPage> webPageList;
	// selectedWebPageList is , in prepare update , the web pages that role to
	// be updated currently have
	private List<WebPage> selectedWebPageList;
	// tempSelectedWebPageList is to hold the updated role's new webpages
	private List<WebPage> tempSelectedWebPageList;

	@PostConstruct
	public void init() {
		createNewRole();
		rebindData();
		webPageList = webPageService.findAllWebPage();
	}

	public void createNewRole() {
		createNew = true;
		role = new Role();
		selectedWebPageList = new ArrayList<WebPage>();
		tempSelectedWebPageList = new ArrayList<WebPage>();
	}

	private void rebindData() {
		roleList = roleService.findAllRole();

	}

	public void prepareUpdateRole(Role role) {
		createNew = false;
		this.role = role;
		selectedWebPageList = role.getWebpages();
		tempSelectedWebPageList = role.getWebpages();
	}

	public void addNewRole() {
		try {
			if (!selectedWebPageList.isEmpty()) {
				dataRepService.insert(role);
				addInfoMessage(null, MessageId.INSERT_SUCCESS, role.getName());
				createNewRole();
				rebindData();
			} else {
				addErrorMessage("roleEntryForm:selectWebPageListLink", UIInput.REQUIRED_MESSAGE_ID);
			}
		} catch (SystemException ex) {
			handleSysException(ex);
		}
	}

	public void updateRole() {
		try {
			if (role.getWebpages().size() > 0) {
				dataRepService.update(role);
				addInfoMessage(null, MessageId.UPDATE_SUCCESS, role.getName());
				createNewRole();
				rebindData();
			} else {
				addErrorMessage("roleEntryForm:selectWebPageListLink", UIInput.REQUIRED_MESSAGE_ID);
			}
		} catch (SystemException ex) {
			handleSysException(ex);
		}
	}

	public String deleteRole(Role role) {
		try {
			dataRepService.delete(role);
			addInfoMessage(null, MessageId.DELETE_SUCCESS, role.getName());
			createNewRole();
			rebindData();
		} catch (SystemException ex) {
			handleSysException(ex);
		}
		return null;
	}

	public void removeWebPage(WebPage webPage) {
		selectedWebPageList.remove(webPage);
	}

	public void handleDialogClose(AjaxBehaviorEvent event) {
		if (tempSelectedWebPageList.size() > 0) {
			selectedWebPageList = tempSelectedWebPageList;
		} else {
			tempSelectedWebPageList = new ArrayList<WebPage>();
		}
	}

	public void webpageDetailClose(AjaxBehaviorEvent event) {
		webpages = null;
	}

	public void addRoleWebPage() {
		this.selectedWebPageList = tempSelectedWebPageList;
		role.addWebpages(selectedWebPageList);
		tempSelectedWebPageList = role.getWebpages();
		selectedWebPageList = role.getWebpages();
	}

	public boolean isCreateNew() {
		return createNew;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public List<WebPage> getselectedWebPageList() {
		selectedWebPageList.sort(new Comparator<WebPage>() {
			@Override
			public int compare(WebPage o1, WebPage o2) {
				return ((Integer) o1.getCustomOrder()).compareTo(o2.getCustomOrder());
			}
		});
		return selectedWebPageList;
	}

	public void setselectedWebPageList(List<WebPage> selectedWebPageList) {
		this.selectedWebPageList = selectedWebPageList;
	}

	public List<WebPage> getWebPageList() {
		return webPageList;
	}

	public List<WebPage> getTempSelectedWebPageList() {
		return tempSelectedWebPageList;
	}

	public void setTempSelectedWebPageList(List<WebPage> tempSelectedWebPageList) {
		this.tempSelectedWebPageList = tempSelectedWebPageList;
	}

	public List<WebPage> getWebpages() {
		return webpages;
	}

	public void setWebpages(List<WebPage> webpages) {
		this.webpages = webpages;
	}
}
