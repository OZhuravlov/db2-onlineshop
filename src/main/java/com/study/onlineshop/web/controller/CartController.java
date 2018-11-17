package com.study.onlineshop.web.controller;

import com.study.onlineshop.entity.Cart;
import com.study.onlineshop.entity.Product;
import com.study.onlineshop.security.Session;
import com.study.onlineshop.service.CartService;
import com.study.onlineshop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
public class CartController {

    private CartService cartService;
    private PageGenerator pageGenerator = PageGenerator.instance();

    @RequestMapping(path = "/cart", method = RequestMethod.GET)
    @ResponseBody
    public String getCart(@RequestAttribute Session session) {
        HashMap<String, Object> parameters = new HashMap<>();
        Cart cart = session.getCart();
        if (cart != null) {
            List<Product> cartProducts = cart.getProducts();
            parameters.put("cartProducts", cartProducts);
        }
        String page = pageGenerator.getPage("cart", parameters);
        return page;
    }

    @RequestMapping(path = "/cart", method = RequestMethod.POST)
    public String addToCart(@RequestAttribute Session session,
                            @RequestParam("product_id") int productId,
                            HttpServletResponse response) throws IOException {
        Cart cart = session.getCart();
        if (cart == null) {
            cart = new Cart();
            session.setCart(cart);
        }
        cartService.addToCart(cart, productId);
        return "redirect:/products";
    }

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }
}
