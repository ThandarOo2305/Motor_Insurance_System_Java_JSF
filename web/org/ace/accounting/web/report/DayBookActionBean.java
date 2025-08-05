package org.ace.accounting.web.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.dto.DayBookDto;
import org.ace.accounting.dto.DayBookReportDto1;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.report.daybook.service.interfaces.IDayBookService;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.java.web.common.BaseBean;
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

@ManagedBean(name = "DayBookActionBean")
@ViewScoped
public class DayBookActionBean extends BaseBean {

	@ManagedProperty(value = "#{DayBookService}")
	private IDayBookService dayBookService;

	public void setDayBookService(IDayBookService dayBookService) {
		this.dayBookService = dayBookService;
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

	@ManagedProperty(value = "#{CurrencyService}")
	private ICurrencyService currencyService;

	public void setCurrencyService(ICurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	@ManagedProperty(value = "#{UserProcessService}")
	private IUserProcessService userProcessService;

	public void setUserProcessService(IUserProcessService userProcessService) {
		this.userProcessService = userProcessService;
	}

	public final String formId = "dayBookForm";

	private DayBookDto dayBookDto;
	private List<Branch> branchList;
	private List<Currency> currencyList;

	List<DayBookReportDto1> dayBookReportList;

	private boolean reverse;
	/**
	 * For Report Path
	 */
	private String dirPath = "/pdf-report/" + "accLedgerListingReport" + "/" + System.currentTimeMillis() + "/";
	private final String fileName = "Day Book Domestic";

	@PostConstruct
	public void init() {
		createNewDayBook();
		rebindData();

	}

	public void rebindData() {
		branchList = branchService.findAllBranch();
		currencyList = currencyService.findAllCurrency();
		if (dayBookDto.isAdmin()) {
			dayBookDto.setBranch(branchList.get(0));
		} else {
			dayBookDto.setBranch(branchService.findBranchByBranchCode(userProcessService.getLoginUser().getBranch().getBranchCode()));
			dayBookDto.setAllBranch(false);
		}

	}

	public void createNewDayBook() {
		dayBookDto = new DayBookDto();
		dayBookDto.setRequiredDate(null);
		dayBookDto.setCurrencyType(CurrencyType.HOMECURRENCY);
		dayBookDto.setHomeCurrency(true);
		dayBookDto.setAllBranch(false);
		dayBookDto.setAdmin(userProcessService.getLoginUser().isAdmin());
		dayBookDto.setRequiredDate(new Date());
		currencyList = new ArrayList<Currency>();
		branchList = new ArrayList<Branch>();
		reverse = false;
	}

	public void previewReport() {
		dayBookReportList = new ArrayList<DayBookReportDto1>();
		dayBookReportList = dayBookService.findDayBookListWithGrandTotal(dayBookDto);
		for (DayBookReportDto1 dto : dayBookReportList) {
			dto.setHomeCurrencyConverted(dayBookDto.isHomeCurrencyConverted());
		}

		if (dayBookReportList.size() == 0) {
			addErrorMessage(null, MessageId.NO_RESULT);
		} else if (generateReport(dayBookReportList)) {
			PrimeFaces.current().executeScript("PF('dayBookPdfDialog').show();");
			/*
			 * RequestContext context = RequestContext.getCurrentInstance();
			 * context.execute("PF('dayBookPdfDialog').show();");
			 */
			createNewDayBook();
			rebindData();
		}

	}

	public String getStream() {
		String fullFilePath = dirPath + fileName.concat(".pdf");
		return fullFilePath;
	}

	public boolean generateReport(List<DayBookReportDto1> dtoList) {
		try {

			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("dayBookReport.jrxml");
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("ICON_RSOURCES"));
			Map<String, Object> parameters = new HashMap<String, Object>();
			String currencyCode = dayBookDto.getCurrencyType().equals(CurrencyType.HOMECURRENCY) ? "All Currencies" : dayBookDto.getCurrency().getCurrencyCode();
			if (dayBookDto.isHomeCurrencyConverted()) {
				currencyCode = currencyCode + " By Home Amount";
			}
			parameters.put("logoPath", image);
			parameters.put("startDate", dayBookDto.getRequiredDate());
			parameters.put("branches", dayBookDto.isAllBranch() ? "All Branches" : dayBookDto.getBranch().getName());
			// parameters.put("currency",
			// (dayBookDto.getCurrencyType().equals(CurrencyType.HOMECURRENCY))
			// ? "All Currencies" : dayBookDto.getCurrency().getCurrencyCode());
			parameters.put("currency", currencyCode);
			parameters.put("dayBookList", new JRBeanCollectionDataSource(dtoList));
			parameters.put("homeCurrency", dayBookDto.isHomeCurrency());
			parameters.put("homeCurrencyConverted", dayBookDto.isHomeCurrencyConverted());

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

	public StreamedContent getDownload() {
		StreamedContent result = null;
		dayBookReportList = new ArrayList<DayBookReportDto1>();
		dayBookReportList = dayBookService.findDayBookListWithGrandTotal(dayBookDto);
		if (dayBookReportList.size() == 0) {
			addErrorMessage(null, MessageId.NO_RESULT);
		} else {
			result = getDownloadValue(dayBookReportList);
			createNewDayBook();
			rebindData();
		}
		return result;
	}

	private StreamedContent getDownloadValue(List<DayBookReportDto1> dtoList) {

		try {
			List<JasperPrint> prints = new ArrayList<JasperPrint>();
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("dayBookReport.jrxml");
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("ICON_RSOURCES"));
			Map<String, Object> parameters = new HashMap<String, Object>();
			String currencyCode = dayBookDto.getCurrencyType().equals(CurrencyType.HOMECURRENCY) ? "All Currencies" : dayBookDto.getCurrency().getCurrencyCode();
			if (dayBookDto.isHomeCurrencyConverted()) {
				currencyCode = currencyCode + " By Home Amount";
			}
			parameters.put("logoPath", image);
			parameters.put("startDate", dayBookDto.getRequiredDate());
			parameters.put("branches", dayBookDto.isAllBranch() ? "All Branches" : dayBookDto.getBranch().getName());
			// parameters.put("currency",
			// (dayBookDto.getCurrencyType().equals(CurrencyType.HOMECURRENCY))
			// ? "MMK" : dayBookDto.getCurrency().getCurrencyCode());
			parameters.put("currency", currencyCode);
			parameters.put("dayBookList", new JRBeanCollectionDataSource(dtoList));
			parameters.put("homeCurrency", dayBookDto.isHomeCurrency());

			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(dtoList));
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

	public void changeCurrencyType(AjaxBehaviorEvent event) {
		dayBookDto.setHomeCurrency(dayBookDto.getCurrencyType().equals(CurrencyType.HOMECURRENCY));
	}

	public CurrencyType[] getCurrencyTypes() {
		return CurrencyType.values();
	}

	public Date getTodayDate() {
		return new Date();

	}

	public List<Branch> getBranchList() {
		return branchList;
	}

	public List<Currency> getCurrencyList() {
		return currencyList;
	}

	public DayBookDto getDayBookDto() {
		return dayBookDto;
	}

	public void setDayBookDto(DayBookDto dayBookDto) {
		this.dayBookDto = dayBookDto;
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

}
