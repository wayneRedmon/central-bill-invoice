package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.customer.Customer;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ShipToRows {

    public static void set(XSSFSheet xssfSheet, Customer customer) {
        XSSFRow xssfRow = xssfSheet.getRow(6);

        XSSFCell xssfCell = xssfRow.createCell(4);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_LEFT_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("SHIP TO");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 4, 6));

        xssfRow = xssfSheet.getRow(7);

        xssfCell = xssfRow.createCell(4);
        xssfCell.setCellType(CellType.STRING);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_ALIGN_LEFT);
        xssfCell.setCellValue(customer.getContact().getName());

        xssfRow = xssfSheet.getRow(8);

        xssfCell = xssfRow.createCell(4);
        xssfCell.setCellType(CellType.STRING);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_ALIGN_LEFT);
        xssfCell.setCellValue(customer.getContact().getStreet());

        xssfRow = xssfSheet.getRow(9);

        xssfCell = xssfRow.createCell(4);
        xssfCell.setCellType(CellType.STRING);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_ALIGN_LEFT);
        xssfCell.setCellValue(customer.getContact().getAddress());
    }
}
