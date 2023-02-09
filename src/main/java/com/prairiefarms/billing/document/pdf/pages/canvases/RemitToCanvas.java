package com.prairiefarms.billing.document.pdf.pages.canvases;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.prairiefarms.billing.document.pdf.PdfEnvironment;
import com.prairiefarms.billing.document.pdf.utils.FontSize;
import com.prairiefarms.billing.utils.Contact;
import org.apache.commons.lang3.StringUtils;

public class RemitToCanvas {

    public static final Rectangle LOGO_RECTANGLE = new Rectangle(36f, 656f, 100f, 100f);
    public static final Rectangle REMIT_TO_RECTANGLE = new Rectangle(0f, 656f, 548f, 100f);

    private final Contact contact;

    public RemitToCanvas(Contact contact) {
        this.contact = contact;
    }

    public Table logoTable() {
        final float[] LOGO_TABLE_COLUMNS = new float[]{100f};

        return new Table(UnitValue.createPercentArray(LOGO_TABLE_COLUMNS)).setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(
                        new Cell(1, 1)
                                .setPadding(0f)
                                .setBorder(Border.NO_BORDER)
                                .add(PdfEnvironment.getInstance().getLogoImage().setAutoScale(true))
                );
    }

    public Table table() {
        final float[] REMIT_TO_COLUMNS = new float[]{548f};

        return new Table(UnitValue.createPercentArray(REMIT_TO_COLUMNS)).setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontBold())
                        .setFontSize(FontSize.LARGE.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(StringUtils.normalizeSpace(contact.getName())))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(StringUtils.normalizeSpace(contact.getStreet())))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(contact.getAddress()))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(contact.getPhone()))
                );
    }
}
