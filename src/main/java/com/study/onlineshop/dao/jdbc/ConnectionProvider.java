package com.study.onlineshop.dao.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionProvider {
    Properties properties;
    public ConnectionProvider(){
        properties = new Properties();
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            properties.load(classLoader.getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() throws SQLException {
        String url = properties.getProperty("jdbc.url");
        String username = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);
    }
}
