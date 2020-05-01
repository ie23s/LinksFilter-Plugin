package com.ie23s.minecraft.plugin.linksfilter.utils.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLUtil {
    private Connection connection = null;

    private final String host;
    private final int port;
    private final String user;
    private final String password;
    private final String db;
    private final String prefix;

    public MySQLUtil(String host, int port, String user, String password, String db, String prefix) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.db = db;
        this.prefix = prefix;
    }

    public String strip(String str) {
        str = str.replaceAll("<[^>]*>", "");
        str = str.replace("\\", "\\\\");
        str = str.trim();
        return str;
    }

    public void connect() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db +
                "?useUnicode=true&characterEncoding=UTF-8&" + "user=" + user +
                ((password.length() != 0) ? "&password=" + password : ""));

    }

    public boolean isNotConnected() {
        try {
            return connection.isClosed();
        } catch (Exception var1) {
            return true;
        }
    }

    public void executeSync(String query) throws SQLException {
        if (isNotConnected()) {
            connect();
        }
        connection.createStatement().execute(strip(query));

    }

    public ResultSet executeQuery(String query) throws SQLException {
        if (isNotConnected()) {
            connect();
        }
        return connection.createStatement().executeQuery(strip(query));
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    public String getPrefix() {
        return prefix;
    }
}