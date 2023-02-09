package com.prairiefarms.billing.invoice.centralBill.customer;

import com.prairiefarms.billing.customer.Customer;
import com.prairiefarms.billing.invoice.Invoice;

import java.util.List;

public class CustomerInvoice {

    private final Customer customer;
    private final List<Invoice> invoices;

    public CustomerInvoice(Customer customer,
                           List<Invoice> invoices) {
        this.customer = customer;
        this.invoices = invoices;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getId() { return customer.getContact().getId(); }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public double getAmountDue() {
        return invoices.stream().mapToDouble(Invoice::amountDue).sum();
    }

    public int getSortSequence() { return customer.getSortSequence(); }
}
