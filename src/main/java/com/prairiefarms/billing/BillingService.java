package com.prairiefarms.billing;

import com.prairiefarms.billing.accountsReceivable.OpenDAO;
import com.prairiefarms.billing.centralBill.CentralBill;
import com.prairiefarms.billing.centralBill.CentralBillDAO;
import com.prairiefarms.billing.customer.Customer;
import com.prairiefarms.billing.customer.CustomerDAO;
import com.prairiefarms.billing.document.pdf.PdfService;
import com.prairiefarms.billing.invoice.Header;
import com.prairiefarms.billing.invoice.Invoice;
import com.prairiefarms.billing.invoice.centralBill.CentralBillInvoice;
import com.prairiefarms.billing.invoice.centralBill.customer.CustomerInvoice;
import com.prairiefarms.billing.invoice.item.Item;
import com.prairiefarms.billing.sale.SaleDAO;
import com.prairiefarms.utils.database.HostConnection;
import org.apache.commons.lang3.ObjectUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BillingService {

    private List<CentralBillInvoice> centralBillInvoices;

    public BillingService() {
    }

    public void init() throws Exception {
        getCentralBillInvoices();

        if (ObjectUtils.isNotEmpty(centralBillInvoices)) {
            ExecutorService executorService = Executors.newFixedThreadPool(centralBillInvoices.size());

            for (CentralBillInvoice centralBillInvoice : centralBillInvoices) {
                if (centralBillInvoice.getCentralBill().getDocumentType().fileExtension.equals(".pdf")) {
                    Runnable runnableWorker = new PdfService(centralBillInvoice);
                    executorService.execute(runnableWorker);
                }
            }

            executorService.shutdown();

            while (!executorService.isTerminated()) {
                //ignore;
            }
        }
    }

    private void getCentralBillInvoices() throws SQLException {
        centralBillInvoices = new ArrayList<>();

        try (HostConnection hostConnection =
                     new HostConnection(
                             BillingEnvironment.getInstance().getServer(),
                             BillingEnvironment.getInstance().getCredentials(),
                             BillingEnvironment.getInstance().getLibrary())) {
            final SaleDAO saleDAO = new SaleDAO(hostConnection.get());
            final CentralBillDAO centralBillDAO = new CentralBillDAO(hostConnection.get());
            final CustomerDAO customerDAO = new CustomerDAO(hostConnection.get());
            final OpenDAO openDAO = new OpenDAO(hostConnection.get());
            final List<Integer> centralBIlls = saleDAO.centralBillIdList();

            centralBillInvoices = new ArrayList<>();

            for (Integer centralBillId : centralBIlls) {
                CentralBill centralBill = centralBillDAO.get(centralBillId);

                List<CustomerInvoice> customerInvoices = new ArrayList<>();

                List<Integer> customerIds = saleDAO.customerIdList(centralBillId);

                for (Integer customerId : customerIds) {
                    Customer customer = customerDAO.get(customerId);

                    List<Invoice> invoices = new ArrayList<>();

                    List<Header> headers = saleDAO.headerList(centralBillId, customerId);

                    for (Header header : headers) {
                        List<Item> items = saleDAO.itemList(
                                centralBillId,
                                customerId,
                                header.getId()
                        );

                        if (ObjectUtils.isNotEmpty(items)) {
                            invoices.add(
                                    new Invoice(
                                            header,
                                            items,
                                            openDAO.get(customerId, header.getDeliveryDate(), header.getId()),
                                            centralBill.fixedDiscountRate(),
                                            customer.getTaxRate()
                                    )
                            );
                        }
                    }

                    if (ObjectUtils.isNotEmpty(invoices)) customerInvoices.add(
                            new CustomerInvoice(customer, invoices)
                    );
                }

                if (ObjectUtils.isNotEmpty(customerInvoices)) centralBillInvoices.add(
                        new CentralBillInvoice(centralBill, customerInvoices)
                );
            }
        }
    }
}
