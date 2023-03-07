package com.prairiefarms.billing.document.pdf.pages.tables;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.prairiefarms.billing.document.pdf.utils.Color;
import com.prairiefarms.billing.document.pdf.utils.FontSize;
import com.prairiefarms.billing.document.pdf.utils.Number;
import com.prairiefarms.billing.invoice.Invoice;

import java.io.IOException;

public class InvoiceTotalTable {

    private static final float[] INVOICE_TOTAL_COLUMNS = new float[]{5f, 1f};

    private final Invoice invoice;

    public InvoiceTotalTable(Invoice invoice) {
        this.invoice = invoice;
    }

    public Table detail() throws IOException {
        return new Table(UnitValue.createPercentArray(INVOICE_TOTAL_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph("SUBTOTAL"))
                )
                .addCell(new Cell(1, 1)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setBorder(TableBase.LABEL_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getItemFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setFontColor(invoice.subTotal() < 0 ? Color.RED.asDeviceRgb() : Color.BLACK.asDeviceRgb())
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph(Number.CURRENCY.type.format(invoice.subTotal())))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph("APPLIED"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(TableBase.LABEL_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getItemFont())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setFontColor(invoice.getAppliedAmount() < 0 ? Color.RED.asDeviceRgb() : Color.BLACK.asDeviceRgb())
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph(Number.CURRENCY.type.format(invoice.getAppliedAmount())))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph((invoice.getDiscountRate() == 0 ? "" : invoice.getDiscountRate() + "% ") + "DISCOUNT"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(TableBase.LABEL_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getItemFont())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setFontColor(invoice.getDiscountAmount() < 0 ? Color.RED.asDeviceRgb() : Color.BLACK.asDeviceRgb())
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph(Number.CURRENCY.type.format(invoice.getDiscountAmount())))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph((invoice.getTaxRate() == 0 ? "" : invoice.getTaxRate() + "% ") + "TAX"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(TableBase.LABEL_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getItemFont())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setFontColor(invoice.getTaxAmount() < 0 ? Color.RED.asDeviceRgb() : Color.BLACK.asDeviceRgb())
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph(Number.CURRENCY.type.format(invoice.getTaxAmount())))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontBold())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph("TOTAL DUE"))
                )
                .addCell(new Cell(1, 1)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setBorder(TableBase.LABEL_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getItemFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setFontColor(invoice.amountDue() < 0 ? Color.RED.asDeviceRgb() : Color.BLACK.asDeviceRgb())
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph(Number.CURRENCY.type.format(invoice.amountDue())))
                );
    }
}
