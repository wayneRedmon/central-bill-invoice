package com.prairiefarms.billing.remit;

import com.prairiefarms.billing.utils.Contact;

public class Remit {

    private final Contact contact;

    public Remit(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }
}
