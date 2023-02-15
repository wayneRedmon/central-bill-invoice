package com.prairiefarms.billing.document.xlsx;

import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.InvoiceSheet;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.ItemSummarySheet;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.RemittanceSheet;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.overlay.StampLogo;
import com.prairiefarms.billing.invoice.Invoice;
import com.prairiefarms.billing.invoice.centralBill.CentralBillInvoice;
import com.prairiefarms.billing.invoice.centralBill.customer.CustomerInvoice;
import com.prairiefarms.billing.invoice.item.Item;
import com.prairiefarms.billing.invoice.item.ItemSummary;
import com.prairiefarms.billing.utils.FolderMaintenance;
import com.prairiefarms.utils.email.Email;
import com.prairiefarms.utils.email.Message;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class XlsxService {

    private static final Logger LOGGER = LoggerFactory.getLogger(XlsxService.class);

    private final CentralBillInvoice centralBillInvoice;

    private List<ItemSummary> sortedItemSummaries;
    private String documentName;

    public XlsxService(CentralBillInvoice centralBillInvoice) {
        this.centralBillInvoice = centralBillInvoice;
    }

    public String call() {
        StringBuilder loggingText = new StringBuilder()
                .append("[")
                .append(centralBillInvoice.getCentralBill().getContact().getId())
                .append("] ")
                .append(centralBillInvoice.getCentralBill().getContact().getName());

        boolean success = false;

        try {
            this.setItemSummary();
            this.createDocument();
            this.emailDocument();
            this.archiveDocument();

            success = true;
        } catch (Exception exception) {
            LOGGER.error("Exception in XlsxService.call()", exception);
        } finally {
            loggingText
                    .append(" - was an invoice generated? (")
                    .append(success ? " YES " : " *** NO *** ")
                    .append(")");
        }

        return loggingText.toString();
    }

    private void setItemSummary() {
        sortedItemSummaries = new ArrayList<>();

        List<ItemSummary> itemSummaries = new ArrayList<>();

        for (CustomerInvoice customerInvoice : centralBillInvoice.getCustomerInvoices()) {
            for (Invoice invoice : customerInvoice.getInvoices()) {
                for (Item item : invoice.getItems()) {
                    boolean found = false;

                    for (ItemSummary itemSummary : sortedItemSummaries) {
                        if (itemSummary.getSalesType().equals(item.getSalesType()) &&
                                itemSummary.getId() == item.getId() &&
                                itemSummary.getPriceEach() == item.getPriceEach()) {
                            itemSummary.setQuantity(item.getQuantity());
                            itemSummary.setExtension(item.getExtension());
                            itemSummary.setPointsEach(item.getPointsEach());

                            found = true;

                            break;
                        }
                    }

                    if (!found) {
                        itemSummaries.add(
                                new ItemSummary(
                                        item.getSalesType(),
                                        item.getId(),
                                        item.getName(),
                                        item.getQuantity(),
                                        item.getPriceEach(),
                                        item.isPromotion(),
                                        item.getExtension(),
                                        item.getSize(),
                                        item.getType(),
                                        item.getLabel(),
                                        item.getPointsEach()
                                )
                        );
                    }
                }
            }
        }

        sortedItemSummaries = itemSummaries.stream()
                .sorted(
                        Comparator.comparing(ItemSummary::getSalesType)
                                .thenComparing(ItemSummary::getSize)
                                .thenComparing(ItemSummary::getType)
                                .thenComparing(ItemSummary::getLabel)
                                .thenComparing(ItemSummary::getName)
                                .thenComparing(ItemSummary::getId)
                                .thenComparing(ItemSummary::getPriceEach)
                                .thenComparing(ItemSummary::isPromotion)
                )
                .collect(Collectors.toList());
    }

    private void createDocument() {
        documentName = "Invoice_" +
                String.format("%03d", Environment.getInstance().getDairyId()) +
                "_" +
                String.format("%03d", centralBillInvoice.getCentralBill().getContact().getId()) +
                "_" +
                Environment.getInstance().billingDateAsYYMMD() +
                StringUtils.normalizeSpace(centralBillInvoice.getCentralBill().getDocumentType().fileExtension);

        try (XSSFWorkbook xssfWorkbook = new XSSFWorkbook(Environment.getInstance().getXlsxTemplate())) {
            //ZipSecureFile.setMinInflateRatio(0);

            WorkbookEnvironment.getInstance().init(xssfWorkbook);

            StampLogo.set(xssfWorkbook);

            new RemittanceSheet(xssfWorkbook).generate(centralBillInvoice);
            new ItemSummarySheet(xssfWorkbook).generate(centralBillInvoice.getCentralBill(), sortedItemSummaries);
            new InvoiceSheet(xssfWorkbook).generate(centralBillInvoice);

            if (ObjectUtils.isNotEmpty(xssfWorkbook)) xssfWorkbook.removeSheetAt(2);

            try (FileOutputStream fileOutputStream = new FileOutputStream(Environment.getInstance().emailOutBox() + documentName)) {
                xssfWorkbook.write(fileOutputStream);
            }
        } catch (IOException exception) {
            LOGGER.error("Exception in XlsxService.createDocument()", exception);
        }
    }

    private void emailDocument() throws Exception {
        final String subject =
                "[" + String.format("%03d", Environment.getInstance().getDairyId()) + "-" +
                        String.format("%03d", centralBillInvoice.getCentralBill().getContact().getId()) + "] " +
                        Environment.getInstance().frequencyAsText() + " invoices for " +
                        Environment.getInstance().billingDateAsUSA();

        final StringBuilder messageBody = new StringBuilder()
                .append("<p>Attached are your <b>")
                .append(Environment.getInstance().frequencyAsText())
                .append("</b> invoices for <b>")
                .append(Environment.getInstance().billingDateAsUSA())
                .append("</b>.<br><br><b><i>Thank you for your business!</i></b><br><br>");

        new Email(
                Environment.getInstance().getEmailServer(),
                new Message(
                        centralBillInvoice.getCentralBill().getRemit().getContact().getEmail(),
                        centralBillInvoice.getCentralBill().getContact().getEmail(),
                        Environment.getInstance().emailCarbonCopy(),
                        subject,
                        messageBody,
                        new ArrayList<>(Collections.singletonList(new File(Environment.getInstance().emailOutBox() + documentName)))
                )
        ).send();
    }

    private void archiveDocument() throws Exception {
        FolderMaintenance.move(
                Environment.getInstance().emailOutBox() + documentName,
                Environment.getInstance().emailSentBox() + documentName
        );
    }
}
