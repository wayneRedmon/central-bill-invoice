package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.centralBill.CentralBill;
import com.prairiefarms.billing.customer.Customer;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.InvoiceAppliedCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.InvoiceDiscountCells;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.InvoiceTaxCells;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.InvoiceTotalCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item.totals.ItemTotalExtensionCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item.totals.ItemTotalPointsCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item.totals.ItemTotalQuantityCell;
import com.prairiefarms.billing.invoice.item.Item;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ItemDetailRow {

    public static void set(XSSFSheet xssfSheet, int rowNumber, Item item) {
        XSSFCell cell = xssfSheet.getRow(rowNumber).getCell(0);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getItemIdStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(item.getSalesType().equals("A") ? String.valueOf(item.getId()) : item.getIdAsAccount());

        cell = xssfSheet.getRow(rowNumber).getCell(1);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getItemNameStyle());
        cell.setCellType(CellType.STRING);
        cell.setCellValue(item.getName());

        CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNumber, rowNumber, 1, 2);
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, xssfSheet);

        cell = xssfSheet.getRow(rowNumber).getCell(3);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getPointsColumnStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(item.getTotalPoints());

        cell = xssfSheet.getRow(rowNumber).getCell(4);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getQuantityColumnStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(item.getQuantity());

        cell = xssfSheet.getRow(rowNumber).getCell(5);
        cell.setCellStyle(item.isPromotion() ? WorkbookEnvironment.getInstance().getPromotionPriceStyle() : WorkbookEnvironment.getInstance().getPriceEachStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(item.getPriceEach());

        cell = xssfSheet.getRow(rowNumber).getCell(6);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getTotalColumnStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(item.getExtension());
    }

    public static void set(XSSFSheet sheet, int startingRowNumber, int endingRowNumber, Double invoiceSubtotal, CentralBill centralBill, Customer customer, Double appliedAmount) {
        ItemTotalPointsCell.set(sheet, startingRowNumber, endingRowNumber);
        ItemTotalQuantityCell.set(sheet, startingRowNumber, endingRowNumber);
        ItemTotalExtensionCell.set(sheet, startingRowNumber, endingRowNumber);

        int rowNumber = endingRowNumber + 2;
        String formula = "G" + rowNumber;

        if (appliedAmount > 0d) {
            invoiceSubtotal -= appliedAmount;
            InvoiceAppliedCell.set(sheet, rowNumber, appliedAmount);
            formula = formula.trim() + " - G" + rowNumber;
        } else {
            InvoiceAppliedCell.set(sheet, rowNumber);
        }

        rowNumber++;
        if (centralBill.fixedDiscountRate() > 0d) {
            invoiceSubtotal -= (invoiceSubtotal * centralBill.fixedDiscountRate());
            InvoiceDiscountCells.set(sheet, rowNumber, centralBill.fixedDiscountRate(), invoiceSubtotal);
            formula = formula.trim() + " - G" + rowNumber;
        } else {
            InvoiceDiscountCells.set(sheet, rowNumber);
        }

        rowNumber++;
        if (customer.getTaxRate() > 0D) {
            invoiceSubtotal -= (invoiceSubtotal * customer.getTaxRate());
            InvoiceTaxCells.set(sheet, rowNumber, customer.getTaxRate(), invoiceSubtotal);
            formula = formula.trim() + " + G" + rowNumber;
        } else {
            InvoiceTaxCells.set(sheet, rowNumber);
        }

        rowNumber++;
        InvoiceTotalCell.set(sheet, rowNumber, formula);
    }
}
