package org.ace.accounting.web.listing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import javax.faces.event.AjaxBehaviorEvent;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.PropertiesManager;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.CashBookDTO;
import org.ace.accounting.dto.CashBookDetail;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.report.CashBookCriteria;
import org.ace.accounting.report.listingReport.service.interfaces.IListingReportService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.accounting.user.User;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.apache.commons.io.FileUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

/**
 * This serves as the presentation layer to manipulate the CashBook listing.An
 * then generate cash book receipt PDF.
 * 
 * @author HS
 * @version 1.0.0
 * @Date 2015/04/08
 */
@ManagedBean(name = "CashBookListActionBean")
@ViewScoped
public class CashBookListActionBean extends BaseBean {

	@ManagedProperty(value = "#{CurrencyService}")
	private ICurrencyService currencyService;

	public void setCurrencyService(ICurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	@ManagedProperty(value = "#{PropertiesManager}")
	private PropertiesManager propertiesManager;

	public void setPropertiesManager(PropertiesManager propertiesManager) {
		this.propertiesManager = propertiesManager;
	}

	@ManagedProperty(value = "#{BranchService}")
	private IBranchService branchService;

	public void setBranchService(IBranchService branchService) {
		this.branchService = branchService;
	}

	@ManagedProperty(value = "#{ListingReportService}")
	private IListingReportService listingReportService;

	public void setListingReportService(IListingReportService listingReportService) {
		this.listingReportService = listingReportService;
	}

	@ManagedProperty(value = "#{UserProcessService}")
	private IUserProcessService userProcessService;

	public void setUserProcessService(IUserProcessService userProcessService) {
		this.userProcessService = userProcessService;
	}

	private CashBookCriteria criteria;
	private List<Currency> currencyList;
	private List<Branch> branchList;
	private Date currentDate;
	private List<CashBookDTO> dtoList = null;

	private boolean admin;

	@PostConstruct
	private void init() {
		currentDate = new Date();
		criteria = new CashBookCriteria();
		Currency currency = currencyService.findHomeCurrency();
		criteria.setCurrencyType(CurrencyType.HOMECURRENCY);
		criteria.setCurrency(currency);
		criteria.setFromDate(DateUtils.minusDays(new Date(), 7));
		criteria.setToDate(new Date());
		criteria.setHomeCurrency(true);
		currencyList = currencyService.findAllCurrency();
		branchList = branchService.findAllBranch();
		rebindData();
	}

	public void rebindData() {
		User user = userProcessService.getLoginUser();
		if (!user.isAdmin()) {
			criteria.setBranch(branchService.findBranchByBranchCode(userProcessService.getLoginUser().getBranch().getBranchCode()));
			admin = true;
		}
	}

	public void search() {
		try {
			if (validate()) {
				dtoList = listingReportService.findCashbookListingReport(criteria);
				if (!dtoList.isEmpty()) {
					generatePdf();
					PrimeFaces.current().executeScript("PF('pdfDialog').show();");
				} else {
					addErrorMessage(null, MessageId.NO_RESULT);
				}

				/*
				 * RequestContext.getCurrentInstance().execute(
				 * "PF('pdfDialog').show();");
				 */
			}

		} catch (SystemException systemException) {
			handleSysException(systemException);
		}

	}

	public StreamedContent getDownload() {
		if (validate()) {
			dtoList = listingReportService.findCashbookListingReport(criteria);
			if (dtoList.size() == 0) {
				addErrorMessage(null, MessageId.NO_RESULT);
				return null;
			} else {
				return getDownloadValue(dtoList);

			}
		}
		return null;
	}

	private StreamedContent getDownloadValue(List<CashBookDTO> dtoList) {
		try {
			List<JasperPrint> prints = new ArrayList<JasperPrint>();
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("report-template/cashBookListing.jrxml");
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			Branch branch = criteria.getBranch();
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("LOGO"));
			String currencyString = "";
			if (criteria.getCurrency() == null) {
				currencyString = "All Currencies";
			} else {
				currencyString = criteria.getCurrency().getCurrencyCode();
				if (criteria.isHomeCurrencyConverted()) {
					currencyString = currencyString + " By Home Amount";
				}
			}
			// String currencyCode =
			// criteria.getCurrencyType().equals(CurrencyType.HOMECURRENCY) ?
			// "MMK" : criteria.getCurrency().getCurrencyCode();
			List<CashBookDetail> details = null;
			for (CashBookDTO cashBook : dtoList) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				details = cashBook.getDetails();
				paramMap.put("branchName", branch == null ? "All Branches" : branch.getName());
				paramMap.put("currencyName", currencyString);
				paramMap.put("reportDate", new Date());
				paramMap.put("fromDate", criteria.getFromDate());
				paramMap.put("toDate", criteria.getToDate());
				paramMap.put("acName", cashBook.getAcName());
				paramMap.put("openingBalance", cashBook.getOpeningBalance());
				paramMap.put("records", details.size());
				paramMap.put("logoPath", image);
				paramMap.put("detailReports", new JRBeanCollectionDataSource(details));
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, paramMap, new JREmptyDataSource());
				prints.add(jasperPrint);
			}

			FileUtils.forceMkdir(new File(dirPath));

			File destFile = new File(dirPath + fileName.concat(".xls"));

			JRXlsExporter exporter = new JRXlsExporter();

			exporter.setExporterInput(SimpleExporterInput.getInstance(prints));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setDetectCellType(true);
			configuration.setIgnoreCellBorder(false);
			configuration.setAutoFitPageHeight(true);
			configuration.setCollapseRowSpan(true);
			configuration.setFontSizeFixEnabled(true);
			configuration.setColumnWidthRatio(2.5F);

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

	private boolean validate() {
		if (criteria.getFromDate().after(currentDate)) {
			addErrorMessage(null, MessageId.START_DATE_EXCEEDED);
			return false;
		} else if (criteria.getToDate().after(currentDate)) {
			addErrorMessage(null, MessageId.END_DATE_EXCEEDED);
			return false;
		} else if (criteria.getFromDate().after(criteria.getToDate())) {
			addErrorMessage(null, MessageId.SE_INVALID_DATE);
			return false;
		} else {
			return true;
		}
	}

	public CurrencyType[] getCurrencyReportTypes() {
		return CurrencyType.values();
	}

	public List<Currency> getCurrencyList() {
		return currencyList;
	}

	public List<Branch> getBranches() {
		return branchList;
	}

	public CashBookCriteria getCriteria() {
		return criteria;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	/**
	 * 
	 * Export PDF
	 **/

	private final String reportName = "cashBookListing";
	private final String pdfDirPath = "/pdf-report/" + reportName + "/" + System.currentTimeMillis() + "/";
	private final String dirPath = getWebRootPath() + pdfDirPath;
	private final String fileName = reportName;

	/**
	 * To show PDF in dialog
	 * 
	 * @return pdfDirPath
	 */
	public String getReportStream() {
		return pdfDirPath + fileName.concat(".pdf");
	}

	/**
	 * export PDF file to according dirPath
	 */
	private void generatePdf() {
		try {
			List<JasperPrint> prints = new ArrayList<JasperPrint>();
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("report-template/cashBookListing.jrxml");
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			Branch branch = criteria.getBranch();
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("LOGO"));
			String currencyString = "";
			if (criteria.getCurrency() == null) {
				currencyString = "All Currencies";
			} else {
				currencyString = criteria.getCurrency().getCurrencyCode();
				if (criteria.isHomeCurrencyConverted()) {
					currencyString = currencyString + " By Home Amount";
				}
			}
			// String currencyCode =
			// criteria.getCurrencyType().equals(CurrencyType.HOMECURRENCY) ?
			// "MMK" : criteria.getCurrency().getCurrencyCode();
			List<CashBookDetail> details = null;
			for (CashBookDTO cashBook : dtoList) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				details = cashBook.getDetails();
				paramMap.put("branchName", branch == null ? "All Branches" : branch.getName());
				paramMap.put("currencyName", currencyString);
				paramMap.put("reportDate", new Date());
				paramMap.put("fromDate", criteria.getFromDate());
				paramMap.put("toDate", criteria.getToDate());
				paramMap.put("acName", cashBook.getAcName());
				paramMap.put("openingBalance", cashBook.getOpeningBalance());
				paramMap.put("records", details.size());
				paramMap.put("logoPath", image);
				paramMap.put("detailReports", new JRBeanCollectionDataSource(details));
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, paramMap, new JREmptyDataSource());
				prints.add(jasperPrint);
			}

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(SimpleExporterInput.getInstance(prints));
			FileUtils.forceMkdir(new File(dirPath));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(dirPath + fileName.concat(".pdf")));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			configuration.setCreatingBatchModeBookmarks(true);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
		} catch (JRException exception) {
			addErrorMessage(null, MessageId.REPORT_ERROR);
		} catch (Exception e) {
			addErrorMessage(null, MessageId.REPORT_ERROR);
		}

	}

	public void handleClose(CloseEvent event) {
		try {
			FileUtils.forceDelete(new File(dirPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Date getTodayDate() {
		return new Date();
	}

	public void changeCurrencyType(AjaxBehaviorEvent event) {
		criteria.setHomeCurrency(criteria.getCurrencyType().equals(CurrencyType.HOMECURRENCY));
	}

	public boolean isAdmin() {
		return admin;
	}

}
