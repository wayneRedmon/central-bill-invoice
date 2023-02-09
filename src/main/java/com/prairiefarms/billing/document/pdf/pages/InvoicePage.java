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
import com.prairiefarms.billing.customer.Customer;
import com.prairiefarms.billing.document.pdf.PdfEnvironment;
import com.prairiefarms.billing.document.pdf.pages.canvases.*;
import com.prairiefarms.billing.document.pdf.utils.Color;
import com.prairiefarms.billing.document.pdf.utils.FontSize;
import com.prairiefarms.billing.document.pdf.utils.Number;
import com.prairiefarms.billing.invoice.Invoice;
import com.prairiefarms.billing.invoice.item.Item;
import com.prairiefarms.billing.utils.Contact;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class InvoicePage {

    private static final int ITEM_LINES_PER_PAGE = 35;
    private static final int TOTAL_LINES_PER_PAGE = 5;

    private final Document document;
    private final Contact remitToContact;
    private final Contact billToContact;
    private final Customer customer;
    private final Invoice invoice;

    private Canvas canvas;
    private ItemCanvas itemCanvas;
    private int pages;
    private int page;
    private int line;

    public InvoicePage(Document document, Contact remitToContact, Contact billToContact, Customer customer, Invoice invoice) {
        this.document = document;
        this.remitToContact = remitToContact;
        this.billToContact = billToContact;
        this.customer = customer;
        this.invoice = invoice;
    }

    public void generate() {
        pages = PdfEnvironment.getInstance().getPageCount(ITEM_LINES_PER_PAGE, invoice.getItems().size() + TOTAL_LINES_PER_PAGE);
        page = 0;

        stampHeader();

        List<Item> sortedItems = invoice.getItems().stream()
                .sorted(Comparator.comparing(Item::getSalesType)
                        .thenComparing(Item::getSize)
                        .thenComparing(Item::getType)
                        .thenComparing(Item::getLabel)
                        .thenComparing(Item::getName)
                        .thenComparing(Item::getId))
                .collect(Collectors.toList());

        for (Item item : sortedItems) {
            if (line > ITEM_LINES_PER_PAGE) stampHeader();
            canvas.add(itemCanvas.itemTable(item));
            line++;
        }

        if ((line + TOTAL_LINES_PER_PAGE) > ITEM_LINES_PER_PAGE) stampHeader();

        canvas.add(total());

        canvas.close();
    }

    private void stampHeader() {
        PdfPage pdfPage = document.getPdfDocument().addNewPage();

        page++;

        RemitToCanvas remitToCanvas = new RemitToCanvas(remitToContact);
        canvas = new Canvas(new PdfCanvas(pdfPage), RemitToCanvas.LOGO_RECTANGLE);
        canvas.add(remitToCanvas.logoTable());
        canvas = new Canvas(new PdfCanvas(pdfPage), RemitToCanvas.REMIT_TO_RECTANGLE);
        canvas.add(remitToCanvas.table());

        SubjectCanvas subjectCanvas = new SubjectCanvas(
                "INVOICE #" + (customer.isExtendedInvoiceId() ? invoice.getHeader().getExtendedId() : String.valueOf(invoice.getHeader().getId())),
                page,
                pages,
                billToContact.getId(),
                customer.getContact().getId()
        );
        canvas = new Canvas(new PdfCanvas(pdfPage), SubjectCanvas.INVOICE_SUBJECT_RECTANGLE);
        canvas.add(subjectCanvas.table());

        AddressCanvas billToCanvas = new AddressCanvas("bill to", billToContact);
        canvas = new Canvas(new PdfCanvas(pdfPage), AddressCanvas.BILL_TO_RECTANGLE);
        canvas.add(billToCanvas.table());

        AddressCanvas shipToCanvas = new AddressCanvas("ship to", customer.getContact());
        canvas = new Canvas(new PdfCanvas(pdfPage), AddressCanvas.SHIP_TO_RECTANGLE);
        canvas.add(shipToCanvas.table());

        SaleCanvas saleCanvas = new SaleCanvas(
                invoice.getHeader().getPurchaseOrder(),
                customer.getSalesperson(),
                invoice.getHeader().getDeliveryDate()
        );
        canvas = new Canvas(new PdfCanvas(pdfPage), SaleCanvas.SALE_RECTANGLE);
        canvas.add(saleCanvas.table());

        itemCanvas = new ItemCanvas();
        canvas = new Canvas(new PdfCanvas(pdfPage), ItemCanvas.ITEM_TABLE_RECTANGLE);
        canvas.add(itemCanvas.headerTable());

        line = 0;
    }

    private Table total() {
        return new Table(UnitValue.createPercentArray(new float[]{5f, 1f}))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph("SUBTOTAL"))
                )
                .addCell(new Cell(1, 1)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setBorder(PdfEnvironment.getInstance().LABEL_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getItemFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setFontColor(invoice.subTotal() < 0 ? Color.RED.asDeviceRgb() : Color.BLACK.asDeviceRgb())
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph(Number.CURRENCY.type.format(invoice.subTotal())))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph("APPLIED"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(PdfEnvironment.getInstance().LABEL_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getItemFont())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setFontColor(invoice.getAppliedAmount() < 0 ? Color.RED.asDeviceRgb() : Color.BLACK.asDeviceRgb())
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph(Number.CURRENCY.type.format(invoice.getAppliedAmount())))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph((invoice.getDiscountRate() == 0 ? "" : invoice.getDiscountRate() + "% ") + "DISCOUNT"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(PdfEnvironment.getInstance().LABEL_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getItemFont())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setFontColor(invoice.getDiscountAmount() < 0 ? Color.RED.asDeviceRgb() : Color.BLACK.asDeviceRgb())
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph(Number.CURRENCY.type.format(invoice.getDiscountAmount())))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph((invoice.getTaxRate() == 0 ? "" : invoice.getTaxRate() + "% ") + "TAX"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(PdfEnvironment.getInstance().LABEL_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getItemFont())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setFontColor(invoice.getTaxAmount() < 0 ? Color.RED.asDeviceRgb() : Color.BLACK.asDeviceRgb())
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph(Number.CURRENCY.type.format(invoice.getTaxAmount())))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontBold())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph("TOTAL DUE"))
                )
                .addCell(new Cell(1, 1)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setBorder(PdfEnvironment.getInstance().LABEL_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getItemFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setFontColor(invoice.amountDue() < 0 ? Color.RED.asDeviceRgb() : Color.BLACK.asDeviceRgb())
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph(Number.CURRENCY.type.format(invoice.amountDue())))
                );
    }
}
