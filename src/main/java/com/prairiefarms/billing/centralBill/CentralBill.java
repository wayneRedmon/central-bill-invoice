package com.prairiefarms.billing.centralBill;

import com.prairiefarms.billing.remit.Remit;
import com.prairiefarms.billing.utils.Contact;
import com.prairiefarms.billing.utils.DocumentType;

public class CentralBill {

    private final Contact contact;
    private final double discountRate;
    private final Remit remit;
    private final DocumentType documentType;

    public CentralBill(Contact contact,
                       double discountRate,
                       Remit remit,
                       DocumentType documentType) {
        this.contact = contact;
        this.discountRate = discountRate;
        this.remit = remit;
        this.documentType = documentType;
    }

    public Contact getContact() {
        return contact;
    }

    public Remit getRemit() {
        return remit;
    }

    public double fixedDiscountRate() {
        return Math.round((discountRate / 100f) * 100.0) / 100.0;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public String getIdAsText() {
        return String.format("%03d", contact.getId());
    }
}
