package org.ace.accounting.customer;

import java.io.File;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.common.MaritalStatus;
import org.ace.accounting.common.Salutation;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.occupation.Occupation;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.apache.commons.io.FileUtils;
import org.apache.http.protocol.RequestContent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.reactive.result.view.RequestContext;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "ManageCustomerActionBean2")
@ViewScoped
public class ManageCustomerActionBean2 extends BaseBean {
	
	@ManagedProperty(value = "#{CustomerService}")
	private ICustomerService customerService;
	
	public ICustomerService getCustomerService() {
		return customerService;
	}


	public void setCustomerService(ICustomerService customerService) {
		this.customerService = customerService;
	}

	private Customer customer;
	private CustomerDTO customerDTO;
	private List<Customer> customerList;
	private final String reportName = "customer";
    private final String pdfDirPath = "/pdf-report/" + reportName + "/" + System.currentTimeMillis() + "/";
    private final String dirPath = getWebRootPath() + pdfDirPath;
    private final String fileName = reportName;
    private boolean skip;
    private Map<String, CustomerDTO> tempFormData = new HashMap<>();
	
	
	public Map<String, CustomerDTO> getTempFormData() {
		return tempFormData;
	}


	public void setTempFormData(Map<String, CustomerDTO> tempFormData) {
		this.tempFormData = tempFormData;
	}

	private boolean createNew;
    
	@PostConstruct
	public void init() {
		
		if(customer == null ) { 
			createNew = true;
		//	customer = new Customer();
		//	customerDTO = new CustomerDTO();
		}
		 
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		customerDTO = new CustomerDTO();
		customerDTO.setId((String) flash.get("customerId"));
		if(customerDTO.getId() != null) {
			createNew = false;
			customerDTO.setSalutation((String) flash.get("salutation"));
			customerDTO.setFirstName((String) flash.get("firstName"));
			customerDTO.setMiddleName((String) flash.get("middleName"));
			customerDTO.setLastName((String) flash.get("lastName"));
			customerDTO.setFatherName((String) flash.get("fatherName"));
			customerDTO.setGender((String) flash.get("gender"));
			customerDTO.setDateOfBirth((Date) flash.get("dateOfBirth"));
			customerDTO.setBirthMark((String) flash.get("birthMark"));
			customerDTO.setMaritalStatus((String) flash.get("maritalStatus"));
			customerDTO.setOccupation((Occupation) flash.get("occupation"));
			customerDTO.setResidentAddress((String) flash.get("residentAddress"));
			customerDTO.setResidentTownship((String) flash.get("residentTownship"));
			customerDTO.setPermanentAddress((String) flash.get("permanentAddress"));
			customerDTO.setPermanentTownship((String) flash.get("permanentTownship"));
			customerDTO.setOfficeAddress((String) flash.get("officeAddress"));
			customerDTO.setOfficeTownship((String) flash.get("officeTownship"));
			customerDTO.setPhone((String) flash.get("phone"));
			customerDTO.setFax((String) flash.get("fax"));
			customerDTO.setMobile((String) flash.get("mobile"));
			customerDTO.setEmail((String) flash.get("email"));
			customerDTO.setRelativeSalutation((String) flash.get("relativeSalutation"));
			customerDTO.setRelativeFirstName((String) flash.get("relativeFirstName"));
			customerDTO.setRelativeMiddleName((String) flash.get("relativeMiddleName"));
			customerDTO.setRelativeLastName((String) flash.get("relativeLastName"));
			customerDTO.setRelationship((String) flash.get("relationship"));
			customerDTO.setRelativeDOB((Date) flash.get("relativeDOB"));
			customerDTO.setVersion((int) flash.get("version"));
			customerDTO.setBasicEntity((BasicEntity) flash.get("basicEntity"));
		}
		rebindData();
	}
	
	private void rebindData() {
		customerList = customerService.findAll();
	}
	
	public String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            return dateFormat.format(date);
        } else {
            return ""; // or any other default value
        }
    }
	
	public String addNewCustomer() {
		customer = customerService.changeDTOToCustomer(customerDTO);
		customerService.addNewCustomer(customer);
	//	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Customer Created Successfully"));
		return "customerList";
	}
	
	public String preparedUpdateCustomer(Customer customer) {
		customerDTO = customerService.changeCustomerToDTO(customer);
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		flash.put("customerId", customerDTO.getId());
		flash.put("salutation", customerDTO.getSalutation());
		flash.put("firstName", customerDTO.getFirstName());
		flash.put("middleName", customerDTO.getMiddleName());
		flash.put("lastName", customerDTO.getLastName());
		flash.put("fatherName", customerDTO.getFatherName());
		flash.put("gender", customerDTO.getGender());
		flash.put("dateOfBirth", customerDTO.getDateOfBirth());
		flash.put("birthMark", customerDTO.getBirthMark());
		flash.put("maritalStatus", customerDTO.getMaritalStatus());
		flash.put("occupation", customerDTO.getOccupation());
		flash.put("residentAddress", customerDTO.getResidentAddress());
		flash.put("residentTownship", customerDTO.getResidentTownship());
		flash.put("permanentAddress", customerDTO.getPermanentAddress());
		flash.put("permanentTownship", customerDTO.getPermanentTownship());
		flash.put("officeAddress", customerDTO.getOfficeAddress());
		flash.put("officeTownship", customerDTO.getOfficeTownship());
		flash.put("phone", customerDTO.getPhone());
		flash.put("fax", customerDTO.getFax());
		flash.put("mobile", customerDTO.getMobile());
		flash.put("email", customerDTO.getEmail());
		flash.put("relativeSalutation", customerDTO.getRelativeSalutation());
		flash.put("relativeFirstName", customerDTO.getRelativeFirstName());
		flash.put("relativeMiddleName", customerDTO.getRelativeMiddleName());
		flash.put("relativeLastName", customerDTO.getRelativeLastName());
		flash.put("relationship", customerDTO.getRelationship());
		flash.put("relativeDOB", customerDTO.getRelativeDOB());
		flash.put("version", customerDTO.getVersion());
		flash.put("basicEntity", customerDTO.getBasicEntity());
		return "manageCustomer";
	}


	public boolean isCreateNew() {
		return createNew;
	}

	public String updateCustomer() {
		try {
			customer = customerService.changeDTOToCustomer(customerDTO);
			customerService.updateCustomer(customer);
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Customer Updated Successfully"));
			rebindData();
		} catch (SystemException ex) {
			handleSysException(ex);
		}
		return "customerList";
	}
	
	public String deleteCustomer(Customer customer) {
		try {
			customerService.deleteCustomer(customer);
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Customer Deleted Successfully"));
			rebindData();
		} catch (SystemException ex) {
			handleSysException(ex);
		}
		return "customerList";
	}
	
	public void setCreateNew(boolean createNew) {
		this.createNew = createNew;
	}


	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	

	public CustomerDTO getCustomerDTO() {
		return customerDTO;
	}


	public void setCustomerDTO(CustomerDTO customerDTO) {
		this.customerDTO = customerDTO;
	}


	public List<Customer> getCustomerList() {
		return customerList;
	}


	public void setCustomerList(List<Customer> customerList) {
		this.customerList = customerList;
	}


	public String createNewCustomer() {
		return "manageCustomer";
	}
	
	public EnumSet<Salutation> getSalutations(){
		return EnumSet.of(Salutation.Mr,Salutation.Mrs,Salutation.Miss);
	}
	
	public EnumSet<MaritalStatus> getMaritalStatus(){
		return EnumSet.of(MaritalStatus.SINGLE,MaritalStatus.MARRIED);
	}

	public void returnOccupation(SelectEvent event) {
		Occupation occupation = (Occupation)event.getObject();
		customer.setOccupation(occupation);
	}

	public void generateReport(Customer customer) {
    	try {
    		List<Customer> customers = new ArrayList<Customer>();
            customers.add(customer);
            InputStream templateStream = getClass().getResourceAsStream("/report-template/customerReport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(customers);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
            FileUtils.forceMkdir(new File(dirPath));
            JasperExportManager.exportReportToPdfFile(jasperPrint,dirPath+fileName.concat(".pdf"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	
	
	public String getStream() {
		String fullFilePath = pdfDirPath + fileName.concat(".pdf");
		return fullFilePath;

	}
	
	public String onFlowProcess(FlowEvent event) {
		String currentStep = event.getOldStep();
		String newStep = event.getNewStep();
		if(skip || currentStep != null) {
			if(skip) {
				skip = false;
				return "confirm";
			}else {
				System.out.println(currentStep);
				switch (currentStep) {
				case "personal":
					saveFormData("personal", customerDTO);
				case "contact":
					saveFormData("contact", customerDTO);
				case "family":
					saveFormData("family", customerDTO);
					break;

				default:
					break;
			}
			}
		
		}	
			return newStep;
		
	}
	
//	public String onFlowProcess(FlowEvent event) {
//			if (skip) {
//	            skip = false; // reset in case user goes back
//	            return "confirm";
//			}else {
//				return event.getNewStep();
//	    	}
//		
//		
//		
//	}

	public boolean isSkip() {
		return skip;
	}


	public void setSkip(boolean skip) {
		this.skip = skip;
	}
	
	private String residentAddress;
	private String residentTownship;
	private String permanentAddress;
	private String permanentTownship;
	private String officeAddress;
	private String officeTownship;
	private String phone;
	private String fax;
	private String mobile;
	private String email;

	public String getResidentAddress() {
		return residentAddress;
	}


	public void setResidentAddress(String residentAddress) {
		this.residentAddress = residentAddress;
	}


	public String getResidentTownship() {
		return residentTownship;
	}


	public void setResidentTownship(String residentTownship) {
		this.residentTownship = residentTownship;
	}


	public String getPermanentAddress() {
		return permanentAddress;
	}


	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}


	public String getPermanentTownship() {
		return permanentTownship;
	}


	public void setPermanentTownship(String permanentTownship) {
		this.permanentTownship = permanentTownship;
	}


	public String getOfficeAddress() {
		return officeAddress;
	}


	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}


	public String getOfficeTownship() {
		return officeTownship;
	}


	public void setOfficeTownship(String officeTownship) {
		this.officeTownship = officeTownship;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getFax() {
		return fax;
	}


	public void setFax(String fax) {
		this.fax = fax;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getFileName() {
		return fileName;
	}
  
//	public void saveContactInfo() {
//		residentAddress = customerDTO.getResidentAddress();
//		System.out.println(residentAddress);
//		residentTownship = customerDTO.getResidentTownship();
//		permanentAddress = customerDTO.getPermanentAddress();
//		permanentTownship = customerDTO.getPermanentTownship();
//		officeAddress = customerDTO.getOfficeAddress();
//		officeTownship = customerDTO.getOfficeTownship();
//		phone = customerDTO.getPhone();
//		fax = customerDTO.getFax();
//		mobile = customerDTO.getMobile();
//		email = customerDTO.getEmail();
//	}
	
	public void saveFormData(String stepName,CustomerDTO customerDTO) {
		tempFormData.put(stepName, customerDTO);
	}
	
	public CustomerDTO getFormData(String stepName) {
		return tempFormData.get(stepName);
	}
}

