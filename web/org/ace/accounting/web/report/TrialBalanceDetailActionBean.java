package org.ace.accounting.web.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.MonthNames;
import org.ace.accounting.common.PropertiesManager;
import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.TrialBalanceCriteriaDto;
import org.ace.accounting.dto.TrialBalanceReportDto;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.report.TrialBalanceAccountType;
import org.ace.accounting.report.trialBalanceDetail.service.interfaces.ITrialBalanceDetailService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.java.component.ErrorCode;
import org.ace.java.component.SystemException;
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

@ManagedBean(name = "TrialBalanceDetailActionBean")
@ViewScoped
public class TrialBalanceDetailActionBean extends BaseBean {

	@ManagedProperty(value = "#{PropertiesManager}")
	private PropertiesManager propertiesManager;

	public void setPropertiesManager(PropertiesManager propertiesManager) {
		this.propertiesManager = propertiesManager;
	}

	@ManagedProperty(value = "#{TrialBalanceDetailService}")
	private ITrialBalanceDetailService trialBalanceService;

	public void setTrialBalanceService(ITrialBalanceDetailService trialBalanceService) {
		this.trialBalanceService = trialBalanceService;
	}

	@ManagedProperty(value = "#{UserProcessService}")
	private IUserProcessService userProcessService;

	public void setUserProcessService(IUserProcessService userProcessService) {
		this.userProcessService = userProcessService;
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

	@ManagedProperty(value = "#{CcoaService}")
	private ICcoaService ccoaService;

	public void setCcoaService(ICcoaService ccoaService) {
		this.ccoaService = ccoaService;
	}

	private TrialBalanceCriteriaDto trialBalanceCriteriaDto;
	private List<TrialBalanceReportDto> reportResultList;
	private List<Currency> currencyList;
	private List<Branch> branchList;
	// private EnumSet<MonthNames> monthSet;
	private List<MonthNames> monthSet;
	private List<Integer> yearList;
	private boolean isBranchDiabled = true;
	private List<String> budgetYearList;
	private String budgetYear;
	private boolean isBeforeBudgetEnd;
	private boolean isBeforeDisabled;
	private Date postingDate;

	@PostConstruct
	public void init() {
		// monthSet = EnumSet.allOf(MonthNames.class);
		monthSet = Arrays.asList(MonthNames.values());
		yearList = DateUtils.getActiveYears();
		isBeforeDisabled = true;
		bindBudgetYear();
		createNewTrialBalance();
	}

	public void createNewTrialBalance() {
		branchList = branchService.findAllBranch();
		currencyList = currencyService.findAllCurrency();

		trialBalanceCriteriaDto = new TrialBalanceCriteriaDto();
		if (userProcessService.getLoginUser().isAdmin()) {
			trialBalanceCriteriaDto.setBranch(null);
			isBranchDiabled = false;
		} else {
			trialBalanceCriteriaDto.setBranch(userProcessService.getLoginUser().getBranch());
			isBranchDiabled = true;
		}
		trialBalanceCriteriaDto.setCurrencyType(CurrencyType.HOMECURRENCY);
		trialBalanceCriteriaDto.setAccountType(TrialBalanceAccountType.Gl_ACODE);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		trialBalanceCriteriaDto.setRequiredMonth(cal.get(Calendar.MONTH));
		trialBalanceCriteriaDto.setRequiredYear(cal.get(Calendar.YEAR));

	}

	public void bindBudgetYear() {
		budgetYearList = ccoaService.findAllBudgetYear();
	}

	public void changeBudgetYear() {
		String currentBudget;

		currentBudget = BusinessUtil.getCurrentBudget();
		if (!budgetYear.equals(currentBudget)) {
			isBeforeDisabled = false;
			isBeforeBudgetEnd = true;
		} else {
			isBeforeDisabled = true;
			isBeforeBudgetEnd = false;
		}
		monthSet = bindMonth();
		yearList = getActiveYears();

	}

	public List<MonthNames> bindMonth() {

		Date budgetEndDate;
		Date budgetStartDate;
		String currentBudget;

		currentBudget = BusinessUtil.getCurrentBudget();
		if (budgetYear.equals(currentBudget)) {
			budgetEndDate = BusinessUtil.getBudgetEndDate();
			budgetStartDate = BusinessUtil.getBudgetStartDate();
		} else {
			budgetEndDate = BusinessUtil.getPrevBudgetEndDate(budgetYear);
			budgetStartDate = BusinessUtil.getPrevBudgetStartDate(budgetYear);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(budgetStartDate);
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(budgetEndDate);
		List<MonthNames> list1 = new ArrayList<MonthNames>();
		int x = 0;

		for (MonthNames mth : MonthNames.values()) {
			x++;
			if (x >= cal.get(Calendar.MONTH) + 1) {
				list1.add(mth);
			}
		}
		x = 0;
		for (MonthNames mth : MonthNames.values()) {
			x++;
			if (x <= cal1.get(Calendar.MONTH) + 1) {
				list1.add(mth);
			}
		}
		return list1;
	}

	public List<Integer> getActiveYears() {
		List<Integer> yearList = new ArrayList<>();
		int startYear = 3000;
		int endYear = 0;

		// TODO - two records in prev_ccoa has no budget
		if (budgetYear != null) {
			if (Integer.parseInt(budgetYear.split("/")[0]) < startYear) {
				yearList.add(Integer.parseInt(budgetYear.split("/")[0]));
			}
			if (Integer.parseInt(budgetYear.split("/")[0]) > endYear) {
				yearList.add(Integer.parseInt(budgetYear.split("/")[1]));
			}
		}

		/*
		 * for (int i = startYear; i <= endYear; i++) { yearList.add(i); } // if
		 * the current budget year is 2015/2016 , then endyear is 2015 //
		 * following handle the 2016 if the year is already 2016 but budget //
		 * year is still 2015/2016 Calendar cal = Calendar.getInstance();
		 * cal.setTime(new Date()); if (endYear < cal.get(Calendar.YEAR))
		 * yearList.add(endYear + 1);
		 */
		return yearList;
	}

	public void previewReport() {
		trialBalanceCriteriaDto.setBudgetYear(budgetYear);

		if (isBeforeBudgetEnd) {
			reportResultList = trialBalanceService.findTrialBalanceDetailListByClone(trialBalanceCriteriaDto);
		} else {
			reportResultList = trialBalanceService.findTrialBalanceDetailList(trialBalanceCriteriaDto);
		}
		if (reportResultList.size() == 0) {
			addErrorMessage(null, MessageId.NO_RESULT);
		} else
			try {
				if (generateReport(reportResultList)) {
					PrimeFaces.current().executeScript("PF('TBPdfDialog').show();");
					/*
					 * RequestContext context =
					 * RequestContext.getCurrentInstance();
					 * context.execute("PF('TBPdfDialog').show();");
					 */
				}
			} catch (Exception e) {
				handleException(e);
			}
	}

	public boolean generateReport(List<TrialBalanceReportDto> reportResultList2) throws IOException, JRException {
		try {
			InputStream inputStream = null;
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("trialBalanceReport.jrxml");
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("ICON_RSOURCES"));
			Map<String, Object> parameters = new HashMap<String, Object>();
			String branch;
			String currency;
			if (trialBalanceCriteriaDto.getBranch() == null) {
				branch = "All Branches";
			} else {
				branch = trialBalanceCriteriaDto.getBranch().getName();
			}
			if (trialBalanceCriteriaDto.getCurrency() == null) {
				currency = "All Currencies";
			} else {
				currency = trialBalanceCriteriaDto.getCurrency().getCurrencyCode();
				if (trialBalanceCriteriaDto.isHomeCurrencyConverted()) {
					currency = currency + " By Home Amount";
				}
			}
			String month = null;
			int monthVal = trialBalanceCriteriaDto.getRequiredMonth();
			month = String.valueOf(MonthNames.values()[monthVal]);
			parameters.put("logoPath", image);
			parameters.put("reportDate", DateUtils.formatDateToString(new Date()));
			parameters.put("reportTime", DateUtils.formatDateToStringTime(new Date()));
			parameters.put("branch", branch);
			parameters.put("currency", currency);
			parameters.put("year", trialBalanceCriteriaDto.getRequiredYear());
			parameters.put("month", month);
			JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(reportResultList2);
			parameters.put("datasource", source);

			BigDecimal totalMC = BigDecimal.ZERO;
			BigDecimal totalMD = BigDecimal.ZERO;
			BigDecimal totalC = BigDecimal.ZERO;
			BigDecimal totalD = BigDecimal.ZERO;
			for (TrialBalanceReportDto dto : reportResultList2) {
				totalMC = totalMC.add(dto.getmCredit());
				totalMD = totalMD.add(dto.getmDebit());
				totalC = totalC.add(dto.getCredit());
				totalD = totalD.add(dto.getDebit());
			}

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
		//reportResultList.clear();
		trialBalanceCriteriaDto.setBudgetYear(budgetYear);
		StreamedContent result = null;
		if (isBeforeBudgetEnd) {
			reportResultList = trialBalanceService.findTrialBalanceDetailListByClone(trialBalanceCriteriaDto);
		} else {
			reportResultList = trialBalanceService.findTrialBalanceDetailList(trialBalanceCriteriaDto);
		}

		if (reportResultList.size() == 0) {
			addErrorMessage(null, MessageId.NO_RESULT);
		} else {
			// result = getDownloadValue(reportResultList);
			exportExcel(reportResultList);
		}
		return result;
	}
	
	public void exportExcel(List<TrialBalanceReportDto> reportResultList2) {
		ExternalContext ec = getFacesContext().getExternalContext();
		ec.responseReset();
		ec.setResponseContentType("application/vnd.ms-excel");
		String dirPath = "/report-template/";
		String fileName = "TrialBalance_Report.xlsx";
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		try {
			OutputStream op = ec.getResponseOutputStream();
			TrialBalanceDetailReportExcel trialBalanceDetailReportExcel = new TrialBalanceDetailReportExcel();
			trialBalanceDetailReportExcel.generate(op, reportResultList2, trialBalanceCriteriaDto, postingDate);
			getFacesContext().responseComplete();
		} catch (IOException e) {
			throw new SystemException(ErrorCode.SYSTEM_ERROR, "Failed to export TrialBalance_Report.xlsx", e);
		}
	}

	private StreamedContent getDownloadValue(List<TrialBalanceReportDto> reportResultList2) {
		try {
			List<JasperPrint> prints = new ArrayList<JasperPrint>();
			InputStream inputStream = null;
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("trialBalanceExcelReport.jrxml");
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("ICON_RSOURCES"));
			Map<String, Object> parameters = new HashMap<String, Object>();
			String branch;
			String currency;
			if (trialBalanceCriteriaDto.getBranch() == null) {
				branch = "All Branches";
			} else {
				branch = trialBalanceCriteriaDto.getBranch().getName();
			}
			if (trialBalanceCriteriaDto.getCurrency() == null) {
				currency = "All Currencies";
			} else {
				currency = trialBalanceCriteriaDto.getCurrency().getCurrencyCode();
				if (trialBalanceCriteriaDto.isHomeCurrencyConverted()) {
					currency = currency + " By Home Amount";
				}
			}
			String month = null;
			int monthVal = trialBalanceCriteriaDto.getRequiredMonth();
			month = String.valueOf(MonthNames.values()[monthVal]);
			parameters.put("logoPath", image);
			parameters.put("reportDate", DateUtils.formatDateToString(new Date()));
			parameters.put("reportTime", DateUtils.formatDateToStringTime(new Date()));
			parameters.put("branch", branch);
			parameters.put("currency", currency);
			parameters.put("year", trialBalanceCriteriaDto.getRequiredYear());
			parameters.put("month", month);
			JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(reportResultList2);
			parameters.put("datasource", source);

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

	public TrialBalanceCriteriaDto getTrialBalanceDto() {
		return trialBalanceCriteriaDto;
	}

	public List<Currency> getCurrencyList() {
		return currencyList;
	}

	public List<Branch> getBranchList() {
		return branchList;
	}

	public CurrencyType[] getCurrencyTypes() {
		return CurrencyType.values();
	}

	public TrialBalanceAccountType[] getTrialBalanceAcodeTypes() {
		return TrialBalanceAccountType.values();
	}

	public List<Integer> getYearList() {
		return yearList;
	}

	public List<MonthNames> getMonthSet() {
		return monthSet;
	}

	public boolean isBranchDiabled() {
		return isBranchDiabled;
	}

	public List<String> getBudgetYearList() {
		return budgetYearList;
	}

	public String getBudgetYear() {
		return budgetYear;
	}

	public void setBudgetYear(String budgetYear) {
		this.budgetYear = budgetYear;
	}

	// path for jrxml template.
	private String dirPath = "/pdf-report/" + "trialBalanceReport" + "/" + System.currentTimeMillis() + "/";
	// pdf name.
	private final String fileName = "Trial Balance Report";

	public String getStream() {
		String fileFullName = dirPath + fileName.concat(".pdf");
		return fileFullName;
	}

	public TrialBalanceCriteriaDto getTrialBalanceCriteriaDto() {
		return trialBalanceCriteriaDto;
	}

	public void setTrialBalanceCriteriaDto(TrialBalanceCriteriaDto trialBalanceCriteriaDto) {
		this.trialBalanceCriteriaDto = trialBalanceCriteriaDto;
	}

	public List<TrialBalanceReportDto> getReportResultList() {
		return reportResultList;
	}

	public void setReportResultList(List<TrialBalanceReportDto> reportResultList) {
		this.reportResultList = reportResultList;
	}

	public boolean isBeforeBudgetEnd() {
		return isBeforeBudgetEnd;
	}

	public void setBeforeBudgetEnd(boolean isBeforeBudgetEnd) {
		this.isBeforeBudgetEnd = isBeforeBudgetEnd;
	}

	public boolean isBeforeDisabled() {
		return isBeforeDisabled;
	}

	public void setBeforeDisabled(boolean isBeforeDisabled) {
		this.isBeforeDisabled = isBeforeDisabled;
	}

	public Date getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}
	
	

}
