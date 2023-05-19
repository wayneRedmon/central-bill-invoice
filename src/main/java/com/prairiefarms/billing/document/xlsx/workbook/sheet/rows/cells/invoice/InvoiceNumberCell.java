package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class InvoiceNumberCell {

    public static void set(XSSFSheet xssfSheet, Integer invoiceNumber) {
        XSSFRow xssfRow = xssfSheet.getRow(2);

        XSSFCell xssfCell = xssfRow.createCell(5);
        xssfCell.setCellType(CellType.STRING);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_BLUE);
        xssfCell.setCellValue("NUMBER");

        xssfCell = xssfRow.createCell(6);
        xssfCell.setCellType(CellType.STRING);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED);
        xssfCell.setCellValue(String.valueOf(invoiceNumber));
    }
}
