package com.study.onlineshop.dao.jdbc;

import com.study.onlineshop.PropertiesService;
import com.study.onlineshop.ServiceLocator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {

    public Connection getConnection() throws SQLException {
        PropertiesService propertiesService = ServiceLocator.getServiceLocator().getService(PropertiesService.class);
        String url = propertiesService.getProperty("jdbc.url");
        String username = propertiesService.getProperty("jdbc.username");
        String password = propertiesService.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);
    }
}
