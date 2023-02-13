package com.prairiefarms.billing.document.pdf.pages.tables;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.prairiefarms.billing.document.pdf.utils.Color;
import com.prairiefarms.billing.document.pdf.utils.FontSize;
import com.prairiefarms.billing.document.pdf.utils.Number;
import com.prairiefarms.billing.invoice.item.Item;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class ItemTable {

    public static final Rectangle ITEM_TABLE_RECTANGLE = new Rectangle(36f, 36f, 540f, 536f);
    public static final Rectangle ITEM_SUMMARY_TABLE_RECTANGLE = new Rectangle(36f, 36f, 540f, 568f);

    private static final float[] ITEM_TABLE_COLUMNS = new float[]{.75f, 2.25f, 1f, 1f, 1f};

    public ItemTable() {
    }

    public Table header() throws IOException {
        return new Table(UnitValue.createPercentArray(ITEM_TABLE_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 2)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("item"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("quantity"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("price each"))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getFontDefault())
                        .setFontSize(FontSize.LABEL.asFloat)
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("total"))
                );
    }

    public Table itemTable(Item item) throws IOException {
        Paragraph priceEachParagraph = new Paragraph(item.isPromotion() ? "(P)" : "");
        priceEachParagraph.add(new Tab());
        priceEachParagraph.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
        priceEachParagraph.add(Number.PRICE.type.format(item.getPriceEach()));

        return new Table(UnitValue.createPercentArray(ITEM_TABLE_COLUMNS))
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout()
                .addCell(new Cell(1, 1)
                        .setBorder(TableBase.LABEL_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getItemFont())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph(item.getSalesType().equals("A") ? String.valueOf(item.getId()) : item.getIdAsAccount()))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(TableBase.LABEL_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getItemFont())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph(StringUtils.normalizeSpace(item.getName())))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(TableBase.LABEL_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getItemFont())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setFontColor(item.getQuantity() < 0 ? Color.RED.asDeviceRgb() : Color.BLACK.asDeviceRgb())
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph(Number.INTEGER.type.format(item.getQuantity())))
                )
                .addCell(new Cell(1, 1)
                        .setBorder(TableBase.LABEL_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getItemFont())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(priceEachParagraph)
                )
                .addCell(new Cell(1, 1)
                        .setBackgroundColor(Color.BLUE.asDeviceRgb())
                        .setBorder(TableBase.LABEL_BORDER)
                        .setPadding(TableBase.DEFAULT_CELL_PADDING)
                        .setFont(TableBase.getItemFont())
                        .setFontSize(FontSize.SMALL.asFloat)
                        .setFontColor(item.getExtension() < 0 ? Color.RED.asDeviceRgb() : Color.BLACK.asDeviceRgb())
                        .setTextAlignment(TextAlignment.RIGHT)
                        .add(new Paragraph(Number.CURRENCY.type.format(item.getExtension())))
                );
    }
}
