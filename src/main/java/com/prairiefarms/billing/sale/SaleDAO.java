package com.prairiefarms.billing.sale;

import com.prairiefarms.billing.invoice.Header;
import com.prairiefarms.billing.invoice.item.Item;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDAO {

    private final Connection connection;

    private PreparedStatement psCustomerId;
    private PreparedStatement psHeader;
    private PreparedStatement psItem;

    public SaleDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Integer> centralBillIdList() throws SQLException {
        final String sql = "select distinct centralBillId from ds364Awrk order by centralBillId";

        List<Integer> centralBillIds = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    centralBillIds.add(resultSet.getInt("centralBillId"));
                }
            }
        }

        return centralBillIds;
    }

    public List<Integer> customerIdList(int centralBillId) throws SQLException {
        final String sql = "select customerId from ds364Awrk where centralBillId=? group by customerId,sortSequence order by sortSequence";

        List<Integer> customerIds = new ArrayList<>();

        if (ObjectUtils.isEmpty(psCustomerId)) psCustomerId = connection.prepareStatement(sql);

        psCustomerId.setInt(1, centralBillId);

        try (ResultSet resultSet = psCustomerId.executeQuery()) {
            while (resultSet.next()) {
                customerIds.add(resultSet.getInt("customerId"));
            }
        }

        return customerIds;
    }

    public List<Header> headerList(int centralBillId, int customerId) throws SQLException {
        final String sql = "select invoiceId,deliveryDate,branchId,routeId,purchaseOrder,isDistributor " +
                "from ds364Awrk " +
                "where centralBillId=? and customerId=? " +
                "group by invoiceId,deliveryDate,branchId,routeId,purchaseOrder,isDistributor " +
                "order by deliveryDate,invoiceId";

        List<Header> headers = new ArrayList<>();

        if (ObjectUtils.isEmpty(psHeader)) psHeader = connection.prepareStatement(sql);

        psHeader.setInt(1, centralBillId);
        psHeader.setInt(2, customerId);

        try (ResultSet rsHeader = psHeader.executeQuery()) {
            while (rsHeader.next()) {
                headers.add(
                        new Header(
                                rsHeader.getInt("invoiceId"),
                                rsHeader.getDate("deliveryDate").toLocalDate(),
                                rsHeader.getString("purchaseOrder"),
                                rsHeader.getInt("isDistributor") > 0
                        )
                );
            }
        }

        return headers;
    }

    public List<Item> itemList(int centralBillId, int customerId, int invoiceId) throws SQLException {
        final String sql = "select salesType,itemId,itemName,quantity,priceEach,isPromotion,extension," +
                "itemSize,itemType,itemLabel,pointsEach,totalPoints " +
                "from ds364Awrk " +
                "where centralBillId=? and customerId=? and invoiceId=? " +
                "order by salesType,itemSize,itemType,itemLabel,itemName,itemId";

        List<Item> items = new ArrayList<>();

        if (ObjectUtils.isEmpty(psItem)) psItem = connection.prepareStatement(sql);

        psItem.setInt(1, centralBillId);
        psItem.setInt(2, customerId);
        psItem.setInt(3, invoiceId);

        try (ResultSet resultSet = psItem.executeQuery()) {
            while (resultSet.next()) {
                items.add(
                        new Item(
                                resultSet.getString("salesType"),
                                resultSet.getInt("itemId"),
                                resultSet.getString("itemName"),
                                resultSet.getInt("quantity"),
                                resultSet.getDouble("priceEach"),
                                StringUtils.normalizeSpace(resultSet.getString("isPromotion")).equals("P"),
                                resultSet.getDouble("extension"),
                                resultSet.getDouble("itemSize"),
                                resultSet.getDouble("itemType"),
                                resultSet.getInt("itemLabel"),
                                resultSet.getDouble("pointsEach"),
                                resultSet.getDouble("totalPoints")
                        )
                );
            }
        }

        return items;
    }
}
