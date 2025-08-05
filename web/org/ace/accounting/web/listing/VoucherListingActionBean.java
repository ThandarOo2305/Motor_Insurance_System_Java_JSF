package org.ace.accounting.web.listing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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

import org.ace.accounting.common.PropertiesManager;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.VLdto;
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

@ManagedBean(name = "VoucherListingActionBean")
@ViewScoped
public class VoucherListingActionBean extends BaseBean {

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
	private Date startDate;
	private Date endDate;
	private Branch branch;
	private Currency currency;
	private String dirPath = "/pdf-report/" + "VoucherListingReport" + "/" + System.currentTimeMillis() + "/";
	private String fileName = "";
	private boolean isOnlyVoucher;
	private boolean allVoucherType;
	private boolean reverse;

	@PostConstruct
	private void init() {
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
		startDate = new Date();
		endDate = new Date();
		allVoucherType = false;
		reverse = false;
	}

	public void changeVType(AjaxBehaviorEvent event) {
		switch (type) {
			case A:
				fileName = "All Voucher Listing";
				break;
			case CC:
				fileName = "Credit Voucher Listing By Cash";
				break;
			case CD:
				fileName = "Debit Voucher Listing By Cash";
				break;
			case T:
				fileName = "Journal Voucher Listing";
				break;
		}
	}

	public void search() {
		if (validate()) {
			List<VLdto> dtoList = listingReportService.findVoucherListingReport(type, startDate, endDate, branch, currency, allVoucherType ? null : Boolean.valueOf(isOnlyVoucher),
					reverse);
			if (dtoList.size() == 0) {
				addErrorMessage(null, MessageId.NO_RESULT);
			} else if (generateReport(dtoList)) {
				PrimeFaces.current().executeScript("PF('voucherListingPdfDialog').show();");

			}
		}
	}

	public boolean generateReport(List<VLdto> dtoList) {
		try {
			Map<String, Object> parameters = new HashMap<>();

			String accountType = "";
			if (type.equals(VoucherListingType.CC)) {
				accountType = "Credit Voucher Listing";
			} else if (type.equals(VoucherListingType.CD)) {
				accountType = "Debit Voucher Listing";
			} else if (type.equals(VoucherListingType.T)) {
				accountType = "Transfer";
			} else if (type.equals(VoucherListingType.A)) {
				accountType = "Voucher Listing";
			}
			parameters.put("ACCOUNT_TYPE", accountType);
			parameters.put("START_DATE", startDate);
			parameters.put("END_DATE", endDate);

			parameters.put("BRANCH", branch == null ? "All Branches" : branch.getName());
			parameters.put("CURRENCY", currency == null ? "All Currencies" : currency.getDescription());

			String newDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			parameters.put("NEWDATE", newDate);
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("ICON_RSOURCES"));
			parameters.put("LOGO", image);
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("voucherListingReport.jrxml");
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(dtoList));

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
			List<VLdto> dtoList = listingReportService.findVoucherListingReport(type, startDate, endDate, branch, currency, allVoucherType ? null : Boolean.valueOf(isOnlyVoucher),
					reverse);
			if (dtoList.size() == 0) {
				addErrorMessage(null, MessageId.NO_RESULT);
				return null;
			} else {
				return getDownloadValue(dtoList);

			}
		}
		return null;
	}

	private StreamedContent getDownloadValue(List<VLdto> dtoList) {
		try {
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("voucherListingReport.jrxml");
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("LOGO"));
			Map<String, Object> parameters = new HashMap<String, Object>();
			String accountType = "";
			if (type.equals(VoucherListingType.CC)) {
				accountType = "Credit Voucher Listing";
			} else if (type.equals(VoucherListingType.CD)) {
				accountType = "Debit Voucher Listing";
			} else if (type.equals(VoucherListingType.T)) {
				accountType = "Transfer";
			} else if (type.equals(VoucherListingType.A)) {
				accountType = "Voucher Listing";
			}
			parameters.put("ACCOUNT_TYPE", accountType);
			parameters.put("START_DATE", startDate);
			parameters.put("END_DATE", endDate);

			parameters.put("BRANCH", branch == null ? "All Branches" : branch.getName());
			parameters.put("CURRENCY", currency == null ? "All Currencies" : currency.getDescription());

			String newDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			parameters.put("NEWDATE", newDate);
			parameters.put("LOGO", image);

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

	private boolean validate() {
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
		} else {
			return true;
		}
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

	public boolean isOnlyVoucher() {
		return isOnlyVoucher;
	}

	public void setOnlyVoucher(boolean isOnlyVoucher) {
		this.isOnlyVoucher = isOnlyVoucher;
	}

	public boolean isAllVoucherType() {
		return allVoucherType;
	}

	public void setAllVoucherType(boolean allVoucherType) {
		this.allVoucherType = allVoucherType;
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

}
