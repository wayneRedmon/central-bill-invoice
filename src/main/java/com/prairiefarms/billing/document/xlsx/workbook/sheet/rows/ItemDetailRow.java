package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.centralBill.CentralBill;
import com.prairiefarms.billing.customer.Customer;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.centralBill.CustomerAccountCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.*;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item.*;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item.totals.ItemTotalExtensionCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item.totals.ItemTotalPointsCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item.totals.ItemTotalQuantityCell;
import com.prairiefarms.billing.invoice.item.Item;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ItemDetailRow {

    public static void set(XSSFSheet sheet, int remitId, int invoiceId, int centralBillId, int customerId) {
        InvoiceDateCell.set(sheet);
        InvoiceNumberCell.set(sheet, invoiceId);
        CustomerAccountCell.set(sheet, remitId, centralBillId, customerId);
    }

    public static void set(XSSFSheet sheet, int rowNumber, Item item) {
        for (int column = 0; column < sheet.getRow(rowNumber).getLastCellNum(); column++) {
            switch (column) {
                case 0:
                    ItemNumberCell.set(sheet, rowNumber, column, item.getId());
                    break;
                case 1:
                    ItemDescriptionCell.set(sheet, rowNumber, column, item.getName());
                    break;
                case 3:
                    ItemPointsCell.set(sheet, rowNumber, column, (BigDecimal.valueOf(item.getPointsEach()).multiply(new BigDecimal(item.getQuantity()))).doubleValue());
                    break;
                case 4:
                    ItemQuantityCell.set(sheet, rowNumber, column, item.getQuantity());
                    break;
                case 5:
                    ItemPriceCell.set(sheet, rowNumber, column, item.isPromotion(), item.getPriceEach());
                    break;
                case 6:
                    ItemExtensionCell.set(sheet, rowNumber, column, item.getExtension());
                    break;
            }
        }
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
