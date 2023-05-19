package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class InvoiceSubtotalRow {

    public static String set(XSSFSheet xssfSheet, int startingRowNumber, int endingRowNumber) {
        XSSFRow xssfRow = xssfSheet.createRow(endingRowNumber);

        XSSFCell xssfCell = xssfRow.createCell(1);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("TOTAL POINTS & QUANTITY");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 1, 2));

        xssfCell = xssfRow.createCell(3);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE);
        xssfCell.setCellFormula("SUM(D" + (startingRowNumber + 1) + ":D" + (endingRowNumber) + ")");

        xssfCell = xssfRow.createCell(4);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_INTEGER);
        xssfCell.setCellFormula("SUM(E" + (startingRowNumber + 1) + ":E" + (endingRowNumber) + ")");

        xssfCell = xssfRow.createCell(5);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("SUBTOTAL");

        xssfCell = xssfRow.createCell(6);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_CURRENCY);
        xssfCell.setCellFormula("SUM(G" + (startingRowNumber + 1) + ":G" + (endingRowNumber) + ")");

        return xssfCell.getReference();
    }
}
