package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.customerRemittance;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DeliveryDateCell {

    public static void set(XSSFSheet sheet, int rowNumber, int columnNumber, LocalDate deliveryDate) {
        XSSFCell cell = sheet.getRow(rowNumber).getCell(columnNumber);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceDetailDateStyle());
        cell.setCellType(CellType.STRING);
        cell.setCellValue(deliveryDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    }
}
