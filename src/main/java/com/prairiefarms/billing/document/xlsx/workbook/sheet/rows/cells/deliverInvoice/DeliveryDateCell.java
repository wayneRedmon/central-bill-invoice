package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.deliverInvoice;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DeliveryDateCell {

    public static void set(XSSFSheet sheet, LocalDate deliveryDate) {
        XSSFCell cell = sheet.getRow(1).getCell(14);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getProportionalCenterBackground());
        cell.setCellValue(deliveryDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    }
}
