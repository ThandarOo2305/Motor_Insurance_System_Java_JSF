package org.ace.accounting.web.listing;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.ace.accounting.common.Utils;
import org.ace.accounting.dto.AccountLedgerDto;
import org.ace.accounting.dto.AccountLedgerGroupDto;
import org.ace.accounting.system.chartaccount.ChartOfAccount;
import org.ace.accounting.web.common.ExcelUtils;
import org.ace.java.component.ErrorCode;
import org.ace.java.component.SystemException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AccLedgerListingExcel {

	private XSSFWorkbook wb;
	// private FormulaEvaluator evaluator ;

	public AccLedgerListingExcel() {
		load();
	}

	private void load() {
		try {
			InputStream inp = this.getClass().getResourceAsStream("/report-template/AccLedgerListing.xlsx");
			wb = new XSSFWorkbook(inp);
		} catch (IOException e) {
			throw new SystemException(ErrorCode.SYSTEM_ERROR, "Failed to load AccLedgerListing.xlsx tempalte", e);
		}
	}

	public void generate(OutputStream op, List<AccountLedgerDto> list, List<ChartOfAccount> coaList, List<AccountLedgerGroupDto> groupList, String branchString,
			String currencyString, String startDate, String endDate) {
		try {
			Sheet sheet = wb.getSheet("AccLedgerListing");
			XSSFCellStyle textCellStyle = ExcelUtils.getTextCellStyle(wb);
			XSSFCellStyle currencyCellStyle = ExcelUtils.getCurrencyCellStyle(wb);
			XSSFCellStyle defaultCellStyle = ExcelUtils.getDefaultCellStyle(wb);
			XSSFCellStyle pyiDaungSuStyleRight = ExcelUtils.getPyiDaungSuStyleRight(wb);
			XSSFCellStyle pDSFontBoldAlignCenterStyle = ExcelUtils.getPDSFontBoldAlignCenterStyle(wb);
			XSSFCellStyle pDSCurrencyFontBoldCellStyle = ExcelUtils.getPDSCurrencyFontBoldCellStyle(wb);
			XSSFCellStyle zawgyiCellStyleLeft = ExcelUtils.getZawgyiCellStyleLeft(wb);
			XSSFCellStyle zawgyiCellStyleLeftBorder = ExcelUtils.getZawgyiCellStyleLeftBorder(wb);
			XSSFCellStyle pDSDateCellStyle = ExcelUtils.getPDSDateCellStyle(wb);
			XSSFCellStyle textCellStyleLeft = ExcelUtils.getTextCellLeftStyleII(wb);
			XSSFCellStyle pDSFontBoldAlignLeftStyle = ExcelUtils.getPDSFontBoldAlignLeftStyle(wb);
			XSSFCellStyle pyiDaungSuStyleLeft = ExcelUtils.getPyiDaungSuStyleLeft(wb);
			XSSFCellStyle backgroundStyle = ExcelUtils.getBackgroundcolor(wb);
			// this is not necessary using apache poi 3.17

			Row row;
			Cell cell;
			row = sheet.getRow(0);
			cell = row.getCell(0);

			row = sheet.getRow(1);
			cell = row.getCell(0);
			String dateValue = "";
			dateValue = " Account Ledger Listing " + startDate + " To " + endDate;
			cell.setCellValue(dateValue);
			cell.setCellStyle(pDSDateCellStyle);

			row = sheet.getRow(3);
			cell = row.getCell(0);
			branchString = "Branch   : " + branchString;
			cell.setCellValue(branchString);
			cell.setCellStyle(pyiDaungSuStyleLeft);

			row = sheet.getRow(4);
			cell = row.getCell(0);
			currencyString = "Currency : " + currencyString;
			cell.setCellValue(currencyString);
			cell.setCellStyle(pyiDaungSuStyleLeft);

			cell = row.getCell(6);
			String reportDate = "";
			reportDate = "Date : " + Utils.getDateFormatString(new Date());
			cell.setCellValue(reportDate);
			cell.setCellStyle(pyiDaungSuStyleRight);

			// the header rows and cells
			String[] headers = { "Sr No.", "Date", "Voucher No. ", "Transaction Type", "Debit", "Credit", "Balance" };

			int i = 5;
			int j = 5;
			int index = 0;
			String accode = null;
			String acname = null;
			for (AccountLedgerGroupDto group : groupList) {
				accode = group.getCoa().getAcCode();
				acname = group.getCoa().getAcName();
				row = sheet.createRow(j);
				
				cell = row.createCell(0);
				// cell.setCellValue(dto.getCoa().getAcCode());
				cell.setCellValue(accode);
				cell.setCellStyle(zawgyiCellStyleLeftBorder);
				
				cell = row.createCell(1);
				// cell.setCellValue(dto.getCoa().getAcName());
				cell.setCellValue(acname);				
				cell.setCellStyle(zawgyiCellStyleLeft);

				cell = row.createCell(6);
				cell.setCellStyle(pyiDaungSuStyleRight);

				List<AccountLedgerDto> listing = group.getAccountLedgerList();
				j = j + 1;
				row = sheet.createRow(j);
				for (int c = 0; c < headers.length; c++) {
					cell = row.createCell(c);
					cell.setCellValue(headers[c]);
					cell.setCellStyle(pDSFontBoldAlignCenterStyle);
					cell.setCellStyle(backgroundStyle);
				}
				i = j;
				for (AccountLedgerDto dto : listing) {
					i = i + 1;
					index = index + 1;
					row = sheet.createRow(i);
					cell = row.createCell(0);
					if (dto.getNarration() == "Closing Balance") {
						cell.setCellValue(index);
						cell.setCellStyle(defaultCellStyle);

						cell = row.createCell(1);
						cell.setCellValue(Utils.getDateFormatString(dto.getSettlementDate()));
						cell.setCellStyle(textCellStyle);
						
						cell = row.createCell(2);
						cell.setCellValue(dto.geteNo());
						cell.setCellStyle(textCellStyleLeft);


						cell = row.createCell(3);
						cell.setCellValue(dto.getNarration());
						cell.setCellStyle(textCellStyleLeft);

						cell = row.createCell(4);
						if (null != dto.getDebit()) {
							cell.setCellValue(Double.valueOf(dto.getDebit().toString()));
							cell.setCellStyle(currencyCellStyle);
						} else {
							cell.setCellValue(0);
							cell.setCellStyle(currencyCellStyle);
						}

						cell = row.createCell(5);
						if (null != dto.getCredit()) {
							cell.setCellValue(Double.valueOf(dto.getCredit().toString()));
							cell.setCellStyle(currencyCellStyle);
						}

						cell = row.createCell(6);
						if (null != dto.getDblBalance()) {
							cell.setCellValue(Double.valueOf(dto.getDblBalance().toString()));
							cell.setCellStyle(currencyCellStyle);
						}
					} else {
						cell.setCellValue(index);
						cell.setCellStyle(defaultCellStyle);

						cell = row.createCell(1);
						cell.setCellValue(Utils.getDateFormatString(dto.getSettlementDate()));
						cell.setCellStyle(textCellStyle);
						
						cell = row.createCell(2);
						cell.setCellValue(dto.geteNo());
						cell.setCellStyle(textCellStyleLeft);

						cell = row.createCell(3);
						if (dto.getNarration() == "Opening Balance") {
							cell.setCellValue(dto.getNarration());
							 cell.setCellStyle(textCellStyleLeft);
						} else {
							
						 cell.setCellValue(dto.getNarration());
						 cell.setCellStyle(textCellStyleLeft);
						 
						}

						/*
						 * cell = row.createCell(4); if (null != dto.getDebit()) {
						 * cell.setCellValue(Double.valueOf(dto.getDebit().toString()));
						 * cell.setCellStyle(currencyCellStyle); } else { cell.setCellValue(0);
						 * cell.setCellStyle(currencyCellStyle); }
						 */
						
						cell = row.createCell(4);
						if (dto.getDebit().toString().equals("0")) {
							cell.setCellValue("");
							cell.setCellStyle(currencyCellStyle);
						} else {
							cell.setCellValue(Double.valueOf(dto.getDebit().toString()));
							cell.setCellStyle(currencyCellStyle);
						}

						/*
						 * cell = row.createCell(5); if (null != dto.getCredit()) {
						 * cell.setCellValue(Double.valueOf(dto.getCredit().toString()));
						 * cell.setCellStyle(currencyCellStyle); }
						 */
						
						cell = row.createCell(5);
						if (dto.getCredit().toString().equals("0")) {
							cell.setCellValue("");
							cell.setCellStyle(currencyCellStyle);
						} else {
							cell.setCellValue(Double.valueOf(dto.getCredit().toString()));
							cell.setCellStyle(currencyCellStyle);
						}


						/*
						 * cell = row.createCell(6); if (null != dto.getDblBalance()) {
						 * cell.setCellValue(Double.valueOf(dto.getDblBalance().toString()));
						 * cell.setCellStyle(currencyCellStyle); }
						 */
						
						cell = row.createCell(6);
						if (dto.getDblBalance().toString().equals("0")) {
							cell.setCellValue("");
							cell.setCellStyle(currencyCellStyle);
						} else {
							cell.setCellValue(Double.valueOf(dto.getDblBalance().toString()));
							cell.setCellStyle(currencyCellStyle);
						}

					}

				}
				j = i + 1;
				i = j;
				index = 0;
			}
			wb.setPrintArea(0, 0, 6, 0, i);
			wb.write(op);
			op.flush();
			op.close();

		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

}
