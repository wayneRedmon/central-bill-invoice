package com.prairiefarms.billing.document.xlsx.workbook.sheet.overlay;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class StampLogo  {

    public static void set(XSSFWorkbook workbook) {
        final Integer DOCUMENT_STATIC_SHEETS = 3;
        final Logo logo = new Logo(workbook);

        for (int sheetIndex = 0; sheetIndex < DOCUMENT_STATIC_SHEETS; sheetIndex++) {
            XSSFSheet cloneableSheet = workbook.getSheetAt(sheetIndex);
            cloneableSheet.setDefaultRowHeightInPoints(18f);
            logo.stamp(cloneableSheet);
        }
    }
}
