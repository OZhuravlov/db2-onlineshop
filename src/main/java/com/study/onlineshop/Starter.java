package com.study.onlineshop;

import com.study.onlineshop.dao.jdbc.JdbcProductDao;
import com.study.onlineshop.dao.jdbc.JdbcUserDao;
import com.study.onlineshop.dao.jdbc.ConnectionProvider;
import com.study.onlineshop.security.SecurityService;
import com.study.onlineshop.service.impl.DefaultProductService;
import com.study.onlineshop.service.impl.DefaultUserService;
import com.study.onlineshop.web.filter.AdminRoleSecurityFilter;
import com.study.onlineshop.web.filter.UserRoleSecurityFilter;
import com.study.onlineshop.web.servlet.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class Starter {
    public static void main(String[] args) throws Exception {
        // configure daos
        ConnectionProvider connectionProvider = new ConnectionProvider();
        connectionProvider.init();
        JdbcProductDao jdbcProductDao = new JdbcProductDao();
        jdbcProductDao.setConnectionProvider(connectionProvider);
        JdbcUserDao jdbcUserDao = new JdbcUserDao();
        jdbcUserDao.setConnectionProvider(connectionProvider);

        // configure services
        DefaultProductService productService = new DefaultProductService(jdbcProductDao);
        DefaultUserService userService = new DefaultUserService(jdbcUserDao);
        SecurityService securityService = new SecurityService();
        securityService.setUserService(userService);

        // servlets
        ProductsServlet productsServlet = new ProductsServlet();
        productsServlet.setProductService(productService);
        productsServlet.setSecurityService(securityService);

        DeleteProductServlet deleteProductServlet = new DeleteProductServlet();
        deleteProductServlet.setProductService(productService);

        AddProductServlet addProductServlet = new AddProductServlet();
        addProductServlet.setProductService(productService);

        EditProductServlet editProductServlet = new EditProductServlet();
        editProductServlet.setProductService(productService);

//        ProductsApiServlet productsApiServlet = new ProductsApiServlet(productService);

        // config web server
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.addServlet(new ServletHolder(productsServlet), "/products");
        servletContextHandler.addServlet(new ServletHolder(productsServlet), "/");
        servletContextHandler.addServlet(new ServletHolder(deleteProductServlet), "/product/delete/*");
        servletContextHandler.addServlet(new ServletHolder(addProductServlet), "/product/add");
        servletContextHandler.addServlet(new ServletHolder(editProductServlet), "/product/edit/*");

//        servletContextHandler.addServlet(new ServletHolder(productsApiServlet), "/api/v1/products");
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(securityService)), "/login");
        servletContextHandler.addServlet(new ServletHolder(new LogoutServlet(securityService)), "/logout");
        RegisterServlet registerServlet = new RegisterServlet(securityService);
        registerServlet.setUserService(userService);
        servletContextHandler.addServlet(new ServletHolder(registerServlet), "/register");

        servletContextHandler.addFilter(new FilterHolder(new UserRoleSecurityFilter(securityService)), "/products",
                EnumSet.of(DispatcherType.REQUEST));

        servletContextHandler.addFilter(new FilterHolder(new AdminRoleSecurityFilter(securityService)), "/product",
                EnumSet.of(DispatcherType.REQUEST));

        Server server = new Server(8080);
        server.setHandler(servletContextHandler);
        server.start();
    }
}
