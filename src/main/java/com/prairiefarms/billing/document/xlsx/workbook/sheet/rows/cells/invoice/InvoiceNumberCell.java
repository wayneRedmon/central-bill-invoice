package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class InvoiceNumberCell {

    public static void set(XSSFSheet sheet, Integer invoiceNumber) {
        XSSFCell cell = sheet.getRow(2).getCell(6);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalCenterBackground());
        cell.setCellValue(String.valueOf(invoiceNumber));
    }
}
