package org.ace.accounting.web.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
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
import javax.servlet.ServletContext;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.FormatStyle;
import org.ace.accounting.common.MonthNames;
import org.ace.accounting.common.PropertiesManager;
import org.ace.accounting.common.ReportFormat;
import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.common.utils.StringUtil;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.LeftAndRightDto;
import org.ace.accounting.dto.LiabilitiesACDto;
import org.ace.accounting.dto.ReportFormatDto;
import org.ace.accounting.dto.ReportStatementDto;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.report.ReportType;
import org.ace.accounting.report.reportStatement.service.interfaces.IReportStatementService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.chartaccount.AccountType;
import org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.accounting.system.formatfile.ColType;
import org.ace.accounting.system.formatfile.service.interfaces.IFormatFileService;
import org.ace.accounting.system.setup.service.ISetupService;
import org.ace.java.component.ErrorCode;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.apache.commons.io.FileUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;
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

@ManagedBean(name = "ReportStatementActionBean")
@ViewScoped
public class ReportStatementActionBean extends BaseBean {
	//
	// @ManagedProperty(value = "#{CoaService}")
	// private ICoaService coaService;
	//
	// public void setCoaService(ICoaService coaService) {
	// this.coaService = coaService;
	// }
	@ManagedProperty(value = "#{PropertiesManager}")
	private PropertiesManager propertiesManager;

	public void setPropertiesManager(PropertiesManager propertiesManager) {
		this.propertiesManager = propertiesManager;
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

	@ManagedProperty(value = "#{UserProcessService}")
	private IUserProcessService userProcessService;

	public void setUserProcessService(IUserProcessService userProcessService) {
		this.userProcessService = userProcessService;
	}

	@ManagedProperty(value = "#{FormatFileService}")
	private IFormatFileService formatFileService;

	public void setFormatFileService(IFormatFileService formatFileService) {
		this.formatFileService = formatFileService;
	}

	@ManagedProperty(value = "#{ReportStatementService}")
	private IReportStatementService reportStatementService;

	public void setReportStatementService(IReportStatementService reportStatementService) {
		this.reportStatementService = reportStatementService;
	}

	@ManagedProperty(value = "#{CcoaService}")
	private ICcoaService ccoaService;

	public void setCcoaService(ICcoaService ccoaService) {
		this.ccoaService = ccoaService;
	}

	@ManagedProperty(value = "#{SetupService}")
	private ISetupService setupService;

	public void setSetupService(ISetupService setupService) {
		this.setupService = setupService;
	}

	private boolean isReportTypeSelection;
	private ReportType reportType;

	private boolean isCurrencySelection;
	private CurrencyType currencyType;
	private Currency currency;

	private boolean isReportSelection;
	private boolean isBranchDisabled;
	private Branch branch;
	private int reportMonth;
	private int reportYear;
	// private EnumSet<MonthNames> monthSet;
	private List<MonthNames> monthSet;
	private List<String> mthList;
	private List<Integer> yearList;
	private String heading;
	private ReportFormat reportFormat;
	private FormatStyle formatStyle;
	private ReportFormatDto reportFormatDto;
	private List<ReportFormatDto> reportFormatDtoList = null;
	private List<ReportFormatDto> filteredList = null;
	private List<ReportStatementDto> reportStatementDtoList = null;
	private boolean isIncludeObal;
	private boolean isBeforeBudgetEnd;

	private boolean isPreviewDone = false;
	private boolean isLacSelection;
	private List<LiabilitiesACDto> lAcodeDtos;
	private LiabilitiesACDto plAcode;
	private LiabilitiesACDto taxAcode;
	private List<String> budgetYearList;
	private String budgetYear;
	private boolean isInterval;
	private boolean isBeforeDisabled;
	private ReportStatementDto reportStatementDto;

	private final String reportName = "ReportStatement";
	// private final String pdfDirPath = "/pdf-report/" + reportName + "/" +
	// System.currentTimeMillis() + "/";
	private String dirPath = "/pdf-report/" + "ReportStatement" + "/" + System.currentTimeMillis() + "/";
	private final String fileName = reportName;

	@PostConstruct
	public void init() {
		Date budgetEndDate = BusinessUtil.getBudgetEndDate();
		Date budgetStartDate = BusinessUtil.getBudgetStartDate();
		int month = DateUtils.monthsBetween(budgetStartDate, budgetEndDate, false, false) + 1;

		monthSet = new ArrayList<MonthNames>();
		monthSet = Arrays.asList(MonthNames.values());
		yearList = DateUtils.getActiveYears();
		reportType = ReportType.PL;
		isReportTypeSelection = true;
		isBeforeDisabled = true;
		bindBudgetYear();

	}

	public void selectReportType() {
		currencyType = CurrencyType.HOMECURRENCY;
		isCurrencySelection = true;
		isReportTypeSelection = false;
		isReportSelection = false;
		createNewReport();
	}

	public void selectCurrency() {
		isReportSelection = true;
		isCurrencySelection = false;
		isReportTypeSelection = false;
	}

	public void back() {
		if (isCurrencySelection) {
			reportType = ReportType.PL;
			isReportTypeSelection = true;
			isCurrencySelection = false;
			isReportSelection = false;
		} else if (isReportSelection) {
			if (isPreviewDone && reportType.equals(ReportType.PL)) {
				isLacSelection = true;
				isReportTypeSelection = false;
				isCurrencySelection = false;
				isReportSelection = false;
				lAcodeDtos = new ArrayList<>();
				for (ReportStatementDto dto : reportStatementDtoList) {
					if (dto.getAcType() != null && dto.getAcType().equals(AccountType.L)) {
						LiabilitiesACDto lacDto = new LiabilitiesACDto(dto.getAcCode(), dto.getdCode());
						if (!lAcodeDtos.contains(lacDto)) {
							lAcodeDtos.add(lacDto);
						}
					}
					if (dto.getRacType() != null && dto.getRacType().equals(AccountType.L)) {
						LiabilitiesACDto lacDto = new LiabilitiesACDto(dto.getrAcCode(), dto.getdCode());
						if (!lAcodeDtos.contains(lacDto)) {
							lAcodeDtos.add(lacDto);
						}
					}
				}
				lAcodeDtos.sort(new Comparator<LiabilitiesACDto>() {
					@Override
					public int compare(LiabilitiesACDto dto1, LiabilitiesACDto dto2) {
						String acCode1 = dto1.getAcCode().toUpperCase();
						String acCode2 = dto2.getAcCode().toUpperCase();
						return acCode1.compareTo(acCode2);
					}
				});
				if (lAcodeDtos.size() == 0) {
					isLacSelection = false;
					reportType = ReportType.PL;
					isReportTypeSelection = true;
					addErrorMessage(null, MessageId.NO_SUITABLE_ACCOUNT);
				}
			} else {
				isCurrencySelection = true;
				currencyType = CurrencyType.HOMECURRENCY;
				currency = null;
				isReportTypeSelection = false;
				isReportSelection = false;
			}
		} else if (isLacSelection) {
			reportType = ReportType.PL;
			isReportTypeSelection = true;
			isCurrencySelection = false;
			isLacSelection = false;
			isReportSelection = false;
			isPreviewDone = false;
			reportStatementDtoList = null;
		}

	}

	public void preview() {
		Calendar cal1 = Calendar.getInstance();
		cal1.set(reportYear, reportMonth, 1);
		if (validate()) {
			try {
				Calendar cal = Calendar.getInstance();
				cal.set(reportYear, reportMonth, 1);
				Date reportDate = cal.getTime();
				isInterval = DateUtils.isIntervalBugetYear(reportDate);
				if (isBeforeBudgetEnd) {
					reportStatementDtoList = reportStatementService.previewProcedureForCloneData(isIncludeObal,
							reportType, currencyType, currency, branch, reportDate, budgetYear,
							reportFormatDto.getFormatType());
				} else {
					if (isInterval) {

						reportStatementDtoList = reportStatementService.previewProcedure(isIncludeObal, reportType,
								currencyType, currency, branch, reportDate, reportFormatDto.getFormatType());
					} else {
						reportStatementDtoList = reportStatementService.prevPreviewProcedure(isIncludeObal, reportType,
								currencyType, currency, branch, reportDate, budgetYear,
								reportFormatDto.getFormatType());
					}
				}
				/*
				 * if (reportType.equals(ReportType.BS)) { reportStatementDtoList =
				 * reportStatementService.liabilitiesAcodeCheck(plAcode, taxAcode,
				 * reportStatementDtoList); }
				 */
				generateReport(reportStatementDtoList);
				PrimeFaces.current().executeScript("PF('reportStatementPdfDialog').show();");
				isPreviewDone = true;
			} catch (SystemException se) {
				handleSysException(se);
			} catch (Exception e) {
				handleException(e);
			}
		}
	}

	public void submitLAC() {
		// TODO -- to consult
		// here , taxAcode and plAcode dtos just have to hold the cbal for BS
		// reports
		// -----
		// get the first ever match instead of total throughout the list
		// even though there are duplicate acode in same formattype
		// try following query
		/*
		 * select * from ( SELECT FORMATSTATUS,FORMATTYPE,ACODE,COUNT(ACODE) as c FROM
		 * FORMATFILE where ACODE!='' GROUP By FORMATTYPE,ACODE ,FORMATSTATUS )x where
		 * c>1 order by FORMATTYPE
		 */
		// and only match the ACODE ( RACODE not included)
		for (ReportStatementDto dto : reportStatementDtoList) {
			if (plAcode.getAcCode().equals(dto.getAcCode())) {
				plAcode.setcBal(dto.getcBal());
				break;
			}
		}
		// did two time , to get first result for each
		for (ReportStatementDto dto : reportStatementDtoList) {
			if (taxAcode.getAcCode().equals(dto.getAcCode())) {
				taxAcode.setcBal(dto.getcBal());
				break;
			}
		}
		reportType = ReportType.BS;
		isReportTypeSelection = true;
		isCurrencySelection = false;
		isReportSelection = false;
		isPreviewDone = false;
		isLacSelection = false;
		createNewReport();
	}

	private boolean validate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		// if reportYear is greater than current year
		if (reportYear == cal.get(Calendar.YEAR) && reportMonth > cal.get(Calendar.MONTH)) {
			addErrorMessage(null, MessageId.MONTH_EXCEEDED);
		} else {
			return true;
		}
		return false;
	}

	private List<LeftAndRightDto> populateLeftAndRight(List<ReportStatementDto> leftList,
			List<ReportStatementDto> rightList) {
		List<LeftAndRightDto> leftRightList = new ArrayList<>();
		int listSize = leftList.size() >= rightList.size() ? leftList.size() : rightList.size();
		for (int i = 0; i < listSize; i++) {
			LeftAndRightDto dto = new LeftAndRightDto();
			if (i < leftList.size()) {
				dto = populateLeftValue(dto, leftList.get(i));
			}

			if (i < rightList.size()) {
				dto = populateRightValue(dto, rightList.get(i));
			}
			leftRightList.add(dto);
		}
		return leftRightList;
	}

	private LeftAndRightDto populateLeftValue(LeftAndRightDto dto, ReportStatementDto leftDto) {

		dto.setLno(leftDto.getLno());
		dto.setAcCode(leftDto.getAcCode());
		dto.setDesp(leftDto.getDesp());
		dto.setShowHide(leftDto.isShowHide());
		dto.setAmountTotal(leftDto.getAmountTotal());
		dto.setStatus(leftDto.getStatus());
		dto.setAmt(leftDto.getAmt());
		dto.setTotal(leftDto.getTotal());
		dto.setAcType(leftDto.getAcType());
		dto.setColType(leftDto.getColType());
		dto.setcBal(leftDto.getcBal());
		return dto;
	}

	private LeftAndRightDto populateRightValue(LeftAndRightDto dto, ReportStatementDto rightDto) {

		dto.setRlno(rightDto.getRlno());
		dto.setrAcCode(rightDto.getrAcCode());
		dto.setrDesp(rightDto.getrDesp());
		dto.setRightShowHide(rightDto.isShowHide());
		dto.setrAmountTotal(rightDto.getAmountTotal());
		dto.setrStatus(rightDto.getStatus());
		dto.setrAmt(rightDto.getrAmt());
		dto.setrTotal(rightDto.getTotal());
		dto.setRacType(rightDto.getRacType());
		dto.setrColType(rightDto.getColType());
		dto.setrCBal(rightDto.getcBal());
		return dto;
	}

	private void generateReport(List<ReportStatementDto> reportStatementDtoList2) throws Exception {
		try {
			InputStream inputStream;
			if (formatStyle.equals(FormatStyle.VF)) {
				if (isIncludeObal) {
					inputStream = Thread.currentThread().getContextClassLoader()
							.getResourceAsStream("reportStatementReportWithObal.jrxml");
				} else {
					inputStream = Thread.currentThread().getContextClassLoader()
							.getResourceAsStream("reportStatementReport.jrxml");
				}
			} else {
				inputStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("reportStatementReportHorizontal.jrxml");
			}

			// System.out.println((reportStatementDtoList2.get(reportStatementDtoList2.size()-1).getrAcCode()!=null
			// ||
			// reportStatementDtoList2.get(reportStatementDtoList2.size()-1).getrAcCode().equals("")));

			Map<String, Object> parameters = new HashMap<>();
			String newDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			String branchString = branch == null ? branchString = "All Branches" : branch.getName();
			String currencyString = currency == null ? "All Currencies" : currency.getCurrencyCode();
			if (currencyType.equals(CurrencyType.HOMECURRENCY)) {
				currencyString = currencyString + " By Home Amount";
			}
			String image = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath(propertiesManager.getProperties("LOGO"));
			parameters.put("FORMAT_NAME", reportFormatDto.getFormatName());
			parameters.put("BRANCH", branchString);
			parameters.put("CURRENCY", currencyString);
			parameters.put("NEWDATE", newDate);
			parameters.put("LOGO", image);
			parameters.put("REPORT_MONTH", StringUtil.getMonthName(reportMonth + 1) + " - " + reportYear);
			parameters.put("HEADING", heading);

			if (formatStyle.equals(FormatStyle.VF)) {
				if (isIncludeObal) {
					parameters.put("DTOLIST", new JRBeanCollectionDataSource(reportStatementDtoList2.stream()
							// .filter(report -> report.getcBal().doubleValue()
							// != 0.00)
							.collect(Collectors.toList())));
					/* For Left Record */
					// parameters.put("DTOLIST", new
					// JRBeanCollectionDataSource(reportStatementDtoList2.stream().filter(report->null
					// !=
					// report.getColType()).filter(report->report.getColType().equals(ColType.L)).filter(report->report.getcBal().doubleValue()
					// != 0.00).collect(Collectors.toList())));
					// /* For Right Record */
					// parameters.put("RightDTOList", new
					// JRBeanCollectionDataSource(reportStatementDtoList2.stream().filter(report->null
					// !=
					// report.getColType()).filter(report->report.getColType().equals(ColType.R)).filter(report->report.getcBal().doubleValue()
					// != 0.00).collect(Collectors.toList())));
				} else {

					parameters.put("DTOLIST", new JRBeanCollectionDataSource(reportStatementDtoList2.stream()
							// .filter(report -> report.getcBal().doubleValue()
							// != 0.00)
							.collect(Collectors.toList())));

					/* For Left Record */
					// parameters.put("DTOLIST", new
					// JRBeanCollectionDataSource(reportStatementDtoList2.stream()
					// .filter(report -> null != report.getColType())
					// .filter(report -> report.getColType().equals(ColType.L))
					// .filter(report -> report.getcBal().doubleValue() != 0.00)
					// .collect(Collectors.toList())));
					/* For Right Record */
					// parameters.put("RightDTOList", new
					// JRBeanCollectionDataSource(reportStatementDtoList2.stream()
					// .filter(report -> null != report.getColType())
					// .filter(report -> report.getColType().equals(ColType.R))
					// .filter(report -> report.getcBal().doubleValue() != 0.00)
					// .collect(Collectors.toList())));

				}
			} else {
				/* For Left Record */
				List<ReportStatementDto> leftList = reportStatementDtoList2.stream()
						.filter(report -> null != report.getColType())
						.filter(report -> report.getColType().equals(ColType.L))
						// .filter(report -> report.getcBal().doubleValue() !=
						// 0.00)
						.collect(Collectors.toList());
				/* For Right Record */
				List<ReportStatementDto> rightList = reportStatementDtoList2.stream()
						.filter(report -> null != report.getColType())
						.filter(report -> report.getColType().equals(ColType.R))
						// .filter(report -> report.getcBal().doubleValue() !=
						// 0.00)
						.collect(Collectors.toList());
				if (isIncludeObal) {
					parameters.put("DTOLIST", new JRBeanCollectionDataSource(reportStatementDtoList2.stream()
							// .filter(report -> report.getcBal().doubleValue()
							// != 0.00)
							.collect(Collectors.toList())));
				}
				parameters.put("DTOLIST", new JRBeanCollectionDataSource(populateLeftAndRight(leftList, rightList)));

			}

			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
			String path = getWebRootPath() + dirPath;
			FileUtils.forceMkdir(new File(path));
			JasperExportManager.exportReportToPdfFile(jasperPrint, path + fileName.concat(".pdf"));

		} catch (SystemException e) {
			throw e;
		} catch (Exception ee) {
			throw ee;
		}
	}

	protected String getWebRootPath() {
		Object context = getFacesContext().getExternalContext().getContext();
		String systemPath = ((ServletContext) context).getRealPath("/");
		return systemPath;
	}

	public void rebindData() {
		filteredList = null;
		budgetYearList = ccoaService.findAllBudgetYear();
		reportFormatDtoList = formatFileService.findByReportType(reportType);

	}

	public StreamedContent getDownload() {
		StreamedContent result = null;

		if (validate()) {
			try {
				reportStatementDtoList = new ArrayList<>();
				Calendar cal = Calendar.getInstance();
				cal.set(reportYear, reportMonth, 1);
				Date reportDate = cal.getTime();
				isInterval = DateUtils.isIntervalBugetYear(reportDate);
				if (isBeforeBudgetEnd) {
					reportStatementDtoList = reportStatementService.previewProcedureForCloneData(isIncludeObal,
							reportType, currencyType, currency, branch, reportDate, budgetYear,
							reportFormatDto.getFormatType());
				} else {
					if (isInterval) {

						reportStatementDtoList = reportStatementService.previewProcedure(isIncludeObal, reportType,
								currencyType, currency, branch, reportDate, reportFormatDto.getFormatType());

						// List<ReportStatementDto> totalList =
						// reportStatementDtoList.stream().filter(report -> null
						// !=
						// report.getDesp())
						// .filter(report ->
						// report.getDesp().equals("Total")).collect(Collectors.toList());
					} else {
						reportStatementDtoList = reportStatementService.prevPreviewProcedure(isIncludeObal, reportType,
								currencyType, currency, branch, reportDate, budgetYear,
								reportFormatDto.getFormatType());
					}
				}

				if (reportStatementDtoList.isEmpty()) {
					addErrorMessage(null, MessageId.NO_RESULT);
				} else {
					// result = getDownloadValue(reportStatementDtoList);
					exportExcel(reportStatementDtoList);
					rebindData();
				}
			} catch (SystemException se) {
				handleSysException(se);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return result;
	}

	private StreamedContent getDownloadValue(List<ReportStatementDto> dtoList) {

		try {
			List<JasperPrint> prints = new ArrayList<JasperPrint>();
			InputStream inputStream;
			if (formatStyle.equals(FormatStyle.VF)) {
				if (isIncludeObal) {
					inputStream = Thread.currentThread().getContextClassLoader()
							.getResourceAsStream("reportStatementReportWithObal.jrxml");
				} else {
					inputStream = Thread.currentThread().getContextClassLoader()
							.getResourceAsStream("reportStatementReport.jrxml");
				}
			} else {
				inputStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("reportStatementReportHorizontal.jrxml");
			}
			Map<String, Object> parameters = new HashMap<String, Object>();
			String newDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			String branchString = branch == null ? branchString = "All Branches" : branch.getName();
			String currencyString = currency == null ? "All Currencies" : currency.getCurrencyCode();
			if (currencyType.equals(CurrencyType.HOMECURRENCY)) {
				currencyString = currencyString + " By Home Amount";
			}
			String image = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath(propertiesManager.getProperties("LOGO"));
			parameters.put("FORMAT_NAME", reportFormatDto.getFormatName());
			parameters.put("BRANCH", branchString);
			parameters.put("CURRENCY", currencyString);
			parameters.put("NEWDATE", newDate);
			parameters.put("LOGO", image);
			parameters.put("REPORT_MONTH", StringUtil.getMonthName(reportMonth + 1) + " - " + reportYear);
			parameters.put("HEADING", heading);
			parameters.put("logoPath", image);
			parameters.put("branches", branch);

			// parameters.put("homeCurrency", reportFormatDto

			if (formatStyle.equals(FormatStyle.VF)) {
				if (isIncludeObal) {
					parameters.put("DTOLIST", new JRBeanCollectionDataSource(dtoList.stream()
							// .filter(report -> report.getcBal().doubleValue()
							// != 0.00)
							.collect(Collectors.toList())));
					/* For Left Record */
					// parameters.put("DTOLIST", new
					// JRBeanCollectionDataSource(reportStatementDtoList2.stream().filter(report->null
					// !=
					// report.getColType()).filter(report->report.getColType().equals(ColType.L)).filter(report->report.getcBal().doubleValue()
					// != 0.00).collect(Collectors.toList())));
					// /* For Right Record */
					// parameters.put("RightDTOList", new
					// JRBeanCollectionDataSource(reportStatementDtoList2.stream().filter(report->null
					// !=
					// report.getColType()).filter(report->report.getColType().equals(ColType.R)).filter(report->report.getcBal().doubleValue()
					// != 0.00).collect(Collectors.toList())));
				} else {
					parameters.put("DTOLIST", new JRBeanCollectionDataSource(dtoList.stream()
							// .filter(report -> report.getcBal().doubleValue()
							// != 0.00)
							.collect(Collectors.toList())));

					/* For Left Record */
					// parameters.put("DTOLIST", new
					// JRBeanCollectionDataSource(dtoList.stream()
					// .filter(report -> null != report.getColType())
					// .filter(report -> report.getColType().equals(ColType.L))
					// .filter(report -> report.getcBal().doubleValue() != 0.00)
					// .collect(Collectors.toList())));
					/* For Right Record */
					// parameters.put("RightDTOList", new
					// JRBeanCollectionDataSource(dtoList.stream()
					// .filter(report -> null != report.getColType())
					// .filter(report -> report.getColType().equals(ColType.R))
					// .filter(report -> report.getcBal().doubleValue() != 0.00)
					// .collect(Collectors.toList())));

				}
			} else {
				/* For Left Record */
				List<ReportStatementDto> leftList = dtoList.stream().filter(report -> null != report.getColType())
						.filter(report -> report.getColType().equals(ColType.L))
						// .filter(report -> report.getcBal().doubleValue() !=
						// 0.00)
						.collect(Collectors.toList());
				/* For Right Record */
				List<ReportStatementDto> rightList = dtoList.stream().filter(report -> null != report.getColType())
						.filter(report -> report.getColType().equals(ColType.R))
						// .filter(report -> report.getcBal().doubleValue() !=
						// 0.00)
						.collect(Collectors.toList());
				if (isIncludeObal) {
					parameters.put("DTOLIST", new JRBeanCollectionDataSource(dtoList.stream()
							// .filter(report -> report.getcBal().doubleValue()
							// != 0.00)
							.collect(Collectors.toList())));
				}
				parameters.put("DTOLIST", new JRBeanCollectionDataSource(populateLeftAndRight(leftList, rightList)));

			}

			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
					new JRBeanCollectionDataSource(dtoList));
			prints.add(jasperPrint);

			FileUtils.forceMkdir(new File(dirPath));

			File destFile = new File(dirPath + fileName.concat(".xls"));

			JRXlsExporter exporter = new JRXlsExporter();

			exporter.setExporterInput(SimpleExporterInput.getInstance(prints));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			// configuration.setOnePagePerSheet(false);
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

	public void createNewReport() {
		reportFormat = ReportFormat.AF;
		formatStyle = FormatStyle.HF;
		isIncludeObal = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		reportMonth = cal.get(Calendar.MONTH);
		reportYear = cal.get(Calendar.YEAR);

		budgetYearList = new ArrayList<String>();
		budgetYear = "";
		if (userProcessService.getLoginUser().isAdmin()) {
			isBranchDisabled = false;
			branch = null;
		} else {
			isBranchDisabled = true;
			branch = userProcessService.getLoginUser().getBranch();
		}
		/*
		 * if (!yearList.contains(reportYear)) { reportYear =
		 * yearList.get(yearList.size() - 1); }
		 */
		heading = null;
		reportFormatDto = null;
		rebindData();
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

	public void bindBudgetYear() {
		budgetYearList = ccoaService.findAllBudgetYear();
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
		 * for (int i = startYear; i <= endYear; i++) { yearList.add(i); } // if the
		 * current budget year is 2015/2016 , then endyear is 2015 // following handle
		 * the 2016 if the year is already 2016 but budget // year is still 2015/2016
		 * Calendar cal = Calendar.getInstance(); cal.setTime(new Date()); if (endYear <
		 * cal.get(Calendar.YEAR)) yearList.add(endYear + 1);
		 */
		return yearList;
	}

	public void exportExcel(List<ReportStatementDto> dtoList) {
		ExternalContext ec = getFacesContext().getExternalContext();
		ec.responseReset();
		ec.setResponseContentType("application/vnd.ms-excel");
		String dirPath = "/report-template/";
		String fileName;
		if (formatStyle.equals(FormatStyle.VF)) {
			if (isIncludeObal) {
				fileName = "ReportStatementWithOblReport.xlsx";
			} else {
				fileName = "ReportStatementVerticalReport.xlsx";
			}
		} else {
			fileName = "ReportStatementHorizontalReport.xlsx";
		}
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		String newDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		String headingString = heading;
		String branchString = branch == null ? branchString = "All Branches" : branch.getName();
		String currencyString = currency == null ? "All Currencies" : currency.getCurrencyCode();
		if (currencyType.equals(CurrencyType.HOMECURRENCY)) {
			currencyString = currencyString;
		}
		List<ReportStatementDto> leftdtoList = null;
		List<ReportStatementDto> rightdtoList = null;
		List<LeftAndRightDto> leftrightdtoList = null;
		//
		if (formatStyle.equals(FormatStyle.VF)) {
			if (isIncludeObal) {

				// dtoList = dtoList.stream().collect(Collectors.toList());
				dtoList = dtoList.stream().filter(report -> report.isShowHide() == true).collect(Collectors.toList());

			} else {

				/*
				 * // dtoList = dtoList.stream().collect(Collectors.toList()); For Left Record
				 * leftdtoList = dtoList.stream().filter(report -> null != report.getColType())
				 * .filter(report -> report.getColType().equals(ColType.L)) .filter(report ->
				 * report.isShowHide() == true).collect(Collectors.toList()); For Right Record
				 * rightdtoList = dtoList.stream().filter(report -> null != report.getColType())
				 * .filter(report -> report.getColType().equals(ColType.R)) .filter(report ->
				 * report.isShowHide() == true).collect(Collectors.toList());
				 */
				dtoList = dtoList.stream().filter(report -> report.isShowHide() == true).collect(Collectors.toList());

			}
		} else {
			/*
			 * For Left Record // List<ReportStatementDto> leftdtoList =
			 * dtoList.stream().filter(report -> null != report.getColType()).filter(report
			 * -> report.getColType().equals(ColType.L)) .collect(Collectors.toList()); For
			 * Right Record // List<ReportStatementDto> rightdtoList =
			 * dtoList.stream().filter(report -> null != report.getColType()).filter(report
			 * -> report.getColType().equals(ColType.R)) .collect(Collectors.toList()); For
			 * Both Record List<ReportStatementDto> bothList =
			 * dtoList.stream().filter(report -> null != report.getColType()).filter(report
			 * -> report.getColType().equals(ColType.B)) .collect(Collectors.toList());
			 */

			/* For Left Record */
			// List<ReportStatementDto>
			leftdtoList = dtoList.stream().filter(report -> null != report.getColType())
					.filter(report -> report.getColType().equals(ColType.L))
					.filter(report -> report.isShowHide() == true).collect(Collectors.toList());
			/* For Right Record */
			// List<ReportStatementDto>
			rightdtoList = dtoList.stream().filter(report -> null != report.getColType())
					.filter(report -> report.getColType().equals(ColType.R))
					.filter(report -> report.isShowHide() == true).collect(Collectors.toList());
			/* For Both Record */
			List<ReportStatementDto> bothList = dtoList.stream().filter(report -> null != report.getColType())
					.filter(report -> report.getColType().equals(ColType.B))
					.filter(report -> report.isShowHide() == true).collect(Collectors.toList());

			leftdtoList.addAll(bothList);
			rightdtoList.addAll(bothList);
			if (isIncludeObal) {
				// dtoList = dtoList.stream().collect(Collectors.toList());

				dtoList = dtoList.stream().filter(report -> report.isShowHide() == true).collect(Collectors.toList());
			}
			leftrightdtoList = populateLeftAndRight(leftdtoList, rightdtoList);

		}
		//

		try {
			OutputStream op = ec.getResponseOutputStream();
			ReportStatementReportExcel reportStatementReportExcel = new ReportStatementReportExcel(formatStyle,
					isIncludeObal);

			if (fileName == "ReportStatementWithOblReport.xlsx") {
				reportStatementReportExcel.generateObl(op, dtoList, reportStatementDto, branchString, currencyString,
						headingString);
			}
			if (fileName == "ReportStatementVerticalReport.xlsx") {
				reportStatementReportExcel.generate(op, dtoList, reportStatementDto, branchString, currencyString,
						headingString);
				// reportStatementReportExcel.generate(op, rightdtoList,
				// reportStatementDto, branchString, currencyString,
				// headingString);
			}
			/*
			 * else if (fileName == "ReportStatementHorizontalReport.xlsx" && isIncludeObal)
			 * { reportStatementReportExcel.generateHorizontal(op, dtoList,
			 * reportStatementDto, branchString, currencyString, headingString); }
			 */

			else if (fileName == "ReportStatementHorizontalReport.xlsx") {
				reportStatementReportExcel.generateHorizontalBoth(op, leftrightdtoList, reportStatementDto,
						branchString, currencyString, headingString);
			}

			else {
				reportStatementReportExcel.generateObl(op, dtoList, reportStatementDto, branchString, currencyString,
						headingString);
			}
			getFacesContext().responseComplete();
		} catch (IOException e) {
			throw new SystemException(ErrorCode.SYSTEM_ERROR, "Failed to export ReportStatementVerticalReport.xlsx", e);
		}
	}

	public boolean isReportTypeSelection() {
		return isReportTypeSelection;
	}

	public boolean isCurrencySelection() {
		return isCurrencySelection;
	}

	public boolean isReportSelection() {
		return isReportSelection;
	}

	public List<ReportType> getReportTypes() {
		return Arrays.asList(ReportType.values());
	}

	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public void setCurrencyType(CurrencyType currencyType) {
		this.currencyType = currencyType;
	}

	public CurrencyType getCurrencyType() {
		return currencyType;
	}

	public List<CurrencyType> getCurTypes() {
		return Arrays.asList(CurrencyType.values());
	}

	public List<Currency> getCurrencies() {
		return currencyService.findAllCurrency();
	}

	public List<Branch> getBranches() {
		return branchService.findAllBranch();
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Currency getCurrency() {
		return currency;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public boolean isBranchDisabled() {
		return isBranchDisabled;
	}

	public void setReportFormat(ReportFormat reportFormat) {
		this.reportFormat = reportFormat;
	}

	public ReportFormat getReportFormat() {
		return reportFormat;
	}

	public List<ReportFormat> getReportFormats() {
		return Arrays.asList(ReportFormat.values());
	}

	public boolean isIncludeObal() {
		return isIncludeObal;
	}

	public void setIncludeObal(boolean isIncludeObal) {
		this.isIncludeObal = isIncludeObal;
	}

	public void setFormatStyle(FormatStyle formatStyle) {
		this.formatStyle = formatStyle;
	}

	public FormatStyle getFormatStyle() {
		return formatStyle;
	}

	public List<FormatStyle> getFormatStyles() {
		return Arrays.asList(FormatStyle.values());
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getHeading() {
		return heading;
	}

	public ReportFormatDto getReportFormatDto() {
		return reportFormatDto;
	}

	public void setReportFormatDto(ReportFormatDto dto) {
		this.reportFormatDto = dto;
	}

	public List<ReportFormatDto> getReportFormatDtoList() {
		return reportFormatDtoList == null ? new ArrayList<>() : reportFormatDtoList;
	}

	public void selectFormatFileDto(ReportFormatDto dto) {
		this.reportFormatDto = dto;
	}

	public int getReportMonth() {
		return reportMonth;
	}

	public void setReportMonth(int reportMonth) {
		this.reportMonth = reportMonth;
	}

	public int getReportYear() {
		return reportYear;
	}

	public void setReportYear(int reportYear) {
		this.reportYear = reportYear;
	}

	public List<MonthNames> getMonthSet() {
		return monthSet;
	}

	public List<Integer> getYearList() {
		return yearList;
	}

	public List<String> getMthList() {
		return mthList;
	}

	public boolean getIsLacSelection() {
		return isLacSelection;
	}

	public String getStream() {
		String fullFilePath = dirPath + fileName.concat(".pdf");
		return fullFilePath;
	}

	public List<ReportFormatDto> getFilteredList() {
		return filteredList;
	}

	public void setFilteredList(List<ReportFormatDto> filteredList) {
		this.filteredList = filteredList;
	}

	public List<LiabilitiesACDto> getlAcodeDtos() {
		return lAcodeDtos;
	}

	public void setPlAcode(LiabilitiesACDto plAcode) {
		this.plAcode = plAcode;
	}

	public LiabilitiesACDto getPlAcode() {
		return plAcode;
	}

	public void setTaxAcode(LiabilitiesACDto taxAcode) {
		this.taxAcode = taxAcode;
	}

	public LiabilitiesACDto getTaxAcode() {
		return taxAcode;
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

	public boolean isInterval() {
		return isInterval;
	}

	public void setInterval(boolean isInterval) {
		this.isInterval = isInterval;
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

	public void handleClose(CloseEvent event) {
		try {
			FileUtils.forceDelete(new File(dirPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
