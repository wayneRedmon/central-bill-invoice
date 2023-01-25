package com.prairiefarms;

import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class XFi_Invoice_XLS implements XFi_Invoice {

    private static final String xlsTemplate = "templates/Invoice.xls";

    private Invoice invoice;
    private Location location;
    private ArrayList<Item> itemList;
    private Item item;

    private String workbookName;

    private int currentRow;
    private int summaryRow;

    private POIFSFileSystem fileSystem;

    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private HSSFSheet summarySheet;
    private HSSFSheet logoSheet;
    private HSSFPatriarch drawingPatricarch;
    private HSSFPicture logoImage;

    private Row row;
    private Row sourceRow;
    private Row newRow;
    private Cell cell;
    private Cell oldCell;
    private Cell newCell;

    private CreationHelper helper;
    private ClientAnchor logoAnchor;

    private FileInputStream logoFile;
    private FileOutputStream fileSystem_Out;

    private byte[] bytes;
    private int logoIndex;

    private String columnLetter;
    private int itemCount;


    public void open(String title) {
        try {
            workbookName = title.trim();

            fileSystem = new POIFSFileSystem(new FileInputStream(xlsTemplate.trim()));

            workbook = new HSSFWorkbook(fileSystem, true);

            summarySheet = workbook.getSheetAt(0);

            logoSheet = summarySheet;

            setLogo();

            summaryRow = 16;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void addInvoice(Invoice invoice, String frequency) {
        this.invoice = invoice;

        sheet = workbook.cloneSheet(1);

        sheet.setDefaultRowHeightInPoints(18f);

        if (invoice.isUseExtendedID()) {
            workbook.setSheetName(workbook.getSheetIndex(sheet), "Invoice #" + invoice.getExtendedID());

        } else {
            workbook.setSheetName(workbook.getSheetIndex(sheet), "Invoice #" + Integer.toString(invoice.getID()));
        }

        currentRow = 16;

        logoSheet = sheet;
        setLogo();

        setBlock_Remit();
        setBlock_Customer();
        setBlock_Sales();
        setBlock_Item();
        setBlock_Total();
        setItems();
        setBlock_Summary();

        for (int x = currentRow + 1; x < (currentRow + 10); x++) {
            sheet.getRow(x).setHeightInPoints(sheet.getDefaultRowHeightInPoints());
        }
    }

    public void setLogo() {
        try {
            logoFile = new FileInputStream(location.getLogo());
            bytes = IOUtils.toByteArray(logoFile);
            logoIndex = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            logoFile.close();

            helper = workbook.getCreationHelper();

            logoAnchor = helper.createClientAnchor();
            logoAnchor.setRow1(0);
            logoAnchor.setCol1(0);
            logoAnchor.setRow2(1);
            logoAnchor.setCol2(0);

            drawingPatricarch = logoSheet.createDrawingPatriarch();

            logoImage = drawingPatricarch.createPicture(logoAnchor, logoIndex);

            logoImage.resize();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setBlock_Summary() {
        summaryRow++;

        if (summaryRow > 30) {
            sourceRow = summarySheet.getRow(17);

            newRow = summarySheet.getRow(summaryRow);

            summarySheet.shiftRows(summaryRow, summaryRow + 1, 1);
            summarySheet.getRow(summaryRow + 1).setHeightInPoints(summarySheet.getDefaultRowHeightInPoints());

            for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
                columnLetter = CellReference.convertNumToColString(i);

                oldCell = sourceRow.getCell(i);
                newCell = newRow.createCell(i);

                if (oldCell == null) {
                    newCell = null;
                    continue;
                }

                newCell.setCellStyle(oldCell.getCellStyle());

                if (newCell.getCellComment() != null) {
                    newCell.setCellComment(oldCell.getCellComment());
                }

                if (oldCell.getHyperlink() != null) {
                    newCell.setHyperlink(oldCell.getHyperlink());
                }

                newCell.setCellType(oldCell.getCellType());

                switch (oldCell.getCellType()) {
                    case Cell.CELL_TYPE_BLANK:
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        newCell.setCellValue(oldCell.getBooleanCellValue());
                        break;
                    case Cell.CELL_TYPE_ERROR:
                        newCell.setCellErrorValue(oldCell.getErrorCellValue());
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        newCell.setCellFormula(oldCell.getCellFormula());
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        newCell.setCellValue(oldCell.getNumericCellValue());
                        break;
                    case Cell.CELL_TYPE_STRING:
                        newCell.setCellValue(oldCell.getRichStringCellValue());
                        if (columnLetter.equals("A")) {
                            summarySheet.addMergedRegion(new CellRangeAddress(summaryRow, summaryRow, 0, 2));
                        }
                        break;
                }
            }
        }

        invoice.setDiscount((invoice.getSubTotal() - invoice.getApplied()) * invoice.getBilling().getDiscountRateFixed());
        invoice.setTax((invoice.getSubTotal() - invoice.getApplied() - invoice.getBilling().getDiscountRateFixed()) * invoice.getCustomer().getTaxRateFixed());
        invoice.setTotal(invoice.getSubTotal() - invoice.getApplied() - invoice.getDiscount() + invoice.getTax());

        row = summarySheet.getRow(summaryRow);
        cell = row.getCell(0);
        cell.setCellValue("[" + invoice.getCustomer().getID() + "]  " + invoice.getCustomer().getName().trim());
        cell = row.getCell(3);

        if (invoice.isUseExtendedID()) {
            cell.setCellValue(invoice.getExtendedID());

        } else {
            cell.setCellValue(invoice.getID());
        }

        cell = row.getCell(4);
        cell.setCellValue(invoice.getDeliveryDate_USA());
        cell = row.getCell(5);
        cell.setCellValue(invoice.getTotal());
    }

    public void setBlock_SummaryHeader() {
    }

    public void setBlock_SummaryFooter() {
    }

    public void setBlock_Remit() {
        row = sheet.getRow(0);
        cell = row.getCell(1);
        cell.setCellValue(invoice.getRemit().getName().trim());
        row = summarySheet.getRow(0);
        cell = row.getCell(1);
        cell.setCellValue(invoice.getRemit().getName().trim());

        row = sheet.getRow(1);
        cell = row.getCell(1);
        cell.setCellValue(invoice.getRemit().getAddress1().trim());
        cell = row.getCell(6);
        cell.setCellValue(invoice.getInvoiceDate_USA());
        row = summarySheet.getRow(1);
        cell = row.getCell(1);
        cell.setCellValue(invoice.getRemit().getAddress1().trim());
        cell = row.getCell(6);
        cell.setCellValue(invoice.getInvoiceDate_USA());

        row = sheet.getRow(2);
        cell = row.getCell(1);
        cell.setCellValue(invoice.getRemit().getAddress_Formatted().trim());
        cell = row.getCell(6);

        if (invoice.isUseExtendedID()) {
            cell.setCellValue(invoice.getExtendedID());
        } else {
            cell.setCellValue(invoice.getID());
        }

        row = summarySheet.getRow(2);
        cell = row.getCell(1);
        cell.setCellValue(invoice.getRemit().getAddress_Formatted().trim());

        row = sheet.getRow(3);
        cell = row.getCell(1);
        cell.setCellValue(invoice.getRemit().getPhone_Formatted().trim());
        cell = row.getCell(6);
        cell.setCellValue(location.getID()
                + "-"
                + invoice.getBilling().getID()
                + "-"
                + invoice.getCustomer().getID());
        row = summarySheet.getRow(3);
        cell = row.getCell(1);
        cell.setCellValue(invoice.getRemit().getPhone_Formatted().trim());

        row = sheet.getRow(4);
        cell = row.getCell(1);
        cell.setCellValue(invoice.getRemit().getFax_Formatted().trim());
        row = summarySheet.getRow(4);
        cell = row.getCell(1);
        cell.setCellValue(invoice.getRemit().getFax_Formatted().trim());
    }

    public void setBlock_Customer() {
        row = sheet.getRow(8);
        cell = row.getCell(0);
        cell.setCellValue(invoice.getBilling().getName().trim());
        cell = row.getCell(4);
        cell.setCellValue(invoice.getCustomer().getName().trim());
        row = summarySheet.getRow(8);
        cell = row.getCell(0);
        cell.setCellValue(invoice.getBilling().getName().trim());

        row = sheet.getRow(9);
        cell = row.getCell(0);
        cell.setCellValue(invoice.getBilling().getAddress1().trim());
        cell = row.getCell(4);
        cell.setCellValue(invoice.getCustomer().getAddress1().trim());
        row = summarySheet.getRow(9);
        cell = row.getCell(0);
        cell.setCellValue(invoice.getBilling().getAddress1().trim());

        row = sheet.getRow(10);
        cell = row.getCell(0);
        cell.setCellValue(invoice.getBilling().getAddress_Formatted().trim());
        cell = row.getCell(4);
        cell.setCellValue(invoice.getCustomer().getAddress_Formatted().trim());
        row = summarySheet.getRow(10);
        cell = row.getCell(0);
        cell.setCellValue(invoice.getBilling().getAddress_Formatted().trim());

        row = sheet.getRow(11);
        cell = row.getCell(0);
        cell.setCellValue(invoice.getBilling().getPhone_Formatted().trim());
        cell = row.getCell(4);
        cell.setCellValue(invoice.getCustomer().getPhone_Formatted().trim());
        row = summarySheet.getRow(11);
        cell = row.getCell(0);
        cell.setCellValue(invoice.getBilling().getPhone_Formatted().trim());
    }

    public void setBlock_Sales() {
        row = sheet.getRow(14);
        cell = row.getCell(0);
        cell.setCellValue(invoice.getSalesperson().getName().trim());
        cell = row.getCell(2);
        cell.setCellValue(invoice.getPurchaseOrder().getContract().trim());
        cell = row.getCell(4);
        cell.setCellValue(invoice.getDeliveryDate_USA());
        cell = row.getCell(5);
        cell.setCellValue(invoice.getTerms().getDueByDate_USA());
    }

    public void setBlock_Item() {
    }

    public void setItems() {
        itemList = new ArrayList<Item>(invoice.getItem());

        itemCount = 0;

        for (int x = 0; x < itemList.size(); x++) {
            item = new Item();

            item = itemList.get(x);

            currentRow++;

            itemCount++;

            if (itemCount > 14 && itemCount < itemList.size()) {
                sourceRow = sheet.getRow(17);

                newRow = sheet.getRow(currentRow);

                sheet.shiftRows(currentRow, sheet.getLastRowNum(), 1);
                sheet.getRow(currentRow + 1).setHeightInPoints(sheet.getDefaultRowHeightInPoints());

                for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
                    columnLetter = CellReference.convertNumToColString(i);

                    oldCell = sourceRow.getCell(i);
                    newCell = newRow.createCell(i);

                    if (oldCell == null) {
                        newCell = null;
                        continue;
                    }

                    newCell.setCellStyle(oldCell.getCellStyle());

                    if (newCell.getCellComment() != null) {
                        newCell.setCellComment(oldCell.getCellComment());
                    }

                    if (oldCell.getHyperlink() != null) {
                        newCell.setHyperlink(oldCell.getHyperlink());
                    }

                    newCell.setCellType(oldCell.getCellType());

                    switch (oldCell.getCellType()) {
                        case Cell.CELL_TYPE_BLANK:
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            newCell.setCellValue(oldCell.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_ERROR:
                            newCell.setCellErrorValue(oldCell.getErrorCellValue());
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            if (columnLetter.equals("G")) {
                                String strFormula = "E" + (currentRow + 1) + "*" + "F" + (currentRow + 1);
                                newCell.setCellFormula(strFormula.trim());
                            } else {
                                newCell.setCellFormula(oldCell.getCellFormula());
                            }
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            newCell.setCellValue(oldCell.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_STRING:
                            newCell.setCellValue(oldCell.getRichStringCellValue());
                            if (columnLetter.equals("B")) {
                                sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 1, 3));
                            }
                            break;
                    }
                }
            }

            row = sheet.getRow(currentRow);
            cell = row.getCell(0);
            cell.setCellValue(item.getID());
            cell = row.getCell(1);
            cell.setCellValue(" " + item.getDescription().trim());
            cell = row.getCell(4);
            cell.setCellValue(item.getQuantity());
            cell = row.getCell(5);
            cell.setCellValue(item.getPrice());
        }
    }

    public void setBlock_Total() {
        row = sheet.getRow(33);
        cell = row.getCell(6);
        cell.setCellValue(invoice.getApplied());

        row = sheet.getRow(34);
        cell = row.getCell(0);
        cell.setCellValue("1. Terms are " + invoice.getTerms().getDescription().trim());
        if (invoice.getBilling().getDiscountRateRaw() != 0) {
            cell = row.getCell(3);
            cell.setCellValue("[" + invoice.getBilling().getDiscountRateRaw() + "%]");
            cell = row.getCell(6);
            cell.setCellValue(invoice.getDiscount());
        }

        if (invoice.getCustomer().getTaxRateRaw() != 0) {
            row = sheet.getRow(35);
            cell = row.getCell(3);
            cell.setCellValue("[" + invoice.getCustomer().getTaxRateRaw() + "%]");
            cell = row.getCell(6);
            cell.setCellValue(invoice.getTax());
        }

        row = sheet.getRow(37);
        cell = row.getCell(0);
        cell.setCellValue(location.getDescription().trim());

        row = sheet.getRow(39);
        cell = row.getCell(0);
        cell.setCellValue("Accounts Receivables at " + invoice.getRemit().getPhone_Formatted().trim() + ".");
    }

    public void setBlock_ProductSummary() {

    }

    public void setBlock_ThankYou() {
    }

    public void close() {
        workbook.removeSheetAt(1);

        try {
            fileSystem_Out = new FileOutputStream(workbookName.trim());

            workbook.write(fileSystem_Out);

            fileSystem_Out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProductSummary(ArrayList<Item> items) {
        // TODO Auto-generated method stub
    }
}
