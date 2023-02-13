package com.prairiefarms.billing.document.pdf.pages;

import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.customer.Customer;
import com.prairiefarms.billing.document.pdf.pages.tables.*;
import com.prairiefarms.billing.invoice.Invoice;
import com.prairiefarms.billing.invoice.item.Item;
import com.prairiefarms.billing.utils.Contact;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("DuplicatedCode")
public class InvoicePage {

    private static final int ITEM_LINES_PER_PAGE = 35;
    private static final int TOTAL_LINES_PER_PAGE = 5;

    private final Document document;
    private final Contact remitToContact;
    private final Contact billToContact;
    private final Customer customer;
    private final Invoice invoice;

    private Canvas canvas;
    private ItemTable itemTable;
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

    public void generate() throws IOException {
        pages = Environment.getInstance().getPageCount(ITEM_LINES_PER_PAGE, invoice.getItems().size() + TOTAL_LINES_PER_PAGE);
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
            canvas.add(itemTable.itemTable(item));
            line++;
        }

        if ((line + TOTAL_LINES_PER_PAGE) > ITEM_LINES_PER_PAGE) stampHeader();

        InvoiceTotalTable invoiceTotalTable = new InvoiceTotalTable(invoice);
        canvas.add(invoiceTotalTable.detail());

        canvas.close();
    }

    private void stampHeader() throws IOException {
        PdfPage pdfPage = document.getPdfDocument().addNewPage();

        page++;

        RemitToTable remitToTable = new RemitToTable(remitToContact);
        canvas = new Canvas(new PdfCanvas(pdfPage), RemitToTable.LOGO_RECTANGLE);
        canvas.add(remitToTable.logoTable());
        canvas = new Canvas(new PdfCanvas(pdfPage), RemitToTable.REMIT_TO_RECTANGLE);
        canvas.add(remitToTable.detail());

        SubjectTable subjectTable = new SubjectTable(
                "INVOICE #" + (customer.isExtendedInvoiceId() ? invoice.getHeader().getExtendedId() : String.valueOf(invoice.getHeader().getId())),
                page,
                pages,
                billToContact.getId(),
                customer.getContact().getId()
        );
        canvas = new Canvas(new PdfCanvas(pdfPage), SubjectTable.INVOICE_SUBJECT_RECTANGLE);
        canvas.add(subjectTable.detail());

        AddressTable billToCanvas = new AddressTable("bill to", billToContact);
        canvas = new Canvas(new PdfCanvas(pdfPage), AddressTable.BILL_TO_RECTANGLE);
        canvas.add(billToCanvas.detail());

        AddressTable shipToCanvas = new AddressTable("ship to", customer.getContact());
        canvas = new Canvas(new PdfCanvas(pdfPage), AddressTable.SHIP_TO_RECTANGLE);
        canvas.add(shipToCanvas.detail());

        SaleTable saleTable = new SaleTable(
                invoice.getHeader().getPurchaseOrder(),
                customer.getSalesperson(),
                invoice.getHeader().getDeliveryDate()
        );
        canvas = new Canvas(new PdfCanvas(pdfPage), SaleTable.SALE_RECTANGLE);
        canvas.add(saleTable.detail());

        itemTable = new ItemTable();
        canvas = new Canvas(new PdfCanvas(pdfPage), ItemTable.ITEM_TABLE_RECTANGLE);
        canvas.add(itemTable.header());

        line = 0;
    }
}
