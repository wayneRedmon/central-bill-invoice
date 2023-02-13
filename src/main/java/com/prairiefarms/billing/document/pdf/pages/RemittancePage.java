package com.prairiefarms.billing.document.pdf.pages;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.prairiefarms.billing.BillingEnvironment;
import com.prairiefarms.billing.document.pdf.pages.tables.*;
import com.prairiefarms.billing.invoice.Invoice;
import com.prairiefarms.billing.invoice.centralBill.CentralBillInvoice;
import com.prairiefarms.billing.invoice.centralBill.customer.CustomerInvoice;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RemittancePage {

    private static final int TABLE_DETAIL_LINES_PER_PAGE = 35;
    private static final Rectangle SUMMARY_TABLE_RECTANGLE = new Rectangle(36f, 36f, 540f, 568f);

    private final Document document;
    private final CentralBillInvoice centralBillInvoice;
    private final RemittanceDetailTable remittanceDetailTable;

    private Canvas canvas;

    private int pages;
    private int page;
    private int line;

    public RemittancePage(Document document, CentralBillInvoice centralBillInvoice) {
        this.document = document;
        this.centralBillInvoice = centralBillInvoice;
        this.remittanceDetailTable = new RemittanceDetailTable();
    }

    public void generate() throws IOException {
        final int lines = ((int) centralBillInvoice.getCustomerInvoices().stream()
                .mapToLong(customerInvoice -> customerInvoice.getInvoices().size())
                .sum()) +
                ((centralBillInvoice.getCustomerInvoices().size() * 3) + 1);
        // the above ' * 3' = customer row, invoice header row, and total due row
        // the above ' + 1' = remittance due row
        pages = BillingEnvironment.getInstance().getPageCount(TABLE_DETAIL_LINES_PER_PAGE, lines);
        page = 0;

        stampHeader();

        final List<CustomerInvoice> sortedCustomers = centralBillInvoice.getCustomerInvoices().stream()
                .sorted(Comparator.comparing(CustomerInvoice::getSortSequence).thenComparing(CustomerInvoice::getId))
                .collect(Collectors.toList());

        for (CustomerInvoice customerInvoice : sortedCustomers) {
            if (line > TABLE_DETAIL_LINES_PER_PAGE - 2) stampHeader();
            // the above ' - 2' = customer row and invoice detail header row

            canvas.add(remittanceDetailTable.customer(customerInvoice.getCustomer()));
            line++;

            canvas.add(remittanceDetailTable.invoiceTableHead());
            line++;

            final List<Invoice> sortedInvoices = customerInvoice.getInvoices().stream()
                    .sorted(Comparator.comparing(Invoice::getDeliveryDate)
                            .thenComparing(Invoice::getInvoiceId))
                    .collect(Collectors.toList());

            for (Invoice invoice : sortedInvoices) {
                if (line > TABLE_DETAIL_LINES_PER_PAGE) {
                    stampHeader();

                    canvas.add(remittanceDetailTable.customer(customerInvoice.getCustomer()));
                    line++;

                    canvas.add(remittanceDetailTable.invoiceTableHead());
                    line++;
                }

                canvas.add(remittanceDetailTable.invoice(invoice, customerInvoice.getCustomer().isExtendedInvoiceId()));
                line++;
            }

            canvas.add(remittanceDetailTable.customerAmountDue(customerInvoice.getAmountDue()));
            line++;

            canvas.add(remittanceDetailTable.blankRow());
            line++;
        }

        canvas.add(remittanceDetailTable.remittanceAmountDue(centralBillInvoice.getAmountDue()));

        canvas.close();
    }

    private void stampHeader() throws IOException {
        PdfPage pdfPage = document.getPdfDocument().addNewPage();

        page++;

        RemitToTable remitToTable = new RemitToTable(centralBillInvoice.getCentralBill().getRemit().getContact());
        canvas = new Canvas(new PdfCanvas(pdfPage), RemitToTable.LOGO_RECTANGLE);
        canvas.add(remitToTable.logoTable());
        canvas = new Canvas(new PdfCanvas(pdfPage), RemitToTable.REMIT_TO_RECTANGLE);
        canvas.add(remitToTable.detail());

        SubjectTable subjectTable = new SubjectTable("REMITTANCE",
                page,
                pages,
                centralBillInvoice.getCentralBill().getContact().getId(),
                0
        );
        canvas = new Canvas(new PdfCanvas(pdfPage), SubjectTable.INVOICE_SUBJECT_RECTANGLE);
        canvas.add(subjectTable.detail());

        AddressTable billToCanvas = new AddressTable("bill to", centralBillInvoice.getCentralBill().getContact());
        canvas = new Canvas(new PdfCanvas(pdfPage), AddressTable.BILL_TO_RECTANGLE);
        canvas.add(billToCanvas.detail());

        InstructionTable instructionTable = new InstructionTable(centralBillInvoice.getCentralBill().getRemit().getContact().getPhone());
        canvas = new Canvas(new PdfCanvas(pdfPage), InstructionTable.INSTRUCTION_RECTANGLE);
        canvas.add(instructionTable.detail());

        canvas = new Canvas(new PdfCanvas(pdfPage), SUMMARY_TABLE_RECTANGLE);
        line = 0;
    }
}
