package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item.*;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item.totals.ItemTotalExtensionCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item.totals.ItemTotalPointsCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.item.totals.ItemTotalQuantityCell;
import com.prairiefarms.billing.invoice.item.Item;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.math.BigDecimal;

public class ItemSummaryRow {

    public static void set(XSSFSheet sheet, int rowNumber, Item item) {
        for (int column = 0; column < sheet.getRow(rowNumber).getLastCellNum(); column++) {
            switch (column) {
                case 0:
                    ItemNumberCell.set(sheet, rowNumber, column, item.getId());
                    break;
                case 1:
                    ItemDescriptionCell.set(sheet, rowNumber, column, item.getName());
                    break;
                case 3:
                    ItemPointsCell.set(sheet, rowNumber, column, (BigDecimal.valueOf(item.getPointsEach()).multiply(new BigDecimal(item.getQuantity()))).doubleValue());
                    break;
                case 4:
                    ItemQuantityCell.set(sheet, rowNumber, column, item.getQuantity());
                    break;
                case 5:
                    ItemPriceCell.set(sheet, rowNumber, column, item.isPromotion(), (BigDecimal.valueOf(item.getPriceEach()).doubleValue()));
                    break;
                case 6:
                    ItemExtensionCell.set(sheet, rowNumber, column, item.getExtension());
                    break;
            }
        }
    }

    public static void setTotal(XSSFSheet sheet, int startingRowNumber, int endingRowNumber) {
        ItemTotalPointsCell.set(sheet, startingRowNumber, endingRowNumber);
        ItemTotalQuantityCell.set(sheet, startingRowNumber, endingRowNumber);
        ItemTotalExtensionCell.set(sheet, startingRowNumber, endingRowNumber);
    }
}
