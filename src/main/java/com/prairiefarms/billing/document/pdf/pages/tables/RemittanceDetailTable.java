package com.prairiefarms.billing.document.pdf.pages.tables;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.prairiefarms.billing.customer.Customer;
import com.prairiefarms.billing.document.pdf.utils.Color;
import com.prairiefarms.billing.document.pdf.utils.FontSize;
import com.prairiefarms.billing.document.pdf.utils.Number;
import com.prairiefarms.billing.invoice.Invoice;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class RemittanceDetailTable {

    private static final float[] SUMMARY_TABLE_COLUMNS = new float[]{270f, 270f};
    private static final float[] INVOICE_TABLE_COLUMNS = new float[]{100f, 80f, 90f, 272f};

    public RemittanceDetailTable() {
    }

    public Table customer(Customer customer) throws IOException {
        return new Table(UnitValue.createPercentArray(SUMMARY_TABLE_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setBackgroundColor(Color.LIGHT_GRAY.asDeviceRgb())
                        .setFont(TableBase.getFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph("" +
                                "[" + customer.getContact().getId() + "] " + StringUtils.normalizeSpace(customer.getContact().getName())))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setBackgroundColor(Color.LIGHT_GRAY.asDeviceRgb())
                        .setFont(TableBase.getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph(StringUtils.isBlank(customer.getTerms().getName()) ? "" :
                                "terms are " + StringUtils.normalizeSpace(customer.getTerms().getName())))
                );
    }

    public Table invoiceTableHead() throws IOException {
        return new Table(UnitValue.createPercentArray(INVOICE_TABLE_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("invoice"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("delivered"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("total"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .add(new Paragraph(""))
                );
    }

    public Table invoice(Invoice invoice, boolean isExtended) throws IOException {
        return new Table(UnitValue.createPercentArray(INVOICE_TABLE_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getItemFont())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(isExtended ?
                                invoice.getHeader().getExtendedId() :
                                String.format("%07d", invoice.getHeader().getId())))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getItemFont())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(invoice.getHeader().getDeliveryDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getItemFont())
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

    public Table customerAmountDue(Double totalDue) throws IOException {
        return new Table(UnitValue.createPercentArray(INVOICE_TABLE_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 2)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph("TOTAL DUE"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getItemFontBold())
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

    public Table blankRow() {
        return new Table(UnitValue.createPercentArray(SUMMARY_TABLE_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 4)
                        .setBorder(Border.NO_BORDER)
                        .setFontSize(FontSize.SMALL.asFloat)
                        .add(new Paragraph(""))
                );
    }

    public Table remittanceAmountDue(Double remittanceDue) throws IOException {
        return new Table(UnitValue.createPercentArray(INVOICE_TABLE_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 2)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontBold())
                        .setFontSize(FontSize.MEDIUM.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph("REMITTANCE DUE"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getItemFontBold())
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
