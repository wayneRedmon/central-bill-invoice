package com.prairiefarms.billing.document.pdf.pages;

import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.prairiefarms.billing.BillingEnvironment;
import com.prairiefarms.billing.document.pdf.pages.tables.*;
import com.prairiefarms.billing.invoice.item.ItemSummary;
import com.prairiefarms.billing.utils.Contact;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("DuplicatedCode")
public class ItemSummaryPage {

    private static final int ITEM_LINES_PER_PAGE = 35;

    private final Document document;
    private final Contact remitContact;
    private final Contact centralBillContact;
    private final List<ItemSummary> itemSummaries;

    private Canvas canvas;
    private ItemTable itemTable;
    private int pages;
    private int page;
    private int line;

    public ItemSummaryPage(Document document,
                           Contact remitContact,
                           Contact centralBillContact,
                           List<ItemSummary> itemSummaries) {
        this.document = document;
        this.remitContact = remitContact;
        this.centralBillContact = centralBillContact;
        this.itemSummaries = itemSummaries;
    }

    public void generate() throws IOException {
        pages = BillingEnvironment.getInstance().getPageCount(ITEM_LINES_PER_PAGE, itemSummaries.size());
        page = 0;

        stampHeader();

        final List<ItemSummary> sortedItemSummaries = itemSummaries.stream()
                .sorted(Comparator.comparing(ItemSummary::getSalesType)
                        .thenComparing(ItemSummary::getSize)
                        .thenComparing(ItemSummary::getType)
                        .thenComparing(ItemSummary::getLabel)
                        .thenComparing(ItemSummary::getName)
                        .thenComparing(ItemSummary::getId))
                .collect(Collectors.toList());

        for (ItemSummary itemSummary : sortedItemSummaries) {
            if (line > ITEM_LINES_PER_PAGE) stampHeader();
            canvas.add(itemTable.itemTable(itemSummary.getItem()));
            line++;
        }

        ItemSummaryTotalTable itemSummaryTotalTable = new ItemSummaryTotalTable(itemSummaries.stream().mapToDouble(ItemSummary::getExtension).sum());
        canvas.add(itemSummaryTotalTable.detail());

        canvas.close();
    }

    private void stampHeader() throws IOException {
        PdfPage pdfPage = document.getPdfDocument().addNewPage();

        page++;

        RemitToTable remitToTable = new RemitToTable(remitContact);
        canvas = new Canvas(new PdfCanvas(pdfPage), RemitToTable.LOGO_RECTANGLE);
        canvas.add(remitToTable.logoTable());
        canvas = new Canvas(new PdfCanvas(pdfPage), RemitToTable.REMIT_TO_RECTANGLE);
        canvas.add(remitToTable.detail());

        SubjectTable subjectTable = new SubjectTable(
                "ITEM SUMMARY",
                page,
                pages,
                centralBillContact.getId(),
                0);
        canvas = new Canvas(new PdfCanvas(pdfPage), SubjectTable.INVOICE_SUBJECT_RECTANGLE);
        canvas.add(subjectTable.detail());

        AddressTable billToCanvas = new AddressTable("bill to", centralBillContact);
        canvas = new Canvas(new PdfCanvas(pdfPage), AddressTable.BILL_TO_RECTANGLE);
        canvas.add(billToCanvas.detail());

        itemTable = new ItemTable();
        canvas = new Canvas(new PdfCanvas(pdfPage), ItemTable.ITEM_SUMMARY_TABLE_RECTANGLE);
        canvas.add(itemTable.header());

        line = 0;
    }
}
