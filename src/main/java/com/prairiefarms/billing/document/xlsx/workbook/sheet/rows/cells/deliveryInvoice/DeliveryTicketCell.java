package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.deliveryInvoice;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DeliveryTicketCell {

    public static void set(XSSFSheet xssfSheet, int invoiceId) {
        XSSFRow xssfRow = xssfSheet.getRow(2);

        XSSFCell xssfCell = xssfRow.createCell(13);
        xssfCell.setCellType(CellType.STRING);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_BLUE);
        xssfCell.setCellValue("Ticket");

        xssfCell = xssfRow.createCell(14);
        xssfCell.setCellType(CellType.NUMERIC);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED);
        xssfCell.setCellValue(invoiceId);
    }
}
