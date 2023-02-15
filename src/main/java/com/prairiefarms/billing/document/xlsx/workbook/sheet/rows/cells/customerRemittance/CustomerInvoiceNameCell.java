package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.customerRemittance;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class CustomerInvoiceNameCell {

    public static void set(XSSFSheet sheet, int rowNumber, int columnNumber, Integer customerNumber, String customerName) {
        XSSFCell cell = sheet.getRow(rowNumber).getCell(columnNumber);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceDefaultStyle());
        cell.setCellType(CellType.STRING);
        cell.setCellValue(customerNumber + "  " + customerName);
    }
}
