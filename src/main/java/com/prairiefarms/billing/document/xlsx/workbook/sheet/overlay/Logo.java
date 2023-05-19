package com.prairiefarms.billing.document.xlsx.workbook.sheet.overlay;

import com.prairiefarms.billing.Environment;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class Logo {

    public static void stamp(XSSFSheet xssfSheet) throws IOException {
        int pictureIndex;

        try (FileInputStream logoFile = new FileInputStream(Environment.getInstance().getDairyLogoPath())) {
            pictureIndex = xssfSheet.getWorkbook().addPicture(IOUtils.toByteArray(logoFile), XSSFWorkbook.PICTURE_TYPE_PNG);
        }

        ClientAnchor clientAnchor = xssfSheet.getWorkbook().getCreationHelper().createClientAnchor();
        clientAnchor.setRow1(0);
        clientAnchor.setCol1(0);
        clientAnchor.setRow2(1);
        clientAnchor.setCol2(0);

        Picture logoImage = xssfSheet.createDrawingPatriarch().createPicture(clientAnchor, pictureIndex);
        logoImage.resize();
    }
}
