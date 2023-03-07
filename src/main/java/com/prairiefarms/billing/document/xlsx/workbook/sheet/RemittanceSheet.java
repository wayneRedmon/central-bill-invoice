package com.prairiefarms.billing.document.xlsx.workbook.sheet;

import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.BillToRows;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.CopyRow;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.RemitToRows;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.RemittanceSummaryRow;
import com.prairiefarms.billing.invoice.Invoice;
import com.prairiefarms.billing.invoice.centralBill.CentralBillInvoice;
import com.prairiefarms.billing.invoice.centralBill.customer.CustomerInvoice;
import com.prairiefarms.billing.invoice.item.Item;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class RemittanceSheet {

    private final XSSFWorkbook xssfWorkbook;

    public RemittanceSheet(XSSFWorkbook xssfWorkbook) {
        this.xssfWorkbook = xssfWorkbook;
    }

    public void generate(CentralBillInvoice centralBillInvoice) {
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(WorkbookEnvironment.getInstance().REMITTANCE_SHEET_TO_COPY);
        xssfSheet.setDefaultRowHeightInPoints(WorkbookEnvironment.getInstance().DEFAULT_ROW_HEIGHT_IN_POINTS);
        xssfSheet.setDisplayGridlines(false);
        xssfSheet.setPrintGridlines(false);

        RemitToRows.set(xssfSheet, centralBillInvoice.getCentralBill().getRemit());
        BillToRows.set(xssfSheet, centralBillInvoice.getCentralBill().getContact());
        RemittanceSummaryRow remittanceSummaryRow = new RemittanceSummaryRow(xssfSheet);

        int rowNumber = 12;
        int customerId = 0;
        int startingCanonicalRow = 0;
        int endingCanonicalRow = 0;

        remittanceSummaryRow.set(centralBillInvoice.getCentralBill().getContact().getId());

        for (CustomerInvoice customerInvoice : centralBillInvoice.getCustomerInvoices()) {
            for (Invoice invoice : customerInvoice.getInvoices()) {
                double points = 0d;
                int quantity = 0;
                double subTotal = 0d;

                for (Item item : invoice.getItems()) {
                    points += item.getTotalPoints();
                    quantity += item.getQuantity();
                    subTotal += item.getExtension();
                }

                rowNumber++;
                if (startingCanonicalRow == 0) startingCanonicalRow = rowNumber + 1;
                endingCanonicalRow = rowNumber + 1;

                if (customerId != customerInvoice.getCustomer().getContact().getId()) {
                    CopyRow.set(xssfSheet, rowNumber, WorkbookEnvironment.getInstance().REMITTANCE_ROW_TO_COPY);
                    remittanceSummaryRow.setCustomerColumn(rowNumber, customerInvoice.getCustomer().getContact());

                    customerId = customerInvoice.getCustomer().getContact().getId();
                } else {
                    CopyRow.set(xssfSheet, rowNumber, WorkbookEnvironment.getInstance().REMITTANCE_ROW_TO_COPY);
                }

                remittanceSummaryRow.set(
                        rowNumber,
                        invoice.getDeliveryDate(),
                        customerInvoice.getCustomer().getDueByDate(invoice.getHeader().getDeliveryDate()),
                        invoice.getHeader().getId(),
                        points,
                        quantity,
                        subTotal
                );
            }

            rowNumber++;
            CopyRow.set(xssfSheet, rowNumber, WorkbookEnvironment.getInstance().REMITTANCE_ROW_TO_COPY);
            remittanceSummaryRow.setCustomerTotal(rowNumber, startingCanonicalRow, endingCanonicalRow);
        }

        rowNumber++;
        remittanceSummaryRow.setTotal(rowNumber);

        xssfSheet.protectSheet(Environment.getInstance().getXlsxDocumentPassword());
    }
}
