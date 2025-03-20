package com.Project_Group2.controller.userController;

import ch.qos.logback.core.model.Model;
import com.Project_Group2.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;


    @GetMapping("/checkout")
    public String getCheckoutPage(Model model) {
        return "user/checkout";
    }

    @PostMapping("cart/add")
    public String addToCart(@RequestParam("productId") int productId,
                            @RequestParam("size") String size,
                            @RequestParam("color") String color,
                            @RequestParam("quantity") int quantity) {
        int userId = 1;

        cartService.addToCart(userId, productId, size, color, quantity);
        return "redirect:/cart";
    }
}
