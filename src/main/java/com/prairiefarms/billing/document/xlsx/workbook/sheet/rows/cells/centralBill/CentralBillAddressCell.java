package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.centralBill;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class CentralBillAddressCell {

    public static void set(XSSFSheet sheet, String address) {
        XSSFCell cell = sheet.getRow(10).getCell(0);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalDefaultStyle());
        cell.setCellType(CellType.STRING);
        cell.setCellValue(address);
    }
}
