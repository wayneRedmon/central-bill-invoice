package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class InvoiceTotalCell {

    public static void set(XSSFSheet xssfSheet, int rowNumber, String formula) {
        XSSFRow xssfRow = xssfSheet.createRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(5);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("TOTAL");

        xssfCell = xssfRow.createCell(6);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_CURRENCY);
        xssfCell.setCellFormula(formula);
    }
}
