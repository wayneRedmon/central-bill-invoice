package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item.totals;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ItemTotalPointsCell {

    public static void set(XSSFSheet sheet, int startingRowNumber, int endingRowNumber) {
        XSSFCell cell = sheet.getRow(endingRowNumber + 1).getCell(3);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceTotalDoubleStyle());
        cell.setCellFormula("SUM(D" + (startingRowNumber + 2) + ":D" + (endingRowNumber + 1) + ")");
    }
}
