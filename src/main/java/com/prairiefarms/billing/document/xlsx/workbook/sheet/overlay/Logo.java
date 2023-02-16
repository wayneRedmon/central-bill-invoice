package com.prairiefarms.billing.document.xlsx.workbook.sheet.overlay;

import com.prairiefarms.billing.Environment;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class Logo {

    private final XSSFWorkbook xssfWorkbook;

    public Logo(XSSFWorkbook xssfWorkbook) {
        this.xssfWorkbook = xssfWorkbook;
    }

    public void stamp() throws IOException {
        final int DOCUMENT_STATIC_SHEETS = 3;

        FileInputStream logoFile = null;
        int logoIndex = 0;

        for (int sheetIndex = 0; sheetIndex < DOCUMENT_STATIC_SHEETS; sheetIndex++) {
            XSSFSheet cloneableSheet = xssfWorkbook.getSheetAt(sheetIndex);
            cloneableSheet.setDefaultRowHeightInPoints(18f);

            if (ObjectUtils.isEmpty(logoFile)) {
                logoFile = new FileInputStream(Environment.getInstance().getDairyLogoPath());
                byte[] bytes = IOUtils.toByteArray(logoFile);
                logoIndex = xssfWorkbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);
                logoFile.close();
            }

            ClientAnchor logoAnchor;
            logoAnchor = xssfWorkbook.getCreationHelper().createClientAnchor();
            logoAnchor.setRow1(0);
            logoAnchor.setCol1(0);
            logoAnchor.setRow2(1);
            logoAnchor.setCol2(0);

            Picture logoImage = cloneableSheet.createDrawingPatriarch().createPicture(logoAnchor, logoIndex);
            logoImage.resize();
        }
    }
}
