package com.prairiefarms.billing.document.pdf.pages.tables;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Image;
import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.document.pdf.utils.Color;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

class TableBase {

    protected static final float DEFAULT_CELL_PADDING = 0.5f;
    protected static final SolidBorder LABEL_BORDER = new SolidBorder(Color.LIGHT_GRAY.asDeviceRgb(), 0.5f);

    public static PdfFont getFontDefault() throws IOException {
        return PdfFontFactory.createFont(StandardFonts.HELVETICA);
    }

    public static PdfFont getFontBold() throws IOException {
        return PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
    }

    public static PdfFont getItemFont() throws IOException {
        return PdfFontFactory.createFont(StandardFonts.COURIER);
    }

    public static PdfFont getItemFontBold() throws IOException {
        return PdfFontFactory.createFont(StandardFonts.COURIER_BOLD);
    }

    public static Image getLogoImage() throws MalformedURLException {
        return new Image(ImageDataFactory.create(new File(Environment.getInstance().getDairyLogoPath()).getAbsolutePath()));
    }
}
