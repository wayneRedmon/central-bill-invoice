package com.prairiefarms.billing.document.pdf.pages.canvases;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.prairiefarms.billing.BillingEnvironment;
import com.prairiefarms.billing.document.pdf.PdfEnvironment;
import com.prairiefarms.billing.document.pdf.utils.Color;
import com.prairiefarms.billing.document.pdf.utils.FontSize;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SubjectCanvas {

    public static final Rectangle INVOICE_SUBJECT_RECTANGLE = new Rectangle(440f, 656f, 165f, 100f);

    private static final float[] TABLE_COLUMNS = new float[]{55f, 5f, 5f, 100f};

    private final String title;
    private final int page;
    private final int pages;
    private final int billToId;
    private final int shipToId;

    public SubjectCanvas(String title, int page, int pages,int billToId, int shipToId) {
        this.title = title;
        this.page = page;
        this.pages = pages;
        this.billToId = billToId;
        this.shipToId = shipToId;
    }

    public Table table() {
        return new Table(UnitValue.createPercentArray(TABLE_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 4)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph(StringUtils.normalizeSpace(title).toUpperCase(Locale.ROOT)))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .add(new Paragraph("account"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .add(new Paragraph())
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .add(new Paragraph())
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph(
                                String.format("%03d", BillingEnvironment.getInstance().getDairyId()) +
                                        "-" +
                                        String.format("%03d", billToId) +
                                        (ObjectUtils.isEmpty(shipToId) || shipToId == 0 ? "" : "-" + String.format("%05d", shipToId))
                        ))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .add(new Paragraph("invoice date"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .add(new Paragraph())
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .add(new Paragraph())
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph(BillingEnvironment.getInstance().getBillingDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .add(new Paragraph("page"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .add(new Paragraph())
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .add(new Paragraph())
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph(page + " of " + pages))
                );
    }
}
