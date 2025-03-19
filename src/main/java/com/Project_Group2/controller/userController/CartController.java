package com.Project_Group2.controller.userController;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartController {
    @GetMapping("/checkout")
    public String getCheckoutPage(Model model) {
        return "user/checkout";
    }
}
