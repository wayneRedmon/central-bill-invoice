package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class InvoiceTotalCell {

    public static void set(XSSFSheet sheet, int rowNumber, String formula) {
        XSSFCell cell = sheet.getRow(rowNumber).getCell(6);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceTotalCurrencyStyle());
        cell.setCellFormula(formula);
    }
}
