package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.centralBill;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class CustomerAccountCell {

    public static void set(XSSFSheet sheet, int remitId, int centralBillId, int customerId) {
        XSSFCell cell = sheet.getRow(3).getCell(6);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalCenterBackground());
        cell.setCellValue(String.format("%03d", remitId) + "-" + String.format("%03d", centralBillId) + "-" + String.format("%05d", customerId));
    }
}
