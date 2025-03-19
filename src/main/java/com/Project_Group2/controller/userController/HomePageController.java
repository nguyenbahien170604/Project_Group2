package com.Project_Group2.controller.userController;

import com.Project_Group2.dto.BlogDTO;
import com.Project_Group2.dto.ProductDTO;
import com.Project_Group2.service.BlogService;
import com.Project_Group2.service.ProductService;
import com.Project_Group2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomePageController {

    private final ProductService productService;
    private final UserService userService;
    private final BlogService blogService;

    public HomePageController(ProductService productService, UserService userService, BlogService blogService) {
        this.productService = productService;
        this.userService = userService;
        this.blogService = blogService;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<ProductDTO> listProducts = productService.getAllProducts();
        model.addAttribute("listProducts", listProducts);
        return "user/homepage";
    }

    @GetMapping("/shop")
    public String getShopPage(Model model) {
        List<ProductDTO> listProducts = productService.getAllProducts();
        model.addAttribute("listProducts", listProducts);
        return "user/shop";
    }

    @GetMapping("/blog")
    public String getBlogPage(Model model) {
        List<BlogDTO> blogDTOList = blogService.getAllBlogs();
        model.addAttribute("blogDTOList", blogDTOList);
        return "user/blog";
    }

    @GetMapping("/contact")
    public String getContactPage(Model model) {
        return "user/contact";
    }


    @GetMapping("/cart")
    public String getCartPage(Model model) {
        return "user/cart";
    }


}
