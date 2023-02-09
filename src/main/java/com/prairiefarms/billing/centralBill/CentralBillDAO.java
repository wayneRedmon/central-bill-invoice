package com.prairiefarms.billing.centralBill;

import com.prairiefarms.billing.BillingEnvironment;
import com.prairiefarms.billing.remit.Remit;
import com.prairiefarms.billing.utils.Contact;
import com.prairiefarms.billing.utils.DocumentType;
import org.apache.commons.lang3.ObjectUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class CentralBillDAO {

    private static final String SQL_STATEMENT = "select bil#,bilCusNam,bilAddr,bilCity,bilState,bilZipCd,bilPhone,bilEmlAdr,bilDiscnt,bilHdgCd,bilSndEml," +
            "coalesce(hdg#,0) as remitId,coalesce(hdgNameRE,'') as remitName,coalesce(hdgAddrRE,'') as remitStreet,coalesce(hdgCityRE,'') as remitCity,coalesce(hdgStateRE,'') as remitState,coalesce(hdgZipCdRE,0) as remitZip,coalesce(hdgPhoneRE,0) as remitPhone " +
            "from ds_Bil " +
            "left join ds_Hdg on hdg#=bilHdgCd " +
            "where bil#=?";

    private final Connection connection;

    private PreparedStatement preparedStatement;

    public CentralBillDAO(Connection connection) {
        this.connection = connection;
    }

    public CentralBill get(int centralBillId) throws SQLException {
        CentralBill centralBill = null;

        if (ObjectUtils.isEmpty(preparedStatement)) preparedStatement = connection.prepareStatement(SQL_STATEMENT);

        preparedStatement.setInt(1, centralBillId);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                centralBill = new CentralBill(
                        new Contact(
                                resultSet.getInt("bil#"),
                                resultSet.getString("bilCusNam"),
                                resultSet.getString("bilAddr"),
                                resultSet.getString("bilCity"),
                                resultSet.getString("bilState"),
                                resultSet.getInt("bilZipCd"),
                                resultSet.getLong("bilPhone"),
                                new ArrayList<>(Collections.singletonList(resultSet.getString("bilEmlAdr")))
                        ),
                        resultSet.getDouble("bilDiscnt"),
                        new Remit(
                                new Contact(
                                        resultSet.getInt("remitId"),
                                        resultSet.getString("remitName"),
                                        resultSet.getString("remitStreet"),
                                        resultSet.getString("remitCity"),
                                        resultSet.getString("remitState"),
                                        resultSet.getInt("remitZip"),
                                        resultSet.getLong("remitPhone"),
                                        BillingEnvironment.getInstance().emailCarbonCopy()
                                )
                        ),
                        DocumentType.getEnumByString(resultSet.getString("bilSndEml"))
                );
            }
        }

        return centralBill;
    }
}
