package com.prairiefarms.billing.document.pdf;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.document.DocumentThread;
import com.prairiefarms.billing.document.ItemSort;
import com.prairiefarms.billing.document.pdf.pages.InvoicePage;
import com.prairiefarms.billing.document.pdf.pages.ItemSummaryPage;
import com.prairiefarms.billing.document.pdf.pages.RemittancePage;
import com.prairiefarms.billing.invoice.Invoice;
import com.prairiefarms.billing.invoice.centralBill.CentralBillInvoice;
import com.prairiefarms.billing.invoice.centralBill.customer.CustomerInvoice;
import com.prairiefarms.billing.utils.FolderMaintenance;
import com.prairiefarms.utils.email.Email;
import com.prairiefarms.utils.email.Message;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;

public class PdfService implements Callable<DocumentThread> {

    private final CentralBillInvoice centralBillInvoice;

    private String documentName;

    public PdfService(CentralBillInvoice centralBillInvoice) {
        this.centralBillInvoice = centralBillInvoice;
    }

    @Override
    public DocumentThread call() {
        Exception threadException = null;

        try {
            this.createDocument();
            this.emailDocument();
            this.archiveDocument();
        } catch (Exception exception) {
            threadException = exception;
        }

        return new DocumentThread(
                centralBillInvoice.getCentralBill(),
                threadException
        );
    }

    private void createDocument() throws IOException {
        documentName = "Invoice_" +
                String.format("%03d", Environment.getInstance().getDairyId()) +
                "_" +
                String.format("%03d", centralBillInvoice.getCentralBill().getContact().getId()) +
                "_" +
                Environment.getInstance().billingDateAsYYMMD() +
                StringUtils.normalizeSpace(centralBillInvoice.getCentralBill().getDocumentType().fileExtension);

        try (PdfDocument pdfDocument = new PdfDocument(new PdfWriter(new File(Environment.getInstance().emailOutBox() + documentName)))) {
            PdfDocumentInfo pdfDocumentInfo = pdfDocument.getDocumentInfo();
            pdfDocumentInfo.setTitle("Central Bill Invoice");
            pdfDocumentInfo.setAuthor(centralBillInvoice.getCentralBill().getRemit().getContact().getName());
            pdfDocumentInfo.setSubject("invoice");
            pdfDocumentInfo.setKeywords("central,bill,invoice");
            pdfDocumentInfo.setCreator(
                    centralBillInvoice.getCentralBill().getRemit().getContact().getName() + ", " +
                            centralBillInvoice.getCentralBill().getRemit().getContact().getPhone()
            );

            try (Document document = new Document(pdfDocument)) {
                document.getPdfDocument().setDefaultPageSize(PageSize.LETTER);
                document.setMargins(36f, 36f, 36f, 36f);

                final RemittancePage remittancePage = new RemittancePage(document, centralBillInvoice);
                remittancePage.generate();

                for (CustomerInvoice customerInvoice : centralBillInvoice.getCustomerInvoices()) {
                    for (Invoice invoice : customerInvoice.getInvoices()) {
                        InvoicePage invoicePage = new InvoicePage(
                                document,
                                centralBillInvoice.getCentralBill().getRemit().getContact(),
                                centralBillInvoice.getCentralBill().getContact(),
                                customerInvoice.getCustomer(),
                                invoice
                        );

                        invoicePage.generate();
                    }
                }

                final ItemSummaryPage itemSummaryPage = new ItemSummaryPage(
                        document,
                        centralBillInvoice.getCentralBill().getRemit().getContact(),
                        centralBillInvoice.getCentralBill().getContact(),
                        new ItemSort(centralBillInvoice.getCustomerInvoices()).sort()
                );

                itemSummaryPage.generate();
            }
        }
    }

    private void emailDocument() throws Exception {
        Email email = new Email(
                Environment.getInstance().getEmailServer(),
                new Message(
                        centralBillInvoice.getCentralBill().getRemit().getContact().getEmail(),
                        centralBillInvoice.getCentralBill().getContact().getEmail(),
                        Environment.getInstance().emailCarbonCopy(),
                        Environment.getInstance().getEmailSubject(centralBillInvoice.getCentralBill().getContact().getId()),
                        Environment.getInstance().getEmailMessageBody(),
                        new ArrayList<>(Collections.singletonList(new File(Environment.getInstance().emailOutBox() + documentName)))
                )
        );

        email.send();
    }

    private void archiveDocument() throws Exception {
        FolderMaintenance.move(
                Environment.getInstance().emailOutBox() + documentName,
                Environment.getInstance().emailSentBox() + documentName
        );
    }
}
