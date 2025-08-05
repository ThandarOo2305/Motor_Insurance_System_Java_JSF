package org.ace.accounting.web.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.MonthNames;
import org.ace.accounting.common.PropertiesManager;
import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.report.incomeandexpenditure.IncomeAndExpenditureDto;
import org.ace.accounting.report.incomeandexpenditure.IncomeExpenseCriteria;
import org.ace.accounting.report.incomeandexpenditure.service.interfaces.IIncomeAndExpenditureService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.accounting.user.User;
import org.ace.java.web.common.BaseBean;
import org.apache.commons.io.FileUtils;
import org.primefaces.PrimeFaces;
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
 * @Date 2016-04-25
 * @author PPA
 * @Version 1.0
 * @Purpose This class serves as presentation layer to manipulate
 *          <code>IncomeAndExpenditure</code> Income and Expenditure Report
 *          process.
 * @CopyRight ACEPLUS SOLUTIONS CO., Ltd.
 ***************************************************************************************/

@ViewScoped
@ManagedBean(name = "IncomeAndExpenditureActionBean")
public class IncomeAndExpenditureActionBean extends BaseBean {
	@ManagedProperty(value = "#{PropertiesManager}")
	private PropertiesManager propertiesManager;

	public void setPropertiesManager(PropertiesManager propertiesManager) {
		this.propertiesManager = propertiesManager;
	}

	@ManagedProperty("#{IncomeAndExpenditureService}")
	private IIncomeAndExpenditureService incomeAndExpenditureService;

	public void setIncomeAndExpenditureService(IIncomeAndExpenditureService incomeAndExpenditureService) {
		this.incomeAndExpenditureService = incomeAndExpenditureService;
	}

	@ManagedProperty(value = "#{CurrencyService}")
	private ICurrencyService currencyService;

	public void setCurrencyService(ICurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	@ManagedProperty(value = "#{BranchService}")
	private IBranchService branchService;

	public void setBranchService(IBranchService branchService) {
		this.branchService = branchService;
	}

	private List<Integer> yearList;
	// Month Names Enum Set
	private EnumSet<MonthNames> monthSet;
	// IncomeAndExpenditure List
	private List<IncomeAndExpenditureDto> incomeAndExpenseList;
	// Criteria for user choice.
	private IncomeExpenseCriteria criteria;

	private boolean isBranchDisabled;

	// path for jrxml template.
	private String dirPath = "/pdf-report/" + "incomeAndExpenditureReport" + "/" + System.currentTimeMillis() + "/";
	// pdf name.
	private final String fileName = "incomeAndExpenditureReport";

	/**
	 * Get Current User for the system. Load all of Month Names Enum to EnumSet.
	 * System Current Year and Month is set to criteria. Currency type of
	 * criteria is set home currency as default. When page load, currency will
	 * be all and according to by home Currency Converted.
	 * 
	 * @return void.
	 */
	@PostConstruct
	public void init() {
		yearList = DateUtils.getActiveYears();
		monthSet = EnumSet.allOf(MonthNames.class);
		criteria = new IncomeExpenseCriteria();
		Calendar now = Calendar.getInstance();
		int maxYear = now.get(Calendar.YEAR);
		int maxMonth = now.get(Calendar.MONTH);
		criteria.setQuarterly(false);
		criteria.setQuarter(1);
		criteria.setYear(maxYear);
		criteria.setMonth(maxMonth);
		criteria.setCurrencyType(CurrencyType.HOMECURRENCY);
		criteria.setHomeCur(true);
		criteria.setHomeConverted(true);
		User user = (User) getParam("LoginUser");
		if (user.isAdmin()) {
			isBranchDisabled = false;
		} else {
			isBranchDisabled = true;
			criteria.setBranch(user.getBranch());
		}
	}

	/**
	 * Get Enum Currrency Type.
	 * 
	 * @return CurrencyType Array.
	 */
	public CurrencyType[] getCurrencyTypes() {
		return CurrencyType.values();
	}

	/**
	 * Change home currency and source currency.
	 * 
	 * @param AjaxBehaviorEvent
	 * 
	 * @return void.
	 */
	public void changeCurrencyType(AjaxBehaviorEvent event) {
		if (criteria.getCurrencyType().equals(CurrencyType.HOMECURRENCY)) {
			criteria.setHomeCur(true);
			criteria.setHomeConverted(true);
			criteria.setCurrency(null);
		} else {
			criteria.setHomeCur(false);
			criteria.setHomeConverted(false);
		}
	}

	/**
	 * Get all Currency List.
	 * 
	 * @return List<Currency>.
	 */
	public List<Currency> getCurrencyList() {
		return currencyService.findAllCurrency();
	}

	/**
	 * Get all Branch List.
	 * 
	 * @return List<Branch>.
	 */
	public List<Branch> getBranchList() {
		return branchService.findAllBranch();
	}

	/**
	 * Preview Data with dialog for Report.
	 * 
	 * @return void.
	 * @throws Exception.
	 */
	public void previewReport() throws Exception {
		incomeAndExpenseList = incomeAndExpenditureService.findIncomeExpense(criteria);
		printReport();
	}

	/**
	 * Preview Data with pdf format for Report.
	 * 
	 * @return void.
	 * @throws Exception.
	 */
	public void printReport() throws Exception {
		if (incomeAndExpenseList.size() == 0) {
			addErrorMessage(null, MessageId.NO_RESULT);
		} else if (generateReport(incomeAndExpenseList)) {
			PrimeFaces.current().executeScript("PF('IEPdfDialog').show();");
			/*
			 * RequestContext context = RequestContext.getCurrentInstance();
			 * context.execute("PF('IEPdfDialog').show();");
			 */
		}
	}

	/**
	 * Generate pdf file for Report.
	 * 
	 * @return boolean.
	 * @param List<IncomeAndExpenditure>.
	 * @throws IOException,
	 *             JRException.
	 */
	public boolean generateReport(List<IncomeAndExpenditureDto> incomeAndExpenseList) throws IOException, JRException {
		try {
			InputStream inputStream = null;
			if (!criteria.isQuarterly()) {
				inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("incomeAndExpenditureReport.jrxml");
			} else {
				if (criteria.getQuarter() == 1) {
					inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("incomeAndExpenditureReport3M.jrxml");
				} else if (criteria.getQuarter() == 2) {
					inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("incomeAndExpenditureReport6M.jrxml");
				} else if (criteria.getQuarter() == 3) {
					inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("incomeAndExpenditureReport9M.jrxml");
				} else if (criteria.getQuarter() == 4) {
					inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("incomeAndExpenditureReport12M.jrxml");
				}
			}
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("ICON_RSOURCES"));
			Map<String, Object> parameters = new HashMap<String, Object>();
			String branch;
			String currency;
			if (criteria.getBranch() == null) {
				branch = "All Branches";
			} else {
				branch = criteria.getBranch().getName();
			}
			if (criteria.getCurrency() == null) {
				currency = "All Currencies";
			} else {
				currency = criteria.getCurrency().getCurrencyCode();
				if (criteria.isHomeConverted()) {
					currency = currency + " By Home Amount";
				}
			}
			String month = null;
			int monthVal = criteria.getMonth();
			month = String.valueOf(MonthNames.values()[monthVal]);
			parameters.put("isGroup", criteria.isGroup());
			parameters.put("logoPath", image);
			parameters.put("reportDate", DateUtils.formatDateToString(new Date()));
			parameters.put("reportTime", DateUtils.formatDateToStringTime(new Date()));
			parameters.put("branch", branch);
			parameters.put("currency", currency);
			parameters.put("year", criteria.getYear());
			parameters.put("month", month);
			if (criteria.isQuarterly()) {
				parameters.put("monthList", Arrays.asList(MonthNames.values()));
				parameters.put("budsmth", BusinessUtil.getBudSmth());
			}

			// TODO FIXME PSH
			/**
			 * Remove this line for 0 Case by PSH
			 */

			if (!criteria.isQuarterly()) {
				incomeAndExpenseList = incomeAndExpenseList.stream().filter(dto -> dto.getYearToDate().compareTo(BigDecimal.ZERO) != 0).collect(Collectors.toList());
			}

			JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(incomeAndExpenseList);
			parameters.put("incomeAndExpenseList", source);

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

	public StreamedContent getDownload() {
		incomeAndExpenseList = incomeAndExpenditureService.findIncomeExpense(criteria);
		StreamedContent result = null;
		if (incomeAndExpenseList.size() == 0) {
			addErrorMessage(null, MessageId.NO_RESULT);
		} else {
			result = getDownloadValue(incomeAndExpenseList);
		}
		return result;
	}

	private StreamedContent getDownloadValue(List<IncomeAndExpenditureDto> incomeAndExpenseList) {
		try {
			List<JasperPrint> prints = new ArrayList<JasperPrint>();
			InputStream inputStream = null;
			if (!criteria.isQuarterly()) {
				inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("incomeAndExpenditureReport.jrxml");
			} else {
				if (criteria.getQuarter() == 1) {
					inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("incomeAndExpenditureReport3M.jrxml");
				} else if (criteria.getQuarter() == 2) {
					inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("incomeAndExpenditureReport6M.jrxml");
				} else if (criteria.getQuarter() == 3) {
					inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("incomeAndExpenditureReport9M.jrxml");
				} else if (criteria.getQuarter() == 4) {
					inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("incomeAndExpenditureReport12M.jrxml");
				}
			}
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("ICON_RSOURCES"));
			Map<String, Object> parameters = new HashMap<String, Object>();
			String branch;
			String currency;
			if (criteria.getBranch() == null) {
				branch = "All Branches";
			} else {
				branch = criteria.getBranch().getName();
			}
			if (criteria.getCurrency() == null) {
				currency = "All Currencies";
			} else {
				currency = criteria.getCurrency().getCurrencyCode();
				if (criteria.isHomeConverted()) {
					currency = currency + " By Home Amount";
				}
			}
			String month = null;
			int monthVal = criteria.getMonth();
			month = String.valueOf(MonthNames.values()[monthVal]);
			parameters.put("isGroup", criteria.isGroup());
			parameters.put("logoPath", image);
			parameters.put("reportDate", DateUtils.formatDateToString(new Date()));
			parameters.put("reportTime", DateUtils.formatDateToStringTime(new Date()));
			parameters.put("branch", branch);
			parameters.put("currency", currency);
			parameters.put("year", criteria.getYear());
			parameters.put("month", month);
			if (criteria.isQuarterly()) {
				parameters.put("monthList", Arrays.asList(MonthNames.values()));
				parameters.put("budsmth", BusinessUtil.getBudSmth());
			}
			JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(incomeAndExpenseList);
			parameters.put("incomeAndExpenseList", source);

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

	/**
	 * Get full file path.
	 * 
	 * @return String[full file path with file name].
	 */
	public String getStream() {
		String fileFullName = dirPath + fileName.concat(".pdf");
		return fileFullName;
	}

	public IncomeExpenseCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(IncomeExpenseCriteria criteria) {
		this.criteria = criteria;
	}

	public List<Integer> getYearList() {
		return yearList;
	}

	public EnumSet<MonthNames> getMonthSet() {
		return monthSet;
	}

	public boolean getIsBranchDisabled() {
		return isBranchDisabled;
	}

	public List<IncomeAndExpenditureDto> getIncomeAndExpenseList() {
		return incomeAndExpenseList;
	}

}
