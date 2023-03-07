package com.prairiefarms.billing.document;

import com.prairiefarms.billing.centralBill.CentralBill;

public class DocumentThread {

    private final CentralBill centralBill;
    private final Exception exception;

    public DocumentThread(CentralBill centralBill,
                          Exception exception) {
        this.centralBill = centralBill;
        this.exception = exception;
    }

    public CentralBill getCentralBill() {
        return centralBill;
    }

    public Exception getException() {
        return exception;
    }
}
