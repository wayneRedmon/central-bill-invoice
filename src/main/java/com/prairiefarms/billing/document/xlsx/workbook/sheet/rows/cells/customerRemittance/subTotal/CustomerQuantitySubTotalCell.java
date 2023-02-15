package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.customerRemittance.subTotal;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class CustomerQuantitySubTotalCell {

    public static void set(XSSFSheet sheet, int rowNumber, int columnNumber, int startingRowNumber, int endingRowNumber) {
        final String SUBTOTAL_COLUMN = "G";

        XSSFCell cell = sheet.getRow(rowNumber).getCell(columnNumber);
        cell.removeFormula();
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceSubtotalIntegerStyle());
        cell.setCellFormula("SUM(" + SUBTOTAL_COLUMN + (startingRowNumber + 2) + ":" + SUBTOTAL_COLUMN + (endingRowNumber + 1) + ")");
    }
}