package com.prairiefarms.billing.document.pdf.pages;

import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.prairiefarms.billing.document.pdf.PdfEnvironment;
import com.prairiefarms.billing.document.pdf.pages.canvases.AddressCanvas;
import com.prairiefarms.billing.document.pdf.pages.canvases.ItemCanvas;
import com.prairiefarms.billing.document.pdf.pages.canvases.RemitToCanvas;
import com.prairiefarms.billing.document.pdf.pages.canvases.SubjectCanvas;
import com.prairiefarms.billing.document.pdf.utils.Color;
import com.prairiefarms.billing.document.pdf.utils.FontSize;
import com.prairiefarms.billing.document.pdf.utils.Number;
import com.prairiefarms.billing.invoice.item.ItemSummary;
import com.prairiefarms.billing.utils.Contact;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ItemSummaryPage {

    private static final int ITEM_LINES_PER_PAGE = 35;

    private final Document document;
    private final Contact remitContact;
    private final Contact centralBillContact;
    private final List<ItemSummary> itemSummaries;

    private Canvas canvas;
    private ItemCanvas itemCanvas;
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

    public void generate() {
        pages = PdfEnvironment.getInstance().getPageCount(ITEM_LINES_PER_PAGE, itemSummaries.size());
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
            canvas.add(itemCanvas.itemTable(itemSummary.getItem()));
            line++;
        }

        canvas.add(totalTable(itemSummaries.stream().mapToDouble(ItemSummary::getExtension).sum()));

        canvas.close();
    }

    private void stampHeader() {
        PdfPage pdfPage = document.getPdfDocument().addNewPage();

        page++;

        RemitToCanvas remitToCanvas = new RemitToCanvas(remitContact);
        canvas = new Canvas(new PdfCanvas(pdfPage), RemitToCanvas.LOGO_RECTANGLE);
        canvas.add(remitToCanvas.logoTable());
        canvas = new Canvas(new PdfCanvas(pdfPage), RemitToCanvas.REMIT_TO_RECTANGLE);
        canvas.add(remitToCanvas.table());

        SubjectCanvas subjectCanvas = new SubjectCanvas(
                "ITEM SUMMARY",
                page,
                pages,
                centralBillContact.getId(),
                0);
        canvas = new Canvas(new PdfCanvas(pdfPage), SubjectCanvas.INVOICE_SUBJECT_RECTANGLE);
        canvas.add(subjectCanvas.table());

        AddressCanvas billToCanvas = new AddressCanvas("bill to", centralBillContact);
        canvas = new Canvas(new PdfCanvas(pdfPage), AddressCanvas.BILL_TO_RECTANGLE);
        canvas.add(billToCanvas.table());

        itemCanvas = new ItemCanvas();
        canvas = new Canvas(new PdfCanvas(pdfPage), ItemCanvas.ITEM_SUMMARY_TABLE_RECTANGLE);
        canvas.add(itemCanvas.headerTable());

        line = 0;
    }

    private Table totalTable(Double remittanceDue) {
        return new Table(UnitValue.createPercentArray(new float[]{5f, 1f}))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph("TOTAL"))
                )
                .addCell(new Cell(1, 1)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setBorder(PdfEnvironment.getInstance().LABEL_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getItemFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setFontColor(remittanceDue < 0 ? Color.RED.asDeviceRgb() : Color.BLACK.asDeviceRgb())
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph(Number.CURRENCY.type.format(remittanceDue)))
                );
    }
}
