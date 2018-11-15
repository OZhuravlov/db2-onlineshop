package com.study.onlineshop.web.controller;

import com.study.onlineshop.entity.Cart;
import com.study.onlineshop.entity.Product;
import com.study.onlineshop.entity.UserRole;
import com.study.onlineshop.security.Session;
import com.study.onlineshop.service.ProductService;
import com.study.onlineshop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = {"/products", "/"}, method = RequestMethod.GET)
    public String getAll(HttpServletRequest request, Model model) throws IOException {
        List<Product> products = productService.getAll();
        model.addAttribute("products", products);
        Session session = (Session) request.getAttribute("session");
        int cartCount = 0;
        if (session != null) {
            Cart cart = session.getCart();
            if (cart != null) {
                cartCount = cart.getProducts().size();
            }
        }
        model.addAttribute("cartCount", cartCount);
        boolean editMode = session.getUser().getUserRole().equals(UserRole.ADMIN);
        model.addAttribute("editMode", editMode);

        return "products";
    }

    @RequestMapping(path = "/product/{id}", method = RequestMethod.GET)
    public String getByIdPath(@PathVariable int id, Model model) throws IOException {
        List<Product> products = Arrays.asList(productService.getById(id));
        model.addAttribute("products", products);
        return "products";
    }

    @RequestMapping(path = "/product/add", method = RequestMethod.POST)
    public String addProduct(HttpServletRequest request
            , HttpServletResponse response
            , @ModelAttribute Product product) throws IOException {
        product.setCreationDate(LocalDateTime.now());
        int id = productService.add(product);
        product.setId(id);
        return "redirect:/products";
    }


    @RequestMapping(path = "/product/edit/{id}", method = RequestMethod.GET)
    public String getEditProduct(@PathVariable int id, Model model) throws IOException {
        Product product = productService.getById(id);
        HashMap<String, Object> parameters = new HashMap<>();
        model.addAttribute("product", product);
        return "product-edit";
    }


    @RequestMapping(path = "/product/edit/{id}", method = RequestMethod.POST)
    public String editProduct(HttpServletRequest request, HttpServletResponse response
            , @PathVariable int id) throws IOException {
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        if (name == null || name.isEmpty() || price <= 0d) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            productService.update(id, name, price);
            return ("redirect:/products");
        }
    }

    @RequestMapping(path = "/product/delete/{id}", method = RequestMethod.POST)
    public String deleteProduct(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws IOException {
        productService.delete(id);
        return "redirect:/products";
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
