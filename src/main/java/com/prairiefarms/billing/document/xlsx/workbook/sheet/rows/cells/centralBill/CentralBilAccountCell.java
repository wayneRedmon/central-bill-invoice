package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.centralBill;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class CentralBilAccountCell {

    public static void set(XSSFSheet sheet, int remitId, int customerId) {
        XSSFCell cell = sheet.getRow(2).getCell(6);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalCenterBold());
        cell.setCellValue(String.format("%03d", remitId) + "-" + String.format("%03d", customerId));
    }
}
