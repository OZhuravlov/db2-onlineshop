package com.study.onlineshop.web.controller;

import com.study.onlineshop.entity.Cart;
import com.study.onlineshop.entity.Product;
import com.study.onlineshop.entity.UserRole;
import com.study.onlineshop.security.Session;
import com.study.onlineshop.service.ProductService;
import com.study.onlineshop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    private PageGenerator pageGenerator = PageGenerator.instance();

    @RequestMapping(value = {"/products", "/"}, method = RequestMethod.GET)
    @ResponseBody
    public String getAll(HttpServletRequest request) throws IOException {
        List<Product> products = productService.getAll();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("products", products);
        Session session = (Session) request.getAttribute("session");
        int cartCount = 0;
        if (session != null) {
            Cart cart = session.getCart();
            if (cart != null) {
                cartCount = cart.getProducts().size();
            }
        }
        parameters.put("cartCount", cartCount);

        boolean editMode = session.getUser().getUserRole().equals(UserRole.ADMIN);
        parameters.put("editMode", editMode);

        String page = pageGenerator.getPage("products", parameters);
        return page;
    }

    @RequestMapping(path = "/product/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getByIdPath(@PathVariable int id) throws IOException {
        List<Product> products = Arrays.asList(productService.getById(id));
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("products", products);
        String page = pageGenerator.getPage("products", parameters);
        return page;
    }

    @RequestMapping(path = "/product/add", method = RequestMethod.POST)
    public void addProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
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


    @RequestMapping(path = "/product/edit/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getEditProduct(@PathVariable int id) throws IOException {
        PageGenerator pageGenerator = PageGenerator.instance();
        Product product = productService.getById(id);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("product", product);
        String page = pageGenerator.getPage("product-edit", parameters);
        return page;
    }


    @RequestMapping(path = "/product/edit/{id}", method = RequestMethod.POST)
    public void editProduct(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws IOException {
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        if (name == null || name.isEmpty() || price <= 0d) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            productService.update(id, name, price);
            response.sendRedirect("/products");
        }
    }

    @RequestMapping(path = "/product/delete/{id}", method = RequestMethod.POST)
    public void deleteProduct(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws IOException {
        productService.delete(id);
        response.sendRedirect("/products");
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
