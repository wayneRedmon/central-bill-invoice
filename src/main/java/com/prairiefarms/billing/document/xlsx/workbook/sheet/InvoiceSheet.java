package com.prairiefarms.billing.document.xlsx.workbook.sheet;

import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.overlay.Logo;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.*;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.centralBill.ShipToAccountCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.deliveryInvoice.DeliveryAccountCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.deliveryInvoice.DeliveryDateCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.deliveryInvoice.DeliveryTicketCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.InvoiceDateCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.InvoiceNumberCell;
import com.prairiefarms.billing.invoice.Invoice;
import com.prairiefarms.billing.invoice.centralBill.CentralBillInvoice;
import com.prairiefarms.billing.invoice.centralBill.customer.CustomerInvoice;
import com.prairiefarms.billing.invoice.item.Item;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

public class InvoiceSheet {

    private static final String SHEET_NAME = "INVOICE";

    public static void generate(XSSFWorkbook xssfWorkbook, CentralBillInvoice centralBillInvoice) throws IOException {
        for (CustomerInvoice customerInvoice : centralBillInvoice.getCustomerInvoices()) {
            for (Invoice invoice : customerInvoice.getInvoices()) {
                String sheetName = "INVOICE " + getSheetName(
                        xssfWorkbook,
                        customerInvoice.getCustomer().isExtendedInvoiceId() ?
                                invoice.getHeader().getExtendedId() :
                                String.valueOf(invoice.getHeader().getId())
                );

                XSSFSheet xssfSheet = xssfWorkbook.createSheet(sheetName);
                xssfSheet.setDefaultRowHeightInPoints(WorkbookEnvironment.DEFAULT_ROW_HEIGHT_IN_POINTS);
                xssfSheet.setDisplayGridlines(false);
                xssfSheet.setPrintGridlines(false);

                for (int column = 0; column < 9; column++) {
                    xssfSheet.setColumnWidth(column, 15 * 256);
                }

                for (int column = 9; column < 15; column++) {
                    xssfSheet.setColumnWidth(column, 15 * 256);
                }

                Logo.stamp(xssfSheet);

                RemitToRows.set(xssfSheet, centralBillInvoice.getCentralBill().getRemit(), SHEET_NAME);

                InvoiceDateCell.set(xssfSheet, Environment.getInstance().getBillingDate());
                InvoiceNumberCell.set(xssfSheet, invoice.getHeader().getId());
                ShipToAccountCell.set(
                        xssfSheet,
                        Environment.getInstance().getDairyId(),
                        centralBillInvoice.getCentralBill().getContact().getId(),
                        customerInvoice.getCustomer().getContact().getId()
                );

                BillToRows.set(xssfSheet, centralBillInvoice.getCentralBill().getContact());
                ShipToRows.set(xssfSheet, customerInvoice.getCustomer());

                SalesRows.set(xssfSheet, customerInvoice.getCustomer(), invoice);

                int rowNumberStart = 0;
                int deliveryRowNumber = 11;
                int rowNumber = 14;

                ItemDetailRow.setTableHeader(xssfSheet, rowNumber);

                if (invoice.getHeader().isDistributor()) {
                    DeliveryDateCell.set(xssfSheet, invoice.getHeader().getDeliveryDate());
                    DeliveryTicketCell.set(xssfSheet, invoice.getHeader().getId());
                    DeliveryAccountCell.set(xssfSheet, Environment.getInstance().getDairyId(), centralBillInvoice.getCentralBill().getContact().getId(), customerInvoice.getCustomer().getContact().getId());

                    DeliverToRow.set(xssfSheet, customerInvoice.getCustomer().getContact());
                    DeliveryItemDetailRows.setTableHeader(xssfSheet, deliveryRowNumber);
                }

                double invoiceSubtotal = 0D;

                for (Item item : invoice.getItems()) {
                    rowNumber++;

                    if (rowNumberStart == 0) rowNumberStart = rowNumber;

                    ItemDetailRow.setItemRow(xssfSheet, rowNumber, item);

                    if (invoice.getHeader().isDistributor()) {
                        deliveryRowNumber++;
                        DeliveryItemDetailRows.setItemRow(xssfSheet, deliveryRowNumber, item);
                    }

                    invoiceSubtotal += item.getExtension();
                }

                rowNumber++;

                ItemDetailRow.setInvoiceTotal(
                        xssfSheet,
                        rowNumberStart,
                        rowNumber,
                        invoiceSubtotal,
                        centralBillInvoice.getCentralBill(),
                        customerInvoice.getCustomer(),
                        invoice.getAppliedAmount()
                );

/*                if (invoice.getHeader().isDistributor())
                    DeliveryItemDetailRows.setTotalRow(xssfSheet, deliveryRowNumber);
*/
                xssfSheet.protectSheet(Environment.getInstance().getXlsxDocumentPassword());
            }
        }
    }

    private static String getSheetName(XSSFWorkbook xssfWorkbook, String invoiceIdAsText) {
        String sheetName = invoiceIdAsText;

        char suffix = 'A';

        for (int index = 0; index < xssfWorkbook.getNumberOfSheets(); index++) {
            if (xssfWorkbook.getSheetName(index).trim().equalsIgnoreCase(sheetName)) {
                sheetName += sheetName.trim() + " (" + suffix + ")";
                suffix += 1;
                index = 0;
            }
        }

        return sheetName.trim();
    }
}
