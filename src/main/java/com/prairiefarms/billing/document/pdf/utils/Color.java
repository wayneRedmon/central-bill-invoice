package com.prairiefarms.billing.document.pdf.utils;

import com.itextpdf.kernel.colors.DeviceRgb;

public enum Color {

    BLACK(0, 0, 0),
    BLUE(207, 231, 245),
    GREEN(180, 248, 91),
    WHITE(255, 255, 255),
    LIGHT_GRAY(167, 167, 167),
    RED(255, 0, 0);

    private final int red;
    private final int green;
    private final int blue;

    Color(final int red, final int green, final int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public DeviceRgb asDeviceRgb() {
        return new DeviceRgb(red, green, blue);
    }
}
