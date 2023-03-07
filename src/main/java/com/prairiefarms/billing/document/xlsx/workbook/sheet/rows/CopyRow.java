package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.document.xlsx.workbook.WorkbookEnvironment;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class CopyRow {

    public static void set(XSSFSheet sheet, int rowToInsert, int rowToCopy) {
        sheet.shiftRows(
                rowToInsert,
                sheet.getLastRowNum() + 1,
                1,
                true,
                false
        );

        sheet.copyRows(
                rowToCopy,
                rowToCopy,
                rowToInsert,
                WorkbookEnvironment.getInstance().CELL_COPY_POLICY
        );
    }
}
