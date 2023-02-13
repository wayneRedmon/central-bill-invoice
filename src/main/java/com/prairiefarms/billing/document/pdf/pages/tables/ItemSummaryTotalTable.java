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

import java.io.IOException;

public class ItemSummaryTotalTable {

    private static final float[] ITEM_SUMMARY_TOTAL_COLUMNS = new float[]{5f, 1f};

    private final double remittanceDue;

    public ItemSummaryTotalTable(double remittanceDue) {
        this.remittanceDue = remittanceDue;
    }

    public Table detail() throws IOException {
        return new Table(UnitValue.createPercentArray(ITEM_SUMMARY_TOTAL_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph("TOTAL"))
                )
                .addCell(new Cell(1, 1)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setBorder(TableBase.LABEL_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getItemFontBold())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setFontColor(remittanceDue < 0 ? Color.RED.asDeviceRgb() : Color.BLACK.asDeviceRgb())
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph(Number.CURRENCY.type.format(remittanceDue)))
                );
    }
}
