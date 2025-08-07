package org.ace.accounting.web.system;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.common.validation.ValidationResult;
import org.ace.accounting.system.motor.MotorPolicy;
import org.ace.accounting.system.motor.MotorPolicyVehicleLink;
import org.ace.accounting.system.motor.enumTypes.BranchType;
import org.ace.accounting.system.motor.enumTypes.PaymentType;
import org.ace.accounting.system.motor.enumTypes.SaleChannelType;
import org.ace.accounting.system.motor.service.interfaces.IMotorPolicyService;
import org.ace.accounting.system.motor.service.interfaces.IMotorPolicyVehicleLinkService;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.primefaces.event.FlowEvent;

@ManagedBean(name = "ManageMotorActionBean")
@ViewScoped
public class ManageMotorActionBean extends BaseBean {
	@ManagedProperty(value = "#{MotorPolicyService}")
	private IMotorPolicyService motorPolicyService;

	public void setMotorPolicyService(IMotorPolicyService motorPolicyService) {
		this.motorPolicyService = motorPolicyService;
	}

	@ManagedProperty(value = "#{MotorPolicyVehicleLinkService}")
	private IMotorPolicyVehicleLinkService motorVehicleLinkService;

	public void setMotorVehicleLinkService(IMotorPolicyVehicleLinkService motorVehicleLinkService) {
		this.motorVehicleLinkService = motorVehicleLinkService;
	}

	@ManagedProperty(value = "#{MotorPolicyValidator}")
	private IDataValidator<MotorPolicy> motorPolicyValidator;

	public void setMotorPolicyValidator(IDataValidator<MotorPolicy> motorPolicyValidator) {
		this.motorPolicyValidator = motorPolicyValidator;
	}

	@ManagedProperty(value = "#{MotorPolicyVehicleValidator}")
	private IDataValidator<MotorPolicyVehicleLink> motorPolicyVehicleValidator;

	public void setMotorPolicyVehicleValidator(IDataValidator<MotorPolicyVehicleLink> motorPolicyVehicleValidator) {
		this.motorPolicyVehicleValidator = motorPolicyVehicleValidator;
	}

	private MotorPolicy motorPolicy;
	private MotorPolicyVehicleLink vehicle;
	private List<MotorPolicyVehicleLink> addvehicleList;

	@PostConstruct
	private void init() {
		createNewMotorPolicyInfo();
		createNewVehicleInfo();
	}

	private void createNewMotorPolicyInfo() {
		motorPolicy = new MotorPolicy();
		addvehicleList = new ArrayList<>();
	}

	private void createNewVehicleInfo() {
		vehicle = new MotorPolicyVehicleLink();

	}

	// i dont think this method is need but next btn method is needs in MotorPolicy
	// info page
//	private void addNewMotorPolicyInfo(MotorPolicy motorpolicy) {
//		this.motorPolicy = motorpolicy;
//	}

	// method for Add btn in Vehicle Info page
	private void addNewVehicleInfo(MotorPolicyVehicleLink vehicle) {
		ValidationResult result = motorPolicyVehicleValidator.validate(vehicle, true);
		if (result.isVerified()) {
			addvehicleList.add(vehicle);
			createNewVehicleInfo();
			System.out.print("success in add vehicle to list");
		}
	}

	public String onFlowProcess(FlowEvent event) {
		// always allow forward/backward navigation
		System.out.println("Moving from " + event.getOldStep() + " to " + event.getNewStep());
		return event.getNewStep();
	}

	// calculating method for policy end date
	private void calculateAndSetPolicyEndDate() {
		Date startDate = motorPolicy.getPolicyStartDate();
		int period = motorPolicy.getPeriod();

		if (startDate != null && period > 0) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.add(Calendar.MONTH, period);// add start date and period

			Date endDate = cal.getTime();
			motorPolicy.setPolicyEndDate(endDate);
		} else {
			System.out.println("Invalid start date or period for calculating policy end date.");
		}
	}

	// for submitPolicy btn in Premium Info page
	private void submitPolicy() {
		try {
			calculateAndSetPolicyEndDate();

			// link all vehicle to motorPolicy
			for (MotorPolicyVehicleLink v : addvehicleList) {
				v.setMotorPolicy(motorPolicy);
			}
			// set vehicle List for motorPolicy
			motorPolicy.setMotorPolicyVehicleLinks(addvehicleList);

			// for adding motor policy at the submit policy btn
			motorPolicyService.addNewMotorPolicy(motorPolicy);
			addInfoMessage(null, MessageId.UPDATE_SUCCESS, motorPolicy.getPolicyNo());

			// for adding vehicle info(s) at the submit policy btn
			for (MotorPolicyVehicleLink ve : addvehicleList) {
				motorVehicleLinkService.addNewMotorPolicyVehicleLink(ve);
			}
			addInfoMessage(null, MessageId.UPDATE_SUCCESS, vehicle.getRegistrationNo());
			createNewMotorPolicyInfo();
			createNewVehicleInfo();
		} catch (SystemException ex) {
			handleSysException(ex);
		}
	}

//	//for cancel btn in PolicyInfo
//	private void cancelPolicyInfo() {
//		this.motorPolicy = new MotorPolicy();
//	}
//	
//	//for cancel btn in VehicleInfo
//	private void cancelVehicleInfo() {
//		this.vehicle = new MotorPolicyVehicleLink();
//		this.addvehicleList = new ArrayList<>();
//	}
//	//for cancel btn in PreminumInfo
//	private void cancelPreminumInfo() {
//		cancelVehicleInfo();
//		cancelPolicyInfo();
//	}

	// for cancel btn all page
	// if cancel method works and will clear all data and will go to home.xhtml
	private String cancel() {
		this.motorPolicy = new MotorPolicy();
		this.vehicle = new MotorPolicyVehicleLink();
		this.addvehicleList = new ArrayList<>();
		return "home";
	}

	public BranchType[] getBranchType() {
		return BranchType.values();
	}

	public CurrencyType[] getCurrencyType() {
		return CurrencyType.values();
	}

	public PaymentType[] getPaymentType() {
		return PaymentType.values();
	}

	public SaleChannelType[] getSaleChannelType() {
		return SaleChannelType.values();
	}

	public MotorPolicy getMotorPolicy() {
		return motorPolicy;
	}

	public void setMotorPolicy(MotorPolicy motorPolicy) {
		this.motorPolicy = motorPolicy;
	}

	public List<MotorPolicyVehicleLink> getVehicleLink() {
		return addvehicleList;
	}

	public void setVehicleLink(List<MotorPolicyVehicleLink> vehicleLink) {
		this.addvehicleList = vehicleLink;
	}

	public MotorPolicyVehicleLink getVehicle() {
		return vehicle;
	}

	public void setVehicle(MotorPolicyVehicleLink vehicle) {
		this.vehicle = vehicle;
	}

}