package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.customerRemittance.total;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class CustomerPointsTotalCell {

    public static void set(XSSFSheet sheet, int rowNumber, String formula) {
        final int COLUMN_NUMBER = 5;

        XSSFCell cell = sheet.getRow(rowNumber).getCell(COLUMN_NUMBER);
        cell.removeFormula();
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceTotalDoubleStyle());
        cell.setCellFormula(formula);
    }
}
