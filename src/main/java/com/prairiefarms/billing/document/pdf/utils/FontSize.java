package com.prairiefarms.billing.document.pdf.utils;

public enum FontSize {

    LABEL(9),
    SMALL(10),
    MEDIUM(11),
    LARGE(12);

    public final float asFloat;

    FontSize(float asFloat) {
        this.asFloat = asFloat;
    }
}
