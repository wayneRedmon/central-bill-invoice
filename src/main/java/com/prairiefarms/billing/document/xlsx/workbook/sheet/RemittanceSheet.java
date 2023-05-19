package com.prairiefarms.billing.document.xlsx.workbook.sheet;

import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.customer.Customer;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.overlay.Logo;
import com.prairiefarms.billing.invoice.Invoice;
import com.prairiefarms.billing.invoice.centralBill.CentralBillInvoice;
import com.prairiefarms.billing.invoice.centralBill.customer.CustomerInvoice;
import com.prairiefarms.billing.invoice.item.Item;
import com.prairiefarms.billing.remit.Remit;
import com.prairiefarms.billing.utils.Contact;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RemittanceSheet {

    private static final String SHEET_NAME = "REMITTANCE";

    private XSSFCellStyle PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_CENTER;
    private XSSFCellStyle PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_RIGHT;
    private XSSFCellStyle PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_RIGHT_BLUE;
    private XSSFCellStyle PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_LEFT_BLUE;
    private XSSFCellStyle PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_BLUE;
    private XSSFCellStyle PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED;
    private XSSFCellStyle PROPORTIONAL_FONT_10_POINT_ALIGN_LEFT;
    private XSSFCellStyle PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE;
    private XSSFCellStyle PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER;
    private XSSFCellStyle PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_YELLOW;

    private XSSFCellStyle MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE;
    private XSSFCellStyle MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_CURRENCY;
    private XSSFCellStyle MONOSPACE_FONT_10_POINT_BOLD_ALIGN_LEFT_YELLOW;
    private XSSFCellStyle MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_YELLOW;
    private XSSFCellStyle MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED;
    private XSSFCellStyle MONOSPACE_FONT_10_POINT_ALIGN_CENTER_BORDERED;
    private XSSFCellStyle MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_DOUBLE;
    private XSSFCellStyle MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_INTEGER;
    private XSSFCellStyle MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_CURRENCY;
    private XSSFCellStyle MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW;
    private XSSFCellStyle MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_INTEGER_YELLOW;
    private XSSFCellStyle MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_CURRENCY_YELLOW;

    private final XSSFWorkbook xssfWorkbook;
    private final CentralBillInvoice centralBillInvoice;
    private final XSSFSheet xssfSheet;
    private final List<String> pointsSubtotalCellReferences = new ArrayList<>();
    private final List<String> quantitySubtotalCellReferences = new ArrayList<>();
    private final List<String> amountDueSubtotalCellReferences = new ArrayList<>();

    private int startingCanonicalRow;
    private int endingCanonicalRow;

    public RemittanceSheet(XSSFWorkbook xssfWorkbook, CentralBillInvoice centralBillInvoice) {
        this.xssfWorkbook = xssfWorkbook;
        this.centralBillInvoice = centralBillInvoice;
        this.xssfSheet = this.xssfWorkbook.createSheet(SHEET_NAME);

        setCellStyles();
    }

    public void generate() throws IOException {
        xssfSheet.setDisplayGridlines(false);
        xssfSheet.setPrintGridlines(false);
        xssfSheet.setDefaultRowHeightInPoints(WorkbookEnvironment.DEFAULT_ROW_HEIGHT_IN_POINTS);
        for (int column = 0; column < 9; column++) {
            xssfSheet.setColumnWidth(column, 15 * 256);
        }

        Logo.stamp(xssfSheet);

        setSheetHeader(centralBillInvoice.getCentralBill().getRemit());
        setBillTo(centralBillInvoice.getCentralBill().getContact());
        setInstructions();
        setDetail();

        xssfSheet.protectSheet(Environment.getInstance().getXlsxDocumentPassword());
    }

    private void setSheetHeader(Remit remit) {
        XSSFRow xssfRow = xssfSheet.createRow(0);

        XSSFCell xssfCell = xssfRow.createCell(1);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_CENTER);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(StringUtils.normalizeSpace(remit.getContact().getName()));
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 1, 4));

        xssfCell = xssfRow.createCell(6);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_RIGHT);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(SHEET_NAME);

        xssfRow = xssfSheet.createRow(1);

        xssfCell = xssfRow.createCell(1);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_CENTER);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(remit.getContact().getStreet());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 1, 4));

        xssfCell = xssfRow.createCell(5);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_BLUE);
        xssfCell.setCellValue("DATE");

        xssfCell = xssfRow.createCell(6);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED);
        xssfCell.setCellValue(Environment.getInstance().billingDateAsUSA());

        xssfRow = xssfSheet.createRow(2);

        xssfCell = xssfRow.createCell(1);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_CENTER);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(remit.getContact().getAddress());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 1, 4));

        xssfCell = xssfRow.createCell(5);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_BLUE);
        xssfCell.setCellValue("ACCOUNT");

        xssfCell = xssfRow.createCell(6);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED);
        xssfCell.setCellValue(Environment.getInstance().getDairyIdAsText() + "-" + centralBillInvoice.getCentralBill().getIdAsText());

        if (StringUtils.isNotBlank(remit.getContact().getPhone())) {
            xssfRow = xssfSheet.createRow(3);

            xssfCell = xssfRow.createCell(1);
            xssfCell.setCellStyle(PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_CENTER);
            xssfCell.setCellType(CellType.STRING);
            xssfCell.setCellValue(remit.getContact().getPhone());
            xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 1, 4));
        }
    }

    public void setBillTo(Contact contact) {
        XSSFRow xssfRow = xssfSheet.createRow(6);

        XSSFCell xssfCell = xssfRow.createCell(0);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_LEFT_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("BILL TO");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 0, 2));

        xssfRow = xssfSheet.createRow(7);

        xssfCell = xssfRow.createCell(0);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_ALIGN_LEFT);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(contact.getName());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 0, 2));

        xssfRow = xssfSheet.createRow(8);

        xssfCell = xssfRow.createCell(0);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_ALIGN_LEFT);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(contact.getStreet());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 0, 2));

        xssfRow = xssfSheet.createRow(9);

        xssfCell = xssfRow.createCell(0);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_ALIGN_LEFT);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(contact.getAddress());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 0, 2));
    }

    public void setInstructions() {
        XSSFRow xssfRow = xssfSheet.getRow(6);

        XSSFCell xssfCell = xssfRow.createCell(4);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("INSTRUCTIONS");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 4, 6));

        xssfRow = xssfSheet.getRow(7);

        xssfCell = xssfRow.createCell(4);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("Make checks payable to: " + Environment.getInstance().getCorporateName());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 4, 6));

        xssfRow = xssfSheet.getRow(8);

        xssfCell = xssfRow.createCell(4);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("Please return this page with your payment.");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 4, 6));

        xssfRow = xssfSheet.getRow(9);

        xssfCell = xssfRow.createCell(4);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("Thank you for your business!");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 4, 6));
    }

    private void setDetail() {
        int rowNumber = 10;

        for (CustomerInvoice customerInvoice : centralBillInvoice.getCustomerInvoices()) {
            rowNumber++;

            setCustomer(rowNumber, customerInvoice.getCustomer());

            startingCanonicalRow = 0;

            for (Invoice invoice : customerInvoice.getInvoices()) {
                rowNumber++;
                setCustomerInvoice(rowNumber, invoice, customerInvoice.getCustomer().getDueByDate(invoice.getHeader().getDeliveryDate()));

                if (startingCanonicalRow == 0) startingCanonicalRow = rowNumber + 1;
            }

            endingCanonicalRow = rowNumber + 1;

            rowNumber++;

            setCustomerTotal(rowNumber);
        }

        rowNumber++;

        XSSFRow xssfRow = xssfSheet.createRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(1);
        xssfCell.removeFormula();
        xssfCell.setCellStyle(PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_RIGHT_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("REMITTANCE TOTAL");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 1, 2));

        xssfCell = xssfRow.createCell(3);
        xssfCell.removeFormula();
        xssfCell.setCellStyle(MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE);
        xssfCell.setCellFormula(StringUtils.join(pointsSubtotalCellReferences, "+"));

        xssfCell = xssfRow.createCell(4);
        xssfCell.removeFormula();
        xssfCell.setCellStyle(MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE);
        xssfCell.setCellFormula(StringUtils.join(quantitySubtotalCellReferences, "+"));

        xssfCell = xssfRow.createCell(5);
        xssfCell.removeFormula();
        xssfCell.setCellStyle(MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_CURRENCY);
        xssfCell.setCellFormula(StringUtils.join(amountDueSubtotalCellReferences, "+"));
    }

    private void setCustomer(int rowNumber, Customer customer) {
        XSSFRow xssfRow = xssfSheet.createRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(0);
        xssfCell.setCellStyle(MONOSPACE_FONT_10_POINT_BOLD_ALIGN_LEFT_YELLOW);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(
                "[" +
                        String.format("%1$5s", customer.getContact().getId()) +
                        "] " +
                        customer.getContact().getName()
        );
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 0, 3));

        xssfCell = xssfRow.createCell(4);
        xssfCell.setCellStyle(MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_YELLOW);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(StringUtils.normalizeSpace(customer.getTerms().getName()));
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 4, 6));

        rowNumber++;

        xssfRow = xssfSheet.createRow(rowNumber);

        xssfCell = xssfRow.createCell(1);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("INVOICE");

        xssfCell = xssfRow.createCell(2);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("DELIVERED");

        xssfCell = xssfRow.createCell(3);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("POINTS");

        xssfCell = xssfRow.createCell(4);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("QUANTITY");

        xssfCell = xssfRow.createCell(5);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("TOTAL");

        xssfCell = xssfRow.createCell(6);
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("DUE BY");
    }

    private void setCustomerInvoice(int rowNumber, Invoice invoice, LocalDate dueByDate) {
        rowNumber++;

        double points = invoice.getItems().stream()
                .map(Item::getTotalPoints)
                .mapToDouble(Double::doubleValue)
                .sum();
        int quantity = invoice.getItems().stream()
                .map(Item::getQuantity)
                .mapToInt(Integer::intValue)
                .sum();
        double subTotal = invoice.getItems().stream()
                .map(Item::getExtension)
                .mapToDouble(Double::doubleValue)
                .sum();

        XSSFRow xssfRow = xssfSheet.getRow(rowNumber);

        if (ObjectUtils.isEmpty(xssfRow)) xssfRow = xssfSheet.createRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(1);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(invoice.getHeader().getId());
        xssfCell.setCellStyle(MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED);

        xssfCell = xssfRow.createCell(2);
        xssfCell.setCellStyle(MONOSPACE_FONT_10_POINT_ALIGN_CENTER_BORDERED);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(invoice.getDeliveryDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        xssfCell = xssfRow.createCell(3);
        xssfCell.setCellStyle(MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_DOUBLE);
        xssfCell.setCellType(CellType.NUMERIC);
        xssfCell.setCellValue(points);

        xssfCell = xssfRow.createCell(4);
        xssfCell.setCellStyle(MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_INTEGER);
        xssfCell.setCellType(CellType.NUMERIC);
        xssfCell.setCellValue(quantity);

        xssfCell = xssfRow.createCell(5);
        xssfCell.setCellStyle(MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_CURRENCY);
        xssfCell.setCellType(CellType.NUMERIC);
        xssfCell.setCellValue(subTotal);

        xssfCell = xssfRow.createCell(6);
        xssfCell.setCellStyle(MONOSPACE_FONT_10_POINT_ALIGN_CENTER_BORDERED);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(dueByDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        startingCanonicalRow = rowNumber + 1;
    }

    private void setCustomerTotal(int rowNumber) {
        XSSFRow xssfRow = xssfSheet.createRow(rowNumber);

        XSSFCell xssfCell = xssfRow.createCell(1);
        xssfCell.removeFormula();
        xssfCell.setCellStyle(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_YELLOW);
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue("CUSTOMER SUBTOTAL");
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 1, 2));

        xssfCell = xssfRow.createCell(3);
        xssfCell.removeFormula();
        xssfCell.setCellStyle(MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW);
        xssfCell.setCellFormula("SUM(D" + startingCanonicalRow + ":D" + endingCanonicalRow + ")");
        pointsSubtotalCellReferences.add(xssfCell.getReference());

        xssfCell = xssfRow.createCell(4);
        xssfCell.removeFormula();
        xssfCell.setCellStyle(MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_INTEGER_YELLOW);
        xssfCell.setCellFormula("SUM(E" + startingCanonicalRow + ":E" + endingCanonicalRow + ")");
        quantitySubtotalCellReferences.add(xssfCell.getReference());

        xssfCell = xssfRow.createCell(5);
        xssfCell.removeFormula();
        xssfCell.setCellStyle(MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_CURRENCY_YELLOW);
        xssfCell.setCellFormula("SUM(F" + startingCanonicalRow + ":F" + endingCanonicalRow + ")");
        amountDueSubtotalCellReferences.add(xssfCell.getReference());
    }

    private void setCellStyles() {
        XSSFFont PROPORTIONAL_FONT_12_POINT_BOLD = xssfWorkbook.createFont();
        PROPORTIONAL_FONT_12_POINT_BOLD.setFontName(WorkbookEnvironment.PROPORTIONAL_FONT_NAME);
        PROPORTIONAL_FONT_12_POINT_BOLD.setFontHeightInPoints(WorkbookEnvironment.FONT_SIZE_12_POINT);
        PROPORTIONAL_FONT_12_POINT_BOLD.setBold(true);

        PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_CENTER = xssfWorkbook.createCellStyle();
        PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_CENTER.setFont(PROPORTIONAL_FONT_12_POINT_BOLD);
        PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_CENTER.setVerticalAlignment(VerticalAlignment.CENTER);
        PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_CENTER.setAlignment(HorizontalAlignment.CENTER);

        PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_RIGHT = xssfWorkbook.createCellStyle();
        PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_RIGHT.setFont(PROPORTIONAL_FONT_12_POINT_BOLD);
        PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_RIGHT.setVerticalAlignment(VerticalAlignment.CENTER);
        PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_RIGHT.setAlignment(HorizontalAlignment.RIGHT);

        PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_RIGHT_BLUE = xssfWorkbook.createCellStyle();
        PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_RIGHT_BLUE.cloneStyleFrom(PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_CENTER);
        PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_RIGHT_BLUE.setAlignment(HorizontalAlignment.RIGHT);
        PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_RIGHT_BLUE.setFillForegroundColor(WorkbookEnvironment.CELL_BACKGROUND_CUSTOM_BLUE);
        PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_RIGHT_BLUE.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont PROPORTIONAL_FONT_10_POINT_BOLD = xssfWorkbook.createFont();
        PROPORTIONAL_FONT_10_POINT_BOLD.setFontName(WorkbookEnvironment.PROPORTIONAL_FONT_NAME);
        PROPORTIONAL_FONT_10_POINT_BOLD.setFontHeightInPoints(WorkbookEnvironment.FONT_SIZE_10_POINT);
        PROPORTIONAL_FONT_10_POINT_BOLD.setBold(true);

        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_LEFT_BLUE = xssfWorkbook.createCellStyle();
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_LEFT_BLUE.setFont(PROPORTIONAL_FONT_10_POINT_BOLD);
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_LEFT_BLUE.setVerticalAlignment(VerticalAlignment.CENTER);
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_LEFT_BLUE.setAlignment(HorizontalAlignment.LEFT);
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_LEFT_BLUE.setFillForegroundColor(WorkbookEnvironment.CELL_BACKGROUND_CUSTOM_BLUE);
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_LEFT_BLUE.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_BLUE = xssfWorkbook.createCellStyle();
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_BLUE.cloneStyleFrom(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_LEFT_BLUE);
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_BLUE.setAlignment(HorizontalAlignment.RIGHT);

        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED = xssfWorkbook.createCellStyle();
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED.setFont(PROPORTIONAL_FONT_10_POINT_BOLD);
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED.setVerticalAlignment(VerticalAlignment.CENTER);
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED.setAlignment(HorizontalAlignment.CENTER);
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED.setBorderBottom(BorderStyle.THIN);
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED.setBorderTop(BorderStyle.THIN);
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED.setBorderRight(BorderStyle.THIN);
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED.setBorderLeft(BorderStyle.THIN);
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BORDERED.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

        XSSFFont PROPORTIONAL_FONT_10_POINT = xssfWorkbook.createFont();
        PROPORTIONAL_FONT_10_POINT.setFontName(WorkbookEnvironment.PROPORTIONAL_FONT_NAME);
        PROPORTIONAL_FONT_10_POINT.setFontHeightInPoints(WorkbookEnvironment.FONT_SIZE_10_POINT);
        PROPORTIONAL_FONT_10_POINT.setBold(false);

        PROPORTIONAL_FONT_10_POINT_ALIGN_LEFT = xssfWorkbook.createCellStyle();
        PROPORTIONAL_FONT_10_POINT_ALIGN_LEFT.setFont(PROPORTIONAL_FONT_10_POINT);
        PROPORTIONAL_FONT_10_POINT_ALIGN_LEFT.setVerticalAlignment(VerticalAlignment.CENTER);
        PROPORTIONAL_FONT_10_POINT_ALIGN_LEFT.setAlignment(HorizontalAlignment.LEFT);

        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE = xssfWorkbook.createCellStyle();
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE.cloneStyleFrom(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_LEFT_BLUE);
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER_BLUE.setAlignment(HorizontalAlignment.CENTER);

        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER = xssfWorkbook.createCellStyle();
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER.setFont(PROPORTIONAL_FONT_10_POINT_BOLD);
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER.setVerticalAlignment(VerticalAlignment.CENTER);
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_CENTER.setAlignment(HorizontalAlignment.CENTER);

        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_YELLOW = xssfWorkbook.createCellStyle();
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_YELLOW.cloneStyleFrom(PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_LEFT_BLUE);
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_YELLOW.setAlignment(HorizontalAlignment.RIGHT);
        PROPORTIONAL_FONT_10_POINT_BOLD_ALIGN_RIGHT_YELLOW.setFillForegroundColor(WorkbookEnvironment.CELL_BACKGROUND_YELLOW);

        XSSFFont MONOSPACE_FONT_12_POINT_BOLD = xssfWorkbook.createFont();
        MONOSPACE_FONT_12_POINT_BOLD.setFontName(WorkbookEnvironment.MONOSPACE_FONT_NAME);
        MONOSPACE_FONT_12_POINT_BOLD.setFontHeightInPoints(WorkbookEnvironment.FONT_SIZE_12_POINT);
        MONOSPACE_FONT_12_POINT_BOLD.setBold(true);

        MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE = xssfWorkbook.createCellStyle();
        MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE.setFont(MONOSPACE_FONT_12_POINT_BOLD);
        MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE.setDataFormat(xssfWorkbook.createDataFormat().getFormat(WorkbookEnvironment.DOUBLE_VALUE_FORMAT));
        MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE.setBorderBottom(BorderStyle.THIN);
        MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE.setBorderTop(BorderStyle.THIN);
        MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE.setBorderRight(BorderStyle.THIN);
        MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE.setBorderLeft(BorderStyle.THIN);
        MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

        MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_CURRENCY = xssfWorkbook.createCellStyle();
        MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_CURRENCY.cloneStyleFrom(MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE);
        MONOSPACE_FONT_12_POINT_BOLD_ALIGN_RIGHT_BORDERED_CURRENCY.setDataFormat(xssfWorkbook.createDataFormat().getFormat(WorkbookEnvironment.CURRENCY_VALUE_FORMAT));

        XSSFFont MONOSPACE_FONT_10_POINT_BOLD = xssfWorkbook.createFont();
        MONOSPACE_FONT_10_POINT_BOLD.setFontName(WorkbookEnvironment.MONOSPACE_FONT_NAME);
        MONOSPACE_FONT_10_POINT_BOLD.setFontHeightInPoints(WorkbookEnvironment.FONT_SIZE_10_POINT);
        MONOSPACE_FONT_10_POINT_BOLD.setBold(true);

        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_LEFT_YELLOW = xssfWorkbook.createCellStyle();
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_LEFT_YELLOW.setFont(MONOSPACE_FONT_10_POINT_BOLD);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_LEFT_YELLOW.setAlignment(HorizontalAlignment.LEFT);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_LEFT_YELLOW.setVerticalAlignment(VerticalAlignment.CENTER);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_LEFT_YELLOW.setFillForegroundColor(WorkbookEnvironment.CELL_BACKGROUND_YELLOW);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_LEFT_YELLOW.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_YELLOW = xssfWorkbook.createCellStyle();
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_YELLOW.setFont(MONOSPACE_FONT_10_POINT_BOLD);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_YELLOW.setAlignment(HorizontalAlignment.RIGHT);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_YELLOW.setVerticalAlignment(VerticalAlignment.CENTER);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_YELLOW.setFillForegroundColor(WorkbookEnvironment.CELL_BACKGROUND_YELLOW);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_YELLOW.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont MONOSPACE_FONT_10_POINT = xssfWorkbook.createFont();
        MONOSPACE_FONT_10_POINT.setFontName(WorkbookEnvironment.MONOSPACE_FONT_NAME);
        MONOSPACE_FONT_10_POINT.setFontHeightInPoints(WorkbookEnvironment.FONT_SIZE_10_POINT);
        MONOSPACE_FONT_10_POINT.setBold(false);

        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED = xssfWorkbook.createCellStyle();
        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED.setFont(MONOSPACE_FONT_10_POINT);
        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED.setVerticalAlignment(VerticalAlignment.CENTER);
        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED.setAlignment(HorizontalAlignment.RIGHT);
        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED.setAlignment(HorizontalAlignment.LEFT);
        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED.setBorderBottom(BorderStyle.THIN);
        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED.setBorderTop(BorderStyle.THIN);
        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED.setBorderRight(BorderStyle.THIN);
        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED.setBorderLeft(BorderStyle.THIN);
        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

        MONOSPACE_FONT_10_POINT_ALIGN_CENTER_BORDERED = xssfWorkbook.createCellStyle();
        MONOSPACE_FONT_10_POINT_ALIGN_CENTER_BORDERED.cloneStyleFrom(MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED);
        MONOSPACE_FONT_10_POINT_ALIGN_CENTER_BORDERED.setAlignment(HorizontalAlignment.CENTER);

        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_DOUBLE = xssfWorkbook.createCellStyle();
        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_DOUBLE.cloneStyleFrom(MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED);
        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_DOUBLE.setDataFormat(xssfWorkbook.createDataFormat().getFormat(WorkbookEnvironment.DOUBLE_VALUE_FORMAT));

        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_INTEGER = xssfWorkbook.createCellStyle();
        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_INTEGER.cloneStyleFrom(MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED);
        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_INTEGER.setDataFormat(xssfWorkbook.createDataFormat().getFormat(WorkbookEnvironment.INTEGER_VALUE_FORMAT));

        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_CURRENCY = xssfWorkbook.createCellStyle();
        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_CURRENCY.cloneStyleFrom(MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED);
        MONOSPACE_FONT_10_POINT_ALIGN_RIGHT_BORDERED_CURRENCY.setDataFormat(xssfWorkbook.createDataFormat().getFormat(WorkbookEnvironment.CURRENCY_VALUE_FORMAT));

        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW = xssfWorkbook.createCellStyle();
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW.setDataFormat(xssfWorkbook.createDataFormat().getFormat(WorkbookEnvironment.DOUBLE_VALUE_FORMAT));
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW.setFont(MONOSPACE_FONT_10_POINT_BOLD);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW.setAlignment(HorizontalAlignment.RIGHT);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW.setVerticalAlignment(VerticalAlignment.CENTER);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW.setFillForegroundColor(WorkbookEnvironment.CELL_BACKGROUND_YELLOW);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW.setBorderBottom(BorderStyle.THIN);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW.setBorderTop(BorderStyle.THIN);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW.setBorderRight(BorderStyle.THIN);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW.setBorderLeft(BorderStyle.THIN);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_INTEGER_YELLOW = xssfWorkbook.createCellStyle();
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_INTEGER_YELLOW.cloneStyleFrom(MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_INTEGER_YELLOW.setDataFormat(xssfWorkbook.createDataFormat().getFormat(WorkbookEnvironment.INTEGER_VALUE_FORMAT));

        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_CURRENCY_YELLOW = xssfWorkbook.createCellStyle();
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_CURRENCY_YELLOW.cloneStyleFrom(MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_DOUBLE_YELLOW);
        MONOSPACE_FONT_10_POINT_BOLD_ALIGN_RIGHT_BORDERED_CURRENCY_YELLOW.setDataFormat(xssfWorkbook.createDataFormat().getFormat(WorkbookEnvironment.CURRENCY_VALUE_FORMAT));
    }
}
