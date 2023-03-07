package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class InvoiceTaxCells {

    public static void set(XSSFSheet sheet, int rowNumber, Double taxRate, Double taxAmount) {
        XSSFCell cell = sheet.getRow(rowNumber).getCell(5);
        cell.setCellType(CellType.NUMERIC);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceDoubleStyle());
        cell.setCellValue(taxRate);
        cell = sheet.getRow(rowNumber).getCell(6);
        cell.setCellType(CellType.NUMERIC);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceDoubleStyle());
        cell.setCellValue(taxAmount);
    }

    public static void set(XSSFSheet sheet, int rowNumber) {
        XSSFCell cell = sheet.getRow(rowNumber).getCell(6);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceTextRightStyle());
        cell.setCellValue("N/A");
    }
}
