package org.ace.accounting.web.system;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.system.motor.MotorPolicy;
import org.ace.accounting.system.motor.MotorPolicyVehicleLink;
import org.ace.accounting.system.motor.enumTypes.BranchType;
import org.ace.accounting.system.motor.enumTypes.PaymentType;
import org.ace.accounting.system.motor.enumTypes.SaleChannelType;
import org.ace.accounting.system.motor.service.MotorService;

@ManagedBean(name = "ManageMotorActionBean")
@ViewScoped
public class ManageMotorActionBean {
	@ManagedProperty(value = "#{MotorService}")
	private MotorService motorService;
	
	public void setManageMotorActionBean(MotorService motorService) {
		this.motorService = motorService;
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

//	private void addNewMotorPolicyInfo(MotorPolicy motorpolicy) {
//		this.motorPolicy = motorpolicy;
//	}
	
	private void addNewVehicleInfo(MotorPolicyVehicleLink vehicle) {
		addvehicleList.add(vehicle);
		createNewVehicleInfo();
	}
	
	private void submitPolicy() {
//		try {
			//link all vehicle to motorPolicy
//			for(MotorPolicyVehicleLink v : addvehicleList) {
//				v.setMotorPolicies(motorPolicy);
//			}
			//set vehicle List for motorPolicy
//			motorPolicy.setMotorPolicyVehicleLink(addvehicleList);
		
			//for adding motor policy at the submit policy btn
//			//motorService.addpolicy(motorPolicy);
//			addInfoMessage(null, MessageId.UPDATE_SUCCESS, branch.getName());
			
			//for adding vehicle info at the submit policy btn
			//motorService.addvehicle(motorvehicle);
//			addInfoMessage(null, MessageId.UPDATE_SUCCESS, branch.getName());
			
//			createNewMotorPolicyInfo();
//			createNewVehicleInfo();
//			
//		} catch (systemException ex) {
//			handleSysException(ex);
//		}
	}
	private void cancelPolicyInfo() {
		this.motorPolicy = new MotorPolicy();
	}
	private void cancelVehicleInfo() {
		this.addvehicleList = new ArrayList<>();
	}
	private void cancelPreminumInfo() {
		this.addvehicleList = new ArrayList<>();
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

