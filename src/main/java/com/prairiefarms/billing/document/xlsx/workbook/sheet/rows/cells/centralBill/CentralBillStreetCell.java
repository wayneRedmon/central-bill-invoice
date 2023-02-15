package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.centralBill;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class CentralBillStreetCell {

    public static void set(XSSFSheet sheet, String street) {
        XSSFCell cell = sheet.getRow(9).getCell(0);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalDefaultStyle());
        cell.setCellType(CellType.STRING);
        cell.setCellValue(street);
    }
}
