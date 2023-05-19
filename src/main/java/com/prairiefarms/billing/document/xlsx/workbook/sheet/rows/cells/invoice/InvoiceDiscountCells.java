package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class InvoiceDiscountCells {

    public static String set(XSSFSheet xssfSheet, int rowNumber, Double discountRate, Double discountAmount) {
        XSSFRow xssfRow = xssfSheet.createRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(5);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("DISCOUNT");

        if (discountRate > 0D) {
            xssfCell = xssfRow.createCell(4);
            xssfCell.setCellType(CellType.NUMERIC);
            //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_DOUBLE);
            xssfCell.setCellValue(discountRate);
        }

        xssfCell = xssfRow.createCell(6);
        xssfCell.setCellType(CellType.NUMERIC);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_CURRENCY);
        xssfCell.setCellValue(discountAmount);

        return xssfCell.getReference();
    }
}
