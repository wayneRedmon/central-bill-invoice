package com.prairiefarms.billing.document.xlsx.workbook;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class WorkbookEnvironment {

    public final float DEFAULT_ROW_HEIGHT_IN_POINTS = 18f;
    public final int REMITTANCE_SHEET_TO_COPY = 0;
    public final int REMITTANCE_ROW_TO_COPY = 13;
    public final int ITEM_SUMMARY_SHEET_TO_COPY = 1;
    public final int ITEM_SUMMARY_ROW_TO_COPY = 13;
    public final int INVOICE_SHEET_TO_COPY = 2;
    public final int INVOICE_ROW_TO_COPY = 16;
    public final CellCopyPolicy CELL_COPY_POLICY = new CellCopyPolicy.Builder().mergedRegions(true).cellFormula(false).cellStyle(false).cellValue(false).build();

    private static final String DOUBLE_VALUE_FORMAT = "_(* #,##0.00_);[RED]_(* \\(#,##0.00\\);_(* -??_);_(@_)";
    private static final String CURRENCY_VALUE_FORMAT = "_(* $#,##0.00_);[RED]_(* \\($#,##0.00\\);_(* -??_);_(@_)";
    private static final String INTEGER_VALUE_FORMAT = "_(* #,##0_);[RED]_(* \\(#,##0\\);_(* -??_);_(@_)";
    private static final String CUSTOM_PRICE_FORMAT = "_(* ##0.0000_);[RED]_(* \\(##0.0000\\);_(* -??_);_(@_)";
    private static final short SUBTOTAL_CELL_BACKGROUND = IndexedColors.LEMON_CHIFFON.getIndex();
    private static final XSSFColor TOTAL_CELL_BACKGROUND = new XSSFColor(new java.awt.Color(207, 231, 245), new DefaultIndexedColorMap());
    private static final String PROPORTIONAL_FONT_NAME = "Arial";
    private static final String MONOSPACE_FONT_NAME = "Courier New";
    private static final Short DEFAULT_FONT_SIZE = 10;

    private static XSSFWorkbook xssfWorkbook;

    private static XSSFCellStyle proportionalDefaultStyle;
    private static XSSFCellStyle proportionalCenterBold;
    private static XSSFCellStyle proportionalCenterBackground;
    private static XSSFCellStyle proportionalRightBold;

    private static XSSFCellStyle monospaceDefaultStyle;
    private static XSSFCellStyle monospaceCenterStyle;
    private static XSSFCellStyle monospaceTextRightStyle;
    private static XSSFCellStyle monospaceDoubleStyle;
    private static XSSFCellStyle monospaceIntegerStyle;
    private static XSSFCellStyle monospacePriceStyle;
    private static XSSFCellStyle monospacePromotionStyle;
    private static XSSFCellStyle monospaceCurrencyStyle;
    private static XSSFCellStyle monospaceTotalDoubleStyle;
    private static XSSFCellStyle monospaceSubtotalDoubleStyle;
    private static XSSFCellStyle monospaceTotalIntegerStyle;
    private static XSSFCellStyle monospaceSubtotalIntegerStyle;
    private static XSSFCellStyle monospaceTotalCurrencyStyle;
    private static XSSFCellStyle monospaceSubtotalCurrencyStyle;

    private static class SingletonHelper {
        private static final WorkbookEnvironment INSTANCE = new WorkbookEnvironment();
    }

    WorkbookEnvironment() {
    }

    public static WorkbookEnvironment getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public boolean init(XSSFWorkbook xssfWorkbook) {
        this.xssfWorkbook = xssfWorkbook;

        boolean ready = false;

        setProportionalStyle();
        setMonospaceStyle();

        return ready;
    }

    public XSSFCellStyle getProportionalDefaultStyle() {return proportionalDefaultStyle;}

    public XSSFCellStyle getProportionalCenterBold() {
        return proportionalCenterBold;
    }

    public XSSFCellStyle getProportionalCenterBackground() {
        return proportionalCenterBackground;
    }

    public XSSFCellStyle getProportionalRightBold() {
        return proportionalRightBold;
    }

    public XSSFCellStyle getMonospaceDefaultStyle() {return monospaceDefaultStyle;}

    public XSSFCellStyle getMonospaceCenterStyle() {return monospaceCenterStyle;}

    public XSSFCellStyle getMonospaceTextRightStyle(){return monospaceTextRightStyle;}

    public XSSFCellStyle getMonospaceDoubleStyle() {return monospaceDoubleStyle;}

    public XSSFCellStyle getMonospaceIntegerStyle() {return monospaceIntegerStyle;}

    public XSSFCellStyle getMonospacePriceStyle() {return monospacePriceStyle;}

    public XSSFCellStyle getMonospacePromotionStyle() {return monospacePromotionStyle;}

    public XSSFCellStyle getMonospaceCurrencyStyle() {return monospaceCurrencyStyle;}

    public XSSFCellStyle getMonospaceTotalDoubleStyle() {return monospaceTotalDoubleStyle;}

    public XSSFCellStyle getMonospaceSubtotalDoubleStyle() {return monospaceSubtotalDoubleStyle;}

    public XSSFCellStyle getMonospaceTotalIntegerStyle() {return monospaceTotalIntegerStyle;}

    public XSSFCellStyle getMonospaceSubtotalIntegerStyle() {return monospaceSubtotalIntegerStyle;}

    public XSSFCellStyle getMonospaceTotalCurrencyStyle() {return monospaceTotalCurrencyStyle;}

    public XSSFCellStyle getMonospaceSubtotalCurrencyStyle() {return monospaceSubtotalCurrencyStyle;}

    private static void setProportionalStyle() {
        XSSFFont proportionalFontNormal = xssfWorkbook.createFont();
        proportionalFontNormal.setFontName(PROPORTIONAL_FONT_NAME);
        proportionalFontNormal.setFontHeightInPoints(DEFAULT_FONT_SIZE);
        proportionalFontNormal.setBold(false);

        XSSFFont proportionalFontBold = xssfWorkbook.createFont();
        proportionalFontBold.setFontName(PROPORTIONAL_FONT_NAME);
        proportionalFontBold.setFontHeightInPoints(DEFAULT_FONT_SIZE);
        proportionalFontBold.setBold(true);

        proportionalDefaultStyle = xssfWorkbook.createCellStyle();
        proportionalDefaultStyle.setFont(proportionalFontNormal);
        proportionalDefaultStyle.setAlignment(HorizontalAlignment.LEFT);
        proportionalDefaultStyle.setBorderBottom(BorderStyle.NONE);
        proportionalDefaultStyle.setBorderLeft(BorderStyle.NONE);
        proportionalDefaultStyle.setBorderRight(BorderStyle.NONE);

        proportionalCenterBold = xssfWorkbook.createCellStyle();
        proportionalCenterBold.cloneStyleFrom(proportionalDefaultStyle);
        proportionalCenterBold.setFont(proportionalFontBold);
        proportionalCenterBold.setAlignment(HorizontalAlignment.CENTER);
        proportionalCenterBold.setVerticalAlignment(VerticalAlignment.CENTER);
        proportionalCenterBold.setBorderRight(BorderStyle.THIN);
        proportionalCenterBold.setRightBorderColor(IndexedColors.BLACK.getIndex());

        proportionalCenterBackground = xssfWorkbook.createCellStyle();
        proportionalCenterBackground.cloneStyleFrom(proportionalCenterBold);
        proportionalCenterBackground.setBorderLeft(BorderStyle.THIN);
        proportionalCenterBackground.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        proportionalCenterBackground.setBorderRight(BorderStyle.THIN);
        proportionalCenterBackground.setRightBorderColor(IndexedColors.BLACK.getIndex());
        proportionalCenterBackground.setBorderTop(BorderStyle.THIN);
        proportionalCenterBackground.setTopBorderColor(IndexedColors.BLACK.getIndex());
        proportionalCenterBackground.setBorderBottom(BorderStyle.THIN);
        proportionalCenterBackground.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        proportionalCenterBackground.setFillForegroundColor(TOTAL_CELL_BACKGROUND);
        proportionalCenterBackground.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        proportionalRightBold = xssfWorkbook.createCellStyle();
        proportionalRightBold.cloneStyleFrom(proportionalDefaultStyle);
        proportionalRightBold.setFont(proportionalFontBold);
        proportionalRightBold.setBorderRight(BorderStyle.THIN);
        proportionalRightBold.setRightBorderColor(IndexedColors.BLACK.getIndex());
        proportionalRightBold.setVerticalAlignment(VerticalAlignment.BOTTOM);
        proportionalRightBold.setAlignment(HorizontalAlignment.RIGHT);
    }

    private static void setMonospaceStyle() {
        XSSFFont monospaceFontNormal = xssfWorkbook.createFont();
        monospaceFontNormal.setFontName(MONOSPACE_FONT_NAME);
        monospaceFontNormal.setFontHeightInPoints(DEFAULT_FONT_SIZE);
        monospaceFontNormal.setBold(false);

        XSSFFont monospaceFontBold = xssfWorkbook.createFont();
        monospaceFontBold.setFontName(MONOSPACE_FONT_NAME);
        monospaceFontBold.setFontHeightInPoints(DEFAULT_FONT_SIZE);
        monospaceFontBold.setBold(true);

        monospaceDefaultStyle = xssfWorkbook.createCellStyle();
        monospaceDefaultStyle.setFont(monospaceFontNormal);
        monospaceDefaultStyle.setAlignment(HorizontalAlignment.LEFT);
        monospaceDefaultStyle.setBorderBottom(BorderStyle.THIN);
        monospaceDefaultStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        monospaceDefaultStyle.setBorderTop(BorderStyle.THIN);
        monospaceDefaultStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        monospaceDefaultStyle.setBorderRight(BorderStyle.THIN);
        monospaceDefaultStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        monospaceDefaultStyle.setBorderLeft(BorderStyle.THIN);
        monospaceDefaultStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());

        monospaceCenterStyle = xssfWorkbook.createCellStyle();
        monospaceCenterStyle.cloneStyleFrom(monospaceDefaultStyle);
        monospaceCenterStyle.setAlignment(HorizontalAlignment.CENTER);

        monospaceTextRightStyle = xssfWorkbook.createCellStyle();
        monospaceTextRightStyle.cloneStyleFrom(monospaceDefaultStyle);
        monospaceTextRightStyle.setAlignment(HorizontalAlignment.RIGHT);

        monospaceDoubleStyle = xssfWorkbook.createCellStyle();
        monospaceDoubleStyle.cloneStyleFrom(monospaceDefaultStyle);
        monospaceDoubleStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(DOUBLE_VALUE_FORMAT));
        monospaceDoubleStyle.setAlignment(HorizontalAlignment.RIGHT);

        monospaceSubtotalDoubleStyle = xssfWorkbook.createCellStyle();
        monospaceSubtotalDoubleStyle.cloneStyleFrom(monospaceDefaultStyle);
        monospaceSubtotalDoubleStyle.setFont(monospaceFontBold);
        monospaceSubtotalDoubleStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(DOUBLE_VALUE_FORMAT));
        monospaceSubtotalDoubleStyle.setAlignment(HorizontalAlignment.RIGHT);
        monospaceSubtotalDoubleStyle.setFillForegroundColor(SUBTOTAL_CELL_BACKGROUND);
        monospaceSubtotalDoubleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        monospaceTotalDoubleStyle = xssfWorkbook.createCellStyle();
        monospaceTotalDoubleStyle.cloneStyleFrom(monospaceDefaultStyle);
        monospaceTotalDoubleStyle.setFont(monospaceFontBold);
        monospaceTotalDoubleStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(DOUBLE_VALUE_FORMAT));
        monospaceTotalDoubleStyle.setAlignment(HorizontalAlignment.RIGHT);
        monospaceTotalDoubleStyle.setFillForegroundColor(TOTAL_CELL_BACKGROUND);
        monospaceTotalDoubleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        monospaceIntegerStyle = xssfWorkbook.createCellStyle();
        monospaceIntegerStyle.cloneStyleFrom(monospaceDefaultStyle);
        monospaceIntegerStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(INTEGER_VALUE_FORMAT));
        monospaceIntegerStyle.setAlignment(HorizontalAlignment.RIGHT);

        monospaceSubtotalIntegerStyle = xssfWorkbook.createCellStyle();
        monospaceSubtotalIntegerStyle.cloneStyleFrom(monospaceDefaultStyle);
        monospaceSubtotalIntegerStyle.setFont(monospaceFontBold);
        monospaceSubtotalIntegerStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(INTEGER_VALUE_FORMAT));
        monospaceSubtotalIntegerStyle.setAlignment(HorizontalAlignment.RIGHT);
        monospaceSubtotalIntegerStyle.setFillForegroundColor(SUBTOTAL_CELL_BACKGROUND);
        monospaceSubtotalIntegerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        monospaceTotalIntegerStyle = xssfWorkbook.createCellStyle();
        monospaceTotalIntegerStyle.cloneStyleFrom(monospaceDefaultStyle);
        monospaceTotalIntegerStyle.setFont(monospaceFontBold);
        monospaceTotalIntegerStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(INTEGER_VALUE_FORMAT));
        monospaceTotalIntegerStyle.setAlignment(HorizontalAlignment.RIGHT);
        monospaceTotalIntegerStyle.setFillForegroundColor(TOTAL_CELL_BACKGROUND);
        monospaceTotalIntegerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        monospacePriceStyle = xssfWorkbook.createCellStyle();
        monospacePriceStyle.cloneStyleFrom(monospaceDefaultStyle);
        monospacePriceStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(CUSTOM_PRICE_FORMAT));
        monospacePriceStyle.setAlignment(HorizontalAlignment.RIGHT);

        monospacePromotionStyle = xssfWorkbook.createCellStyle();
        monospacePromotionStyle.cloneStyleFrom(monospaceDefaultStyle);
        monospacePromotionStyle.setFillBackgroundColor(IndexedColors.CORAL.getIndex());
        monospacePromotionStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        monospaceCurrencyStyle = xssfWorkbook.createCellStyle();
        monospaceCurrencyStyle.cloneStyleFrom(monospaceDefaultStyle);
        monospaceCurrencyStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(CURRENCY_VALUE_FORMAT));
        monospaceCurrencyStyle.setAlignment(HorizontalAlignment.RIGHT);

        monospaceSubtotalCurrencyStyle = xssfWorkbook.createCellStyle();
        monospaceSubtotalCurrencyStyle.cloneStyleFrom(monospaceDefaultStyle);
        monospaceSubtotalCurrencyStyle.setFont(monospaceFontBold);
        monospaceSubtotalCurrencyStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(CURRENCY_VALUE_FORMAT));
        monospaceSubtotalCurrencyStyle.setAlignment(HorizontalAlignment.RIGHT);
        monospaceSubtotalCurrencyStyle.setFillForegroundColor(SUBTOTAL_CELL_BACKGROUND);
        monospaceSubtotalCurrencyStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        monospaceTotalCurrencyStyle = xssfWorkbook.createCellStyle();
        monospaceTotalCurrencyStyle.cloneStyleFrom(monospaceDefaultStyle);
        monospaceTotalCurrencyStyle.setFont(monospaceFontBold);
        monospaceTotalCurrencyStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(CURRENCY_VALUE_FORMAT));
        monospaceTotalCurrencyStyle.setAlignment(HorizontalAlignment.RIGHT);
        monospaceTotalCurrencyStyle.setFillForegroundColor(TOTAL_CELL_BACKGROUND);
        monospaceTotalCurrencyStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }
}
