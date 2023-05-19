package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.customer.Customer;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.invoice.Invoice;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.time.format.DateTimeFormatter;

public class SalesRows {

    public static void set(XSSFSheet xssfSheet, Customer customer, Invoice invoice) {
        XSSFRow xssfRow = xssfSheet.createRow(11);

        XSSFCell xssfCell = xssfRow.createCell(0);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("PURCHASE ORDER");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 0, 1));

        xssfCell = xssfRow.createCell(2);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("SALESPERSON");

        xssfCell = xssfRow.createCell(3);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("DELIVERED");

        xssfCell = xssfRow.createCell(4);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("TERMS");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 4, 5));

        xssfCell = xssfRow.createCell(6);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("TOTAL DUE BY");

        xssfRow = xssfSheet.createRow(12);

        xssfCell = xssfRow.createCell(0);
        xssfCell.setCellType(CellType.STRING);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_CENTER_BORDERED);
        xssfCell.setCellValue(invoice.getHeader().getPurchaseOrder());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 0, 1));

        CellRangeAddress cellRangeAddress = new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 0, 1);
        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex(), cellRangeAddress, xssfSheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex(), cellRangeAddress, xssfSheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex(), cellRangeAddress, xssfSheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex(), cellRangeAddress, xssfSheet);

        xssfCell = xssfRow.createCell(2);
        xssfCell.setCellType(CellType.STRING);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_CENTER_BORDERED);
        xssfCell.setCellValue(StringUtils.normalizeSpace(customer.getSalesperson()));

        xssfCell = xssfRow.createCell(3);
        xssfCell.setCellType(CellType.STRING);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_CENTER_BORDERED);
        xssfCell.setCellValue(invoice.getDeliveryDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        xssfCell = xssfRow.createCell(4);
        xssfCell.setCellType(CellType.STRING);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_CENTER_BORDERED);
        xssfCell.setCellValue(customer.getTerms().getName());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 4, 5));

        cellRangeAddress = new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 4, 5);
        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex(), cellRangeAddress, xssfSheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex(), cellRangeAddress, xssfSheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex(), cellRangeAddress, xssfSheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex(), cellRangeAddress, xssfSheet);

        xssfCell = xssfRow.createCell(6);
        xssfCell.setCellType(CellType.STRING);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_CENTER_BORDERED);
        xssfCell.setCellValue(customer.getDueByDate(invoice.getHeader().getDeliveryDate()).format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    }
}
