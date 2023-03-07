package com.prairiefarms.billing.document.xlsx;

import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.document.DocumentThread;
import com.prairiefarms.billing.document.ItemSort;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.InvoiceSheet;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.ItemSummarySheet;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.RemittanceSheet;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.overlay.Logo;
import com.prairiefarms.billing.invoice.centralBill.CentralBillInvoice;
import com.prairiefarms.billing.utils.FolderMaintenance;
import com.prairiefarms.utils.email.Email;
import com.prairiefarms.utils.email.Message;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.Callable;

public class XlsxService implements Callable<DocumentThread> {

    private static final String XLSX_TEMPLATE_PATH = "templates/DistributorInvoice.xlsx";

    private final CentralBillInvoice centralBillInvoice;

    private String documentName;

    public XlsxService(CentralBillInvoice centralBillInvoice) {
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

        try (XSSFWorkbook xssfWorkbook = new XSSFWorkbook(Objects.requireNonNull(XlsxService.class.getClassLoader().getResourceAsStream(XLSX_TEMPLATE_PATH)))) {
            new Logo(xssfWorkbook).stamp();
            new RemittanceSheet(xssfWorkbook).generate(centralBillInvoice);
            new ItemSummarySheet(xssfWorkbook).generate(
                    centralBillInvoice.getCentralBill(),
                    new ItemSort(centralBillInvoice.getCustomerInvoices()).sort()
            );
            new InvoiceSheet(xssfWorkbook).generate(centralBillInvoice);

            if (ObjectUtils.isNotEmpty(xssfWorkbook)) xssfWorkbook.removeSheetAt(2);

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

    private void archiveDocument() throws Exception {
        FolderMaintenance.move(
                Environment.getInstance().emailOutBox() + documentName,
                Environment.getInstance().emailSentBox() + documentName
        );
    }
}
