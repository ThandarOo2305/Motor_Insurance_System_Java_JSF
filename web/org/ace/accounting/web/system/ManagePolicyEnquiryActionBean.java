package org.ace.accounting.web.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.ace.accounting.common.validation.ErrorMessage;
import org.ace.accounting.common.validation.IDataValidator;
import org.ace.accounting.common.validation.ValidationResult;
import org.ace.accounting.system.motor.MotorEnquiry;
import org.ace.accounting.system.motor.MotorEnquiryDTO;
import org.ace.accounting.system.motor.MotorPolicy;
import org.ace.accounting.system.motor.MotorPolicyVehicleLink;
import org.ace.accounting.system.motor.service.interfaces.IMotorEnquiryService;
import org.ace.java.web.common.BaseBean;
import org.apache.commons.io.FileUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "ManagePolicyEnquiryActionBean")
@ViewScoped
public class ManagePolicyEnquiryActionBean extends BaseBean {

	@ManagedProperty(value = "#{MotorEnquiryService}")
	private IMotorEnquiryService policyEnquiryService;

	public void setPolicyEnquiryService(IMotorEnquiryService policyEnquiryService) {
		this.policyEnquiryService = policyEnquiryService;
	}

	@ManagedProperty(value = "#{MotorEnquiryValidator}")
	private IDataValidator<MotorEnquiry> enquiryValidator;
	
	public void setEnquiryValidator(IDataValidator<MotorEnquiry> enquiryValidator) {
		this.enquiryValidator = enquiryValidator;
	}

	private Date policyStartDate;
	private Date policyEndDate;
	private String policyNo;
	private String registrationNo;
	private MotorEnquiry mpq;
	// Search Result Lists
	private List<MotorEnquiryDTO> results;
	
	private MotorPolicy selectedPolicy;

	public MotorPolicy getSelectedPolicy() {
	    return selectedPolicy;
	}

	public void setSelectedPolicy(MotorPolicy selectedPolicy) {
	    this.selectedPolicy = selectedPolicy;
	}
	
	public void viewDetails(MotorEnquiryDTO enquiry) {
	    if (enquiry != null && enquiry.getPolicyNo() != null) {
	        try {
	            // Fetch full MotorPolicy with vehicle links
	            selectedPolicy = policyEnquiryService.findByPolicyNo(enquiry.getPolicyNo());
	            System.out.println("Vehicle links count = " + selectedPolicy.getVehicleLinks().size());
//	            System.out.println("Additional Covers = " + getAdditionalCoversAsString(selectedPolicy));
	            } catch (Exception e) {
	            e.printStackTrace();
	            addErrorMessage(null, "Failed to load policy details");
	        }
	    }
	}
	
//	public String getAdditionalCoversAsString(MotorPolicy policy) {
//	    if (policy == null || policy.getVehicleLinks() == null || policy.getVehicleLinks().isEmpty()) {
//	        return "";
//	    }
//
//	    // Example: Take first vehicle. You can loop if needed.
//	    MotorPolicyVehicleLink vehicle = policy.getVehicleLinks().get(0);
//	    List<String> covers = new ArrayList<>();
//
//	    if (Boolean.TRUE.equals(vehicle.isActsOfGod())) covers.add("Acts Of God");
//	    if (Boolean.TRUE.equals(vehicle.isNilExcess())) covers.add("Nil Excess");
//	    if (Boolean.TRUE.equals(vehicle.isSrcc())) covers.add("SRCC");
//	    if (Boolean.TRUE.equals(vehicle.isTheft())) covers.add("Theft");
//	    if (Boolean.TRUE.equals(vehicle.isWarRisk())) covers.add("War Risk");
//	    if (Boolean.TRUE.equals(vehicle.isBetterment())) covers.add("Betterment");
//	    if (Boolean.TRUE.equals(vehicle.isPaAndMt())) covers.add("PA and MT");
//	    if (Boolean.TRUE.equals(vehicle.isSunRoof())) covers.add("Sun Roof");
//	    if (Boolean.TRUE.equals(vehicle.isThirdParty())) covers.add("Third Party");
//	    if (Boolean.TRUE.equals(vehicle.isWindScreen())) covers.add("Wind Screen");
//
//	    return String.join(", ", covers);
//	}


	// init
	@PostConstruct
	public void init() {
		setDefaultDates();
	}

	private void setDefaultDates() {
		Calendar endCal = Calendar.getInstance();
		endCal.set(Calendar.HOUR_OF_DAY, 0);// for end date
		endCal.set(Calendar.MINUTE, 0);
		endCal.set(Calendar.SECOND, 0);
		endCal.set(Calendar.MILLISECOND, 0);
		this.policyEndDate = endCal.getTime();

		Calendar startCal = (Calendar) endCal.clone();// for start date
		startCal.add(Calendar.DAY_OF_MONTH, -7);
		this.policyStartDate = startCal.getTime();
	}

	// Search Method
	public void search() {
		try {
			setMotorEnquiry();
			
			ValidationResult result = enquiryValidator.validate(mpq, true);
			
			System.out.println("startdate: " + policyStartDate + ", enddate: " + policyEndDate + ", policyNo: "
					+ policyNo + ", registrationNo: " + registrationNo);
			if (result.isVerified()) {
				
				results = policyEnquiryService.search(policyStartDate, policyEndDate, policyNo, registrationNo);
				
				System.out.println("success__________________________");
			} else {
				System.out.println("Validation failed for vehicle.");
		        for(ErrorMessage e: result.getErrorMessages()) {
		        	addErrorMessage(null , e.getErrorcode(), e.getParams());
		        }
		}		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reset() {
		this.policyStartDate = null;
		this.policyEndDate = null;
		this.policyNo = null;
		this.registrationNo = null;
		this.results = new ArrayList<>();
		this.mpq = new MotorEnquiry();
	}

	public void setMotorEnquiry() {
		mpq = new MotorEnquiry();
		mpq.setPolicyStartDate(policyStartDate);
		mpq.setPolicyEndDate(policyEndDate);
		mpq.setPolicyNo(policyNo);
		mpq.setRegistrationNo(registrationNo);
	}
	
	private final String reportName = "motorPolicyEnquiryLetter";
	private final String fileName = "MotorPolicyEnquiryLetter";
	private final String pdfDirPath = "/pdf-report/" + reportName + "/" + System.currentTimeMillis() + "/";
	private final String dirPath = getWebRootPath() + pdfDirPath;

	public void generateReport() {
		
		if (results == null || results.isEmpty()) {
	        addErrorMessage(null, "No search results to generate report");
	        return;
	    }

	    // Example: take the first result, or better, use selectedPolicy/selected DTO
	    MotorEnquiryDTO dto = results.get(0);

		String pdfFilePath = dirPath + fileName + ".pdf";
		System.out.println("generateReport: Writing PDF to " + dirPath + fileName + ".pdf");

		try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("motorPolicyEnquiry.jrxml")) {

			if (inputStream == null) {
				addErrorMessage(null, "Report design file not found");
				return;
			}

			Map<String, Object> parameters = new HashMap<>();
			System.out.println("Customer Name = " + dto.getCustomerName());
			System.out.println("Policy No     = " + dto.getPolicyNo());
			System.out.println("Proposal No   = " + dto.getProposalNo());
			parameters.put("CustomerName", dto.getCustomerName());
			parameters.put("PolicyNo", dto.getPolicyNo());
			parameters.put("ProposalNo", dto.getProposalNo());
			parameters.put("RegistrationNo", dto.getRegistrationNo());
			parameters.put("SaleChannel", dto.getSaleChannel().name());
			parameters.put("Branch", dto.getBranch());
			parameters.put("ClaimCount", dto.getClaimCount());

			parameters.put("TotalSumInsured", dto.getTotalSumInsured());
			parameters.put("BasicPremium", dto.getBasicPremium());
			parameters.put("SubmittedDate", dto.getSubmittedDate());

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


	// Getters and setters for all fields
	public Date getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(Date policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public Date getPolicyEndDate() {
		return policyEndDate;
	}

	public void setPolicyEndDate(Date policyEndDate) {
		this.policyEndDate = policyEndDate;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public List<MotorEnquiryDTO> getResults() {
		return results;
	}

	public void setResults(List<MotorEnquiryDTO> results) {
		this.results = results;
	}

	public MotorEnquiry getMpq() {
		return mpq;
	}

	public void setMpq(MotorEnquiry mpq) {
		this.mpq = mpq;
	}

	

}