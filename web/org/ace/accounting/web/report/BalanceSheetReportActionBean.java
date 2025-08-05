package org.ace.accounting.web.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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

import org.ace.accounting.common.CurrencyType;
import org.ace.accounting.common.MonthNames;
import org.ace.accounting.common.PropertiesManager;
import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.common.validation.MessageId;
import org.ace.accounting.report.balancesheet.BalanceSheetCriteria;
import org.ace.accounting.report.balancesheet.BalanceSheetDTO;
import org.ace.accounting.system.branch.Branch;
import org.ace.accounting.system.branch.service.interfaces.IBranchService;
import org.ace.accounting.system.chartaccount.service.interfaces.ICcoaService;
import org.ace.accounting.system.currency.Currency;
import org.ace.accounting.system.currency.service.interfaces.ICurrencyService;
import org.ace.accounting.system.setup.persistence.interfaces.ISetup_HistoryDAO;
import org.ace.accounting.system.tlf.service.interfaces.ITLFService;
import org.ace.accounting.user.User;
import org.ace.java.component.ErrorCode;
import org.ace.java.component.SystemException;
import org.ace.java.web.common.BaseBean;
import org.apache.commons.io.FileUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
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

@ViewScoped
@ManagedBean(name = "BalanceSheetReportActionBean")
public class BalanceSheetReportActionBean extends BaseBean {

	@ManagedProperty(value = "#{PropertiesManager}")
	private PropertiesManager propertiesManager;

	public void setPropertiesManager(PropertiesManager propertiesManager) {
		this.propertiesManager = propertiesManager;
	}

	@ManagedProperty(value = "#{TLFService}")
	private ITLFService tlfService;

	public void setTlfService(ITLFService tlfService) {
		this.tlfService = tlfService;
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

	@ManagedProperty(value = "#{Setup_HistoryDAO}")
	private ISetup_HistoryDAO setup_HistoryDAO;

	public void setSetup_HistoryDAO(ISetup_HistoryDAO setup_HistoryDAO) {
		this.setup_HistoryDAO = setup_HistoryDAO;
	}

	private List<BalanceSheetDTO> dtoList;

	private String dirPath = "/pdf-report/" + "balancesheetReport" + "/" + System.currentTimeMillis() + "/";
	private final String fileName = "Report";

	private BalanceSheetCriteria criteria;
	private boolean isBranchDisabled;

	private String budgetYear;
	private List<String> budgetYearList;
	private boolean isBeforeDisabled;
	private boolean isBeforeBudgetEnd;

	@PostConstruct
	public void init() {
		criteria = new BalanceSheetCriteria();
		criteria.setCurrencyType(CurrencyType.HOMECURRENCY);
		criteria.setHomeCur(true);
		criteria.setHomeConverted(true);
		criteria.setReportType("B");
		criteria.setStartDate(new Date());
		criteria.setEndDate(new Date());
		User user = (User) getParam("LoginUser");
		isBeforeDisabled = true;
		if (user.isAdmin()) {
			isBranchDisabled = false;
		} else {
			isBranchDisabled = true;
			criteria.setBranch(user.getBranch());
		}
		bindBudgetYear();
		dtoList = new ArrayList<>();
	}

	public void previewReport() {
		dtoList.clear();
		String branchId = criteria.getBranch() == null ? null : criteria.getBranch().getId();
		String currencyId = criteria.getCurrency() == null ? null : criteria.getCurrency().getId();
		String budgetYear;
		Date reportDate = DateUtils.resetStartDate(criteria.getStartDate());

		if (criteria.isMonth()) {
			budgetYear = criteria.getBudgetYear();
		} else {
			if (DateUtils.isIntervalBugetYear(reportDate))
				budgetYear = BusinessUtil.getBudgetInfo(reportDate, 2);
			else
				budgetYear = setup_HistoryDAO.findbudgetyear(reportDate);
		}

		if (isBeforeBudgetEnd) {
			if (criteria.isMonth()) {
				dtoList = tlfService.generateBalanceSheetByClone(branchId, currencyId, criteria.isHomeCur(), budgetYear);
			} else {
				List<BalanceSheetDTO> dtoDayList = tlfService.generateCloneBalanceSheetByDate(branchId, currencyId, criteria.getStartDate(), criteria.getEndDate(),
						criteria.isHomeCur());
				/*
				 * BalanceSheetDTO blsheetDto = dtoDayList.stream().filter(c ->
				 * c.getAcCode().equals("02-015-001")).findAny().orElse(null);
				 */

				if (!dtoDayList.isEmpty()) {
					calculateGroupTotal(dtoList, dtoDayList, criteria.getReportType());
				}
			}
		} else {

			if (criteria.isMonth()) {
				dtoList = tlfService.generateBalanceSheet(branchId, currencyId, criteria.isHomeCur(), budgetYear);
			} else {
				List<BalanceSheetDTO> dtoDayList = tlfService.generateBalanceSheetByDate(branchId, currencyId, criteria.getStartDate(), criteria.getEndDate(),
						criteria.isHomeCur());
				/*
				 * BalanceSheetDTO blsheetDto = dtoDayList.stream().filter(c ->
				 * c.getAcCode().equals("02-015-001")).findAny().orElse(null);
				 */

				if (!dtoDayList.isEmpty()) {
					calculateGroupTotal(dtoList, dtoDayList, criteria.getReportType());
				}
			}
		}

		List<BalanceSheetDTO> orderList = claculateOrder(dtoList, criteria.getReportType());
		List<BalanceSheetDTO> formatList = new ArrayList<>();

		if (!criteria.getReportType().equals("B")) {
			formatList = generateFormat(orderList);
		} else {
			formatList = generateBSFormat(orderList);
		}

		if (formatList.size() == 0) {
			addErrorMessage(null, MessageId.NO_RESULT);
		} else if (generateReport(formatList)) {
			PrimeFaces.current().executeScript("PF('balanceSheetDialog').show();");
		}
	}

	private List<BalanceSheetDTO> generateBSFormat(List<BalanceSheetDTO> dtoList) {

		List<BalanceSheetDTO> resultList = new ArrayList<>();

		BalanceSheetDTO _2SeriesObject = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-000-000")).findFirst().get();
		BalanceSheetDTO _3SeriesObject = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("03-000-000")).findFirst().get();

		List<BalanceSheetDTO> _1series = dtoList.subList(0, dtoList.indexOf(_2SeriesObject));
		_1series = formatBS1Series(_1series);
		resultList.addAll(_1series);

		List<BalanceSheetDTO> _2series = dtoList.subList(dtoList.indexOf(_2SeriesObject), dtoList.indexOf(_3SeriesObject));
		_2series = formatBS2Series(_2series);
		resultList.addAll(_2series);

		List<BalanceSheetDTO> _3series = dtoList.subList(dtoList.indexOf(_3SeriesObject), dtoList.size());
		_3series = formatBS3Series(_3series);
		resultList.addAll(_3series);

		return resultList.stream().distinct().collect(Collectors.toList());
	}

	private List<BalanceSheetDTO> formatBS1Series(List<BalanceSheetDTO> dtoList) {

		List<BalanceSheetDTO> resultList = new ArrayList<>();

		BalanceSheetDTO assets = dtoList.get(0);
		resultList.add(assets);
		// FIxed Assets Tangible
		BalanceSheetDTO fixedAssetsTangible = new BalanceSheetDTO();
		fixedAssetsTangible.setAcCode("");
		fixedAssetsTangible.setAcName("Fixed Assets - Tangible");
		resultList.add(fixedAssetsTangible);

		// 01-010-000
		BalanceSheetDTO _01_010_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-010-000")).findFirst().get();
		resultList.addAll(dtoList.subList(1, dtoList.indexOf(_01_010_000Object)));

		// Total Fixed Assets - Tangible
		BalanceSheetDTO tFixedAssetsTangible = new BalanceSheetDTO();
		tFixedAssetsTangible.setAcCode("");
		tFixedAssetsTangible.setAcName("Total Fixed Assets - Tangible");

		List<BalanceSheetDTO> tFixedAssetsTangibleList = new ArrayList<>();
		tFixedAssetsTangibleList.addAll(dtoList.subList(1, dtoList.indexOf(_01_010_000Object)));
		tFixedAssetsTangibleList = tFixedAssetsTangibleList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());
		generateTotalMValue(tFixedAssetsTangible, tFixedAssetsTangibleList);
		resultList.add(tFixedAssetsTangible);

		// Fixed Assets - Intangible
		BalanceSheetDTO fixedAssetsIntangible = new BalanceSheetDTO();
		fixedAssetsIntangible.setAcCode("");
		fixedAssetsIntangible.setAcName("Fixed Assets - Intangible");
		resultList.add(fixedAssetsIntangible);

		// 01-015-000
		BalanceSheetDTO _01_015_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-015-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_010_000Object), dtoList.indexOf(_01_015_000Object)));

		// Total Fixed Assets - Intangible
		BalanceSheetDTO tFixedAssetsIntangible = new BalanceSheetDTO();
		tFixedAssetsIntangible.setAcCode("");
		tFixedAssetsIntangible.setAcName("Total Fixed Assets - Intangible");

		List<BalanceSheetDTO> tFixedAssetsIntangibleList = new ArrayList<>();
		tFixedAssetsIntangibleList.addAll(dtoList.subList(dtoList.indexOf(_01_010_000Object), dtoList.indexOf(_01_015_000Object)));
		tFixedAssetsIntangibleList = tFixedAssetsIntangibleList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());
		generateTotalMValue(tFixedAssetsIntangible, tFixedAssetsIntangibleList);
		resultList.add(tFixedAssetsIntangible);

		// Fixed Assets - Intangible
		BalanceSheetDTO otherAssets = new BalanceSheetDTO();
		otherAssets.setAcCode("");
		otherAssets.setAcName("Other  Assets");
		resultList.add(otherAssets);

		// 01-017-000
		BalanceSheetDTO _01_017_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-017-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_015_000Object), dtoList.indexOf(_01_017_000Object)));

		// Fixed Assets - Intangible
		BalanceSheetDTO tOtherAssets = new BalanceSheetDTO();
		tOtherAssets.setAcCode("");
		tOtherAssets.setAcName("Total Other  Assets ");

		List<BalanceSheetDTO> tOtherAssetsList = new ArrayList<>();
		tOtherAssetsList.addAll(dtoList.subList(dtoList.indexOf(_01_015_000Object), dtoList.indexOf(_01_017_000Object)));
		tOtherAssetsList = tOtherAssetsList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());
		generateTotalMValue(tOtherAssets, tOtherAssetsList);

		resultList.add(tOtherAssets);

		// 01-018-012
		BalanceSheetDTO _01_018_012Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-018-012")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_017_000Object), dtoList.indexOf(_01_018_012Object)));

		// 01-018-013
		BalanceSheetDTO _01_018_013Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-018-013")).findFirst().get();
		resultList.add(_01_018_013Object);

		// 01-019-010
		BalanceSheetDTO _01_019_010Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-019-010")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_018_012Object), dtoList.indexOf(_01_019_010Object)));

		// 01-019-040
		BalanceSheetDTO _01_019_040Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-019-040")).findFirst().get();
		// 01-019-043
		BalanceSheetDTO _01_019_043Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-019-043")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_019_040Object), dtoList.indexOf(_01_019_043Object)));

		resultList.add(_01_019_010Object);

		// 01-019-045
		BalanceSheetDTO _01_019_045Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-019-045")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_019_043Object), dtoList.indexOf(_01_019_045Object)));

		// 01-019-011
		BalanceSheetDTO _01_019_011Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-019-011")).findFirst().get();
		// 01-019-015
		BalanceSheetDTO _01_019_015Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-019-015")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_019_011Object), dtoList.indexOf(_01_019_015Object)));

		// 01-019-037
		BalanceSheetDTO _01_019_037Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-019-037")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_019_037Object), dtoList.indexOf(_01_019_040Object)));

		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_019_015Object), dtoList.indexOf(_01_019_037Object)));

		// 01-020-000
		BalanceSheetDTO _01_020_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-020-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_019_045Object), dtoList.indexOf(_01_020_000Object)));

		// 01-020-013
		BalanceSheetDTO _01_020_013Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-020-013")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_020_000Object), dtoList.indexOf(_01_020_013Object)));

		// 01-020-061
		BalanceSheetDTO _01_020_061Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-020-061")).findFirst().get();
		// 01-020-063
		BalanceSheetDTO _01_020_063Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-020-063")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_020_061Object), dtoList.indexOf(_01_020_063Object)));

		// 01-020-053
		BalanceSheetDTO _01_020_053Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-020-053")).findFirst().get();
		resultList.add(_01_020_053Object);

		// 01-027-003
		BalanceSheetDTO _01_027_003Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-027-003")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_020_013Object), dtoList.indexOf(_01_027_003Object)));

		// 01-027-004
		BalanceSheetDTO _01_027_004Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-027-004")).findFirst().get();
		resultList.add(_01_027_004Object);
		resultList.add(_01_027_003Object);

		// 01-027-005
		BalanceSheetDTO _01_027_005Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-027-005")).findFirst().get();

		// 01-028-000
		BalanceSheetDTO _01_028_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-028-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_027_005Object), dtoList.indexOf(_01_028_000Object)));

		// Other Current Assets
		BalanceSheetDTO otherCurrentAssets = new BalanceSheetDTO();
		otherCurrentAssets.setAcCode("");
		otherCurrentAssets.setAcName("Other Current Assets");

		resultList.add(otherCurrentAssets);

		// 01-037-000
		BalanceSheetDTO _01_037_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-037-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_028_000Object), dtoList.indexOf(_01_037_000Object)));

		// 01-051-000
		BalanceSheetDTO _01_051_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-051-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_051_000Object), dtoList.size() - 1));

		// 01-044-005
		BalanceSheetDTO _01_044_005Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-044-005")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_037_000Object), dtoList.indexOf(_01_044_005Object)));

		// 01-044-022
		BalanceSheetDTO _01_044_022Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-044-022")).findFirst().get();
		resultList.add(_01_044_022Object);

		// 01-044-028
		BalanceSheetDTO _01_044_028Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-044-028")).findFirst().get();
		resultList.add(_01_044_028Object);

		// 01-044-007
		BalanceSheetDTO _01_044_007Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-044-007")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_044_005Object), dtoList.indexOf(_01_044_007Object)));

		// 01-044-027
		BalanceSheetDTO _01_044_027Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-044-027")).findFirst().get();
		resultList.add(_01_044_027Object);

		// 01-044-029
		BalanceSheetDTO _01_044_029Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-044-029")).findFirst().get();
		resultList.add(_01_044_029Object);

		// 01-044-018
		BalanceSheetDTO _01_044_018Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-044-018")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_044_007Object), dtoList.indexOf(_01_044_018Object)));

		// 01-044-023
		BalanceSheetDTO _01_044_023Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-044-023")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_044_023Object), dtoList.indexOf(_01_044_027Object)));

		// 01-049-000
		BalanceSheetDTO _01_049_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-049-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_044_018Object), dtoList.indexOf(_01_049_000Object)));

		// Total Other Current Assets
		BalanceSheetDTO tOtherCurrentAssets = new BalanceSheetDTO();
		tOtherCurrentAssets.setAcCode("");
		tOtherCurrentAssets.setAcName("Total Other Current Assets");

		List<BalanceSheetDTO> tOtherCurrentAssetsList = new ArrayList<>();
		tOtherCurrentAssetsList.addAll(dtoList.subList(dtoList.indexOf(_01_028_000Object), dtoList.indexOf(_01_049_000Object)));
		tOtherCurrentAssetsList = tOtherCurrentAssetsList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());
		generateTotalMValue(tOtherCurrentAssets, tOtherCurrentAssetsList);

		resultList.add(tOtherCurrentAssets);

		// 01-049-010
		BalanceSheetDTO _01_049_010Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-049-010")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_049_000Object), dtoList.indexOf(_01_049_010Object)));

		// 01-049-011
		BalanceSheetDTO _01_049_011Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-049-011")).findFirst().get();
		// 01-049-014+1
		BalanceSheetDTO _01_049_014_1Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-049-014")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_049_011Object), dtoList.indexOf(_01_049_014_1Object) + 1));
		resultList.add(_01_049_010Object);
		resultList.add(dtoList.get(dtoList.indexOf(_01_049_014_1Object) + 1));

		resultList.add(_01_049_011Object);
		resultList.add(dtoList.get(dtoList.indexOf(_01_049_014_1Object) + 1));

		// 01-050-000
		BalanceSheetDTO _01_050_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("01-050-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_01_050_000Object), dtoList.indexOf(_01_051_000Object)));
		resultList.add(dtoList.get(dtoList.size() - 1));

		// Blank
		BalanceSheetDTO blank = new BalanceSheetDTO();
		blank.setAcCode("");
		blank.setAcName("");
		resultList.add(blank);

		return resultList;

	}

	private List<BalanceSheetDTO> formatBS2Series(List<BalanceSheetDTO> dtoList) {
		List<BalanceSheetDTO> resultList = new ArrayList<>();

		BalanceSheetDTO liabilities = dtoList.get(0);
		resultList.add(liabilities);
		// Current Liabilities
		BalanceSheetDTO currentLiabilities = new BalanceSheetDTO();
		currentLiabilities.setAcCode("");
		currentLiabilities.setAcName("Current Liabilities");
		resultList.add(currentLiabilities);

		// 02-001-012
		BalanceSheetDTO _02_001_012Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-001-012")).findFirst().get();
		resultList.addAll(dtoList.subList(1, dtoList.indexOf(_02_001_012Object)));

		// 02-001-014
		BalanceSheetDTO _02_001_014Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-001-014")).findFirst().get();
		resultList.add(_02_001_014Object);

		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_001_012Object), dtoList.indexOf(_02_001_014Object)));

		// 02-001-015
		BalanceSheetDTO _02_001_015Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-001-015")).findFirst().get();

		// 02-003-003
		BalanceSheetDTO _02_003_003Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-003")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_001_015Object), dtoList.indexOf(_02_003_003Object)));

		// 02-003-023
		BalanceSheetDTO _02_003_023Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-023")).findFirst().get();
		resultList.add(_02_003_023Object);

		resultList.add(_02_003_003Object);

		// 02-003-017
		BalanceSheetDTO _02_003_017Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-017")).findFirst().get();
		resultList.add(_02_003_017Object);

		// 02-003-020
		BalanceSheetDTO _02_003_020Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-020")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_003_020Object), dtoList.indexOf(_02_003_023Object)));

		// 02-003-015
		BalanceSheetDTO _02_003_015Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-015")).findFirst().get();
		resultList.add(_02_003_015Object);

		// 02-003-024
		BalanceSheetDTO _02_003_024Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-024")).findFirst().get();
		resultList.add(_02_003_024Object);

		// 02-003-027
		BalanceSheetDTO _02_003_027Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-027")).findFirst().get();
		// 02-003-030
		BalanceSheetDTO _02_003_030Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-030")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_003_027Object), dtoList.indexOf(_02_003_030Object)));

		// 02-003-008
		BalanceSheetDTO _02_003_008Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-008")).findFirst().get();
		// 02-003-011
		BalanceSheetDTO _02_003_011Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-011")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_003_008Object), dtoList.indexOf(_02_003_011Object)));

		// 02-003-006
		BalanceSheetDTO _02_003_006Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-006")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_003_006Object), dtoList.indexOf(_02_003_008Object)));

		// 02-003-012
		BalanceSheetDTO _02_003_012Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-012")).findFirst().get();
		resultList.add(_02_003_012Object);

		// 02-003-014
		BalanceSheetDTO _02_003_014Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-014")).findFirst().get();
		resultList.add(_02_003_014Object);

		// 02-003-013
		BalanceSheetDTO _02_003_013Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-013")).findFirst().get();
		resultList.add(_02_003_013Object);

		resultList.add(_02_003_011Object);

		// 02-003-005
		BalanceSheetDTO _02_003_005Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-005")).findFirst().get();
		resultList.add(_02_003_005Object);

		// 02-003-004
		BalanceSheetDTO _02_003_004Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-004")).findFirst().get();
		resultList.add(_02_003_004Object);

		// 02-003-025
		BalanceSheetDTO _02_003_025Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-025")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_003_025Object), dtoList.indexOf(_02_003_027Object)));

		// 02-003-016
		BalanceSheetDTO _02_003_016Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-016")).findFirst().get();
		resultList.add(_02_003_016Object);

		// 02-003-018
		BalanceSheetDTO _02_003_018Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-003-018")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_003_018Object), dtoList.indexOf(_02_003_020Object)));

		// 02-005-002
		BalanceSheetDTO _02_005_002Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-005-002")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_003_030Object), dtoList.indexOf(_02_005_002Object)));

		// 02-005-003
		BalanceSheetDTO _02_005_003Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-005-003")).findFirst().get();
		resultList.add(_02_005_003Object);
		resultList.add(_02_005_002Object);

		// 02-005-006
		BalanceSheetDTO _02_005_006Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-005-006")).findFirst().get();
		resultList.add(_02_005_006Object);

		// 02-005-004
		BalanceSheetDTO _02_005_004Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-005-004")).findFirst().get();
		resultList.add(_02_005_004Object);

		// 02-005-007
		BalanceSheetDTO _02_005_007Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-005-007")).findFirst().get();
		resultList.add(_02_005_007Object);

		// 02-005-005
		BalanceSheetDTO _02_005_005Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-005-005")).findFirst().get();
		resultList.add(_02_005_005Object);

		// 02-005-008
		BalanceSheetDTO _02_005_008Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-005-008")).findFirst().get();

		// 02-006-000
		BalanceSheetDTO _02_006_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-006-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_005_008Object), dtoList.indexOf(_02_006_000Object)));

		// 02-013-000
		BalanceSheetDTO _02_013_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-013-000")).findFirst().get();

		// 02-014-000
		BalanceSheetDTO _02_014_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-014-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_013_000Object), dtoList.indexOf(_02_014_000Object)));

		BalanceSheetDTO tCurrentLiabilities = new BalanceSheetDTO();
		tCurrentLiabilities.setAcCode("");
		tCurrentLiabilities.setAcName("Total Current Liabilities");

		List<BalanceSheetDTO> tCurrentLiabilitiesList = new ArrayList<>();
		tCurrentLiabilitiesList.addAll(resultList.subList(1, resultList.size()));
		// tCurrentLiabilitiesList.addAll(resultList.subList(1,
		// dtoList.indexOf(_02_006_000Object)));
		tCurrentLiabilitiesList = tCurrentLiabilitiesList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());
		generateTotalMValue(tCurrentLiabilities, tCurrentLiabilitiesList);

		resultList.add(tCurrentLiabilities);

		BalanceSheetDTO otherLiabilities = new BalanceSheetDTO();
		otherLiabilities.setAcCode("");
		otherLiabilities.setAcName("Other Liabilities");
		resultList.add(otherLiabilities);

		// 02-007-013
		BalanceSheetDTO _02_007_013Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-007-013")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_006_000Object), dtoList.indexOf(_02_007_013Object)));

		// 02-007-015
		BalanceSheetDTO _02_007_015Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-007-015")).findFirst().get();
		resultList.add(_02_007_015Object);

		// 02-008-008
		BalanceSheetDTO _02_008_008Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-008-008")).findFirst().get();
		// 02-012-003
		BalanceSheetDTO _02_012_003Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-012-003")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_007_013Object), dtoList.indexOf(_02_008_008Object) + 1));

		// 02-008-009
		BalanceSheetDTO _02_008_009Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-008-009")).findFirst().get();
		resultList.add(_02_008_009Object);
		// 02-008-0010
		BalanceSheetDTO _02_008_010Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-008-010")).findFirst().get();
		resultList.add(_02_008_010Object);
		// 02-008-011
		BalanceSheetDTO _02_008_011Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-008-011")).findFirst().get();
		resultList.add(_02_008_011Object);

		// 02-008-012
		BalanceSheetDTO _02_008_012Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-008-012")).findFirst().get();
		resultList.add(_02_008_012Object);
		// 02-008-013
		BalanceSheetDTO _02_008_013Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-008-013")).findFirst().get();
		resultList.add(_02_008_013Object);

		// 02-010-000
		BalanceSheetDTO _02_010_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-010-000")).findFirst().get();

		// 02-009-000
		BalanceSheetDTO _02_009_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-009-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_009_000Object) - 1, dtoList.indexOf(_02_010_000Object) + 1));

		// 02-010-001
		BalanceSheetDTO _02_010_001Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-010-001")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_010_001Object) - 1, dtoList.indexOf(_02_012_003Object) - 1));

		// 02-010-002
		BalanceSheetDTO _02_010_002Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-010-002")).findFirst().get();
		resultList.remove(_02_010_002Object);

		// 02-012-004
		BalanceSheetDTO _02_012_004Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-012-004")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_012_004Object), dtoList.indexOf(_02_013_000Object)));

		// remove _02_012_003Object amount from List
		generateSubstractValue(resultList.get(resultList.size() - 1), _02_012_003Object);
		// 02-016-000
		BalanceSheetDTO _02_016_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-016-000")).findFirst().get();
		// 02-015-000
		BalanceSheetDTO _02_015_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-015-000")).findFirst().get();
		// remove _02_015_000Object
		resultList.remove(_02_015_000Object);
		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_015_000Object), dtoList.indexOf(_02_016_000Object)));

		// 02-016-000 start
		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_016_000Object), dtoList.size() - 1));

		BalanceSheetDTO defferredIncome = new BalanceSheetDTO();
		defferredIncome.setAcCode("");
		defferredIncome.setAcName("Deferred Income");
		resultList.add(defferredIncome);
		resultList.add(_02_012_003Object);

		BalanceSheetDTO tDefferredIncome = new BalanceSheetDTO();
		tDefferredIncome.setAcCode("");
		tDefferredIncome.setAcName("Total Deferred Income");

		List<BalanceSheetDTO> tDefferredIncomeList = new ArrayList<>();
		tDefferredIncomeList.add(dtoList.get(dtoList.indexOf(_02_012_003Object)));
		// tDefferredIncomeList = tDefferredIncomeList.stream().filter(result ->
		// result.getAcName().contains("Total")).collect(Collectors.toList());
		generateTotalMValue(tDefferredIncome, tDefferredIncomeList);

		resultList.add(tDefferredIncome);

		BalanceSheetDTO tOtherLiabilities = new BalanceSheetDTO();
		tOtherLiabilities.setAcCode("");

		tOtherLiabilities.setAcName("Total Other Liabilities");

		List<BalanceSheetDTO> tOtherLiabilitiesList = new ArrayList<>();
		tOtherLiabilitiesList.addAll(resultList.subList(resultList.indexOf(_02_006_000Object), resultList.size()));
		tOtherLiabilitiesList = tOtherLiabilitiesList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());
		generateTotalMValue(tOtherLiabilities, tOtherLiabilitiesList);

		resultList.add(tOtherLiabilities);

		// 02-012-001
		BalanceSheetDTO _02_012_001Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-012-001")).findFirst().get();
		// 02-012-002
		BalanceSheetDTO _02_012_002Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-012-002")).findFirst().get();
		resultList.add(resultList.indexOf(_02_012_001Object) + 1, _02_012_002Object);

		BalanceSheetDTO nonCurrentLiability = new BalanceSheetDTO();
		nonCurrentLiability.setAcCode("");
		nonCurrentLiability.setAcName("Non Current Liability");
		resultList.add(nonCurrentLiability);

		// 02-014-001 +1
		BalanceSheetDTO _02_014_001_1Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("02-014-001")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_02_014_000Object), dtoList.indexOf(_02_014_001_1Object) + 1));

		BalanceSheetDTO tLeaseLiability = new BalanceSheetDTO();
		tLeaseLiability.setAcCode("");
		tLeaseLiability.setAcName("Total Lease Liability");

		List<BalanceSheetDTO> tLeaseLiabilityList = new ArrayList<>();
		tLeaseLiabilityList.addAll(dtoList.subList(dtoList.indexOf(_02_014_000Object), dtoList.size() - 100));
		tLeaseLiabilityList = tLeaseLiabilityList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());
		generateTotalMValue(tLeaseLiability, tLeaseLiabilityList);
		resultList.add(tLeaseLiability);
		resultList.add(_02_012_002Object);

		// 02-010-002
		resultList.add(_02_010_002Object);

		resultList.add(dtoList.get(dtoList.indexOf(_02_014_001_1Object) + 1));
		resultList.add(dtoList.get(dtoList.indexOf(_02_014_001_1Object) + 2));
		resultList.add(dtoList.get(dtoList.size() - 1));

		BalanceSheetDTO blank = new BalanceSheetDTO();
		blank.setAcCode("");
		blank.setAcName("");
		resultList.add(blank);

		return resultList;
	}

	private List<BalanceSheetDTO> formatBS3Series(List<BalanceSheetDTO> dtoList) {
		List<BalanceSheetDTO> resultList = new ArrayList<>();

		BalanceSheetDTO capitalAndReserve = dtoList.get(0);
		resultList.add(capitalAndReserve);
		// Capital
		BalanceSheetDTO capital = new BalanceSheetDTO();
		capital.setAcCode("");
		capital.setAcName("Capital");
		resultList.add(capital);

		// 03-002-000
		BalanceSheetDTO _03_002_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("03-002-000")).findFirst().get();
		resultList.addAll(dtoList.subList(1, dtoList.indexOf(_03_002_000Object)));

		BalanceSheetDTO tCapital = new BalanceSheetDTO();
		tCapital.setAcCode("");
		tCapital.setAcName("Total Capital");

		List<BalanceSheetDTO> tCapitalList = new ArrayList<>();
		tCapitalList.addAll(dtoList.subList(1, dtoList.indexOf(_03_002_000Object)));
		tCapitalList = tCapitalList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());
		generateTotalMValue(tCapital, tCapitalList);
		resultList.add(tCapital);

		BalanceSheetDTO reserve = new BalanceSheetDTO();
		reserve.setAcCode("");
		reserve.setAcName("Reserve");
		resultList.add(reserve);

		resultList.addAll(dtoList.subList(dtoList.indexOf(_03_002_000Object), dtoList.size() - 2));

		BalanceSheetDTO blank = new BalanceSheetDTO();
		blank.setAcCode("");
		blank.setAcName("");
		resultList.add(blank);

		resultList.add(dtoList.get(dtoList.size() - 1));

		return resultList;
	}

	private List<BalanceSheetDTO> generateFormat(List<BalanceSheetDTO> dtoList) {
		List<BalanceSheetDTO> resultList = new ArrayList<>();

		BalanceSheetDTO _5SeriesObject = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-000-000")).findFirst().get();
		BalanceSheetDTO _6SeriesObject = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("06-000-000")).findFirst().get();
		BalanceSheetDTO _7SeriesObject = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("07-000-000")).findFirst().get();

		List<BalanceSheetDTO> _4series = dtoList.subList(0, dtoList.indexOf(_6SeriesObject));
		_4series = format4Series(_4series);
		resultList.addAll(_4series);

		List<BalanceSheetDTO> _5series = dtoList.subList(dtoList.indexOf(_5SeriesObject), dtoList.indexOf(_7SeriesObject));
		_5series = format5Series(_5series);
		resultList.addAll(_5series);

		List<BalanceSheetDTO> _6series = dtoList.subList(dtoList.indexOf(_6SeriesObject), dtoList.indexOf(_5SeriesObject));
		_6series = format6Series(_6series);
		resultList.addAll(_6series);

		List<BalanceSheetDTO> _7series = dtoList.subList(dtoList.indexOf(_7SeriesObject), dtoList.size());
		_7series = format7Series(_7series);
		resultList.addAll(_7series);

		List<BalanceSheetDTO> resultList1 = new ArrayList<>();
		for (BalanceSheetDTO dto : resultList) {
			// if (dto.getAcName().equalsIgnoreCase("Total Donations - Water
			// Project")) {
			// dto.setAcName("Total CSR Activities");
			// }
			if (dto.getAcName().equalsIgnoreCase("Total Life Claim Expenses")) {
				dto.setAcName("Total Life Claim");
			}
			if (dto.getAcName().equalsIgnoreCase("Operating Profit")) {
				BalanceSheetDTO tIncome = resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Income")).findFirst().get();
				BalanceSheetDTO tExpenses = resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Expenses")).findFirst().get();
				BalanceSheetDTO operatingProfit = new BalanceSheetDTO();

				operatingProfit.setM1(tIncome.getM1().subtract(tExpenses.getM1()));
				operatingProfit.setM2(tIncome.getM2().subtract(tExpenses.getM2()));
				operatingProfit.setM3(tIncome.getM3().subtract(tExpenses.getM3()));
				operatingProfit.setM4(tIncome.getM4().subtract(tExpenses.getM4()));
				operatingProfit.setM5(tIncome.getM5().subtract(tExpenses.getM5()));
				operatingProfit.setM6(tIncome.getM6().subtract(tExpenses.getM6()));
				operatingProfit.setM7(tIncome.getM7().subtract(tExpenses.getM7()));
				operatingProfit.setM8(tIncome.getM8().subtract(tExpenses.getM8()));
				operatingProfit.setM9(tIncome.getM9().subtract(tExpenses.getM9()));
				operatingProfit.setM10(tIncome.getM10().subtract(tExpenses.getM10()));
				operatingProfit.setM11(tIncome.getM11().subtract(tExpenses.getM11()));
				operatingProfit.setM12(tIncome.getM12().subtract(tExpenses.getM12()));

				resultList1.add(operatingProfit);
				generateTotalMValue(dto, resultList1);
				resultList1.clear();
			}
			if (dto.getAcName().equalsIgnoreCase("Total Other Expenses")) {
				BalanceSheetDTO tIncome = resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Adjustment Expenses")).findFirst().get();
				BalanceSheetDTO tExpenses = resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total CSR Activities")).findFirst().get();
				BalanceSheetDTO operatingProfit = new BalanceSheetDTO();

				operatingProfit.setM1(tIncome.getM1().add(tExpenses.getM1()));
				operatingProfit.setM2(tIncome.getM2().add(tExpenses.getM2()));
				operatingProfit.setM3(tIncome.getM3().add(tExpenses.getM3()));
				operatingProfit.setM4(tIncome.getM4().add(tExpenses.getM4()));
				operatingProfit.setM5(tIncome.getM5().add(tExpenses.getM5()));
				operatingProfit.setM6(tIncome.getM6().add(tExpenses.getM6()));
				operatingProfit.setM7(tIncome.getM7().add(tExpenses.getM7()));
				operatingProfit.setM8(tIncome.getM8().add(tExpenses.getM8()));
				operatingProfit.setM9(tIncome.getM9().add(tExpenses.getM9()));
				operatingProfit.setM10(tIncome.getM10().add(tExpenses.getM10()));
				operatingProfit.setM11(tIncome.getM11().add(tExpenses.getM11()));
				operatingProfit.setM12(tIncome.getM12().add(tExpenses.getM12()));

				resultList1.add(operatingProfit);
				generateTotalMValue(dto, resultList1);
				resultList1.clear();
			}
		}

		return resultList;
	}

	private List<BalanceSheetDTO> format4Series(List<BalanceSheetDTO> dtoList) {

		List<BalanceSheetDTO> resultList = new ArrayList<>();
		BalanceSheetDTO income = dtoList.get(0);

		BalanceSheetDTO premiumIncome = new BalanceSheetDTO();
		premiumIncome.setAcCode("");
		premiumIncome.setAcName("Premium Income");
		resultList.add(income);
		resultList.add(premiumIncome);

		BalanceSheetDTO _04_003Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-003-000")).findFirst().get();
		resultList.addAll(dtoList.subList(1, dtoList.indexOf(_04_003Object)));

		// 04-006-004
		BalanceSheetDTO _04_06Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-006-000")).findFirst().get();
		// 04-007-000
		BalanceSheetDTO _04_07Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-007-000")).findFirst().get();
		// 04-008-000
		BalanceSheetDTO _04_08Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-008-000")).findFirst().get();

		// 04-012-000
		BalanceSheetDTO _04_012Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-012-000")).findFirst().get();

		// 04-0004-000
		BalanceSheetDTO _04_004Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-004-000")).findFirst().get();

		// 04-005-000
		BalanceSheetDTO _04_005Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-005-000")).findFirst().get();

		// 04-010-000
		BalanceSheetDTO _04_010Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-010-000")).findFirst().get();

		
		// 04-010-000
		BalanceSheetDTO _04_011Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-011-000")).findFirst().get();
		
		resultList.addAll(dtoList.subList(dtoList.indexOf(_04_07Object), dtoList.indexOf(_04_08Object)));

		resultList.addAll(dtoList.subList(dtoList.indexOf(_04_06Object), dtoList.indexOf(_04_07Object)));

		resultList.addAll(dtoList.subList(dtoList.indexOf(_04_012Object), dtoList.size() - 1));

		resultList.addAll(dtoList.subList(dtoList.indexOf(_04_003Object), dtoList.indexOf(_04_004Object)));

		resultList.addAll(dtoList.subList(dtoList.indexOf(_04_005Object), dtoList.indexOf(_04_06Object)));

		resultList.addAll(dtoList.subList(dtoList.indexOf(_04_004Object), dtoList.indexOf(_04_005Object)));

		resultList.add(_04_08Object);

		// 04-008-002
		BalanceSheetDTO __04__008_002Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-008-002")).findFirst().get();
		resultList.add(__04__008_002Object);

		// 04-008-007
		BalanceSheetDTO _04__008_007Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-008-007")).findFirst().get();
		resultList.add(_04__008_007Object);

		// 04-008-001
		BalanceSheetDTO _04__008_001Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-008-001")).findFirst().get();
		resultList.add(_04__008_001Object);

		// 04-008-006
		BalanceSheetDTO _04__008_006Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-008-006")).findFirst().get();
		resultList.add(_04__008_006Object);

		// 04-008-003
		BalanceSheetDTO _04__008_003Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-008-003")).findFirst().get();
		resultList.add(_04__008_003Object);
		// 04-008-004
		BalanceSheetDTO _04__008_004Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-008-004")).findFirst().get();
		resultList.add(_04__008_004Object);
		// 04-008-008
		BalanceSheetDTO _04__008_008Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-008-008")).findFirst().get();
		resultList.add(_04__008_008Object);
		// 04-008-005
		BalanceSheetDTO _04__008_005Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-008-005")).findFirst().get();
		resultList.add(_04__008_005Object);

		// 04-009-002
		BalanceSheetDTO _04__009_002Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-009-002")).findFirst().get();
		// 04-008-009
		BalanceSheetDTO _04__008_009Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-008-009")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_04__008_009Object), dtoList.indexOf(_04__009_002Object)));

		// 04-009-003
		BalanceSheetDTO _04__009_003Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-009-003")).findFirst().get();
		resultList.add(_04__009_003Object);
		// 04-009-004
		BalanceSheetDTO _04__009_004Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-009-004")).findFirst().get();
		resultList.add(_04__009_004Object);
		// 04-009-005
		BalanceSheetDTO _04__009_005Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("04-009-005")).findFirst().get();
		resultList.add(_04__009_005Object);

		resultList.add(_04__009_002Object);

		//resultList.addAll(dtoList.subList(dtoList.indexOf(_04__009_005Object) + 1, dtoList.indexOf(_04_010Object)));
		
		resultList.addAll(dtoList.subList(dtoList.indexOf(_04__009_005Object) + 1, dtoList.indexOf(_04_010Object)));

		BalanceSheetDTO totalPremiumIncome = new BalanceSheetDTO();
		totalPremiumIncome.setAcCode("");
		totalPremiumIncome.setAcName("Total Premium Income");

		// NEED TO REFECTOR
		/* {6}+{9}+{12}+{15}+{21}+{26}+{29}+{33}+{44}+{51} */
		/* Total Premium Income */
		List<BalanceSheetDTO> totalPremiumIncomeList = new ArrayList<>();
		totalPremiumIncomeList = resultList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());

		generateTotalMValue(totalPremiumIncome, totalPremiumIncomeList);

		resultList.add(totalPremiumIncome);

		BalanceSheetDTO unearnedPremiumIncome = new BalanceSheetDTO();
		unearnedPremiumIncome.setAcCode("");
		unearnedPremiumIncome.setAcName("Unearned Premium Income");

		resultList.add(unearnedPremiumIncome);

		resultList.addAll(dtoList.subList(dtoList.indexOf(_04_010Object), dtoList.indexOf(_04_012Object)));

		//resultList.add(dtoList.get(dtoList.indexOf(_04_012Object) - 1));

		/* {52}+{58} */
		/* Total Income */
		List<BalanceSheetDTO> totalIncomeList = new ArrayList<>();
		totalIncomeList.add(resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Premium Income")).findFirst().get());
		totalIncomeList.add(resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Unearned Premium Income")).findFirst().get());
		totalIncomeList.add(resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Parcel Insurance")).findFirst().get());
		BalanceSheetDTO totalIncome = dtoList.get(dtoList.size() - 1);
		generateTotalMValue(totalIncome, totalIncomeList);

		resultList.add(totalIncome);

		BalanceSheetDTO blank = new BalanceSheetDTO();
		blank.setAcCode("");
		blank.setAcName("");

		resultList.add(blank);

		return resultList;

	}

	private void generateSubstractValue(BalanceSheetDTO target, BalanceSheetDTO dto) {
		target.setM1(target.getM1().subtract(dto.getM1()));
		target.setM2(target.getM2().subtract(dto.getM2()));
		target.setM3(target.getM3().subtract(dto.getM3()));
		target.setM4(target.getM4().subtract(dto.getM4()));
		target.setM5(target.getM5().subtract(dto.getM5()));
		target.setM6(target.getM6().subtract(dto.getM6()));
		target.setM7(target.getM7().subtract(dto.getM7()));
		target.setM8(target.getM8().subtract(dto.getM8()));
		target.setM9(target.getM9().subtract(dto.getM9()));
		target.setM10(target.getM10().subtract(dto.getM10()));
		target.setM11(target.getM11().subtract(dto.getM11()));
		target.setM12(target.getM12().subtract(dto.getM12()));

	}

	private void generateTotalMValue(BalanceSheetDTO totalPremiumIncome, List<BalanceSheetDTO> totalPremiumIncomeList) {
		BigDecimal totalMPremiumIncome = BigDecimal.ZERO;
		totalMPremiumIncome = totalPremiumIncomeList.stream().map(result -> result.getM1()).reduce(BigDecimal.ZERO, BigDecimal::add);

		totalPremiumIncome.setM1(totalMPremiumIncome);

		// M2
		totalMPremiumIncome = totalPremiumIncomeList.stream().map(result -> result.getM2()).reduce(BigDecimal.ZERO, BigDecimal::add);
		totalPremiumIncome.setM2(totalMPremiumIncome);

		// M3
		totalMPremiumIncome = totalPremiumIncomeList.stream().map(result -> result.getM3()).reduce(BigDecimal.ZERO, BigDecimal::add);
		totalPremiumIncome.setM3(totalMPremiumIncome);
		// M4
		totalMPremiumIncome = totalPremiumIncomeList.stream().map(result -> result.getM4()).reduce(BigDecimal.ZERO, BigDecimal::add);
		totalPremiumIncome.setM4(totalMPremiumIncome);
		// M5
		totalMPremiumIncome = totalPremiumIncomeList.stream().map(result -> result.getM5()).reduce(BigDecimal.ZERO, BigDecimal::add);
		totalPremiumIncome.setM5(totalMPremiumIncome);
		// M6
		totalMPremiumIncome = totalPremiumIncomeList.stream().map(result -> result.getM6()).reduce(BigDecimal.ZERO, BigDecimal::add);
		totalPremiumIncome.setM6(totalMPremiumIncome);
		// M7
		totalMPremiumIncome = totalPremiumIncomeList.stream().map(result -> result.getM7()).reduce(BigDecimal.ZERO, BigDecimal::add);
		totalPremiumIncome.setM7(totalMPremiumIncome);
		// M8
		totalMPremiumIncome = totalPremiumIncomeList.stream().map(result -> result.getM8()).reduce(BigDecimal.ZERO, BigDecimal::add);
		totalPremiumIncome.setM8(totalMPremiumIncome);
		// M9
		totalMPremiumIncome = totalPremiumIncomeList.stream().map(result -> result.getM9()).reduce(BigDecimal.ZERO, BigDecimal::add);
		totalPremiumIncome.setM9(totalMPremiumIncome);
		// M10
		totalMPremiumIncome = totalPremiumIncomeList.stream().map(result -> result.getM10()).reduce(BigDecimal.ZERO, BigDecimal::add);
		totalPremiumIncome.setM10(totalMPremiumIncome);
		// M11
		totalMPremiumIncome = totalPremiumIncomeList.stream().map(result -> result.getM11()).reduce(BigDecimal.ZERO, BigDecimal::add);
		totalPremiumIncome.setM11(totalMPremiumIncome);
		// M12
		totalMPremiumIncome = totalPremiumIncomeList.stream().map(result -> result.getM12()).reduce(BigDecimal.ZERO, BigDecimal::add);
		totalPremiumIncome.setM12(totalMPremiumIncome);
	}

	private List<BalanceSheetDTO> format5Series(List<BalanceSheetDTO> dtoList) {

		List<BalanceSheetDTO> resultList = new ArrayList<>();
		BalanceSheetDTO expense = dtoList.get(0);
		resultList.add(expense);

		BalanceSheetDTO claimExpenses = new BalanceSheetDTO();
		claimExpenses.setAcCode("");
		claimExpenses.setAcName("Claim Expenses");
		resultList.add(claimExpenses);

		// 05-001-000
		BalanceSheetDTO _05_001Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-001-000")).findFirst().get();
		resultList.add(_05_001Object);

		// 05-001-003
		BalanceSheetDTO _05_001_003Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-001-003")).findFirst().get();
		resultList.add(_05_001_003Object);

		// 05-003-000
		BalanceSheetDTO _05_003Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-003-000")).findFirst().get();
		List<BalanceSheetDTO> _05List = dtoList.subList(dtoList.indexOf(_05_001Object), dtoList.indexOf(_05_003Object));
		_05List.remove(_05_001Object);
		_05List.remove(_05_001_003Object);

		resultList.addAll(_05List);

		// 05-006-000
		BalanceSheetDTO _05_006_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-006-000")).findFirst().get();
		// 05-007-000
		BalanceSheetDTO _05_007_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-007-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_006_000Object), dtoList.indexOf(_05_007_000Object)));

		// 05-005-000
		BalanceSheetDTO _05_005_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-005-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_005_000Object), dtoList.indexOf(_05_006_000Object)));

		// 05-004-000
		BalanceSheetDTO _05_004_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-004-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_003Object), dtoList.indexOf(_05_004_000Object)));

		// 05-070-000
		BalanceSheetDTO _05_070_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-070-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_070_000Object), dtoList.size()));

		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_004_000Object), dtoList.indexOf(_05_005_000Object)));

		// 05-008-000
		BalanceSheetDTO _05_008_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-008-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_007_000Object), dtoList.indexOf(_05_008_000Object)));

		BalanceSheetDTO tClaimExpenses = new BalanceSheetDTO();
		tClaimExpenses.setAcCode("");
		tClaimExpenses.setAcName("Total Claim Expenses");

		resultList.remove(resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Expenses")).findFirst().get());
		// NEED TO REFECTOR
		/* {67}+{70}+{73}+{76}+{81}+{87}+{91}+{99} */
		/* Total Claim Expenses */
		List<BalanceSheetDTO> tClaimExpensesList = new ArrayList<>();
		tClaimExpensesList = resultList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());

		generateTotalMValue(tClaimExpenses, tClaimExpensesList);
		resultList.add(tClaimExpenses);

		BalanceSheetDTO commissionExpenses = new BalanceSheetDTO();
		commissionExpenses.setAcCode("");
		commissionExpenses.setAcName("Commission Expenses");
		resultList.add(commissionExpenses);

		// 05-010-000
		BalanceSheetDTO _05_010_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-010-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_008_000Object), dtoList.indexOf(_05_010_000Object)));

		// 05-069-000
		BalanceSheetDTO _05_069_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-069-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_069_000Object), dtoList.indexOf(_05_070_000Object)));

		// 05-011-000
		BalanceSheetDTO _05_011_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-011-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_010_000Object), dtoList.indexOf(_05_011_000Object)));

		// 05-063-000
		BalanceSheetDTO _05_063_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-063-000")).findFirst().get();
		// 05-066-000
		BalanceSheetDTO _05_066_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-066-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_063_000Object), dtoList.indexOf(_05_066_000Object)));

		// 05-012-001
		BalanceSheetDTO _05_012_001Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-012-001")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_011_000Object), dtoList.indexOf(_05_012_001Object)));

		// 05-012-002
		BalanceSheetDTO _05_012_002Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-012-002")).findFirst().get();
		resultList.add(_05_012_002Object);
		resultList.add(_05_012_001Object);

		// 05-012-003
		BalanceSheetDTO _05_012_003Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-012-003")).findFirst().get();

		// 05-013-000
		BalanceSheetDTO _05_013_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-013-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_012_003Object), dtoList.indexOf(_05_013_000Object)));

		BalanceSheetDTO tCommissionExpenses = new BalanceSheetDTO();
		tCommissionExpenses.setAcCode("");
		tCommissionExpenses.setAcName("Total Commission  Expenses");

		// NEED TO REFECTOR
		/* {104}+{107}+{113}+{118}+{121}+{124}+{127}+{132}+{139} */
		/* Total Commission Expenses */
		List<BalanceSheetDTO> tCommissionExpensesList = new ArrayList<>();
		tCommissionExpensesList.addAll(dtoList.subList(dtoList.indexOf(_05_008_000Object), dtoList.indexOf(_05_010_000Object)));
		tCommissionExpensesList.addAll(dtoList.subList(dtoList.indexOf(_05_069_000Object), dtoList.indexOf(_05_070_000Object)));
		tCommissionExpensesList.addAll(dtoList.subList(dtoList.indexOf(_05_010_000Object), dtoList.indexOf(_05_011_000Object)));
		tCommissionExpensesList.addAll(dtoList.subList(dtoList.indexOf(_05_063_000Object), dtoList.indexOf(_05_066_000Object)));
		tCommissionExpensesList.addAll(dtoList.subList(dtoList.indexOf(_05_011_000Object), dtoList.indexOf(_05_013_000Object)));
		tCommissionExpensesList = tCommissionExpensesList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());
		generateTotalMValue(tCommissionExpenses, tCommissionExpensesList);

		resultList.add(tCommissionExpenses);

		BalanceSheetDTO lCDexp = new BalanceSheetDTO();
		lCDexp.setAcCode("");
		lCDexp.setAcName("Labour Cost&Officia; Dist:exp");
		resultList.add(lCDexp);

		// 05-015-000
		BalanceSheetDTO _05_015_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-015-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_013_000Object), dtoList.indexOf(_05_015_000Object)));

		// 05-067-000
		BalanceSheetDTO _05_067_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-067-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_066_000Object), dtoList.indexOf(_05_067_000Object)));

		// 05-016-000
		BalanceSheetDTO _05_016_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-016-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_015_000Object), dtoList.indexOf(_05_016_000Object)));

		BalanceSheetDTO tLCDexp = new BalanceSheetDTO();
		tLCDexp.setAcCode("");
		tLCDexp.setAcName("Total Labour Cost&Official Dist:Exp");

		// NEED TO REFECTOR
		/* {147}+{153}+{159}+{163} */
		/* Total Labour Cost&Official Dist:Exp */
		List<BalanceSheetDTO> tLCDexpList = new ArrayList<>();
		tLCDexpList.addAll(dtoList.subList(dtoList.indexOf(_05_013_000Object), dtoList.indexOf(_05_015_000Object)));
		tLCDexpList.addAll(dtoList.subList(dtoList.indexOf(_05_066_000Object), dtoList.indexOf(_05_067_000Object)));
		tLCDexpList.addAll(dtoList.subList(dtoList.indexOf(_05_015_000Object), dtoList.indexOf(_05_016_000Object)));
		tLCDexpList = tLCDexpList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());
		generateTotalMValue(tLCDexp, tLCDexpList);

		resultList.add(tLCDexp);

		BalanceSheetDTO oAE = new BalanceSheetDTO();
		oAE.setAcCode("");
		oAE.setAcName("Operating & Administration Exp");
		resultList.add(oAE);

		// 05-029-000
		BalanceSheetDTO _05_029_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-029-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_016_000Object), dtoList.indexOf(_05_029_000Object)));

		BalanceSheetDTO referralFeesExpenses = new BalanceSheetDTO();
		referralFeesExpenses.setAcCode("");
		referralFeesExpenses.setAcName("Referral feesExpenses");

		resultList.add(referralFeesExpenses);

		// 05-068-000
		BalanceSheetDTO _05_068_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-068-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_068_000Object), dtoList.indexOf(_05_069_000Object)));

		// 05-036-000
		BalanceSheetDTO _05_036_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-036-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_029_000Object), dtoList.indexOf(_05_036_000Object)));

		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_067_000Object), dtoList.indexOf(_05_068_000Object)));

		// 05-038-000
		BalanceSheetDTO _05_038_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-038-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_036_000Object), dtoList.indexOf(_05_038_000Object)));

		BalanceSheetDTO tOAExp = new BalanceSheetDTO();
		tOAExp.setAcCode("");
		tOAExp.setAcName("Total Operating & Administration Exp");

		/*
		 * 174,185,195,201,207,212,218,221,232,235,238,241,253,260,269,276,282,
		 * 286,294,301,
		 */
		/* Total Operating & Administration Exp */

		List<BalanceSheetDTO> tOAExpList = new ArrayList<>();

		tOAExpList.addAll(resultList.subList(resultList.indexOf(_05_016_000Object), resultList.size()));
		tOAExpList = tOAExpList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());
		generateTotalMValue(tOAExp, tOAExpList);
		resultList.add(tOAExp);

		BalanceSheetDTO miscellaneousExpenses = new BalanceSheetDTO();
		miscellaneousExpenses.setAcCode("");
		miscellaneousExpenses.setAcName("Miscellaneous Expenses");
		resultList.add(miscellaneousExpenses);

		// 05-048-000
		BalanceSheetDTO _05_048_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-048-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_038_000Object), dtoList.indexOf(_05_048_000Object)));

		BalanceSheetDTO tME = new BalanceSheetDTO();
		tME.setAcCode("");
		tME.setAcName("Total Miscellaneous Expenses");

		/* Total Miscellaneous Expenses */
		List<BalanceSheetDTO> tMEList = new ArrayList<>();
		tMEList.addAll(dtoList.subList(dtoList.indexOf(_05_038_000Object), dtoList.indexOf(_05_048_000Object)));
		tMEList = tMEList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());
		generateTotalMValue(tME, tMEList);
		resultList.add(tME);

		BalanceSheetDTO auditFees = new BalanceSheetDTO();
		auditFees.setAcCode("");
		auditFees.setAcName("Audit Fees");
		resultList.add(auditFees);

		// 05-052-000
		BalanceSheetDTO _05_052_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-052-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_048_000Object), dtoList.indexOf(_05_052_000Object)));

		BalanceSheetDTO tAuditFees = new BalanceSheetDTO();
		tAuditFees.setAcCode("");
		tAuditFees.setAcName("Total Audit Fees");

		/* Total Audit Fees */
		List<BalanceSheetDTO> tAuditFeesList = new ArrayList<>();
		tAuditFeesList.addAll(dtoList.subList(dtoList.indexOf(_05_048_000Object), dtoList.indexOf(_05_052_000Object)));
		tAuditFeesList = tAuditFeesList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());
		generateTotalMValue(tAuditFees, tAuditFeesList);

		resultList.add(tAuditFees);

		// 05-054-000
		BalanceSheetDTO _05_054_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-054-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_052_000Object), dtoList.indexOf(_05_054_000Object)));

		BalanceSheetDTO tCE = new BalanceSheetDTO();
		tCE.setAcCode("");
		tCE.setAcName("Total Ceremony Expenses ");

		/* Total Audit Fees */
		List<BalanceSheetDTO> tCEList = new ArrayList<>();
		tCEList.addAll(dtoList.subList(dtoList.indexOf(_05_052_000Object), dtoList.indexOf(_05_054_000Object)));
		tCEList = tCEList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());
		generateTotalMValue(tCE, tCEList);
		resultList.add(tCE);

		BalanceSheetDTO currencyExchange = new BalanceSheetDTO();
		currencyExchange.setAcCode("");
		currencyExchange.setAcName("Currency Exchange");
		resultList.add(currencyExchange);

		// 05-055-000
		BalanceSheetDTO _05_055_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("05-055-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_054_000Object), dtoList.indexOf(_05_055_000Object)));

		BalanceSheetDTO tCurrencyExchange = new BalanceSheetDTO();
		tCurrencyExchange.setAcCode("");
		tCurrencyExchange.setAcName("Total Currency Exchange");

		/* Total Currency Exchange */
		List<BalanceSheetDTO> tCurrencyExchangeList = new ArrayList<>();
		tCurrencyExchangeList.addAll(dtoList.subList(dtoList.indexOf(_05_054_000Object), dtoList.indexOf(_05_055_000Object)));
		tCurrencyExchangeList = tCurrencyExchangeList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());
		generateTotalMValue(tCurrencyExchange, tCurrencyExchangeList);

		resultList.add(tCurrencyExchange);

		resultList.addAll(dtoList.subList(dtoList.indexOf(_05_055_000Object), dtoList.indexOf(_05_063_000Object)));

		BalanceSheetDTO totalTax = new BalanceSheetDTO();
		totalTax.setAcCode("");
		totalTax.setAcName("Total Tax");

		/* Total Tax */
		List<BalanceSheetDTO> totalTaxList = new ArrayList<>();
		totalTaxList.addAll(dtoList.subList(dtoList.indexOf(_05_055_000Object), dtoList.indexOf(_05_063_000Object)));
		totalTaxList = totalTaxList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());
		generateTotalMValue(totalTax, totalTaxList);

		resultList.add(totalTax);

		BalanceSheetDTO totalExpenses = new BalanceSheetDTO();
		totalExpenses.setAcCode("");
		totalExpenses.setAcName("Total Expenses");
		// Total Commercial Tax
		BalanceSheetDTO tcT = resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Tax")).findFirst().get();
		tcT.setAcName("Total Commercial Tax");

		/* {100}+{147}+{164}+{340}+{379}+{393}+{400}+{406}+{420} */
		/* Total Expenses */
		List<BalanceSheetDTO> totalExpensesList = new ArrayList<>();

		totalExpensesList.add(resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Claim Expenses")).findFirst().get());
		// Total Commission Expenses
		totalExpensesList.add(resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Commission  Expenses")).findFirst().get());

		totalExpensesList.add(resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Labour Cost&Official Dist:Exp")).findFirst().get());

		totalExpensesList.add(resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Operating & Administration Exp")).findFirst().get());
		totalExpensesList.add(resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Miscellaneous Expenses")).findFirst().get());
		totalExpensesList.add(resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Audit Fees")).findFirst().get());
		totalExpensesList.add(resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Ceremony Expenses ")).findFirst().get());
		totalExpensesList.add(resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Currency Exchange")).findFirst().get());
		totalExpensesList.add(resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Tax")).findFirst().get());

		generateTotalMValue(totalExpenses, totalExpensesList);

		resultList.add(totalExpenses);

		BalanceSheetDTO blank = new BalanceSheetDTO();
		blank.setAcCode("");
		blank.setAcName("");
		resultList.add(blank);

		BalanceSheetDTO operationProfit = new BalanceSheetDTO();
		operationProfit.setAcCode("");
		operationProfit.setAcName("Operating Profit");
		resultList.add(operationProfit);

		BalanceSheetDTO blank1 = new BalanceSheetDTO();
		blank1.setAcCode("");
		blank1.setAcName("");
		resultList.add(blank1);

		return resultList;
	}

	private List<BalanceSheetDTO> format6Series(List<BalanceSheetDTO> dtoList) {
		List<BalanceSheetDTO> resultList = new ArrayList<>();
		BalanceSheetDTO otherIncome = dtoList.get(0);
		resultList.add(otherIncome);

		BalanceSheetDTO interestIncome = new BalanceSheetDTO();
		interestIncome.setAcCode("");
		interestIncome.setAcName("Interest Income");
		resultList.add(interestIncome);

		// 06-002-016
		BalanceSheetDTO _06_002_016Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("06-002-016")).findFirst().get();
		resultList.addAll(dtoList.subList(1, dtoList.indexOf(_06_002_016Object)));

		// 06-002-048
		BalanceSheetDTO _06_002_048Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("06-002-048")).findFirst().get();
		resultList.add(_06_002_048Object);

		// 06-002-023
		BalanceSheetDTO _06_002_023Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("06-002-023")).findFirst().get();

		resultList.addAll(dtoList.subList(dtoList.indexOf(_06_002_016Object), dtoList.indexOf(_06_002_023Object)));

		// 06-002-042
		BalanceSheetDTO _06_002_042Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("06-002-042")).findFirst().get();
		resultList.add(_06_002_042Object);

		// 06-002-031
		BalanceSheetDTO _06_002_031Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("06-002-031")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_06_002_023Object), dtoList.indexOf(_06_002_031Object)));

		// 06-002-053
		BalanceSheetDTO _06_002_053Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("06-002-053")).findFirst().get();
		resultList.add(_06_002_053Object);

		// 06-002-033
		BalanceSheetDTO _06_002_033Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("06-002-033")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_06_002_031Object), dtoList.indexOf(_06_002_033Object)));

		// 06-002-044
		BalanceSheetDTO _06_002_044Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("06-002-044")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_06_002_044Object), dtoList.indexOf(_06_002_048Object)));

		// 06-002-049
		BalanceSheetDTO _06_002_049Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("06-002-049")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_06_002_049Object), dtoList.indexOf(_06_002_053Object)));

		// 06-002-037
		BalanceSheetDTO _06_002_037Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("06-002-037")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_06_002_033Object), dtoList.indexOf(_06_002_037Object)));

		// 06-002-043
		BalanceSheetDTO _06_002_043Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("06-002-043")).findFirst().get();
		resultList.add(_06_002_043Object);

		resultList.addAll(dtoList.subList(dtoList.indexOf(_06_002_037Object), dtoList.indexOf(_06_002_042Object)));
		// 06-002-054
		BalanceSheetDTO _06_002_054Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("06-002-054")).findFirst().get();
		// 06-004-000
		BalanceSheetDTO _06_004_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("06-004-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_06_002_054Object), dtoList.indexOf(_06_004_000Object)));

		BalanceSheetDTO totalInterestIncome = new BalanceSheetDTO();
		totalInterestIncome.setAcCode("");
		totalInterestIncome.setAcName("Total Interest Income");

		// NEED TO REFECTOR
		/* {430}+{492}+{495} */
		/* Total Interest Income */
		List<BalanceSheetDTO> totalInterestIncomeList = new ArrayList<>();
		totalInterestIncomeList = resultList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());

		generateTotalMValue(totalInterestIncome, totalInterestIncomeList);

		resultList.add(totalInterestIncome);

		BalanceSheetDTO commissionIncome = new BalanceSheetDTO();
		commissionIncome.setAcCode("");
		commissionIncome.setAcName("Commission Income");
		resultList.add(commissionIncome);

		// 06-005-000
		BalanceSheetDTO _06_005_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("06-005-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_06_004_000Object), dtoList.indexOf(_06_005_000Object)));

		BalanceSheetDTO totalCommissionIncome = new BalanceSheetDTO();
		totalCommissionIncome.setAcCode("");
		totalCommissionIncome.setAcName("Total Commission Income");

		// Total Co-Insurance Commission Income
		// NEED TO REFECTOR
		/* {430}+{492}+{495} */
		/* Total Commission Income */
		List<BalanceSheetDTO> totalCommissionIncomeList = new ArrayList<>();
		totalCommissionIncomeList = resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Co-Insurance Commission Income")).collect(Collectors.toList());

		generateTotalMValue(totalCommissionIncome, totalCommissionIncomeList);

		resultList.add(totalCommissionIncome);

		BalanceSheetDTO nameChangeIncome = new BalanceSheetDTO();
		nameChangeIncome.setAcCode("");
		nameChangeIncome.setAcName("Name Change Income");
		resultList.add(nameChangeIncome);

		// 06-007-000
		BalanceSheetDTO _06_007_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("06-007-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_06_005_000Object), dtoList.indexOf(_06_007_000Object)));

		BalanceSheetDTO tNCIncome = new BalanceSheetDTO();
		tNCIncome.setAcCode("");
		tNCIncome.setAcName("Total Name Change Income");
		// TODO FIX ME
		/* {515} */
		/* Total Name Change Income */
		List<BalanceSheetDTO> tNCIncomeList = dtoList.subList(dtoList.indexOf(_06_005_000Object), dtoList.indexOf(_06_007_000Object));
		tNCIncomeList = tNCIncomeList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Bank& Owner Name Change Income ")).collect(Collectors.toList());

		generateTotalMValue(tNCIncome, tNCIncomeList);

		resultList.add(tNCIncome);

		BalanceSheetDTO rsFees = new BalanceSheetDTO();
		rsFees.setAcCode("");
		rsFees.setAcName("Reinstatement Stamp Fees");
		resultList.add(rsFees);

		// 06-008-000
		BalanceSheetDTO _06_008_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("06-008-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_06_007_000Object), dtoList.indexOf(_06_008_000Object)));

		BalanceSheetDTO tRSFees = new BalanceSheetDTO();
		tRSFees.setAcCode("");
		tRSFees.setAcName("Total Reinstatement Stamp Fees");

		/* {521} */
		/* Total Reinstatement Stamp Fees */
		List<BalanceSheetDTO> tRSFeesList = dtoList.subList(dtoList.indexOf(_06_007_000Object), dtoList.indexOf(_06_008_000Object));
		tRSFeesList = tRSFeesList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());

		generateTotalMValue(tRSFees, tRSFeesList);
		resultList.add(tRSFees);

		BalanceSheetDTO eMO = new BalanceSheetDTO();
		eMO.setAcCode("");
		eMO.setAcName("Excess Money & Others");
		resultList.add(eMO);

		resultList.addAll(dtoList.subList(dtoList.indexOf(_06_008_000Object), dtoList.size() - 1));

		BalanceSheetDTO tEMO = new BalanceSheetDTO();
		tEMO.setAcCode("");
		tEMO.setAcName("Total Excess Money & Others");

		/* {527}+{533}+{549} */
		/* Total Excess Money & Others */
		List<BalanceSheetDTO> tEMOList = dtoList.subList(dtoList.indexOf(_06_008_000Object), dtoList.size() - 1);
		tEMOList = tEMOList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());

		generateTotalMValue(tEMO, tEMOList);

		resultList.add(tEMO);

		resultList.add(dtoList.get(dtoList.size() - 1));

		BalanceSheetDTO tOI = new BalanceSheetDTO();
		tOI.setAcCode("");
		tOI.setAcName("Total Other Income");

		/* {496}+{505}+{516}+{522}+{550} */
		/* Total Other Income */
		List<BalanceSheetDTO> tOIList = new ArrayList<>();
		tOIList.add(resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Interest Income")).findFirst().get());
		tOIList.add(resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Commission Income")).findFirst().get());
		tOIList.add(resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Name Change Income")).findFirst().get());
		tOIList.add(resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Reinstatement Stamp Fees")).findFirst().get());
		tOIList.add(resultList.stream().filter(result -> result.getAcName().equalsIgnoreCase("Total Excess Money & Others")).findFirst().get());

		generateTotalMValue(tOI, tOIList);

		// resultList.add(tOI);

		BalanceSheetDTO blank = new BalanceSheetDTO();
		blank.setAcCode("");
		blank.setAcName("");
		resultList.add(blank);

		return resultList;
	}

	private List<BalanceSheetDTO> format7Series(List<BalanceSheetDTO> dtoList) {
		List<BalanceSheetDTO> resultList = new ArrayList<>();
		BalanceSheetDTO otherExpense = dtoList.get(0);
		resultList.add(otherExpense);

		BalanceSheetDTO adjustmentExpenses = new BalanceSheetDTO();
		adjustmentExpenses.setAcCode("");
		adjustmentExpenses.setAcName("Adjustment Expenses");
		resultList.add(adjustmentExpenses);

		// 07-003-000
		BalanceSheetDTO _07_003_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("07-003-000")).findFirst().get();
		resultList.addAll(dtoList.subList(1, dtoList.indexOf(_07_003_000Object)));

		BalanceSheetDTO tAdjustmentExpenses = new BalanceSheetDTO();
		tAdjustmentExpenses.setAcCode("");
		tAdjustmentExpenses.setAcName("Total Adjustment Expenses");

		// Total Co-Insurance Commission Income
		// NEED TO REFECTOR
		/* {430}+{492}+{495} */
		/* Total Adjustment Expenses */
		List<BalanceSheetDTO> tAdjustmentExpensesList = new ArrayList<>();
		tAdjustmentExpensesList = resultList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());

		generateTotalMValue(tAdjustmentExpenses, tAdjustmentExpensesList);
		resultList.add(tAdjustmentExpenses);

		BalanceSheetDTO csrActivities = new BalanceSheetDTO();
		csrActivities.setAcCode("");
		csrActivities.setAcName("CSR Activities");
		resultList.add(csrActivities);

		// 07-004-000
		BalanceSheetDTO _07_004_000Object = dtoList.stream().filter(dto -> dto.getAcCode().equalsIgnoreCase("07-004-000")).findFirst().get();
		resultList.addAll(dtoList.subList(dtoList.indexOf(_07_003_000Object), dtoList.indexOf(_07_004_000Object)));

		BalanceSheetDTO socialActivities = new BalanceSheetDTO();
		socialActivities.setAcCode("");
		socialActivities.setAcName("Social Activities");
		resultList.add(socialActivities);

		resultList.addAll(dtoList.subList(dtoList.indexOf(_07_004_000Object), dtoList.size() - 1));
		// Total Donations - Water Project
		BalanceSheetDTO totalDonations = resultList.stream().filter(dto -> dto.getAcName().equalsIgnoreCase("Total Donations - Water Project")).findFirst().get();

		List<BalanceSheetDTO> csrActivitiesList = new ArrayList<>();
		csrActivitiesList = dtoList.subList(dtoList.indexOf(_07_003_000Object), dtoList.size() - 2);
		csrActivitiesList = csrActivitiesList.stream().filter(result -> result.getAcName().contains("Total")).collect(Collectors.toList());

		// Total CSR Activities

		BalanceSheetDTO tcsrActivities = new BalanceSheetDTO();
		tcsrActivities.setAcCode("");
		tcsrActivities.setAcName("Total CSR Activities");

		generateTotalMValue(tcsrActivities, csrActivitiesList);
		resultList.add(resultList.indexOf(totalDonations), tcsrActivities);

		resultList.remove(totalDonations);

		BalanceSheetDTO blank = new BalanceSheetDTO();
		blank.setAcCode("");
		blank.setAcName("");
		resultList.add(blank);

		resultList.add(dtoList.get(dtoList.size() - 1));

		return resultList;
	}

	private boolean validateCode(String acCode, String data) {
		if (null != acCode && !acCode.isEmpty() && acCode.substring(0, 2).equalsIgnoreCase(data)) {
			return true;
		} else {
			return false;
		}
	}

	public List<BalanceSheetDTO> calculateGroupTotal(List<BalanceSheetDTO> resultList, List<BalanceSheetDTO> targetList, String reportType) {

		List<BalanceSheetDTO> groupList = new ArrayList<>();
		List<BalanceSheetDTO> detailList = new ArrayList<>();
		List<BalanceSheetDTO> headList = new ArrayList<>();
		/*
		 * if (reportType.equals("B")) { groupList =
		 * targetList.stream().filter(dto ->
		 * !dto.getAcCodeType().equals("DETAIL") && (dto.getAcType().equals("A")
		 * || dto.getAcType().equals("L"))) .collect(Collectors.toList());
		 * detailList = targetList.stream().filter(dto ->
		 * dto.getAcCodeType().equals("DETAIL") && (dto.getAcType().equals("A")
		 * || dto.getAcType().equals("L"))) .collect(Collectors.toList()); }
		 * else { groupList = targetList.stream().filter(dto ->
		 * !dto.getAcCodeType().equals("DETAIL") && (dto.getAcType().equals("I")
		 * || dto.getAcType().equals("E"))) .collect(Collectors.toList());
		 * detailList = targetList.stream().filter(dto ->
		 * dto.getAcCodeType().equals("DETAIL") && (dto.getAcType().equals("I")
		 * || dto.getAcType().equals("E"))) .collect(Collectors.toList()); }
		 */
		headList = targetList.stream().filter(dto -> dto.getAcCodeType().equals("HEAD")).collect(Collectors.toList());
		groupList = targetList.stream().filter(dto -> dto.getAcCodeType().equals("GROUP")).collect(Collectors.toList());
		detailList = targetList.stream().filter(dto -> dto.getAcCodeType().equals("DETAIL")).collect(Collectors.toList());

		if (!groupList.isEmpty()) {
			for (BalanceSheetDTO group : groupList) {

				/*
				 * for (BalanceSheetDTO target : detailList) { if
				 * (target.getParentId().equals(group.getId())) {
				 * group.setM1((target.getM1()));
				 * group.getM2().add(target.getM2());
				 * group.getM3().add(target.getM3());
				 * group.getM4().add(target.getM4());
				 * group.getM5().add(target.getM5());
				 * group.getM6().add(target.getM6());
				 * group.getM7().add(target.getM7());
				 * group.getM8().add(target.getM8());
				 * group.getM9().add(target.getM9());
				 * group.getM10().add(target.getM10());
				 * group.getM11().add(target.getM11());
				 * group.getM12().add(target.getM12()); } }
				 */
				BigDecimal m1 = detailList.stream().filter(dto -> dto.getParentId().equals(group.getId())).map(dto -> dto.getM1()).reduce(BigDecimal.ZERO, BigDecimal::add);
				group.setM1(m1);
				BigDecimal m2 = detailList.stream().filter(dto -> dto.getParentId().equals(group.getId())).map(dto -> dto.getM2()).reduce(BigDecimal.ZERO, BigDecimal::add);
				group.setM2(m2);
				BigDecimal m3 = detailList.stream().filter(dto -> dto.getParentId().equals(group.getId())).map(dto -> dto.getM3()).reduce(BigDecimal.ZERO, BigDecimal::add);
				group.setM3(m3);
				BigDecimal m4 = detailList.stream().filter(dto -> dto.getParentId().equals(group.getId())).map(dto -> dto.getM4()).reduce(BigDecimal.ZERO, BigDecimal::add);
				group.setM4(m4);
				BigDecimal m5 = detailList.stream().filter(dto -> dto.getParentId().equals(group.getId())).map(dto -> dto.getM5()).reduce(BigDecimal.ZERO, BigDecimal::add);
				group.setM5(m5);
				BigDecimal m6 = detailList.stream().filter(dto -> dto.getParentId().equals(group.getId())).map(dto -> dto.getM6()).reduce(BigDecimal.ZERO, BigDecimal::add);
				group.setM6(m6);
				BigDecimal m7 = detailList.stream().filter(dto -> dto.getParentId().equals(group.getId())).map(dto -> dto.getM7()).reduce(BigDecimal.ZERO, BigDecimal::add);
				group.setM7(m7);
				BigDecimal m8 = detailList.stream().filter(dto -> dto.getParentId().equals(group.getId())).map(dto -> dto.getM8()).reduce(BigDecimal.ZERO, BigDecimal::add);
				group.setM8(m8);
				BigDecimal m9 = detailList.stream().filter(dto -> dto.getParentId().equals(group.getId())).map(dto -> dto.getM9()).reduce(BigDecimal.ZERO, BigDecimal::add);
				group.setM9(m9);
				BigDecimal m10 = detailList.stream().filter(dto -> dto.getParentId().equals(group.getId())).map(dto -> dto.getM10()).reduce(BigDecimal.ZERO, BigDecimal::add);
				group.setM10(m10);
				BigDecimal m11 = detailList.stream().filter(dto -> dto.getParentId().equals(group.getId())).map(dto -> dto.getM11()).reduce(BigDecimal.ZERO, BigDecimal::add);
				group.setM11(m11);
				BigDecimal m12 = detailList.stream().filter(dto -> dto.getParentId().equals(group.getId())).map(dto -> dto.getM12()).reduce(BigDecimal.ZERO, BigDecimal::add);
				group.setM12(m12);

			}
		}

		for (BalanceSheetDTO head : headList) {
			BigDecimal m1 = groupList.stream().filter(dto -> dto.getParentId().equals(head.getId())).map(dto -> dto.getM1()).reduce(BigDecimal.ZERO, BigDecimal::add);
			head.setM1(m1);
			BigDecimal m2 = groupList.stream().filter(dto -> dto.getParentId().equals(head.getId())).map(dto -> dto.getM2()).reduce(BigDecimal.ZERO, BigDecimal::add);
			head.setM2(m2);
			BigDecimal m3 = groupList.stream().filter(dto -> dto.getParentId().equals(head.getId())).map(dto -> dto.getM3()).reduce(BigDecimal.ZERO, BigDecimal::add);
			head.setM3(m3);
			BigDecimal m4 = groupList.stream().filter(dto -> dto.getParentId().equals(head.getId())).map(dto -> dto.getM4()).reduce(BigDecimal.ZERO, BigDecimal::add);
			head.setM4(m4);
			BigDecimal m5 = groupList.stream().filter(dto -> dto.getParentId().equals(head.getId())).map(dto -> dto.getM5()).reduce(BigDecimal.ZERO, BigDecimal::add);
			head.setM5(m5);
			BigDecimal m6 = groupList.stream().filter(dto -> dto.getParentId().equals(head.getId())).map(dto -> dto.getM6()).reduce(BigDecimal.ZERO, BigDecimal::add);
			head.setM6(m6);
			BigDecimal m7 = groupList.stream().filter(dto -> dto.getParentId().equals(head.getId())).map(dto -> dto.getM7()).reduce(BigDecimal.ZERO, BigDecimal::add);
			head.setM7(m7);
			BigDecimal m8 = groupList.stream().filter(dto -> dto.getParentId().equals(head.getId())).map(dto -> dto.getM8()).reduce(BigDecimal.ZERO, BigDecimal::add);
			head.setM8(m8);
			BigDecimal m9 = groupList.stream().filter(dto -> dto.getParentId().equals(head.getId())).map(dto -> dto.getM9()).reduce(BigDecimal.ZERO, BigDecimal::add);
			head.setM9(m9);
			BigDecimal m10 = groupList.stream().filter(dto -> dto.getParentId().equals(head.getId())).map(dto -> dto.getM10()).reduce(BigDecimal.ZERO, BigDecimal::add);
			head.setM10(m10);
			BigDecimal m11 = groupList.stream().filter(dto -> dto.getParentId().equals(head.getId())).map(dto -> dto.getM11()).reduce(BigDecimal.ZERO, BigDecimal::add);
			head.setM11(m11);
			BigDecimal m12 = groupList.stream().filter(dto -> dto.getParentId().equals(head.getId())).map(dto -> dto.getM12()).reduce(BigDecimal.ZERO, BigDecimal::add);
			head.setM12(m12);
		}

		resultList.clear();
		resultList.addAll(headList);
		resultList.addAll(groupList);
		resultList.addAll(detailList);
		return resultList;
	}

	private List<BalanceSheetDTO> claculateOrder(List<BalanceSheetDTO> dtoList, String reportType) {
		List<BalanceSheetDTO> aHeaderList = new ArrayList<>();
		List<BalanceSheetDTO> lHeaderList = new ArrayList<>();
		List<BalanceSheetDTO> aGroupList = new ArrayList<>();
		List<BalanceSheetDTO> lGroupList = new ArrayList<>();
		List<BalanceSheetDTO> detailList = new ArrayList<>();

		BalanceSheetDTO lastTotal = new BalanceSheetDTO();
		BalanceSheetDTO lastTotalEqu = new BalanceSheetDTO();

		List<BalanceSheetDTO> orderList = new ArrayList<>();
		BigDecimal totalEquitym1 = BigDecimal.ZERO;
		BigDecimal totalEquitym2 = BigDecimal.ZERO;
		BigDecimal totalEquitym3 = BigDecimal.ZERO;
		BigDecimal totalEquitym4 = BigDecimal.ZERO;
		BigDecimal totalEquitym5 = BigDecimal.ZERO;
		BigDecimal totalEquitym6 = BigDecimal.ZERO;
		BigDecimal totalEquitym7 = BigDecimal.ZERO;
		BigDecimal totalEquitym8 = BigDecimal.ZERO;
		BigDecimal totalEquitym9 = BigDecimal.ZERO;
		BigDecimal totalEquitym10 = BigDecimal.ZERO;
		BigDecimal totalEquitym11 = BigDecimal.ZERO;
		BigDecimal totalEquitym12 = BigDecimal.ZERO;

		if (reportType.equals("B")) {
			aHeaderList = dtoList.stream().filter(dto -> dto.getAcCodeType().equals("HEAD") && dto.getAcType().equals("A")).collect(Collectors.toList());
			lHeaderList = dtoList.stream().filter(dto -> dto.getAcCodeType().equals("HEAD") && dto.getAcType().equals("L")).collect(Collectors.toList());
			aGroupList = dtoList.stream().filter(dto -> dto.getAcCodeType().equals("GROUP") && dto.getAcType().equals("A")).collect(Collectors.toList());
			lGroupList = dtoList.stream().filter(dto -> dto.getAcCodeType().equals("GROUP") && dto.getAcType().equals("L")).collect(Collectors.toList());
			detailList = dtoList.stream().filter(dto -> dto.getAcCodeType().equals("DETAIL") && (dto.getAcType().equals("A") || dto.getAcType().equals("L")))
					.collect(Collectors.toList());

			List<BalanceSheetDTO> iDetailList = dtoList.stream().filter(dto -> dto.getAcCodeType().equals("DETAIL") && dto.getAcType().equals("I")).collect(Collectors.toList());
			List<BalanceSheetDTO> eDetailList = dtoList.stream().filter(dto -> dto.getAcCodeType().equals("DETAIL") && dto.getAcType().equals("E")).collect(Collectors.toList());

			List<BalanceSheetDTO> iHeaderList = new ArrayList();
			List<BalanceSheetDTO> eHeaderList = new ArrayList();

			iHeaderList = dtoList.stream().filter(dto -> dto.getAcCodeType().equals("HEAD") && dto.getAcType().equals("I")).collect(Collectors.toList());
			eHeaderList = dtoList.stream().filter(dto -> dto.getAcCodeType().equals("HEAD") && dto.getAcType().equals("E")).collect(Collectors.toList());
			detailList.stream().filter(dto -> dto.getAcCode().equals("03-007-001")).findAny().get();
			BigDecimal totalI = BigDecimal.ZERO;
			BigDecimal totalE = BigDecimal.ZERO;
			BigDecimal m1Different = BigDecimal.ZERO;
			BigDecimal m2Different = BigDecimal.ZERO;
			BigDecimal m3Different = BigDecimal.ZERO;
			BigDecimal m4Different = BigDecimal.ZERO;
			BigDecimal m5Different = BigDecimal.ZERO;
			BigDecimal m6Different = BigDecimal.ZERO;
			BigDecimal m7Different = BigDecimal.ZERO;
			BigDecimal m8Different = BigDecimal.ZERO;
			BigDecimal m9Different = BigDecimal.ZERO;
			BigDecimal m10Different = BigDecimal.ZERO;
			BigDecimal m11Different = BigDecimal.ZERO;
			BigDecimal m12Different = BigDecimal.ZERO;

			totalI = iHeaderList.stream().map(dto -> dto.getM1()).reduce(BigDecimal.ZERO, BigDecimal::add);
			totalE = eHeaderList.stream().map(dto -> dto.getM1()).reduce(BigDecimal.ZERO, BigDecimal::add);
			if (totalI.doubleValue() == 0.0 && totalE.doubleValue() == 0.0) {
				BigDecimal iTotal = iDetailList.stream().map(dto -> dto.getM1()).reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal eTotal = eDetailList.stream().map(dto -> dto.getM1()).reduce(BigDecimal.ZERO, BigDecimal::add);
				m1Different = (iTotal.subtract(eTotal));
			} else {
				m1Different = (totalI.subtract(totalE));
			}

			totalI = iHeaderList.stream().map(dto -> dto.getM2()).reduce(BigDecimal.ZERO, BigDecimal::add);
			totalE = eHeaderList.stream().map(dto -> dto.getM2()).reduce(BigDecimal.ZERO, BigDecimal::add);
			if (totalI.doubleValue() == 0.0 && totalE.doubleValue() == 0.0) {
				BigDecimal iTotal = iDetailList.stream().map(dto -> dto.getM2()).reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal eTotal = eDetailList.stream().map(dto -> dto.getM2()).reduce(BigDecimal.ZERO, BigDecimal::add);
				m2Different = (iTotal.subtract(eTotal));
			} else {
				m2Different = (totalI.subtract(totalE));
			}

			totalI = iHeaderList.stream().map(dto -> dto.getM3()).reduce(BigDecimal.ZERO, BigDecimal::add);
			totalE = eHeaderList.stream().map(dto -> dto.getM3()).reduce(BigDecimal.ZERO, BigDecimal::add);
			if (totalI.doubleValue() == 0.0 && totalE.doubleValue() == 0.0) {
				BigDecimal iTotal = iDetailList.stream().map(dto -> dto.getM3()).reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal eTotal = eDetailList.stream().map(dto -> dto.getM3()).reduce(BigDecimal.ZERO, BigDecimal::add);
				m3Different = (iTotal.subtract(eTotal));
			} else {
				m3Different = (totalI.subtract(totalE));
			}

			totalI = iHeaderList.stream().map(dto -> dto.getM4()).reduce(BigDecimal.ZERO, BigDecimal::add);
			totalE = eHeaderList.stream().map(dto -> dto.getM4()).reduce(BigDecimal.ZERO, BigDecimal::add);
			if (totalI.doubleValue() == 0.0 && totalE.doubleValue() == 0.0) {
				BigDecimal iTotal = iDetailList.stream().map(dto -> dto.getM4()).reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal eTotal = eDetailList.stream().map(dto -> dto.getM4()).reduce(BigDecimal.ZERO, BigDecimal::add);
				m4Different = (iTotal.subtract(eTotal));
			} else {
				m4Different = (totalI.subtract(totalE));
			}

			totalI = iHeaderList.stream().map(dto -> dto.getM5()).reduce(BigDecimal.ZERO, BigDecimal::add);
			totalE = eHeaderList.stream().map(dto -> dto.getM5()).reduce(BigDecimal.ZERO, BigDecimal::add);
			if (totalI.doubleValue() == 0.0 && totalE.doubleValue() == 0.0) {
				BigDecimal iTotal = iDetailList.stream().map(dto -> dto.getM5()).reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal eTotal = eDetailList.stream().map(dto -> dto.getM5()).reduce(BigDecimal.ZERO, BigDecimal::add);
				m5Different = (iTotal.subtract(eTotal));
			} else {
				m5Different = (totalI.subtract(totalE));
			}

			totalI = iHeaderList.stream().map(dto -> dto.getM6()).reduce(BigDecimal.ZERO, BigDecimal::add);
			totalE = eHeaderList.stream().map(dto -> dto.getM6()).reduce(BigDecimal.ZERO, BigDecimal::add);
			if (totalI.doubleValue() == 0.0 && totalE.doubleValue() == 0.0) {
				BigDecimal iTotal = iDetailList.stream().map(dto -> dto.getM6()).reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal eTotal = eDetailList.stream().map(dto -> dto.getM6()).reduce(BigDecimal.ZERO, BigDecimal::add);
				m6Different = (iTotal.subtract(eTotal));
			} else {
				m6Different = (totalI.subtract(totalE));
			}

			totalI = iHeaderList.stream().map(dto -> dto.getM7()).reduce(BigDecimal.ZERO, BigDecimal::add);
			totalE = eHeaderList.stream().map(dto -> dto.getM7()).reduce(BigDecimal.ZERO, BigDecimal::add);
			if (totalI.doubleValue() == 0.0 && totalE.doubleValue() == 0.0) {
				BigDecimal iTotal = iDetailList.stream().map(dto -> dto.getM7()).reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal eTotal = eDetailList.stream().map(dto -> dto.getM7()).reduce(BigDecimal.ZERO, BigDecimal::add);
				m7Different = (iTotal.subtract(eTotal));
			} else {
				m7Different = (totalI.subtract(totalE));
			}

			totalI = iHeaderList.stream().map(dto -> dto.getM8()).reduce(BigDecimal.ZERO, BigDecimal::add);
			totalE = eHeaderList.stream().map(dto -> dto.getM8()).reduce(BigDecimal.ZERO, BigDecimal::add);
			if (totalI.doubleValue() == 0.0 && totalE.doubleValue() == 0.0) {
				BigDecimal iTotal = iDetailList.stream().map(dto -> dto.getM8()).reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal eTotal = eDetailList.stream().map(dto -> dto.getM8()).reduce(BigDecimal.ZERO, BigDecimal::add);
				m8Different = (iTotal.subtract(eTotal));
			} else {
				m8Different = (totalI.subtract(totalE));
			}

			totalI = iHeaderList.stream().map(dto -> dto.getM9()).reduce(BigDecimal.ZERO, BigDecimal::add);
			totalE = eHeaderList.stream().map(dto -> dto.getM9()).reduce(BigDecimal.ZERO, BigDecimal::add);
			if (totalI.doubleValue() == 0.0 && totalE.doubleValue() == 0.0) {
				BigDecimal iTotal = iDetailList.stream().map(dto -> dto.getM9()).reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal eTotal = eDetailList.stream().map(dto -> dto.getM9()).reduce(BigDecimal.ZERO, BigDecimal::add);
				m9Different = (iTotal.subtract(eTotal));
			} else {
				m9Different = (totalI.subtract(totalE));
			}

			totalI = iHeaderList.stream().map(dto -> dto.getM10()).reduce(BigDecimal.ZERO, BigDecimal::add);
			totalE = eHeaderList.stream().map(dto -> dto.getM10()).reduce(BigDecimal.ZERO, BigDecimal::add);
			if (totalI.doubleValue() == 0.0 && totalE.doubleValue() == 0.0) {
				BigDecimal iTotal = iDetailList.stream().map(dto -> dto.getM10()).reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal eTotal = eDetailList.stream().map(dto -> dto.getM10()).reduce(BigDecimal.ZERO, BigDecimal::add);
				m10Different = (iTotal.subtract(eTotal));
			} else {
				m10Different = (totalI.subtract(totalE));
			}

			totalI = iHeaderList.stream().map(dto -> dto.getM11()).reduce(BigDecimal.ZERO, BigDecimal::add);
			totalE = eHeaderList.stream().map(dto -> dto.getM11()).reduce(BigDecimal.ZERO, BigDecimal::add);
			if (totalI.doubleValue() == 0.0 && totalE.doubleValue() == 0.0) {
				BigDecimal iTotal = iDetailList.stream().map(dto -> dto.getM11()).reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal eTotal = eDetailList.stream().map(dto -> dto.getM11()).reduce(BigDecimal.ZERO, BigDecimal::add);
				m11Different = (iTotal.subtract(eTotal));
			} else {
				m11Different = (totalI.subtract(totalE));
			}

			totalI = iHeaderList.stream().map(dto -> dto.getM12()).reduce(BigDecimal.ZERO, BigDecimal::add);
			totalE = eHeaderList.stream().map(dto -> dto.getM12()).reduce(BigDecimal.ZERO, BigDecimal::add);
			if (totalI.doubleValue() == 0.0 && totalE.doubleValue() == 0.0) {
				BigDecimal iTotal = iDetailList.stream().map(dto -> dto.getM12()).reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal eTotal = eDetailList.stream().map(dto -> dto.getM12()).reduce(BigDecimal.ZERO, BigDecimal::add);
				m12Different = (iTotal.subtract(eTotal));
			} else {
				m12Different = (totalI.subtract(totalE));
			}

			for (BalanceSheetDTO dto : detailList) {
				if (dto.getAcCode().equals("03-007-001")) {
					dto.setM1(dto.getM1().add(m1Different));
					dto.setM2(dto.getM2().add(m2Different));
					dto.setM3(dto.getM3().add(m3Different));
					dto.setM4(dto.getM4().add(m4Different));
					dto.setM5(dto.getM5().add(m5Different));
					dto.setM6(dto.getM6().add(m6Different));
					dto.setM7(dto.getM7().add(m7Different));
					dto.setM8(dto.getM8().add(m8Different));
					dto.setM9(dto.getM9().add(m9Different));
					dto.setM10(dto.getM10().add(m10Different));
					dto.setM11(dto.getM11().add(m11Different));
					dto.setM12(dto.getM12().add(m12Different));
				}
			}

			for (BalanceSheetDTO dto : lGroupList) {
				if (dto.getAcCode().equals("03-007-000")) {
					dto.setM1(dto.getM1().add(m1Different));
					dto.setM2(dto.getM2().add(m2Different));
					dto.setM3(dto.getM3().add(m3Different));
					dto.setM4(dto.getM4().add(m4Different));
					dto.setM5(dto.getM5().add(m5Different));
					dto.setM6(dto.getM6().add(m6Different));
					dto.setM7(dto.getM7().add(m7Different));
					dto.setM8(dto.getM8().add(m8Different));
					dto.setM9(dto.getM9().add(m9Different));
					dto.setM10(dto.getM10().add(m10Different));
					dto.setM11(dto.getM11().add(m11Different));
					dto.setM12(dto.getM12().add(m12Different));
				}
			}

			/////////
			// lHeaderList.stream().filter(dto ->
			///////// dto.getAcCode().equals("03-000-000")).findAny().get();

			for (BalanceSheetDTO dto : lHeaderList) {
				if (dto.getAcCode().equals("03-000-000")) {
					dto.setM1(dto.getM1().add(m1Different));
					dto.setM2(dto.getM2().add(m2Different));
					dto.setM3(dto.getM3().add(m3Different));
					dto.setM4(dto.getM4().add(m4Different));
					dto.setM5(dto.getM5().add(m5Different));
					dto.setM6(dto.getM6().add(m6Different));
					dto.setM7(dto.getM7().add(m7Different));
					dto.setM8(dto.getM8().add(m8Different));
					dto.setM9(dto.getM9().add(m9Different));
					dto.setM10(dto.getM10().add(m10Different));
					dto.setM11(dto.getM11().add(m11Different));
					dto.setM12(dto.getM12().add(m12Different));
				}
			}

			////////

			BigDecimal lm1 = BigDecimal.ZERO;
			BigDecimal lm2 = BigDecimal.ZERO;
			BigDecimal lm3 = BigDecimal.ZERO;
			BigDecimal lm4 = BigDecimal.ZERO;
			BigDecimal lm5 = BigDecimal.ZERO;
			BigDecimal lm6 = BigDecimal.ZERO;
			BigDecimal lm7 = BigDecimal.ZERO;
			BigDecimal lm8 = BigDecimal.ZERO;
			BigDecimal lm9 = BigDecimal.ZERO;
			BigDecimal lm10 = BigDecimal.ZERO;
			BigDecimal lm11 = BigDecimal.ZERO;
			BigDecimal lm12 = BigDecimal.ZERO;

			lm1 = lHeaderList.stream().map(dto -> dto.getM1()).reduce(BigDecimal.ZERO, BigDecimal::add);
			lm2 = lHeaderList.stream().map(dto -> dto.getM2()).reduce(BigDecimal.ZERO, BigDecimal::add);
			lm3 = lHeaderList.stream().map(dto -> dto.getM3()).reduce(BigDecimal.ZERO, BigDecimal::add);
			lm4 = lHeaderList.stream().map(dto -> dto.getM4()).reduce(BigDecimal.ZERO, BigDecimal::add);
			lm5 = lHeaderList.stream().map(dto -> dto.getM5()).reduce(BigDecimal.ZERO, BigDecimal::add);
			lm6 = lHeaderList.stream().map(dto -> dto.getM6()).reduce(BigDecimal.ZERO, BigDecimal::add);
			lm7 = lHeaderList.stream().map(dto -> dto.getM7()).reduce(BigDecimal.ZERO, BigDecimal::add);
			lm8 = lHeaderList.stream().map(dto -> dto.getM8()).reduce(BigDecimal.ZERO, BigDecimal::add);
			lm9 = lHeaderList.stream().map(dto -> dto.getM9()).reduce(BigDecimal.ZERO, BigDecimal::add);
			lm10 = lHeaderList.stream().map(dto -> dto.getM10()).reduce(BigDecimal.ZERO, BigDecimal::add);
			lm11 = lHeaderList.stream().map(dto -> dto.getM11()).reduce(BigDecimal.ZERO, BigDecimal::add);
			lm12 = lHeaderList.stream().map(dto -> dto.getM12()).reduce(BigDecimal.ZERO, BigDecimal::add);

			/*
			 * totalEquitym1 = totalEquitym1.add(lm1.add(m1Different));
			 * totalEquitym2 = totalEquitym2.add(lm2.add(m2Different));
			 * totalEquitym3 = totalEquitym3.add(lm3.add(m3Different));
			 * totalEquitym4 = totalEquitym4.add(lm4.add(m4Different));
			 * totalEquitym5 = totalEquitym5.add(lm5.add(m5Different));
			 * totalEquitym6 = totalEquitym6.add(lm6.add(m6Different));
			 * totalEquitym7 = totalEquitym7.add(lm7.add(m7Different));
			 * totalEquitym8 = totalEquitym8.add(lm8.add(m8Different));
			 * totalEquitym9 = totalEquitym9.add(lm9.add(m9Different));
			 * totalEquitym10 = totalEquitym10.add(lm10.add(m10Different));
			 * totalEquitym11 = totalEquitym11.add(lm11.add(m11Different));
			 * totalEquitym12 = totalEquitym12.add(lm12.add(m12Different));
			 */

			lastTotalEqu.setAcName("Total Equity and Liabilities ");
			lastTotalEqu.setAcCode("");
			lastTotalEqu.setAcCodeType("DETAIL");

			/*
			 * lastTotalEqu.setM1(totalEquitym1);
			 * lastTotalEqu.setM2(totalEquitym2);
			 * lastTotalEqu.setM3(totalEquitym3);
			 * lastTotalEqu.setM4(totalEquitym4);
			 * lastTotalEqu.setM5(totalEquitym5);
			 * lastTotalEqu.setM6(totalEquitym6);
			 * lastTotalEqu.setM7(totalEquitym7);
			 * lastTotalEqu.setM8(totalEquitym8);
			 * lastTotalEqu.setM9(totalEquitym9);
			 * lastTotalEqu.setM10(totalEquitym10);
			 * lastTotalEqu.setM11(totalEquitym11);
			 * lastTotalEqu.setM12(totalEquitym12);
			 */

			lastTotalEqu.setM1(lm1);
			lastTotalEqu.setM2(lm2);
			lastTotalEqu.setM3(lm3);
			lastTotalEqu.setM4(lm4);
			lastTotalEqu.setM5(lm5);
			lastTotalEqu.setM6(lm6);
			lastTotalEqu.setM7(lm7);
			lastTotalEqu.setM8(lm8);
			lastTotalEqu.setM9(lm9);
			lastTotalEqu.setM10(lm10);
			lastTotalEqu.setM11(lm11);
			lastTotalEqu.setM12(lm12);

		} else {
			aHeaderList = dtoList.stream().filter(dto -> dto.getAcCodeType().equals("HEAD") && dto.getAcType().equals("I")).collect(Collectors.toList());
			lHeaderList = dtoList.stream().filter(dto -> dto.getAcCodeType().equals("HEAD") && dto.getAcType().equals("E")).collect(Collectors.toList());
			aGroupList = dtoList.stream().filter(dto -> dto.getAcCodeType().equals("GROUP") && dto.getAcType().equals("I")).collect(Collectors.toList());
			lGroupList = dtoList.stream().filter(dto -> dto.getAcCodeType().equals("GROUP") && dto.getAcType().equals("E")).collect(Collectors.toList());
			detailList = dtoList.stream().filter(dto -> dto.getAcCodeType().equals("DETAIL") && (dto.getAcType().equals("I") || dto.getAcType().equals("E")))
					.collect(Collectors.toList());

			lastTotal.setAcName("Net Profit and Loss");
			lastTotal.setAcCode("");
			lastTotal.setAcCodeType("DETAIL");
			BigDecimal m1 = aHeaderList.stream().map(dto -> dto.getM1()).reduce(BigDecimal.ZERO, BigDecimal::add);
			m1 = m1.subtract(lHeaderList.stream().map(dto -> dto.getM1()).reduce(BigDecimal.ZERO, BigDecimal::add));
			lastTotal.setM1(m1);
			BigDecimal m2 = aHeaderList.stream().map(dto -> dto.getM2()).reduce(BigDecimal.ZERO, BigDecimal::add);
			m2 = m2.subtract(lHeaderList.stream().map(dto -> dto.getM2()).reduce(BigDecimal.ZERO, BigDecimal::add));
			lastTotal.setM2(m2);
			BigDecimal m3 = aHeaderList.stream().map(dto -> dto.getM3()).reduce(BigDecimal.ZERO, BigDecimal::add);
			m3 = m3.subtract(lHeaderList.stream().map(dto -> dto.getM3()).reduce(BigDecimal.ZERO, BigDecimal::add));
			lastTotal.setM3(m3);
			BigDecimal m4 = aHeaderList.stream().map(dto -> dto.getM4()).reduce(BigDecimal.ZERO, BigDecimal::add);
			m4 = m4.subtract(lHeaderList.stream().map(dto -> dto.getM4()).reduce(BigDecimal.ZERO, BigDecimal::add));
			lastTotal.setM4(m4);
			BigDecimal m5 = aHeaderList.stream().map(dto -> dto.getM5()).reduce(BigDecimal.ZERO, BigDecimal::add);
			m5 = m5.subtract(lHeaderList.stream().map(dto -> dto.getM5()).reduce(BigDecimal.ZERO, BigDecimal::add));
			lastTotal.setM5(m5);
			BigDecimal m6 = aHeaderList.stream().map(dto -> dto.getM6()).reduce(BigDecimal.ZERO, BigDecimal::add);
			m6 = m6.subtract(lHeaderList.stream().map(dto -> dto.getM6()).reduce(BigDecimal.ZERO, BigDecimal::add));
			lastTotal.setM6(m6);
			BigDecimal m7 = aHeaderList.stream().map(dto -> dto.getM7()).reduce(BigDecimal.ZERO, BigDecimal::add);
			m7 = m7.subtract(lHeaderList.stream().map(dto -> dto.getM7()).reduce(BigDecimal.ZERO, BigDecimal::add));
			lastTotal.setM7(m7);
			BigDecimal m8 = aHeaderList.stream().map(dto -> dto.getM8()).reduce(BigDecimal.ZERO, BigDecimal::add);
			m8 = m8.subtract(lHeaderList.stream().map(dto -> dto.getM8()).reduce(BigDecimal.ZERO, BigDecimal::add));
			lastTotal.setM8(m8);
			BigDecimal m9 = aHeaderList.stream().map(dto -> dto.getM9()).reduce(BigDecimal.ZERO, BigDecimal::add);
			m9 = m9.subtract(lHeaderList.stream().map(dto -> dto.getM9()).reduce(BigDecimal.ZERO, BigDecimal::add));
			lastTotal.setM9(m9);
			BigDecimal m10 = aHeaderList.stream().map(dto -> dto.getM10()).reduce(BigDecimal.ZERO, BigDecimal::add);
			m10 = m10.subtract(lHeaderList.stream().map(dto -> dto.getM10()).reduce(BigDecimal.ZERO, BigDecimal::add));
			lastTotal.setM10(m10);
			BigDecimal m11 = aHeaderList.stream().map(dto -> dto.getM11()).reduce(BigDecimal.ZERO, BigDecimal::add);
			m11 = m11.subtract(lHeaderList.stream().map(dto -> dto.getM11()).reduce(BigDecimal.ZERO, BigDecimal::add));
			lastTotal.setM11(m11);
			BigDecimal m12 = aHeaderList.stream().map(dto -> dto.getM12()).reduce(BigDecimal.ZERO, BigDecimal::add);
			m12 = m12.subtract(lHeaderList.stream().map(dto -> dto.getM12()).reduce(BigDecimal.ZERO, BigDecimal::add));
			lastTotal.setM12(m12);
		}

		for (BalanceSheetDTO head : aHeaderList) {
			BalanceSheetDTO headDTO = new BalanceSheetDTO();
			headDTO.setAcName(head.getAcName());
			headDTO.setAcCode(head.getAcCode());
			headDTO.setAcCodeType("DETAIL");
			orderList.add(headDTO);

			for (BalanceSheetDTO group : aGroupList) {
				if (group.getParentId().equals(head.getId())) {
					BalanceSheetDTO groupDTO = new BalanceSheetDTO();
					groupDTO.setAcName(group.getAcName());
					groupDTO.setAcCode(group.getAcCode());
					groupDTO.setAcCodeType("DETAIL");
					orderList.add(groupDTO);

					orderList.addAll(detailList.stream().filter(dto -> dto.getParentId().equals(group.getId())).collect(Collectors.toList()));
					orderList.add(group);
				}

			}
			orderList.add(head);

		}

		for (BalanceSheetDTO head : lHeaderList) {
			BalanceSheetDTO headDTO = new BalanceSheetDTO();
			headDTO.setAcName(head.getAcName());
			headDTO.setAcCode(head.getAcCode());
			headDTO.setAcCodeType("DETAIL");
			orderList.add(headDTO);

			for (BalanceSheetDTO group : lGroupList) {
				if (group.getParentId().equals(head.getId())) {

					BalanceSheetDTO groupDTO = new BalanceSheetDTO();
					groupDTO.setAcName(group.getAcName());
					groupDTO.setAcCode(group.getAcCode());
					groupDTO.setAcCodeType("DETAIL");
					orderList.add(groupDTO);

					orderList.addAll(detailList.stream().filter(dto -> dto.getParentId().equals(group.getId())).collect(Collectors.toList()));
					orderList.add(group);
				}

			}
			orderList.add(head);
		}
		for (BalanceSheetDTO dto : orderList) {
			if (!dto.getAcCodeType().equals("DETAIL")) {
				dto.setAcCode("");
				dto.setAcName("Total ".concat(dto.getAcName()));
			}
		}

		if (!reportType.equals("B")) {
			orderList.add(lastTotal);
		} else {
			orderList.add(lastTotalEqu);
		}
		return orderList;
	}

	public boolean generateReport(List<BalanceSheetDTO> dtoList) {
		try {

			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("balanceSheetReport.jrxml");

			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("ICON_RSOURCES"));
			Map<String, Object> parameters = new HashMap<String, Object>();

			String branch = "";
			String currency = "";
			String type = "";
			boolean isDateRange = false;
			int startYear = 3000;
			int endYear = 0;
			int y1 = 0, y2 = 0;
			String budgetYear;
			Date budgetEndDate;
			int budsmth;
			Date budgetStartDate = BusinessUtil.getBudgetStartDate();
			int month = DateUtils.getMonthFromDate(budgetStartDate) + 1;

			String currentYear = BusinessUtil.getCurrentBudget();

			Date reportDate = DateUtils.resetStartDate(criteria.getStartDate());

			if (criteria.isMonth()) {
				budgetYear = criteria.getBudgetYear();
			} else {
				if (DateUtils.isIntervalBugetYear(reportDate))
					budgetYear = BusinessUtil.getBudgetInfo(reportDate, 2);
				else
					budgetYear = setup_HistoryDAO.findbudgetyear(reportDate);
			}

			// TODO - two records in prev_ccoa has no budget
			if (budgetYear != null) {
				if (Integer.parseInt(budgetYear.split("/")[0]) < startYear) {
					y1 = Integer.parseInt(budgetYear.split("/")[0]);
				}
				if (Integer.parseInt(budgetYear.split("/")[0]) > endYear) {
					y2 = Integer.parseInt(budgetYear.split("/")[1]);
				}
			}

			if (!budgetYear.equals(currentYear)) {
				budsmth = BusinessUtil.getPrevBudSmth(budgetYear);
				budgetStartDate = BusinessUtil.getPrevBudgetStartDate(budgetYear);
				budgetEndDate = BusinessUtil.getPrevBudgetEndDate(budgetYear);
			} else {
				budsmth = BusinessUtil.getBudSmth();
				budgetStartDate = BusinessUtil.getBudgetStartDate();
				budgetEndDate = BusinessUtil.getBudgetEndDate();

			}

			parameters.put("logoPath", image);

			if (criteria.isMonth()) {
				parameters.put("startDate", budgetStartDate);
				parameters.put("endDate", budgetEndDate);
				isDateRange = false;
				type = "Monthly ";
			} else {
				parameters.put("endDate", criteria.getEndDate());
				parameters.put("startDate", criteria.getStartDate());
				isDateRange = true;
				type = "Date Range ";
			}

			if (criteria.getBranch() == null) {
				branch = "All Branches";
			} else {
				branch = criteria.getBranch().getName();
			}
			parameters.put("branches", branch);

			if (criteria.getCurrency() == null) {
				currency = "All Currencies";
			} else {
				currency = criteria.getCurrency().getCurrencyCode();
				if (criteria.isHomeConverted() && criteria.getCurrency() != null) {
					currency = currency + " By Home Amount";
				}
			}

			if (criteria.getReportType().equalsIgnoreCase("B")) {

				type = type.concat("Balance Sheet as at ");
			} else {
				type = type.concat("Profit And Loss as at ");
			}

			parameters.put("currency", currency);
			parameters.put("type", type);
			parameters.put("isDateRange", isDateRange);
			parameters.put("monthList", Arrays.asList(MonthNames.values()));
			parameters.put("budsmth", budsmth);
			parameters.put("y1", y1);
			parameters.put("y2", y2);
			parameters.put("month", month);

			parameters.put("dtoList", new JRBeanCollectionDataSource(dtoList));
			parameters.put("homeCurrency", criteria.isHomeCur());
			parameters.put("homeCurrencyConverted", criteria.isHomeConverted());

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

	public void getDownload() {
		dtoList.clear();
		String branchId = criteria.getBranch() == null ? null : criteria.getBranch().getId();
		String currencyId = criteria.getCurrency() == null ? null : criteria.getCurrency().getId();
		String budgetYear = criteria.getBudgetYear();

		if (isBeforeBudgetEnd) {
			if (criteria.isMonth()) {
				dtoList = tlfService.generateBalanceSheetByClone(branchId, currencyId, criteria.isHomeCur(), budgetYear);
			} else {
				List<BalanceSheetDTO> dtoDayList = tlfService.generateCloneBalanceSheetByDate(branchId, currencyId, criteria.getStartDate(), criteria.getEndDate(),
						criteria.isHomeCur());

				if (!dtoDayList.isEmpty()) {
					calculateGroupTotal(dtoList, dtoDayList, criteria.getReportType());
				}
			}
		} else {

			if (criteria.isMonth()) {
				dtoList = tlfService.generateBalanceSheet(branchId, currencyId, criteria.isHomeCur(), budgetYear);
			} else {
				List<BalanceSheetDTO> dtoDayList = tlfService.generateBalanceSheetByDate(branchId, currencyId, criteria.getStartDate(), criteria.getEndDate(),
						criteria.isHomeCur());

				if (!dtoDayList.isEmpty()) {
					calculateGroupTotal(dtoList, dtoDayList, criteria.getReportType());
				}
			}
		}

		List<BalanceSheetDTO> orderList = claculateOrder(dtoList, criteria.getReportType());
		List<BalanceSheetDTO> formatList = new ArrayList<>();
		if (!criteria.getReportType().equals("B")) {

			formatList = generateFormat(orderList);
		} else {
			formatList = generateBSFormat(orderList);
		}
		StreamedContent result = null;
		if (formatList.isEmpty()) {
			addErrorMessage(null, MessageId.NO_RESULT);
		} else {
			// result = exportExcel(orderList);
			exportExcel(formatList);
		}
		// return result;
	}

	private StreamedContent getDownloadValue(List<BalanceSheetDTO> dtoList) {
		try {
			List<JasperPrint> prints = new ArrayList<JasperPrint>();
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("balanceSheetReport.jrxml");
			String image = FacesContext.getCurrentInstance().getExternalContext().getRealPath(propertiesManager.getProperties("ICON_RSOURCES"));
			Map<String, Object> parameters = new HashMap<String, Object>();

			String branch = "";
			String currency = "";

			parameters.put("logoPath", image);
			parameters.put("startDate", new Date());
			parameters.put("endDate", new Date());

			if (criteria.getBranch() == null) {
				branch = "All Branches";
			} else {
				branch = criteria.getBranch().getName();
			}
			parameters.put("branches", branch);

			if (criteria.getCurrency() == null) {
				currency = "All Currencies";
			} else {
				currency = criteria.getCurrency().getCurrencyCode();
				if (criteria.isHomeConverted()) {
					currency = currency + " By Home Amount";
				}
			}
			parameters.put("currency", currency);

			parameters.put("dtoList", new JRBeanCollectionDataSource(dtoList));
			parameters.put("homeCurrency", criteria.isHomeCur());
			parameters.put("homeCurrencyConverted", criteria.isHomeConverted());
			parameters.put("monthList", Arrays.asList(MonthNames.values()));
			parameters.put("budsmth", BusinessUtil.getBudSmth());

			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(dtoList));
			prints.add(jasperPrint);

			FileUtils.forceMkdir(new File(dirPath));

			File destFile = new File(dirPath + fileName.concat(".xlsx"));

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
			File file = new File(dirPath + fileName.concat(".xlsx"));
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

	public void exportExcel(List<BalanceSheetDTO> dtoList) {
		ExternalContext ec = getFacesContext().getExternalContext();
		ec.responseReset();
		ec.setResponseContentType("application/vnd.ms-excel");
		// String dirPath = "/report-template/" + System.currentTimeMillis() +
		// "/";
		// String dirPath = "D:/CYPAccount/accounting/report-template/" ;
		String dirPath = "/report-template/";
		String fileName;
		int budsmth;
		List<MonthNames> monthList = Arrays.asList(MonthNames.values());
		String budgetYear;
		Date reportDate = DateUtils.resetStartDate(criteria.getStartDate());
		String currentYear = BusinessUtil.getCurrentBudget();

		if (criteria.isMonth()) {
			budgetYear = criteria.getBudgetYear();
		} else {
			if (DateUtils.isIntervalBugetYear(reportDate))
				budgetYear = BusinessUtil.getBudgetInfo(reportDate, 2);
			else
				budgetYear = setup_HistoryDAO.findbudgetyear(reportDate);
		}

		if (budgetYear.equals(currentYear)) {
			budsmth = BusinessUtil.getBudSmth();
		} else {
			budsmth = BusinessUtil.getPrevBudSmth(budgetYear);
		}

		if (criteria.isMonth()) {
			fileName = "Report.xlsx";
		} else {
			fileName = "ReportDateRange.xlsx";
		}
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		try {
			OutputStream op = ec.getResponseOutputStream();
			BalanceSheetReportExcel balanceSheetReportExcel = new BalanceSheetReportExcel(criteria);
			if (fileName == "Report.xlsx") {
				balanceSheetReportExcel.generate(op, dtoList, criteria, monthList, budsmth, budgetYear);
			} else {
				balanceSheetReportExcel.generateDateRange(op, dtoList, criteria, monthList, budsmth, budgetYear);
			}
			getFacesContext().responseComplete();

			/*
			 * StreamedContent download = new DefaultStreamedContent(); File
			 * file = new File(dirPath + fileName); InputStream input = new
			 * FileInputStream(file); ExternalContext externalContext =
			 * FacesContext.getCurrentInstance().getExternalContext(); download
			 * = new DefaultStreamedContent(input,
			 * externalContext.getMimeType(file.getName()), file.getName());
			 * 
			 * return download;
			 */

		} catch (IOException e) {
			throw new SystemException(ErrorCode.SYSTEM_ERROR, "Failed to export Balance_Sheet_Report.xlsx", e);
		}
	}

	public void dateSelect(SelectEvent event) {
		changeBudgetYear();
	}

	public void dateChange(AjaxBehaviorEvent event) {
		changeBudgetYear();
	}

	public void changeFormatType(AjaxBehaviorEvent event) {
		if (!criteria.isMonth()) {
			criteria.setStartDate(new Date());
			criteria.setEndDate(new Date());
			isBeforeDisabled = true;
			isBeforeBudgetEnd = false;
			// changeBudgetYear();

		} else {

			String currentBudget = BusinessUtil.getCurrentBudget();
			criteria.setBudgetYear(currentBudget);
			isBeforeDisabled = true;
			isBeforeBudgetEnd = false;

		}

	}

	public void changeBudgetYear() {
		String currentBudget;
		currentBudget = BusinessUtil.getCurrentBudget();
		Date reportDate = DateUtils.resetStartDate(criteria.getStartDate());
		if (criteria.isMonth()) {
			// isBeforeDisabled = true;
			budgetYear = criteria.getBudgetYear();
		} else {
			if (DateUtils.isIntervalBugetYear(reportDate))
				budgetYear = BusinessUtil.getBudgetInfo(reportDate, 2);
			else
				budgetYear = setup_HistoryDAO.findbudgetyear(reportDate);
		}
		if (!budgetYear.equals(currentBudget)) {
			isBeforeDisabled = false;
			isBeforeBudgetEnd = true;
		} else {
			isBeforeDisabled = true;
			isBeforeBudgetEnd = false;
		}
		// monthSet = bindMonth();
		// yearList = getActiveYears();

	}

	public String getStream() {
		String fullFilePath = dirPath + fileName.concat(".pdf");
		return fullFilePath;
	}

	public List<BalanceSheetDTO> getDtoList() {
		return dtoList;
	}

	public BalanceSheetCriteria getCriteria() {
		return criteria;
	}

	public List<Currency> getCurrencyList() {
		return currencyService.findAllCurrency();
	}

	public CurrencyType[] getCurrencyTypes() {
		return CurrencyType.values();
	}

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

	public void bindBudgetYear() {
		budgetYearList = ccoaService.findAllBudgetYear();
	}

	public boolean isBranchDisabled() {
		return isBranchDisabled;
	}

	public void setBranchDisabled(boolean isBranchDisabled) {
		this.isBranchDisabled = isBranchDisabled;
	}

	public List<Branch> getBranchList() {
		return branchService.findAllBranch();
	}

	public String getBudgetYear() {
		return budgetYear;
	}

	public void setBudgetYear(String budgetYear) {
		this.budgetYear = budgetYear;
	}

	public List<String> getBudgetYearList() {
		return budgetYearList;
	}

	public boolean isBeforeDisabled() {
		return isBeforeDisabled;
	}

	public void setBeforeDisabled(boolean isBeforeDisabled) {
		this.isBeforeDisabled = isBeforeDisabled;
	}

	public boolean isBeforeBudgetEnd() {
		return isBeforeBudgetEnd;
	}

	public void setBeforeBudgetEnd(boolean isBeforeBudgetEnd) {
		this.isBeforeBudgetEnd = isBeforeBudgetEnd;
	}

}
