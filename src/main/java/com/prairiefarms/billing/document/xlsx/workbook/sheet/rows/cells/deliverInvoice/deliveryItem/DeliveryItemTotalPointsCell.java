package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.deliverInvoice.deliveryItem;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DeliveryItemTotalPointsCell {

    public static void set(XSSFSheet sheet, int startingRowNumber, int endingRowNumber) {
        XSSFCell cell = sheet.getRow(endingRowNumber + 1).getCell(13);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceTotalDoubleStyle());
        cell.setCellFormula("SUM(N" + (startingRowNumber + 2) + ":N" + (endingRowNumber + 1) + ")");
    }
}
