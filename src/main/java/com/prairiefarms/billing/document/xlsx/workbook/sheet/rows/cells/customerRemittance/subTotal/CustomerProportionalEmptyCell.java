package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.customerRemittance.subTotal;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class CustomerProportionalEmptyCell {

    public static void set(XSSFSheet sheet, int rowNumber, int columnNumber) {
        XSSFCell cell = sheet.getRow(rowNumber).getCell(columnNumber);
        cell.removeFormula();
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalDefaultStyle());
    }
}
