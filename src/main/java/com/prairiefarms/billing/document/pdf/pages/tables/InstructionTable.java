package com.prairiefarms.billing.document.pdf.pages.tables;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.prairiefarms.billing.BillingEnvironment;
import com.prairiefarms.billing.document.pdf.utils.Color;
import com.prairiefarms.billing.document.pdf.utils.FontSize;

import java.io.IOException;

public class InstructionTable {

    public static final Rectangle INSTRUCTION_RECTANGLE = new Rectangle(360f, 580f, 220f, 100f);

    private static final float[] TABLE_COLUMNS = new float[]{220f};

    private final String remitToPhone;

    public InstructionTable(String remitToPhone) {
        this.remitToPhone = remitToPhone;
    }

    public Table detail() throws IOException {
        return new Table(UnitValue.createPercentArray(TABLE_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontBold())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("Instructions"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph("Make check(s) payable to: " + BillingEnvironment.getInstance().getCorporateName()))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph("Include invoice number(s) on your check(s)"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph("Questions? Call us at " + remitToPhone))
                );
    }
}
