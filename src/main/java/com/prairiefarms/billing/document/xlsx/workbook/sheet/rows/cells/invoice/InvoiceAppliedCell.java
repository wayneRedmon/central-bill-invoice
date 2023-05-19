package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class InvoiceAppliedCell {

    public static String set(XSSFSheet xssfSheet, int rowNumber, Double appliedAmount) {
        XSSFRow xssfRow = xssfSheet.createRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(5);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("APPLIED");

        xssfCell = xssfRow.createCell(6);
        xssfCell.setCellType(CellType.NUMERIC);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_CURRENCY);
        xssfCell.setCellValue(appliedAmount);

        return xssfCell.getReference();
    }
}
