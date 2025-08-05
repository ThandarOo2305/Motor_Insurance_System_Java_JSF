package org.ace.accounting.web.listing;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

import org.ace.accounting.common.PropertiesManager;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.CoaListingDto;
import org.ace.accounting.report.listingReport.service.interfaces.IListingReportService;
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
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

@ManagedBean(name = "CoaListingActionBean")
@ViewScoped
public class CoaListingActionBean extends BaseBean {

	@ManagedProperty(value = "#{ListingReportService}")
	private IListingReportService listingReportService;

	public void setListingReportService(IListingReportService listingReportService) {
		this.listingReportService = listingReportService;
	}

	@ManagedProperty(value = "#{PropertiesManager}")
	private PropertiesManager propertiesManager;

	public void setPropertiesManager(PropertiesManager propertiesManager) {
		this.propertiesManager = propertiesManager;
	}

	private final String reportName = "coaListingReport";
	private final String pdfDirPath = "/pdf-report/" + reportName + "/" + System.currentTimeMillis() + "/";
	private final String dirPath = getWebRootPath() + pdfDirPath;
	private final String fileName = reportName;

	private CoaListingDto dto;
	private List<CoaListingDto> dtoList;

	@PostConstruct
	public void init() {
		dtoList = listingReportService.findAllCoaList();
	}

	public void generateReport() {
		try {
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("coaListingReport.jrxml");
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("reportDate", new Date());
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("LOGO"));
			parameters.put("logoPath", image);
			parameters.put("records", dtoList.size());
			parameters.put("COALIST", new JRBeanCollectionDataSource(dtoList));
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
			FileUtils.forceMkdir(new File(dirPath));
			JasperExportManager.exportReportToPdfFile(jasperPrint, dirPath + fileName.concat(".pdf"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public StreamedContent getDownloadValue() {
		try {
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("coaListingReport.jrxml");
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("reportDate", new Date());
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("LOGO"));
			parameters.put("logoPath", image);
			parameters.put("records", dtoList.size());
			parameters.put("COALIST", new JRBeanCollectionDataSource(dtoList));

			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(dtoList));

			FileUtils.forceMkdir(new File(dirPath));

			File destFile = new File(dirPath + fileName.concat(".xls"));

			JRXlsExporter exporter = new JRXlsExporter();

			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setOnePagePerSheet(false);
			configuration.setAutoFitPageHeight(true);
			configuration.setDetectCellType(true);
			configuration.setPrintPageWidth(200);
			configuration.setIgnoreCellBorder(false);
			configuration.setAutoFitPageHeight(true);
			configuration.setCollapseRowSpan(true);

			exporter.setConfiguration(configuration);

			exporter.exportReport();

			StreamedContent download = new DefaultStreamedContent();
			File file = new File(dirPath + fileName.concat(".xls"));
			InputStream input = new FileInputStream(file);
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			download = new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName());
			return download;

		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage(null, MessageId.REPORT_ERROR);
			return null;
		}

	}

	public String getStream() {
		String fullFilePath = pdfDirPath + fileName.concat(".pdf");
		return fullFilePath;

	}

	public CoaListingDto getDto() {
		return dto;
	}

	public void setDto(CoaListingDto dto) {
		this.dto = dto;
	}

	public List<CoaListingDto> getDtoList() {
		dtoList = listingReportService.findAllCoaList();
		return dtoList;
	}

	public void setDtoList(List<CoaListingDto> dtoList) {
		this.dtoList = dtoList;
	}
}
