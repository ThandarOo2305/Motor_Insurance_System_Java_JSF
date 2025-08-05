package org.ace.accounting.web.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.PropertiesManager;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.BankCashDto;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.accounting.system.view.bankcash.service.interfaces.IBankCashService;
import org.ace.accounting.user.User;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.ace.java.web.common.ParamId;
import org.apache.commons.io.FileUtils;
import org.primefaces.PrimeFaces;
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

/**************************************************************************
 * $21-04-2016$ $Thaw Oo Khaing$ $1$ ACEPLUS SOLUTIONS CO., Ltd.
 *************************************************************************/

@ManagedBean(name = "BankCashActionBean")
@ViewScoped
public class BankCashActionBean extends BaseBean {

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

	@ManagedProperty(value = "#{BankCashService}")
	private IBankCashService bankCashService;

	public void setBankCashService(IBankCashService bankCashService) {
		this.bankCashService = bankCashService;
	}

	@ManagedProperty(value = "#{PropertiesManager}")
	private PropertiesManager propertiesManager;

	public void setPropertiesManager(PropertiesManager propertiesManager) {
		this.propertiesManager = propertiesManager;
	}

	private CurrencyType currencyReportType;
	private Currency currency;
	private Branch branch;
	private boolean isBranchDisabled;
	private boolean isCurDisabled;
	private boolean isCurConverted;
	private Date reportDate;

	private final String reportName = "bankCashScroll";
	private final String pdfDirPath = "/pdf-report/" + reportName + "/" + System.currentTimeMillis() + "/";
	private final String dirPath = getWebRootPath() + pdfDirPath;
	private final String fileName = "Bank Cash Scroll";

	@PostConstruct
	private void init() {
		isCurDisabled = true;
		isCurConverted = false;
		currencyReportType = CurrencyType.HOMECURRENCY;
		reportDate = new Date();
		User user = (User) getParam(ParamId.LOGIN_USER);
		if (user.isAdmin()) {
			isBranchDisabled = false;
		} else {
			branch = user.getBranch();
			isBranchDisabled = true;
		}
	}

	public void curRadioSelect() {
		if (currencyReportType.equals(CurrencyType.HOMECURRENCY)) {
			currency = null;
			isCurDisabled = true;
		} else if (currencyReportType.equals(CurrencyType.SOURCECURRENCY)) {
			currency = null;
			isCurDisabled = false;
		}
	}

	public void search() {
		if (validate()) {
			try {
				List<BankCashDto> bankCashDtoList = bankCashService.findDailyTransaction(currencyReportType, currency, reportDate, isCurConverted, branch);
				if (bankCashDtoList.size() > 0) {
					generateReport(bankCashDtoList);
					PrimeFaces.current().executeScript("PF('bankCashPdfDialog').show();");
					/*
					 * RequestContext context =
					 * RequestContext.getCurrentInstance();
					 * context.execute("PF('bankCashPdfDialog').show();");
					 */
				} else {
					addErrorMessage(null, MessageId.NO_RECORDS);
				}
			} catch (SystemException e) {
				handleSysException(e);
			} catch (Exception e) {
				handleException(e);
			}
		}
	}

	private void generateReport(List<BankCashDto> bankCashDtoList) throws Exception {
		try {
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("bankCashScroll.jrxml");
			// Map parameters = new HashMap();
			Map<String, Object> parameters = new HashMap<String, Object>();
			String runDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

			String branchString = "";
			if (branch == null) {
				branchString = "All Branches";
			} else {
				branchString = branch.getName();
			}

			String currencyString = "";
			if (currency == null) {
				currencyString = "All Currencies";
			} else {
				currencyString = currency.getCurrencyCode();
				if (isCurConverted || currencyReportType.equals(CurrencyType.HOMECURRENCY)) {
					currencyString = currencyString + " By Home Amount";
				}
			}

			BigDecimal debitTotal = BigDecimal.ZERO;
			BigDecimal creditTotal = BigDecimal.ZERO;
			for (BankCashDto bankCashDto : bankCashDtoList) {
				debitTotal = debitTotal.add(bankCashDto.getDebit());
				creditTotal = creditTotal.add(bankCashDto.getCredit());
			}

			BigDecimal debitBalanceAsOn = bankCashService.findOpeningBalance(currencyReportType, currency, reportDate, isCurConverted, branch);
			if (null != debitBalanceAsOn) {

				// TODO not sure there will be data for this one , converse with
				// MMSN
				BigDecimal creditBalanceAsOn = BigDecimal.ZERO;
				BigDecimal totalDifference = BigDecimal.ZERO;
				// TODO not sure about this calculation , converse with
				// MMSN
				/*
				 * if (debitTotal.doubleValue() > creditTotal.doubleValue()) {
				 * totalDifference = debitTotal.subtract(creditTotal); } else {
				 * totalDifference = creditTotal.subtract(debitTotal); }
				 */

				totalDifference = debitTotal.subtract(creditTotal);
				// TODO not sure there will be data for this one , converse with
				// MMSN
				BigDecimal debitBalanceTonight = BigDecimal.ZERO;
				BigDecimal creditBalanceTonight = debitBalanceAsOn.add(totalDifference);
				BigDecimal debitTotalCash = debitTotal.add(debitBalanceAsOn.add(debitBalanceTonight));
				BigDecimal creditTotalCash = creditTotal.add(creditBalanceAsOn.add(creditBalanceTonight));

				BigDecimal debitTrasAndClear = bankCashService.findTotalTransfer(currencyReportType, currency, reportDate, isCurConverted, branch);
				// TODO not sure , converse
				BigDecimal creditTrasAndClear = debitTrasAndClear;

				BigDecimal debitGrandTotal = debitTotalCash.add(debitTrasAndClear);
				BigDecimal creditGrandTotal = creditTotalCash.add(creditTrasAndClear);

				String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("ICON_RSOURCES"));
				parameters.put("Logo", image);
				parameters.put("Date", reportDate);
				parameters.put("Branch", branchString);
				parameters.put("NewDate", runDate);
				parameters.put("Currency", currencyString);
				parameters.put("DebitTotal", debitTotal.setScale(2, RoundingMode.CEILING));
				parameters.put("CreditTotal", creditTotal.setScale(2, RoundingMode.CEILING));
				parameters.put("DebitBalanceAsOn", debitBalanceAsOn.setScale(2, RoundingMode.CEILING));
				parameters.put("CreditBalanceAsOn", creditBalanceAsOn.setScale(2, RoundingMode.CEILING));
				parameters.put("DebitBalanceTonight", debitBalanceTonight.setScale(2, RoundingMode.CEILING));
				parameters.put("CreditBalanceTonight", creditBalanceTonight.setScale(2, RoundingMode.CEILING));
				parameters.put("DebitTotalCash", debitTotalCash.setScale(2, RoundingMode.CEILING));
				parameters.put("CreditTotalCash", creditTotalCash.setScale(2, RoundingMode.CEILING));
				parameters.put("DebitTras&Clear", debitTrasAndClear.setScale(2, RoundingMode.CEILING));
				parameters.put("CreditTras&Clear", creditTrasAndClear.setScale(2, RoundingMode.CEILING));
				parameters.put("DebitGrandTotal", debitGrandTotal.setScale(2, RoundingMode.CEILING));
				parameters.put("CreditGrandTotal", creditGrandTotal.setScale(2, RoundingMode.CEILING));

				JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
				JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
				JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(bankCashDtoList);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, source);
				FileUtils.forceMkdir(new File(dirPath));
				JasperExportManager.exportReportToPdfFile(jasperPrint, dirPath + fileName.concat(".pdf"));
			}
		} catch (SystemException e) {
			handleSysException(e);
		}
	}

	public StreamedContent getDownload() {
		if (validate()) {
			List<BankCashDto> bankCashDtoList = bankCashService.findDailyTransaction(currencyReportType, currency, reportDate, isCurConverted, branch);
			if (bankCashDtoList.size() == 0) {
				addErrorMessage(null, MessageId.NO_RESULT);
				return null;
			} else {
				return getDownloadValue(bankCashDtoList);

			}
		}
		return null;
	}

	private StreamedContent getDownloadValue(List<BankCashDto> bankCashDtoList) {
		try {
			List<JasperPrint> prints = new ArrayList<JasperPrint>();

			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("bankCashScroll.jrxml");
			// Map parameters = new HashMap();
			Map<String, Object> parameters = new HashMap<String, Object>();

			String runDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

			String branchString = "";
			if (branch == null) {
				branchString = "All Branches";
			} else {
				branchString = branch.getName();
			}

			String currencyString = "";
			if (currency == null) {
				currencyString = "All Currencies";
			} else {
				currencyString = currency.getCurrencyCode();
				if (isCurConverted || currencyReportType.equals(CurrencyType.HOMECURRENCY)) {
					currencyString = currencyString + " By Home Amount";
				}
			}

			BigDecimal debitTotal = BigDecimal.ZERO;
			BigDecimal creditTotal = BigDecimal.ZERO;
			for (BankCashDto bankCashDto : bankCashDtoList) {
				debitTotal = debitTotal.add(bankCashDto.getDebit());
				creditTotal = creditTotal.add(bankCashDto.getCredit());
			}

			BigDecimal debitBalanceAsOn = bankCashService.findOpeningBalance(currencyReportType, currency, reportDate, isCurConverted, branch);
			if (null != debitBalanceAsOn) {

				// TODO not sure there will be data for this one , converse with
				// MMSN
				BigDecimal creditBalanceAsOn = BigDecimal.ZERO;
				BigDecimal totalDifference = BigDecimal.ZERO;
				// TODO not sure about this calculation , converse with
				// MMSN
				/*
				 * if (debitTotal.doubleValue() > creditTotal.doubleValue()) {
				 * totalDifference = debitTotal.subtract(creditTotal); } else {
				 * totalDifference = creditTotal.subtract(debitTotal); }
				 */

				totalDifference = debitTotal.subtract(creditTotal);
				// TODO not sure there will be data for this one , converse with
				// MMSN
				BigDecimal debitBalanceTonight = BigDecimal.ZERO;
				BigDecimal creditBalanceTonight = debitBalanceAsOn.add(totalDifference);
				BigDecimal debitTotalCash = debitTotal.add(debitBalanceAsOn.add(debitBalanceTonight));
				BigDecimal creditTotalCash = creditTotal.add(creditBalanceAsOn.add(creditBalanceTonight));

				BigDecimal debitTrasAndClear = bankCashService.findTotalTransfer(currencyReportType, currency, reportDate, isCurConverted, branch);
				// TODO not sure , converse
				BigDecimal creditTrasAndClear = debitTrasAndClear;

				BigDecimal debitGrandTotal = debitTotalCash.add(debitTrasAndClear);
				BigDecimal creditGrandTotal = creditTotalCash.add(creditTrasAndClear);

				String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("ICON_RSOURCES"));
				parameters.put("Logo", image);
				parameters.put("Date", reportDate);
				parameters.put("Branch", branchString);
				parameters.put("NewDate", runDate);
				parameters.put("Currency", currencyString);
				parameters.put("DebitTotal", debitTotal.setScale(2, RoundingMode.CEILING));
				parameters.put("CreditTotal", creditTotal.setScale(2, RoundingMode.CEILING));
				parameters.put("DebitBalanceAsOn", debitBalanceAsOn.setScale(2, RoundingMode.CEILING));
				parameters.put("CreditBalanceAsOn", creditBalanceAsOn.setScale(2, RoundingMode.CEILING));
				parameters.put("DebitBalanceTonight", debitBalanceTonight.setScale(2, RoundingMode.CEILING));
				parameters.put("CreditBalanceTonight", creditBalanceTonight.setScale(2, RoundingMode.CEILING));
				parameters.put("DebitTotalCash", debitTotalCash.setScale(2, RoundingMode.CEILING));
				parameters.put("CreditTotalCash", creditTotalCash.setScale(2, RoundingMode.CEILING));
				parameters.put("DebitTras&Clear", debitTrasAndClear.setScale(2, RoundingMode.CEILING));
				parameters.put("CreditTras&Clear", creditTrasAndClear.setScale(2, RoundingMode.CEILING));
				parameters.put("DebitGrandTotal", debitGrandTotal.setScale(2, RoundingMode.CEILING));
				parameters.put("CreditGrandTotal", creditGrandTotal.setScale(2, RoundingMode.CEILING));

				JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
				JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
				JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(bankCashDtoList);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, source);
				prints.add(jasperPrint);
			}

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

	private boolean validate() {
		Date todayDate = getTodayDate();
		if (reportDate.after(todayDate)) {
			addErrorMessage(null, MessageId.START_DATE_EXCEEDED);
			return false;
		} else {
			return true;
		}
	}

	public CurrencyType getCurrencyReportType() {
		return currencyReportType;
	}

	public void setCurrencyReportType(CurrencyType currencyReportType) {
		this.currencyReportType = currencyReportType;
	}

	public boolean isCurDisabled() {
		return isCurDisabled;
	}

	public CurrencyType[] getReportTypes() {
		return CurrencyType.values();
	}

	public List<Currency> getCurs() {
		return currencyService.findAllCurrency();
	}

	public List<Branch> getBranches() {
		return branchService.findAllBranch();
	}

	public boolean isCurConverted() {
		return isCurConverted;
	}

	public void setCurConverted(boolean isCurConverted) {
		this.isCurConverted = isCurConverted;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public Date getTodayDate() {
		return new Date();
	}

	public String getStream() {
		String fullFilePath = pdfDirPath + fileName.concat(".pdf");
		return fullFilePath;
	}

	public boolean isBranchDisabled() {
		return isBranchDisabled;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
}
