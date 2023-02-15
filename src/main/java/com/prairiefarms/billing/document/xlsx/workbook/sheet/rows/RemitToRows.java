package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.remit.Remit;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class RemitToRows {

    public static void set(XSSFSheet sheet, Remit remit) {
        XSSFRow row = sheet.getRow(0);
        XSSFCell cell = row.getCell(1);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalCenterBold());
        cell.setCellType(CellType.STRING);
        cell.setCellValue(remit.getContact().getName());

        row = sheet.getRow(1);
        cell = row.getCell(1);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalCenterBold());
        cell.setCellType(CellType.STRING);
        cell.setCellValue(remit.getContact().getStreet());

        row = sheet.getRow(2);
        cell = row.getCell(1);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalCenterBold());
        cell.setCellType(CellType.STRING);
        cell.setCellValue(remit.getContact().getAddress());

        row = sheet.getRow(3);
        cell = row.getCell(1);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalCenterBold());
        cell.setCellType(CellType.STRING);
        cell.setCellValue("Phone: " + remit.getContact().getPhone());
    }
}
