package com.prairiefarms.billing;

import com.prairiefarms.billing.accountsReceivable.OpenDAO;
import com.prairiefarms.billing.centralBill.CentralBill;
import com.prairiefarms.billing.centralBill.CentralBillDAO;
import com.prairiefarms.billing.customer.Customer;
import com.prairiefarms.billing.customer.CustomerDAO;
import com.prairiefarms.billing.document.DocumentThread;
import com.prairiefarms.billing.document.pdf.PdfService;
import com.prairiefarms.billing.document.xlsx.XlsxService;
import com.prairiefarms.billing.invoice.Header;
import com.prairiefarms.billing.invoice.Invoice;
import com.prairiefarms.billing.invoice.centralBill.CentralBillInvoice;
import com.prairiefarms.billing.invoice.centralBill.customer.CustomerInvoice;
import com.prairiefarms.billing.invoice.item.Item;
import com.prairiefarms.billing.sale.SaleDAO;
import com.prairiefarms.billing.utils.DocumentType;
import com.prairiefarms.billing.utils.FolderMaintenance;
import com.prairiefarms.utils.database.HostConnection;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);

    private List<CentralBillInvoice> centralBillInvoices;

    public Service() {
    }

    public void init() throws Exception {
        FolderMaintenance.clean(Environment.getInstance().emailSentBox(), 7);

        centralBillInvoices = new ArrayList<>();

        this.getCentralBillInvoices();

        if (ObjectUtils.isNotEmpty(centralBillInvoices)) this.executeThreads();
    }

    private void getCentralBillInvoices() throws SQLException {
        centralBillInvoices = new ArrayList<>();

        try (HostConnection hostConnection =
                     new HostConnection(
                             Environment.getInstance().getServer(),
                             Environment.getInstance().getCredentials(),
                             Environment.getInstance().getLibrary())) {
            final SaleDAO saleDAO = new SaleDAO(hostConnection.get());
            final CentralBillDAO centralBillDAO = new CentralBillDAO(hostConnection.get());
            final CustomerDAO customerDAO = new CustomerDAO(hostConnection.get());
            final OpenDAO openDAO = new OpenDAO(hostConnection.get());

            for (Integer centralBillId : saleDAO.centralBillIdList()) {
                CentralBill centralBill = centralBillDAO.get(centralBillId);

                List<CustomerInvoice> customerInvoices = new ArrayList<>();

                for (Integer customerId : saleDAO.customerIdList(centralBillId)) {
                    Customer customer = customerDAO.get(customerId);

                    List<Invoice> invoices = new ArrayList<>();

                    for (Header header : saleDAO.headerList(centralBillId, customerId)) {
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

                    if (ObjectUtils.isNotEmpty(invoices))
                        customerInvoices.add(
                                new CustomerInvoice(customer, invoices)
                        );
                }

                if (ObjectUtils.isNotEmpty(customerInvoices))
                    centralBillInvoices.add(
                            new CentralBillInvoice(centralBill, customerInvoices)
                    );
            }
        }
    }

    private void executeThreads() throws InterruptedException, ExecutionException {
        Set<Callable<DocumentThread>> callables = new HashSet<>();

        for (CentralBillInvoice centralBillInvoice : centralBillInvoices) {
            if (DocumentType.S.fileExtension.equals(centralBillInvoice.getCentralBill().getDocumentType().fileExtension)) {
                callables.add(new PdfService(centralBillInvoice));
            } else if (DocumentType.W.fileExtension.equals(centralBillInvoice.getCentralBill().getDocumentType().fileExtension)) {
                callables.add(new XlsxService(centralBillInvoice));
            }
        }

        if (ObjectUtils.isNotEmpty(callables)) {
            ExecutorService executorService = Executors.newFixedThreadPool(callables.size());

            List<Future<DocumentThread>> futures = executorService.invokeAll(callables);

            for (Future<DocumentThread> future : futures) {
                if (ObjectUtils.isNotEmpty(future.get().getException())) {
                    LOGGER.error(
                            "\r\nException in Service.executeThreads() while processing " +
                                    "[" + String.format("%03d",future.get().getCentralBill().getContact().getId()) + "] " +
                                    future.get().getCentralBill().getContact().getName(),
                            future.get().getException().getCause()
                    );
                } else {
                    LOGGER.info(
                            "[" + String.format("%03d",future.get().getCentralBill().getContact().getId()) + "] " +
                                    future.get().getCentralBill().getContact().getName() +
                                    "   *** OK ***"
                    );
                }
            }

            executorService.shutdown();
        }
    }
}
