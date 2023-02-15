package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.deliverInvoice.deliveryItem.*;
import com.prairiefarms.billing.invoice.item.Item;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.math.BigDecimal;

public class DeliveryItemDetailRows {

    public static void set(XSSFSheet sheet, int rowNumber, Item item) {
        for (int column = 0; column < sheet.getRow(rowNumber).getLastCellNum(); column++) {
            switch (column) {
                case 9:
                    DeliveryItemNumberCell.set(sheet, rowNumber, column, item.getId());
                    break;
                case 10:
                    DeliveryItemDescriptionCell.set(sheet, rowNumber, column, item.getName());
                    break;
                case 13:
                    DeliveryItemPointsCell.set(sheet, rowNumber, column, (BigDecimal.valueOf(item.getPointsEach()).multiply(new BigDecimal(item.getQuantity()))).doubleValue());
                    break;
                case 14:
                    DeliveryItemQuantityCell.set(sheet, rowNumber, column, item.getQuantity());
                    break;
            }
        }
    }

    public static void set(XSSFSheet sheet, int startingRowNumber, int endingRowNumber) {
        DeliveryItemTotalPointsCell.set(sheet, startingRowNumber, endingRowNumber);
        DeliveryItemTotalQuantityCell.set(sheet, startingRowNumber, endingRowNumber);
    }
}
