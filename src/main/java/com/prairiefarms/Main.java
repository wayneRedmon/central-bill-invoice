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

    private static final long purgeTime = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);

    private static SimpleDateFormat isoFormat = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat usaFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static SimpleDateFormat mdyFormat = new SimpleDateFormat("MMddyy");

    private static Connection connection;

    private static Statement statementBilling;
    private static ResultSet resultsetBilling;

    private static PreparedStatement statementRemit;
    private static ResultSet resultsetRemit;

    private static PreparedStatement statementInvoice;
    private static ResultSet resultsetInvoice;

    private static PreparedStatement statementCustomer;
    private static ResultSet resultsetCustomer;

    private static PreparedStatement statementTerms;
    private static ResultSet resultsetTerms;

    private static PreparedStatement statementSalesperson;
    private static ResultSet resultsetSalesperson;

    private static PreparedStatement statementPurchaseOrder;
    private static ResultSet resultsetPurchaseOrder;

    private static PreparedStatement statementItem;
    private static ResultSet resultsetItem;

    private static PreparedStatement statementProductSummary;
    private static ResultSet resultsetProductSummary;

    private static XFi_Invoice document;

    private static File directorySent;
    private static Path movefrom;
    private static Path target;

    private static String schema;
    private static String locale;
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
    private static Email email;

    private static ArrayList<Item> items;

    private static StringBuilder stringInvoice;

    private static String[] attachment;


    public static void main(String[] args) {
        schema = args[0];
        locale = args[1];
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
                resultsetBilling.close();
                statementBilling.close();
                resultsetInvoice.close();
                statementInvoice.close();
                resultsetItem.close();
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

            resultsetBilling = statementBilling.executeQuery(sqlBilling);

            while (resultsetBilling.next()) {
                try {
                    fileName = "Invoice_"
                            + String.format("%03d", resultsetBilling.getInt("BIL#"))
                            + "_"
                            + isoFormat.format(mdyFormat.parse(date));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                if (resultsetBilling.getString("BILSNDEML").equals("W")) {
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

                movefrom = FileSystems.getDefault().getPath(location.getEmailBoxOutgoing().trim() + fileName.trim());
                target = FileSystems.getDefault().getPath(location.getEmailBoxSent() + fileName.trim());

                try {
                    Files.move(movefrom, target, StandardCopyOption.REPLACE_EXISTING);
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
            centralBill.setID(resultsetBilling.getInt("BIL#"));
            centralBill.setName(resultsetBilling.getString("BILCUSNAM"));
            centralBill.setAddress1(resultsetBilling.getString("BILADDR"));
            centralBill.setCity(resultsetBilling.getString("BILCITY"));
            centralBill.setStateProvince(resultsetBilling.getString("BILSTATE"));
            centralBill.setPostalCode(resultsetBilling.getString("BILZIPCD"));
            centralBill.setPhone(resultsetBilling.getString("BILPHONE"));
            centralBill.setEmailType(resultsetBilling.getString("BILSNDEML"));
            centralBill.setEmailAddress(resultsetBilling.getString("BILEMLADR"));
            centralBill.setRemitID(resultsetBilling.getInt("BILHDGCD"));
            centralBill.setDiscountRate(resultsetBilling.getDouble("BILDISCNT"));
            centralBill.setStatus(resultsetBilling.getString("BILSTATUS"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setRemit() {
        remit = new Remit();

        try {
            statementRemit.setInt(1, centralBill.getRemitID());
            resultsetRemit = statementRemit.executeQuery();

            if (resultsetRemit.next()) {
                remit.setID(resultsetRemit.getInt("HDG#"));
                remit.setName(resultsetRemit.getString("HDGNAMERE"));
                remit.setAddress1(resultsetRemit.getString("HDGADDRRE"));
                remit.setCity(resultsetRemit.getString("HDGCITYRE"));
                remit.setStateProvince(resultsetRemit.getString("HDGSTATERE"));
                remit.setPostalCode(resultsetRemit.getString("HDGZIPCDRE"));
                remit.setPhone(resultsetRemit.getLong("HDGPHONERE"));
                remit.setFax(resultsetRemit.getLong("HDGFAX#1"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setInvoice() {
        try {
            statementInvoice.setInt(1, centralBill.getID());
            resultsetInvoice = statementInvoice.executeQuery();

            while (resultsetInvoice.next()) {
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
                    invoice.setDeliveryDate(resultsetInvoice.getDate("DELIVER_DATE"));
                    invoice.setInvoiceDate(mdyFormat.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                invoice.setID(resultsetInvoice.getInt("INVOICE_NUMBER"));
                invoice.setUseExtendedID(false);

                if (resultsetInvoice.getInt("useExtendedInvoice") == 1) {
                    stringInvoice = new StringBuilder(16);

                    stringInvoice.append(String.format("%03d", location.getID()));
                    stringInvoice.append(mdyFormat.format(resultsetInvoice.getDate("DELIVER_DATE")));
                    stringInvoice.append(resultsetInvoice.getInt("INVOICE_NUMBER"));

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
            statementCustomer.setInt(1, resultsetInvoice.getInt("CUSTOMER"));
            resultsetCustomer = statementCustomer.executeQuery();

            if (resultsetCustomer.next()) {
                customer.setID(resultsetCustomer.getInt("CUS#"));
                customer.setName(resultsetCustomer.getString("CUSNAME"));
                customer.setAddress1(resultsetCustomer.getString("CUSADDR"));
                customer.setCity(resultsetCustomer.getString("CUSCITY"));
                customer.setStateProvince(resultsetCustomer.getString("CUSSTATE"));
                customer.setPostalCode(resultsetCustomer.getString("CUSZIPCD"));
                customer.setPhone(resultsetCustomer.getString("CUSPHONE"));
                customer.setTerms(resultsetCustomer.getInt("CUSPYMTTRM"));
                customer.setSalesperson(resultsetCustomer.getInt("CUSSLSMAN"));
                customer.setDiscount(resultsetCustomer.getDouble("CUSDSCRATE"));
                customer.setTaxRate(resultsetCustomer.getDouble("CUSTAXRATE"));
                customer.setEmailType(resultsetCustomer.getString("CUSSNDEMAI"));
                customer.setEmailAddress(resultsetCustomer.getString("CUSEMAIL"));
                customer.setStatus(resultsetCustomer.getString("CUSSTATUS"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setTerms() {
        terms = new Terms();

        try {
            statementTerms.setInt(1, customer.getTerms());
            resultsetTerms = statementTerms.executeQuery();

            if (resultsetTerms.next()) {
                terms.setID(resultsetTerms.getInt("TRM#"));
                terms.setDescription(resultsetTerms.getString("TRMDESC"));
                terms.setDueDays(resultsetTerms.getInt("TRMPAYDAYS"));
                terms.setType(resultsetTerms.getString("TRMWM"));
                terms.setDueByDate(resultsetInvoice.getDate("DELIVER_DATE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setSalesperson() {
        salesperson = new Salesperson();

        try {
            statementSalesperson.setInt(1, customer.getSalesperson());
            resultsetSalesperson = statementSalesperson.executeQuery();

            if (resultsetSalesperson.next()) {
                salesperson.setID(resultsetSalesperson.getInt("SLM#"));
                salesperson.setName(resultsetSalesperson.getString("SLMNAME"));
                salesperson.setLocation(resultsetSalesperson.getString("SLMLOCATN"));
                salesperson.setEmailAddress(resultsetSalesperson.getString("SLMEMAIL"));
                salesperson.setStatus(resultsetSalesperson.getString("SLMSTATUS"));
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    private static void setPurchaseOrder() {
        purchaseOrder = new PurchaseOrder();

        try {
            statementPurchaseOrder.setDate(1, resultsetInvoice.getDate("DELIVER_DATE"));
            statementPurchaseOrder.setInt(2, resultsetInvoice.getInt("ROUTE"));
            statementPurchaseOrder.setInt(3, resultsetInvoice.getInt("INVOICE_NUMBER"));
            statementPurchaseOrder.setInt(4, resultsetInvoice.getInt("CUSTOMER"));

            resultsetPurchaseOrder = statementPurchaseOrder.executeQuery();

            if (resultsetPurchaseOrder.next()) {
                purchaseOrder.setContract(resultsetPurchaseOrder.getString("ipoPO#"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setItem() throws SQLException {
        statementItem.setInt(1, centralBill.getID());
        statementItem.setInt(2, customer.getID());
        statementItem.setInt(3, invoice.getID());

        resultsetItem = statementItem.executeQuery();

        while (resultsetItem.next()) {
            item = new Item();

            item.setID(resultsetItem.getInt("ITEM_NUMBER"));
            item.setDescription(resultsetItem.getString("DESCRIPTION"));
            item.setSalesType(resultsetItem.getString("SALES_TYPE"));
            item.setQuantity(resultsetItem.getInt("QUANTITY"));
            item.setExtension(resultsetItem.getDouble("EXTENSION"));
            item.setPrice(0);
            item.setPromotion(" ");

            if (resultsetItem.getString("SALES_TYPE").equals("A")) {
                item.setPrice(resultsetItem.getDouble("PRICE"));

                if (resultsetItem.getString("PROMOTION").equals("P")) {
                    item.setPromotion(resultsetItem.getString("PROMOTION"));
                }
            }

            invoice.addItem(item);
            invoice.setSubTotal(invoice.getSubTotal() + item.getExtension());
        }
    }

    private static void setProductSummary() throws SQLException {
        items = new ArrayList<Item>();

        statementProductSummary.setInt(1, centralBill.getID());

        resultsetProductSummary = statementProductSummary.executeQuery();

        while (resultsetProductSummary.next()) {
            item = new Item();

            item.setID(resultsetProductSummary.getInt("ITEM_NUMBER"));
            item.setDescription(resultsetProductSummary.getString("PRDDESC"));
            item.setQuantity(resultsetProductSummary.getInt("QUANTITY"));
            item.setPrice(resultsetProductSummary.getDouble("PRICE"));
            item.setExtension(resultsetProductSummary.getDouble("EXTENSION"));

            items.add(item);
        }

        document.setProductSummary(items);
    }

    private static void transmit() {
        email = new Email();

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

        attachment = new String[1];
        attachment[0] = location.getEmailBoxOutgoing().trim() + fileName.trim();

        email.setAttachment(attachment);
        email.setDisclaimer(location.getDisclaimer().trim());
        email.send();
    }

    private static void maintainDirectory() {
        directorySent = new File(location.getEmailBoxSent());

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
