package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.deliverInvoice.deliveryItem;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DeliveryItemDescriptionCell {

    public static void set(XSSFSheet sheet, int rowNumber, int columnNumber, String description) {
        XSSFCell cell = sheet.getRow(rowNumber).getCell(columnNumber);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceDefaultStyle());
        cell.setCellType(CellType.STRING);
        cell.setCellValue(description);
    }
}
