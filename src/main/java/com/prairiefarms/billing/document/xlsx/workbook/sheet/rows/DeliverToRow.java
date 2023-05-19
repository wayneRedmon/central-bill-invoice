package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.utils.Contact;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DeliverToRow {

    public static void set(XSSFSheet xssfSheet, Contact contact) {
        XSSFRow xssfRow = xssfSheet.getRow(6);

        XSSFCell xssfCell = xssfRow.createCell(9);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_LEFT_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("DELIVER TO");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 9, 11));

        CellRangeAddress cellRangeAddress = new CellRangeAddress(6, 6, 9, 11);
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, xssfSheet);

        xssfRow = xssfSheet.getRow(7);

        xssfCell = xssfRow.createCell(9);
        xssfCell.setCellType(CellType.STRING);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_ALIGN_LEFT);
        xssfCell.setCellValue(contact.getName());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 9, 11));

        xssfRow = xssfSheet.getRow(8);

        xssfCell = xssfRow.createCell(9);
        xssfCell.setCellType(CellType.STRING);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_ALIGN_LEFT);
        xssfCell.setCellValue(contact.getStreet());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 9, 11));

        xssfRow = xssfSheet.getRow(9);

        xssfCell = xssfRow.createCell(9);
        xssfCell.setCellType(CellType.STRING);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_ALIGN_LEFT);
        xssfCell.setCellValue(contact.getAddress());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 9, 11));
    }
}
