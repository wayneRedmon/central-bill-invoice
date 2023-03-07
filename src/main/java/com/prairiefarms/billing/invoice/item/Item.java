package com.prairiefarms.billing.invoice.item;

public class Item {

    private final String salesType;
    private final int id;
    private final String name;
    private final int quantity;
    private final double priceEach;
    private final boolean promotion;
    private final double extension;
    private final double size;
    private final double type;
    private final int label;
    private final double pointsEach;
    private final double totalPoints;

    public Item(String salesType,
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
        this.priceEach = priceEach;
        this.promotion = promotion;
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

    public String getIdAsAccount() {
        return String.format("%07d", id).substring(0, 2) + "-" +
                String.format("%07d", id).substring(2, 5) + "-" +
                String.format("%07d", id).substring(5, 7);
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
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

    public double getTotalPoints() { return totalPoints; }
}
