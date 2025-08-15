package org.ace.accounting.web.system;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.validation.ErrorMessage;
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
	private List<MotorPolicyVehicleLink> addVehicleList;
	private double privateRate = 1.072;
	private double commercialRate = 1.734;
	private double fleetDiscountRate = 10;
	
	private List<String> selectedAdditionalCovers;
	
	@PostConstruct
	private void init() {
		createNewMotorPolicyInfo();
		createNewVehicleInfo();
		selectedAdditionalCovers = new ArrayList<>();
	}
	
	private void createNewMotorPolicyInfo() {
		motorPolicy = new MotorPolicy();
		addVehicleList = new ArrayList<>();
	}
	
	private void createNewVehicleInfo() {
		vehicle = new MotorPolicyVehicleLink();	
		
	}  
	
	public void addVehicle() {
        addVehicleList.add(vehicle);
        vehicle = new MotorPolicyVehicleLink();
    }
	
	public void addNewVehicleInfo() {
	    vehicle.setActsOfGod(selectedAdditionalCovers.contains("ActsOfGod"));
	    vehicle.setNilExcess(selectedAdditionalCovers.contains("NilExcess"));
	    vehicle.setSrcc(selectedAdditionalCovers.contains("SRCC"));
	    vehicle.setTheft(selectedAdditionalCovers.contains("Theft"));
	    vehicle.setWarRisk(selectedAdditionalCovers.contains("WarRisk"));
	    vehicle.setBetterment(selectedAdditionalCovers.contains("Betterment"));
	    vehicle.setPaAndMt(selectedAdditionalCovers.contains("PA_MT"));
	    vehicle.setSunRoof(selectedAdditionalCovers.contains("SunRoof"));
	    vehicle.setThirdParty(selectedAdditionalCovers.contains("ThirdParty"));
	    vehicle.setWindScreen(selectedAdditionalCovers.contains("WindScreen"));

	    ValidationResult result = motorPolicyVehicleValidator.validate(vehicle, true);
	    if(result.isVerified()) {
	        addVehicleList.add(vehicle);
	        createNewVehicleInfo();
	        selectedAdditionalCovers.clear();
	        
	        applyFleetDiscount();
	        updatePremiumValuesToVehicles();
	        addInfoMessage("vehicle is successfully added!");
	        System.out.println("success in add vehicle to list");
	    } else {
	        System.out.println("Validation failed for vehicle.");
	        for(ErrorMessage e: result.getErrorMeesages()) {
	        	addErrorMessage(null , e.getErrorcode(), e.getParams());
	        }
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
	        if (addVehicleList == null || addVehicleList.isEmpty()) {
	            ValidationResult result = motorPolicyVehicleValidator.validate(vehicle, true);
	            if (!result.isVerified()) {
	                return event.getOldStep();
	            }
	        }
	    }

	    return event.getNewStep();
	}
	
	public double oneYearBasicPremiumCalculation(MotorPolicyVehicleLink v) {
		double rate = v.getProductType().equals("Private") ? privateRate : commercialRate;
		
		double oneYearBasicPremium = v.getSumInsured() * (rate / 100);
		
		return oneYearBasicPremium;
	}
	
	public double basicTermPremiumCalculation(MotorPolicyVehicleLink v, PaymentType paymentType) {
		double oneYearBasicPremium = oneYearBasicPremiumCalculation(v);
		
		if(paymentType != null) {
			switch (paymentType) {
			
			case SEMI_ANNUAL:
				return oneYearBasicPremium / 2;
			
			case QUARTER:
				return oneYearBasicPremium / 4;
			
			case MONTHLY:
				return oneYearBasicPremium / 12;
				
			default:
				return oneYearBasicPremium;
			}
		}
		return oneYearBasicPremium;
	}
	
	public double oneYearAddonPremiun(MotorPolicyVehicleLink v) {
		double oneYearAddOnPremium = 0.0;
		
		if (v.isActsOfGod()) oneYearAddOnPremium += v.getSumInsured() * (0.10 / 100);
	    if (v.isTheft()) oneYearAddOnPremium += v.getSumInsured() * (0.05 / 100);
	    if (v.isWarRisk()) oneYearAddOnPremium += v.getSumInsured() * (0.05 / 100);
	    if (v.isNilExcess()) oneYearAddOnPremium += v.getSumInsured() * (0.02 / 100);
	    if (v.isSrcc()) oneYearAddOnPremium += v.getSumInsured() * (0.03 / 100);
	    if (v.isBetterment()) oneYearAddOnPremium += v.getSumInsured() * (0.03 / 100);
	    if (v.isPaAndMt()) oneYearAddOnPremium += v.getSumInsured() * (0.02 / 100);
 	    if (v.isSunRoof()) oneYearAddOnPremium += 5000;
	    if (v.isThirdParty()) oneYearAddOnPremium += 20000;
	    if (v.isWindScreen()) oneYearAddOnPremium += 5000;
		
		return oneYearAddOnPremium;
	}
	
	public double basicTermAddOnPremium(MotorPolicyVehicleLink v, PaymentType paymentType) {
		double oneYearAddOnPremium = oneYearAddonPremiun(v);
		
		if(paymentType != null) {
			switch (paymentType) {
			case SEMI_ANNUAL:
				return oneYearAddOnPremium / 2;

			case QUARTER:
				return oneYearAddOnPremium / 4;
			
			case MONTHLY:
				return oneYearAddOnPremium / 12;
			default:
				return oneYearAddOnPremium;
			}
		}
		return oneYearAddOnPremium;
	}
	
	public void applyFleetDiscount() {
	    int fleetThreshold = 2;
	    double discountRate = fleetDiscountRate / 100;

	    if (addVehicleList.size() < fleetThreshold) {
	        for (MotorPolicyVehicleLink v : addVehicleList) {
	            v.setFleet(false);
	            v.setFleetDiscount(0);
	        }
	        return;
	    }

	    double totalPremiumBeforeDiscount = 0.0;
	    for (MotorPolicyVehicleLink v : addVehicleList) {
	        double basic = basicTermPremiumCalculation(v, motorPolicy.getPaymentType());
	        double addOn = basicTermAddOnPremium(v, motorPolicy.getPaymentType());
	        totalPremiumBeforeDiscount += (basic + addOn);
	    }

	    double totalDiscountAmount = totalPremiumBeforeDiscount * discountRate;

	    double perVehicleDiscount = totalDiscountAmount / addVehicleList.size();

	    for (MotorPolicyVehicleLink v : addVehicleList) {
	        v.setFleet(true);
	        v.setFleetDiscount(perVehicleDiscount);
	    }
	}

	
	public double totalPremiunCalculation() {
	    double totalPremium = 0.0;

	    for (MotorPolicyVehicleLink v : addVehicleList) {
	        double basic = basicTermPremiumCalculation(v, motorPolicy.getPaymentType());
	        double addOn = basicTermAddOnPremium(v, motorPolicy.getPaymentType());

	        double vehiclePremium = basic + addOn;

	        if (v.isFleet()) {
	            vehiclePremium -= v.getFleetDiscount();
	        }

	        totalPremium += vehiclePremium;
	    }

	    return totalPremium;
	}
	
	private void updatePremiumValuesToVehicles() {
	    for (MotorPolicyVehicleLink v : addVehicleList) {
	        double oneYearBasic = oneYearBasicPremiumCalculation(v);
	        double oneYearAddOn = oneYearAddonPremiun(v);
	        double basicTerm = basicTermPremiumCalculation(v, motorPolicy.getPaymentType());
	        double addOnTerm = basicTermAddOnPremium(v, motorPolicy.getPaymentType());
	        boolean isFleetVehicle = v.isFleet();
	        double fleetDisc = isFleetVehicle ? v.getFleetDiscount() : 0.0;
	        double total = basicTerm + addOnTerm - fleetDisc;

	        v.setOneYearBasicPremium(oneYearBasic);
	        v.setOneYearAddonPremium(oneYearAddOn);
	        v.setBasicTermPremium(basicTerm);
	        v.setAddOnTermPremium(addOnTerm);
	        v.setTotalPremium(total);
	    }
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
	
	public void checkVehicleTable() {
	    if (addVehicleList == null || addVehicleList.isEmpty()) {
	        FacesContext.getCurrentInstance().validationFailed();
	        FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                    "Please add at least one vehicle before  proceeding.", null));
	    }
	}
	
	public String submitPolicy() {
	    try {
	        calculateAndSetPolicyEndDate();
	        
	        applyFleetDiscount();
	        
	        updatePremiumValuesToVehicles();

	        motorPolicy.getMotorPolicyVehicleLinks().clear();

	        for (MotorPolicyVehicleLink v : addVehicleList) {
	        	motorPolicy.addVehicleLink(v);
	        }
	        
	        motorPolicy.setProposalNo(motorPolicyService.generateProposalNo());

	        motorPolicyService.addNewMotorPolicy(motorPolicy);

	        addInfoMessage(null, MessageId.INSERT_SUCCESS, motorPolicy.getPolicyNo());
	        addInfoMessage(null, MessageId.INSERT_SUCCESS, vehicle.getRegistrationNo());

	        createNewMotorPolicyInfo();
	        createNewVehicleInfo();
	        selectedAdditionalCovers.clear();
	    } catch (SystemException ex) {
	        handleSysException(ex);
	    }
	    
	    return "/ui/system/home.xhtml?faces-redirect=true";
	}
	
	public String cancel() {
		return "/ui/system/home.xhtml?faces-redirect=true";
	}
	
	/*
	 * public void editVehicle(MotorPolicyVehicleLink selectedVehicle) {
	 * this.vehicle = selectedVehicle; if(vehicle.getCovers() != null &&
	 * !vehicle.getCovers().isEmpty()) { this.selectedAdditionalCovers =
	 * Arrays.asList(vehicle.getCovers().split(",")); } else {
	 * this.selectedAdditionalCovers = new ArrayList<>(); } }
	 */

	public void deleteVehicle(MotorPolicyVehicleLink selectedVehicle) {
	    addVehicleList.remove(selectedVehicle);
	    
	    applyFleetDiscount();
	    updatePremiumValuesToVehicles();
	}
	
	public void saveEditedVehicle() {
	    vehicle.setActsOfGod(selectedAdditionalCovers.contains("ActsOfGod"));
	    vehicle.setNilExcess(selectedAdditionalCovers.contains("NilExcess"));
	    vehicle.setSrcc(selectedAdditionalCovers.contains("SRCC"));
	    vehicle.setTheft(selectedAdditionalCovers.contains("Theft"));
	    vehicle.setWarRisk(selectedAdditionalCovers.contains("WarRisk"));
	    vehicle.setBetterment(selectedAdditionalCovers.contains("Betterment"));
	    vehicle.setPaAndMt(selectedAdditionalCovers.contains("PA_MT"));
	    vehicle.setSunRoof(selectedAdditionalCovers.contains("SunRoof"));
	    vehicle.setThirdParty(selectedAdditionalCovers.contains("ThirdParty"));
	    vehicle.setWindScreen(selectedAdditionalCovers.contains("WindScreen"));

	    applyFleetDiscount();
	    updatePremiumValuesToVehicles();

	    selectedAdditionalCovers.clear();

	    addInfoMessage(null, "Vehicle updated successfully");
	}
	
	public int getCurrentYear() {
	    return LocalDate.now().getYear();
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

	public List<MotorPolicyVehicleLink> getAddVehicleList() {
		return addVehicleList;
	}

	public void setAddVehicleList(List<MotorPolicyVehicleLink> addVehicleList) {
		this.addVehicleList = addVehicleList;
	}
	
	public double getOneYearBasicPremium(MotorPolicyVehicleLink v) {
	    return oneYearBasicPremiumCalculation(v);
	}

	public double getOneYearAddOnPremium(MotorPolicyVehicleLink v) {
	    return oneYearAddonPremiun(v);
	}

	public double getBasicTermPremium(MotorPolicyVehicleLink v) {
	    return basicTermPremiumCalculation(v, motorPolicy.getPaymentType());
	}

	public double getAddOnTermPremium(MotorPolicyVehicleLink v) {
	    return basicTermAddOnPremium(v, motorPolicy.getPaymentType());
	}

	public double getFleetDiscount(MotorPolicyVehicleLink v) {
	    return v.isFleet() ? v.getFleetDiscount() : 0.0;
	}

	public double getTotalPremiumPerVehicle(MotorPolicyVehicleLink v) {
	    double total = getBasicTermPremium(v) + getAddOnTermPremium(v) - getFleetDiscount(v);
	    return total > 0 ? total : 0;
	}

	// Total sums for footer
	public double getTotalSumInsured() {
	    return addVehicleList.stream()
	        .mapToDouble(MotorPolicyVehicleLink::getSumInsured)
	        .sum();
	}

	public double getTotalOneYearBasicPremium() {
	    return addVehicleList.stream()
	        .mapToDouble(this::getOneYearBasicPremium)
	        .sum();
	}

	public double getTotalOneYearAddOnPremium() {
	    return addVehicleList.stream()
	        .mapToDouble(this::getOneYearAddOnPremium)
	        .sum();
	}

	public double getTotalBasicTermPremium() {
	    return addVehicleList.stream()
	        .mapToDouble(this::getBasicTermPremium)
	        .sum();
	}

	public double getTotalAddOnTermPremium() {
	    return addVehicleList.stream()
	        .mapToDouble(this::getAddOnTermPremium)
	        .sum();
	}

	public double getTotalFleetDiscount() {
	    return addVehicleList.stream()
	        .mapToDouble(this::getFleetDiscount)
	        .sum();
	}

	public double getTotalTotalPremium() {
	    return addVehicleList.stream()
	        .mapToDouble(this::getTotalPremiumPerVehicle)
	        .sum();
	}	
}