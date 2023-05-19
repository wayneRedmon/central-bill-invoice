package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.customer.Terms;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.utils.Contact;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RemittanceSummaryRow {

    private static final List<String> pointsSubtotalCellReferences = new ArrayList<>();
    private static final List<String> quantitySubtotalCellReferences = new ArrayList<>();
    private static final List<String> amountDueSubtotalCellReferences = new ArrayList<>();

    public static void setTableHeader(XSSFSheet xssfSheet, int rowNumber) {
        XSSFRow xssfRow = xssfSheet.createRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(1);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("INVOICE");

        xssfCell = xssfRow.createCell(2);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("DELIVERED");

        xssfCell = xssfRow.createCell(3);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("POINTS");

        xssfCell = xssfRow.createCell(4);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("QUANTITY");

        xssfCell = xssfRow.createCell(5);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("TOTAL");

        xssfCell = xssfRow.createCell(6);
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("DUE BY");
    }

    public static void setCustomerRow(XSSFSheet xssfSheet, int rowNumber, Contact contact, Terms terms) {
        XSSFRow xssfRow = xssfSheet.createRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(0);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_BOLD_ALIGN_LEFT_YELLOW);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("[" + String.format("%1$5s", contact.getId()) + "] " + contact.getName());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 0, 3));

        xssfCell = xssfRow.createCell(4);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_YELLOW);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(StringUtils.normalizeSpace(terms.getName()));
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 4, 6));
    }

    public static void setCustomerInvoiceRow(XSSFSheet xssfSheet,
                                             int rowNumber,
                                             LocalDate deliveryDate,
                                             LocalDate dueByDate,
                                             int invoiceId,
                                             double points,
                                             int quantity,
                                             double subTotal) {
        XSSFRow xssfRow = xssfSheet.getRow(rowNumber);

        if (ObjectUtils.isEmpty(xssfRow)) xssfRow = xssfSheet.createRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(1);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(invoiceId);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED);

        xssfCell = xssfRow.createCell(2);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_CENTER_BORDERED);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(deliveryDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        xssfCell = xssfRow.createCell(3);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_DOUBLE);
        xssfCell.setCellType(CellType.NUMERIC);
        xssfCell.setCellValue(points);

        xssfCell = xssfRow.createCell(4);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_INTEGER);
        xssfCell.setCellType(CellType.NUMERIC);
        xssfCell.setCellValue(quantity);

        xssfCell = xssfRow.createCell(5);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_CURRENCY);
        xssfCell.setCellType(CellType.NUMERIC);
        xssfCell.setCellValue(subTotal);

        xssfCell = xssfRow.createCell(6);
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_ALIGN_CENTER_BORDERED);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(dueByDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    }

    public static void setCustomerTotal(XSSFSheet xssfSheet,
                                        int rowNumber,
                                        int startingCanonicalRow,
                                        int endingCanonicalRow) {
        XSSFRow xssfRow = xssfSheet.createRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(1);
        xssfCell.removeFormula();
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_YELLOW);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("CUSTOMER SUBTOTAL");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 1, 2));

        xssfCell = xssfRow.createCell(3);
        xssfCell.removeFormula();
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW);
        xssfCell.setCellFormula("SUM(D" + startingCanonicalRow + ":D" + endingCanonicalRow + ")");
        pointsSubtotalCellReferences.add(xssfCell.getReference());

        xssfCell = xssfRow.createCell(4);
        xssfCell.removeFormula();
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_INTEGER_YELLOW);
        xssfCell.setCellFormula("SUM(E" + startingCanonicalRow + ":E" + endingCanonicalRow + ")");
        quantitySubtotalCellReferences.add(xssfCell.getReference());

        xssfCell = xssfRow.createCell(5);
        xssfCell.removeFormula();
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_CURRENCY_YELLOW);
        xssfCell.setCellFormula("SUM(F" + startingCanonicalRow + ":F" + endingCanonicalRow + ")");
        amountDueSubtotalCellReferences.add(xssfCell.getReference());
    }

    public static void setTotal(XSSFSheet xssfSheet, int rowNumber) {
        XSSFRow xssfRow = xssfSheet.createRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(1);
        xssfCell.removeFormula();
        //xssfCell.setCellStyle(WorkbookEnvironment.PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_RIGHT_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("REMITTANCE TOTAL");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 1, 2));

        xssfCell = xssfRow.createCell(3);
        xssfCell.removeFormula();
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE);
        xssfCell.setCellFormula(StringUtils.join(pointsSubtotalCellReferences, "+"));

        xssfCell = xssfRow.createCell(4);
        xssfCell.removeFormula();
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE);
        xssfCell.setCellFormula(StringUtils.join(quantitySubtotalCellReferences, "+"));

        xssfCell = xssfRow.createCell(5);
        xssfCell.removeFormula();
        //xssfCell.setCellStyle(WorkbookEnvironment.MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_CURRENCY);
        xssfCell.setCellFormula(StringUtils.join(amountDueSubtotalCellReferences, "+"));
    }
}
