package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import com.prairiefarms.billing.invoice.item.Item;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;
import java.util.List;

public class ItemSummaryRow {

    private final XSSFSheet xssfSheet;

    private final List<String> pointsSubtotalCellReferences = new ArrayList<>();
    private final List<String> quantitySubtotalCellReferences = new ArrayList<>();
    private final List<String> amountDueSubtotalCellReferences = new ArrayList<>();

    public ItemSummaryRow(XSSFSheet xssfSheet) {
        this.xssfSheet = xssfSheet;
    }

    public void set(int rowNumber, Item item) {
        XSSFCell cell = xssfSheet.getRow(rowNumber).getCell(0);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getItemIdStyle());
        cell.setCellType(CellType.STRING);
        cell.setCellValue(item.getSalesType().equals("A") ? String.valueOf(item.getId()) : item.getIdAsAccount());

        cell = xssfSheet.getRow(rowNumber).getCell(1);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getItemNameStyle());
        cell.setCellType(CellType.STRING);
        cell.setCellValue(item.getName());

        CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNumber, rowNumber, 1, 2);
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, xssfSheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, xssfSheet);

        cell = xssfSheet.getRow(rowNumber).getCell(3);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getPointsColumnStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(item.getTotalPoints());
        pointsSubtotalCellReferences.add(cell.getReference());

        cell = xssfSheet.getRow(rowNumber).getCell(4);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getQuantityColumnStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(item.getQuantity());
        quantitySubtotalCellReferences.add(cell.getReference());

        cell = xssfSheet.getRow(rowNumber).getCell(5);
        cell.setCellStyle(item.isPromotion() ?
                WorkbookEnvironment.getInstance().getPromotionPriceStyle() :
                WorkbookEnvironment.getInstance().getPriceEachStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(item.getPriceEach());

        cell = xssfSheet.getRow(rowNumber).getCell(6);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getTotalColumnStyle());
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(item.getExtension());
        amountDueSubtotalCellReferences.add(cell.getReference());
    }

    public void setTotal(int rowNumber) {
        rowNumber++;

        XSSFCell cell = xssfSheet.getRow(rowNumber).getCell(3);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getPointsTotalStyle());
        cell.setCellFormula("SUM(" + pointsSubtotalCellReferences.get(0) + ":" + pointsSubtotalCellReferences.get(pointsSubtotalCellReferences.size() - 1) + ")");

        cell = xssfSheet.getRow(rowNumber).getCell(4);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getQuantityTotalStyle());
        cell.setCellFormula("SUM(" + quantitySubtotalCellReferences.get(0) + ":" + quantitySubtotalCellReferences.get(pointsSubtotalCellReferences.size() - 1) + ")");

        cell = xssfSheet.getRow(rowNumber).getCell(6);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getAmountDueTotalStyle());
        cell.setCellFormula("SUM(" + amountDueSubtotalCellReferences.get(0) + ":" + amountDueSubtotalCellReferences.get(pointsSubtotalCellReferences.size() - 1) + ")");
    }
}
