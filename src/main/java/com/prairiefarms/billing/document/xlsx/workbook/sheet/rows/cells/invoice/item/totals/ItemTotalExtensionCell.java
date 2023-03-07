package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item.totals;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ItemTotalExtensionCell {

    public static void set(XSSFSheet sheet, int startingRowNumber, int endingRowNumber) {
        final String TOTAL_COLUMN = "G";

        XSSFCell cell = sheet.getRow(endingRowNumber + 1).getCell(6);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceTotalCurrencyStyle());
        cell.setCellFormula("SUM(" + TOTAL_COLUMN + (startingRowNumber + 2) + ":" + TOTAL_COLUMN + (endingRowNumber + 1) + ")");
    }
}
