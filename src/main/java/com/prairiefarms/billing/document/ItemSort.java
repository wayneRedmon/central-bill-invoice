package com.prairiefarms.billing.document;

import com.prairiefarms.billing.invoice.Invoice;
import com.prairiefarms.billing.invoice.centralBill.customer.CustomerInvoice;
import com.prairiefarms.billing.invoice.item.Item;
import com.prairiefarms.billing.invoice.item.ItemSummary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ItemSort {

    private final List<CustomerInvoice> customerInvoices;

    private List<Item> unsortedItems;

    public ItemSort(List<CustomerInvoice> customerInvoices) {
        this.customerInvoices = customerInvoices;

        this.extract();
    }

    private void extract() {
        unsortedItems = new ArrayList<>();

        for (CustomerInvoice customerInvoice : customerInvoices) {
            for (Invoice invoice : customerInvoice.getInvoices())
                unsortedItems.addAll(invoice.getItems());
        }
    }

    public List<ItemSummary> sort() {
        List<ItemSummary> itemSummaries = new ArrayList<>();

        boolean found;

        for (Item item : unsortedItems) {
            found = false;

            for (ItemSummary itemSummary : itemSummaries) {
                if (itemSummary.getSalesType().equals(item.getSalesType()) &&
                        itemSummary.getId() == item.getId() &&
                        itemSummary.getPriceEach() == item.getPriceEach()) {
                    itemSummary.setQuantity(item.getQuantity());
                    itemSummary.setExtension(item.getExtension());
                    itemSummary.setTotalPoints(item.getTotalPoints());

                    found = true;

                    break;
                }
            }

            if (!found) {
                itemSummaries.add(
                        new ItemSummary(
                                item.getSalesType(),
                                item.getId(),
                                item.getName(),
                                item.getQuantity(),
                                item.getPriceEach(),
                                item.isPromotion(),
                                item.getExtension(),
                                item.getSize(),
                                item.getType(),
                                item.getLabel(),
                                item.getPointsEach(),
                                item.getTotalPoints()
                        )
                );
            }
        }

        return itemSummaries.stream().sorted(
                Comparator.comparing(ItemSummary::getSalesType)
                        .thenComparing(ItemSummary::getSize)
                        .thenComparing(ItemSummary::getType)
                        .thenComparing(ItemSummary::getLabel)
                        .thenComparing(ItemSummary::getName)
                        .thenComparing(ItemSummary::getId)
                        .thenComparing(ItemSummary::getPriceEach)
                        .thenComparing(ItemSummary::isPromotion)
        ).collect(Collectors.toList());
    }
}
