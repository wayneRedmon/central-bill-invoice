package com.prairiefarms.billing.invoice;

import com.prairiefarms.billing.BillingEnvironment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Header {

    private final int id;
    private final LocalDate deliveryDate;
    private final String purchaseOrder;

    public Header(int id,
                  LocalDate deliveryDate,
                  String purchaseOrder) {
        this.id = id;
        this.deliveryDate = deliveryDate;
        this.purchaseOrder = purchaseOrder;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public String getPurchaseOrder() {
        return purchaseOrder;
    }

    public String getExtendedId() {
        return String.format("%03d", BillingEnvironment.getInstance().getDairyId()) +
                deliveryDate.format(DateTimeFormatter.ofPattern("MMddyy")) +
                String.format("%07d", id);
    }
}
