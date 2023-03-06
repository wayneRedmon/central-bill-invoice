package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.invoice.item.Item;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DeliveryItemDetailRows {

    public static void set(XSSFSheet xssfSheet, int rowNumber, Item item) {
        XSSFCell cell = xssfSheet.getRow(rowNumber).getCell(9);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getItemIdStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(item.getSalesType().equals("A'") ? String.valueOf(item.getId()) : item.getIdAsAccount());

        cell = xssfSheet.getRow(rowNumber).getCell(10);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getItemNameStyle());
        cell.setCellType(CellType.STRING);
        cell.setCellValue(item.getName());

        CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNumber, rowNumber, 10, 11);
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, xssfSheet);

        cell = xssfSheet.getRow(rowNumber).getCell(12);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getPointsColumnStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(item.getTotalPoints());

        cell = xssfSheet.getRow(rowNumber).getCell(13);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getQuantityColumnStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(item.getQuantity());
    }

    public static void set(XSSFSheet sheet, int startingRowNumber, int endingRowNumber) {
        XSSFCell cell = sheet.getRow(endingRowNumber + 1).getCell(13);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceTotalDoubleStyle());
        cell.setCellFormula("SUM(M" + (startingRowNumber + 2) + ":M" + (endingRowNumber + 1) + ")");

        cell = sheet.getRow(endingRowNumber + 1).getCell(14);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceTotalIntegerStyle());
        cell.setCellFormula("SUM(N" + (startingRowNumber + 2) + ":N" + (endingRowNumber + 1) + ")");
    }
}
