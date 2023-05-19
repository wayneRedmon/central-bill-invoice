package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.utils.Contact;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class BillToRows {

    public static void set(XSSFSheet xssfSheet, Contact contact) {
        XSSFRow xssfRow = xssfSheet.createRow(6);

        XSSFCell xssfCell = xssfRow.createCell(0);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_LEFT_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("BILL TO");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 0, 2));

        xssfRow = xssfSheet.createRow(7);

        xssfCell = xssfRow.createCell(0);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_ALIGN_LEFT);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(contact.getName());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 0, 2));

        xssfRow = xssfSheet.createRow(8);

        xssfCell = xssfRow.createCell(0);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_ALIGN_LEFT);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(contact.getStreet());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 0, 2));

        xssfRow = xssfSheet.createRow(9);

        xssfCell = xssfRow.createCell(0);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_ALIGN_LEFT);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(contact.getAddress());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 0, 2));
    }
}
