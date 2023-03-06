package com.prairiefarms.billing.document.xlsx.workbook.sheet;

import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.centralBill.CentralBill;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.BillToRows;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.CopyRow;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.ItemSummaryRow;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.RemitToRows;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.centralBill.BillToAccountCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.InvoiceDateCell;
import com.prairiefarms.billing.invoice.item.ItemSummary;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public class ItemSummarySheet {

    private final XSSFWorkbook xssfWorkbook;

    public ItemSummarySheet(XSSFWorkbook xssfWorkbook) {
        this.xssfWorkbook = xssfWorkbook;
    }

    public void generate(CentralBill centralBill, List<ItemSummary> itemSummaries) {
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(WorkbookEnvironment.getInstance().ITEM_SUMMARY_SHEET_TO_COPY);
        xssfSheet.setDefaultRowHeightInPoints(WorkbookEnvironment.getInstance().DEFAULT_ROW_HEIGHT_IN_POINTS);
        xssfSheet.setDisplayGridlines(false);
        xssfSheet.setPrintGridlines(false);

        RemitToRows.set(xssfSheet, centralBill.getRemit());
        InvoiceDateCell.set(xssfSheet);
        BillToAccountCell.set(xssfSheet, Environment.getInstance().getDairyId() ,centralBill.getContact().getId());
        BillToRows.set(xssfSheet, centralBill.getContact());

        int rowNumber = 12;
        int startingCanonicalRow = 0;

        ItemSummaryRow itemSummaryRow= new ItemSummaryRow(xssfSheet);

        for (ItemSummary itemSummary : itemSummaries) {
            rowNumber++;
            CopyRow.set(xssfSheet, rowNumber, WorkbookEnvironment.getInstance().ITEM_SUMMARY_ROW_TO_COPY);

            if (startingCanonicalRow == 0) startingCanonicalRow = rowNumber + 1;

            itemSummaryRow.set(rowNumber, itemSummary.getItem());
        }

        itemSummaryRow.setTotal(rowNumber);

        xssfSheet.protectSheet(Environment.getInstance().getXlsxDocumentPassword());
    }
}
