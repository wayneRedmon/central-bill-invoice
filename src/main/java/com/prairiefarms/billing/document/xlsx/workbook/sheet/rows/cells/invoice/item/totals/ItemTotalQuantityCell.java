package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item.totals;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ItemTotalQuantityCell {

    public static void set(XSSFSheet sheet, int startingRowNumber, int endingRowNumber) {
        XSSFCell cell = sheet.getRow(endingRowNumber + 1).getCell(4);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceTotalIntegerStyle());
        cell.setCellFormula("SUM(E" + (startingRowNumber + 2) + ":E" + (endingRowNumber + 1) + ")");
    }
}
