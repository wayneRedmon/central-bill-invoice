package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.remit.Remit;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class RemitToRows {

    public static void set(XSSFSheet xssfSheet, Remit remit, String title) {
        XSSFRow xssfRow = xssfSheet.createRow(0);

        XSSFCell xssfCell = xssfRow.createCell(1);
        //xssfCell.setCellStyle(CellStyleMap.getInstance().getStyle("PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_CENTER"));
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(StringUtils.normalizeSpace(remit.getContact().getName()));
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 1, 4));

        xssfCell = xssfRow.createCell(6);
        //xssfCell.setCellStyle(CellStyleMap.getInstance().getStyle("PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_RIGHT"));
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(title);

        xssfRow = xssfSheet.createRow(1);

        xssfCell = xssfRow.createCell(1);
        //xssfCell.setCellStyle(CellStyleMap.getInstance().getStyle("PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_CENTER"));
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(remit.getContact().getStreet());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 1, 4));

        xssfRow = xssfSheet.createRow(2);

        xssfCell = xssfRow.createCell(1);
        //xssfCell.setCellStyle(CellStyleMap.getInstance().getStyle("PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_CENTER"));
        xssfCell.setCellType(CellType.STRING);
        xssfCell.setCellValue(remit.getContact().getAddress());
        xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 1, 4));

        if (StringUtils.isNotBlank(remit.getContact().getPhone())) {
            xssfRow = xssfSheet.createRow(3);

            xssfCell = xssfRow.createCell(1);
            //xssfCell.setCellStyle(CellStyleMap.getInstance().getStyle("PROPORTIONAL_FONT_12_POINT_BOLD_ALIGN_CENTER"));
            xssfCell.setCellType(CellType.STRING);
            xssfCell.setCellValue("Phone: " + remit.getContact().getPhone());
            xssfSheet.addMergedRegion(new CellRangeAddress(xssfRow.getRowNum(), xssfRow.getRowNum(), 1, 4));
        }
    }
}
