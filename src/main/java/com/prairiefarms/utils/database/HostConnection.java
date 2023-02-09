package com.prairiefarms.utils.database;

import com.prairiefarms.billing.utils.Credentials;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HostConnection implements AutoCloseable {

    private final String server;
    private final Credentials credentials;
    private final String library;

    private Connection connection;

    public HostConnection(String server, Credentials credentials, String library) {
        this.server = server;
        this.credentials = credentials;
        this.library = library;
    }

    public Connection get() throws SQLException {
        final String jdbcURL =
                "jdbc:as400://" + server + ";" +
                        "user=" + credentials.getUsername() + ";" +
                        "password=" + credentials.getPassword() + ";" +
                        "libraries=" + library + ";" +
                        "naming=sql;" +
                        "errors=full;" +
                        "prompt=false;" +
                        "block size=512;" +
                        "use block update=true;" +
                        "do escape processing=false;" +
                        "data compression=true;" +
                        "prefetch=true;" +
                        "thread used=false;";

        connection = null;

        DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver());

        connection = DriverManager.getConnection(jdbcURL);

        return connection;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
