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
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SaleCanvas {

    public static final Rectangle SALE_RECTANGLE = new Rectangle(36f, 36f, 540f, 568f);

    private static final float[] TABLE_COLUMNS = new float[]{2.5f, 2f, 1f};

    private final String purchaseOrder;
    private final String salesperson;
    private final LocalDate deliveryDate;

    public SaleCanvas(String purchaseOrder,
                      String salesperson,
                      LocalDate deliveryDate) {
        this.purchaseOrder = purchaseOrder;
        this.salesperson = salesperson;
        this.deliveryDate = deliveryDate;
    }

    public Table table() {
        return new Table(UnitValue.createPercentArray(TABLE_COLUMNS)).setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("purchase order"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("salesperson"))
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
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(StringUtils.normalizeSpace(purchaseOrder)))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(StringUtils.normalizeSpace(salesperson)))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(PdfEnvironment.getInstance().DEFAULT_CELL_PADDING)
                        .setFont(PdfEnvironment.getInstance().getFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(deliveryDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))))
                );
    }
}
