package com.prairiefarms.billing.invoice;

import com.prairiefarms.billing.Environment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Header {

    private final int id;
    private final LocalDate deliveryDate;
    private final String purchaseOrder;
    private final boolean distributor;

    public Header(int id,
                  LocalDate deliveryDate,
                  String purchaseOrder,
                  boolean distributor) {
        this.id = id;
        this.deliveryDate = deliveryDate;
        this.purchaseOrder = purchaseOrder;
        this.distributor = distributor;
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

    public boolean isDistributor() {
        return distributor;
    }

    public String getExtendedId() {
        return String.format("%03d", Environment.getInstance().getDairyId()) +
                deliveryDate.format(DateTimeFormatter.ofPattern("MMddyy")) +
                String.format("%07d", id);
    }
}
