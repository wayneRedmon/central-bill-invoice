package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.deliveryInvoice;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DeliveryAccountCell {

    public static void set(XSSFSheet xssfSheet, int remitId, Integer centralBillId, Integer customerId) {
        XSSFRow xssfRow = xssfSheet.getRow(3);

        XSSFCell xssfCell = xssfRow.createCell(13);
        xssfCell.setCellType(CellType.STRING);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_BLUE);
        xssfCell.setCellValue("Account");

        xssfCell = xssfRow.createCell(14);
        xssfCell.setCellType(CellType.NUMERIC);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED);
        xssfCell.setCellValue(String.format("%03d", remitId) + "-" + String.format("%03d", centralBillId) + "-" + String.format("%05d", customerId));
    }
}
