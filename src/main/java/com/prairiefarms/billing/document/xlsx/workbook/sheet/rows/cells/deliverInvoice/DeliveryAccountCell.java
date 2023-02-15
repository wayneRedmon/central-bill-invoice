package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.deliverInvoice;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DeliveryAccountCell {

    public static void set(XSSFSheet sheet, int remitId, Integer centralBillId, Integer customerId) {
        XSSFCell cell = sheet.getRow(3).getCell(14);
        cell.setCellType(CellType.NUMERIC);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalCenterBackground());
        cell.setCellValue(String.format("%03d", remitId) + "-" + String.format("%03d", centralBillId) + "-" + String.format("%05d", customerId));
    }
}
