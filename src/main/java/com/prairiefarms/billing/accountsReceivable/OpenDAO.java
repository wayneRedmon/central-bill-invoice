package com.prairiefarms.billing.accountsReceivable;

import org.apache.commons.lang3.ObjectUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OpenDAO {

    private final Connection connection;

    private PreparedStatement preparedStatement;

    public OpenDAO(Connection connection) {
        this.connection = connection;
    }

    public Double get(int customerId, LocalDate deliveryDate, int invoiceId) throws SQLException {
        final String sql = "select opnApplied from ds_Opn where opnCust#=? and opnInvDate=? and opnInv#=?";

        double amountApplied = 0D;

        if (ObjectUtils.isEmpty(preparedStatement)) preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, customerId);
        preparedStatement.setInt(2, Integer.parseInt(deliveryDate.format(DateTimeFormatter.ofPattern("yyMMdd"))));
        preparedStatement.setInt(3, invoiceId);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                amountApplied = resultSet.getDouble("opnApplied");
            }
        }

        return amountApplied;
    }
}
