package com.prairiefarms.billing.document.pdf;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Image;
import com.prairiefarms.billing.BillingEnvironment;
import com.prairiefarms.billing.document.pdf.utils.Color;
import org.apache.commons.lang3.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class PdfEnvironment {

    public final float DEFAULT_CELL_PADDING = 0.5f;
    public final SolidBorder LABEL_BORDER = new SolidBorder(Color.LIGHT_GRAY.asDeviceRgb(), 0.5f);

    private static PdfFont fontDefault;
    private static PdfFont fontBold;
    private static PdfFont itemFont;
    private static PdfFont itemFontBold;
    private static Image logoImage;

    private static class SingletonHelper {
        private static final PdfEnvironment INSTANCE = new PdfEnvironment();
    }

    PdfEnvironment() {
    }

    public static PdfEnvironment getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public void init() throws IOException {
        setFonts();
        setLogoImage();
    }

    private static void setLogoImage() throws MalformedURLException {
        final File logoFile = new File(BillingEnvironment.getInstance().getDairyLogoPath());

        if (ObjectUtils.isNotEmpty(logoFile))
            logoImage = new Image(ImageDataFactory.create(logoFile.getAbsolutePath()));
    }

    public Image getLogoImage() {
        return logoImage;
    }

    private static void setFonts() throws IOException {
        fontDefault = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        itemFont = PdfFontFactory.createFont(StandardFonts.COURIER);
        itemFontBold = PdfFontFactory.createFont(StandardFonts.COURIER_BOLD);
    }

    public PdfFont getFontDefault() {
        return fontDefault;
    }

    public PdfFont getFontBold() {
        return fontBold;
    }

    public PdfFont getItemFont() {
        return itemFont;
    }

    public PdfFont getItemFontBold() {
        return itemFontBold;
    }

    public int getPageCount(int linesPerPage, int lines) {
        linesPerPage = linesPerPage <= 0 ? 1 : linesPerPage;
        lines = lines <= 0 ? 1 : lines;

        return lines % linesPerPage != 0 ? lines / linesPerPage + 1 : lines / linesPerPage;
    }
}
