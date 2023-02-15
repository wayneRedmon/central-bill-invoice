package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ItemPointsCell {

    public static void set(XSSFSheet sheet, int rowNumber, int columnNumber, double points) {
        XSSFCell cell = sheet.getRow(rowNumber).getCell(columnNumber);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceDoubleStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(points);
    }
}
