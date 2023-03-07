package com.prairiefarms.billing.customer;

import com.prairiefarms.billing.utils.Contact;
import org.apache.commons.lang3.ObjectUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class CustomerDAO {

    private final Connection connection;

    private PreparedStatement preparedStatement;

    public CustomerDAO(Connection connection) {
        this.connection = connection;
    }

    public Customer get(int centralBillId) throws SQLException {
        final String sql = "select C.cus# as customerId,cusName,cusAddr,cusCity,cusState,cusZipCd,cusPhone,cusEMail," +
                "coalesce(cusExInv,0) as useExtendedInvoice," +
                "(case when cusTaxEmp='Y' then 0.00 else cusTaxRate end) as taxRate," +
                "coalesce(trmDesc,'') as termsName,coalesce(trmPayDays,0) as termsDueDays,coalesce(slmName,'') as salesperson," +
                "sortSequence " +
                "from ds_Cus as C " +
                "inner join ds364Awrk on customerId=C.cus# " +
                "left join ds_CusXF as CXF on CXF.cus#=C.cus# " +
                "left join ds_Trm on trm#=cusPymtTrm " +
                "left join ds_Slm on slm#=cusSlsMan " +
                "where C.cus#=?";

        Customer customer = null;

        if (ObjectUtils.isEmpty(preparedStatement)) preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, centralBillId);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                customer = new Customer(
                        new Contact(
                                resultSet.getInt("customerId"),
                                resultSet.getString("cusName"),
                                resultSet.getString("cusAddr"),
                                resultSet.getString("cusCity"),
                                resultSet.getString("cusState"),
                                resultSet.getInt("cusZipCd"),
                                resultSet.getLong("cusPhone"),
                                new ArrayList<>(Collections.singletonList(resultSet.getString("cusEmail")))
                        ),
                        new Terms(
                                resultSet.getString("termsName"),
                                resultSet.getInt("termsDueDays")
                        ),
                        resultSet.getString("salesperson"),
                        resultSet.getInt("useExtendedInvoice") == 1,
                        resultSet.getDouble("taxRate"),
                        resultSet.getInt("sortSequence")
                );
            }
        }

        return customer;
    }
}
