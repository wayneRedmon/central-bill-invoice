package com.prairiefarms.billing.document.xlsx.workbook.sheet;

import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.customer.Customer;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.*;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.InvoiceDateCell;
import com.prairiefarms.billing.invoice.Invoice;
import com.prairiefarms.billing.invoice.centralBill.CentralBillInvoice;
import com.prairiefarms.billing.invoice.centralBill.customer.CustomerInvoice;
import com.prairiefarms.billing.invoice.item.Item;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.format.DateTimeFormatter;

public class InvoiceSheet {

    private final XSSFWorkbook workbook;

    public InvoiceSheet(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public void generate(CentralBillInvoice centralBillInvoice) {
        for (CustomerInvoice customerInvoice : centralBillInvoice.getCustomerInvoices()) {
            for (Invoice invoice : customerInvoice.getInvoices()) {
                XSSFSheet sheet = workbook.cloneSheet(WorkbookEnvironment.getInstance().INVOICE_SHEET_TO_COPY);
                sheet.setDefaultRowHeightInPoints(WorkbookEnvironment.getInstance().DEFAULT_ROW_HEIGHT_IN_POINTS);
                sheet.setDisplayGridlines(false);
                sheet.setPrintGridlines(false);
                sheet.getWorkbook().setSheetName(sheet.getWorkbook().getSheetIndex(sheet), setSheetName(customerInvoice.getCustomer(), invoice));

                InvoiceDateCell.set(sheet);
                ItemDetailRow.set(sheet, centralBillInvoice.getCentralBill().getRemit().getContact().getId(), invoice.getHeader().getId(), centralBillInvoice.getCentralBill().getContact().getId(), customerInvoice.getCustomer().getContact().getId());
                RemitToRows.set(sheet, centralBillInvoice.getCentralBill().getRemit());
                BillToRows.set(sheet, centralBillInvoice.getCentralBill());
                ShipToRows.set(sheet, customerInvoice.getCustomer());
                SalesRow.set(sheet, customerInvoice.getCustomer(), invoice);

                if (invoice.getHeader().isDistributor()) {
                    DeliverToRows.set(sheet, centralBillInvoice.getCentralBill(), customerInvoice.getCustomer(), invoice);
                    DeliverToRows.set(sheet, customerInvoice.getCustomer().getContact(), invoice);
                }

                double invoiceSubtotal = 0D;

                int rowNumber = WorkbookEnvironment.getInstance().INVOICE_ROW_TO_COPY;

                for (Item item : invoice.getItems()) {
                    rowNumber++;

                    CopyRow.set(sheet, rowNumber, WorkbookEnvironment.getInstance().INVOICE_ROW_TO_COPY);
                    ItemDetailRow.set(sheet, rowNumber, item);
                    invoiceSubtotal += item.getExtension();

                    ItemDetailRow.set(
                            sheet,
                            WorkbookEnvironment.getInstance().INVOICE_ROW_TO_COPY,
                            rowNumber, invoiceSubtotal,
                            centralBillInvoice.getCentralBill(),
                            customerInvoice.getCustomer(),
                            invoice.getAppliedAmount()
                    );

                    if (invoice.getHeader().isDistributor()) {
                        DeliveryItemDetailRows.set(sheet, rowNumber, item);
                    }
                }

                sheet.protectSheet(Environment.getInstance().getXlsxDocumentPassword());
            }
        }
    }

    private String setSheetName(Customer customer, Invoice invoice) {
        String sheetName = "Invoice #" +
                (customer.isExtendedInvoiceId() ? invoice.getHeader().getExtendedId() : invoice.getHeader().getId()) +
                "_" +
                invoice.getDeliveryDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        char suffix = 'A';

        for (int index = 0; index < workbook.getNumberOfSheets(); index++) {
            if (workbook.getSheetName(index).trim().equalsIgnoreCase(sheetName)) {
                sheetName += sheetName.trim() + " (" + suffix + ")";
                suffix += 1;
                index = 0;
            }
        }

        return sheetName.trim();
    }
}
