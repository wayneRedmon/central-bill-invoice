package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.centralBill.CentralBill;
import com.prairiefarms.billing.customer.Customer;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.InvoiceAppliedCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.InvoiceDiscountCells;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.InvoiceTaxCells;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.InvoiceTotalCell;
import com.prairiefarms.billing.invoice.item.Item;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ItemDetailRow {

    public static void setTableHeader(XSSFSheet xssfSheet, int rowNumber) {
        XSSFRow xssfRow = xssfSheet.createRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(0);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("ITEM");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 0, 2));

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

    public static void setItemRow(XSSFSheet xssfSheet, int rowNumber, Item item) {
        XSSFRow xssfRow = xssfSheet.getRow(rowNumber);

        if (ObjectUtils.isEmpty(xssfRow)) xssfRow = xssfSheet.createRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(0);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_LEFT_BORDERED);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(item.getIdAndName());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 0, 2));

        CellRangeAddress cellRangeAddress = new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 0, 2);
        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex(), cellRangeAddress, xssfSheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex(), cellRangeAddress, xssfSheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex(), cellRangeAddress, xssfSheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex(), cellRangeAddress, xssfSheet);

        xssfCell = xssfRow.createCell(3);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_DOUBLE);
        xssfCell.setCellType(CellType.NUMERIC);
        xssfCell.setCellValue(item.getTotalPoints());

        xssfCell = xssfRow.createCell(4);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_INTEGER);
        xssfCell.setCellType(CellType.NUMERIC);
        xssfCell.setCellValue(item.getQuantity());

        xssfCell = xssfRow.createCell(5);
        /*xssfCell.setCellStyle(
                item.isPromotion() ?
                        WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_PRICE_PROMOTION :
                        WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_PRICE
        );*/
        xssfCell.setCellType(CellType.NUMERIC);
        xssfCell.setCellValue(item.getPriceEach());

        xssfCell = xssfRow.createCell(6);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_CURRENCY);
        xssfCell.setCellType(CellType.NUMERIC);
        xssfCell.setCellValue(item.getExtension());
    }

    public static void setInvoiceTotal(XSSFSheet xssfSheet, int startingRowNumber, int endingRowNumber, Double invoiceSubtotal, CentralBill centralBill, Customer customer, Double appliedAmount) {
        int rowNumber = endingRowNumber;

        String subtotalCellReference = InvoiceSubtotalRow.set(xssfSheet, startingRowNumber, rowNumber);

        rowNumber++;

        invoiceSubtotal -= appliedAmount;

        final String appliedCellReference = InvoiceAppliedCell.set(xssfSheet, rowNumber, appliedAmount);

        rowNumber++;

        double discountedAmount = invoiceSubtotal * centralBill.fixedDiscountRate();

        invoiceSubtotal -= discountedAmount;

        final String discountCellReference = InvoiceDiscountCells.set(xssfSheet, rowNumber, centralBill.fixedDiscountRate(), discountedAmount);

        rowNumber++;

        double taxAmount = invoiceSubtotal * customer.getTaxRate();

        final String taxAmountCellReference = InvoiceTaxCells.set(xssfSheet, rowNumber, customer.getTaxRate(), taxAmount);

        rowNumber++;

        String formula = subtotalCellReference + "-" +
                appliedCellReference + "-" +
                discountCellReference + "+" +
                taxAmountCellReference;

        InvoiceTotalCell.set(xssfSheet, rowNumber, formula);
    }
}
