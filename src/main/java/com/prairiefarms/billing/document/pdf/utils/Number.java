package com.prairiefarms.billing.document.pdf.utils;

import java.text.DecimalFormat;

public enum Number {

    INTEGER(new DecimalFormat("#,##0;(#,##0)")),
    DOUBLE(new DecimalFormat("#,##0.00;(#,##0.00)")),
    CURRENCY(new DecimalFormat("$#,##0.00;($#,##0.00)")),
    PRICE(new DecimalFormat("##0.0000;(##0.0000)")),
    PERCENT(new DecimalFormat("#0.0"));

    public final DecimalFormat type;

    Number(DecimalFormat type) {
        this.type = type;
    }
}
