package com.prairiefarms.billing.invoice.item;

public class ItemSummary {

    private final String salesType;
    private final int id;
    private final double priceEach;
    private final String name;
    private final double size;
    private final double type;
    private final int label;

    private int quantity;
    private double extension;

    public ItemSummary(String salesType,
                       int id,
                       double priceEach,
                       String name,
                       double size,
                       double type,
                       int label,
                       int quantity,
                       double extension) {
        this.salesType = salesType;
        this.id = id;
        this.name = name;
        this.priceEach = priceEach;
        this.size = size;
        this.type = type;
        this.label = label;
        this.quantity = quantity;
        this.extension = extension;
    }

    public String getSalesType() {
        return salesType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPriceEach() {
        return priceEach;
    }

    public void setQuantity(int quantity) {
        this.quantity += quantity;
    }

    public double getExtension() {
        return extension;
    }

    public void setExtension(double extension) {
        this.extension += extension;
    }

    public double getSize() {
        return size;
    }

    public double getType() {
        return type;
    }

    public int getLabel() {
        return label;
    }

    public Item getItem() {
        return new Item(
                salesType,
                id,
                name,
                quantity,
                priceEach,
                false,
                extension,
                size,
                type,
                label
        );
    }
}
