package org.ace.accounting.web.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.PropertiesManager;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.CashBookDTO;
import org.ace.accounting.dto.CashBookDetail;
import org.ace.accounting.report.CashBookCriteria;
import org.ace.accounting.report.ReportCriteria;
import org.ace.accounting.report.cleanCashReport.CleanCashReport;
import org.ace.accounting.report.cleanCashReport.service.interfaces.ICleanCashService;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
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

/***************************************************************************************
 * @Date 2016-04-05
 * @author PPA
 * @Version 1.0
 * @Purpose This class serves as presentation layer to manipulate
 *          <code>CleanCashReport</code> Clean Cash Report process
 * 
 ***************************************************************************************/
@ViewScoped
@ManagedBean(name = "CleanCashActionBean")
public class CleanCashActionBean extends BaseBean {
	@ManagedProperty(value = "#{PropertiesManager}")
	private PropertiesManager propertiesManager;

	public void setPropertiesManager(PropertiesManager propertiesManager) {
		this.propertiesManager = propertiesManager;
	}

	@ManagedProperty("#{CleanCashService}")
	private ICleanCashService cleanCashService;

	// Setter.
	public void setCleanCashService(ICleanCashService cleanCashService) {
		this.cleanCashService = cleanCashService;
	}

	@ManagedProperty(value = "#{CurrencyService}")
	private ICurrencyService currencyService;

	// Setter.
	public void setCurrencyService(ICurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	@ManagedProperty(value = "#{ListingReportService}")
	private IListingReportService listingReportService;

	public void setListingReportService(IListingReportService listingReportService) {
		this.listingReportService = listingReportService;
	}

	@ManagedProperty(value = "#{BranchService}")
	private IBranchService branchService;

	public void setBranchService(IBranchService branchService) {
		this.branchService = branchService;
	}

	// Current User for the system.
	private User user;
	// To store sum of debit and sum of credit amount of Account code with only
	// one record.
	private Map<String, CleanCashReport> cleanCashMap;
	// List of CleanCash.
	private List<CleanCashReport> cleanCashList;
	// Criteria for user choice.
	private ReportCriteria criteria;
	// To store System current date.
	private Date criteriaMaxDate;
	// To know all currencies or not for user selection.
	private boolean isAllCurrency;

	// path for jrxml template.
	private String dirPath = "/pdf-report/" + "cleanCashReport" + "/" + System.currentTimeMillis() + "/";
	// pdf name.
	private final String fileName = "cleanCashReport";

	/**
	 * Get Current User for the system. Create new ClanCash List Object and
	 * Criteria Object. Get System Current date and set to criteria date. When
	 * page load, currency will be all and according to by home amount
	 * 
	 * @return void.
	 */
	@PostConstruct
	public void init() {
		user = (User) getParam("LoginUser");
		cleanCashList = new ArrayList<CleanCashReport>();
		criteria = new ReportCriteria();
		criteriaMaxDate = new Date();
		criteria.setDate(criteriaMaxDate);
		criteria.setByHome(true);
		isAllCurrency = true;
	}

	/**
	 * Search clean cash report data for the given date, currency and branch
	 * criteria.
	 * 
	 * When the given date is greater than system current date, show error
	 * message.
	 * 
	 * When there is no record found for the criteria, show message.
	 * 
	 * @return void.
	 * 
	 * @throws SystemException.
	 */
	public void filter() {
		try {
			String formID = "cleanCashReportForm";
			if (criteria.getDate().after(criteriaMaxDate)) {
				addErrorMessage(formID + ":date", MessageId.GREATER_THAN_CURRENT_DATE);
				return;
			}
			cleanCashMap = cleanCashService.findCleanCashReport(criteria);
			cleanCashList = new ArrayList<CleanCashReport>(cleanCashMap.values());
			if (cleanCashList.size() == 0) {
				addErrorMessage(null, MessageId.NO_RECORDS);
			}
		} catch (SystemException ex) {
			handleSysException(ex);
		}
	}

	/**
	 * Set the selected branch object to branch attribute of criteria.
	 * 
	 * @param SelectEvent
	 *            [Selection event].
	 * 
	 * @return void.
	 */
	public void returnBranch(SelectEvent event) {
		Branch branch = (Branch) event.getObject();
		criteria.setBranch(branch);
	}

	/**
	 * get all currency list for currency selection.
	 * 
	 * @return List [All Currency List].
	 */
	public List<Currency> getCurrencyList() {
		return currencyService.findAllCurrency();
	}

	/**
	 * Configure by home currency or not. If user choose all currencies, it will
	 * be automatically by home currency. If user choose one of currency list,
	 * by home currency will be optional.
	 * 
	 * @return List [All Currency List].
	 */
	public void configHomeCurrency() {
		if (criteria.getCurrency() != null) {
			isAllCurrency = false;
			criteria.setByHome(false);
		} else {
			isAllCurrency = true;
			criteria.setByHome(true);
		}
	}

	/**
	 * If CleanCash List is empty, show no result error message. If CleanCash
	 * List is not empty, show preview to generate pdf report.
	 * 
	 * @return void.
	 * 
	 * @throws Exception[All
	 *             kinds of error].
	 */
	public void previewReport() throws Exception {
		if (cleanCashList.size() == 0) {
			addErrorMessage(null, MessageId.NO_RESULT);
		} else if (generateReport(cleanCashList)) {
			PrimeFaces.current().executeScript("PF('pdfPreviewDialog').show();");
			/*
			 * RequestContext context = RequestContext.getCurrentInstance();
			 * context.execute("PF('pdfPreviewDialog').show();");
			 */
		}
	}

	/**
	 * Generate pdf report with all required data. If user do not choose any
	 * branch, show as all branches. If user do not choose any currency, show as
	 * all currencies by home amount.
	 * 
	 * @param List<CleanCashReport>[cleanCashList].
	 * 
	 * @return boolean[true or false].
	 * 
	 * @throws IOException[Error
	 *             while take input template and Error while generate file
	 *             output], JRException[Jasper Report Error].
	 */
	public boolean generateReport(List<CleanCashReport> cleanCashList) throws IOException, JRException {
		try {
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("cleanCashReport.jrxml");
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("ICON_RSOURCES"));
			Map<String, Object> parameters = new HashMap<String, Object>();
			String branch;
			String currency;
			Date date;
			if (criteria.getBranch() == null) {
				branch = "All Branches";
			} else {
				branch = criteria.getBranch().getName();
			}
			if (criteria.getCurrency() == null) {
				currency = "All Currencies";
			} else {
				currency = criteria.getCurrency().getCurrencyCode();
				if (criteria.isByHome()) {
					currency = currency + " By Home Amount";
				}
			}
			date = criteria.getDate();
			parameters.put("logoPath", image);
			parameters.put("reportDate", new Date());
			parameters.put("branch", branch);
			parameters.put("currency", currency);
			parameters.put("date", date);
			parameters.put("cleanCashList", new JRBeanCollectionDataSource(cleanCashList));
			calculateOpeingAndCloesing();
			parameters.put("cashDebit", openingBalance);
			parameters.put("cashCredit", cloesingBalance);

			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
			String path = getWebRootPath() + dirPath;
			FileUtils.forceMkdir(new File(path));
			JasperExportManager.exportReportToPdfFile(jasperPrint, path + fileName.concat(".pdf"));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage(null, MessageId.REPORT_ERROR);
			return false;
		}
	}

	private BigDecimal openingBalance = BigDecimal.ZERO;
	private BigDecimal cloesingBalance = BigDecimal.ZERO;

	public strictfp void calculateOpeingAndCloesing() {
		List<CashBookDTO> dtoList = new ArrayList<>();
		openingBalance = BigDecimal.ZERO;
		List<CashBookDetail> details = null;
		CashBookCriteria criteria = new CashBookCriteria();
		if (null != this.criteria.getBranch()) {
			criteria.setBranch(this.criteria.getBranch());
		}
		if (null != this.criteria.getCurrency()) {
			criteria.setCurrency(this.criteria.getCurrency());
		} else {
			Currency currency = currencyService.findHomeCurrency();
			criteria.setCurrency(currency);
		}
		if (null != this.criteria.getDate()) {
			criteria.setFromDate(this.criteria.getDate());
		}
		if (null != this.criteria.getDate()) {
			criteria.setToDate(this.criteria.getDate());
		}
		if (this.criteria.isByHome()) {
			criteria.setCurrencyType(CurrencyType.HOMECURRENCY);
		} else {
			criteria.setCurrencyType(CurrencyType.SOURCECURRENCY);
		}
		dtoList = listingReportService.findCashbookListingReport(criteria);
		if (dtoList.size() > 0) {
			openingBalance = dtoList.get(0).getOpeningBalance();
		}
		cloesingBalance = openingBalance;
		for (CashBookDTO cashBook : dtoList) {
			details = cashBook.getDetails();
			for (CashBookDetail detail : details) {
				cloesingBalance = (cloesingBalance.subtract(detail.getDebit())).add(detail.getCredit());
			}
		}
	}

	public StreamedContent getDownload() {
		filter();
		StreamedContent result = null;
		if (cleanCashList.size() == 0) {
			addErrorMessage(null, MessageId.NO_RESULT);
		} else {
			result = getDownloadValue(cleanCashList);
		}
		return result;
	}

	private StreamedContent getDownloadValue(List<CleanCashReport> cleanCashList) {

		try {
			List<JasperPrint> prints = new ArrayList<JasperPrint>();
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("cleanCashReport.jrxml");
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("ICON_RSOURCES"));
			Map<String, Object> parameters = new HashMap<String, Object>();
			String branch;
			String currency;
			Date date;
			if (criteria.getBranch() == null) {
				branch = "All Branches";
			} else {
				branch = criteria.getBranch().getName();
			}
			if (criteria.getCurrency() == null) {
				currency = "All Currencies";
			} else {
				currency = criteria.getCurrency().getCurrencyCode();
				if (criteria.isByHome()) {
					currency = currency + " By Home Amount";
				}
			}
			date = criteria.getDate();
			parameters.put("logoPath", image);
			parameters.put("reportDate", new Date());
			parameters.put("branch", branch);
			parameters.put("currency", currency);
			parameters.put("date", date);
			parameters.put("cleanCashList", new JRBeanCollectionDataSource(cleanCashList));
			calculateOpeingAndCloesing();
			parameters.put("cashDebit", openingBalance);
			parameters.put("cashCredit", cloesingBalance);

			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
			prints.add(jasperPrint);

			FileUtils.forceMkdir(new File(dirPath));

			File destFile = new File(dirPath + fileName.concat(".xls"));

			JRXlsExporter exporter = new JRXlsExporter();

			exporter.setExporterInput(SimpleExporterInput.getInstance(prints));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			// configuration.setOnePagePerSheet(true);
			configuration.setDetectCellType(true);
			configuration.setIgnoreCellBorder(false);
			configuration.setAutoFitPageHeight(true);
			configuration.setCollapseRowSpan(true);
			configuration.setFontSizeFixEnabled(true);
			configuration.setColumnWidthRatio(1.5F);

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

	/**
	 * Get full file path.
	 * 
	 * @return String[full file path with file name].
	 * 
	 */
	public String getStream() {
		String fileFullName = dirPath + fileName.concat(".pdf");
		return fileFullName;
	}

	/**
	 * Get Cash Debit Balance.
	 * 
	 * @return BigDecimal[Amount of cash debit balance].
	 * 
	 */
	public BigDecimal getCashDebitBalance() {
		BigDecimal result = BigDecimal.ZERO;
		// take from database
		return result;
	}

	/**
	 * Get Cash Credit Balance.
	 * 
	 * @return BigDecimal[Amount of cash credit balance].
	 * 
	 */
	public BigDecimal getCashCreditBalance() {
		BigDecimal result = BigDecimal.ZERO;
		// take from database
		return result;
	}

	// Getter.
	public User getUser() {
		return user;
	}

	// Setter.
	public void setUser(User user) {
		this.user = user;
	}

	// Getter.
	public List<CleanCashReport> getCleanCashList() {
		return cleanCashList;
	}

	// Setter.
	public void setCleanCashList(List<CleanCashReport> cleanCashList) {
		this.cleanCashList = cleanCashList;
	}

	// Getter.
	public ReportCriteria getCriteria() {
		return criteria;
	}

	// Setter.
	public void setCriteria(ReportCriteria criteria) {
		this.criteria = criteria;
	}

	// Getter.
	public Date getCriteriaMaxDate() {
		return criteriaMaxDate;
	}

	// Setter.
	public void setCriteriaMaxDate(Date criteriaMaxDate) {
		this.criteriaMaxDate = criteriaMaxDate;
	}

	// Getter.
	public boolean isAllCurrency() {
		return isAllCurrency;
	}

	// Setter.
	public void setAllCurrency(boolean isAllCurrency) {
		this.isAllCurrency = isAllCurrency;
	}

	public List<Branch> getBranches() {
		return branchService.findAllBranch();
	}

}
