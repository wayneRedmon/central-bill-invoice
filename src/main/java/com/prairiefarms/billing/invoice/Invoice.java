package com.prairiefarms.billing.invoice;

import com.prairiefarms.billing.invoice.item.Item;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class Invoice {

    private final Header header;
    private final List<Item> items;
    private final double appliedAmount;
    private final double discountRate;
    private final double taxRate;

    public Invoice(Header header,
                   List<Item> items,
                   double appliedAmount,
                   double discountRate,
                   double taxRate) {
        this.header = header;
        this.items = items;
        this.appliedAmount = appliedAmount;
        this.discountRate = discountRate;
        this.taxRate = taxRate;
    }

    public Header getHeader() {
        return header;
    }

    public int getInvoiceId() {
        return header.getId();
    }

    public LocalDate getDeliveryDate() {
        return header.getDeliveryDate();
    }

    public List<Item> getItems() {
        return items;
    }

    public Double salesTotal() {
        return items.stream().filter(o -> o.getSalesType().equals("A")).mapToDouble(Item::getExtension).sum();
    }

    private Double accountTotal() {
        return items.stream().filter(o -> !o.getSalesType().equals("A")).mapToDouble(Item::getExtension).sum();
    }

    public double subTotal() {
        return BigDecimal.valueOf(salesTotal())
                .add(BigDecimal.valueOf(accountTotal()))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public double getAppliedAmount() {
        return appliedAmount == 0 ? 0 : appliedAmount * -1;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public double getDiscountAmount() {
        final double runningSubTotal = subTotal() - appliedAmount;

        return BigDecimal.valueOf(discountRate * -1)
                .multiply(BigDecimal.valueOf(runningSubTotal))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public double getTaxRate() {
        return taxRate;
    }

    public double getTaxAmount() {
        final double runningSubtotal = (subTotal() - appliedAmount) - getDiscountAmount();

        return BigDecimal.valueOf(taxRate * -1)
                .multiply(BigDecimal.valueOf(runningSubtotal))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public double amountDue() {
        return subTotal()
                - appliedAmount
                - getDiscountAmount()
                + getTaxAmount();
    }
}
