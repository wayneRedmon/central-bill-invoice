package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.utils.Contact;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class BillToRows {

    public static void set(XSSFSheet sheet, Contact contact) {
        XSSFCell xssfCell = sheet.getRow(8).getCell(0);
        xssfCell.setCellStyle(WorkbookEnvironment.getInstance().getContactStyle());
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(contact.getName());

        xssfCell = sheet.getRow(9).getCell(0);
        xssfCell.setCellStyle(WorkbookEnvironment.getInstance().getContactStyle());
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(contact.getStreet());

        xssfCell = sheet.getRow(10).getCell(0);
        xssfCell.setCellStyle(WorkbookEnvironment.getInstance().getContactStyle());
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(contact.getAddress());
    }
}
