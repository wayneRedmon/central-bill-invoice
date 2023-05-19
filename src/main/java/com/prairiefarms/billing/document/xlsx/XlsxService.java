package com.prairiefarms.billing.document.xlsx;

import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.document.DocumentThread;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.RemittanceSheet;
import com.prairiefarms.billing.invoice.centralBill.CentralBillInvoice;
import com.prairiefarms.billing.utils.FolderMaintenance;
import com.prairiefarms.utils.email.Email;
import com.prairiefarms.utils.email.Message;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;

public class XlsxService implements Callable<DocumentThread> {

    private final CentralBillInvoice centralBillInvoice;

    private String documentName;

    public XlsxService(CentralBillInvoice centralBillInvoice) {
        this.centralBillInvoice = centralBillInvoice;
    }

    @Override
    public DocumentThread call() {
        Exception threadException = null;

        try {
            createDocument();
            emailDocument();
            archiveDocument();
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
                Environment.getInstance().getDairyIdAsText() +
                "_" +
                centralBillInvoice.getCentralBill().getIdAsText() +
                "_" +
                Environment.getInstance().billingDateAsYYMMD() +
                StringUtils.normalizeSpace(centralBillInvoice.getCentralBill().getDocumentType().fileExtension);

        try (XSSFWorkbook xssfWorkbook = new XSSFWorkbook()) {
            //RemittanceSheet.generate(xssfWorkbook, centralBillInvoice);
            RemittanceSheet remittanceSheet = new RemittanceSheet(xssfWorkbook, centralBillInvoice);
            remittanceSheet.generate();


/*            ItemSummarySheet.generate(
                    xssfWorkbook,
                    centralBillInvoice.getCentralBill(),
                    ItemSort.sort(centralBillInvoice.getCustomerInvoices())
            );

            InvoiceSheet.generate(xssfWorkbook, centralBillInvoice);
*/
            try (FileOutputStream fileOutputStream = new FileOutputStream(Environment.getInstance().emailOutBox() + documentName)) {
                xssfWorkbook.write(fileOutputStream);
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

    private void archiveDocument() throws IOException {
        FolderMaintenance.move(
                Environment.getInstance().emailOutBox() + documentName,
                Environment.getInstance().emailSentBox() + documentName
        );
    }
}
