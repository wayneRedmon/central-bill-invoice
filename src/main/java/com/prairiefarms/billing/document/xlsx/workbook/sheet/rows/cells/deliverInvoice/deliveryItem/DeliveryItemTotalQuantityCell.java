package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.deliverInvoice.deliveryItem;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DeliveryItemTotalQuantityCell {

    public static void set(XSSFSheet sheet, int startingRowNumber, int endingRowNumber) {
        XSSFCell cell = sheet.getRow(endingRowNumber + 1).getCell(14);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getMonospaceTotalIntegerStyle());
        cell.setCellFormula("SUM(O" + (startingRowNumber + 2) + ":O" + (endingRowNumber + 1) + ")");
    }
}
