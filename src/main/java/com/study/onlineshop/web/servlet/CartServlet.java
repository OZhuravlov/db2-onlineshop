package com.study.onlineshop.web.servlet;

import com.study.onlineshop.ServiceLocator;
import com.study.onlineshop.entity.Cart;
import com.study.onlineshop.entity.Product;
import com.study.onlineshop.security.SecurityService;
import com.study.onlineshop.security.Session;
import com.study.onlineshop.service.CartService;
import com.study.onlineshop.web.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CartServlet extends HttpServlet {
    private SecurityService securityService;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        securityService = ServiceLocator.getServiceLocator().getService(SecurityService.class);
        cartService = ServiceLocator.getServiceLocator().getService(CartService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PageGenerator pageGenerator = PageGenerator.instance();
        HashMap<String, Object> parameters = new HashMap<>();

        String token = securityService.getValidatedToken(request.getCookies());
        if (token != null) {
            Session session = securityService.getSession(token);
            Cart cart = session.getCart();
            if (cart != null) {
                List<Product> cartProducts = cart.getProducts();
                if (!cartProducts.isEmpty()) {
                    parameters.put("cartProducts", cartProducts);
                }
            }
        }
        String page = pageGenerator.getPage("cart", parameters);
        response.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = securityService.getValidatedToken(request.getCookies());
        if (token != null) {
            Session session = securityService.getSession(token);
            Cart cart = session.getCart();
            if (cart == null) {
                cart = new Cart();
                session.setCart(cart);
            }
            int productId = Integer.valueOf(request.getParameter("product_id"));
            cartService.addToCart(cart, productId);
            response.sendRedirect("/products");
        } else {
            response.sendRedirect("/login");
        }
    }

}
