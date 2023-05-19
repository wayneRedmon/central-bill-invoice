package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.deliveryInvoice;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DeliveryDateCell {

    public static void set(XSSFSheet xssfSheet, LocalDate deliveryDate) {
        XSSFRow xssfRow = xssfSheet.getRow(1);

        XSSFCell xssfCell = xssfRow.createCell(13);
        xssfCell.setCellType(CellType.STRING);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_BLUE);
        xssfCell.setCellValue("Date");

        xssfCell = xssfRow.createCell(14);
        xssfCell.setCellType(CellType.STRING);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED);
        xssfCell.setCellValue(deliveryDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    }
}
