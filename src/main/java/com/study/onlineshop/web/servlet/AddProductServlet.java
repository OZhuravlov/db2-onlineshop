package com.study.onlineshop.web.servlet;

import com.study.onlineshop.ServiceLocator;
import com.study.onlineshop.entity.Product;
import com.study.onlineshop.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class AddProductServlet extends HttpServlet {
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        productService = ServiceLocator.getServiceLocator().getService(ProductService.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();

        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCreationDate(LocalDateTime.now());
        int id = productService.add(product);
        product.setId(id);
        response.sendRedirect("/products");
    }

}
