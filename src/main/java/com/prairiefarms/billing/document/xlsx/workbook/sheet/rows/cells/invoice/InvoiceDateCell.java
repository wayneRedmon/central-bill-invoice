package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice;

import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class InvoiceDateCell {

    public static void set(XSSFSheet sheet) {
        XSSFCell cell = sheet.getRow(1).getCell(6);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getPageInformationStyle());
        cell.setCellValue(Environment.getInstance().billingDateAsUSA());
    }
}
