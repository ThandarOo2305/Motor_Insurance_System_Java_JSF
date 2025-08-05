package org.ace.accounting.excelUpload;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExporter {
	
	public static void exportToExcel(List<Proposal> proList, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Column 1 Header");
            headerRow.createCell(1).setCellValue("Column 2 Header");
            // Add more header cells as needed

            // Populate data rows
            int rowNum = 1;
            for (Proposal proposal : proList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(proposal.getGroupName());
                row.createCell(1).setCellValue(proposal.getInsuredName());
                row.createCell(2).setCellValue(proposal.getBank());
                row.createCell(3).setCellValue(proposal.getAddress());
                row.createCell(4).setCellValue(proposal.getPolicyNo());
                row.createCell(5).setCellValue(proposal.getSumInsured());
                row.createCell(6).setCellValue(proposal.getRate());
                row.createCell(7).setCellValue(proposal.getPremium());
                row.createCell(8).setCellValue(proposal.getStartDate());
                row.createCell(9).setCellValue(proposal.getEndDate());
                row.createCell(10).setCellValue(proposal.getRemark());
                // Add more cells as needed
            }

            // Write the workbook to the file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }


}
