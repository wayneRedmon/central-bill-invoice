package com.prairiefarms.billing.document.xlsx.workbook.sheet.rows;

import com.prairiefarms.billing.Environment;
import com.prairiefarms.billing.centralBill.CentralBill;
import com.prairiefarms.billing.customer.Customer;
import com.prairiefarms.billing.document.xlsx.workbook.sheet.rows.cells.deliverInvoice.*;
import com.prairiefarms.billing.invoice.Invoice;
import com.prairiefarms.billing.utils.Contact;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DeliverToRows {

    public static void set(XSSFSheet sheet, CentralBill centralBill, Customer customer, Invoice invoice) {
        DeliveryDateCell.set(sheet, invoice.getHeader().getDeliveryDate());
        DeliveryTicketCell.set(sheet, invoice.getHeader().getId());
        DeliveryAccountCell.set(sheet, Environment.getInstance().getDairyId(), centralBill.getContact().getId(), customer.getContact().getId());
    }

    public static void set(XSSFSheet sheet, Contact contact, Invoice invoice) {
        DeliveryNameCell.set(sheet, contact.getName());
        DeliveryStreetCell.set(sheet, contact.getStreet());
        DeliveryAddressCell.set(sheet, contact.getAddress());
        DeliveryPhoneNumberCell.set(sheet, contact.getPhone());
    }
}
