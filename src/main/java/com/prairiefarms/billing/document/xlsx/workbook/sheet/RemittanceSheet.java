package com.prairiefarms.billing.document.xlsx.workbook.sheet;

import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.BillToRows;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.RemitToRows;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.RemittanceSummaryRow;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.CopyRow;
import com.prairiefarms.billing.invoice.Invoice;
import com.prairiefarms.billing.invoice.centralBill.CentralBillInvoice;
import com.prairiefarms.billing.invoice.centralBill.customer.CustomerInvoice;
import com.prairiefarms.billing.invoice.item.Item;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.math.BigDecimal;

public class RemittanceSheet extends SheetControl {

    private final XSSFWorkbook workbook;

    public RemittanceSheet(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public void generate(CentralBillInvoice centralBillInvoice) {
        XSSFSheet sheet = workbook.getSheetAt(REMITTANCE_SHEET_TO_COPY);
        sheet.setDefaultRowHeightInPoints(DEFAULT_ROW_HEIGHT_IN_POINTS);
        sheet.setDisplayGridlines(false);
        sheet.setPrintGridlines(false);

        RemittanceSummaryRow.setSubtotal(
                sheet,
                centralBillInvoice.getCentralBill().getRemit().getContact().getId(),
                centralBillInvoice.getCentralBill().getContact().getId()
        );
        RemitToRows.set(sheet, centralBillInvoice.getCentralBill().getRemit());
        BillToRows.set(sheet, centralBillInvoice.getCentralBill());

        int rowNumber = REMITTANCE_ROW_TO_COPY;
        int subtotalStartingRowNumber = rowNumber;

        for (CustomerInvoice customerInvoice : centralBillInvoice.getCustomerInvoices()) {
            for (Invoice invoice : customerInvoice.getInvoices()) {
                double points = 0d;
                int quantity = 0;
                double subTotal = 0d;

                for (Item item : invoice.getItems()) {
                    points += (BigDecimal.valueOf(item.getPointsEach()).multiply(new BigDecimal(item.getQuantity()))).doubleValue();
                    quantity += item.getQuantity();
                    subTotal += item.getExtension();
                }

                rowNumber++;

                CopyRow.set(sheet, rowNumber, REMITTANCE_ROW_TO_COPY);
                RemittanceSummaryRow.set(sheet, rowNumber, invoice.getDeliveryDate(), customerInvoice.getCustomer(), invoice.getHeader().getId(), points, quantity, subTotal);
            }

            rowNumber++;

            CopyRow.set(sheet, rowNumber, REMITTANCE_ROW_TO_COPY);
            RemittanceSummaryRow.set(sheet, subtotalStartingRowNumber, (rowNumber - 1));
        }

        rowNumber++;

        CopyRow.set(sheet, rowNumber, REMITTANCE_ROW_TO_COPY);

        RemittanceSummaryRow.set(sheet, REMITTANCE_ROW_TO_COPY + 1, rowNumber);

        sheet.protectSheet(Environment.getInstance().getXlsxDocumentPassword());
    }
}