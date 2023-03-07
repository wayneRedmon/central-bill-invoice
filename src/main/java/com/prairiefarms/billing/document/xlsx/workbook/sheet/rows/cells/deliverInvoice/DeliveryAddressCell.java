package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.deliverInvoice;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DeliveryAddressCell {

    public static void set(XSSFSheet sheet, String address) {
        XSSFCell cell = sheet.getRow(10).getCell(9);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalDefaultStyle());
        cell.setCellValue(address);
    }
}
