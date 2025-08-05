package org.ace.accounting.web.system;

import java.io.File;
import java.io.FileInputStream;
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
import java.util.stream.Stream;

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
import org.ace.accounting.dto.GainAndLossDTO;
import org.ace.accounting.dto.MonthlyRateDto;
import org.ace.accounting.dto.RateDTO;
import org.ace.accounting.dto.VoucherDTO;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.chartaccount.AccountType;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.system.chartaccount.service.interfaces.ICoaService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.accounting.system.gainloss.ExchangeConfig;
import org.ace.accounting.system.gainloss.service.interfaces.IExchangeConfigService;
import org.ace.accounting.system.tlf.service.interfaces.ITLFService;
import org.ace.java.component.SystemException;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.ace.java.web.common.BaseBean;
import org.apache.commons.io.FileUtils;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

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

@ManagedBean(name = "RateMonthlyCurActionBean")
@ViewScoped
public class RateMonthlyCurActionBean extends BaseBean {

	@ManagedProperty(value = "#{CurrencyService}")
	private ICurrencyService currencyService;

	public void setCurrencyService(ICurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	@ManagedProperty(value = "#{TLFService}")
	private ITLFService tlfService;

	public void setTlfService(ITLFService tlfService) {
		this.tlfService = tlfService;
	}

	@ManagedProperty(value = "#{DataRepService}")
	private IDataRepService<Currency> dataRepService;

	public void setDataRepService(IDataRepService<Currency> dataRepService) {
		this.dataRepService = dataRepService;
	}

	@ManagedProperty(value = "#{UserProcessService}")
	private IUserProcessService userProcessService;

	public void setUserProcessService(IUserProcessService userProcessService) {
		this.userProcessService = userProcessService;
	}

	@ManagedProperty(value = "#{PropertiesManager}")
	private PropertiesManager propertiesManager;

	public void setPropertiesManager(PropertiesManager propertiesManager) {
		this.propertiesManager = propertiesManager;
	}

	@ManagedProperty(value = "#{CoaService}")
	private ICoaService coaService;

	public void setCoaService(ICoaService coaService) {
		this.coaService = coaService;
	}

	@ManagedProperty(value = "#{BranchService}")
	private IBranchService branchService;

	public void setBranchService(IBranchService branchService) {
		this.branchService = branchService;
	}

	@ManagedProperty(value = "#{ExchangeConfigService}")
	private IExchangeConfigService exchangeService;

	public void setExchangeService(IExchangeConfigService exchangeService) {
		this.exchangeService = exchangeService;
	}

	private MonthlyRateDto currency;
	private List<MonthlyRateDto> currencyList;
	private VoucherDTO voucherDto;
	private RateDTO ratedto;
	private List<RateDTO> rateList;
	private EnumSet<MonthNames> monthSet;
	private List<Currency> currencyListValue;
	private List<Integer> yearList;

	private List<ChartOfAccount> coaList;
	private TreeNode root;
	private TreeNode[] selectedNodes;
	private final String reportName = "rateDifferentReport";
	private final String pdfDirPath = "/pdf-report/" + reportName + "/" + System.currentTimeMillis() + "/";
	private final String dirPath = getWebRootPath() + pdfDirPath;
	private final String fileName = reportName;

	private List<Branch> branchList;

	private GainAndLossDTO gainAndLossDTO;
	private List<GainAndLossDTO> gainAndLossDTOList;
	private List<ExchangeConfig> exchangeConfigList;
	//private List<MonthNames> monthSet;

	@PostConstruct
	public void init() {

		exchangeConfigList = exchangeService.findAllExchangeConfig();
		ratedto = new RateDTO();
		rateList = new ArrayList<>();

		gainAndLossDTO = new GainAndLossDTO();
		gainAndLossDTOList = new ArrayList<>();
		currencyListValue = new ArrayList<>();
		monthSet = EnumSet.allOf(MonthNames.class);
		//monthSet = Arrays.asList(MonthNames.values());
		yearList = DateUtils.getActiveYears();
		root = new DefaultTreeNode("Root", null);
		selectedNodes = null;
		rebindData();
	}

	private void rebindData() {
		currencyList = currencyService.findForeignCurrencyDto();
		currencyListValue = currencyService.findAllCurrency();
		coaList = coaService.findAllCoaByAccountCodeType();
		branchList = branchService.findAllBranch();
		TreeNode node = new DefaultTreeNode("ChartOfAccount", root);
		coaList.forEach(coa -> {
			node.getChildren().add(new DefaultTreeNode(coa, root));
		});
		//monthSet = bindMonth();
	}

	public void deleteCurrency(MonthlyRateDto currency) {
		currency.setAllZero();
	}

	public void deleteAllCurrency() {
		for (MonthlyRateDto cur : currencyList) {
			cur.setAllZero();
		}
	}
	
	public List<MonthNames> bindMonth() {

		Date budgetEndDate;
		Date budgetStartDate;
		String currentBudget;
		int y1 = 0, y2 = 0;

		currentBudget = BusinessUtil.getCurrentBudget();
	    budgetEndDate = BusinessUtil.getBudgetEndDate();
	    budgetStartDate = BusinessUtil.getBudgetStartDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(budgetStartDate);
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(budgetEndDate);
		List<MonthNames> list1 = new ArrayList<MonthNames>();
		int x = 0;
		


		for (MonthNames mth : MonthNames.values()) {
			x++;
			if (x >= cal.get(Calendar.MONTH) + 1) {
				
				if(mth.getValue()> 12){
					
				}
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
	
	
	public List<Integer> getActiveYear() {
		List<Integer> yearList = new ArrayList<>();
		int startYear = 3000;
		int endYear = 0;
		
		String currentBudget = BusinessUtil.getCurrentBudget();

		// TODO - two records in prev_ccoa has no budget
		if (currentBudget != null) {
			if (Integer.parseInt(currentBudget.split("/")[0]) < startYear) {
				yearList.add(Integer.parseInt(currentBudget.split("/")[0]));
			}
			if (Integer.parseInt(currentBudget.split("/")[0]) > endYear) {
				yearList.add(Integer.parseInt(currentBudget.split("/")[1]));
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

	public void search() {
		ratedto.setCoaList(new ArrayList<>());
		if (selectedNodes != null) {
			selectedNodes = Stream.of(selectedNodes).filter(node -> !node.getData().equals("ChartOfAccount")).toArray(TreeNode[]::new);
			for (TreeNode node : selectedNodes) {
				ratedto.getCoaList().add((ChartOfAccount) node.getData());
			}
		}

		// for exchange account
		ChartOfAccount exchangeCode = coaService.findCoaByAcCode("01-017-003");
		RateDTO tempRateDto = new RateDTO();

		tempRateDto.setCurrencyId("ISSYS0210001000000000129032013      ");
		tempRateDto.setBranchId(ratedto.getBranchId());
		tempRateDto.setCoaList(new ArrayList<>());
		tempRateDto.getCoaList().add(exchangeCode);
		RateDTO tempDto = new RateDTO();
		Map<String, String> exchangeMap = new HashMap<>();
		// to load from db
		exchangeMap.put("01-017-003", "01-017-001");

		rateList = tlfService.findRateTLF(ratedto);
		rateList.addAll(tlfService.findRateTLF(tempRateDto));
		for (RateDTO reportDto : rateList) {
			if (reportDto.getAccountType().equals(AccountType.A) || reportDto.getAccountType().equals(AccountType.E)) {
				reportDto.setHomeAmount(reportDto.getHomeDebitAmount().subtract(reportDto.getHomeCreditAmount()));
				// reportDto.setHomeAmount(reportDto.getHomeAmount().abs());
				reportDto.setMonthlyAmount((reportDto.getLocalDebitAmount().subtract(reportDto.getLocalCreditAmount())));
				reportDto.setMonthlyAmount(reportDto.getMonthlyAmount().multiply(BigDecimal.valueOf(reportDto.getCurrencyRate())));
				// reportDto.setMonthlyAmount(reportDto.getMonthlyAmount().abs());
			} else if (reportDto.getAccountType().equals(AccountType.I) || reportDto.getAccountType().equals(AccountType.L)) {
				reportDto.setHomeAmount(reportDto.getHomeCreditAmount().subtract(reportDto.getHomeDebitAmount()));
				// reportDto.setHomeAmount(reportDto.getHomeAmount().abs());
				reportDto.setMonthlyAmount((reportDto.getLocalCreditAmount().subtract(reportDto.getLocalDebitAmount())));
				reportDto.setMonthlyAmount(reportDto.getMonthlyAmount().multiply(BigDecimal.valueOf(reportDto.getCurrencyRate())));
				// reportDto.setMonthlyAmount(reportDto.getMonthlyAmount().abs());
			}
		}
		List<RateDTO> tempList = new ArrayList<>();
		for (String exchange : exchangeMap.keySet()) {
			for (RateDTO reportDto : rateList) {
				if (reportDto.getAccCode().equals(exchange)) {
					tempDto = reportDto;
					for (RateDTO reportDto1 : rateList) {
						if (reportDto1.getAccCode().equals(exchangeMap.get(exchange))) {
							reportDto1.setHomeAmount(reportDto1.getHomeAmount().add(reportDto.getHomeAmount()));
							tempList.add(reportDto);
							tempDto = null;
						}
					}
				}
			}
		}
		for (RateDTO a : tempList) {
			rateList.remove(a);
		}
		rateList.forEach(rate -> {
			rate.setHomeAmount(rate.getHomeAmount().abs());
			rate.setMonthlyAmount(rate.getMonthlyAmount().abs());
			rate.setDifference(rate.getHomeAmount().subtract(rate.getMonthlyAmount()));

		});

	}

	public void filter() {
		gainAndLossDTOList.clear();
		int monthVal = gainAndLossDTO.getReportMonth();
		int reportYear = gainAndLossDTO.getReportYear();
		int budSmth = BusinessUtil.getBudSmth();
		Date budgetEndDate = BusinessUtil.getBudgetEndDate();
		Date budgetStartDate = BusinessUtil.getBudgetStartDate();
		// reportYear = DateUtils.getYearFromDate(budgetEndDate);
		if ((monthVal < (budSmth - 1) && reportYear == DateUtils.getYearFromDate(budgetEndDate))
				|| ((monthVal >= (budSmth - 1)) && reportYear == DateUtils.getYearFromDate(budgetStartDate))) {
			gainAndLossDTOList = tlfService.findGainAndLosttList(gainAndLossDTO);
			String currencyCode = null;
			for (Currency cur : currencyListValue) {
				if (cur.getId().equalsIgnoreCase(gainAndLossDTO.getCurrencyId())) {
					currencyCode = cur.getCurrencyCode();
					break;
				}
			}

			String branchName = null;
			if (null != gainAndLossDTO.getBranchId()) {
				for (Branch branch : branchList) {
					if (branch.getId().equalsIgnoreCase(gainAndLossDTO.getBranchId())) {
						branchName = branch.getName();
					}
				}
			} else {
				branchName = "All Branch";
			}

			for (GainAndLossDTO dto : gainAndLossDTOList) {
				dto.setCurrencyCode(currencyCode);
				dto.setBranchName(branchName);
				ExchangeConfig coaData = exchangeConfigList.stream().filter(coa -> coa.getCoaCode().equalsIgnoreCase(dto.getAcCode())).findFirst().get();
				dto.setAcName(coaData.getAcName());
			}
		} else {
			addErrorMessage(null, MessageId.NO_RESULT);
		}
	}

	public void generateReport() {
		try {
			filter();
			if (gainAndLossDTOList.size() != 0) {
				InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("rateDifferentReport.jrxml");
				Map<String, Object> parameters = new HashMap<String, Object>();
				String month = null;
				int monthVal = gainAndLossDTO.getReportMonth();
				int reportYear = gainAndLossDTO.getReportYear();
				month = String.valueOf(MonthNames.values()[monthVal]);
				/*
				 * int budSmth = BusinessUtil.getBudSmth(); Date budgetEndDate =
				 * BusinessUtil.getBudgetEndDate(); Date budgetStartDate =
				 * BusinessUtil.getBudgetStartDate(); if (monthVal < (budSmth -
				 * 1)) { reportYear = DateUtils.getYearFromDate(budgetEndDate);
				 * } else { reportYear =
				 * DateUtils.getYearFromDate(budgetStartDate); }
				 */
				parameters.put("reportDate", DateUtils.formatDateToString(new Date()));
				// parameters.put("reportYear", DateUtils.getYearFromDate(new
				// Date()));
				parameters.put("reportYear", reportYear);
				String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("LOGO"));
				parameters.put("logoPath", image);
				parameters.put("month", month);
				parameters.put("records", rateList.size());
				parameters.put("RATELIST", new JRBeanCollectionDataSource(gainAndLossDTOList));
				JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
				JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
				FileUtils.forceMkdir(new File(dirPath));
				JasperExportManager.exportReportToPdfFile(jasperPrint, dirPath + fileName.concat(".pdf"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public StreamedContent getDownloadValue() {
		try {
			filter();
			if (gainAndLossDTOList.size() != 0) {
				InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("rateDifferentReport.jrxml");
				Map<String, Object> parameters = new HashMap<String, Object>();
				String month = null;
				int monthVal = gainAndLossDTO.getReportMonth();
				int reportYear = gainAndLossDTO.getReportYear();
				// int reportYear;
				month = String.valueOf(MonthNames.values()[monthVal]);
				/*
				 * int budSmth = BusinessUtil.getBudSmth(); Date budgetEndDate =
				 * BusinessUtil.getBudgetEndDate(); Date budgetStartDate =
				 * BusinessUtil.getBudgetStartDate(); // int budgetMonth =
				 * DateUtils.getMonthFromDate(budgetStartDate); if (monthVal <
				 * (budSmth - 1)) { reportYear =
				 * DateUtils.getYearFromDate(budgetEndDate); } else { reportYear
				 * = DateUtils.getYearFromDate(budgetStartDate); }
				 */
				parameters.put("reportDate", DateUtils.formatDateToString(new Date()));
				parameters.put("reportYear", reportYear);
				String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("LOGO"));
				parameters.put("logoPath", image);
				parameters.put("month", month);
				parameters.put("records", rateList.size());
				parameters.put("RATELIST", new JRBeanCollectionDataSource(gainAndLossDTOList));
				JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
				JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

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
			}

		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage(null, MessageId.REPORT_ERROR);
			return null;
		}
		return null;

	}

	public String getStream() {
		String fullFilePath = pdfDirPath + fileName.concat(".pdf");
		return fullFilePath;

	}

	public void saveMonthlyCurrencyRate() {
		try {
			currencyService.updateAllMonthlyRate(currencyList);
			addInfoMessage(null, MessageId.UPDATE_SUCCESS, "Rate Monthly Currency");
			rebindData();
		} catch (SystemException ex) {
			handleSysException(ex);
		}
	}

	public RateDTO getRatedto() {
		return ratedto;
	}

	public VoucherDTO getVoucherDto() {
		return voucherDto;
	}

	public CurrencyType[] getCurrencyTypes() {
		return CurrencyType.values();
	}

	public List<Branch> getBranchList() {
		return branchList;
	}

	public void setCurrency(MonthlyRateDto currency) {
		this.currency = currency;
	}

	public MonthlyRateDto getCurrency() {
		return currency;
	}

	public List<Currency> getCurrencyListValue() {
		return currencyListValue;
	}

	public List<MonthlyRateDto> getCurrencyList() {
		return currencyList;
	}

	public void setCurrencyList(List<MonthlyRateDto> currencyList) {
		this.currencyList = currencyList;
	}

	public List<RateDTO> getRateList() {
		return rateList;
	}

	public void setRateList(List<RateDTO> rateList) {
		this.rateList = rateList;
	}

	public EnumSet<MonthNames> getMonthSet() {
		return monthSet;
	}
	
	/*
	 * public List<MonthNames> getMonthSet() { return monthSet; }
	 */
	public List<ChartOfAccount> getCoaList() {
		return coaList;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	public GainAndLossDTO getGainAndLossDTO() {
		return gainAndLossDTO;
	}

	public void setGainAndLossDTO(GainAndLossDTO gainAndLossDTO) {
		this.gainAndLossDTO = gainAndLossDTO;
	}

	public List<GainAndLossDTO> getGainAndLossDTOList() {
		return gainAndLossDTOList;
	}

	public List<Integer> getYearList() {
		return yearList;
	}

}
