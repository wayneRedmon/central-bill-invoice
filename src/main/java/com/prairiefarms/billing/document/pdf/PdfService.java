package com.prairiefarms.billing.document.pdf;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.prairiefarms.billing.BillingEnvironment;
import com.prairiefarms.billing.document.pdf.pages.InvoicePage;
import com.prairiefarms.billing.document.pdf.pages.ItemSummaryPage;
import com.prairiefarms.billing.document.pdf.pages.RemittancePage;
import com.prairiefarms.billing.invoice.Invoice;
import com.prairiefarms.billing.invoice.centralBill.CentralBillInvoice;
import com.prairiefarms.billing.invoice.centralBill.customer.CustomerInvoice;
import com.prairiefarms.billing.invoice.item.Item;
import com.prairiefarms.billing.invoice.item.ItemSummary;
import com.prairiefarms.billing.utils.FolderMaintenance;
import com.prairiefarms.utils.email.Email;
import com.prairiefarms.utils.email.Message;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PdfService implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger("siftingAppender");

    private final CentralBillInvoice centralBillInvoice;

    private String documentName;
    private List<ItemSummary> itemSummaries;

    public PdfService(CentralBillInvoice centralBillInvoice) throws IOException {
        this.centralBillInvoice = centralBillInvoice;

        PdfEnvironment.getInstance().init();
    }

    @Override
    public void run() {
        System.out.println(" starting thread for " + Thread.currentThread().getName() + " = " + centralBillInvoice.getCentralBill().getContact().getId() + " " + centralBillInvoice.getCentralBill().getContact().getName());

        try {
            this.setItemSummary();
            this.createDocument();
            this.emailDocument();
            this.archiveDocument();
        } catch (Exception exception) {
            LOGGER.error("Exception in PdfService.run()", exception);
        }
    }

    private void setItemSummary() {
        itemSummaries = new ArrayList<>();

        for (CustomerInvoice customerInvoice : centralBillInvoice.getCustomerInvoices()) {
            for (Invoice invoice : customerInvoice.getInvoices()) {
                for (Item item : invoice.getItems()) {
                    boolean found = false;

                    for (ItemSummary itemSummary : itemSummaries) {
                        if (itemSummary.getSalesType().equals(item.getSalesType()) &&
                                itemSummary.getId() == item.getId() &&
                                itemSummary.getPriceEach() == item.getPriceEach()) {
                            itemSummary.setQuantity(item.getQuantity());

                            itemSummary.setExtension(item.getExtension());

                            found = true;

                            break;
                        }
                    }

                    if (!found) {
                        itemSummaries.add(
                                new ItemSummary(
                                        item.getSalesType(),
                                        item.getId(),
                                        item.getPriceEach(),
                                        item.getName(),
                                        item.getSize(),
                                        item.getType(),
                                        item.getLabel(),
                                        item.getQuantity(),
                                        item.getExtension()
                                )
                        );
                    }
                }
            }
        }
    }

    private void createDocument() throws FileNotFoundException {
        documentName = "Invoice_" +
                String.format("%03d", BillingEnvironment.getInstance().getDairyId()) +
                "_" +
                String.format("%03d", centralBillInvoice.getCentralBill().getContact().getId()) +
                "_" +
                BillingEnvironment.getInstance().getBillingDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                StringUtils.normalizeSpace(centralBillInvoice.getCentralBill().getDocumentType().fileExtension);

        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(new File(BillingEnvironment.getInstance().emailOutBox() + documentName)));

        PdfDocumentInfo pdfDocumentInfo = pdfDocument.getDocumentInfo();
        pdfDocumentInfo.setTitle("Central Bill Invoice");
        pdfDocumentInfo.setAuthor(centralBillInvoice.getCentralBill().getRemit().getContact().getName());
        pdfDocumentInfo.setSubject("invoice");
        pdfDocumentInfo.setKeywords("central,bill,invoice");
        pdfDocumentInfo.setCreator(
                centralBillInvoice.getCentralBill().getRemit().getContact().getName() + ", " +
                        centralBillInvoice.getCentralBill().getRemit().getContact().getPhone()
        );

        Document document = new Document(pdfDocument);
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

        final ItemSummaryPage itemSummaryPage = new ItemSummaryPage(document,
                centralBillInvoice.getCentralBill().getRemit().getContact(),
                centralBillInvoice.getCentralBill().getContact(),
                itemSummaries
        );

        itemSummaryPage.generate();

        document.close();
    }

    private void emailDocument() throws Exception {
        final String subject =
                "[" + String.format("%03d", BillingEnvironment.getInstance().getDairyId()) + "-" +
                        String.format("%03d", centralBillInvoice.getCentralBill().getContact().getId()) + "] " +
                        BillingEnvironment.getInstance().getFrequency().type + " invoices for " +
                        BillingEnvironment.getInstance().getBillingDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        final StringBuilder messageBody = new StringBuilder()
                .append("<p>Attached are your <b>")
                .append(BillingEnvironment.getInstance().getFrequency().type)
                .append("</b> invoices for <b>")
                .append(BillingEnvironment.getInstance().getBillingDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")))
                .append("</b>.<br><br><b><i>Thank you for your business!</i></b><br><br>");

        new Email(
                BillingEnvironment.getInstance().getEmailServer(),
                new Message(
                        centralBillInvoice.getCentralBill().getRemit().getContact().getEmail(),
                        centralBillInvoice.getCentralBill().getContact().getEmail(),
                        BillingEnvironment.getInstance().emailCarbonCopy(),
                        subject,
                        messageBody,
                        new ArrayList<>(Collections.singletonList(new File(BillingEnvironment.getInstance().emailOutBox() + documentName)))
                )
        ).send();
    }

    private void archiveDocument() throws Exception {
        new FolderMaintenance().move(
                BillingEnvironment.getInstance().emailOutBox() + documentName,
                BillingEnvironment.getInstance().emailSentBox() + documentName
        );
    }
}
