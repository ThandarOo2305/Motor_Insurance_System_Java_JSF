package org.ace.accounting.web.posting;

import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.SystemConstants;
import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.YPDto;
import org.ace.accounting.posting.service.interfaces.IYearlyPostingService;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.setup.service.ISetupService;
import org.ace.accounting.system.systempost.service.interfaces.ISystemPostService;
import org.ace.accounting.user.User;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.ace.java.web.common.DialogId;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "YearlyPostingActionBean")
@ViewScoped
public class YearlyPostingActionBean extends BaseBean {

	@ManagedProperty(value = "#{SystemPostService}")
	private ISystemPostService sysPostService;

	public void setSysPostService(ISystemPostService sysPostService) {
		this.sysPostService = sysPostService;
	}

	@ManagedProperty(value = "#{YearlyPostingService}")
	private IYearlyPostingService yearlyPostingService;

	public void setYearlyPostingService(IYearlyPostingService yearlyPostingService) {
		this.yearlyPostingService = yearlyPostingService;
	}

	@ManagedProperty(value = "#{UserProcessService}")
	private IUserProcessService userProcessService;

	public void setUserProcessService(IUserProcessService userProcessService) {
		this.userProcessService = userProcessService;
	}

	@ManagedProperty(value = "#{SetupService}")
	private ISetupService setupService;

	public void setSetupService(ISetupService setupService) {
		this.setupService = setupService;
	}

	private Date postingDate;

	private YPDto tDto;

	private YPDto plDto;

	private double progress;

	private boolean posting = false;

	private boolean postingAccess = false;

	private Branch branch;

	private boolean isYearlyClosing;

	private boolean isPLAcDisabled;

	@PostConstruct
	private void init() {
		postingDate = DateUtils.formatStringToDate(setupService.findSetupValueByVariable(BusinessUtil.BUDGETEDATE));
		User user = userProcessService.getLoginUser();
		isYearlyClosing = true;
		isPLAcDisabled = false;
		branch = user.getBranch();
		if (user.isAdmin()) {
			postingAccess = true;
		} else {
			postingAccess = false;
		}
	}

	public void post() {

		if (isYearlyClosing) {
			yearlyPostingService.handleYearlyClosing(plDto, branch);
			addInfoMessage(null, MessageId.HO_POSTING_SUCCEEDED);
		} else {

			try {
				posting = true;
				MyThread progressCount = new MyThread();
				Thread progressThread = new Thread(progressCount);
				progressThread.start();

				User user = userProcessService.getLoginUser();

				// Accumulated surplus or deficit code closing for HO
				// yearlyPostingService.createHOClosingForManulProcess(user.getBranch(),
				// branch, plDto);


				yearlyPostingService.handleYearlyPosting(postingDate, plDto, branch);

				progressCount.stopThread();

				progress = 100;
				Runnable methodEndInOneSec = new Runnable() {
					public void run() {
						for (int i = 1; i <= 1; i++) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				};
				new Thread(methodEndInOneSec).run();
			} catch (SystemException e) {
				progress = 0;
				posting = false;
				handleSysException(e);
			}
		}
		posting = false;
	}

	public void onProgressComplete() {
		progress = 0;
		addInfoMessage(null, MessageId.YP_POSTING_SUCCEEDED);
	}

	public void checkPostDate() {
		Date budgetEndDate = DateUtils.formatStringToDate(setupService.findSetupValueByVariable(BusinessUtil.BUDGETEDATE));
		// Date todayDate = DateUtils.formatStringToDate("01-04-2016");
		Date todayDate = new Date();
		if (sysPostService.findbyPostingName(SystemConstants.YEARPOST).getPostingDate() == null) {
			addErrorMessage(null, MessageId.YP_POSTINGDATE_ERROR);
		} else if (budgetEndDate == null) {
			addErrorMessage(null, MessageId.YP_BUDGETEDATE_ERROR);
		} else if (budgetEndDate.after(todayDate)) {
			addErrorMessage(null, MessageId.YP_POSTING_NOTREQUIRED);
			// } else if (DateUtils.getYearFromDate(budgetEndDate) >
			// DateUtils.getYearFromDate(todayDate)) {
			// // if year of budgetEndDate is after current year
			// return false;
			// } else {
			// if (DateUtils.getYearFromDate(budgetEndDate) ==
			// DateUtils.getYearFromDate(todayDate)) {
			// // if year of budgetEndDate and current year are the same
			// if (DateUtils.getMonthFromDate(budgetEndDate) >
			// DateUtils.getMonthFromDate(todayDate)) {
			// // if month of budgetEndDate is after current month
			// return false;
			// } else {
			// if (DateUtils.getDayFromDate(budgetEndDate) >
			// DateUtils.getDayFromDate(todayDate)) {
			// // if day of the budgetEndDate after current day
			// return false;
			// }
			// }
			// }
			// // so... what if the current month is already after the budget
			// end
			// // month and day is still before?
			// // e.g. budgetEndDate = 31-03-2015 , current date = 05-04-2015
		} else {
			PrimeFaces.current().executeScript("PF('confirmationDialog').show();");
			/*
			 * RequestContext.getCurrentInstance().execute(
			 * "PF('confirmationDialog').show();");
			 */
		}
	}

	public void selectDto() {
		Map<String, Object> map = getDialogOptions();
		map.put("width", 700);
		map.put("height", 500);
		PrimeFaces.current().dialog().openDynamic(DialogId.YEARLY_POSTING_COA_DIALOG, map, null);
		/*
		 * RequestContext.getCurrentInstance().openDialog(DialogId.
		 * YEARLY_POSTING_COA_DIALOG, map, null);
		 */
	}

	public void returnTDto(SelectEvent event) {
		YPDto dto = (YPDto) event.getObject();
		tDto = dto;
	}

	public void returnPLdto(SelectEvent event) {
		YPDto dto = (YPDto) event.getObject();
		plDto = dto;
	}

	public YPDto getPlDto() {
		return plDto;
	}

	public YPDto gettDto() {
		return tDto;
	}

	public void settDto(YPDto tDto) {
		this.tDto = tDto;
	}

	public void setPlDto(YPDto plDto) {
		this.plDto = plDto;
	}

	public Date getPostingDate() {
		return postingDate;
	}

	public double getProgress() {
		return progress;
	}

	private class MyThread extends Thread {
		volatile boolean finished = false;

		public void run() {
			for (int i = 1; i <= 60; i++) {
				while (!finished) {
					progress++;
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		public void stopThread() {
			finished = true;
		}
	}

	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}

	public boolean isPostingAccess() {
		return postingAccess;
	}

	public boolean isPosting() {
		// TODO remove later
		return posting;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public boolean isYearlyClosing() {
		return isYearlyClosing;
	}

	public void setYearlyClosing(boolean isYearlyClosing) {
		this.isYearlyClosing = isYearlyClosing;
	}

	public boolean isPLAcDisabled() {
		return isYearlyClosing ? false : true;
	}

	public void setPLAcDisabled(boolean isPLAcDisabled) {
		this.isPLAcDisabled = isPLAcDisabled;
	}

}
