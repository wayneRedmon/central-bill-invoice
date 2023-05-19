package com.prairiefarms.billing.document.xlsx.workbook;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class WorkbookEnvironment {

    public static final float DEFAULT_ROW_HEIGHT_IN_POINTS = 18f;
    public static final String DOUBLE_VALUE_FORMAT = "_(* #,##0.00_);[RED]_(* \\(#,##0.00\\);_(* -??_);_(@_)";
    public static final String CURRENCY_VALUE_FORMAT = "_(* $#,##0.00_);[RED]_(* \\($#,##0.00\\);_(* -??_);_(@_)";
    public static final String INTEGER_VALUE_FORMAT = "_(* #,##0_);[RED]_(* \\(#,##0\\);_(* -??_);_(@_)";
    public static final String CUSTOM_PRICE_FORMAT = "_(* ##0.0000_);[RED]_(* \\(##0.0000\\);_(* -??_);_(@_)";
    public static final short CELL_BACKGROUND_YELLOW = IndexedColors.LEMON_CHIFFON.getIndex();
    public static final short CELL_BACKGROUND_GREEN = IndexedColors.LIGHT_GREEN.getIndex();
    public static final XSSFColor CELL_BACKGROUND_CUSTOM_BLUE = new XSSFColor(new java.awt.Color(207, 231, 245), new DefaultIndexedColorMap());
    public static final String PROPORTIONAL_FONT_NAME = "Helvetica";
    public static final String MONOSPACE_FONT_NAME = "Courier";
    public static final Short FONT_SIZE_12_POINT = 12;
    public static final Short FONT_SIZE_10_POINT = 10;
}
