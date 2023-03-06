package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.instruction;

import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class InstructionCell {

    public static void set(XSSFSheet sheet) {
        XSSFCell cell = sheet.getRow(9).getCell(4);
        cell.setCellStyle(WorkbookEnvironment.getInstance().getInstructionStyle());
        cell.setCellType(CellType.STRING);
        cell.setCellValue(Environment.getInstance().getCorporateName());
    }
}
