package com.study.onlineshop.web.servlet;

import com.study.onlineshop.ServiceLocator;
import com.study.onlineshop.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteProductServlet extends HttpServlet {
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        productService = ServiceLocator.getServiceLocator().getService(ProductService.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        int index = uri.lastIndexOf("/");
        int id = Integer.valueOf(uri.substring(index + 1));
        productService.delete(id);
        resp.sendRedirect("/products");
    }

}
