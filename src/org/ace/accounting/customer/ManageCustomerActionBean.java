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

@ManagedBean(name = "ManageCustomerActionBean")
@ViewScoped
public class ManageCustomerActionBean extends BaseBean {
	
	@ManagedProperty(value = "#{CustomerService}")
	private ICustomerService customerService;
	
	public ICustomerService getCustomerService() {
		return customerService;
	}


	public void setCustomerService(ICustomerService customerService) {
		this.customerService = customerService;
	}

	private Customer customer;
	private List<Customer> customerList;
	private final String reportName = "customer";
    private final String pdfDirPath = "/pdf-report/" + reportName + "/" + System.currentTimeMillis() + "/";
    private final String dirPath = getWebRootPath() + pdfDirPath;
    private final String fileName = reportName;
    private boolean skip;
	
	
	private boolean createNew;
    
	@PostConstruct
	public void init() {
		
		if(customer == null ) { 
			createNew = true;
		//	customer = new Customer();
		}
		 
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		customer = new Customer();
		customer.setId((String) flash.get("customerId"));
		if(customer.getId() != null) {
			createNew = false;
			customer.setSalutation((String) flash.get("salutation"));
			customer.setFirstName((String) flash.get("firstName"));
			customer.setMiddleName((String) flash.get("middleName"));
			customer.setLastName((String) flash.get("lastName"));
			customer.setFatherName((String) flash.get("fatherName"));
			customer.setGender((String) flash.get("gender"));
			customer.setDateOfBirth((Date) flash.get("dateOfBirth"));
			customer.setBirthMark((String) flash.get("birthMark"));
			customer.setMaritalStatus((String) flash.get("maritalStatus"));
			customer.setOccupation((Occupation) flash.get("occupation"));
			customer.setResidentAddress((String) flash.get("residentAddress"));
			customer.setResidentTownship((String) flash.get("residentTownship"));
			customer.setPermanentAddress((String) flash.get("permanentAddress"));
			customer.setPermanentTownship((String) flash.get("permanentTownship"));
			customer.setOfficeAddress((String) flash.get("officeAddress"));
			customer.setOfficeTownship((String) flash.get("officeTownship"));
			customer.setPhone((String) flash.get("phone"));
			customer.setFax((String) flash.get("fax"));
			customer.setMobile((String) flash.get("mobile"));
			customer.setEmail((String) flash.get("email"));
			customer.setRelativeSalutation((String) flash.get("relativeSalutation"));
			customer.setRelativeFirstName((String) flash.get("relativeFirstName"));
			customer.setRelativeMiddleName((String) flash.get("relativeMiddleName"));
			customer.setRelativeLastName((String) flash.get("relativeLastName"));
			customer.setRelationship((String) flash.get("relationship"));
			customer.setRelativeDOB((Date) flash.get("relativeDOB"));
			customer.setVersion((int) flash.get("version"));
			customer.setBasicEntity((BasicEntity) flash.get("basicEntity"));
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
		customerService.addNewCustomer(customer);
	//	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Customer Created Successfully"));
		return "customerList";
	}
	
	public String preparedUpdateCustomer(Customer customer) {
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		flash.put("customerId", customer.getId());
		flash.put("salutation", customer.getSalutation());
		flash.put("firstName", customer.getFirstName());
		flash.put("middleName", customer.getMiddleName());
		flash.put("lastName", customer.getLastName());
		flash.put("fatherName", customer.getFatherName());
		flash.put("gender", customer.getGender());
		flash.put("dateOfBirth", customer.getDateOfBirth());
		flash.put("birthMark", customer.getBirthMark());
		flash.put("maritalStatus", customer.getMaritalStatus());
		flash.put("occupation", customer.getOccupation());
		flash.put("residentAddress", customer.getResidentAddress());
		flash.put("residentTownship", customer.getResidentTownship());
		flash.put("permanentAddress", customer.getPermanentAddress());
		flash.put("permanentTownship", customer.getPermanentTownship());
		flash.put("officeAddress", customer.getOfficeAddress());
		flash.put("officeTownship", customer.getOfficeTownship());
		flash.put("phone", customer.getPhone());
		flash.put("fax", customer.getFax());
		flash.put("mobile", customer.getMobile());
		flash.put("email", customer.getEmail());
		flash.put("relativeSalutation", customer.getRelativeSalutation());
		flash.put("relativeFirstName", customer.getRelativeFirstName());
		flash.put("relativeMiddleName", customer.getRelativeMiddleName());
		flash.put("relativeLastName", customer.getRelativeLastName());
		flash.put("relationship", customer.getRelationship());
		flash.put("relativeDOB", customer.getRelativeDOB());
		flash.put("version", customer.getVersion());
		flash.put("basicEntity", customer.getBasicEntity());
		return "manageCustomer";
	}


	public boolean isCreateNew() {
		return createNew;
	}

	public String updateCustomer() {
		try {
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
		if(skip || "contact".equals(event.getOldStep())) {
			if (skip) {
	            skip = false; // reset in case user goes back
	            return "confirm";
			}else {
				saveContactInfo();
	    	}
		}
		return event.getNewStep();
	}
	

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
  
	public void saveContactInfo() {
		residentAddress = customer.getResidentAddress();
		System.out.println(residentAddress);
		residentTownship = customer.getResidentTownship();
		permanentAddress = customer.getPermanentAddress();
		permanentTownship = customer.getPermanentTownship();
		officeAddress = customer.getOfficeAddress();
		officeTownship = customer.getOfficeTownship();
		phone = customer.getPhone();
		fax = customer.getFax();
		mobile = customer.getMobile();
		email = customer.getEmail();
	}
	
}
