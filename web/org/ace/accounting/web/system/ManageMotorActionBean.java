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
public class ManageMotorActionBean extends BaseBean{
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
	
	private List<String> selectedAdditionalCovers;
	
	@PostConstruct
	private void init() {
		createNewMotorPolicyInfo();
		createNewVehicleInfo();
		selectedAdditionalCovers = new ArrayList<>();
	}
	
	private void createNewMotorPolicyInfo() {
		motorPolicy = new MotorPolicy();
		addvehicleList = new ArrayList<>();
	}
	
	private void createNewVehicleInfo() {
		vehicle = new MotorPolicyVehicleLink();	
		
	}  
	
	public void addVehicle() {
        addvehicleList.add(vehicle);
        vehicle = new MotorPolicyVehicleLink();
    }
	
	public void addNewVehicleInfo() {
		vehicle.setActsOfGod(selectedAdditionalCovers.contains("actsOfGod"));
		vehicle.setNilExcess(selectedAdditionalCovers.contains("nilExcess"));
		vehicle.setSrcc(selectedAdditionalCovers.contains("srcc"));
		vehicle.setTheft(selectedAdditionalCovers.contains("theft"));
		vehicle.setWarRisk(selectedAdditionalCovers.contains("warRisk"));
		vehicle.setBetterment(selectedAdditionalCovers.contains("betterment"));
		vehicle.setPaAndMt(selectedAdditionalCovers.contains("paAndMt"));
		vehicle.setSunRoof(selectedAdditionalCovers.contains("sunRoof"));
		vehicle.setThirdParty(selectedAdditionalCovers.contains("thirdParty"));
		vehicle.setWindScreen(selectedAdditionalCovers.contains("windScreen"));
		
		
		ValidationResult result = motorPolicyVehicleValidator.validate(vehicle, true);
		if(result.isVerified()) {
			addvehicleList.add(vehicle);
			createNewVehicleInfo();
			selectedAdditionalCovers.clear();
			System.out.print("success in add vehicle to list");
		}		
	}

	public String getAdditionalCoversAsString(MotorPolicyVehicleLink vehicle) {
        List<String> covers = new ArrayList<>();
        if (vehicle.isActsOfGod()) covers.add("Acts Of God");
        if (vehicle.isNilExcess()) covers.add("Nil Excess");
        if (vehicle.isSrcc()) covers.add("SRCC");
        if (vehicle.isTheft()) covers.add("Theft");
        if (vehicle.isWarRisk()) covers.add("War Risk");
        if (vehicle.isBetterment()) covers.add("Betterment");
        if (vehicle.isPaAndMt()) covers.add("PA and MT");
        if (vehicle.isSunRoof()) covers.add("Sun Roof");
        if (vehicle.isThirdParty()) covers.add("Third Party");
        if (vehicle.isWindScreen()) covers.add("Wind Screen");
        return String.join(", ", covers);
    }
	
	public String onFlowProcess(FlowEvent event) {
	    if ("policyInfo".equals(event.getOldStep())) {
	        ValidationResult result = motorPolicyValidator.validate(motorPolicy, true);
	        if (!result.isVerified()) {
	            return event.getOldStep();
	        }
	    }
	    if ("vehicleInfo".equals(event.getOldStep())) {
	        ValidationResult result = motorPolicyVehicleValidator.validate(vehicle, true);
	        if (!result.isVerified()) {
	            return event.getOldStep();
	        }
	    }
	    return event.getNewStep();
	}
	
	private void calculateAndSetPolicyEndDate() {
	    Date startDate = motorPolicy.getPolicyStartDate();
	    int period = motorPolicy.getPeriod();
	    System.out.println("Debug - Start Date: " + startDate + ", Period: " + period);

	    if (startDate != null && period > 0) {
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(startDate);
	        cal.add(Calendar.MONTH, period);
	        
	        cal.add(Calendar.DAY_OF_MONTH, -1);
	        
	        Date endDate = cal.getTime();
	        motorPolicy.setPolicyEndDate(endDate);
	    } else {
	        System.out.println("Invalid start date or period for calculating policy end date.");
	    }
	}
	
	public void submitPolicy() {
	    try {
	        calculateAndSetPolicyEndDate();

	        // Clear any previous vehicle links
	        motorPolicy.getMotorPolicyVehicleLinks().clear();

	        // Add vehicles properly to ensure both sides of relationship are set
	        for (MotorPolicyVehicleLink v : addvehicleList) {
	        	v.setMotorPolicy(motorPolicy);
	            motorPolicy.getMotorPolicyVehicleLinks().add(v);
	        }

	        motorPolicyService.addNewMotorPolicy(motorPolicy);

	        addInfoMessage(null, MessageId.INSERT_SUCCESS, motorPolicy.getPolicyNo());
	        addInfoMessage(null, MessageId.INSERT_SUCCESS, vehicle.getRegistrationNo());

	        createNewMotorPolicyInfo();
	        createNewVehicleInfo();
	        selectedAdditionalCovers.clear();
	    } catch (SystemException ex) {
	        handleSysException(ex);
	    }
	}
	
	private String cancel() {
		this.motorPolicy = new MotorPolicy();
		this.vehicle = new MotorPolicyVehicleLink();
		this.addvehicleList = new ArrayList<>();
		return "index";
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
	
	public List<String> getSelectedAdditionalCovers() {
		return selectedAdditionalCovers;
	}

	public void setSelectedAdditionalCovers(List<String> selectedAdditionalCovers) {
		this.selectedAdditionalCovers = selectedAdditionalCovers;
	}

	public List<MotorPolicyVehicleLink> getAddvehicleList() {
		return addvehicleList;
	}

	public void setAddvehicleList(List<MotorPolicyVehicleLink> addvehicleList) {
		this.addvehicleList = addvehicleList;
	}
	
}