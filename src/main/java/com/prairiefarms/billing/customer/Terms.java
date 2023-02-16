package com.prairiefarms.billing.customer;

import org.apache.commons.text.WordUtils;

public class Terms {

    private final String name;
    private final int dueByDays;

    public Terms(String name,
                 int dueByDays) {
        this.name = name;
        this.dueByDays = dueByDays;
    }

    public String getName() {
        return WordUtils.swapCase(name);
    }

    public int getDueByDays() { return dueByDays; }
}
