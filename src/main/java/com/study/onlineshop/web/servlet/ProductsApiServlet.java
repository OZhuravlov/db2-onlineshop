package com.study.onlineshop.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.onlineshop.entity.Product;
import com.study.onlineshop.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProductsApiServlet extends HttpServlet {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private ProductService productService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> products = productService.getAll();
        // products -> json

        String json = OBJECT_MAPPER.writeValueAsString(products);
        resp.getWriter().write(json);
    }

    public ProductsApiServlet(ProductService productService) {
        this.productService = productService;
    }
}
