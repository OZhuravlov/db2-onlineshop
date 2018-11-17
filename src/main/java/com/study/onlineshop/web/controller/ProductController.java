package com.study.onlineshop.web.controller;

import com.study.onlineshop.entity.Cart;
import com.study.onlineshop.entity.Product;
import com.study.onlineshop.entity.UserRole;
import com.study.onlineshop.security.Session;
import com.study.onlineshop.service.ProductService;
import com.study.onlineshop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Controller
public class ProductController {

    private ProductService productService;

    private PageGenerator pageGenerator = PageGenerator.instance();

    @RequestMapping(value = {"/products", "/"}, method = RequestMethod.GET)
    @ResponseBody
    public String getAll(@RequestAttribute Session session) throws IOException {
        List<Product> products = productService.getAll();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("products", products);
        int cartCount = 0;
        Cart cart = session.getCart();
        if (cart != null) {
            cartCount = cart.getProducts().size();
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
    public String addProduct(@RequestParam String name,
                             @RequestParam double price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCreationDate(LocalDateTime.now());
        productService.add(product);
        return "redirect:/products";
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
    public String editProduct(@RequestParam String name, @RequestParam double price, @PathVariable int id) {
        if (name == null || name.isEmpty() || price <= 0d) {
            return "redirect:/product/" + id;
        } else {
            productService.update(id, name, price);
            return "redirect:/products";
        }
    }

    @RequestMapping(path = "/product/delete/{id}", method = RequestMethod.POST)
    public String deleteProduct(@PathVariable int id) {
        productService.delete(id);
        return "redirect:/products";
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
