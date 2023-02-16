package com.prairiefarms.billing.document.xlsx.workbook.sheet;

import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.centralBill.CentralBill;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.BillToRows;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.CopyRow;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.ItemSummaryRow;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.RemitToRows;
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
        XSSFSheet sheet = xssfWorkbook.getSheetAt(WorkbookEnvironment.getInstance().ITEM_SUMMARY_SHEET_TO_COPY);
        sheet.setDefaultRowHeightInPoints(WorkbookEnvironment.getInstance().DEFAULT_ROW_HEIGHT_IN_POINTS);
        sheet.setDisplayGridlines(false);
        sheet.setPrintGridlines(false);

        InvoiceDateCell.set(sheet);
        RemitToRows.set(sheet, centralBill.getRemit());
        BillToRows.set(sheet, centralBill);

        int rowNumber = WorkbookEnvironment.getInstance().ITEM_SUMMARY_ROW_TO_COPY;

        for (ItemSummary itemSummary : itemSummaries) {
            rowNumber++;

            CopyRow.set(sheet, rowNumber, WorkbookEnvironment.getInstance().ITEM_SUMMARY_ROW_TO_COPY);
            ItemSummaryRow.set(sheet, rowNumber, itemSummary.getItem());
        }

        ItemSummaryRow.setTotal(sheet, WorkbookEnvironment.getInstance().ITEM_SUMMARY_ROW_TO_COPY, rowNumber);

        sheet.protectSheet(Environment.getInstance().getXlsxDocumentPassword());
    }
}
