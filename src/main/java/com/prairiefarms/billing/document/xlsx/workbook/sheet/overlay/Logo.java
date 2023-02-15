package com.prairiefarms.billing.document.xlsx.workbook.sheet.overlay;

import com.prairiefarms.billing.Environment;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;

public class Logo {

    private static final Logger LOGGER = LoggerFactory.getLogger(Logo.class);

    private final XSSFWorkbook workbook;

    public Logo(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public void stamp(XSSFSheet sheet) {
        try {
            FileInputStream logoFile = new FileInputStream(Environment.getInstance().getDairyLogoPath());

            byte[] bytes = IOUtils.toByteArray(logoFile);
            int logoIndex = workbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);
            logoFile.close();

            ClientAnchor logoAnchor;
            logoAnchor = workbook.getCreationHelper().createClientAnchor();
            logoAnchor.setRow1(0);
            logoAnchor.setCol1(0);
            logoAnchor.setRow2(1);
            logoAnchor.setCol2(0);

            Picture logoImage = sheet.createDrawingPatriarch().createPicture(logoAnchor, logoIndex);
            logoImage.resize();
        } catch (Exception exception) {
            LOGGER.error("Exception in Logo.stamp()", exception);
        }
    }
}
