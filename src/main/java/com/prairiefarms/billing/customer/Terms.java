package com.prairiefarms.billing.customer;

import org.apache.commons.text.WordUtils;

public class Terms {

    private final String name;

    public Terms(String name) {
        this.name = name;
    }

    public String getName() {
        return WordUtils.swapCase(name);
    }
}
