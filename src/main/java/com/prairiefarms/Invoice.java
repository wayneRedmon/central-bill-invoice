package com.prairiefarms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Invoice {

    private static final SimpleDateFormat legacyFormat = new SimpleDateFormat("MMddyyyy");
    private static final SimpleDateFormat usaFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static final SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");

    private int id;
    private boolean useExtendedID;
    private String extendedID;
    private String frequency;
    private Date invoiceDate;
    private Date deliveryDate;
    private Location location;
    private Remit remit;
    private CentralBill centralBill;
    private Customer customer;
    private Terms terms;
    private Salesperson salesperson;
    private PurchaseOrder purchaseOrder;
    private ArrayList<Item> itemList;
    private double subTotal;
    private double applied;
    private double discount;
    private double tax;
    private double total;
    private String status;


    public int getID() {
        return id;
    }

    public void setID(int iD) {
        this.id = iD;
    }

    public boolean isUseExtendedID() {
        return useExtendedID;
    }

    public void setUseExtendedID(boolean useExtendedID) {
        this.useExtendedID = useExtendedID;
    }

    public String getExtendedID() {
        return extendedID;
    }

    public void setExtendedID(String extendedID) {
        this.extendedID = extendedID;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public String getInvoiceDate_LEGACY() {
        return legacyFormat.format(invoiceDate);
    }

    public String getInvoiceDate_USA() {
        return usaFormat.format(invoiceDate);
    }

    public String getInvoiceDate_ISO() {
        return isoFormat.format(invoiceDate);
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public String getDeliveryDate_LEGACY() {
        return legacyFormat.format(deliveryDate);
    }

    public String getDeliveryDate_USA() {
        return usaFormat.format(deliveryDate);
    }

    public String getDeliveryDate_ISO() {
        return isoFormat.format(deliveryDate);
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Remit getRemit() {
        return remit;
    }

    public void setRemit(Remit remit) {
        this.remit = remit;
    }

    public CentralBill getBilling() {
        return centralBill;
    }

    public void setBilling(CentralBill centralBill) {
        this.centralBill = centralBill;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Terms getTerms() {
        return terms;
    }

    public void setTerms(Terms terms) {
        this.terms = terms;
    }

    public Salesperson getSalesperson() {
        return salesperson;
    }

    public void setSalesperson(Salesperson salesperson) {
        this.salesperson = salesperson;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public ArrayList<Item> getItem() {
        return itemList;
    }

    public void addItem(Item item) {
        this.itemList.add(item);
    }

    public void setItem() {
        itemList = new ArrayList<Item>();
    }

    public double getSubTotal() {
        return Math.round(subTotal * 100.0) / 100.0;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = Math.round(subTotal * 100.0) / 100.0;
    }

    public double getApplied() {
        return Math.round(applied * 100.0) / 100.0;
    }

    public void setApplied(double applied) {
        this.applied = Math.round(applied * 100.0) / 100.0;
    }

    public double getDiscount() {
        return Math.round(discount * 100.0) / 100.0;
    }

    public void setDiscount(double discount) {
        this.discount = Math.round(discount * 100.0) / 100.0;
    }

    public double getTax() {
        return Math.round(tax * 100.0) / 100.0;
    }

    public void setTax(double tax) {
        this.tax = Math.round(tax * 100.0) / 100.0;
    }

    public double getTotal() {
        return Math.round(total * 100.0) / 100.0;
    }

    public void setTotal(double total) {
        this.total = Math.round(total * 100.0) / 100.0;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
