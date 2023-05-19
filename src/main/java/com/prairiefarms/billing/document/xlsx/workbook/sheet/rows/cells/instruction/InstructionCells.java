package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.instruction;

import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class InstructionCells {

    public static void set(XSSFSheet xssfSheet) {
        final int columnStart = 4;
        final int columnEnd = 6;

        int rowNumber = 6;

        XSSFRow xssfRow = xssfSheet.getRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(columnStart);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("INSTRUCTIONS");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), columnStart, columnEnd));

        rowNumber++;
        xssfRow = xssfSheet.getRow(rowNumber);

        xssfCell = xssfRow.createCell(columnStart);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("Make checks payable to: " + Environment.getInstance().getCorporateName());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), columnStart, columnEnd));

        rowNumber++;
        xssfRow = xssfSheet.getRow(rowNumber);

        xssfCell = xssfRow.createCell(columnStart);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("Please return this page with your payment.");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), columnStart, columnEnd));

        rowNumber++;
        xssfRow = xssfSheet.getRow(rowNumber);

        xssfCell = xssfRow.createCell(columnStart);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("Thank you for your business!");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), columnStart, columnEnd));
    }
}
