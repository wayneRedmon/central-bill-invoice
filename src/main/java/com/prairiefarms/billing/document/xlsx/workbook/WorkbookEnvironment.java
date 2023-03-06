package com.prairiefarms.billing.document.xlsx.workbook;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class WorkbookEnvironment {

    public final float DEFAULT_ROW_HEIGHT_IN_POINTS = 18f;
    public final int REMITTANCE_SHEET_TO_COPY = 0;
    public final int REMITTANCE_ROW_TO_COPY = 11;
    public final int ITEM_SUMMARY_SHEET_TO_COPY = 1;
    public final int ITEM_SUMMARY_ROW_TO_COPY = 11;
    public final int INVOICE_SHEET_TO_COPY = 2;
    public final int INVOICE_ROW_TO_COPY = 16;
    public final CellCopyPolicy CELL_COPY_POLICY = new CellCopyPolicy.Builder().mergedRegions(true).cellFormula(false).cellStyle(false).cellValue(false).build();

    private static final String DOUBLE_VALUE_FORMAT = "_(* #,##0.00_);[RED]_(* \\(#,##0.00\\);_(* -??_);_(@_)";
    private static final String CURRENCY_VALUE_FORMAT = "_(* $#,##0.00_);[RED]_(* \\($#,##0.00\\);_(* -??_);_(@_)";
    private static final String INTEGER_VALUE_FORMAT = "_(* #,##0_);[RED]_(* \\(#,##0\\);_(* -??_);_(@_)";
    private static final String CUSTOM_PRICE_FORMAT = "_(* ##0.0000_);[RED]_(* \\(##0.0000\\);_(* -??_);_(@_)";
    private static final short SUBTOTAL_CELL_BACKGROUND = IndexedColors.LEMON_CHIFFON.getIndex();
    private static final XSSFColor TOTAL_CELL_BACKGROUND = new XSSFColor(new java.awt.Color(207, 231, 245), new DefaultIndexedColorMap());
    private static final String PROPORTIONAL_FONT_NAME = "Helvetica";
    private static final String MONOSPACE_FONT_NAME = "Courier";
    private static final Short DEFAULT_FONT_SIZE = 10;

    private static XSSFWorkbook xssfWorkbook;

    private static XSSFCellStyle dairyAddressStyle;
    private static XSSFCellStyle pageInformationStyle;
    private static XSSFCellStyle contactStyle;
    private static XSSFCellStyle instructionStyle;
    private static XSSFCellStyle customerColumnStyle;
    private static XSSFCellStyle invoiceColumnStyle;
    private static XSSFCellStyle deliveryDateColumnStyle;
    private static XSSFCellStyle pointsColumnStyle;
    private static XSSFCellStyle quantityColumnStyle;
    private static XSSFCellStyle totalColumnStyle;
    private static XSSFCellStyle dueByDateColumnStyle;
    private static XSSFCellStyle pointsSubtotalStyle;
    private static XSSFCellStyle quantitySubtotalStyle;
    private static XSSFCellStyle amountDueSubtotalStyle;
    private static XSSFCellStyle pointsTotalStyle;
    private static XSSFCellStyle quantityTotalStyle;
    private static XSSFCellStyle amountDueTotalStyle;
    private static XSSFCellStyle itemIdStyle;
    private static XSSFCellStyle itemNameStyle;
    private static XSSFCellStyle priceEachStyle;
    private static XSSFCellStyle promotionPriceStyle;

    private static XSSFCellStyle proportionalDefaultStyle;
    private static XSSFCellStyle proportionalCenterBold;
    private static XSSFCellStyle proportionalCenterBackground;
    private static XSSFCellStyle proportionalRightBold;

    private static XSSFCellStyle monospaceDefaultStyle;
    private static XSSFCellStyle monospaceDetailDateStyle;
    private static XSSFCellStyle monospaceInstructionStyle;
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

    public static void init(XSSFWorkbook xssfWorkbook) {
        WorkbookEnvironment.xssfWorkbook = xssfWorkbook;

        setProportionalStyle();
        setMonospaceStyle();
    }

    public XSSFCellStyle getDairyAddressStyle() {
        return dairyAddressStyle;
    }

    public XSSFCellStyle getPageInformationStyle() {
        return pageInformationStyle;
    }

    public XSSFCellStyle getContactStyle() {
        return contactStyle;
    }

    public XSSFCellStyle getInstructionStyle() {
        return instructionStyle;
    }

    public XSSFCellStyle getCustomerColumnStyle() {
        return customerColumnStyle;
    }

    public XSSFCellStyle getInvoiceColumnStyle() {
        return invoiceColumnStyle;
    }

    public XSSFCellStyle getDeliveryDateColumnStyle() {
        return deliveryDateColumnStyle;
    }

    public XSSFCellStyle getPointsColumnStyle() {
        return pointsColumnStyle;
    }

    public XSSFCellStyle getPriceEachStyle() {
        return priceEachStyle;
    }

    public XSSFCellStyle getPromotionPriceStyle() {
        return promotionPriceStyle;
    }

    public XSSFCellStyle getQuantityColumnStyle() {
        return quantityColumnStyle;
    }

    public XSSFCellStyle getTotalColumnStyle() {
        return totalColumnStyle;
    }

    public XSSFCellStyle getDueByDateColumnStyle() {
        return dueByDateColumnStyle;
    }

    public XSSFCellStyle getPointsSubtotalStyle() {
        return pointsSubtotalStyle;
    }

    public XSSFCellStyle getQuantitySubtotalStyle() {
        return quantitySubtotalStyle;
    }

    public XSSFCellStyle getAmountDueSubtotalStyle() {
        return amountDueSubtotalStyle;
    }

    public XSSFCellStyle getPointsTotalStyle() {
        return pointsTotalStyle;
    }

    public XSSFCellStyle getQuantityTotalStyle() {
        return quantityTotalStyle;
    }

    public XSSFCellStyle getAmountDueTotalStyle() {
        return amountDueTotalStyle;
    }

    public XSSFCellStyle getItemIdStyle() {
        return itemIdStyle;
    }

    public XSSFCellStyle getItemNameStyle() {
        return itemNameStyle;
    }

    //todo: remove unused styles

    public XSSFCellStyle getProportionalDefaultStyle() {
        return proportionalDefaultStyle;
    }

    public XSSFCellStyle getProportionalCenterBold() {
        return proportionalCenterBold;
    }

    public XSSFCellStyle getProportionalCenterBackground() {
        return proportionalCenterBackground;
    }

    public XSSFCellStyle getProportionalRightBold() {
        return proportionalRightBold;
    }

    public XSSFCellStyle getMonospaceDefaultStyle() {
        return monospaceDefaultStyle;
    }

    public XSSFCellStyle getMonospaceTextRightStyle() {
        return monospaceTextRightStyle;
    }

    public XSSFCellStyle getMonospaceDoubleStyle() {
        return monospaceDoubleStyle;
    }

    public XSSFCellStyle getMonospaceIntegerStyle() {
        return monospaceIntegerStyle;
    }

    public XSSFCellStyle getMonospacePriceStyle() {
        return monospacePriceStyle;
    }

    public XSSFCellStyle getMonospacePromotionStyle() {
        return monospacePromotionStyle;
    }

    public XSSFCellStyle getMonospaceCurrencyStyle() {
        return monospaceCurrencyStyle;
    }

    public XSSFCellStyle getMonospaceTotalDoubleStyle() {
        return monospaceTotalDoubleStyle;
    }

    public XSSFCellStyle getMonospaceTotalIntegerStyle() {
        return monospaceTotalIntegerStyle;
    }

    public XSSFCellStyle getMonospaceTotalCurrencyStyle() {
        return monospaceTotalCurrencyStyle;
    }

    private static void setProportionalStyle() {
        XSSFFont proportionalFontNormal = xssfWorkbook.createFont();
        proportionalFontNormal.setFontName(PROPORTIONAL_FONT_NAME);
        proportionalFontNormal.setFontHeightInPoints(DEFAULT_FONT_SIZE);
        proportionalFontNormal.setBold(false);

        XSSFFont proportionalFontBold = xssfWorkbook.createFont();
        proportionalFontBold.setFontName(PROPORTIONAL_FONT_NAME);
        proportionalFontBold.setFontHeightInPoints(DEFAULT_FONT_SIZE);
        proportionalFontBold.setBold(true);

        XSSFFont monospaceFontNormal = xssfWorkbook.createFont();
        monospaceFontNormal.setFontName(MONOSPACE_FONT_NAME);
        monospaceFontNormal.setFontHeightInPoints(DEFAULT_FONT_SIZE);
        monospaceFontNormal.setBold(false);

        XSSFFont monospaceFontBold = xssfWorkbook.createFont();
        monospaceFontBold.setFontName(MONOSPACE_FONT_NAME);
        monospaceFontBold.setFontHeightInPoints(DEFAULT_FONT_SIZE);
        monospaceFontBold.setBold(true);

        dairyAddressStyle = xssfWorkbook.createCellStyle();
        dairyAddressStyle.setFont(proportionalFontBold);
        dairyAddressStyle.setAlignment(HorizontalAlignment.CENTER);
        dairyAddressStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dairyAddressStyle.setBorderBottom(BorderStyle.NONE);
        dairyAddressStyle.setBorderLeft(BorderStyle.NONE);
        dairyAddressStyle.setBorderRight(BorderStyle.NONE);

        pageInformationStyle = xssfWorkbook.createCellStyle();
        pageInformationStyle.setFont(proportionalFontBold);
        pageInformationStyle.setAlignment(HorizontalAlignment.CENTER);
        pageInformationStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        pageInformationStyle.setBorderLeft(BorderStyle.THIN);
        pageInformationStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        pageInformationStyle.setBorderRight(BorderStyle.THIN);
        pageInformationStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        pageInformationStyle.setBorderTop(BorderStyle.THIN);
        pageInformationStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        pageInformationStyle.setBorderBottom(BorderStyle.THIN);
        pageInformationStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        pageInformationStyle.setFillForegroundColor(TOTAL_CELL_BACKGROUND);
        pageInformationStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        contactStyle = xssfWorkbook.createCellStyle();
        contactStyle.setFont(proportionalFontNormal);
        contactStyle.setAlignment(HorizontalAlignment.LEFT);
        contactStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contactStyle.setBorderBottom(BorderStyle.NONE);
        contactStyle.setBorderLeft(BorderStyle.NONE);
        contactStyle.setBorderRight(BorderStyle.NONE);

        instructionStyle = xssfWorkbook.createCellStyle();
        instructionStyle.setFont(proportionalFontBold);
        instructionStyle.setAlignment(HorizontalAlignment.CENTER);
        instructionStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        instructionStyle.setBorderLeft(BorderStyle.THIN);
        instructionStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        instructionStyle.setBorderRight(BorderStyle.THIN);
        instructionStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

        customerColumnStyle = xssfWorkbook.createCellStyle();
        customerColumnStyle.setFont(proportionalFontBold);
        customerColumnStyle.setAlignment(HorizontalAlignment.LEFT);
        customerColumnStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        customerColumnStyle.setBorderLeft(BorderStyle.THIN);
        customerColumnStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        customerColumnStyle.setBorderRight(BorderStyle.THIN);
        customerColumnStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        customerColumnStyle.setBorderTop(BorderStyle.THIN);
        customerColumnStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        customerColumnStyle.setBorderBottom(BorderStyle.THIN);
        customerColumnStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

        invoiceColumnStyle = xssfWorkbook.createCellStyle();
        invoiceColumnStyle.setFont(monospaceFontNormal);
        invoiceColumnStyle.setAlignment(HorizontalAlignment.RIGHT);
        invoiceColumnStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        invoiceColumnStyle.setBorderBottom(BorderStyle.THIN);
        invoiceColumnStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        invoiceColumnStyle.setBorderTop(BorderStyle.THIN);
        invoiceColumnStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        invoiceColumnStyle.setBorderRight(BorderStyle.THIN);
        invoiceColumnStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        invoiceColumnStyle.setBorderLeft(BorderStyle.THIN);
        invoiceColumnStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

        deliveryDateColumnStyle = xssfWorkbook.createCellStyle();
        deliveryDateColumnStyle.setFont(monospaceFontNormal);
        deliveryDateColumnStyle.setAlignment(HorizontalAlignment.CENTER);
        deliveryDateColumnStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        deliveryDateColumnStyle.setBorderBottom(BorderStyle.THIN);
        deliveryDateColumnStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        deliveryDateColumnStyle.setBorderTop(BorderStyle.THIN);
        deliveryDateColumnStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        deliveryDateColumnStyle.setBorderRight(BorderStyle.THIN);
        deliveryDateColumnStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        deliveryDateColumnStyle.setBorderLeft(BorderStyle.THIN);
        deliveryDateColumnStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

        pointsColumnStyle = xssfWorkbook.createCellStyle();
        pointsColumnStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(DOUBLE_VALUE_FORMAT));
        pointsColumnStyle.setFont(monospaceFontNormal);
        pointsColumnStyle.setAlignment(HorizontalAlignment.RIGHT);
        pointsColumnStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        pointsColumnStyle.setBorderBottom(BorderStyle.THIN);
        pointsColumnStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        pointsColumnStyle.setBorderTop(BorderStyle.THIN);
        pointsColumnStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        pointsColumnStyle.setBorderRight(BorderStyle.THIN);
        pointsColumnStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        pointsColumnStyle.setBorderLeft(BorderStyle.THIN);
        pointsColumnStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

        quantityColumnStyle = xssfWorkbook.createCellStyle();
        quantityColumnStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(INTEGER_VALUE_FORMAT));
        quantityColumnStyle.setFont(monospaceFontNormal);
        quantityColumnStyle.setAlignment(HorizontalAlignment.RIGHT);
        quantityColumnStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        quantityColumnStyle.setBorderBottom(BorderStyle.THIN);
        quantityColumnStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        quantityColumnStyle.setBorderTop(BorderStyle.THIN);
        quantityColumnStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        quantityColumnStyle.setBorderRight(BorderStyle.THIN);
        quantityColumnStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        quantityColumnStyle.setBorderLeft(BorderStyle.THIN);
        quantityColumnStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

        totalColumnStyle = xssfWorkbook.createCellStyle();
        totalColumnStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(CURRENCY_VALUE_FORMAT));
        totalColumnStyle.setFont(monospaceFontNormal);
        totalColumnStyle.setAlignment(HorizontalAlignment.RIGHT);
        totalColumnStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        totalColumnStyle.setBorderBottom(BorderStyle.THIN);
        totalColumnStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        totalColumnStyle.setBorderTop(BorderStyle.THIN);
        totalColumnStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        totalColumnStyle.setBorderRight(BorderStyle.THIN);
        totalColumnStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        totalColumnStyle.setBorderLeft(BorderStyle.THIN);
        totalColumnStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

        dueByDateColumnStyle = xssfWorkbook.createCellStyle();
        dueByDateColumnStyle.setFont(monospaceFontNormal);
        dueByDateColumnStyle.setAlignment(HorizontalAlignment.CENTER);
        dueByDateColumnStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dueByDateColumnStyle.setBorderBottom(BorderStyle.THIN);
        dueByDateColumnStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        dueByDateColumnStyle.setBorderTop(BorderStyle.THIN);
        dueByDateColumnStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        dueByDateColumnStyle.setBorderRight(BorderStyle.THIN);
        dueByDateColumnStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        dueByDateColumnStyle.setBorderLeft(BorderStyle.THIN);
        dueByDateColumnStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

        pointsSubtotalStyle = xssfWorkbook.createCellStyle();
        pointsSubtotalStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(DOUBLE_VALUE_FORMAT));
        pointsSubtotalStyle.setFont(monospaceFontBold);
        pointsSubtotalStyle.setAlignment(HorizontalAlignment.RIGHT);
        pointsSubtotalStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        pointsSubtotalStyle.setBorderBottom(BorderStyle.THIN);
        pointsSubtotalStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        pointsSubtotalStyle.setBorderTop(BorderStyle.THIN);
        pointsSubtotalStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        pointsSubtotalStyle.setBorderRight(BorderStyle.THIN);
        pointsSubtotalStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        pointsSubtotalStyle.setBorderLeft(BorderStyle.THIN);
        pointsSubtotalStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        pointsSubtotalStyle.setFillForegroundColor(SUBTOTAL_CELL_BACKGROUND);
        pointsSubtotalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        quantitySubtotalStyle = xssfWorkbook.createCellStyle();
        quantitySubtotalStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(INTEGER_VALUE_FORMAT));
        quantitySubtotalStyle.setFont(monospaceFontBold);
        quantitySubtotalStyle.setAlignment(HorizontalAlignment.RIGHT);
        quantitySubtotalStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        quantitySubtotalStyle.setBorderBottom(BorderStyle.THIN);
        quantitySubtotalStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        quantitySubtotalStyle.setBorderTop(BorderStyle.THIN);
        quantitySubtotalStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        quantitySubtotalStyle.setBorderRight(BorderStyle.THIN);
        quantitySubtotalStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        quantitySubtotalStyle.setBorderLeft(BorderStyle.THIN);
        quantitySubtotalStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        quantitySubtotalStyle.setFillForegroundColor(SUBTOTAL_CELL_BACKGROUND);
        quantitySubtotalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        amountDueSubtotalStyle = xssfWorkbook.createCellStyle();
        amountDueSubtotalStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(CURRENCY_VALUE_FORMAT));
        amountDueSubtotalStyle.setFont(monospaceFontBold);
        amountDueSubtotalStyle.setAlignment(HorizontalAlignment.RIGHT);
        amountDueSubtotalStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        amountDueSubtotalStyle.setBorderBottom(BorderStyle.THIN);
        amountDueSubtotalStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        amountDueSubtotalStyle.setBorderTop(BorderStyle.THIN);
        amountDueSubtotalStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        amountDueSubtotalStyle.setBorderRight(BorderStyle.THIN);
        amountDueSubtotalStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        amountDueSubtotalStyle.setBorderLeft(BorderStyle.THIN);
        amountDueSubtotalStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        amountDueSubtotalStyle.setFillForegroundColor(SUBTOTAL_CELL_BACKGROUND);
        amountDueSubtotalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        pointsTotalStyle = xssfWorkbook.createCellStyle();
        pointsTotalStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(DOUBLE_VALUE_FORMAT));
        pointsTotalStyle.setFont(monospaceFontBold);
        pointsTotalStyle.setAlignment(HorizontalAlignment.RIGHT);
        pointsTotalStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        pointsTotalStyle.setBorderBottom(BorderStyle.THIN);
        pointsTotalStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        pointsTotalStyle.setBorderTop(BorderStyle.THIN);
        pointsTotalStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        pointsTotalStyle.setBorderRight(BorderStyle.THIN);
        pointsTotalStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        pointsTotalStyle.setBorderLeft(BorderStyle.THIN);
        pointsTotalStyle.setFillForegroundColor(TOTAL_CELL_BACKGROUND);
        pointsTotalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        quantityTotalStyle = xssfWorkbook.createCellStyle();
        quantityTotalStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(INTEGER_VALUE_FORMAT));
        quantityTotalStyle.setFont(monospaceFontBold);
        quantityTotalStyle.setAlignment(HorizontalAlignment.RIGHT);
        quantityTotalStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        quantityTotalStyle.setBorderBottom(BorderStyle.THIN);
        quantityTotalStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        quantityTotalStyle.setBorderTop(BorderStyle.THIN);
        quantityTotalStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        quantityTotalStyle.setBorderRight(BorderStyle.THIN);
        quantityTotalStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        quantityTotalStyle.setBorderLeft(BorderStyle.THIN);
        quantityTotalStyle.setFillForegroundColor(TOTAL_CELL_BACKGROUND);
        quantityTotalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        amountDueTotalStyle = xssfWorkbook.createCellStyle();
        amountDueTotalStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(CURRENCY_VALUE_FORMAT));
        amountDueTotalStyle.setFont(monospaceFontBold);
        amountDueTotalStyle.setAlignment(HorizontalAlignment.RIGHT);
        amountDueTotalStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        amountDueTotalStyle.setBorderBottom(BorderStyle.THIN);
        amountDueTotalStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        amountDueTotalStyle.setBorderTop(BorderStyle.THIN);
        amountDueTotalStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        amountDueTotalStyle.setBorderRight(BorderStyle.THIN);
        amountDueTotalStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        amountDueTotalStyle.setBorderLeft(BorderStyle.THIN);
        amountDueTotalStyle.setFillForegroundColor(TOTAL_CELL_BACKGROUND);
        amountDueTotalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        itemIdStyle = xssfWorkbook.createCellStyle();
        itemIdStyle.setFont(monospaceFontNormal);
        itemIdStyle.setAlignment(HorizontalAlignment.RIGHT);
        itemIdStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        itemIdStyle.setBorderBottom(BorderStyle.THIN);
        itemIdStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        itemIdStyle.setBorderTop(BorderStyle.THIN);
        itemIdStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        itemIdStyle.setBorderRight(BorderStyle.THIN);
        itemIdStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        itemIdStyle.setBorderLeft(BorderStyle.THIN);

        itemNameStyle = xssfWorkbook.createCellStyle();
        itemNameStyle.setFont(monospaceFontNormal);
        itemNameStyle.setAlignment(HorizontalAlignment.LEFT);
        itemNameStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        itemNameStyle.setBorderBottom(BorderStyle.THIN);
        itemNameStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        itemNameStyle.setBorderTop(BorderStyle.THIN);
        itemNameStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        itemNameStyle.setBorderRight(BorderStyle.THIN);
        itemNameStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        itemNameStyle.setBorderLeft(BorderStyle.THIN);

        priceEachStyle = xssfWorkbook.createCellStyle();
        priceEachStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(CUSTOM_PRICE_FORMAT));
        priceEachStyle.setFont(monospaceFontNormal);
        priceEachStyle.setAlignment(HorizontalAlignment.RIGHT);
        priceEachStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        priceEachStyle.setBorderBottom(BorderStyle.THIN);
        priceEachStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        priceEachStyle.setBorderTop(BorderStyle.THIN);
        priceEachStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        priceEachStyle.setBorderRight(BorderStyle.THIN);
        priceEachStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        priceEachStyle.setBorderLeft(BorderStyle.THIN);
        priceEachStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());

        promotionPriceStyle = xssfWorkbook.createCellStyle();
        promotionPriceStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(CUSTOM_PRICE_FORMAT));
        promotionPriceStyle.setFont(monospaceFontNormal);
        promotionPriceStyle.setAlignment(HorizontalAlignment.RIGHT);
        promotionPriceStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        promotionPriceStyle.setBorderBottom(BorderStyle.THIN);
        promotionPriceStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        promotionPriceStyle.setBorderTop(BorderStyle.THIN);
        promotionPriceStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        promotionPriceStyle.setBorderRight(BorderStyle.THIN);
        promotionPriceStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        promotionPriceStyle.setBorderLeft(BorderStyle.THIN);
        promotionPriceStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        promotionPriceStyle.setFillBackgroundColor(IndexedColors.CORAL.getIndex());
        promotionPriceStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);





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

        monospaceDetailDateStyle = xssfWorkbook.createCellStyle();
        monospaceDetailDateStyle.cloneStyleFrom(monospaceDefaultStyle);
        monospaceDetailDateStyle.setAlignment(HorizontalAlignment.CENTER);

        monospaceInstructionStyle = xssfWorkbook.createCellStyle();
        monospaceInstructionStyle.setFont(monospaceFontNormal);
        monospaceInstructionStyle.setBorderBottom(BorderStyle.NONE);
        monospaceInstructionStyle.setBorderTop(BorderStyle.NONE);
        monospaceInstructionStyle.setBorderLeft(BorderStyle.NONE);
        monospaceInstructionStyle.setBorderRight(BorderStyle.NONE);
        monospaceInstructionStyle.setAlignment(HorizontalAlignment.CENTER);
        monospaceInstructionStyle.setVerticalAlignment(VerticalAlignment.CENTER);

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
