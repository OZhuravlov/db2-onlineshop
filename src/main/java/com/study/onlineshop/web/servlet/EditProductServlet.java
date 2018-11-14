package com.study.onlineshop.web.servlet;

import com.study.onlineshop.ServiceLocator;
import com.study.onlineshop.entity.Product;
import com.study.onlineshop.service.ProductService;
import com.study.onlineshop.web.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class EditProductServlet extends HttpServlet {
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        productService = ServiceLocator.getServiceLocator().getService(ProductService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int id = getId(request.getRequestURI());
        PageGenerator pageGenerator = PageGenerator.instance();
        Product product = productService.getById(id);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("product", product);

        String page = pageGenerator.getPage("product-edit", parameters);
        response.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        if (name == null || name.isEmpty() || price <= 0d) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            int id = getId(request.getRequestURI());
            productService.update(id, name, price);
            response.sendRedirect("/products");
        }
    }

    private int getId(String uri) {
        int index = uri.lastIndexOf("/");
        int id = Integer.valueOf(uri.substring(index + 1, uri.length()));
        return id;
    }

}
