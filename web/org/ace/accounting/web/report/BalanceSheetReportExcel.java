package org.ace.accounting.web.report;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ace.accounting.common.MonthNames;
import org.ace.accounting.common.Utils;
import org.ace.accounting.common.utils.BusinessUtil;
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.report.balancesheet.BalanceSheetCriteria;
import org.ace.accounting.report.balancesheet.BalanceSheetDTO;
import org.ace.accounting.web.common.ExcelUtils;
import org.ace.java.component.ErrorCode;
import org.ace.java.component.SystemException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class BalanceSheetReportExcel {

	private XSSFWorkbook wb;
	// private FormulaEvaluator evaluator ;

	public BalanceSheetReportExcel(BalanceSheetCriteria criteria) {
		load(criteria);
	}

	private void load(BalanceSheetCriteria criteria) {
		try {
			InputStream inputStream = null;
			if (criteria.isMonth()) {
				inputStream = this.getClass().getResourceAsStream("/report-template/Report.xlsx");
			} else {
				inputStream = this.getClass().getResourceAsStream("/report-template/ReportDateRange.xlsx");
			}
			wb = new XSSFWorkbook(inputStream);
		} catch (IOException e) {
			throw new SystemException(ErrorCode.SYSTEM_ERROR, "Failed to load Report.xlsx tempalte", e);
		}
	}

	public Map<String, List<BalanceSheetDTO>> separateByPaymentChannel(List<BalanceSheetDTO> orderList) {
		Map<String, List<BalanceSheetDTO>> map = new LinkedHashMap<String, List<BalanceSheetDTO>>();
		if (orderList != null) {
			for (BalanceSheetDTO report : orderList) {
				if (map.containsKey(report.getAcCode())) {
					map.get(report.getAcCode()).add(report);
				} else {
					List<BalanceSheetDTO> list = new ArrayList<BalanceSheetDTO>();
					list.add(report);
					map.put(report.getAcCode(), list);
				}
			}
		}
		return map;

	}

	public void generate(OutputStream op, List<BalanceSheetDTO> orderList, BalanceSheetCriteria criteria, List<MonthNames> monthList, int budsmth, String budgetYear) {
		try {
			Sheet sheet = wb.getSheet("Report");
			String type = "";

			if (criteria.isMonth()) {
				type = "Monthly ";
			} else {
				type = "Date Range ";
			}
			if (criteria.getReportType().equalsIgnoreCase("B")) {
				wb.setSheetName(wb.getSheetIndex("Report"), "BalanceSheet");
				type = type.concat("Balance Sheet ");
			} else {
				wb.setSheetName(wb.getSheetIndex("Report"), "Profit&Loss");
				type = type.concat("Profit And Loss ");
			}

			XSSFCellStyle defaultCellStyle = ExcelUtils.getDefaultCellStyle(wb);
			XSSFCellStyle textCellStyle = ExcelUtils.getTextCellStyle(wb);
			XSSFCellStyle dateCellStyle = ExcelUtils.getDateCellStyle(wb);
			XSSFCellStyle currencyCellStyle = ExcelUtils.getCurrencyCellStyle(wb);
			XSSFCellStyle textAlignRightStyle = ExcelUtils.getTextAlignRightStyle(wb);
			XSSFCellStyle centerCellStyle = ExcelUtils.getAlignCenterStyle(wb);
			XSSFCellStyle textAlignCenterStyle = ExcelUtils.getAlignCenterStyle(wb);

			Row row;
			Cell cell;
			row = sheet.getRow(0);
			cell = row.getCell(0);
			String branch = "";
			String currency = "";
			int startYear = 3000;
			int endYear = 0;
			int y1 = 0, y2 = 0;
			Date budgetEndDate;
			Date budgetStartDate = BusinessUtil.getBudgetStartDate();
			int month = DateUtils.getMonthFromDate(budgetStartDate) + 1;
			String currentYear = BusinessUtil.getCurrentBudget();

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

			if (criteria.getBranch() == null) {
				branch = "All Branches";
			} else {
				branch = criteria.getBranch().getName();
			}

			if (criteria.getCurrency() == null) {
				currency = "All Currencies";
			} else {
				currency = criteria.getCurrency().getCurrencyCode();
			}
			if (criteria.isHomeConverted() && criteria.getCurrency() != null) {
				currency = currency + " By Home Currency Converted";
			}

			row = sheet.getRow(1);
			cell = row.createCell(7);
			String dateValue = "";
			if (criteria.isMonth()) {
				dateValue = type + "as at " + Utils.getDateFormatString(budgetStartDate) + " To " + Utils.getDateFormatString(budgetEndDate);
			} else {
				dateValue = type + "as at " + Utils.getDateFormatString(criteria.getStartDate()) + " To " + Utils.getDateFormatString(criteria.getEndDate());
			}
			cell.setCellValue(dateValue);
			cell.setCellStyle(textAlignCenterStyle);

			row = sheet.getRow(2);
			cell = row.getCell(1);
			cell.setCellValue(branch);
			// cell.setCellStyle(defaultCellStyle);

			row = sheet.getRow(3);
			cell = row.getCell(1);
			cell.setCellValue(currency);
			// cell.setCellStyle(defaultCellStyle);

			// cell = row.createCell(13);
			// cell.setCellValue("Date : ");
			// cell.setCellStyle(defaultCellStyle);

			cell = row.createCell(14);
			cell.setCellValue(Utils.getDateFormatString(new Date()));
			// cell.setCellStyle(dateCellStyle);

			row = sheet.getRow(4);
			cell = row.getCell(0);
			cell.setCellValue("Sr NO.");

			cell = row.getCell(1);
			cell.setCellValue("ACCODE");

			cell = row.getCell(2);
			cell.setCellValue("ACNAME");

			cell = row.getCell(3);
			MonthNames M1 = monthList.get((budsmth - 1 > 11) ? budsmth - 1 - 12 : budsmth - 1);
			if ((M1.ordinal() + 1) <= 12 && (M1.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M1.toString() + "-" + y1);
			} else {
				cell.setCellValue(M1.toString() + "-" + y2);

			}

			cell = row.getCell(4);
			MonthNames M2 = monthList.get((budsmth > 12) ? budsmth - 12 : budsmth);
			if ((M2.ordinal() + 1) <= 12 && (M2.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M2.toString() + "-" + y1);
			} else {
				cell.setCellValue(M2.toString() + "-" + y2);
			}

			cell = row.getCell(5);
			MonthNames M3 = monthList.get((budsmth + 1 > 11) ? budsmth + 1 - 12 : budsmth + 1);
			if ((M3.ordinal() + 1) <= 12 && (M3.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M3.toString() + "-" + y1);
			} else {
				cell.setCellValue(M3.toString() + "-" + y2);
			}

			cell = row.getCell(6);
			MonthNames M4 = monthList.get((budsmth + 2 > 11) ? budsmth + 2 - 12 : budsmth + 2);
			if ((M4.ordinal() + 1) <= 12 && (M4.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M4.toString() + "-" + y1);
			} else {
				cell.setCellValue(M4.toString() + "-" + y2);
			}

			cell = row.getCell(7);
			MonthNames M5 = monthList.get((budsmth + 3 > 11) ? budsmth + 3 - 12 : budsmth + 3);
			if ((M5.ordinal() + 1) <= 12 && (M5.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M5.toString() + "-" + y1);
			} else {
				cell.setCellValue(M5.toString() + "-" + y2);
			}

			cell = row.getCell(8);
			MonthNames M6 = monthList.get((budsmth + 4 > 11) ? budsmth + 4 - 12 : budsmth + 4);
			if ((M6.ordinal() + 1) <= 12 && (M6.ordinal() + 1) >= month) {
				cell.setCellValue(M6.toString() + "-" + y1);
			} else {
				cell.setCellValue(M6.toString() + "-" + y2);
			}

			cell = row.getCell(9);
			MonthNames M7 = monthList.get((budsmth + 5 > 11) ? budsmth + 5 - 12 : budsmth + 5);
			if ((M7.ordinal() + 1) <= 12 && (M7.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M7.toString() + "-" + y1);
			} else {
				cell.setCellValue(M7.toString() + "-" + y2);
			}

			cell = row.getCell(10);
			MonthNames M8 = monthList.get((budsmth + 6 > 11) ? budsmth + 6 - 12 : budsmth + 6);
			if ((M8.ordinal() + 1) <= 12 && (M8.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M8.toString() + "-" + y1);
			} else {
				cell.setCellValue(M8.toString() + "-" + y2);
			}

			cell = row.getCell(11);
			MonthNames M9 = monthList.get((budsmth + 7 > 11) ? budsmth + 7 - 12 : budsmth + 7);
			if ((M9.ordinal() + 1) <= 12 && (M9.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M9.toString() + "-" + y1);
			} else {
				cell.setCellValue(M9.toString() + "-" + y2);
			}

			cell = row.getCell(12);
			MonthNames M10 = monthList.get((budsmth + 8 > 11) ? budsmth + 8 - 12 : budsmth + 8);
			if ((M10.ordinal() + 1) <= 12 && (M10.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M10.toString() + "-" + y1);
			} else {
				cell.setCellValue(M10.toString() + "-" + y2);
			}

			cell = row.getCell(13);
			MonthNames M11 = monthList.get((budsmth + 9 > 11) ? budsmth + 9 - 12 : budsmth + 9);
			if ((M11.ordinal() + 1) <= 12 && (M11.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M11.toString() + "-" + y1);
			} else {
				cell.setCellValue(M11.toString() + "-" + y2);
			}

			cell = row.getCell(14);
			MonthNames M12 = monthList.get((budsmth + 10 > 11) ? budsmth + 10 - 12 : budsmth + 10);
			if ((M12.ordinal() + 1) <= 12 && (M12.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M12.toString() + "-" + y1);
			} else {
				cell.setCellValue(M12.toString() + "-" + y2);
			}

			int i = 4;
			int index = 0;

			for (BalanceSheetDTO report : orderList) {

				i = i + 1;
				index = index + 1;

				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue(index);
				cell.setCellStyle(textCellStyle);

				cell = row.createCell(1);
				cell.setCellValue(report.getAcCode());
				cell.setCellStyle(textCellStyle);

				cell = row.createCell(2);
				cell.setCellValue(report.getAcName());
				cell.setCellStyle(textCellStyle);

				cell = row.createCell(3);
				if (null != report.getM1()) {
					cell.setCellValue(Double.valueOf(report.getM1().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(0);
					cell.setCellStyle(currencyCellStyle);
				}
				cell = row.createCell(4);
				if (null != report.getM2()) {
					cell.setCellValue(Double.valueOf(report.getM2().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(0);
					cell.setCellStyle(currencyCellStyle);
				}

				cell = row.createCell(5);
				if (null != report.getM3()) {
					cell.setCellValue(Double.valueOf(report.getM3().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(0);
					cell.setCellStyle(currencyCellStyle);
				}

				cell = row.createCell(6);
				if (null != report.getM4()) {
					cell.setCellValue(Double.valueOf(report.getM4().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(0);
					cell.setCellStyle(currencyCellStyle);
				}

				cell = row.createCell(7);
				if (null != report.getM5()) {
					cell.setCellValue(Double.valueOf(report.getM5().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(0);
					cell.setCellStyle(currencyCellStyle);
				}

				cell = row.createCell(8);
				if (null != report.getM6()) {
					cell.setCellValue(Double.valueOf(report.getM6().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(0);
					cell.setCellStyle(currencyCellStyle);
				}

				cell = row.createCell(9);
				if (null != report.getM7()) {
					cell.setCellValue(Double.valueOf(report.getM7().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(0);
					cell.setCellStyle(currencyCellStyle);
				}
				cell = row.createCell(10);
				if (null != report.getM8()) {
					cell.setCellValue(Double.valueOf(report.getM8().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(0);
					cell.setCellStyle(currencyCellStyle);
				}

				cell = row.createCell(11);
				if (null != report.getM9()) {
					cell.setCellValue(Double.valueOf(report.getM9().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(0);
					cell.setCellStyle(currencyCellStyle);
				}
				cell = row.createCell(12);
				if (null != report.getM10()) {
					cell.setCellValue(Double.valueOf(report.getM10().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(0);
					cell.setCellStyle(currencyCellStyle);
				}

				cell = row.createCell(13);
				if (null != report.getM11()) {
					cell.setCellValue(Double.valueOf(report.getM11().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(0);
					cell.setCellStyle(currencyCellStyle);
				}

				cell = row.createCell(14);
				if (null != report.getM12()) {
					cell.setCellValue(Double.valueOf(report.getM12().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(0);
					cell.setCellStyle(currencyCellStyle);
				}
				// paymentChannel = report.getPaymentChannel().toString();
			}
			i = i + 1;
			sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 14));
			row = sheet.createRow(i);
			cell = row.createCell(0);

			cell = row.createCell(0);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(1);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(2);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(3);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(4);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");
			cell = row.createCell(5);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(6);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(7);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(8);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(9);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(10);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(11);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(12);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(13);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(14);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			wb.setPrintArea(0, 0, 14, 0, i);
			wb.write(op);
			op.flush();
			op.close();
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generateDateRange(OutputStream op, List<BalanceSheetDTO> orderList, BalanceSheetCriteria criteria, List<MonthNames> monthList, int budsmth, String budgetYear) {
		try {
			Sheet sheet = wb.getSheet("Report");
			String type = "";

			if (criteria.isMonth()) {
				type = "Monthly ";
			} else {
				type = "Date Range ";
			}
			if (criteria.getReportType().equalsIgnoreCase("B")) {
				wb.setSheetName(wb.getSheetIndex("Report"), "BalanceSheet");
				type = type.concat("Balance Sheet ");
			} else {
				wb.setSheetName(wb.getSheetIndex("Report"), "Profit&Loss");
				type = type.concat("Profit And Loss ");
			}

			XSSFCellStyle defaultCellStyle = ExcelUtils.getDefaultCellStyle(wb);
			XSSFCellStyle textCellStyle = ExcelUtils.getTextCellStyle(wb);
			XSSFCellStyle dateCellStyle = ExcelUtils.getDateCellStyle(wb);
			XSSFCellStyle currencyCellStyle = ExcelUtils.getCurrencyCellStyle(wb);
			XSSFCellStyle textAlignRightStyle = ExcelUtils.getTextAlignRightStyle(wb);
			XSSFCellStyle centerCellStyle = ExcelUtils.getAlignCenterStyle(wb);
			XSSFCellStyle textAlignCenterStyle = ExcelUtils.getAlignCenterStyle(wb);
			FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();

			Row row;
			Cell cell;
			row = sheet.getRow(0);
			cell = row.getCell(0);
			String branch = "";
			String currency = "";
			int startIndex;
			int startYear = 3000;
			int endYear = 0;
			int y1 = 0, y2 = 0;
			Date budgetEndDate;
			Date budgetStartDate = BusinessUtil.getBudgetStartDate();
			int month = DateUtils.getMonthFromDate(budgetStartDate) + 1;
			String currentYear = BusinessUtil.getCurrentBudget();

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

			if (criteria.getBranch() == null) {
				branch = "All Branches";
			} else {
				branch = criteria.getBranch().getName();
			}

			if (criteria.getCurrency() == null) {
				currency = "All Currencies";
			} else {
				currency = criteria.getCurrency().getCurrencyCode();
			}
			if (criteria.isHomeConverted() && criteria.getCurrency() != null) {
				currency = currency + " By Home Currency Converted";
			}

			row = sheet.getRow(1);
			cell = row.createCell(7);
			String dateValue = "";
			if (criteria.isMonth()) {
				dateValue = type + "as at " + Utils.getDateFormatString(budgetStartDate) + " To " + Utils.getDateFormatString(budgetEndDate);
			} else {
				dateValue = type + "as at " + Utils.getDateFormatString(criteria.getStartDate()) + " To " + Utils.getDateFormatString(criteria.getEndDate());
			}
			cell.setCellValue(dateValue);
			cell.setCellStyle(textAlignCenterStyle);

			row = sheet.getRow(2);
			cell = row.getCell(1);
			cell.setCellValue(branch);
			// cell.setCellStyle(defaultCellStyle);

			row = sheet.getRow(3);
			cell = row.getCell(1);
			cell.setCellValue(currency);
			// cell.setCellStyle(defaultCellStyle);

			// cell = row.createCell(13);
			// cell.setCellValue("Date : ");
			// cell.setCellStyle(defaultCellStyle);

			cell = row.createCell(15);
			cell.setCellValue(Utils.getDateFormatString(new Date()));

			// cell.setCellStyle(defaultCellStyle);

			row = sheet.getRow(4);
			cell = row.getCell(0);
			cell.setCellValue("Sr NO.");

			cell = row.getCell(1);
			cell.setCellValue("ACCODE");

			cell = row.getCell(2);
			cell.setCellValue("ACNAME");

			cell = row.getCell(3);
			MonthNames M1 = monthList.get((budsmth - 1 > 11) ? budsmth - 1 - 12 : budsmth - 1);
			if ((M1.ordinal() + 1) <= 12 && (M1.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M1.toString() + "-" + y1);
			} else {
				cell.setCellValue(M1.toString() + "-" + y2);

			}

			cell = row.getCell(4);
			MonthNames M2 = monthList.get((budsmth > 12) ? budsmth - 12 : budsmth);
			if ((M2.ordinal() + 1) <= 12 && (M2.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M2.toString() + "-" + y1);
			} else {
				cell.setCellValue(M2.toString() + "-" + y2);
			}

			cell = row.getCell(5);
			MonthNames M3 = monthList.get((budsmth + 1 > 11) ? budsmth + 1 - 12 : budsmth + 1);
			if ((M3.ordinal() + 1) <= 12 && (M3.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M3.toString() + "-" + y1);
			} else {
				cell.setCellValue(M3.toString() + "-" + y2);
			}

			cell = row.getCell(6);
			MonthNames M4 = monthList.get((budsmth + 2 > 11) ? budsmth + 2 - 12 : budsmth + 2);
			if ((M4.ordinal() + 1) <= 12 && (M4.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M4.toString() + "-" + y1);
			} else {
				cell.setCellValue(M4.toString() + "-" + y2);
			}

			cell = row.getCell(7);
			MonthNames M5 = monthList.get((budsmth + 3 > 11) ? budsmth + 3 - 12 : budsmth + 3);
			if ((M5.ordinal() + 1) <= 12 && (M5.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M5.toString() + "-" + y1);
			} else {
				cell.setCellValue(M5.toString() + "-" + y2);
			}

			cell = row.getCell(8);
			MonthNames M6 = monthList.get((budsmth + 4 > 11) ? budsmth + 4 - 12 : budsmth + 4);
			if ((M6.ordinal() + 1) <= 12 && (M6.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M6.toString() + "-" + y1);
			} else {
				cell.setCellValue(M6.toString() + "-" + y2);
			}

			cell = row.getCell(9);
			MonthNames M7 = monthList.get((budsmth + 5 > 11) ? budsmth + 5 - 12 : budsmth + 5);
			if ((M7.ordinal() + 1) <= 12 && (M7.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M7.toString() + "-" + y1);
			} else {
				cell.setCellValue(M7.toString() + "-" + y2);
			}

			cell = row.getCell(10);
			MonthNames M8 = monthList.get((budsmth + 6 > 11) ? budsmth + 6 - 12 : budsmth + 6);
			if ((M8.ordinal() + 1) <= 12 && (M8.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M8.toString() + "-" + y1);
			} else {
				cell.setCellValue(M8.toString() + "-" + y2);
			}

			cell = row.getCell(11);
			MonthNames M9 = monthList.get((budsmth + 7 > 11) ? budsmth + 7 - 12 : budsmth + 7);
			if ((M9.ordinal() + 1) <= 12 && (M9.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M9.toString() + "-" + y1);
			} else {
				cell.setCellValue(M9.toString() + "-" + y2);
			}

			cell = row.getCell(12);
			MonthNames M10 = monthList.get((budsmth + 8 > 11) ? budsmth + 8 - 12 : budsmth + 8);
			if ((M10.ordinal() + 1) <= 12 && (M10.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M10.toString() + "-" + y1);
			} else {
				cell.setCellValue(M10.toString() + "-" + y2);
			}

			cell = row.getCell(13);
			MonthNames M11 = monthList.get((budsmth + 9 > 11) ? budsmth + 9 - 12 : budsmth + 9);
			if ((M11.ordinal() + 1) <= 12 && (M11.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M11.toString() + "-" + y1);
			} else {
				cell.setCellValue(M11.toString() + "-" + y2);
			}

			cell = row.getCell(14);
			MonthNames M12 = monthList.get((budsmth + 10 > 11) ? budsmth + 10 - 12 : budsmth + 10);
			if ((M12.ordinal() + 1) <= 12 && (M12.ordinal() + 1) >= budsmth) {
				cell.setCellValue(M12.toString() + "-" + y1);
			} else {
				cell.setCellValue(M12.toString() + "-" + y2);
			}

			cell = row.getCell(15);
			cell.setCellValue("Total");

			int i = 4;
			int index = 0;
			BigDecimal rowTotal = BigDecimal.ZERO;

			for (BalanceSheetDTO report : orderList) {

				i = i + 1;
				index = index + 1;
				startIndex = i + 1;
				rowTotal = BigDecimal.ZERO;

				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue(index);
				cell.setCellStyle(textCellStyle);

				cell = row.createCell(1);
				cell.setCellValue(report.getAcCode());
				cell.setCellStyle(textCellStyle);

				cell = row.createCell(2);
				cell.setCellValue(report.getAcName());
				cell.setCellStyle(textCellStyle);

				cell = row.createCell(3);
				if (null != report.getM1()) {
					rowTotal = rowTotal.add(report.getM1());
					cell.setCellValue(Double.valueOf(report.getM1().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(" ");
					cell.setCellStyle(currencyCellStyle);
				}
				cell = row.createCell(4);
				if (null != report.getM2()) {
					rowTotal = rowTotal.add(report.getM2());
					cell.setCellValue(Double.valueOf(report.getM2().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(" ");
					cell.setCellStyle(currencyCellStyle);
				}

				cell = row.createCell(5);
				if (null != report.getM3()) {
					rowTotal = rowTotal.add(report.getM3());
					cell.setCellValue(Double.valueOf(report.getM3().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(" ");
					cell.setCellStyle(currencyCellStyle);
				}

				cell = row.createCell(6);
				if (null != report.getM4()) {
					rowTotal = rowTotal.add(report.getM4());
					cell.setCellValue(Double.valueOf(report.getM4().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(" ");
					cell.setCellStyle(currencyCellStyle);
				}

				cell = row.createCell(7);
				if (null != report.getM5()) {
					rowTotal = rowTotal.add(report.getM5());
					cell.setCellValue(Double.valueOf(report.getM5().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(" ");
					cell.setCellStyle(currencyCellStyle);
				}

				cell = row.createCell(8);
				if (null != report.getM6()) {
					rowTotal = rowTotal.add(report.getM6());
					cell.setCellValue(Double.valueOf(report.getM6().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(" ");
					cell.setCellStyle(currencyCellStyle);
				}

				cell = row.createCell(9);
				if (null != report.getM7()) {
					rowTotal = rowTotal.add(report.getM7());
					cell.setCellValue(Double.valueOf(report.getM7().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(" ");
					cell.setCellStyle(currencyCellStyle);
				}
				cell = row.createCell(10);
				if (null != report.getM8()) {
					rowTotal = rowTotal.add(report.getM8());
					cell.setCellValue(Double.valueOf(report.getM8().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(" ");
					cell.setCellStyle(currencyCellStyle);
				}

				cell = row.createCell(11);
				if (null != report.getM9()) {
					rowTotal = rowTotal.add(report.getM9());
					cell.setCellValue(Double.valueOf(report.getM9().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(" ");
					cell.setCellStyle(currencyCellStyle);
				}
				cell = row.createCell(12);
				if (null != report.getM10()) {
					rowTotal = rowTotal.add(report.getM10());
					cell.setCellValue(Double.valueOf(report.getM10().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(" ");
					cell.setCellStyle(currencyCellStyle);
				}

				cell = row.createCell(13);
				if (null != report.getM11()) {
					rowTotal = rowTotal.add(report.getM11());
					cell.setCellValue(Double.valueOf(report.getM11().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(" ");
					cell.setCellStyle(currencyCellStyle);
				}

				cell = row.createCell(14);
				if (null != report.getM12()) {
					rowTotal = rowTotal.add(report.getM12());
					cell.setCellValue(Double.valueOf(report.getM12().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {
					cell.setCellValue(" ");
					cell.setCellStyle(currencyCellStyle);
				}

				cell = row.createCell(15);
				if (null != report.getM1()) {
					cell.setCellStyle(currencyCellStyle);
					// rowTotal = "SUM(D" + startIndex + ":O" + i + ")";
					// cell.setCellFormula(rowTotal);
					cell.setCellValue(Double.valueOf(rowTotal.toString()));
					// formulaEvaluator.evaluateFormulaCell(cell);
				} else {
					cell.setCellStyle(currencyCellStyle);
					cell.setCellValue(" ");
				}

			}
			i = i + 1;
			sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 15));
			row = sheet.createRow(i);
			cell = row.createCell(0);

			cell = row.createCell(0);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(1);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(2);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(3);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(4);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");
			cell = row.createCell(5);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(6);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(7);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(8);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(9);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(10);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(11);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(12);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(13);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(14);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			cell = row.createCell(15);
			cell.setCellStyle(defaultCellStyle);
			cell.setCellValue("-");

			wb.setPrintArea(0, 0, 15, 0, i);
			wb.write(op);
			op.flush();
			op.close();
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
