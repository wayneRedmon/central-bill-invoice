package com.prairiefarms.billing.invoice.centralBill;

import com.prairiefarms.billing.centralBill.CentralBill;
import com.prairiefarms.billing.invoice.centralBill.customer.CustomerInvoice;

import java.util.List;

public class CentralBillInvoice {

    private final CentralBill centralBill;
    private final List<CustomerInvoice> customerInvoices;

    public CentralBillInvoice(CentralBill centralBill,
                              List<CustomerInvoice> customerInvoices) {
        this.centralBill = centralBill;
        this.customerInvoices = customerInvoices;
    }

    public CentralBill getCentralBill() {
        return centralBill;
    }

    public List<CustomerInvoice> getCustomerInvoices() {
        return customerInvoices;
    }

    public double getAmountDue() {
        return customerInvoices.stream().mapToDouble(CustomerInvoice::getAmountDue).sum();
    }

}
