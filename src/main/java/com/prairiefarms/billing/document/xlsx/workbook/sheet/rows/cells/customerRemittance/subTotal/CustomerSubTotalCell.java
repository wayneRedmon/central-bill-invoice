package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.customerRemittance.subTotal;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class CustomerSubTotalCell {

    public static void set(XSSFSheet sheet, int rowNumber, int columnNumber) {
        final String CELL_SUBTOTAL_VALUE = "Subtotal";

        XSSFCell cell = sheet.getRow(rowNumber).getCell(columnNumber);
        cell.removeFormula();
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalRightBold());
        cell.setCellValue(CELL_SUBTOTAL_VALUE);
    }
}
