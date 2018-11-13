package com.study.onlineshop;

import com.study.onlineshop.dao.ProductDao;
import com.study.onlineshop.dao.UserDao;
import com.study.onlineshop.dao.jdbc.JdbcProductDao;
import com.study.onlineshop.dao.jdbc.JdbcUserDao;
import com.study.onlineshop.security.SecurityService;
import com.study.onlineshop.service.CartService;
import com.study.onlineshop.service.ProductService;
import com.study.onlineshop.service.UserService;
import com.study.onlineshop.service.impl.DefaultCartService;
import com.study.onlineshop.service.impl.DefaultProductService;
import com.study.onlineshop.service.impl.DefaultUserService;

import java.util.HashMap;
import java.util.Properties;

public class ServiceLocator {
    private HashMap<Class<?>, Object> SERVICES = new HashMap<>();
    private static ServiceLocator serviceLocator;
    private final static String PROPERTIES_FILENAME = "application.properties";

    public static ServiceLocator getServiceLocator() {
        if (serviceLocator == null) {
            serviceLocator = new ServiceLocator();
        }
        return serviceLocator;
    }

    private ServiceLocator() {
//        //Properties
//        PropertiesService propertiesService = new PropertiesService(PROPERTIES_FILENAME);
//        registerService(PropertiesService.class, propertiesService);
//
//        // ConnectionProvider
//        ConnectionProvider connectionProvider = new ConnectionProvider();
//        registerService(ConnectionProvider.class, connectionProvider);
//
//        // dao config
//        ProductDao productDao = new JdbcProductDao();
//        productDao.setConnectionProvider(connectionProvider);
//        registerService(ProductDao.class, productDao);
//        UserDao userDao = new JdbcUserDao();
//        userDao.setConnectionProvider(connectionProvider);
//        registerService(UserDao.class, productDao);
//
//        // configure services
//        ProductService productService = new DefaultProductService(productDao);
//        registerService(ProductService.class, productService);
//        UserService userService = new DefaultUserService(userDao);
//        registerService(UserService.class, userService);
//        SecurityService securityService = new SecurityService();
//        securityService.setUserService(userService);
//        registerService(SecurityService.class, securityService);
//        CartService cartService = new DefaultCartService();
//        cartService.setProductService(productService);
//        registerService(CartService.class, cartService);

    }

    public void registerService(Class<?> serviceClazz, Object service) {
        SERVICES.put(serviceClazz, service);
    }


    public <T> T getService(Class<T> serviceClass) {
        Object service = SERVICES.get(serviceClass);
        return serviceClass.cast(service);
    }

}
