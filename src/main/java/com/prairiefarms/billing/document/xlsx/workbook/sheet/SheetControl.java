package com.prairiefarms.billing.document.xlsx.workbook.sheet;

import org.apache.poi.ss.usermodel.CellCopyPolicy;

public class SheetControl {

    public static final float DEFAULT_ROW_HEIGHT_IN_POINTS = 18f;
    public static final int REMITTANCE_SHEET_TO_COPY = 0;
    public static final int REMITTANCE_ROW_TO_COPY = 13;
    public static final int ITEM_SUMMARY_SHEET_TO_COPY = 1;
    public static final int ITEM_SUMMARY_ROW_TO_COPY = 13;
    public static final int INVOICE_SHEET_TO_COPY = 2;
    public static final int INVOICE_ROW_TO_COPY = 16;
    public static final CellCopyPolicy CELL_COPY_POLICY = new CellCopyPolicy.Builder().mergedRegions(true).cellFormula(false).cellStyle(false).cellValue(false).build();

}
