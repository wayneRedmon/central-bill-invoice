package com.prairiefarms;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Main {

    private static final String dataArea = "EB132NFO";
    private static final long purgeTime = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);
    private static final SimpleDateFormat isoFormat = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat usaFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static final SimpleDateFormat mdyFormat = new SimpleDateFormat("MMddyy");

    private static final String sqlBilling = "SELECT * "
            + "FROM ds_BIL "
            + "WHERE bil# IN (SELECT billing "
            + "FROM ds364wrk "
            + "GROUP BY billing) "
            + "ORDER BY bil# "
            + "FOR FETCH ONLY";

    private static final String sqlRemit = "SELECT * "
            + "FROM ds_HDG "
            + "WHERE hdg# = ? "
            + "FETCH FIRST ROW ONLY";

    private static final String sqlInvoice = "SELECT customer, "
            + "branch, "
            + "route, "
            + "deliver_Date, "
            + "invoice_Number, "
            + "(CASE WHEN A.cusExInv IS NULL THEN 0 ELSE A.cusExInv END) AS useExtendedInvoice "
            + "FROM ds364wrk "
            + "LEFT OUTER JOIN ds_CUSXF AS A ON A.cus# = customer "
            + "WHERE billing = ? "
            + "GROUP BY bill_Sequence, "
            + "customer, "
            + "branch, "
            + "route, "
            + "deliver_Date, "
            + "invoice_Number, "
            + "A.cusExInv "
            + "ORDER BY bill_Sequence, "
            + "customer, "
            + "deliver_Date, "
            + "invoice_Number "
            + "FOR FETCH ONLY";

    private static final String sqlCustomer = "SELECT * "
            + "FROM ds_CUS "
            + "WHERE cus# = ? "
            + "FETCH FIRST ROW ONLY";

    private static final String sqlTerms = "SELECT * "
            + "FROM ds_TRM "
            + "WHERE trm# = ? "
            + "FETCH FIRST ROW ONLY";

    private static final String sqlSalesperson = "SELECT * "
            + "FROM ds_SLM "
            + "WHERE slm# = ? "
            + "FOR FETCH ONLY";

    private static final String sqlPurchaseOrder = "SELECT * "
            + "FROM ds_IPO "
            + "WHERE DATE(TO_DATE(DIGITS(ipoDat),'MMDDYY')) = ? AND "
            + "ipoRt# = ? AND "
            + "ipoInv# = ? AND "
            + "ipoC# = ? "
            + "FETCH FIRST ROW ONLY";

    private static final String sqlItem = "SELECT sales_Type, "
            + "item_Number, "
            + "description, "
            + "quantity, "
            + "price, "
            + "extension, "
            + "promotion, "
            + "price_Retail "
            + "FROM ds364wrk "
            + "WHERE billing = ? AND "
            + "customer = ? AND "
            + "invoice_Number = ? "
            + "ORDER BY sales_Type, "
            + "item_Size, "
            + "item_Type, "
            + "item_Label, "
            + "description, "
            + "item_Number "
            + "FOR FETCH ONLY";

    private static final String sqlProductSummary = "SELECT item_Number, "
            + "prdDesc, "
            + "SUM(quantity) AS QUANTITY, "
            + "price, "
            + "SUM(extension) AS EXTENSION "
            + "FROM ds364wrk INNER JOIN "
            + "ds_Prd on prd# = item_Number "
            + "WHERE billing = ? "
            + "GROUP BY item_Number, "
            + "prdDesc, "
            + "price, "
            + "prdSize, "
            + "prdType, "
            + "prdLabel "
            + "ORDER BY prdSize, "
            + "prdType, "
            + "prdLabel, "
            + "prdDesc, "
            + "item_Number "
            + "FOR FETCH ONLY";

    private static Connection connection;
    private static Statement statementBilling;
    private static PreparedStatement statementRemit;
    private static PreparedStatement statementInvoice;
    private static PreparedStatement statementCustomer;
    private static PreparedStatement statementTerms;
    private static PreparedStatement statementSalesperson;
    private static PreparedStatement statementPurchaseOrder;
    private static PreparedStatement statementItem;
    private static PreparedStatement statementProductSummary;
    private static ResultSet resultSetBilling;
    private static ResultSet resultSetInvoice;
    private static ResultSet resultSetItem;

    private static XFi_Invoice document;

    private static String frequency;
    private static String date;
    private static String fileName;
    private static Location location;
    private static CentralBill centralBill;
    private static Terms terms;
    private static Remit remit;
    private static Customer customer;
    private static Salesperson salesperson;
    private static PurchaseOrder purchaseOrder;
    private static Invoice invoice;
    private static Item item;

    public static void main(String[] args) {
        String schema = args[0];
        String locale = args[1];
        frequency = args[2];
        date = args[3];
        String username = args[4];
        String password = args[5];

        location = new Location();
        location.setID(Integer.parseInt(locale.trim()));
        location.setSchema(schema);
        location.setUser(username);
        location.setPassword(password);

        try {
            location.setInvoiceDate(mdyFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        location.setFrequency(frequency);
        location.setDataArea(dataArea.trim());
        location.setProperty();
        location.setPath("invoice");

        maintainDirectory();

        try {
            DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver());
            connection = DriverManager.getConnection(location.getHostURL(), location.getUser(), location.getPassword());
            setDocument();
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            try {
                resultSetBilling.close();
                statementBilling.close();
                resultSetInvoice.close();
                statementInvoice.close();
                resultSetItem.close();
                statementItem.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setDocument() {
        try {
            statementBilling = connection.createStatement();
            statementRemit = connection.prepareStatement(sqlRemit);
            statementInvoice = connection.prepareStatement(sqlInvoice);
            statementCustomer = connection.prepareStatement(sqlCustomer);
            statementRemit = connection.prepareStatement(sqlRemit);
            statementTerms = connection.prepareStatement(sqlTerms);
            statementSalesperson = connection.prepareStatement(sqlSalesperson);
            statementPurchaseOrder = connection.prepareStatement(sqlPurchaseOrder);
            statementItem = connection.prepareStatement(sqlItem);
            statementProductSummary = connection.prepareStatement(sqlProductSummary);

            resultSetBilling = statementBilling.executeQuery(sqlBilling);

            while (resultSetBilling.next()) {
                try {
                    fileName = "Invoice_"
                            + String.format("%03d", resultSetBilling.getInt("BIL#"))
                            + "_"
                            + isoFormat.format(mdyFormat.parse(date));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                if (resultSetBilling.getString("BILSNDEML").equals("W")) {
                    document = new XFi_Invoice_XLS();
                    fileName = fileName.trim() + ".xls";
                } else {
                    document = new XFi_Invoice_PDF();
                    fileName = fileName.trim() + ".pdf";
                }

                document.setLocation(location);
                document.open(location.getEmailBoxOutgoing().trim() + fileName);

                setBilling();
                setRemit();
                setInvoice();
                setProductSummary();

                document.close();

                transmit();

                Path moveFrom = FileSystems.getDefault().getPath(location.getEmailBoxOutgoing().trim() + fileName.trim());
                Path moveTo = FileSystems.getDefault().getPath(location.getEmailBoxSent() + fileName.trim());

                try {
                    Files.move(moveFrom, moveTo, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setBilling() {
        centralBill = new CentralBill();

        try {
            centralBill.setID(resultSetBilling.getInt("BIL#"));
            centralBill.setName(resultSetBilling.getString("BILCUSNAM"));
            centralBill.setAddress1(resultSetBilling.getString("BILADDR"));
            centralBill.setCity(resultSetBilling.getString("BILCITY"));
            centralBill.setStateProvince(resultSetBilling.getString("BILSTATE"));
            centralBill.setPostalCode(resultSetBilling.getString("BILZIPCD"));
            centralBill.setPhone(resultSetBilling.getString("BILPHONE"));
            centralBill.setEmailType(resultSetBilling.getString("BILSNDEML"));
            centralBill.setEmailAddress(resultSetBilling.getString("BILEMLADR"));
            centralBill.setRemitID(resultSetBilling.getInt("BILHDGCD"));
            centralBill.setDiscountRate(resultSetBilling.getDouble("BILDISCNT"));
            centralBill.setStatus(resultSetBilling.getString("BILSTATUS"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setRemit() {
        remit = new Remit();

        try {
            statementRemit.setInt(1, centralBill.getRemitID());
            ResultSet resultSetRemit = statementRemit.executeQuery();

            if (resultSetRemit.next()) {
                remit.setID(resultSetRemit.getInt("HDG#"));
                remit.setName(resultSetRemit.getString("HDGNAMERE"));
                remit.setAddress1(resultSetRemit.getString("HDGADDRRE"));
                remit.setCity(resultSetRemit.getString("HDGCITYRE"));
                remit.setStateProvince(resultSetRemit.getString("HDGSTATERE"));
                remit.setPostalCode(resultSetRemit.getString("HDGZIPCDRE"));
                remit.setPhone(resultSetRemit.getLong("HDGPHONERE"));
                remit.setFax(resultSetRemit.getLong("HDGFAX#1"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setInvoice() {
        try {
            statementInvoice.setInt(1, centralBill.getID());
            resultSetInvoice = statementInvoice.executeQuery();

            while (resultSetInvoice.next()) {
                setCustomer();
                setTerms();
                setSalesperson();
                setPurchaseOrder();

                invoice = new Invoice();

                invoice.setFrequency(frequency);
                invoice.setLocation(location);
                invoice.setBilling(centralBill);
                invoice.setRemit(remit);

                try {
                    invoice.setDeliveryDate(resultSetInvoice.getDate("DELIVER_DATE"));
                    invoice.setInvoiceDate(mdyFormat.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                invoice.setID(resultSetInvoice.getInt("INVOICE_NUMBER"));
                invoice.setUseExtendedID(false);

                if (resultSetInvoice.getInt("useExtendedInvoice") == 1) {
                    StringBuilder stringInvoice = new StringBuilder(16);

                    stringInvoice.append(String.format("%03d", location.getID()));
                    stringInvoice.append(mdyFormat.format(resultSetInvoice.getDate("DELIVER_DATE")));
                    stringInvoice.append(resultSetInvoice.getInt("INVOICE_NUMBER"));

                    invoice.setUseExtendedID(true);
                    invoice.setExtendedID(stringInvoice.toString());
                }

                invoice.setApplied(0);
                invoice.setCustomer(customer);
                invoice.setTerms(terms);
                invoice.setSalesperson(salesperson);
                invoice.setPurchaseOrder(purchaseOrder);
                invoice.setItem();

                setItem();

                invoice.setDiscount(invoice.getSubTotal() * invoice.getBilling().getDiscountRateFixed());

                document.addInvoice(invoice, frequency);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private static void setCustomer() {
        customer = new Customer();

        try {
            statementCustomer.setInt(1, resultSetInvoice.getInt("CUSTOMER"));
            ResultSet resultSetCustomer = statementCustomer.executeQuery();

            if (resultSetCustomer.next()) {
                customer.setID(resultSetCustomer.getInt("CUS#"));
                customer.setName(resultSetCustomer.getString("CUSNAME"));
                customer.setAddress1(resultSetCustomer.getString("CUSADDR"));
                customer.setCity(resultSetCustomer.getString("CUSCITY"));
                customer.setStateProvince(resultSetCustomer.getString("CUSSTATE"));
                customer.setPostalCode(resultSetCustomer.getString("CUSZIPCD"));
                customer.setPhone(resultSetCustomer.getString("CUSPHONE"));
                customer.setTerms(resultSetCustomer.getInt("CUSPYMTTRM"));
                customer.setSalesperson(resultSetCustomer.getInt("CUSSLSMAN"));
                customer.setDiscount(resultSetCustomer.getDouble("CUSDSCRATE"));
                customer.setTaxRate(resultSetCustomer.getDouble("CUSTAXRATE"));
                customer.setEmailType(resultSetCustomer.getString("CUSSNDEMAI"));
                customer.setEmailAddress(resultSetCustomer.getString("CUSEMAIL"));
                customer.setStatus(resultSetCustomer.getString("CUSSTATUS"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setTerms() {
        terms = new Terms();

        try {
            statementTerms.setInt(1, customer.getTerms());
            ResultSet resultSetTerms = statementTerms.executeQuery();

            if (resultSetTerms.next()) {
                terms.setID(resultSetTerms.getInt("TRM#"));
                terms.setDescription(resultSetTerms.getString("TRMDESC"));
                terms.setDueDays(resultSetTerms.getInt("TRMPAYDAYS"));
                terms.setType(resultSetTerms.getString("TRMWM"));
                terms.setDueByDate(resultSetInvoice.getDate("DELIVER_DATE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setSalesperson() {
        salesperson = new Salesperson();

        try {
            statementSalesperson.setInt(1, customer.getSalesperson());
            ResultSet resultSetSalesperson = statementSalesperson.executeQuery();

            if (resultSetSalesperson.next()) {
                salesperson.setID(resultSetSalesperson.getInt("SLM#"));
                salesperson.setName(resultSetSalesperson.getString("SLMNAME"));
                salesperson.setLocation(resultSetSalesperson.getString("SLMLOCATN"));
                salesperson.setEmailAddress(resultSetSalesperson.getString("SLMEMAIL"));
                salesperson.setStatus(resultSetSalesperson.getString("SLMSTATUS"));
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    private static void setPurchaseOrder() {
        purchaseOrder = new PurchaseOrder();

        try {
            statementPurchaseOrder.setDate(1, resultSetInvoice.getDate("DELIVER_DATE"));
            statementPurchaseOrder.setInt(2, resultSetInvoice.getInt("ROUTE"));
            statementPurchaseOrder.setInt(3, resultSetInvoice.getInt("INVOICE_NUMBER"));
            statementPurchaseOrder.setInt(4, resultSetInvoice.getInt("CUSTOMER"));

            ResultSet resultSetPurchaseOrder = statementPurchaseOrder.executeQuery();

            if (resultSetPurchaseOrder.next()) {
                purchaseOrder.setContract(resultSetPurchaseOrder.getString("ipoPO#"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setItem() throws SQLException {
        statementItem.setInt(1, centralBill.getID());
        statementItem.setInt(2, customer.getID());
        statementItem.setInt(3, invoice.getID());

        resultSetItem = statementItem.executeQuery();

        while (resultSetItem.next()) {
            item = new Item();

            item.setID(resultSetItem.getInt("ITEM_NUMBER"));
            item.setDescription(resultSetItem.getString("DESCRIPTION"));
            item.setSalesType(resultSetItem.getString("SALES_TYPE"));
            item.setQuantity(resultSetItem.getInt("QUANTITY"));
            item.setExtension(resultSetItem.getDouble("EXTENSION"));
            item.setPrice(0);
            item.setPromotion(" ");

            if (resultSetItem.getString("SALES_TYPE").equals("A")) {
                item.setPrice(resultSetItem.getDouble("PRICE"));

                if (resultSetItem.getString("PROMOTION").equals("P")) {
                    item.setPromotion(resultSetItem.getString("PROMOTION"));
                }
            }

            invoice.addItem(item);
            invoice.setSubTotal(invoice.getSubTotal() + item.getExtension());
        }
    }

    private static void setProductSummary() throws SQLException {
        ArrayList<Item> items = new ArrayList<Item>();

        statementProductSummary.setInt(1, centralBill.getID());

        ResultSet resultSetProductSummary = statementProductSummary.executeQuery();

        while (resultSetProductSummary.next()) {
            item = new Item();

            item.setID(resultSetProductSummary.getInt("ITEM_NUMBER"));
            item.setDescription(resultSetProductSummary.getString("PRDDESC"));
            item.setQuantity(resultSetProductSummary.getInt("QUANTITY"));
            item.setPrice(resultSetProductSummary.getDouble("PRICE"));
            item.setExtension(resultSetProductSummary.getDouble("EXTENSION"));

            items.add(item);
        }

        document.setProductSummary(items);
    }

    private static void transmit() {
        Email email = new Email();

        email.setCredentials(location.getUser(), location.getPassword());
        email.setMailHost(location.getHostEmail());
        email.setRecipient(centralBill.getEmailAddress());
        email.setCarbonCopy(location.getEmailCarbonCopy());
        email.setSender(location.getEmailCarbonCopy());

        try {
            email.setSubject("[" + String.format("%03d", invoice.getBilling().getID()) + "] - " + location.getTextFrequency().trim() + " Invoices for " + usaFormat.format(mdyFormat.parse(date)));
        } catch (ParseException e) {
            email.setSubject(location.getTextFrequency().trim() + " Invoices for " + date);
        }

        try {
            email.setBody("<p>Attached are your <b>"
                    + location.getTextFrequency().trim()
                    + "</b> invoices for <b>"
                    + usaFormat.format(mdyFormat.parse(date))
                    + "</b>.<br><br><b><i>Thank you for your business!</i></b><br><br>"
                    + remit.getName().trim()
                    + "<br>"
                    + remit.getAddress1().trim()
                    + "<br>"
                    + remit.getAddress_Formatted()
                    + "<br>"
                    + remit.getPhone_Formatted());
        } catch (ParseException e) {
            email.setBody("<p>Attached are your <b>"
                    + location.getTextFrequency().trim()
                    + "</b> invoices for <b>"
                    + date
                    + "</b>.<br><br><b><i>Thank you for your business!</i></b><br><br>"
                    + remit.getName().trim()
                    + "<br>"
                    + remit.getAddress1().trim()
                    + "<br>"
                    + remit.getAddress_Formatted()
                    + "<br>"
                    + remit.getPhone_Formatted());
        }

        String[] attachment = new String[1];
        attachment[0] = location.getEmailBoxOutgoing().trim() + fileName.trim();

        email.setAttachment(attachment);
        email.setDisclaimer(location.getDisclaimer().trim());
        email.send();
    }

    private static void maintainDirectory() {
        File directorySent = new File(location.getEmailBoxSent());

        if (location.getEmailBoxSent() != null &&
                !location.getEmailBoxSent().trim().equals("") &&
                location.getEmailBoxSent().contains("invoice")) {
            File directory = new File(location.getEmailBoxSent());

            if (directory.exists()) {
                File[] listFiles = directory.listFiles();

                for (File listFile : listFiles) {
                    if (listFile.lastModified() < purgeTime) {
                        if (!listFile.delete()) {
                            System.err.println("Unable to delete file: " + listFile);
                        }
                    }
                }
            }
        }

        try {
            FileUtils.cleanDirectory(directorySent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
