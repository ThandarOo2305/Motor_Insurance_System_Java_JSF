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
import org.ace.accounting.common.utils.DateUtils;
import org.ace.accounting.dto.TrialBalanceCriteriaDto;
import org.ace.accounting.dto.TrialBalanceReportDto;
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

public class TrialBalanceDetailReportExcel {

	private XSSFWorkbook wb;
	// private FormulaEvaluator evaluator ;

	public TrialBalanceDetailReportExcel() {
		load();
	}

	private void load() {
		try {
			InputStream inp = this.getClass().getResourceAsStream("/report-template/TrialBalance_Report.xlsx");
			wb = new XSSFWorkbook(inp);
		} catch (IOException e) {
			throw new SystemException(ErrorCode.SYSTEM_ERROR, "Failed to load TrialBalance_Report.xlsx tempalte", e);
		}
	}

	public Map<String, List<TrialBalanceReportDto>> separateByPaymentChannel(List<TrialBalanceReportDto> reportResultList) {
		Map<String, List<TrialBalanceReportDto>> map = new LinkedHashMap<String, List<TrialBalanceReportDto>>();
		if (reportResultList != null) {
			for (TrialBalanceReportDto report : reportResultList) {
				if (map.containsKey(report.getAcode())) {
					map.get(report.getAcode()).add(report);
				} else {
					List<TrialBalanceReportDto> list = new ArrayList<TrialBalanceReportDto>();
					list.add(report);
					map.put(report.getAcode(), list);
				}
			}
		}
		return map;

	}

	public void generate(OutputStream op, List<TrialBalanceReportDto> reportResultList, TrialBalanceCriteriaDto criteria, Date postingDate) {
		try {
			Sheet sheet = wb.getSheet("TrialBalanceReport");
			XSSFCellStyle textCellStyle = ExcelUtils.getTextCellStyleII(wb);
			XSSFCellStyle textCellStyleLeft = ExcelUtils.getTextCellLeftStyle(wb);
			XSSFCellStyle currencyCellStyle = ExcelUtils.getCurrencyCellStyleII(wb);
			FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
			XSSFCellStyle pDSDateCellStyle = ExcelUtils.getPDSDateCellStyle(wb);
			XSSFCellStyle pyiDaungSuStyleRight = ExcelUtils.getPyiDaungSuStyleRight(wb);
			XSSFCellStyle pDSCurrencyFontBoldCellStyle = ExcelUtils.getPDSCurrencyFontBoldCellStyle(wb);
			XSSFCellStyle pDSFontBoldAlignCenterStyle = ExcelUtils.getPDSFontBoldAlignCenterStyle(wb);
			XSSFCellStyle pyiDaungSuStyleLeft = ExcelUtils.getPyiDaungSuStyleLeft(wb);

			Row row;
			Cell cell;
			row = sheet.getRow(0);
			cell = row.getCell(0);
			String branch = "";
			String currency = "";
			String type = "";
			String month = null;
			int year;
			int monthVal = criteria.getRequiredMonth();
			month = String.valueOf(MonthNames.values()[monthVal]);
			year = criteria.getRequiredYear();

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
			if (criteria.isHomeCurrencyConverted() && criteria.getCurrency() != null) {
				currency = currency + " By Home Currency Converted";
			}

			/*
			 * if (criteria.isGroup()) { type = "Group "; } else { type = "Detail "; }
			 */

			row = sheet.getRow(1);
			
			
			cell = row.getCell(1);
			String dateValue = "";
			//dateValue = type + " Trial Balance Listing " + month + " as at " + DateUtils.formatDateToString(postingDate);
			dateValue = "Trial Balance Report For " + month + " - " + year ;
			cell.setCellValue(dateValue);
			cell.setCellStyle(pDSFontBoldAlignCenterStyle);
			

			row = sheet.getRow(3);
			cell = row.getCell(0);
			branch = "Branch   : " + branch;
			cell.setCellValue(branch);
			cell.setCellStyle(pyiDaungSuStyleLeft);

			row = sheet.getRow(4);
			cell = row.getCell(0);
			currency = "Currency : " + currency;
			cell.setCellValue(currency);
			cell.setCellStyle(pyiDaungSuStyleLeft);

			cell = row.getCell(6);
			String reportDate = "";
			reportDate = "Date : " + Utils.getDateFormatString(new Date());
			cell.setCellValue(reportDate);
			//cell.setCellStyle(pyiDaungSuStyleLeft);

			int i = 5;
			int index = 0;
			String dbTotal = "";
			String crtotal = "";
			BigDecimal x = BigDecimal.ZERO;

			for (TrialBalanceReportDto report : reportResultList) {

				i = i + 1;
				index = index + 1;

				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue(index);
				cell.setCellStyle(textCellStyle);

				
				cell = row.createCell(1);
				cell.setCellValue(report.getAcode());
				cell.setCellStyle(textCellStyle);

				cell = row.createCell(2);
				cell.setCellValue(report.getAcname());
				cell.setCellStyle(textCellStyleLeft);

				cell = row.createCell(3);
				if (null != report.getmDebit()) {
					cell.setCellValue(Double.valueOf(report.getmDebit().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {

					cell.setCellValue(0);
					cell.setCellStyle(currencyCellStyle);
				}

				
				  cell = row.createCell(4);
				  if (null != report.getmCredit()) {
				  cell.setCellValue(Double.valueOf(report.getmCredit().toString()));
				  cell.setCellStyle(currencyCellStyle);
				  } else {
				  
				  cell.setCellValue(0); 
				  cell.setCellStyle(currencyCellStyle); 
				  }
				 
				
				cell = row.createCell(5);
				if (null != report.getDebit()) {
					cell.setCellValue(Double.valueOf(report.getDebit().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {

					cell.setCellValue(0);
					cell.setCellStyle(currencyCellStyle);
				}
				
				cell = row.createCell(6);
				if (null != report.getCredit()) {
					cell.setCellValue(Double.valueOf(report.getCredit().toString()));
					cell.setCellStyle(currencyCellStyle);
				} else {

					cell.setCellValue(0);
					cell.setCellStyle(currencyCellStyle);
				}

			}
			i = i + 1;
			sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 1));
			row = sheet.createRow(i);
			
			cell = row.createCell(0);
			cell.setCellStyle(pDSFontBoldAlignCenterStyle);
			
			
			cell = row.createCell(1);
			cell.setCellStyle(pDSFontBoldAlignCenterStyle);
			
			cell = row.createCell(2);
			ExcelUtils.setRegionBorderThin(new CellRangeAddress(i, i, 0, 1), sheet);
			cell.setCellValue("Total");
			cell.setCellStyle(pDSFontBoldAlignCenterStyle);

			

			cell = row.createCell(3);
			cell.setCellStyle(pDSCurrencyFontBoldCellStyle);
			dbTotal = "SUM(D6:D" + i + ")";
			// cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula(dbTotal);
			formulaEvaluator.evaluateFormulaCell(cell);

			cell = row.createCell(4);
			cell.setCellStyle(pDSCurrencyFontBoldCellStyle);
			crtotal = "SUM(E6:E" + i + ")";
			// cell.setCellType(CellType.FORMULA);
			cell.setCellFormula(crtotal);
			formulaEvaluator.evaluateFormulaCell(cell);
			
			cell = row.createCell(5);
			cell.setCellStyle(pDSCurrencyFontBoldCellStyle);
			dbTotal = "SUM(F6:F" + i + ")";
			// cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula(dbTotal);
			formulaEvaluator.evaluateFormulaCell(cell);

			cell = row.createCell(6);
			cell.setCellStyle(pDSCurrencyFontBoldCellStyle);
			crtotal = "SUM(G6:G" + i + ")";
			// cell.setCellType(CellType.FORMULA);
			cell.setCellFormula(crtotal);
			formulaEvaluator.evaluateFormulaCell(cell);

			wb.setPrintArea(0, 0, 6, 0, i + 2);
			wb.write(op);
			op.flush();
			op.close();
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
