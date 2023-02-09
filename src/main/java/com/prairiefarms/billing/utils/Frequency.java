package com.prairiefarms.billing.utils;

public enum Frequency {

    W("Weekly"),
    M("Monthly");

    public final String type;

    Frequency(String type) {
        this.type = type;
    }
}
