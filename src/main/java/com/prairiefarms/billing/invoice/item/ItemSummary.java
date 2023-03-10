package com.prairiefarms.billing.invoice.item;

public class ItemSummary {

    private final String salesType;
    private final int id;
    private final String name;
    private final double priceEach;
    private final boolean promotion;
    private final double size;
    private final double type;
    private final int label;
    private final double pointsEach;

    private int quantity;
    private double extension;
    private double totalPoints;

    public ItemSummary(String salesType,
                       int id,
                       String name,
                       int quantity,
                       double priceEach,
                       boolean promotion,
                       double extension,
                       double size,
                       double type,
                       int label,
                       double pointsEach,
                       double totalPoints) {
        this.salesType = salesType;
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.promotion = promotion;
        this.priceEach = priceEach;
        this.extension = extension;
        this.size = size;
        this.type = type;
        this.label = label;
        this.pointsEach = pointsEach;
        this.totalPoints = totalPoints;
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

    public void setQuantity(int quantity) {
        this.quantity += quantity;
    }

    public double getPriceEach() {
        return priceEach;
    }

    public boolean isPromotion() {
        return promotion;
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

    public double getPointsEach() { return pointsEach; }

    public void setTotalPoints(double totalPoints) {
        this.totalPoints += totalPoints;
    }

    public double getTotalPoints() { return totalPoints; }

    public Item getItem() {
        return new Item(
                salesType,
                id,
                name,
                quantity,
                priceEach,
                promotion,
                extension,
                size,
                type,
                label,
                pointsEach,
                totalPoints
        );
    }
}
