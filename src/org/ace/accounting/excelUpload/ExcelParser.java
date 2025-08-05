package org.ace.accounting.excelUpload;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelParser {
    public List<ProposalDTO> parseExcel(InputStream is) throws IOException {
        List<ProposalDTO> dataList = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming first sheet
            for (Row row : sheet) {
                ProposalDTO data = new ProposalDTO();
                data.setId(row.getCell(0).getStringCellValue());
                data.setGroupName(row.getCell(1).getStringCellValue());
                // Set other properties similarly
                dataList.add(data);
            }
        } catch (IOException  e) {
            e.printStackTrace();
            throw e;
        }
        return dataList;
    }
}
