package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.customerRemittance;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class CustomerInvoiceCell {

    public static void set(XSSFSheet sheet, int rowNumber, int columnNumber, Integer invoiceNumber) {
        XSSFCell cell = sheet.getRow(rowNumber).getCell(columnNumber);
        cell.setCellType(CellType.STRING);
        cell.setCellValue(invoiceNumber);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceDefaultStyle());
    }
}
