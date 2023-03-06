package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.centralBill;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ShipToAccountCell {

    public static void set(XSSFSheet sheet, int dairyId, int billToId, int shipToId) {
        XSSFCell cell = sheet.getRow(3).getCell(6);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getPageInformationStyle());
        cell.setCellValue(String.format("%03d", dairyId) + "-" + String.format("%03d", billToId) + "-" + String.format("%05d", shipToId));
    }
}
