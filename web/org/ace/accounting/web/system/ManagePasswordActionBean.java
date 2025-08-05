package org.ace.accounting.web.system;


import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.setup.service.ISetupService;
import org.ace.accounting.user.User;
import org.ace.java.component.service.PasswordCodecHandler;
import org.ace.java.web.common.BaseBean;
import org.ace.java.web.common.ParamId;

@ManagedBean(name = "ManagePasswordActionBean")
@ViewScoped
public class ManagePasswordActionBean extends BaseBean {

	@ManagedProperty(value = "#{SetupService}")
	private ISetupService setupService;

	public void setSetupService(ISetupService setupService) {
		this.setupService = setupService;
	}	
	

	@ManagedProperty( value = "#{PasswordCodecHandler}")
	private PasswordCodecHandler codecHandler;

	public void setCodecHandler(PasswordCodecHandler codecHandler) {
		this.codecHandler = codecHandler;
	}
	
	private String editpassword;
	private Date backDate;
	private Date beforeBudgetSDate;
	private Date todayDate;

	
	@PostConstruct
	public void init() {
		todayDate = new Date();
		beforeBudgetSDate = BusinessUtil.getBudgetStartDate();
		backDate = DateUtils.formatStringToDate(setupService.findSetupValueByVariable(BusinessUtil.BACKDATE));
		this.setEditpassword(codecHandler.decode(setupService.findSetupValueByVariable(BusinessUtil.EDIT_PASSWORD)));
		//editpassword = codecHandler.decode(setupService.findSetupValueByVariable(BusinessUtil.EDIT_PASSWORD));
	}



	
	public void updatePassword() {
		String variable=codecHandler.encode(this.editpassword);
		backDate = this.getBackDate();
		setupService.insert(variable,backDate);
		addInfoMessage(null, MessageId.UPDATE_SUCCESS, "Setup Data");
	}

	public String getEditpassword() {
		return editpassword;
	}

	public void setEditpassword(String editpassword) {
		this.editpassword = editpassword;
	}
	
	/*
	 * public Date getBackDate() { return backDate; }
	 * 
	 * public void setBackDate(Date backDate) { this.backDate = backDate; }
	 */

	/*
	 * public Date getBeforeBudgetSDate() { return beforeBudgetSDate; }
	 * 
	 * public Date getTodayDate() { return todayDate; }
	 */


	public Date getBackDate() {
		return backDate;
	}

	public void setBackDate(Date backDate) {
		this.backDate = backDate;
	}
	
	public Date getBeforeBudgetSDate() {
		return beforeBudgetSDate;
	}
	
	public Date getTodayDate() {
		return todayDate;
	}
	

	


}
