package com.study.onlineshop.web.servlet;

import com.study.onlineshop.ServiceLocator;
import com.study.onlineshop.entity.Cart;
import com.study.onlineshop.entity.Product;
import com.study.onlineshop.entity.UserRole;
import com.study.onlineshop.security.SecurityService;
import com.study.onlineshop.security.Session;
import com.study.onlineshop.service.ProductService;
import com.study.onlineshop.web.service.CookieService;
import com.study.onlineshop.web.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public class ProductsServlet extends HttpServlet {
    private ProductService productService;
    private SecurityService securityService;

    @Override
    public void init() throws ServletException {
        productService = ServiceLocator.getServiceLocator().getService(ProductService.class);
        securityService = ServiceLocator.getServiceLocator().getService(SecurityService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String token = CookieService.getTokenFromCookies(request.getCookies());
        int cartCount = 0;
        if (token != null) {
            PageGenerator pageGenerator = PageGenerator.instance();
            List<Product> products = productService.getAll();

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("products", products);
            Session session = securityService.getSession(token);
            if (session != null) {
                Cart cart = session.getCart();
                if (cart != null) {
                    cartCount = cart.getProducts().size();
                }
            }
            parameters.put("cartCount", cartCount);

            boolean editMode = securityService.checkTokenPermissions(token, EnumSet.of(UserRole.ADMIN));
            parameters.put("editMode", editMode);

            String page = pageGenerator.getPage("products", parameters);
            response.getWriter().write(page);
        } else {
            response.sendRedirect("/login");
        }
    }

}
