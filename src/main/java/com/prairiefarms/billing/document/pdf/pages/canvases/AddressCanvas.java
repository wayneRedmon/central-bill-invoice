package com.prairiefarms.billing.document.pdf.pages.canvases;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.prairiefarms.billing.document.pdf.PdfEnvironment;
import com.prairiefarms.billing.document.pdf.utils.Color;
import com.prairiefarms.billing.document.pdf.utils.FontSize;
import com.prairiefarms.billing.utils.Contact;
import org.apache.commons.lang3.StringUtils;

public class AddressCanvas {

    public static final Rectangle BILL_TO_RECTANGLE = new Rectangle(36f, 580f, 210f, 100f);
    public static final Rectangle SHIP_TO_RECTANGLE = new Rectangle(370f, 580f, 210f, 100f);

    private static final float[] TABLE_COLUMNS = new float[]{210f};

    private final String title;
    private final Contact contact;

    public AddressCanvas(String title, Contact contact) {
        this.title = title;
        this.contact = contact;
    }

    public Table table() {
        return new Table(UnitValue.createPercentArray(TABLE_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph(StringUtils.normalizeSpace(title)))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph(StringUtils.normalizeSpace(contact.getName())))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph(contact.getStreet()))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph(contact.getAddress()))
                );
    }
}
