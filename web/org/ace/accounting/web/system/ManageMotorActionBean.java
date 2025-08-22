package org.ace.accounting.web.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.PropertiesManager;
import org.ace.accounting.common.validation.ErrorMessage;
import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.common.validation.ValidationResult;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.motor.MotorPolicy;
import org.ace.accounting.system.motor.MotorPolicyVehicleLink;
import org.ace.accounting.system.motor.enumTypes.PaymentType;
import org.ace.accounting.system.motor.enumTypes.ProductType;
import org.ace.accounting.system.motor.enumTypes.SaleChannelType;
import org.ace.accounting.system.motor.service.interfaces.IMotorPolicyService;
import org.ace.accounting.system.motor.service.interfaces.IMotorPolicyVehicleLinkService;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.apache.commons.io.FileUtils;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

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

	@ManagedProperty(value = "#{PropertiesManager}")
	private PropertiesManager propertiesManager;

	public void setPropertiesManager(PropertiesManager propertiesManager) {
		this.propertiesManager = propertiesManager;
	}

	private MotorPolicy motorPolicy;
	private MotorPolicyVehicleLink vehicle;
	private List<MotorPolicyVehicleLink> addVehicleList;
	private double privateRate = 1.072;
	private double commercialRate = 1.734;
	private double fleetDiscountRate = 10;
	private boolean sameVehicleNo;
	private List<String> selectedAdditionalCovers;
	private MotorPolicyVehicleLink editingVehicle;
	private boolean editMode = false;
	private MotorPolicyVehicleLink sameVehicle;
	private String currentStep = "PolicyInfo";
	private boolean submitted;
	
	private final String reportName = "motorPolicyLetter";
	private final String fileName = "MotorPolicyLetter";
	private final String pdfDirPath = "/pdf-report/" + reportName + "/" + System.currentTimeMillis() + "/";
	private final String dirPath = getWebRootPath() + pdfDirPath;

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

	public void createNewVehicleInfo() {
		System.out.println("createNewVehicleInfo: Initializing new vehicle");
		vehicle = new MotorPolicyVehicleLink();
		vehicle.setRegistrationNo(null);
		vehicle.setProductType(null);
		vehicle.setModel(null);
		vehicle.setEngineNo(null);
		vehicle.setChassisNo(null);
		vehicle.setYearOfManufacture(0);
		vehicle.setTypeOfBody(null);
		vehicle.setCubicCapacity(0);
		vehicle.setSeating(0);
		vehicle.setSumInsured(0);
		vehicle.setEstimatePresentSumInsurance(0);
		vehicle.setClaimCount(0);
		vehicle.setNcbCount(0);
		vehicle.setNcbAmount(0);
		vehicle.setHirePurchaseCompany(null);
		vehicle.setManufacture(null);
		vehicle.setActsOfGod(false);
		vehicle.setNilExcess(false);
		vehicle.setSrcc(false);
		vehicle.setTheft(false);
		vehicle.setWarRisk(false);
		vehicle.setBetterment(false);
		vehicle.setPaAndMt(false);
		vehicle.setSunRoof(false);
		vehicle.setThirdParty(false);
		vehicle.setWindScreen(false);
		System.out.println("createNewVehicleInfo: New vehicle initialized");
	}

	public void vehicleWithRegistrationNoExist() {
		sameVehicleNo = false;
		sameVehicle = null;

		for (MotorPolicyVehicleLink ve : addVehicleList) {
			if (vehicle.getRegistrationNo().trim().equalsIgnoreCase(ve.getRegistrationNo().trim())) {
				sameVehicleNo = true;
				setSameVehicle(ve);
				break;
			}
		}
	}
	
	public void addVehicle() {
		System.out.println("In addVehicle method");

		if (addVehicleList == null) {
			addVehicleList = new ArrayList<>();
		}

		vehicleWithRegistrationNoExist();

		if (editMode) {
			// in edit mode
			if (sameVehicleNo && !sameVehicle.equals(editingVehicle)) {
				// conflict with another vehicle
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Warning", "Another vehicle with the same registration number already exists."));
				return;
			}

			// update the existing vehicle in the list
			int index = addVehicleList.indexOf(editingVehicle);
			if (index != -1) {
				addVehicleList.set(index, vehicle); // Optional if `vehicle == editingVehicle`
			}

			editMode = false;
			editingVehicle = null;
			createNewVehicleInfo();

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Updated", "Vehicle updated successfully."));
		} else {
			// add mode
			if (sameVehicleNo) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Warning", "There is already a vehicle with the same registration number."));
			} else {
				addVehicleList.add(vehicle);
				createNewVehicleInfo();

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Vehicle added successfully."));
			}
		}
	}

	public void addNewVehicleInfo() {
		System.out.println("Selected covers: " + selectedAdditionalCovers);
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
		if (result.isVerified()) {
			addVehicle();
			selectedAdditionalCovers.clear();
			applyFleetDiscount();
			updatePremiumValuesToVehicles();
			System.out.println("success in add vehicle to list");
		} else {
			System.out.println("Validation failed for vehicle.");
			for (ErrorMessage e : result.getErrorMessages()) {
				addErrorMessage(null, e.getErrorcode(), e.getParams());
			}
		}
	}

	public String getAdditionalCoversAsString(MotorPolicyVehicleLink vehicle) {
		List<String> covers = new ArrayList<>();
		if (vehicle.isActsOfGod())
			covers.add("Acts Of God");
		if (vehicle.isNilExcess())
			covers.add("Nil Excess");
		if (vehicle.isSrcc())
			covers.add("SRCC");
		if (vehicle.isTheft())
			covers.add("Theft");
		if (vehicle.isWarRisk())
			covers.add("War Risk");
		if (vehicle.isBetterment())
			covers.add("Betterment");
		if (vehicle.isPaAndMt())
			covers.add("PA and MT");
		if (vehicle.isSunRoof())
			covers.add("Sun Roof");
		if (vehicle.isThirdParty())
			covers.add("Third Party");
		if (vehicle.isWindScreen())
			covers.add("Wind Screen");
		return String.join(", ", covers);
	}

	public void returnBranch(SelectEvent event) {
		Branch branch = (Branch) event.getObject();
		motorPolicy.setBranch(branch);
	}

	public String getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(String currentStep) {
		this.currentStep = currentStep;
	}

	public void nextStep() {
		System.out.println("nextStep: Starting, currentStep: " + (currentStep != null ? currentStep : "null")
				+ ", addVehicleList size: " + (addVehicleList != null ? addVehicleList.size() : 0));
		FacesContext context = FacesContext.getCurrentInstance();

		if (currentStep == null) {
			System.err.println("nextStep: currentStep is null, initializing to PolicyInfo");
			currentStep = "PolicyInfo";
		}

		if (!validateCurrentStep(currentStep)) {
			System.out.println("nextStep: Validation failed for step: " + currentStep);
			return;
		}

		try {
			switch (currentStep) {
			case "PolicyInfo":
				currentStep = "VehicleInfo";
				System.out.println("nextStep: Set currentStep to VehicleInfo");
				break;
			case "VehicleInfo":
				if (addVehicleList == null) {
					System.err.println("nextStep: addVehicleList is null, initializing");
					addVehicleList = new ArrayList<>();
				}
				if (addVehicleList.isEmpty()) {
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"At least one vehicle must be added before continuing."));
					System.out.println("nextStep: No vehicles in addVehicleList");
					return;
				}
				currentStep = "PremiumInfo";
				resetVehicleForm();
				System.out.println(
						"nextStep: Set currentStep to PremiumInfo, addVehicleList size: " + addVehicleList.size());
				break;
			default:
				System.err.println("nextStep: Invalid currentStep: " + currentStep);
				currentStep = "PolicyInfo";
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid wizard step."));
				break;
			}
		} catch (Exception e) {
			System.err.println("nextStep: Exception occurred: " + e.getMessage());
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Navigation failed: " + e.getMessage()));
		}
	}

	public void backStep() {
		System.out.println("backStep: Starting, currentStep: " + (currentStep != null ? currentStep : "null"));
		if (currentStep == null) {
			System.err.println("backStep: currentStep is null, initializing to PolicyInfo");
			currentStep = "PolicyInfo";
			return;
		}

		if ("VehicleInfo".equals(currentStep)) {
			resetVehicleForm();
			currentStep = "PolicyInfo";
			System.out.println("backStep: Set currentStep to PolicyInfo");
		} else if ("PremiumInfo".equals(currentStep)) {
			currentStep = "VehicleInfo";
			System.out.println("backStep: Set currentStep to VehicleInfo");
		} else {
			System.err.println("backStep: Invalid currentStep: " + currentStep);
			currentStep = "PolicyInfo";
		}
	}

	private void resetVehicleForm() {
		createNewVehicleInfo();
		selectedAdditionalCovers.clear();
		editMode = false;
		editingVehicle = null;
	}

	// Validate fields for each step
	public boolean validateCurrentStep(String step) {
		FacesContext context = FacesContext.getCurrentInstance();
		System.out.println("validateCurrentStep: Validating step: " + (step != null ? step : "null"));

		if (step == null) {
			System.err.println("validateCurrentStep: Step is null, cannot validate");
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid wizard step."));
			return false;
		}

		if ("PolicyInfo".equals(step)) {
			System.out.println("validateCurrentStep: Validating PolicyInfo");
			if (motorPolicyValidator == null) {
				System.err.println("validateCurrentStep: motorPolicyValidator is null");
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Validator not initialized."));
				return false;
			}

			ValidationResult result = motorPolicyValidator.validate(motorPolicy, true);
			if (!result.isVerified()) {
				System.out.println("validateCurrentStep: PolicyInfo validation failed: " + result.getErrorMessages());
				for (ErrorMessage e : result.getErrorMessages()) {
					addErrorMessage(null, e.getErrorcode(), e.getParams());
				}
				return false;
			}
			System.out.println("validateCurrentStep: PolicyInfo validation passed");
		}

		if ("VehicleInfo".equals(step)) {
			System.out.println("validateCurrentStep: Validating VehicleInfo");
			if (addVehicleList == null) {
				System.err.println("validateCurrentStep: addVehicleList is null");
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Vehicle list not initialized."));
				return false;
			}
			if (addVehicleList.isEmpty()) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"Please add at least one vehicle before proceeding."));
				System.out.println("validateCurrentStep: VehicleInfo validation failed: addVehicleList is empty");
				return false;
			}
			System.out.println("validateCurrentStep: VehicleInfo validation passed, addVehicleList size: "
					+ addVehicleList.size());
		}

		return true;
	}

	// Keep existing FlowEvent listener
	public String onFlowProcess(FlowEvent event) {
		String oldStep = event.getOldStep();
		String newStep = event.getNewStep();
		System.out.println("onFlowProcess: Attempting to navigate from " + oldStep + " to " + newStep);

		if (!validateCurrentStep(oldStep)) {
			System.out.println("Validation failed, staying on step: " + oldStep);
			return oldStep; // Prevent moving forward
		}

		currentStep = newStep;
		System.out.println("Navigation successful, new step: " + currentStep);
		return currentStep;
	}

	public double oneYearBasicPremiumCalculation(MotorPolicyVehicleLink v) {

		System.out.println("Product Type: " + v.getProductType());

		double rate = (v.getProductType() == ProductType.Private) ? privateRate : commercialRate;
		double oneYearBasicPremium = v.getSumInsured() * (rate / 100);

		return oneYearBasicPremium;
	}

	public double basicTermPremiumCalculation(MotorPolicyVehicleLink v, PaymentType paymentType) {
		double oneYearBasicPremium = oneYearBasicPremiumCalculation(v);

		if (paymentType != null) {
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

		if (v.isActsOfGod())
			oneYearAddOnPremium += v.getSumInsured() * (0.10 / 100);
		if (v.isTheft())
			oneYearAddOnPremium += v.getSumInsured() * (0.05 / 100);
		if (v.isWarRisk())
			oneYearAddOnPremium += v.getSumInsured() * (0.05 / 100);
		if (v.isNilExcess())
			oneYearAddOnPremium += v.getSumInsured() * (0.02 / 100);
		if (v.isSrcc())
			oneYearAddOnPremium += v.getSumInsured() * (0.03 / 100);
		if (v.isBetterment())
			oneYearAddOnPremium += v.getSumInsured() * (0.03 / 100);
		if (v.isPaAndMt())
			oneYearAddOnPremium += v.getSumInsured() * (0.02 / 100);
		if (v.isSunRoof())
			oneYearAddOnPremium += 5000;
		if (v.isThirdParty())
			oneYearAddOnPremium += 20000;
		if (v.isWindScreen())
			oneYearAddOnPremium += 5000;

		return oneYearAddOnPremium;
	}

	public double basicTermAddOnPremium(MotorPolicyVehicleLink v, PaymentType paymentType) {
		double oneYearAddOnPremium = oneYearAddonPremiun(v);

		if (paymentType != null) {
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
		int fleetThreshold = 10;
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

	public boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
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

			this.submitted = true;

			addInfoMessage(null, MessageId.INSERT_SUCCESS, motorPolicy.getPolicyNo());

		} catch (SystemException ex) {
			handleSysException(ex);
		}

		return null;
	}

	public String cancel() {
		return "/ui/system/home.xhtml?faces-redirect=true";
	}

	public void editVehicle(MotorPolicyVehicleLink vehicle) {
		System.out.println("in edit vehicle method");
		this.vehicle = vehicle;
		this.editMode = true;
		this.editingVehicle = vehicle; // store original reference

		// Re-bind selected covers if needed
		selectedAdditionalCovers = new ArrayList<>();
		if (vehicle.isActsOfGod())
			selectedAdditionalCovers.add("ActsOfGod");
		if (vehicle.isNilExcess())
			selectedAdditionalCovers.add("NilExcess");
		if (vehicle.isSrcc())
			selectedAdditionalCovers.add("SRCC");
		if (vehicle.isTheft())
			selectedAdditionalCovers.add("Theft");
		if (vehicle.isWarRisk())
			selectedAdditionalCovers.add("WarRisk");
		if (vehicle.isBetterment())
			selectedAdditionalCovers.add("Betterment");
		if (vehicle.isPaAndMt())
			selectedAdditionalCovers.add("PA_MT");
		if (vehicle.isSunRoof())
			selectedAdditionalCovers.add("SunRoof");
		if (vehicle.isThirdParty())
			selectedAdditionalCovers.add("ThirdParty");
		if (vehicle.isWindScreen())
			selectedAdditionalCovers.add("WindScreen");
	}

	public void deleteVehicle(MotorPolicyVehicleLink vehicle) {
		System.out.println("deleteVehicle: Deleting vehicle with registrationNo: "
				+ (vehicle != null ? vehicle.getRegistrationNo() : "null"));
		FacesContext context = FacesContext.getCurrentInstance();
		if (addVehicleList != null && vehicle != null) {
			addVehicleList.remove(vehicle);
			try {
				createNewVehicleInfo();
				editMode = false;
				selectedAdditionalCovers.clear();
				applyFleetDiscount();
				updatePremiumValuesToVehicles();
			} catch (Exception e) {
				System.err.println("deleteVehicle: Error in premium calculation: " + e.getMessage());
				e.printStackTrace();
				context.addMessage("motorEntryForm:growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"Failed to calculate premiums: " + e.getMessage()));
				return;
			}
			context.addMessage("motorEntryForm:growl",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Vehicle deleted successfully"));
			System.out.println("deleteVehicle: Vehicle deleted, addVehicleList size: " + addVehicleList.size());
		} else {
			System.err.println("deleteVehicle: addVehicleList or vehicle is null");
			context.addMessage("motorEntryForm:growl",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Vehicle list or vehicle not initialized."));
		}
	}

	public void generateReport() {

		List<MotorPolicyVehicleLink> addVehicleList = motorPolicy.getMotorPolicyVehicleLinks();

		if (addVehicleList == null || addVehicleList.isEmpty()) {
			addErrorMessage(null, "Cannot generate report: No vehicles found in policy.");
			return;
		}

		String pdfFilePath = dirPath + fileName + ".pdf";
		System.out.println("generateReport: Writing PDF to " + dirPath + fileName + ".pdf");

		try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("motorPolicyLetter.jrxml")) {

			if (inputStream == null) {
				addErrorMessage(null, "Report design file not found");
				return;
			}

			Map<String, Object> parameters = new HashMap<>();
			System.out.println("Customer Name = " + motorPolicy.getCustomerName());
			System.out.println("Policy No     = " + motorPolicy.getPolicyNo());
			System.out.println("Proposal No   = " + motorPolicy.getProposalNo());
			System.out.println("Submitted Date = " + motorPolicy.getSubmittedDate());
			parameters.put("CustomerName", motorPolicy.getCustomerName());
			parameters.put("PolicyNo", motorPolicy.getPolicyNo());
			parameters.put("ProposalNo", motorPolicy.getProposalNo());
			parameters.put("SubmittedDate", motorPolicy.getSubmittedDate());
			
			// Join all Registration Nos with comma
			String registrationNos = addVehicleList.stream().map(MotorPolicyVehicleLink::getRegistrationNo)
					.collect(Collectors.joining(", "));

			// Join all Add-On Covers with comma
			String addOnCoversAll = addVehicleList.stream().map(v -> {
				String covers = getAdditionalCoversAsString(v);
				return (covers != null && !covers.isEmpty()) ? covers : "None";
			}).collect(Collectors.joining(", "));

			parameters.put("RegistrationNo", registrationNos);
			parameters.put("AddOnCovers", addOnCoversAll);

			parameters.put("SumInsured", getTotalSumInsured());
			parameters.put("BasicTermPremium", getTotalBasicTermPremium());
			parameters.put("AddOnTermPremium", getTotalAddOnTermPremium());
			parameters.put("TotalPremium", getTotalTotalPremium());

			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

			File pdfFile = new File(pdfFilePath);
			FileUtils.forceMkdir(pdfFile.getParentFile()); // Create parent directories

			JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFilePath);

			addInfoMessage(null, "Report generated successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage(null, "Report Generation Failed: " + e.getMessage());
		}
	}

	public StreamedContent getDownload() {
		try {
			String pdfFilePath = dirPath + fileName + ".pdf";
			System.out.println("getDownload: Looking for PDF at " + pdfFilePath);
			File file = new File(pdfFilePath);

			// Generate report if PDF does not exist
			if (!file.exists()) {
				generateReport();
			}

			if (!file.exists()) {
				addErrorMessage(null, "Download Failed: PDF file could not be generated.");
				return null;
			}

			InputStream input = new FileInputStream(file);
			ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();

			return new DefaultStreamedContent(input, ext.getMimeType(file.getName()), file.getName());

		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage(null, "Download Failed: " + e.getMessage());
			return null;
		}
	}

	public String getFileName() {
		return fileName;
	}

	public String getPdfDirPath() {
		return pdfDirPath;
	}

	public String getDirPath() {
		return dirPath;
	}

	public String getPdfFullPath() {
		return pdfDirPath + fileName + ".pdf";
	}

	public int getCurrentYear() {
		return LocalDate.now().getYear();
	}

	public String getYearOfManufactureStr() {
	    return vehicle.getYearOfManufacture() == 0 ? "" : String.valueOf(vehicle.getYearOfManufacture());
	}

	public void setYearOfManufactureStr(String year) {
	    if (year == null || year.isEmpty()) {
	        vehicle.setYearOfManufacture(0);
	    } else {
	        vehicle.setYearOfManufacture(Integer.parseInt(year));
	    }
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
		return addVehicleList.stream().mapToDouble(MotorPolicyVehicleLink::getSumInsured).sum();
	}

	public double getTotalOneYearBasicPremium() {
		return addVehicleList.stream().mapToDouble(this::getOneYearBasicPremium).sum();
	}

	public double getTotalOneYearAddOnPremium() {
		return addVehicleList.stream().mapToDouble(this::getOneYearAddOnPremium).sum();
	}

	public double getTotalBasicTermPremium() {
		return addVehicleList.stream().mapToDouble(this::getBasicTermPremium).sum();
	}

	public double getTotalAddOnTermPremium() {
		return addVehicleList.stream().mapToDouble(this::getAddOnTermPremium).sum();
	}

	public double getTotalFleetDiscount() {
		return addVehicleList.stream().mapToDouble(this::getFleetDiscount).sum();
	}

	public double getTotalTotalPremium() {
		return addVehicleList.stream().mapToDouble(this::getTotalPremiumPerVehicle).sum();
	}

	public MotorPolicyVehicleLink getEditingVehicle() {
		return editingVehicle;
	}

	public void setEditingVehicle(MotorPolicyVehicleLink editingVehicle) {
		this.editingVehicle = editingVehicle;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean isEditMode) {
		this.editMode = isEditMode;
	}

	public MotorPolicyVehicleLink getSameVehicle() {
		return sameVehicle;
	}

	public void setSameVehicle(MotorPolicyVehicleLink sameVehicle) {
		this.sameVehicle = sameVehicle;
	}

}