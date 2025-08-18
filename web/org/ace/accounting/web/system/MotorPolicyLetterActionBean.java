package org.ace.accounting.web.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.ace.accounting.common.PropertiesManager;
import org.ace.accounting.system.motor.MotorPolicyDTO;
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

@ManagedBean(name = "MotorPolicyLetterActionBean")
@ViewScoped
public class MotorPolicyLetterActionBean extends BaseBean {

    @ManagedProperty(value = "#{PropertiesManager}")
    private PropertiesManager propertiesManager;

    public void setPropertiesManager(PropertiesManager propertiesManager) {
        this.propertiesManager = propertiesManager;
    }

    private final String reportName = "motorPolicyLetter";
    private final String fileName = "MotorPolicyLetter";
    private final String pdfDirPath = "/pdf-report/" + reportName + "/" + System.currentTimeMillis() + "/";
    private final String dirPath = getWebRootPath() + pdfDirPath;

    private MotorPolicyDTO policy;
    //private StreamedContent download;

    @PostConstruct
    public void init() {
        policy = new MotorPolicyDTO(); 
    }

    public void generateReport() {
    	try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("motorPolicyLetter.jrxml")) {

            if (inputStream == null) {
                addErrorMessage(null, "Report design file not found");
                return;
            }

            Map<String, Object> parameters = new HashMap<>();
            
            parameters.put("customerName", policy.getCustomerName());
            parameters.put("policyNo", policy.getPolicyNo());
            parameters.put("proposalNo", policy.getProposalNo());
            parameters.put("sumInsured", policy.getSumInsured());
            parameters.put("premiumTerm", policy.getBasicTermPremium());
            parameters.put("additionalPremium", policy.getAddOnTermPremium());
            parameters.put("totalPremium", policy.getTotalPremium());

            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

           
            FileUtils.forceMkdir(new File(dirPath));
            JasperExportManager.exportReportToPdfFile(jasperPrint, dirPath + fileName.concat(".pdf"));
        } catch (Exception e) {
            e.printStackTrace();
            addErrorMessage(null, "Report Generation Failed");
        }
    }

    public StreamedContent getDownload() {
        try {
            File file = new File(dirPath + fileName.concat(".pdf"));
            if (!file.exists()) {
                generateReport();
            }

            InputStream input = new FileInputStream(file);
            ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();

            return new DefaultStreamedContent(input, ext.getMimeType(file.getName()), file.getName());


        } catch (Exception e) {
            e.printStackTrace();
            addErrorMessage(null, "Download Failed");
            return null;
        }
    }

}
