package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.customer.Customer;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ShipToRows {

    public static void set(XSSFSheet sheet, Customer customer) {
        XSSFRow row = sheet.getRow(8);

        XSSFCell cell = row.getCell(4);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalDefaultStyle());
        cell.setCellValue(customer.getContact().getName());

        row = sheet.getRow(9);
        cell = row.getCell(4);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalDefaultStyle());
        cell.setCellValue(customer.getContact().getStreet());

        row = sheet.getRow(10);
        cell = row.getCell(4);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalDefaultStyle());
        cell.setCellValue(customer.getContact().getAddress());

        row = sheet.getRow(11);
        cell = row.getCell(4);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalDefaultStyle());
        cell.setCellValue(customer.getContact().getPhone());
    }
}
