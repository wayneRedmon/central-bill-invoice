package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.centralBill.BillToAccountCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.instruction.InstructionCell;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.invoice.InvoiceDateCell;
import com.prairiefarms.billing.utils.Contact;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RemittanceSummaryRow {

    private final XSSFSheet xssfSheet;

    private final List<String> pointsSubtotalCellReferences = new ArrayList<>();
    private final List<String> quantitySubtotalCellReferences = new ArrayList<>();
    private final List<String> amountDueSubtotalCellReferences = new ArrayList<>();

    public RemittanceSummaryRow(XSSFSheet xssfSheet) {
        this.xssfSheet = xssfSheet;
    }

    public void set(int billToId) {
        InvoiceDateCell.set(xssfSheet);
        BillToAccountCell.set(xssfSheet, Environment.getInstance().getDairyId(), billToId);
        InstructionCell.set(xssfSheet);
    }

    public void setCustomerColumn(int rowNumber, Contact contact) {
        XSSFCell cell = xssfSheet.getRow(rowNumber).getCell(0);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getCustomerColumnStyle());
        cell.setCellType(CellType.STRING);
        cell.setCellValue("[" + String.format("%1$5s", contact.getId()) + "] " + contact.getName());

        CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNumber, rowNumber, 0, 2);
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, xssfSheet);
    }

    public void set(int rowNumber,
                    LocalDate deliveryDate,
                    LocalDate dueByDate,
                    int invoiceId,
                    double points,
                    int quantity,
                    double subTotal) {
        XSSFCell cell = xssfSheet.getRow(rowNumber).getCell(3);
        cell.setCellType(CellType.STRING);
        cell.setCellValue(invoiceId);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getInvoiceColumnStyle());

        cell = xssfSheet.getRow(rowNumber).getCell(4);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getDeliveryDateColumnStyle());
        cell.setCellType(CellType.STRING);
        cell.setCellValue(deliveryDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        cell = xssfSheet.getRow(rowNumber).getCell(5);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getPointsColumnStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(points);

        cell = xssfSheet.getRow(rowNumber).getCell(6);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getQuantityColumnStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(quantity);

        cell = xssfSheet.getRow(rowNumber).getCell(7);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getTotalColumnStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(subTotal);

        cell = xssfSheet.getRow(rowNumber).getCell(8);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getDueByDateColumnStyle());
        cell.setCellType(CellType.STRING);
        cell.setCellValue(dueByDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    }

    public void setCustomerTotal(int rowNumber,
                                 int startingCanonicalRow,
                                 int endingCanonicalRow) {
        XSSFCell cell = xssfSheet.getRow(rowNumber).getCell(5);
        cell.removeFormula();
        cell.setCellStyle(WorkbookEnvironment.getInstance().getPointsSubtotalStyle());
        cell.setCellFormula("SUM(F" + startingCanonicalRow + ":F" + endingCanonicalRow + ")");
        pointsSubtotalCellReferences.add(cell.getReference());

        cell = xssfSheet.getRow(rowNumber).getCell(6);
        cell.removeFormula();
        cell.setCellStyle(WorkbookEnvironment.getInstance().getQuantitySubtotalStyle());
        cell.setCellFormula("SUM(G" + startingCanonicalRow + ":G" + endingCanonicalRow + ")");
        quantitySubtotalCellReferences.add(cell.getReference());

        cell = xssfSheet.getRow(rowNumber).getCell(7);
        cell.removeFormula();
        cell.setCellStyle(WorkbookEnvironment.getInstance().getAmountDueSubtotalStyle());
        cell.setCellFormula("SUM(H" + startingCanonicalRow + ":H" + endingCanonicalRow + ")");
        amountDueSubtotalCellReferences.add(cell.getReference());
    }

    public void setTotal(int rowNumber) {
        XSSFCell cell = xssfSheet.getRow(rowNumber).getCell(5);
        cell.removeFormula();
        cell.setCellStyle(WorkbookEnvironment.getInstance().getPointsTotalStyle());
        cell.setCellFormula(StringUtils.join(pointsSubtotalCellReferences, "+"));

        cell = xssfSheet.getRow(rowNumber).getCell(6);
        cell.removeFormula();
        cell.setCellStyle(WorkbookEnvironment.getInstance().getQuantityTotalStyle());
        cell.setCellFormula(StringUtils.join(quantitySubtotalCellReferences, "+"));

        cell = xssfSheet.getRow(rowNumber).getCell(7);
        cell.removeFormula();
        cell.setCellStyle(WorkbookEnvironment.getInstance().getAmountDueTotalStyle());
        cell.setCellFormula(StringUtils.join(amountDueSubtotalCellReferences, "+"));
    }
}
