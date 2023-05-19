package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.invoice.item.Item;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;
import java.util.List;

public class ItemSummaryRow {

    private static final List<String> pointsSubtotalCellReferences = new ArrayList<>();
    private static final List<String> quantitySubtotalCellReferences = new ArrayList<>();
    private static final List<String> amountDueSubtotalCellReferences = new ArrayList<>();

    public static void setTableHeader(XSSFSheet xssfSheet, int rowNumber) {
        XSSFRow xssfRow = xssfSheet.createRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(0);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("ITEM");

        xssfCell = xssfRow.createCell(1);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("DESCRIPTION");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 1, 2));

        xssfCell = xssfRow.createCell(3);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("POINTS");

        xssfCell = xssfRow.createCell(4);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("QUANTITY");

        xssfCell = xssfRow.createCell(5);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("PRICE EA");

        xssfCell = xssfRow.createCell(6);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("TOTAL");
    }

    public static void setItemDetail(XSSFSheet xssfSheet, int rowNumber, Item item) {
        XSSFRow xssfRow = xssfSheet.createRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(0);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_LEFT_BORDERED);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(item.getIdAndName());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 0, 2));

        CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNumber, rowNumber, 0, 2);
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, xssfSheet);

        xssfCell = xssfRow.createCell(3);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_DOUBLE);
        xssfCell.setCellType(CellType.NUMERIC);
        xssfCell.setCellValue(item.getTotalPoints());
        pointsSubtotalCellReferences.add(xssfCell.getReference());

        xssfCell = xssfRow.createCell(4);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_INTEGER);
        xssfCell.setCellType(CellType.NUMERIC);
        xssfCell.setCellValue(item.getQuantity());
        quantitySubtotalCellReferences.add(xssfCell.getReference());

        xssfCell = xssfRow.createCell(5);
        /*xssfCell.setCellStyle(item.isPromotion() ?
                WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_PRICE_PROMOTION :
                WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_PRICE);
        xssfCell.setCellType(CellType.NUMERIC);*/
        xssfCell.setCellValue(item.getPriceEach());

        xssfCell = xssfRow.createCell(6);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_CURRENCY);
        xssfCell.setCellType(CellType.NUMERIC);
        xssfCell.setCellValue(item.getExtension());
        amountDueSubtotalCellReferences.add(xssfCell.getReference());
    }

    public static void setTotal(XSSFSheet xssfSheet, int rowNumber) {
        XSSFRow xssfRow = xssfSheet.createRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(2);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("TOTALS");

        xssfCell = xssfRow.createCell(3);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE);
        xssfCell.setCellFormula("SUM(" + pointsSubtotalCellReferences.get(0) + ":" + pointsSubtotalCellReferences.get(pointsSubtotalCellReferences.size() - 1) + ")");

        xssfCell = xssfRow.createCell(4);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_INTEGER);
        xssfCell.setCellFormula("SUM(" + quantitySubtotalCellReferences.get(0) + ":" + quantitySubtotalCellReferences.get(pointsSubtotalCellReferences.size() - 1) + ")");

        xssfCell = xssfRow.createCell(6);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_CURRENCY);
        xssfCell.setCellFormula("SUM(" + amountDueSubtotalCellReferences.get(0) + ":" + amountDueSubtotalCellReferences.get(pointsSubtotalCellReferences.size() - 1) + ")");
    }
}
