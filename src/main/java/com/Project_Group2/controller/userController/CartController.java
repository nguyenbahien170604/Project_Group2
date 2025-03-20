package com.Project_Group2.controller.userController;

import com.Project_Group2.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;



    @GetMapping("/checkout")
    public String getCheckoutPage(Model model) {
        return "user/checkout";
    }

    @PostMapping("/cart/add")
    public String addToCart(HttpSession session,
                            @RequestParam("productId") int productId,
                            @RequestParam("size") String size,
                            @RequestParam("color") String color,
                            @RequestParam("quantity") int quantity) {
        try {
            cartService.addToCart(session, productId, size, color, quantity);
            return "redirect:/product-details?id="+productId;
        } catch (Exception e) {
            session.setAttribute("errorMessage", e.getMessage());
            return "redirect:/product-details?id="+productId;
        }
    }

    @GetMapping("/cart/remove")
    public String removeCartItem(@RequestParam("id") int cartDetailId, HttpSession session) {
        int cartSize = cartService.removeCartItem(cartDetailId);
        if (cartSize != -1) {
            session.setAttribute("cartSize", cartSize);
        }
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session,
                           @RequestParam("receivedName") String receivedName,
                           @RequestParam("receivedPhone") String receivedPhone,
                           @RequestParam("receivedAddress") String receivedAddress,
                           Model model) {

        boolean success = cartService.checkout(session, receivedName, receivedPhone, receivedAddress);

        if (!success) {
            model.addAttribute("error", "Checkout failed! Please check your cart.");
            return "redirect:/cart";
        }

        return "user/order-success";
    }
}

