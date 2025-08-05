package org.ace.accounting.excelUpload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.ace.accounting.web.common.ExcelUtils;
import org.ace.java.component.ErrorCode;
import org.ace.java.component.SystemException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ProposalReportExcel {

	private XSSFWorkbook wb;
	
	
	public ProposalReportExcel( ) {
		load();
	}


	private void load() {
		try {
			InputStream inp = this.getClass().getResourceAsStream("/report-template/PROPOSAL/PROPOSAL_REPORT.xlsx");
			System.out.println("inp"+inp);
			if (inp == null) {
			    System.out.println("=======sdfghjk========" + inp);
			}
			wb = new XSSFWorkbook(inp);
		} catch (IOException e) {
			throw new SystemException(ErrorCode.SYSTEM_ERROR, "Failed to load PROPOSAL_REPORT.xlsx template", e);
		}
	}
	
	
	public void generate(OutputStream op, List<Proposal> proposals) {
		try {
			Sheet sheet = wb.getSheet("proposal");
			
			int i = 2;
			int index = 0;
			XSSFCellStyle defaultCellStyle = ExcelUtils.getDefaultCellStyle(wb);
			XSSFCellStyle textCellStyle = ExcelUtils.getTextCellStyle(wb);
			XSSFCellStyle textCenterStyle = ExcelUtils.getAlignCenterStyle(wb);
			XSSFCellStyle dateCellStyle = ExcelUtils.getDateCellStyle(wb);
			XSSFCellStyle currencyCellStyle = ExcelUtils.getCurrencyCellStyle(wb);

			Row row = null;
			Cell cell = null;
			String strFormula;
			for (Proposal p : proposals) {
				i = i + 1;
				index = index + 1;
				row = sheet.createRow(i);
				
				cell = row.createCell(0);
				cell.setCellValue(index);
				cell.setCellStyle(defaultCellStyle);
				
				cell = row.createCell(1);
				cell.setCellValue(p.getGroupName());
				System.out.println("========1========"+p.getGroupName());
				cell.setCellStyle(textCellStyle);
				
				
				cell = row.createCell(2);
				cell.setCellValue(p.getInsuredName());
				cell.setCellStyle(textCenterStyle);
				
				cell = row.createCell(3);
				cell.setCellValue(p.getBank());
				cell.setCellStyle(textCellStyle);
				
				cell = row.createCell(4);
				cell.setCellValue(p.getAddress());
				cell.setCellStyle(textCellStyle);
				
				cell = row.createCell(5);
				cell.setCellValue(p.getPolicyNo());
				cell.setCellStyle(textCenterStyle);
				
				cell = row.createCell(6);
				cell.setCellValue(p.getSumInsured());
				cell.setCellStyle(currencyCellStyle);
				
				cell = row.createCell(7);
				cell.setCellValue(p.getRate());
				cell.setCellStyle(currencyCellStyle);
				
				cell = row.createCell(8);
				cell.setCellValue(p.getStartDate());
				cell.setCellStyle(dateCellStyle);
				
				cell = row.createCell(9);
				cell.setCellValue(p.getEndDate());
				cell.setCellStyle(dateCellStyle);
				
				cell = row.createCell(10);
				cell.setCellValue(p.getRemark());
				cell.setCellStyle(textCellStyle);
			}

					
			wb.setPrintArea(0, "$A$1:$AG$" + (i + 1));
			wb.write(op);
			System.out.println("==op==="+op);
			op.flush();
			op.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
