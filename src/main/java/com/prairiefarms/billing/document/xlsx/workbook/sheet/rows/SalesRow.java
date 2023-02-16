package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.customer.Customer;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.invoice.Invoice;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.time.format.DateTimeFormatter;

public class SalesRow {

    public static void set(XSSFSheet sheet, Customer customer, Invoice invoice) {
        XSSFRow row = sheet.getRow(14);

        XSSFCell cell = sheet.getRow(14).getCell(0);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalCenterBold());
        cell.setCellValue(customer.getSalesperson());
        cell = row.getCell(2);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalCenterBold());
        cell.setCellValue(invoice.getHeader().getPurchaseOrder());
        cell = row.getCell(3);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalCenterBold());
        cell.setCellValue(invoice.getDeliveryDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        cell = row.getCell(4);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalCenterBold());
        cell.setCellValue(customer.getTerms().getName());
        cell = row.getCell(6);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalCenterBold());
        cell.setCellValue(customer.getDueByDate(invoice.getHeader().getDeliveryDate()));
    }
}
