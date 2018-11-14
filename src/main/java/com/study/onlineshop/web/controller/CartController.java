package com.study.onlineshop.web.controller;

import com.study.onlineshop.entity.Cart;
import com.study.onlineshop.entity.Product;
import com.study.onlineshop.security.Session;
import com.study.onlineshop.service.CartService;
import com.study.onlineshop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    private PageGenerator pageGenerator = PageGenerator.instance();

    @RequestMapping(path = "/cart", method = RequestMethod.GET)
    @ResponseBody
    public String getCart(HttpServletRequest request) throws IOException {
        HashMap<String, Object> parameters = new HashMap<>();
        Session session = (Session) request.getAttribute("session");
        Cart cart = session.getCart();
        if (cart != null) {
            List<Product> cartProducts = cart.getProducts();
            if (!cartProducts.isEmpty()) {
                parameters.put("cartProducts", cartProducts);
            }

        }
        String page = pageGenerator.getPage("cart", parameters);
        return page;
    }

    @RequestMapping(path = "/cart", method = RequestMethod.POST)
    public void addToCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Session session = (Session) request.getAttribute("session");
        Cart cart = session.getCart();
        if (cart == null) {
            cart = new Cart();
            session.setCart(cart);
        }
        int productId = Integer.parseInt(request.getParameter("product_id"));
        cartService.addToCart(cart, productId);
        response.sendRedirect("/products");
    }

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }
}
