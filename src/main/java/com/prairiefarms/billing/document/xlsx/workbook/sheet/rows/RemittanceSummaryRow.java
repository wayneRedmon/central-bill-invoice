package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.customer.Customer;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.centralBill.CentralBillAccountCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.customerRemittance.CustomerInvoiceCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.customerRemittance.CustomerInvoiceNameCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.customerRemittance.DeliveryDateCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.customerRemittance.InvoiceDueByCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.customerRemittance.subTotal.*;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.customerRemittance.total.CustomerExtensionTotalCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.customerRemittance.total.CustomerPointsTotalCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.customerRemittance.total.CustomerQuantityTotalCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.InvoiceDateCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item.ItemExtensionCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item.ItemPointsCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item.ItemQuantityCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.remit.RemitNameCell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.time.LocalDate;

public class RemittanceSummaryRow {

    public static void set(XSSFSheet sheet, int remitId, int customerId) {
        InvoiceDateCell.set(sheet);
        CentralBillAccountCell.set(sheet, remitId, customerId);
        RemitNameCell.set(sheet);
    }

    public static void set(XSSFSheet sheet, int rowNumber, LocalDate deliveryDate, Customer customer, int invoiceNumber, double points, int quantity, double subTotal) {
        for (int column = 0; column < sheet.getRow(rowNumber).getLastCellNum(); column++) {
            switch (column) {
                case 0:
                    CustomerInvoiceNameCell.set(sheet, rowNumber, column, customer.getContact().getId(), customer.getContact().getName());
                    break;
                case 3:
                    CustomerInvoiceCell.set(sheet, rowNumber, column, invoiceNumber);
                    break;
                case 4:
                    DeliveryDateCell.set(sheet, rowNumber, column, deliveryDate);
                    break;
                case 5:
                    ItemPointsCell.set(sheet, rowNumber, column, points);
                    break;
                case 6:
                    ItemQuantityCell.set(sheet, rowNumber, column, quantity);
                    break;
                case 7:
                    ItemExtensionCell.set(sheet, rowNumber, column, subTotal);
                    break;
                case 8:
                    InvoiceDueByCell.set(sheet, rowNumber, column, customer.getDueByDate(deliveryDate));
                    break;
            }
        }
    }

    public static void setSubtotal(XSSFSheet sheet, int startingRowNumber, int endingRowNumber) {
        for (int column = 0; column < sheet.getRow(endingRowNumber + 1).getLastCellNum(); column++) {
            switch (column) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 8:
                    CustomerProportionalEmptyCell.set(sheet, endingRowNumber + 1, column);
                    break;
                case 4:
                    CustomerSubTotalCell.set(sheet, endingRowNumber + 1, column);
                    break;
                case 5:
                    CustomerPointsSubTotalCell.set(sheet, endingRowNumber + 1, column, startingRowNumber, endingRowNumber);
                    break;
                case 6:
                    CustomerQuantitySubTotalCell.set(sheet, endingRowNumber + 1, column, startingRowNumber, endingRowNumber);
                    break;
                case 7:
                    CustomerExtensionSubTotalCell.set(sheet, endingRowNumber + 1, column, startingRowNumber, endingRowNumber);
                    break;
                default:
                    break;
            }
        }
    }

    public static void setTotal(XSSFSheet sheet, int startingRowNumber, int endingRowNumber) {
        String totalPointsFormula = "";
        String totalQuantityFormula = "";
        String totalExtensionFormula = "";

        for (int thisRow = startingRowNumber; thisRow <= endingRowNumber; thisRow++) {
            XSSFRow row = sheet.getRow(thisRow);

            for (int column = 0; column < row.getLastCellNum(); column++) {
                XSSFCell cell = row.getCell(column);

                if (cell.getCellType() == CellType.STRING) {
                    if (cell.getStringCellValue().trim().equalsIgnoreCase("Subtotal")) {
                        int totalRow = thisRow + 1;
                        totalPointsFormula = (totalPointsFormula.trim().equals("")) ? ("F" + totalRow) : (totalPointsFormula.trim() + " + F" + totalRow);
                        totalQuantityFormula = (totalQuantityFormula.trim().equals("")) ? ("G" + totalRow) : (totalQuantityFormula.trim() + " + G" + totalRow);
                        totalExtensionFormula = (totalExtensionFormula.trim().equals("")) ? ("H" + totalRow) : (totalExtensionFormula.trim() + " + H" + totalRow);

                        break;
                    }
                }
            }
        }

        endingRowNumber++;

        CustomerPointsTotalCell.set(sheet, endingRowNumber, totalPointsFormula);
        CustomerQuantityTotalCell.set(sheet, endingRowNumber, totalQuantityFormula);
        CustomerExtensionTotalCell.set(sheet, endingRowNumber, totalExtensionFormula);
    }
}
