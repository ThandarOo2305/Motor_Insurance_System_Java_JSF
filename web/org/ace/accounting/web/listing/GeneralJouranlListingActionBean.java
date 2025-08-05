package org.ace.accounting.web.listing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.faces.event.AjaxBehaviorEvent;

import org.ace.accounting.common.PropertiesManager;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.GenJournalDto;
import org.ace.accounting.dto.VLdto;
import org.ace.accounting.dto.VPFTdto;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.report.listingReport.service.interfaces.IListingReportService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.accounting.user.User;
import org.ace.java.web.common.BaseBean;
import org.apache.commons.io.FileUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

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

@ManagedBean(name = "GeneralJouranlListingActionBean")
@ViewScoped
public class GeneralJouranlListingActionBean extends BaseBean {

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

	private VoucherListingType type;
	private TransactionType tType;
	private boolean chkVoucherNo;
	private VPFTdto ftdto;
	private VPFTdto tidto;
	private Date startDate;
	private Date endDate;
	private Branch branch;
	private Currency currency;
	private String dirPath = "/pdf-report/" + "VoucherListingReport" + "/" + System.currentTimeMillis() + "/";
	private String fileName = "";
	private List<VPFTdto> voucherNoList = new ArrayList<VPFTdto>();
	private List<VPFTdto> filterVoucherNoList;
	private boolean isOnlyVoucher;
	private BigDecimal totalDebitAmount;
	private BigDecimal totalCreditAmount;
	private boolean homeCurrencyConverted;
	private boolean isAllCurrency;
	private List<VLdto> dtoList;

	@PostConstruct
	private void init() {
		chkVoucherNo = false;
		ftdto = new VPFTdto();
		ftdto.setStartDate(new Date());
		tidto = new VPFTdto();
		ftdto.setEndDate(new Date());
		startDate = new Date();
		endDate = new Date();
		User user = userProcessService.getLoginUser();
		if (user.isAdmin()) {
			branch = null;
		} else {
			if (user.getBranch() == null) {
				addErrorMessage(null, MessageId.USER_NO_BRANCH);
			} else {
				branch = user.getBranch();
			}
		}
	}

	public boolean validate() {
		Date todayDate = new Date();
		if (startDate.after(todayDate)) {
			addErrorMessage(null, MessageId.START_DATE_EXCEEDED);
			return false;
		} else if (endDate.after(todayDate)) {
			addErrorMessage(null, MessageId.END_DATE_EXCEEDED);
			return false;
		} else if (startDate.after(endDate)) {
			addErrorMessage(null, MessageId.SE_INVALID_DATE);
			return false;
		} else if (chkVoucherNo == true) {
			if (ftdto == null) {
				addErrorMessage(null, MessageId.REQUIRED_FROMVOUCHER);
				return false;
			} else if (startDate == null) {
				addErrorMessage(null, MessageId.REQUIRED_TOVOUCHER);
				return false;
			} else if (ftdto.getVoucherNo().compareTo(tidto.getVoucherNo()) > 0) {
				addErrorMessage(null, MessageId.VOUCHER_INVALID);
				return false;
			}
			return true;
		} else {
			return true;
		}

	}

	public void changeVType(AjaxBehaviorEvent event) {
		switch (type) {
			case A:
				fileName = "General Journal For All Voucher Listing";
				break;
			case CC:
				fileName = "General Journal For Credit Voucher Listing By Cash";
				break;
			case CD:
				fileName = "General Journal For Debit Voucher Listing By Cash";
				break;
			case T:
				fileName = "General Journal For Journal Voucher Listing";
				break;
		}
	}

	public void chkClick() {
		if (startDate != null && endDate != null) {
			filterVoucherNoList = new ArrayList<>();
			filterVoucherNoList.clear();
			voucherNoList.clear();
			voucherNoList = listingReportService.findFromVoucherNoForGenJournal(type, ftdto.getStartDate(), ftdto.getEndDate(), branch, currency, tType);
			filterVoucherNoList.addAll(voucherNoList);
			chkVoucherNo = true;
		} else {
			chkVoucherNo = false;
			addErrorMessage(null, MessageId.SE_REQUIRED);
		}

	}

	public void search() {
		if (validate()) {
			if (!chkVoucherNo) {
				dtoList = listingReportService.findGenJournalListingReport(type, ftdto.getStartDate(), ftdto.getEndDate(), branch, currency, homeCurrencyConverted, tType);
				if (dtoList.size() == 0) {
					addErrorMessage(null, MessageId.NO_RESULT);
				} else if (generateReport(dtoList)) {
					PrimeFaces.current().executeScript("PF('voucherListingPdfDialog').show();");

				}
			} else {
				dtoList = listingReportService.findGenJournalListingReportByVoucherNo(type, ftdto.getStartDate(), ftdto.getEndDate(), ftdto.getVoucherNo(), tidto.getVoucherNo(),
						branch, currency, homeCurrencyConverted, tType);
				if (dtoList.size() == 0) {
					addErrorMessage(null, MessageId.NO_RESULT);
				} else if (generateReport(dtoList)) {
					PrimeFaces.current().executeScript("PF('voucherListingPdfDialog').show();");

				}
			}
		}
	}

	public void configHomeCurrency() {

		if (currency != null) {
			isAllCurrency = false;
			homeCurrencyConverted = false;
		} else {
			isAllCurrency = true;
			homeCurrencyConverted = true;
		}

	}

	public boolean generateReport(List<VLdto> dtoList) {
		try {
			Map<String, Object> parameters = new HashMap<>();
			String accountType = "";
			String homeConvert = null;
			if (type.equals(VoucherListingType.CC)) {
				accountType = "Credit Vouchers";
			} else if (type.equals(VoucherListingType.CD)) {
				accountType = "Debit Vouchers";
			} else if (type.equals(VoucherListingType.T)) {
				accountType = "Journal Vouchers";
			} else if (type.equals(VoucherListingType.A)) {
				accountType = "All Vouchers";
			}

			if (homeCurrencyConverted) {
				homeConvert = " By Home Amount";
			} else {
				homeConvert = " ";
			}

			parameters.put("ACCOUNT_TYPE", accountType);
			parameters.put("START_DATE", ftdto.getStartDate());
			parameters.put("END_DATE", ftdto.getEndDate());

			parameters.put("BRANCH", branch == null ? "All Branches" : branch.getName());
			parameters.put("CURRENCY", currency == null ? "All Currencies" : currency.getDescription());
			parameters.put("HOMECUR", homeConvert);

			String newDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			parameters.put("NEWDATE", newDate);
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("ICON_RSOURCES"));
			parameters.put("LOGO", image);
			List<GenJournalDto> genJournalList = new ArrayList<>();
			int srNo = 1;
			totalDebitAmount = BigDecimal.ZERO;
			totalCreditAmount = BigDecimal.ZERO;

			for (VLdto dto : dtoList) {
				GenJournalDto genJournal = new GenJournalDto();
				genJournal.setSettlementDate(dto.getSettlementDate());
				genJournal.setVoucherNo(dto.getVoucherNo());
				List<VLdto> voucherList = new ArrayList<>();
				voucherList = dtoList.stream().filter(a -> a.getVoucherNo().equals(dto.getVoucherNo())).collect(Collectors.toList());
				genJournal.setVoucherList(voucherList);
				totalDebitAmount = totalDebitAmount.add(dto.getDebit());
				totalCreditAmount = totalCreditAmount.add(dto.getCredit());
				genJournalList.add(genJournal);
			}

			genJournalList = genJournalList.stream().distinct().collect(Collectors.toList());
			for (GenJournalDto gJ : genJournalList) {
				gJ.setSrNo(srNo);
				for (VLdto dto : gJ.getVoucherList()) {
					dto.setSrNo(srNo);
				}
				srNo++;
			}

			parameters.put("TotalDebit", totalDebitAmount);
			parameters.put("TotalCredit", totalCreditAmount);
			parameters.put("genJournalList", new JRBeanCollectionDataSource(genJournalList));

			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("genJournalReport.jrxml");
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(genJournalList));

			String path = getWebRootPath() + dirPath;
			FileUtils.forceMkdir(new File(path));
			FileUtils.cleanDirectory(new File(path));
			JasperExportManager.exportReportToPdfFile(jasperPrint, path + fileName.concat(".pdf"));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage(null, MessageId.REPORT_ERROR);
			return false;
		}
	}

	public StreamedContent getDownload() {
		if (validate()) {
			if (!chkVoucherNo) {
				dtoList = listingReportService.findGenJournalListingReport(type, ftdto.getStartDate(), ftdto.getEndDate(), branch, currency, homeCurrencyConverted, tType);
				if (dtoList.size() == 0) {
					addErrorMessage(null, MessageId.NO_RESULT);
					return null;
				} else {
					return getDownloadValue(dtoList);

				}
			} else {
				dtoList = listingReportService.findGenJournalListingReportByVoucherNo(type, ftdto.getStartDate(), ftdto.getEndDate(), ftdto.getVoucherNo(), tidto.getVoucherNo(),
						branch, currency, homeCurrencyConverted, tType);
				if (dtoList.size() == 0) {
					addErrorMessage(null, MessageId.NO_RESULT);
					return null;
				} else {
					return getDownloadValue(dtoList);

				}
			}

		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private StreamedContent getDownloadValue(List<VLdto> dtoList) {
		try {
			List<JasperPrint> prints = new ArrayList<JasperPrint>();
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("genJournalExcelReport.jrxml");
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("LOGO"));
			Map<String, Object> parameters = new HashMap<String, Object>();
			String accountType = "";
			String homeConvert = null;
			if (type.equals(VoucherListingType.CC)) {
				accountType = "Credit Vouchers";
			} else if (type.equals(VoucherListingType.CD)) {
				accountType = "Debit Vouchers";
			} else if (type.equals(VoucherListingType.T)) {
				accountType = "Journal Vouchers";
			} else if (type.equals(VoucherListingType.A)) {
				accountType = "All Vouchers";
			}

			if (homeCurrencyConverted) {
				homeConvert = " By Home Amount";
			} else {
				homeConvert = " ";
			}

			parameters.put("ACCOUNT_TYPE", accountType);
			parameters.put("START_DATE", ftdto.getStartDate());
			parameters.put("END_DATE", ftdto.getEndDate());

			parameters.put("BRANCH", branch == null ? "All Branches" : branch.getName());
			parameters.put("CURRENCY", currency == null ? "All Currencies" : currency.getDescription());
			parameters.put("HOMECUR", homeConvert);

			String newDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			parameters.put("NEWDATE", newDate);
			parameters.put("LOGO", image);
			List<GenJournalDto> genJournalList = new ArrayList<>();
			int srNo = 1;
			totalDebitAmount = BigDecimal.ZERO;
			totalCreditAmount = BigDecimal.ZERO;

			for (VLdto dto : dtoList) {
				GenJournalDto genJournal = new GenJournalDto();
				genJournal.setSettlementDate(dto.getSettlementDate());
				genJournal.setVoucherNo(dto.getVoucherNo());
				List<VLdto> voucherList = new ArrayList<>();
				voucherList = dtoList.stream().filter(a -> a.getVoucherNo().equals(dto.getVoucherNo())).collect(Collectors.toList());
				genJournal.setVoucherList(voucherList);
				totalDebitAmount = totalDebitAmount.add(dto.getDebit());
				totalCreditAmount = totalCreditAmount.add(dto.getCredit());
				genJournalList.add(genJournal);
			}

			genJournalList = genJournalList.stream().distinct().collect(Collectors.toList());
			for (GenJournalDto gJ : genJournalList) {
				gJ.setSrNo(srNo);
				for (VLdto dto : gJ.getVoucherList()) {
					dto.setSrNo(srNo);
				}
				srNo++;
			}
			parameters.put("genJournalList", new JRBeanCollectionDataSource(genJournalList));
			parameters.put("TotalDebit", totalDebitAmount);
			parameters.put("TotalCredit", totalCreditAmount);
			parameters.put("genJournalList", new JRBeanCollectionDataSource(genJournalList));

			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(genJournalList));
			prints.add(jasperPrint);

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
			configuration.setColumnWidthRatio(1.5F);
			configuration.setPrintPageTopMargin(30);
			configuration.setPrintPageLeftMargin(50);
			configuration.setPrintPageRightMargin(30);
			configuration.setPrintPageBottomMargin(30);

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

	public void checkData() {
		voucherNoList = listingReportService.findFromVoucherNoForGenJournal(type, ftdto.getStartDate(), ftdto.getEndDate(), branch, currency, tType);
	}

	public void selectFromVoucherNo(VPFTdto ftdto) {
		this.ftdto = ftdto;

	}

	public void selectToVoucherNo(VPFTdto tidto) {
		this.tidto = tidto;
	}

	public boolean isChkVoucherNo() {
		return chkVoucherNo;
	}

	public void setChkVoucherNo(boolean chkVoucherNo) {
		this.chkVoucherNo = chkVoucherNo;
	}

	public void setType(VoucherListingType type) {
		this.type = type;
	}

	public VoucherListingType getType() {
		return type;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Currency getCurrency() {
		return currency;
	}

	public VoucherListingType[] getVoucherListingTypes() {
		return VoucherListingType.values();
	}

	public TransactionType[] getTransactionTypes() {
		return TransactionType.values();
	}

	public List<Branch> getBranches() {
		return branchService.findAllBranch();
	}

	public List<Currency> getCurrencies() {
		return currencyService.findAllCurrency();
	}

	public String getStream() {
		String fullFilePath = dirPath + fileName.concat(".pdf");
		return fullFilePath;
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

	public VPFTdto getFtdto() {
		return ftdto;
	}

	public void setFtdto(VPFTdto ftdto) {
		this.ftdto = ftdto;
	}

	public VPFTdto getTidto() {
		return tidto;
	}

	public void setTidto(VPFTdto tidto) {
		this.tidto = tidto;
	}

	public List<VPFTdto> getVoucherNoList() {
		return voucherNoList;
	}

	/*
	 * public void setVoucherNoList(List<VPFTdto> voucherNoList) {
	 * this.voucherNoList = voucherNoList; }
	 */
	public List<VPFTdto> getFilterVoucherNoList() {
		return filterVoucherNoList;
	}

	public void setFilterVoucherNoList(List<VPFTdto> filterVoucherNoList) {
		this.filterVoucherNoList = filterVoucherNoList;
	}

	public boolean isOnlyVoucher() {
		return isOnlyVoucher;
	}

	public void setOnlyVoucher(boolean isOnlyVoucher) {
		this.isOnlyVoucher = isOnlyVoucher;
	}

	public TransactionType gettType() {
		return tType;
	}

	public void settType(TransactionType tType) {
		this.tType = tType;
	}

	public boolean isHomeCurrencyConverted() {
		return homeCurrencyConverted;
	}

	public void setHomeCurrencyConverted(boolean homeCurrencyConverted) {
		this.homeCurrencyConverted = homeCurrencyConverted;
	}

	public boolean isAllCurrency() {
		return isAllCurrency;
	}

	public void setAllCurrency(boolean isAllCurrency) {
		this.isAllCurrency = isAllCurrency;
	}

}
