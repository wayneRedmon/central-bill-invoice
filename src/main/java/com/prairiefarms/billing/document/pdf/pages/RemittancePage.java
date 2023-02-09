package com.prairiefarms.billing.document.pdf.pages;

import com.itextpdf.kernel.geom.Rectangle;
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
import com.prairiefarms.billing.document.pdf.pages.canvases.AddressCanvas;
import com.prairiefarms.billing.document.pdf.pages.canvases.InstructionCanvas;
import com.prairiefarms.billing.document.pdf.pages.canvases.RemitToCanvas;
import com.prairiefarms.billing.document.pdf.pages.canvases.SubjectCanvas;
import com.prairiefarms.billing.document.pdf.utils.Color;
import com.prairiefarms.billing.document.pdf.utils.FontSize;
import com.prairiefarms.billing.document.pdf.utils.Number;
import com.prairiefarms.billing.invoice.Invoice;
import com.prairiefarms.billing.invoice.centralBill.CentralBillInvoice;
import com.prairiefarms.billing.invoice.centralBill.customer.CustomerInvoice;
import org.apache.commons.lang3.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RemittancePage {

    private static final int TABLE_DETAIL_LINES_PER_PAGE = 35;
    private static final float[] SUMMARY_TABLE_COLUMNS = new float[]{270f, 270f};
    private static final float[] INVOICE_TABLE_COLUMNS = new float[]{100f, 80f, 90f, 272f};
    private static final Rectangle SUMMARY_TABLE_RECTANGLE = new Rectangle(36f, 36f, 540f, 568f);

    private final Document document;
    private final CentralBillInvoice centralBillInvoice;

    private Canvas canvas;

    private int pages;
    private int page;
    private int line;

    public RemittancePage(Document document, CentralBillInvoice centralBillInvoice) {
        this.document = document;
        this.centralBillInvoice = centralBillInvoice;
    }

    public void generate() {
        final int lines = ((int) centralBillInvoice.getCustomerInvoices().stream()
                .mapToLong(customerInvoice -> customerInvoice.getInvoices().size())
                .sum()) +
                ((centralBillInvoice.getCustomerInvoices().size() * 3) + 1);
        // the above ' * 3' = customer row, invoice headerTable row, and total due row
        // the above ' + 1' = remittance due row
        pages = PdfEnvironment.getInstance().getPageCount(TABLE_DETAIL_LINES_PER_PAGE, lines);
        page = 0;

        stampHeader();

        final List<CustomerInvoice> sortedCustomers = centralBillInvoice.getCustomerInvoices().stream()
                .sorted(Comparator.comparing(CustomerInvoice::getSortSequence).thenComparing(CustomerInvoice::getId))
                .collect(Collectors.toList());

        for (CustomerInvoice customerInvoice : sortedCustomers) {
            if (line > TABLE_DETAIL_LINES_PER_PAGE - 2) stampHeader();
            // the above ' - 2' = customer row and invoice table headerTable row

            canvas.add(customerRow(customerInvoice.getCustomer()));
            line++;

            canvas.add(invoiceTableHeader());
            line++;

            final List<Invoice> sortedInvoices = customerInvoice.getInvoices().stream()
                    .sorted(Comparator.comparing(Invoice::getDeliveryDate)
                            .thenComparing(Invoice::getInvoiceId))
                    .collect(Collectors.toList());

            for (Invoice invoice : sortedInvoices) {
                if (line > TABLE_DETAIL_LINES_PER_PAGE) {
                    stampHeader();

                    canvas.add(customerRow(customerInvoice.getCustomer()));
                    line++;

                    canvas.add(invoiceTableHeader());
                    line++;
                }

                canvas.add(invoiceRow(invoice, customerInvoice.getCustomer().isExtendedInvoiceId()));
                line++;
            }

            canvas.add(totalDueRow(customerInvoice.getAmountDue()));
            line++;

            canvas.add(blankRow());
            line++;
        }

        canvas.add(remittanceDueRow(centralBillInvoice.getAmountDue()));

        canvas.close();
    }

    private void stampHeader() {
        PdfPage pdfPage = document.getPdfDocument().addNewPage();

        page++;

        RemitToCanvas remitToCanvas = new RemitToCanvas(centralBillInvoice.getCentralBill().getRemit().getContact());
        canvas = new Canvas(new PdfCanvas(pdfPage), RemitToCanvas.LOGO_RECTANGLE);
        canvas.add(remitToCanvas.logoTable());
        canvas = new Canvas(new PdfCanvas(pdfPage), RemitToCanvas.REMIT_TO_RECTANGLE);
        canvas.add(remitToCanvas.table());

        SubjectCanvas subjectCanvas = new SubjectCanvas("REMITTANCE",
                page,
                pages,
                centralBillInvoice.getCentralBill().getContact().getId(),
                0
        );
        canvas = new Canvas(new PdfCanvas(pdfPage), SubjectCanvas.INVOICE_SUBJECT_RECTANGLE);
        canvas.add(subjectCanvas.table());

        AddressCanvas billToCanvas = new AddressCanvas("bill to", centralBillInvoice.getCentralBill().getContact());
        canvas = new Canvas(new PdfCanvas(pdfPage), AddressCanvas.BILL_TO_RECTANGLE);
        canvas.add(billToCanvas.table());

        InstructionCanvas instructionCanvas = new InstructionCanvas(centralBillInvoice.getCentralBill().getRemit().getContact().getPhone());
        canvas = new Canvas(new PdfCanvas(pdfPage), InstructionCanvas.INSTRUCTION_RECTANGLE);
        canvas.add(instructionCanvas.table());

        canvas = new Canvas(new PdfCanvas(pdfPage), SUMMARY_TABLE_RECTANGLE);
        line = 0;
    }

    private Table customerRow(Customer customer) {
        return new Table(UnitValue.createPercentArray(SUMMARY_TABLE_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setFont(PdfEnvironment.getInstance().getFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph("" +
                                "[" + customer.getContact().getId() + "] " + StringUtils.normalizeSpace(customer.getContact().getName())))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph(StringUtils.isBlank(customer.getTerms().getName()) ? "" :
                                "terms are " + StringUtils.normalizeSpace(customer.getTerms().getName())))
                );
    }

    private Table invoiceTableHeader() {
        return new Table(UnitValue.createPercentArray(INVOICE_TABLE_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("invoice"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("delivered"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("total"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .add(new Paragraph(""))
                );
    }

    private Table invoiceRow(Invoice invoice, boolean isExtended) {
        return new Table(UnitValue.createPercentArray(INVOICE_TABLE_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getItemFont())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(isExtended ?
                                invoice.getHeader().getExtendedId() :
                                String.format("%07d", invoice.getHeader().getId())))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getItemFont())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(invoice.getHeader().getDeliveryDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getItemFont())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setFontColor(invoice.amountDue() < 0 ? Color.RED.asDeviceRgb() : Color.BLACK.asDeviceRgb())
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph(Number.CURRENCY.type.format(invoice.amountDue())))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .add(new Paragraph(""))
                );
    }

    private Table totalDueRow(Double totalDue) {
        return new Table(UnitValue.createPercentArray(INVOICE_TABLE_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 2)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph("TOTAL DUE"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getItemFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setFontColor(totalDue < 0 ? Color.RED.asDeviceRgb() : Color.BLACK.asDeviceRgb())
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph(Number.CURRENCY.type.format(totalDue)))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .add(new Paragraph(""))
                );
    }

    private Table blankRow() {
        return new Table(UnitValue.createPercentArray(SUMMARY_TABLE_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 4)
                        .setBorder(Border.NO_BORDER)
                        .setFontSize(FontSize.SMALL.asFloat)
                        .add(new Paragraph(""))
                );
    }

    private Table remittanceDueRow(Double remittanceDue) {
        return new Table(UnitValue.createPercentArray(INVOICE_TABLE_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 2)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontBold())
                        .setFontSize(FontSize.MEDIUM.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph("REMITTANCE DUE"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getItemFontBold())
                        .setFontSize(FontSize.MEDIUM.asFloat)
                        .setFontColor(remittanceDue < 0 ? Color.RED.asDeviceRgb() : Color.BLACK.asDeviceRgb())
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph(Number.CURRENCY.type.format(remittanceDue)))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .add(new Paragraph(""))
                );
    }
}
