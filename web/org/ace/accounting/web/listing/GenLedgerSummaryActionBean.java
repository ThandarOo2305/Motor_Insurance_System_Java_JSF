package org.ace.accounting.web.listing;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.GenLedgerCriteria;
import org.ace.accounting.dto.GenLedgerDetailsDTO;
import org.ace.accounting.dto.GenLedgerSummaryDTO;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.chartaccount.service.interfaces.ICoaService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.apache.commons.io.FileUtils;
import org.primefaces.PrimeFaces;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "GenLedgerSummaryActionBean")
@ViewScoped
public class GenLedgerSummaryActionBean extends BaseBean {

	@ManagedProperty(value = "#{CoaService}")
	private ICoaService coaService;

	public void setCoaService(ICoaService coaService) {
		this.coaService = coaService;
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

	private GenLedgerCriteria criteria;
	private CurrencyType currencyType;

	private List<Branch> branchList;
	private List<Currency> currencyList;
	private List<GenLedgerSummaryDTO> genLedgerSummaryDTOList;

	private String dirPath = "/pdf-report/" + "generalLedgerSummary" + "/" + System.currentTimeMillis() + "/";
	private final String fileName = "General Ledger Summary Listing.pdf";

	@PostConstruct
	public void init() {
		genLedgerSummaryDTOList = new ArrayList<>();
		criteria = new GenLedgerCriteria();
		currencyType = CurrencyType.HOMECURRENCY;
		criteria.setHomeCurrencyConverted(false);
		criteria.setHomeCurrency(true);
		criteria.setAllBranch(false);
		criteria.setStartDate(new Date());
		criteria.setEndDate(new Date());
		initializeData();
	}

	public void initializeData() {
		currencyList = new ArrayList<>();
		branchList = new ArrayList<>();

		currencyList = currencyService.findAllCurrency();
		branchList = branchService.findAllBranch();
	}

	public void searchReportList() {
		try {

			Map<String, BigDecimal> accountAndEndBalanceMap = new HashMap<>();
			Map<String, BigDecimal> creditMap = new HashMap<>();
			Map<String, BigDecimal> debitMap = new HashMap<>();
			Map<String, BigDecimal> openingMap = new HashMap<>();
			Date resetStartDate = DateUtils.resetStartDate(criteria.getStartDate());

			genLedgerSummaryDTOList = coaService.findGenLedgerSummaryCOAList(criteria);
			genLedgerSummaryDTOList.stream().forEach(dto -> DateUtils.resetEndDate(dto.getSettlementDate()));

			List<GenLedgerSummaryDTO> beforeStartDateList = genLedgerSummaryDTOList.stream().filter(dto -> dto.getSettlementDate().before(resetStartDate))
					.collect(Collectors.toList());

			if (beforeStartDateList.size() > 0) {
				for (GenLedgerSummaryDTO dto : beforeStartDateList) {

					accountAndEndBalanceMap.computeIfPresent(dto.getAccountCode(), (k, v) -> calculateAmountByAccountType(dto, accountAndEndBalanceMap.get(dto.getAccountCode())));
					//TODO FIXME PSH 30/07/2020 
//					accountAndEndBalanceMap.computeIfAbsent(dto.getAccountCode(), k -> calculateAmountByAccountType(dto, dto.getBeginningBalance()));
					accountAndEndBalanceMap.computeIfAbsent(dto.getAccountCode(), k -> dto.getBeginningBalance());
				}

			} else {
				for (GenLedgerSummaryDTO dto : genLedgerSummaryDTOList) {
					//TODO FIXME PSH 30/07/2020 
//					accountAndEndBalanceMap.computeIfPresent(dto.getAccountCode(), (k, v) -> calculateAmountByAccountType(dto, accountAndEndBalanceMap.get(dto.getAccountCode())));
//					accountAndEndBalanceMap.computeIfAbsent(dto.getAccountCode(), k -> calculateAmountByAccountType(dto, dto.getBeginningBalance()));
					
					accountAndEndBalanceMap.computeIfAbsent(dto.getAccountCode(), k -> dto.getBeginningBalance());
				}

			}

			List<GenLedgerSummaryDTO> afterStartDateList = genLedgerSummaryDTOList.stream()
					.filter(dto -> dto.getSettlementDate().equals(resetStartDate) || dto.getSettlementDate().after(resetStartDate)).collect(Collectors.toList());

			if (afterStartDateList.size() > 0) {
				/* FOR TEST */
				// afterStartDateList =
				// genLedgerSummaryDTOList.stream().collect(Collectors.toList());

				for (GenLedgerSummaryDTO dto : afterStartDateList) {

					creditMap.computeIfPresent(dto.getAccountCode(), (k, v) -> creditMap.get(dto.getAccountCode()).add(dto.getCreditAmount()));
					creditMap.computeIfAbsent(dto.getAccountCode(), k -> dto.getCreditAmount());

					debitMap.computeIfPresent(dto.getAccountCode(), (k, v) -> debitMap.get(dto.getAccountCode()).add(dto.getDebitAmount()));
					debitMap.computeIfAbsent(dto.getAccountCode(), k -> dto.getDebitAmount());

					openingMap.computeIfAbsent(dto.getAccountCode(), k -> accountAndEndBalanceMap.get(dto.getAccountCode()));
				}

				Set<String> set = new HashSet<>(afterStartDateList.size());

				afterStartDateList = afterStartDateList.stream().filter(dto -> set.add(dto.getAccountCode())).collect(Collectors.toList());

				afterStartDateList.stream().forEach(dto -> {
					dto.setBeginningBalance(openingMap.get(dto.getAccountCode()));
					dto.setCreditAmount(creditMap.get(dto.getAccountCode()));
					dto.setDebitAmount(debitMap.get(dto.getAccountCode()));
					dto.setEndingBalance(calculateAmountByAccountType(dto, dto.getBeginningBalance()));
					dto.setNetBalance(calculateNetAmountByAccountType(dto));
				});

				if (generateSummaryReport(afterStartDateList)) {
					PrimeFaces.current().executeScript("PF('genLedgerListingPdfDialog').show();");
				}
			}
		} catch (SystemException e) {
			handleSysException(e);
		}
	}

	public void searchGenLedgerDetailsList() {
		try {
			Map<String, BigDecimal> accountAndEndBalanceMap = new HashMap<>();
			Map<String, List<GenLedgerSummaryDTO>> detailsMap = new HashMap<>();
			List<GenLedgerDetailsDTO> detailsList = new ArrayList<>();
			Date resetStartDate = DateUtils.resetStartDate(criteria.getStartDate());

			genLedgerSummaryDTOList = coaService.findGenLedgerSummaryCOAList(criteria);
			genLedgerSummaryDTOList.stream().forEach(dto -> DateUtils.resetEndDate(dto.getSettlementDate()));

			List<GenLedgerSummaryDTO> beforeStartDateList = genLedgerSummaryDTOList.stream().filter(dto -> dto.getSettlementDate().before(resetStartDate))
					.collect(Collectors.toList());

			if (beforeStartDateList.size() > 0) {

				for (GenLedgerSummaryDTO dto : beforeStartDateList) {

					accountAndEndBalanceMap.computeIfPresent(dto.getAccountCode(), (k, v) -> calculateAmountByAccountType(dto, accountAndEndBalanceMap.get(dto.getAccountCode())));
					accountAndEndBalanceMap.computeIfAbsent(dto.getAccountCode(), k -> calculateAmountByAccountType(dto, dto.getBeginningBalance()));
				}

			} else {

				for (GenLedgerSummaryDTO dto : genLedgerSummaryDTOList) {

					accountAndEndBalanceMap.computeIfPresent(dto.getAccountCode(), (k, v) -> calculateAmountByAccountType(dto, accountAndEndBalanceMap.get(dto.getAccountCode())));
					accountAndEndBalanceMap.computeIfAbsent(dto.getAccountCode(), k -> calculateAmountByAccountType(dto, dto.getBeginningBalance()));
				}

			}

			List<GenLedgerSummaryDTO> afterStartDateList = genLedgerSummaryDTOList.stream()
					.filter(dto -> dto.getSettlementDate().equals(resetStartDate) || dto.getSettlementDate().after(resetStartDate)).collect(Collectors.toList());

			if (afterStartDateList.size() > 0) {
				/* FOR TEST */
				// afterStartDateList =
				// genLedgerSummaryDTOList.stream().collect(Collectors.toList());

				for (GenLedgerSummaryDTO dto : afterStartDateList) {

					detailsMap.computeIfPresent(dto.getAccountCode(), (k, v) -> {
						v.add(dto);
						return v;
					});

					detailsMap.computeIfAbsent(dto.getAccountCode(), k -> {
						List<GenLedgerSummaryDTO> genList = new ArrayList<>();
						genList.add(dto);
						return genList;
					});

				}

				for (List<GenLedgerSummaryDTO> dtos : detailsMap.values()) {
					String accountCode = dtos.get(0).getAccountCode();
					String accountName = dtos.get(0).getAccountName();
					detailsList.add(new GenLedgerDetailsDTO(accountCode, accountName, accountAndEndBalanceMap.get(accountCode), dtos));
				}

				detailsList.forEach(dto -> {
					Collections.sort(dto.getDetailsAccountList(), Comparator.comparing(GenLedgerSummaryDTO::getSettlementDate));

					double openingBalance = dto.getOpeningBalance().doubleValue();

					for (GenLedgerSummaryDTO summaryDTO : dto.getDetailsAccountList()) {

						summaryDTO.setEndingBalance(calculateAmountByAccountType(summaryDTO, new BigDecimal(openingBalance)));
						openingBalance = summaryDTO.getEndingBalance().doubleValue();
						summaryDTO.setNetBalance(calculateNetAmountByAccountType(summaryDTO));
					}
				});

				if (generateDetilsReport(detailsList)) {
					PrimeFaces.current().executeScript("PF('genLedgerListingPdfDialog').show();");
				}

			}
		} catch (SystemException e) {
			handleSysException(e);
		}

	}

	private BigDecimal calculateAmountByAccountType(GenLedgerSummaryDTO dto, BigDecimal amount) {
		switch (dto.getAccountType()) {
			case A:
			case E:
				return amount.add(dto.getDebitAmount().subtract(dto.getCreditAmount()));
			case I:
			case L:
				return amount.add(dto.getCreditAmount().subtract(dto.getDebitAmount()));
			default:
				return amount.add(dto.getCreditAmount().subtract(dto.getDebitAmount()));
		}

	}

	private BigDecimal calculateNetAmountByAccountType(GenLedgerSummaryDTO dto) {
		switch (dto.getAccountType()) {
			case A:
			case E:
				return dto.getDebitAmount().subtract(dto.getCreditAmount());
			case I:
			case L:
				return dto.getCreditAmount().subtract(dto.getDebitAmount());
			default:
				return dto.getCreditAmount().subtract(dto.getDebitAmount());
		}
	}

	public void changeCurrencyType(AjaxBehaviorEvent event) {
		criteria.setHomeCurrency(this.currencyType.equals(CurrencyType.HOMECURRENCY));
	}

	private boolean generateSummaryReport(List<GenLedgerSummaryDTO> reportDTOList) {
		try {
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("generalLedgerSummary.jrxml");
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath("pdf-report/efi_Life_Logo.png");
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("reportTitle", "AcePlus Accounting System");
			parameters.put("logoPath", image);
			parameters.put("reportDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			parameters.put("startDate", criteria.getStartDate());
			parameters.put("endDate", criteria.getEndDate());
			parameters.put("branches", criteria.isAllBranch() ? "All Branches" : criteria.getBranch().getName());
			parameters.put("currency", (currencyType.equals(CurrencyType.HOMECURRENCY)) ? "MMK" : criteria.getCurrency().getCurrencyCode());
			parameters.put("summaryList", new JRBeanCollectionDataSource(reportDTOList));
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
			String path = getWebRootPath() + dirPath;

			FileUtils.forceMkdir(new File(path));
			JasperExportManager.exportReportToPdfFile(jasperPrint, path + fileName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage(null, MessageId.REPORT_ERROR);
			return false;
		}

	}

	private boolean generateDetilsReport(List<GenLedgerDetailsDTO> reportDTOList) {
		try {
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("detailsReport.jrxml");
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath("pdf-report/efi_Life_Logo.png");
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("reportTitle", "AcePlus Accounting System");
			parameters.put("logoPath", image);
			parameters.put("reportDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			parameters.put("fromDate", criteria.getStartDate());
			parameters.put("toDate", criteria.getEndDate());
			parameters.put("branches", criteria.isAllBranch() ? "All Branches" : criteria.getBranch().getName());
			parameters.put("currency", (currencyType.equals(CurrencyType.HOMECURRENCY)) ? "MMK" : criteria.getCurrency().getCurrencyCode());
			parameters.put("reportList", new JRBeanCollectionDataSource(reportDTOList));

			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			// JasperReport jasperReport =
			// (JasperReport)JRLoader.loadObject(inputStream);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(reportDTOList));
			String path = getWebRootPath() + dirPath;

			FileUtils.forceMkdir(new File(path));
			JasperExportManager.exportReportToPdfFile(jasperPrint, path + fileName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage(null, MessageId.REPORT_ERROR);
			return false;
		}

	}

	public GenLedgerCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(GenLedgerCriteria criteria) {
		this.criteria = criteria;
	}

	public CurrencyType[] getCurrencyTypes() {
		return CurrencyType.values();
	}

	public CurrencyType getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(CurrencyType currencyType) {
		this.currencyType = currencyType;
	}

	public List<Currency> getCurrencyList() {
		return currencyList;
	}

	public List<Branch> getBranchList() {
		return branchList;
	}

	public Date getTodayDate() {
		return new Date();
	}

	public String getStream() {
		String fullFilePath = dirPath + fileName;
		return fullFilePath;
	}
}
