package org.ace.accounting.web.report;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.ace.accounting.common.FormatStyle;
import org.ace.accounting.common.Utils;
import org.ace.accounting.dto.LeftAndRightDto;
import org.ace.accounting.dto.ReportStatementDto;
import org.ace.accounting.system.formatfile.ColType;
import org.ace.accounting.web.common.ExcelUtils;
import org.ace.java.component.ErrorCode;
import org.ace.java.component.SystemException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReportStatementReportExcel {

	private XSSFWorkbook wb;
	// private FormulaEvaluator evaluator ;

	public ReportStatementReportExcel() {
	}

	public ReportStatementReportExcel(FormatStyle formatStyle, boolean isIncludeObal) {
		load(formatStyle, isIncludeObal);
	}

	private void load(FormatStyle formatStyle, boolean isIncludeObal) {
		try {
			InputStream inputStream;
			if (formatStyle.equals(FormatStyle.VF)) {
				if (isIncludeObal) {
					inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("ReportStatementWithOblReport.xlsx");
				} else {
					inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("ReportStatementVerticalReport.xlsx");
				}
			} else {
				inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("ReportStatementHorizontalReport.xlsx");
			}
			wb = new XSSFWorkbook(inputStream);
		} catch (IOException e) {
			throw new SystemException(ErrorCode.SYSTEM_ERROR, "Failed to load ReportStatementVerticalReport.xlsx tempalte", e);
		}
	}

	public void generate(OutputStream op, List<ReportStatementDto> dtoList, ReportStatementDto reportStatementDto, String branchString, String currencyString,
			String headingString) {
		try {
			Sheet sheet = wb.getSheet("ReportStatementVerticalReport");
			XSSFCellStyle defaultCellStyle = ExcelUtils.getDefaultCellStyleII(wb);
			XSSFCellStyle textCellStyle = ExcelUtils.getTextCellStyleII(wb);
			XSSFCellStyle currencyCellStyleI = ExcelUtils.getCurrencyCellStyle(wb);
			XSSFCellStyle currencyCellStyle = ExcelUtils.getCurrencyCellStyleII(wb);
			XSSFCellStyle zawgyiCellStyle = ExcelUtils.getZawgyiCellStyleRight(wb);
			XSSFCellStyle pyiDaungSuStyleRight = ExcelUtils.getPyiDaungSuStyleRight(wb);
			XSSFCellStyle pDSCurrencyFontBoldCellStyle = ExcelUtils.getPDSCurrencyFontBoldCellStyle(wb);
			XSSFCellStyle pDSFontBoldAlignCenterStyle = ExcelUtils.getPDSFontBoldAlignCenterStyle(wb);
			XSSFCellStyle pDSDateCellStyle = ExcelUtils.getPDSDateCellStyle(wb);
			XSSFCellStyle pyiDaungSuStyleLeft = ExcelUtils.getPyiDaungSuStyleLeft(wb);

			Row row;
			Cell cell;
			row = sheet.getRow(0);
			cell = row.getCell(0);

			row = sheet.getRow(1);
			cell = row.getCell(0);
			cell.setCellValue(headingString);
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

			cell = row.getCell(2);
			String reportDate = "Date : " + Utils.getDateFormatString(new Date());
			cell.setCellValue(reportDate);
			cell.setCellStyle(pyiDaungSuStyleRight);

			int i = 5;
			int index = 0;

			for (ReportStatementDto report : dtoList) {
				i = i + 1;
				index = index + 1;
				row = sheet.createRow(i);
				cell = row.createCell(0);
				if (null != report.getAcCode()) {
					cell.setCellValue(report.getAcCode());
					cell.setCellStyle(textCellStyle);

					cell = row.createCell(1);
					if (null != report.getDesp()) {
						cell.setCellValue(report.getDesp());
						cell.setCellStyle(textCellStyle);
					} else {
						if (null != report.getrDesp()) {
							cell.setCellValue(report.getrDesp());
							cell.setCellStyle(textCellStyle);
						} /*
							 * else { cell.setCellValue("");
							 * cell.setCellStyle(textCellStyle); }
							 */

					}

					cell = row.createCell(2);
					if ((null != report.getcBal() && report.getcBal().doubleValue() != 0)) {
						cell.setCellValue(new DecimalFormat("#,##0.00").format(report.getcBal().setScale(2, RoundingMode.CEILING)));
						cell.setCellValue(Double.valueOf(report.getcBal().toString()));
						cell.setCellStyle(currencyCellStyle);
					} else {
						if ((null != report.getrAmt() && report.getrAmt().doubleValue() != 0)) {
							cell.setCellValue(Double.valueOf(report.getrAmt().toString()));
							cell.setCellStyle(currencyCellStyle);
						} else {
							cell.setCellValue("");
							cell.setCellStyle(currencyCellStyle);
						}

					}
				} else {
					
					if (null != report.getrAcCode()) {
						cell.setCellValue(report.getrAcCode());
						cell.setCellStyle(textCellStyle);
					} else {
						cell.setCellValue(report.getrAcCode());
						cell.setCellStyle(textCellStyle);
					}

					cell = row.createCell(1);
					if (null != report.getDesp()) {
						cell.setCellValue(report.getDesp());
						cell.setCellStyle(textCellStyle);
					} else {
						if (null != report.getrDesp() && null != report.getrAcCode()) {
							cell.setCellValue(report.getrDesp());
							cell.setCellStyle(textCellStyle);
						} else {
							cell.setCellValue(report.getrDesp());
							cell.setCellStyle(textCellStyle);
						}

					}

					cell = row.createCell(2);			
					if ((null != report.getcBal() && report.getcBal().doubleValue() != 0)) {
						if (null != report.getDesp() || null == report.getrAcCode()) {
						cell.setCellValue(new DecimalFormat("#,##0.00").format(report.getcBal().setScale(2, RoundingMode.CEILING)));
						cell.setCellValue(Double.valueOf(report.getcBal().toString()));
						cell.setCellStyle(currencyCellStyle);
						} else {
							
							cell.setCellValue(new DecimalFormat("#,##0.00").format(report.getcBal().setScale(2, RoundingMode.CEILING)));
							cell.setCellValue(Double.valueOf(report.getcBal().toString()));
							cell.setCellStyle(currencyCellStyle);
						}
					} else {
						
						if(null != report.getrAcCode()) {
						if ((null != report.getrAmt() && report.getrAmt().doubleValue() != 0)) {
							cell.setCellValue(Double.valueOf(report.getrAmt().toString()));
							cell.setCellStyle(currencyCellStyle);
						} else {
							cell.setCellValue("");
							cell.setCellStyle(currencyCellStyleI);
						}
						} else {
							if ((null != report.getrAmt() && report.getrAmt().doubleValue() != 0)) {
								cell.setCellValue(Double.valueOf(report.getrAmt().toString()));
								cell.setCellStyle(currencyCellStyleI);
							} else {
								cell.setCellValue("");
								cell.setCellStyle(currencyCellStyleI);
							}
						}

					}
				}

				// }
			}

			// i = i + 1;
			// sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 1));
			wb.setPrintArea(0, 0, 2, 0, i + 2);
			wb.write(op);
			op.flush();
			op.close();
			// }
		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	public void generateHorizontal(OutputStream op, List<ReportStatementDto> dtoList, ReportStatementDto reportStatementDto, String branchString, String currencyString,
			String headingString) {
		try {
			Sheet sheet = wb.getSheet("ReportStatementHorizontalReport");
			XSSFCellStyle defaultCellStyle = ExcelUtils.getDefaultCellStyleII(wb);
			XSSFCellStyle textCellStyle = ExcelUtils.getTextCellStyleII(wb);
			XSSFCellStyle currencyCellStyle = ExcelUtils.getCurrencyCellStyleII(wb);
			XSSFCellStyle zawgyiCellStyle = ExcelUtils.getZawgyiCellStyleRight(wb);
			XSSFCellStyle pyiDaungSuStyleRight = ExcelUtils.getPyiDaungSuStyleRight(wb);
			XSSFCellStyle pDSCurrencyFontBoldCellStyle = ExcelUtils.getPDSCurrencyFontBoldCellStyle(wb);
			XSSFCellStyle pDSFontBoldAlignCenterStyle = ExcelUtils.getPDSFontBoldAlignCenterStyle(wb);
			XSSFCellStyle pDSDateCellStyle = ExcelUtils.getPDSDateCellStyle(wb);
			XSSFCellStyle pyiDaungSuStyleLeft = ExcelUtils.getPyiDaungSuStyleLeft(wb);

			Row row;
			Cell cell;
			row = sheet.getRow(0);
			cell = row.getCell(0);

			row = sheet.getRow(1);
			cell = row.getCell(0);
			cell.setCellValue(headingString);
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

			cell = row.getCell(5);
			String reportDate = "";
			reportDate = "Date : " + Utils.getDateFormatString(new Date());
			cell.setCellValue(reportDate);
			cell.setCellStyle(pyiDaungSuStyleRight);

			int i = 5;
			int index = 0;

			for (ReportStatementDto report : dtoList) {
				// if ((null != report.getrAmt() &&
				// report.getrAmt().doubleValue() != 0) || (null !=
				// report.getcBal() && report.getcBal().doubleValue() != 0)) {

				i = i + 1;
				index = index + 1;

				row = sheet.createRow(i);
				cell = row.createCell(0);

				if (null != report.getAcCode()) {
					if (report.getColType().equals(ColType.L)) {
						cell.setCellValue(report.getAcCode());
						cell.setCellStyle(textCellStyle);
						/*
						 * } else { cell.setCellValue("");
						 * cell.setCellStyle(textCellStyle); }
						 */
						cell = row.createCell(1);
						if (null != report.getDesp()) {
							cell.setCellValue(report.getDesp());
							cell.setCellStyle(textCellStyle);
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textCellStyle);
						}

						cell = row.createCell(2);
						/*
						 * if (null != report.getAcCode() &&
						 * report.getAcCode().equals("_________________")) {
						 * cell.setCellValue("");
						 * cell.setCellStyle(currencyCellStyle); } else {
						 */
						if (null == report.getcBal() && report.getcBal().doubleValue() == 0) {
							cell.setCellValue("");
							cell.setCellStyle(defaultCellStyle);

						} else {
							// cell.setCellValue(new
							// DecimalFormat("#,##0.00").format(report.getcBal().setScale(2,
							// RoundingMode.CEILING)));
							cell.setCellValue(Double.valueOf(report.getcBal().toString()));
							cell.setCellStyle(currencyCellStyle);
						}

					}
					//

					if (report.getColType().equals(ColType.R)) {
						cell = row.createCell(3);
						if (null != report.getrAcCode()) {
							cell.setCellValue(report.getrAcCode());
							cell.setCellStyle(textCellStyle);
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textCellStyle);
						}

						cell = row.createCell(4);
						if (null != report.getrDesp()) {
							cell.setCellValue(report.getrDesp());
							cell.setCellStyle(textCellStyle);
						} else {
							cell.setCellValue("");
							cell.setCellStyle(defaultCellStyle);
						}

						cell = row.createCell(5);
						/*
						 * if (null != report.getrAcCode() &&
						 * report.getrAcCode().equals("_________________")) {
						 * cell.setCellValue("");
						 * cell.setCellStyle(currencyCellStyle); } else {
						 */
						if (null != report.getrAmt() && report.getrAmt().doubleValue() != 0) {
							cell.setCellValue(Double.valueOf(report.getrAmt().toString()));
							// cell.setCellValue(new
							// DecimalFormat("#,##0.00").format(report.getrAmt().setScale(2,
							// RoundingMode.CEILING)));
							cell.setCellStyle(currencyCellStyle);
						} else {
							cell.setCellValue("");
							cell.setCellStyle(defaultCellStyle);

						}
					}
					//
				}

				else {
					if (report.getColType().equals(ColType.L)) {
						cell.setCellValue(report.getAcCode());
						cell.setCellStyle(textCellStyle);
						/*
						 * } else { cell.setCellValue("");
						 * cell.setCellStyle(textCellStyle); }
						 */
						cell = row.createCell(1);
						if (null != report.getDesp()) {
							cell.setCellValue(report.getDesp());
							cell.setCellStyle(pDSFontBoldAlignCenterStyle);
						} else {
							cell.setCellValue("");
							cell.setCellStyle(defaultCellStyle);
						}

						cell = row.createCell(2);
						/*
						 * if (null != report.getAcCode() &&
						 * report.getAcCode().equals("_________________")) {
						 * cell.setCellValue("");
						 * cell.setCellStyle(currencyCellStyle); } else {
						 */
						if (null == report.getcBal() && report.getcBal().doubleValue() == 0) {
							cell.setCellValue("");
							cell.setCellStyle(defaultCellStyle);

						} else {
							// cell.setCellValue(new
							// DecimalFormat("#,##0.00").format(report.getcBal().setScale(2,
							// RoundingMode.CEILING)));
							cell.setCellValue(Double.valueOf(report.getcBal().toString()));
							cell.setCellStyle(pDSCurrencyFontBoldCellStyle);
						}
					}
					//
					if (report.getColType().equals(ColType.R)) {
						cell = row.createCell(3);
						if (null != report.getrAcCode()) {
							cell.setCellValue(report.getrAcCode());
							cell.setCellStyle(textCellStyle);
						} else {
							cell.setCellValue("");
							cell.setCellStyle(textCellStyle);
						}

						cell = row.createCell(4);
						if (null != report.getrDesp()) {
							cell.setCellValue(report.getrDesp());
							cell.setCellStyle(textCellStyle);
						} else {
							cell.setCellValue("");
							cell.setCellStyle(defaultCellStyle);
						}

						cell = row.createCell(5);
						/*
						 * if (null != report.getrAcCode() &&
						 * report.getrAcCode().equals("_________________")) {
						 * cell.setCellValue("");
						 * cell.setCellStyle(currencyCellStyle); } else {
						 */
						if (null != report.getrAmt() && report.getrAmt().doubleValue() != 0) {
							cell.setCellValue(Double.valueOf(report.getrAmt().toString()));
							// cell.setCellValue(new
							// DecimalFormat("#,##0.00").format(report.getrAmt().setScale(2,
							// RoundingMode.CEILING)));
							cell.setCellStyle(pDSCurrencyFontBoldCellStyle);
						} else {
							cell.setCellValue("");
							cell.setCellStyle(defaultCellStyle);
						}
						//
					}
				}

			}

			// i = i + 1;
			// sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 1));
			// wb.setPrintArea(0, 0, 5, 0, i);
			wb.write(op);
			op.flush();
			op.close();
			// }
		} catch (

		Exception e) {
			e.printStackTrace();
		}

	}

	///

	public void generateHorizontalBoth(OutputStream op, List<LeftAndRightDto> dtoList, ReportStatementDto reportStatementDto, String branchString, String currencyString,
			String headingString) {
		try {
			Sheet sheet = wb.getSheet("ReportStatementHorizontalReport");
			XSSFCellStyle defaultCellStyle = ExcelUtils.getDefaultCellStyleII(wb);
			XSSFCellStyle textCellStyle = ExcelUtils.getTextCellLeftStyleI(wb);
			XSSFCellStyle currencyCellStyleI = ExcelUtils.getCurrencyCellStyleII(wb);
			XSSFCellStyle currencyCellStyle = ExcelUtils.getCurrencyCellStyleII(wb);
			XSSFCellStyle zawgyiCellStyle = ExcelUtils.getZawgyiCellStyleRight(wb);
			XSSFCellStyle pyiDaungSuStyleRight = ExcelUtils.getPyiDaungSuStyleRight(wb);
			XSSFCellStyle pDSCurrencyFontBoldCellStyle = ExcelUtils.getPDSCurrencyFontBoldCellStyle(wb);
			XSSFCellStyle pDSFontBoldAlignLeftStyle = ExcelUtils.getPDSFontBoldAlignLeftStyle(wb);	
			XSSFCellStyle pDSFontBoldAlignCenterStyle = ExcelUtils.getPDSFontBoldAlignCenterStyle(wb);
			XSSFCellStyle pDSDateCellStyle = ExcelUtils.getPDSDateCellStyle(wb);
			XSSFCellStyle textCellStyleLeft = ExcelUtils.getTextCellLeftStyle(wb);
			XSSFCellStyle pyiDaungSuStyleLeft = ExcelUtils.getPyiDaungSuStyleLeft(wb);

			Row row;
			Cell cell;
			row = sheet.getRow(0);
			cell = row.getCell(0);

			row = sheet.getRow(1);
			cell = row.getCell(0);
			cell.setCellValue(headingString);
			// cell.setCellStyle(pDSDateCellStyle);

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

			cell = row.getCell(5);
			String reportDate = "";
			reportDate = "Date : " + Utils.getDateFormatString(new Date());
			cell.setCellValue(reportDate);
			cell.setCellStyle(pyiDaungSuStyleRight);

			int i = 5;
			int index = 0;

			for (LeftAndRightDto report : dtoList) {

				i = i + 1;
				index = index + 1;

				row = sheet.createRow(i);
				cell = row.createCell(0);

				if (null == report.getAcCode() || null == report.getrAcCode()) {
					  cell.setCellValue(report.getAcCode());
					  cell.setCellStyle(textCellStyle);
					 

					cell = row.createCell(1);
					if (null != report.getDesp()) {
						cell.setCellValue(report.getDesp());
						cell.setCellStyle(textCellStyle);
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textCellStyle);
					}

					cell = row.createCell(2);
					if (report.getcBal().toString().equals("0.0000")) {
						cell.setCellValue("");
						cell.setCellStyle(textCellStyle);
						
					} else {
						
						cell.setCellValue(Double.valueOf(report.getcBal().toString()));
						cell.setCellStyle(currencyCellStyle);
					}

					/*
					 * cell = row.createCell(2); if (null !=
					 * report.getrAcCode()) {
					 * cell.setCellValue(report.getrAcCode());
					 * cell.setCellStyle(pDSFontBoldAlignCenterStyle); } else {
					 * cell.setCellValue("");
					 * cell.setCellStyle(pDSFontBoldAlignCenterStyle); }
					 */

					cell = row.createCell(3);
					if (null != report.getrAcCode()) {
						// if (null != report.getrDesp()) {
						cell.setCellValue(report.getrAcCode());
						cell.setCellStyle(textCellStyleLeft);
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textCellStyleLeft);
					}
					// }
					
					
					cell = row.createCell(4);
					if (null != report.getrDesp()) {
						cell.setCellValue(report.getrDesp());
						cell.setCellStyle(textCellStyle);
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textCellStyle);
					}

					cell = row.createCell(5);
					if (null != report.getrDesp()) {

					/*	cell.setCellValue(Double.valueOf(report.getrCBal().toString()));
						cell.setCellStyle(currencyCellStyle);

					} 
					 else { 
						cell.setCellValue("");
					    cell.setCellStyle(textCellStyle); 
						}*/
						if (report.getrCBal().toString().equals("0.0000")) {
							cell.setCellValue("");
							cell.setCellStyle(currencyCellStyle);
							
						} else {
							
							cell.setCellValue(Double.valueOf(report.getrCBal().toString()));
							cell.setCellStyle(currencyCellStyle);
						}
					}
					else {
						cell.setCellValue("");
						cell.setCellStyle(currencyCellStyle);
					}
					
				}

				else if (null != report.getAcCode() || null != report.getrAcCode() ) {					
					  cell.setCellValue(report.getAcCode());
					  cell.setCellStyle(textCellStyle);
					  
				    cell = row.createCell(1);
					if (null != report.getDesp()) {
						cell.setCellValue(report.getDesp());
						cell.setCellStyle(textCellStyle);
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textCellStyle);
					}

					cell = row.createCell(2);
					if (report.getcBal().toString().equals ("0.0000")) {
						cell.setCellValue("");
						cell.setCellStyle(currencyCellStyle);
					} else {
						cell.setCellValue(Double.valueOf(report.getcBal().toString()));
					    cell.setCellStyle(currencyCellStyle);
					}

					/*
					 * cell = row.createCell(2); if (null !=
					 * report.getrAcCode()) {
					 * cell.setCellValue(report.getrAcCode());
					 * cell.setCellStyle(textCellStyle); } else {
					 * cell.setCellValue(""); cell.setCellStyle(textCellStyle);
					 * }
					 */

					/*
					 * cell = row.createCell(2); if (null != report.getrDesp())
					 * { cell.setCellValue(report.getrDesp());
					 * cell.setCellStyle(textCellStyleLeft); }
					 * cell.setCellValue("");
					 * cell.setCellStyle(defaultCellStyle); }
					 */
					
					cell = row.createCell(3);
					if (null != report.getrAcCode()) {
						cell.setCellValue(report.getrAcCode());
						cell.setCellStyle(textCellStyle);
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textCellStyle);
					}
					  
					cell = row.createCell(4);
					if (null != report.getrAcCode()) {
						if (null != report.getrDesp()) {
							cell.setCellValue(report.getrDesp());
							cell.setCellStyle(textCellStyleLeft);
						} else {
							cell.setCellValue("");
							cell.setCellStyle(defaultCellStyle);
						}
					} else {
						cell.setCellValue(report.getrDesp());
						cell.setCellStyle(defaultCellStyle);
					}

				
					
					cell = row.createCell(5);
					if (null != report.getrAcCode()) {

						/*cell.setCellValue(Double.valueOf(report.getrCBal().toString()));
						cell.setCellStyle(currencyCellStyle);

					} else {
						cell.setCellValue(" ");
						cell.setCellStyle(pDSCurrencyFontBoldCellStyle);
					}*/
						if (report.getrCBal().toString().equals ("0.0000")) {
							cell.setCellValue("");
							cell.setCellStyle(currencyCellStyle);
						} else {
							cell.setCellValue(Double.valueOf(report.getrCBal().toString()));
						    cell.setCellStyle(currencyCellStyle);
						}
					}
					
					else {
						cell.setCellValue("");
						cell.setCellStyle(currencyCellStyle);
					}
				}

				else {

					
					cell.setCellValue(report.getAcCode());
					cell.setCellStyle(currencyCellStyle);
					 

				    cell = row.createCell(1);
					if (null != report.getDesp()) {
						cell.setCellValue(report.getDesp());
						cell.setCellStyle(textCellStyle);
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textCellStyle);
					}

					cell = row.createCell(2);

					/*
					 * cell.setCellValue("");
					 * cell.setCellStyle(defaultCellStyle);
					 */
					if (null != report.getcBal()) {
						cell.setCellValue(Double.valueOf(report.getcBal().toString()));
						cell.setCellStyle(currencyCellStyle);
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textCellStyle);
					}

					/*
					 * cell = row.createCell(2); if (null !=
					 * report.getrAcCode()) {
					 * cell.setCellValue(report.getrAcCode());
					 * cell.setCellStyle(pDSFontBoldAlignCenterStyle); } else {
					 * cell.setCellValue("");
					 * cell.setCellStyle(pDSFontBoldAlignCenterStyle); }
					 */
					
					cell = row.createCell(3);
					if (null != report.getrAcCode()) {
						// if (null != report.getrDesp()) {
						cell.setCellValue(report.getrAcCode());
						cell.setCellStyle(textCellStyleLeft);
					} else {
						cell.setCellValue(report.getrAcCode());
						cell.setCellStyle(textCellStyle);
					}

					cell = row.createCell(4);
					if (null != report.getrAcCode()) {
						// if (null != report.getrDesp()) {
						cell.setCellValue(report.getrDesp());
						cell.setCellStyle(textCellStyleLeft);
					} else {
						cell.setCellValue(report.getrDesp());
						cell.setCellStyle(textCellStyleLeft);
					}
					// }

					cell = row.createCell(5);
					if (null != report.getrAcCode()) {

						cell.setCellValue(Double.valueOf(report.getrCBal().toString()));
						cell.setCellStyle(currencyCellStyle);

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textCellStyleLeft);
					}
				}

			}

			// i = i + 1;

			// sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 5));
			wb.setPrintArea(0, 0, 5, 0, i + 2);

			wb.write(op);
			op.flush();
			op.close();
			// }
		} catch (

		Exception e) {
			e.printStackTrace();
		}

	}

	////
	public void generateObl(OutputStream op, List<ReportStatementDto> dtoList, ReportStatementDto reportStatementDto, String branchString, String currencyString,
			String headingString) {
		try {
			Sheet sheet = wb.getSheet("ReportStatementWithOblReport");
			XSSFCellStyle defaultCellStyle = ExcelUtils.getDefaultCellStyleII(wb);
			XSSFCellStyle textCellStyle = ExcelUtils.getTextCellStyleII(wb);
			XSSFCellStyle currencyCellStyleI = ExcelUtils.getCurrencyCellStyle(wb);
			XSSFCellStyle currencyCellStyle = ExcelUtils.getCurrencyCellStyleII(wb);
			XSSFCellStyle pyiDaungSuStyleRight = ExcelUtils.getPyiDaungSuStyleRight(wb);
			XSSFCellStyle pDSCurrencyFontBoldCellStyle = ExcelUtils.getPDSCurrencyFontBoldCellStyle(wb);
			XSSFCellStyle pDSFontBoldAlignCenterStyle = ExcelUtils.getPDSFontBoldAlignCenterStyle(wb);
			XSSFCellStyle pDSDateCellStyle = ExcelUtils.getPDSDateCellStyle(wb);
			XSSFCellStyle textCellStyleLeft = ExcelUtils.getTextCellLeftStyleI(wb);
			XSSFCellStyle pyiDaungSuStyleLeft = ExcelUtils.getPyiDaungSuStyleLeft(wb);

			Row row;
			Cell cell;
			row = sheet.getRow(0);
			cell = row.getCell(0);

			row = sheet.getRow(1);
			cell = row.getCell(0);
			cell.setCellValue(headingString);

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

			cell = row.getCell(4);
			String reportDate = "";
			reportDate = "Date : " + Utils.getDateFormatString(new Date());
			cell.setCellValue(reportDate);
			cell.setCellStyle(pyiDaungSuStyleRight);

			int i = 5;
			int index = 0;

			for (ReportStatementDto report : dtoList) {
				i = i + 1;
				index = index + 1;
				row = sheet.createRow(i);
				cell = row.createCell(0);

				if (null != report.getAcCode()) {

					if (null != report.getrAcCode()) {
						cell.setCellValue(report.getrAcCode());
						cell.setCellStyle(textCellStyle);
					} else {
						cell.setCellValue(report.getAcCode());
						cell.setCellStyle(textCellStyle);
					}

					cell = row.createCell(1);
					if (null != report.getDesp()) {
						cell.setCellValue(report.getDesp());
						cell.setCellStyle(textCellStyleLeft);
					} else {
						if (null != report.getrDesp()) {
							cell.setCellValue(report.getrDesp());
							cell.setCellStyle(textCellStyleLeft);
						} else {
							cell.setCellStyle(defaultCellStyle);
							cell.setCellValue("");
						}
					}

					cell = row.createCell(2);
					if (null != report.getoBal() && report.getoBal().doubleValue() != 0) {
						cell.setCellValue(Double.valueOf(report.getoBal().toString()));
						cell.setCellStyle(currencyCellStyle);
					} else {
						cell.setCellValue("");
						cell.setCellStyle(currencyCellStyle);
					}

					cell = row.createCell(3);
					if (null != report.getAmt() && report.getAmt().doubleValue() != 0) {
						cell.setCellValue(Double.valueOf(report.getAmt().toString()));
						cell.setCellStyle(currencyCellStyle);
					} else {
						cell.setCellValue("");
						cell.setCellStyle(defaultCellStyle);
					}

					cell = row.createCell(4);
					if (null != report.getcBal() && report.getcBal().doubleValue() != 0) {
						cell.setCellValue(Double.valueOf(report.getcBal().toString()));
						cell.setCellStyle(currencyCellStyle);
					} else {
						cell.setCellValue("");
						cell.setCellStyle(defaultCellStyle);
					}
				}
			
				else {
								
					cell.setCellValue(report.getrAcCode());
					cell.setCellStyle(textCellStyle);

					cell = row.createCell(1);
					if (null != report.getDesp() ) {
						cell.setCellValue(report.getDesp());
						cell.setCellStyle(textCellStyle);
					} else {
						if (null != report.getrAcCode()) {
							cell.setCellValue(report.getrDesp());
							cell.setCellStyle(textCellStyleLeft);
						} else {
							cell.setCellValue(report.getrDesp());
							cell.setCellStyle(textCellStyle);
							
						}
					}

					cell = row.createCell(2);
					
					if (null != report.getrAcCode()) {
						if (null != report.getoBal() && report.getoBal().doubleValue() != 0) {
						cell.setCellValue(Double.valueOf(report.getoBal().toString()));
						cell.setCellStyle(currencyCellStyle);
						}
						else {
							cell.setCellValue("");
							cell.setCellStyle(currencyCellStyle);
						}
					} else {
				
					if (null != report.getoBal() && report.getoBal().doubleValue() != 0) {
						cell.setCellValue(Double.valueOf(report.getoBal().toString()));
						cell.setCellStyle(currencyCellStyle);
					} else {
						cell.setCellValue("");
						cell.setCellStyle(currencyCellStyle);
					}
					}

					cell = row.createCell(3);
					if (null != report.getrAcCode()) {
						if (null != report.getAmt() && report.getAmt().doubleValue() != 0) {
							cell.setCellValue(Double.valueOf(report.getAmt().toString()));
							cell.setCellStyle(currencyCellStyle);
						} else {
							cell.setCellValue("");
							cell.setCellStyle(defaultCellStyle);
						}
					} else {
					if (null != report.getAmt() && report.getAmt().doubleValue() != 0) {
						cell.setCellValue(Double.valueOf(report.getAmt().toString()));
						cell.setCellStyle(currencyCellStyle);
					} else {
						cell.setCellValue("");
						cell.setCellStyle(defaultCellStyle);
					}
					}

					cell = row.createCell(4);
					if (null != report.getrAcCode()) {
						if (null != report.getcBal() && report.getcBal().doubleValue() != 0) {
							cell.setCellValue(Double.valueOf(report.getcBal().toString()));
							cell.setCellStyle(currencyCellStyle);
						} else {
							cell.setCellValue("");
							cell.setCellStyle(defaultCellStyle);
						}
					} else {
					if (null != report.getcBal() && report.getcBal().doubleValue() != 0) {
						cell.setCellValue(Double.valueOf(report.getcBal().toString()));
						cell.setCellStyle(currencyCellStyle);
					} else {
						cell.setCellValue("");
						cell.setCellStyle(defaultCellStyle);
					}
					}
				}
			}
			

			wb.setPrintArea(0, 0, 4, 0, i + 2);
			wb.write(op);
			op.flush();
			op.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
