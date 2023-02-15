package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.customerRemittance;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class InvoiceDueByCell {

    public static void set(XSSFSheet sheet, int rowNumber, int columnNumber, int dueByDays) {
        XSSFCell cell = sheet.getRow(rowNumber).getCell(columnNumber);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceCenterStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(""); //:todo add due by...
    }
}
