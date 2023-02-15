package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.deliverInvoice.deliveryItem;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DeliveryItemQuantityCell {

    public static void set(XSSFSheet sheet, int rowNumber, int columnNumber, Integer quantity) {
        XSSFCell cell = sheet.getRow(rowNumber).getCell(columnNumber);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceIntegerStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(quantity);
    }
}