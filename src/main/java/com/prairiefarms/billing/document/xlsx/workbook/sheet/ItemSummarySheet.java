package com.prairiefarms.billing.document.xlsx.workbook.sheet;

import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.centralBill.CentralBill;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.overlay.Logo;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.BillToRows;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.ItemSummaryRow;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.RemitToRows;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.centralBill.BillToAccountCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.InvoiceDateCell;
import com.prairiefarms.billing.invoice.item.ItemSummary;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

public class ItemSummarySheet {

    private static final String SHEET_NAME="ITEM SUMMARY";

    public static void generate(XSSFWorkbook xssfWorkbook, CentralBill centralBill, List<ItemSummary> itemSummaries) throws IOException {
        XSSFSheet xssfSheet = xssfWorkbook.createSheet(SHEET_NAME);
        xssfSheet.setDefaultRowHeightInPoints(WorkbookEnvironment.DEFAULT_ROW_HEIGHT_IN_POINTS);
        xssfSheet.setDisplayGridlines(false);
        xssfSheet.setPrintGridlines(false);

        for (int column = 0; column < 9; column++) {
            xssfSheet.setColumnWidth(column, 15 * 256);
        }

        Logo.stamp(xssfSheet);

        RemitToRows.set(xssfSheet, centralBill.getRemit(), SHEET_NAME);

        InvoiceDateCell.set(xssfSheet, Environment.getInstance().getBillingDate());
        BillToAccountCell.set(xssfSheet, Environment.getInstance().getDairyId(), centralBill.getContact().getId());

        BillToRows.set(xssfSheet, centralBill.getContact());

        int rowNumber = 11;
        int startingCanonicalRow = 0;

        ItemSummaryRow.setTableHeader(xssfSheet, rowNumber);

        for (ItemSummary itemSummary : itemSummaries) {
            rowNumber++;

            if (startingCanonicalRow == 0) startingCanonicalRow = rowNumber + 1;

            ItemSummaryRow.setItemDetail(xssfSheet, rowNumber, itemSummary.getItem());
        }

        rowNumber++;

        ItemSummaryRow.setTotal(xssfSheet, rowNumber);

        xssfSheet.protectSheet(Environment.getInstance().getXlsxDocumentPassword());
    }
}
