package com.prairiefarms.billing.customer;

import com.prairiefarms.billing.utils.Contact;

import java.time.LocalDate;

public class Customer {

    private final Contact contact;
    private final Terms terms;
    private final String salesperson;
    private final boolean extendedInvoiceId;
    private final Double taxRate;
    private final int sortSequence;

    public Customer(Contact contact,
                    Terms terms,
                    String salesperson,
                    boolean extendedInvoiceId,
                    Double taxRate,
                    int sortSequence) {
        this.contact = contact;
        this.terms = terms;
        this.salesperson = salesperson;
        this.extendedInvoiceId = extendedInvoiceId;
        this.taxRate=taxRate;
        this.sortSequence = sortSequence;
    }

    public Contact getContact() {
        return contact;
    }

    public Terms getTerms() {
        return terms;
    }

    public String getSalesperson() {
        return salesperson;
    }

    public boolean isExtendedInvoiceId() {
        return extendedInvoiceId;
    }

    public Double getTaxRate() { return taxRate; }

    public int getSortSequence() { return sortSequence; }

    public LocalDate getDueByDate(LocalDate invoiceDeliveryDate) {
        return invoiceDeliveryDate.plusDays(terms.getDueByDays());
    }
}
