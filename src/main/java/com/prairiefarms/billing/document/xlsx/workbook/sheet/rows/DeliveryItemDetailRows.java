package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.invoice.item.Item;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DeliveryItemDetailRows {

    public static void setTableHeader(XSSFSheet xssfSheet, int rowNumber) {
        XSSFRow xssfRow = xssfSheet.getRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(9);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("ITEM");

        xssfCell = xssfRow.createCell(10);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("DESCRIPTION");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 10, 11));

        xssfCell = xssfRow.createCell(12);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("POINTS");

        xssfCell = xssfRow.createCell(13);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("QUANTITY");

        CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNumber, rowNumber, 9, 13);
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, xssfSheet);
    }

    public static void setItemRow(XSSFSheet xssfSheet, int rowNumber, Item item) {
        XSSFRow xssfRow = xssfSheet.getRow(rowNumber);

        if (ObjectUtils.isEmpty(xssfRow)) xssfRow = xssfSheet.createRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(9);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_LEFT_BORDERED);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(item.getIdAndName());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 9, 11));

        CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNumber, rowNumber, 9, 11);
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, xssfSheet);

        xssfCell = xssfRow.createCell(12);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_CURRENCY);
        xssfCell.setCellType(CellType.NUMERIC);
        xssfCell.setCellValue(item.getTotalPoints());

        xssfCell = xssfRow.createCell(13);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED);
        xssfCell.setCellType(CellType.NUMERIC);
        xssfCell.setCellValue(item.getQuantity());
    }

    public static void setTotalRow(XSSFSheet xssfSheet, int startingRowNumber, int endingRowNumber) {
        XSSFRow xssfRow = xssfSheet.getRow(endingRowNumber + 1);

        XSSFCell xssfCell = xssfRow.createCell(13);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_DOUBLE);
        xssfCell.setCellFormula("SUM(M" + (startingRowNumber + 2) + ":M" + (endingRowNumber + 1) + ")");

        xssfCell = xssfRow.createCell(14);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_INTEGER);
        xssfCell.setCellFormula("SUM(N" + (startingRowNumber + 2) + ":N" + (endingRowNumber + 1) + ")");
    }
}
