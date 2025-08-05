package org.ace.accounting.web.listing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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
import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.GenLedgerDto;
import org.ace.accounting.dto.LedgerDto;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.report.LedgerListingReport.service.interfaces.ILedgerListingReportService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
import org.ace.accounting.system.chartaccount.service.interfaces.ICoaService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.accounting.system.setup.persistence.interfaces.ISetup_HistoryDAO;
import org.ace.accounting.system.tlf.service.interfaces.ITLFService;
import org.ace.java.web.common.BaseBean;
import org.apache.commons.io.FileUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

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

@ManagedBean(name = "GenLedgerListActionBean")
@ViewScoped
public class GenLedgerListActionBean extends BaseBean {

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

	@ManagedProperty(value = "#{UserProcessService}")
	private IUserProcessService userProcessService;

	public void setUserProcessService(IUserProcessService userProcessService) {
		this.userProcessService = userProcessService;
	}

	@ManagedProperty(value = "#{CcoaService}")
	private ICcoaService ccoaService;

	public void setCcoaService(ICcoaService ccoaService) {
		this.ccoaService = ccoaService;
	}

	@ManagedProperty(value = "#{CoaService}")
	private ICoaService coaService;

	public void setCoaService(ICoaService coaService) {
		this.coaService = coaService;
	}

	@ManagedProperty(value = "#{TLFService}")
	private ITLFService tlfService;

	public void setTlfService(ITLFService tlfService) {
		this.tlfService = tlfService;
	}

	@ManagedProperty(value = "#{LedgerListingReportService}")
	private ILedgerListingReportService ledgerListingReportService;

	public void setLedgerListingReportService(ILedgerListingReportService ledgerListingReportService) {
		this.ledgerListingReportService = ledgerListingReportService;
	}

	@ManagedProperty(value = "#{Setup_HistoryDAO}")
	private ISetup_HistoryDAO setup_HistoryDAO;

	public void setSetup_HistoryDAO(ISetup_HistoryDAO setup_HistoryDAO) {
		this.setup_HistoryDAO = setup_HistoryDAO;
	}

	public static String formId = "genLedgerListingForm";

	private List<Currency> currencyList;
	private List<ChartOfAccount> coaList;
	private List<Branch> branchList;
	private LedgerDto ledgerDto;
	private ChartOfAccount coa;
	private String budgetYear;

	private String dirPath = "/pdf-report/" + "genLedgerListingReport" + "/" + System.currentTimeMillis() + "/";
	private final String fileName = "General Ledger Listing";

	@PostConstruct
	public void init() {
		createNewGenLedgerList();
		rebindData();
	}

	public void createNewGenLedgerList() {
		ledgerDto = new LedgerDto();
		ledgerDto.setCurrencyType(CurrencyType.HOMECURRENCY);
		ledgerDto.setHomeCurrencyConverted(false);
		ledgerDto.setHomeCurrency(true);
		ledgerDto.setAllBranch(false);
		ledgerDto.setAdmin(userProcessService.getLoginUser().isAdmin());
		ledgerDto.setStartDate(new Date());
		ledgerDto.setEndDate(new Date());
		ledgerDto.setBeforeDisabled(true);
		currencyList = new ArrayList<Currency>();
		coaList = new ArrayList<ChartOfAccount>();
		branchList = new ArrayList<Branch>();

	}

	public void rebindData() {
		currencyList = currencyService.findAllCurrency();
		coaList = coaService.findAllCoaByAccountCodeType();
		branchList = branchService.findAllBranch();
		if (ledgerDto.isAdmin()) {
			ledgerDto.setBranch(branchList.get(0));
		} else {
			ledgerDto.setBranch(branchService.findBranchByBranchCode(userProcessService.getLoginUser().getBranch().getBranchCode()));
			ledgerDto.setAllBranch(false);
		}
	}

	public boolean generateReport(List<GenLedgerDto> dtoList) throws IOException, JRException {
		try {
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("genLedgerListingReport.jrxml");
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("ICON_RSOURCES"));
			Map<String, Object> parameters = new HashMap<String, Object>();
			String currencyString = "";
			if (ledgerDto.getCurrency() == null) {
				currencyString = "All Currencies";
			} else {
				currencyString = ledgerDto.getCurrency().getCurrencyCode();
				if (ledgerDto.isHomeCurrencyConverted()) {
					currencyString = currencyString + " By Home Amount";
				}
			}
			parameters.put("reportTitle", "AcePlus Accounting System");
			parameters.put("logoPath", image);
			parameters.put("reportDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			parameters.put("startDate", ledgerDto.getStartDate());
			parameters.put("endDate", ledgerDto.getEndDate());
			parameters.put("branches", (ledgerDto.isAllBranch() && ledgerDto.isAdmin()) ? "All Branches" : ledgerDto.getBranch().getName());
			// parameters.put("currency",
			// (ledgerDto.getCurrencyType().equals(CurrencyType.HOMECURRENCY)) ?
			// "MMK" : ledgerDto.getCurrency().getCurrencyCode());
			parameters.put("currency", currencyString);
			parameters.put("ledgerList", new JRBeanCollectionDataSource(dtoList));

			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(dtoList));
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

	public StreamedContent getDownloadValue() {
		try {
			List<GenLedgerDto> dtoList;

			if (ledgerDto.isBeforeBudgetEnd()) {
				dtoList = ledgerListingReportService.findGenLedgerListingByClone(ledgerDto, coa);
			} else {
				dtoList = ledgerListingReportService.findGenLedgerListing(ledgerDto, coa);
			}
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("genLedgerListingReport.jrxml");
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("LOGO"));
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("reportTitle", "AcePlus Accounting System");
			parameters.put("logoPath", image);
			parameters.put("reportDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			parameters.put("startDate", ledgerDto.getStartDate());
			parameters.put("endDate", ledgerDto.getEndDate());
			parameters.put("branches", (ledgerDto.isAllBranch() && ledgerDto.isAdmin()) ? "All Branches" : ledgerDto.getBranch().getName());
			parameters.put("currency", (ledgerDto.getCurrencyType().equals(CurrencyType.HOMECURRENCY)) ? "MMK" : ledgerDto.getCurrency().getCurrencyCode());
			parameters.put("ledgerList", new JRBeanCollectionDataSource(dtoList));

			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(dtoList));

			String path = getWebRootPath() + dirPath;

			FileUtils.forceMkdir(new File(path));

			File destFile = new File(path + fileName.concat(".xls"));

			JRXlsExporter exporter = new JRXlsExporter();

			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			// configuration.setOnePagePerSheet(true);
			// configuration.setAutoFitPageHeight(true);
			configuration.setPrintPageWidth(100);
			exporter.setConfiguration(configuration);

			exporter.exportReport();

			StreamedContent download = new DefaultStreamedContent();
			File file = new File(path + fileName.concat(".xls"));
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

	public void previewReport() throws Exception {
		List<GenLedgerDto> list;
		if (validate()) {
			// if (dblBalance.doubleValue() == 0) {
			// addInfoMessage(null, MessageId.OPENING_BALANCE_ERROR);
			// return;
			// }

			if (ledgerDto.isBeforeBudgetEnd()) {
				list = ledgerListingReportService.findGenLedgerListingByClone(ledgerDto, coa);
			} else {
				list = ledgerListingReportService.findGenLedgerListing(ledgerDto, coa);
			}
			if (list.size() == 0) {
				addErrorMessage(null, MessageId.NO_RESULT);
			} else if (generateReport(list)) {
				PrimeFaces.current().executeScript("PF('genLedgerListingPdfDialog').show();");
				/*
				 * RequestContext context = RequestContext.getCurrentInstance();
				 * context.execute("PF('genLedgerListingPdfDialog').show();");
				 */
				// createNewGenLedgerList();
				// rebindData();
			}

		} // end of validate
		ledgerDto.setBranch(branchList.get(0));
	}

	public void changeBudgetYear() {
		String currentBudget;
		currentBudget = BusinessUtil.getCurrentBudget();
		Date reportDate = DateUtils.resetStartDate(ledgerDto.getStartDate());
		if (DateUtils.isIntervalBugetYear(reportDate))
			budgetYear = BusinessUtil.getBudgetInfo(reportDate, 2);
		else
			budgetYear = setup_HistoryDAO.findbudgetyear(reportDate);

		if (!budgetYear.equals(currentBudget)) {
			ledgerDto.setBeforeDisabled(false);
			ledgerDto.setBeforeBudgetEnd(true);
		} else {
			ledgerDto.setBeforeDisabled(true);
			ledgerDto.setBeforeBudgetEnd(false);

		}
		// monthSet = bindMonth();
		// yearList = getActiveYears();

	}

	public void dateSelect(SelectEvent event) {
		changeBudgetYear();
	}

	public void dateChange(AjaxBehaviorEvent event) {
		changeBudgetYear();
	}

	public String getStream() {
		String fullFilePath = dirPath + fileName.concat(".pdf");
		return fullFilePath;
	}

	public boolean validate() {
		boolean valid = true;
		Date todayDate = new Date();
		Date startDate = ledgerDto.getStartDate();
		Date endDate = ledgerDto.getEndDate();

		if (startDate.after(todayDate)) {
			valid = false;
			addInfoMessage(null, MessageId.DATE_EXCEEDED, "StartDate", startDate, todayDate);
		}
		if (endDate.after(todayDate)) {
			valid = false;
			addInfoMessage(null, MessageId.DATE_EXCEEDED, "EndDate", endDate, todayDate);
		}
		if (endDate.before(startDate)) {
			valid = false;
			addInfoMessage(null, MessageId.ENDDATE_INVALID, endDate, startDate);
		}
		return valid;
	}

	public void changeCurrencyType(AjaxBehaviorEvent event) {
		ledgerDto.setHomeCurrency(ledgerDto.getCurrencyType().equals(CurrencyType.HOMECURRENCY));
	}

	public CurrencyType[] getCurrencyTypes() {
		return CurrencyType.values();
	}

	public List<Currency> getCurrencyList() {
		return currencyList;
	}

	public List<ChartOfAccount> getCoaList() {
		return coaList;
	}

	public List<Branch> getBranchList() {
		return branchList;
	}

	public LedgerDto getLedgerDto() {
		return ledgerDto;
	}

	public void setLedgerDto(LedgerDto ledgerDto) {
		this.ledgerDto = ledgerDto;
	}

	public void selectAccountCode(ChartOfAccount coa) {
		this.coa = coa;
	}

	public void setCoa(ChartOfAccount coa) {
		this.coa = coa;
	}

	public ChartOfAccount getCoa() {
		return coa;
	}

	private List<ChartOfAccount> filterCoaList;

	public List<ChartOfAccount> getFilterCoaList() {
		return filterCoaList;
	}

	public void setFilterCoaList(List<ChartOfAccount> filterCoaList) {
		this.filterCoaList = filterCoaList;
	}

	public Date getTodayDate() {
		return new Date();

	}

	public String getBudgetYear() {
		return budgetYear;
	}

	public void setBudgetYear(String budgetYear) {
		this.budgetYear = budgetYear;
	}

}
