package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.deliverInvoice;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DeliveryTicketCell {

    public static void set(XSSFSheet sheet, int invoiceId) {
        XSSFCell cell = sheet.getRow(2).getCell(14);
        cell.setCellType(CellType.NUMERIC);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalCenterBackground());
        cell.setCellValue(invoiceId);
    }
}
