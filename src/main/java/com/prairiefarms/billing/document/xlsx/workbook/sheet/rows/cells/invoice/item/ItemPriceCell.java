package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ItemPriceCell {

    public static void set(XSSFSheet sheet, int rowNumber, int columnNumber, Boolean isPromotion, Double price) {
        XSSFCell cell = sheet.getRow(rowNumber).getCell(columnNumber);
        cell.setCellStyle(isPromotion ? WorkbookEnvironment.getInstance().getMonospacePromotionStyle() : WorkbookEnvironment.getInstance().getMonospacePriceStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(price);
    }
}
