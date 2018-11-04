package com.study.onlineshop;

import com.study.onlineshop.dao.jdbc.ConnectionProvider;
import com.study.onlineshop.dao.jdbc.JdbcProductDao;
import com.study.onlineshop.dao.jdbc.JdbcUserDao;
import com.study.onlineshop.security.SecurityService;
import com.study.onlineshop.service.CartService;
import com.study.onlineshop.service.ProductService;
import com.study.onlineshop.service.UserService;
import com.study.onlineshop.service.impl.DefaultCartService;
import com.study.onlineshop.service.impl.DefaultProductService;
import com.study.onlineshop.service.impl.DefaultUserService;
import com.study.onlineshop.web.filter.AdminRoleSecurityFilter;
import com.study.onlineshop.web.filter.UserRoleSecurityFilter;
import com.study.onlineshop.web.servlet.*;
import com.study.onlineshop.web.templater.PageGenerator;
import freemarker.template.Configuration;
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
        ProductService productService = new DefaultProductService(jdbcProductDao);
        UserService userService = new DefaultUserService(jdbcUserDao);
        SecurityService securityService = new SecurityService();
        securityService.setUserService(userService);
        DefaultCartService cartService = new DefaultCartService();
        cartService.setProductService(productService);

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

        CartServlet cartServlet = new CartServlet();
        cartServlet.setSecurityService(securityService);
        cartServlet.setCartService(cartService);

//        ProductsApiServlet productsApiServlet = new ProductsApiServlet(productService);

        // config web server
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.addServlet(new ServletHolder(productsServlet), "/products");
        servletContextHandler.addServlet(new ServletHolder(productsServlet), "/");
        servletContextHandler.addServlet(new ServletHolder(deleteProductServlet), "/product/delete/*");
        servletContextHandler.addServlet(new ServletHolder(addProductServlet), "/product/add");
        servletContextHandler.addServlet(new ServletHolder(editProductServlet), "/product/edit/*");
        servletContextHandler.addServlet(new ServletHolder(cartServlet), "/cart");
        servletContextHandler.addServlet(new ServletHolder(cartServlet), "/cart/*");

//        servletContextHandler.addServlet(new ServletHolder(productsApiServlet), "/api/v1/products");
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(securityService)), "/login");
        servletContextHandler.addServlet(new ServletHolder(new LogoutServlet(securityService)), "/logout");
        RegisterServlet registerServlet = new RegisterServlet(securityService);
        registerServlet.setUserService(userService);
        servletContextHandler.addServlet(new ServletHolder(registerServlet), "/register");

        servletContextHandler.addFilter(new FilterHolder(new UserRoleSecurityFilter(securityService)), "/products",
                EnumSet.of(DispatcherType.REQUEST));
        servletContextHandler.addFilter(new FilterHolder(new UserRoleSecurityFilter(securityService)), "/cart",
                EnumSet.of(DispatcherType.REQUEST));

        servletContextHandler.addFilter(new FilterHolder(new AdminRoleSecurityFilter(securityService)), "/product",
                EnumSet.of(DispatcherType.REQUEST));

        Server server = new Server(8080);
        server.setHandler(servletContextHandler);
        server.start();
    }
}
