package org.ace.accounting.web.listing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.ace.accounting.dto.AccountLedgerDto;
import org.ace.accounting.dto.AccountLedgerGroupDto;
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
import org.ace.java.component.ErrorCode;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.apache.commons.io.FileUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@ManagedBean(name = "AccLedgerListActionBean")
@ViewScoped
public class AccLedgerListActionBean extends BaseBean {
	public static String formId = "accLedgerListingForm";

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

	@ManagedProperty(value = "#{CurrencyService}")
	private ICurrencyService currencyService;

	public void setCurrencyService(ICurrencyService currencyService) {
		this.currencyService = currencyService;
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

	@ManagedProperty(value = "#{CcoaService}")
	private ICcoaService ccoaService;

	public void setCcoaService(ICcoaService ccoaService) {
		this.ccoaService = ccoaService;
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

	private List<Currency> currencyList;
	private List<ChartOfAccount> coaList;
	private List<Branch> branchList;
	private LedgerDto ledgerDto;
	private ChartOfAccount coa;

	private String dirPath = "/pdf-report/" + "accLedgerListingReport" + "/" + System.currentTimeMillis() + "/";
	private final String fileName = "Account Ledger Listing";

	private TreeNode root;
	private TreeNode[] selectedNodes;
	private String budgetYear;
	// private boolean isBeforeBudgetEnd;
	// private boolean isBeforeDisabled;

	@PostConstruct
	public void init() {
		createNewAccLedgerList();
		rebindData();
	}

	public void createNewAccLedgerList() {
		ledgerDto = new LedgerDto();
		ledgerDto.setCurrencyType(CurrencyType.HOMECURRENCY);
		ledgerDto.setHomeCurrency(true);
		ledgerDto.setAllBranch(false);
		ledgerDto.setAdmin(userProcessService.getLoginUser().isAdmin());
		currencyList = new ArrayList<Currency>();
		coaList = new ArrayList<ChartOfAccount>();
		branchList = new ArrayList<Branch>();
		ledgerDto.setStartDate(new Date());
		ledgerDto.setEndDate(new Date());
		root = new DefaultTreeNode("Root", null);
		selectedNodes = null;
		// ledgerDto.setBeforeDisabled(true);
	}

	public void rebindData() {
		currencyList = currencyService.findAllCurrency();
		coaList = coaService.findAllCoaByAccountCodeType();
		TreeNode node = new DefaultTreeNode("ChartOfAccount", root);

		coaList.forEach(coa -> {
			node.getChildren().add(new DefaultTreeNode(coa, root));
		});

		branchList = branchService.findAllBranch();
		if (ledgerDto.isAdmin()) {
			ledgerDto.setBranch(branchList.get(0));
		} else {
			ledgerDto.setBranch(branchService.findBranchByBranchCode(userProcessService.getLoginUser().getBranch().getBranchCode()));
			ledgerDto.setAllBranch(true);
		}
	}

	private boolean generateReport(List<AccountLedgerDto> dtoList) {
		try {
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("accLedgerListingReport.jrxml");
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
			// parameters.put("reportDate", new
			// SimpleDateFormat("dd-MM-yyyy").format(new Date()));
			parameters.put("reportDate", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
			parameters.put("startDate", ledgerDto.getStartDate());
			parameters.put("endDate", ledgerDto.getEndDate());
			parameters.put("branches", (ledgerDto.isAllBranch() && ledgerDto.isAdmin()) ? "All Branches" : ledgerDto.getBranch().getName());
			// parameters.put("currency",
			// (ledgerDto.getCurrencyType().equals(CurrencyType.HOMECURRENCY)) ?
			// "MMK" : ledgerDto.getCurrency().getCurrencyCode());
			parameters.put("currency", currencyString);
			List<ChartOfAccount> coaList = new ArrayList<>();
			selectedNodes = Stream.of(selectedNodes).filter(node -> !node.getData().equals("ChartOfAccount")).toArray(TreeNode[]::new);
			for (TreeNode node : selectedNodes) {
				coaList.add((ChartOfAccount) node.getData());
			}
			List<AccountLedgerGroupDto> groupList = new ArrayList<>();

			for (ChartOfAccount selectedCoa : coaList) {
				AccountLedgerGroupDto genLedgerGroup = new AccountLedgerGroupDto();
				genLedgerGroup.setCoa(selectedCoa);
				List<AccountLedgerDto> accList = new ArrayList<>();
				accList = dtoList.stream().filter(a -> a.getCoaId().equals(selectedCoa.getId())).collect(Collectors.toList());
				genLedgerGroup.setAccountLedgerList(accList);
				groupList.add(genLedgerGroup);
			}

			groupList = groupList.stream().distinct().collect(Collectors.toList());
			parameters.put("ledgerList", new JRBeanCollectionDataSource(groupList));

			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(groupList));

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
		if (validate()) {
			try {

				List<ChartOfAccount> coaList = new ArrayList<>();
				List<AccountLedgerDto> dtoList;

				selectedNodes = Stream.of(selectedNodes).filter(node -> !node.getData().equals("ChartOfAccount")).toArray(TreeNode[]::new);
				for (TreeNode node : selectedNodes) {
					coaList.add((ChartOfAccount) node.getData());
				}

				if (ledgerDto.isBeforeBudgetEnd()) {
					dtoList = ledgerListingReportService.findAccLedgerListingByClone(ledgerDto, coaList);
				} else {
					dtoList = ledgerListingReportService.findAccLedgerListing(ledgerDto, coaList);
				}

				if (dtoList.size() == 0) {
					addErrorMessage(null, MessageId.NO_RESULT);
					return null;
				} else {
					InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("accLedgerListingExcelReport.jrxml");
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
					// parameters.put("reportDate", new
					// SimpleDateFormat("dd/MM/yyyy").format(new Date()));
					parameters.put("reportDate", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
					parameters.put("startDate", ledgerDto.getStartDate());
					parameters.put("endDate", ledgerDto.getEndDate());
					parameters.put("branches", (ledgerDto.isAllBranch() && ledgerDto.isAdmin()) ? "All Branches" : ledgerDto.getBranch().getName());
					// parameters.put("currency",
					// (ledgerDto.getCurrencyType().equals(CurrencyType.HOMECURRENCY))
					// ? "MMK" : ledgerDto.getCurrency().getCurrencyCode());
					parameters.put("currency", currencyString);
					for (TreeNode node : selectedNodes) {
						coaList.add((ChartOfAccount) node.getData());
					}
					List<AccountLedgerGroupDto> groupList = new ArrayList<>();
					for (ChartOfAccount selectedCoa : coaList) {
						AccountLedgerGroupDto genLedgerGroup = new AccountLedgerGroupDto();
						genLedgerGroup.setCoa(selectedCoa);
						List<AccountLedgerDto> accList = new ArrayList<>();
						accList = dtoList.stream().filter(a -> a.getCoaId().equals(selectedCoa.getId())).collect(Collectors.toList());
						genLedgerGroup.setAccountLedgerList(accList);
						groupList.add(genLedgerGroup);
					}

					groupList = groupList.stream().distinct().collect(Collectors.toList());
					parameters.put("ledgerList", new JRBeanCollectionDataSource(groupList));

					JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
					JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(groupList));

					String path = getWebRootPath() + dirPath;

					FileUtils.forceMkdir(new File(path));

					File destFile = new File(path + fileName.concat(".xlsx"));

					JRXlsxExporter exporter = new JRXlsxExporter();

					exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
					exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
					SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
					configuration.setPrintPageWidth(100);
					configuration.setDetectCellType(true);
					configuration.setColumnWidthRatio(1.5F);
					// configuration.setRemoveEmptySpaceBetweenRows(true);
					// configuration.setOnePagePerSheet(false);
					/*
					 * configuration.setDetectCellType(true);
					 * configuration.setIgnoreCellBorder(false);
					 * configuration.setAutoFitPageHeight(true);
					 * configuration.setCollapseRowSpan(false);
					 * configuration.setFontSizeFixEnabled(true);
					 * configuration.setColumnWidthRatio(2F);
					 */
					// configuration.setRemoveEmptySpaceBetweenRows(true);

					exporter.setConfiguration(configuration);

					exporter.exportReport();

					StreamedContent download = new DefaultStreamedContent();
					File file = new File(path + fileName.concat(".xlsx"));
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
		} else {
			return null;
		}

	}

	public void previewReport() throws Exception {
		// BigDecimal dblBalance = BigDecimal.ZERO;
		List<AccountLedgerDto> list;
		if (validate()) {
			// if (dblBalance.doubleValue() == 0) {
			// addInfoMessage(null, MessageId.OPENING_BALANCE_ERROR);
			// return;
			// }
			List<ChartOfAccount> coaList = new ArrayList<>();
			selectedNodes = Stream.of(selectedNodes).filter(node -> !node.getData().equals("ChartOfAccount")).toArray(TreeNode[]::new);
			for (TreeNode node : selectedNodes) {
				coaList.add((ChartOfAccount) node.getData());
			}
			if (ledgerDto.isBeforeBudgetEnd()) {
				list = ledgerListingReportService.findAccLedgerListingByClone(ledgerDto, coaList);
			} else {
				list = ledgerListingReportService.findAccLedgerListing(ledgerDto, coaList);
			}

			if (list.size() == 0) {
				addErrorMessage(null, MessageId.NO_RESULT);
			} else if (generateReport(list)) {
				PrimeFaces.current().executeScript("PF('accLedgerListingPdfDialog').show();");
				/*
				 * RequestContext context = RequestContext.getCurrentInstance();
				 * context.execute("PF('accLedgerListingPdfDialog').show();");
				 */
			}
		} // End Of Valid
	}
	
	public StreamedContent getDownload() {
		if (validate()) {
			List<ChartOfAccount> coaList = new ArrayList<>();
			List<AccountLedgerDto> dtoList;
			selectedNodes = Stream.of(selectedNodes).filter(node -> !node.getData().equals("ChartOfAccount")).toArray(TreeNode[]::new);
			for (TreeNode node : selectedNodes) {
				coaList.add((ChartOfAccount) node.getData());
			}

			if (ledgerDto.isBeforeBudgetEnd()) {
					dtoList = ledgerListingReportService.findAccLedgerListingByClone(ledgerDto, coaList);
				} else {
					dtoList = ledgerListingReportService.findAccLedgerListing(ledgerDto, coaList);
				}
			if (dtoList.size() == 0) {
				addErrorMessage(null, MessageId.NO_RESULT);
				return null;
			} else {
				// return getDownloadValue(bankCashDtoList);
				exportExcel(dtoList);

			}
		}
		return null;
	}

	public void exportExcel(List<AccountLedgerDto> list) {
		ExternalContext ec = getFacesContext().getExternalContext();
		ec.responseReset();
		ec.setResponseContentType("application/vnd.ms-excel");
		String dirPath = "/report-template/";
		String fileName = "AccLedgerListing.xlsx";
		String runDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		String startDate = new SimpleDateFormat("dd-MM-yyyy").format(ledgerDto.getStartDate());
		String endDate = new SimpleDateFormat("dd-MM-yyyy").format(ledgerDto.getEndDate());
		String currencyString = "";
		if (ledgerDto.getCurrency() == null) {
			currencyString = "All Currencies";
		} else {
			currencyString = ledgerDto.getCurrency().getCurrencyCode();
			if (ledgerDto.isHomeCurrencyConverted()) {
				currencyString = currencyString + " By Home Currency Converted";
			}
		}

		String branchString = "";
		if (ledgerDto.isAllBranch() && ledgerDto.isAdmin()) {
			branchString = "All Branches";
		} else {
			branchString = ledgerDto.getBranch().getName();
		}
		List<ChartOfAccount> coaList = new ArrayList<>();
		selectedNodes = Stream.of(selectedNodes).filter(node -> !node.getData().equals("ChartOfAccount")).toArray(TreeNode[]::new);
		for (TreeNode node : selectedNodes) {
			coaList.add((ChartOfAccount) node.getData());
		}

		List<AccountLedgerGroupDto> groupList = new ArrayList<>();
		/*
		 * for (AccountLedgerDto dto : list) { AccountLedgerGroupDto
		 * genLedgerGroup = new AccountLedgerGroupDto();
		 * genLedgerGroup.setCoa(dto.getCoa()); List<AccountLedgerDto> accList =
		 * new ArrayList<>(); accList = list.stream().filter(a ->
		 * a.getCoa().equals(dto.getCoa())).collect(Collectors.toList());
		 * genLedgerGroup.setAccountLedgerList(accList);
		 * groupList.add(genLedgerGroup); }
		 */
		for (ChartOfAccount selectedCoa : coaList) {
			AccountLedgerGroupDto genLedgerGroup = new AccountLedgerGroupDto();
			genLedgerGroup.setCoa(selectedCoa);
			List<AccountLedgerDto> accList = new ArrayList<>();
			accList = list.stream().filter(a -> a.getCoaId().equals(selectedCoa.getId())).collect(Collectors.toList());
			genLedgerGroup.setAccountLedgerList(accList);
			groupList.add(genLedgerGroup);
		}
		groupList = groupList.stream().distinct().collect(Collectors.toList());

		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		try {
			OutputStream op = ec.getResponseOutputStream();
			AccLedgerListingExcel accLedgerListingExcel = new AccLedgerListingExcel();
			accLedgerListingExcel.generate(op, list, coaList, groupList, branchString, currencyString, startDate, endDate);
			getFacesContext().responseComplete();
		} catch (IOException e) {
			throw new SystemException(ErrorCode.SYSTEM_ERROR, "Failed to export BankCash_Report.xlsx", e);
		}
	}


	public void changeBudgetYear() {
		String currentBudget;
		currentBudget = BusinessUtil.getCurrentBudget();
		Date reportDate = DateUtils.resetStartDate(ledgerDto.getStartDate());
		if (DateUtils.isIntervalBugetYear(reportDate))
			budgetYear = BusinessUtil.getBudgetInfo(reportDate, 2);
		else
			budgetYear = setup_HistoryDAO.findbudgetyear(reportDate);

		/*
		 * if (!budgetYear.equals(currentBudget)) {
		 * ledgerDto.setBeforeDisabled(false);
		 * ledgerDto.setBeforeBudgetEnd(true); } else {
		 * ledgerDto.setBeforeDisabled(true);
		 * ledgerDto.setBeforeBudgetEnd(false); }
		 */
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
			addInfoMessage(null, MessageId.DATE_EXCEEDED, "StartDate", ledgerDto.getStartDate(), todayDate);
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

	public void selectAccountCode(ChartOfAccount coa) {
		this.coa = coa;
	}

	public ChartOfAccount getCoa() {
		return coa;
	}

	public void setCoa(ChartOfAccount coa) {
		this.coa = coa;
	}

	public LedgerDto getLedgerDto() {
		return ledgerDto;
	}

	public void setLedgerDto(LedgerDto ledgerDto) {
		this.ledgerDto = ledgerDto;
	}

	public List<ChartOfAccount> getCoaList() {
		return coaList;
	}

	public List<Currency> getCurrencyList() {
		return currencyList;
	}

	public List<Branch> getBranchList() {
		return branchList;
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

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	public TreeNode getRoot() {
		return root;
	}

	public String getBudgetYear() {
		return budgetYear;
	}

	public void setBudgetYear(String budgetYear) {
		this.budgetYear = budgetYear;
	}

	/*
	 * public boolean isBeforeBudgetEnd() { return isBeforeBudgetEnd; }
	 * 
	 * public void setBeforeBudgetEnd(boolean isBeforeBudgetEnd) {
	 * this.isBeforeBudgetEnd = isBeforeBudgetEnd; }
	 * 
	 * public boolean isBeforeDisabled() { return isBeforeDisabled; }
	 * 
	 * public void setBeforeDisabled(boolean isBeforeDisabled) {
	 * this.isBeforeDisabled = isBeforeDisabled; }
	 */

}
