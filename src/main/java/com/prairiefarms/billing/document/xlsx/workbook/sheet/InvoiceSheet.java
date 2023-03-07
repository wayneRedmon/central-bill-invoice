package com.prairiefarms.billing.document.xlsx.workbook.sheet;

import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.*;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.centralBill.ShipToAccountCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.InvoiceDateCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.InvoiceNumberCell;
import com.prairiefarms.billing.invoice.Invoice;
import com.prairiefarms.billing.invoice.centralBill.CentralBillInvoice;
import com.prairiefarms.billing.invoice.centralBill.customer.CustomerInvoice;
import com.prairiefarms.billing.invoice.item.Item;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class InvoiceSheet {

    private final XSSFWorkbook xssfWorkbook;

    public InvoiceSheet(XSSFWorkbook xssfWorkbook) {
        this.xssfWorkbook = xssfWorkbook;
    }

    public void generate(CentralBillInvoice centralBillInvoice) {
        for (CustomerInvoice customerInvoice : centralBillInvoice.getCustomerInvoices()) {
            for (Invoice invoice : customerInvoice.getInvoices()) {
                XSSFSheet xssfSheet = xssfWorkbook.cloneSheet(
                        WorkbookEnvironment.getInstance().INVOICE_SHEET_TO_COPY,
                        getSheetName(customerInvoice.getCustomer().isExtendedInvoiceId() ? invoice.getHeader().getExtendedId() : String.valueOf(invoice.getHeader().getId()))
                );

                xssfSheet.setDefaultRowHeightInPoints(WorkbookEnvironment.getInstance().DEFAULT_ROW_HEIGHT_IN_POINTS);
                xssfSheet.setDisplayGridlines(false);
                xssfSheet.setPrintGridlines(false);

                RemitToRows.set(xssfSheet, centralBillInvoice.getCentralBill().getRemit());
                InvoiceDateCell.set(xssfSheet);
                InvoiceNumberCell.set(xssfSheet, invoice.getHeader().getId());
                ShipToAccountCell.set(
                        xssfSheet,
                        Environment.getInstance().getDairyId(),
                        centralBillInvoice.getCentralBill().getContact().getId(),
                        customerInvoice.getCustomer().getContact().getId()
                );
                BillToRows.set(xssfSheet, centralBillInvoice.getCentralBill().getContact());
                ShipToRows.set(xssfSheet, customerInvoice.getCustomer());
                SalesRow.set(xssfSheet, customerInvoice.getCustomer(), invoice);

                if (invoice.getHeader().isDistributor()) {
                    DeliverToRows.set(xssfSheet, centralBillInvoice.getCentralBill(), customerInvoice.getCustomer(), invoice);
                    DeliverToRows.set(xssfSheet, customerInvoice.getCustomer().getContact(), invoice);
                }

                double invoiceSubtotal = 0D;

                int rowNumber = WorkbookEnvironment.getInstance().INVOICE_ROW_TO_COPY;

                for (Item item : invoice.getItems()) {
                    rowNumber++;

                    CopyRow.set(xssfSheet, rowNumber, WorkbookEnvironment.getInstance().INVOICE_ROW_TO_COPY);
                    ItemDetailRow.set(xssfSheet, rowNumber, item);
                    invoiceSubtotal += item.getExtension();

                    ItemDetailRow.set(
                            xssfSheet,
                            WorkbookEnvironment.getInstance().INVOICE_ROW_TO_COPY,
                            rowNumber, invoiceSubtotal,
                            centralBillInvoice.getCentralBill(),
                            customerInvoice.getCustomer(),
                            invoice.getAppliedAmount()
                    );

                    if (invoice.getHeader().isDistributor()) {
                        DeliveryItemDetailRows.set(xssfSheet, rowNumber, item);
                    }
                }

                xssfSheet.protectSheet(Environment.getInstance().getXlsxDocumentPassword());
            }
        }
    }

    private String getSheetName(String invoiceIdAsText) {
        String sheetName = "Invoice #" + invoiceIdAsText;

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
