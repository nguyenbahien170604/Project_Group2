package com.Project_Group2.controller.userController;

import com.Project_Group2.dto.BlogDTO;
import com.Project_Group2.dto.ProductDTO;
import com.Project_Group2.entity.*;
import com.Project_Group2.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Controller
public class HomePageController {

    private final ProductService productService;
    private final UserService userService;
    private final BlogService blogService;
    private final CartService cartService;
    private final SliderService sliderService;

    public HomePageController(ProductService productService, UserService userService, BlogService blogService, CartService cartService, SliderService sliderService) {
        this.productService = productService;
        this.userService = userService;
        this.blogService = blogService;
        this.cartService = cartService;
        this.sliderService = sliderService;
    }

    @GetMapping("/")
    public String getHomePage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // Nếu chưa đăng nhập, chuyển hướng về login
        }
        List<ProductDTO> listProducts = productService.getAllProducts();
        model.addAttribute("listProducts", listProducts);

        List<ProductDTO> products1 = productService.getProductsByCategoryId(1);
        model.addAttribute("products1", products1);

        List<ProductDTO> products2 = productService.getProductsByCategoryId(2);
        model.addAttribute("products2", products2);

        List<ProductDTO> products3 = productService.getProductsByCategoryId(3);
        model.addAttribute("products3", products3);

        List<ProductDTO> latestProducts = productService.get6LatestProducts();

        List<Slider> top3Slider = sliderService.getTop3Slider();

        ProductDTO last1 = latestProducts.get(latestProducts.size()-1);
        ProductDTO last2 = latestProducts.get(latestProducts.size()-2);
        ProductDTO last3 = latestProducts.get(latestProducts.size()-3);
        ProductDTO last4 = latestProducts.get(latestProducts.size()-4);
        ProductDTO last5 = latestProducts.get(latestProducts.size()-5);
        ProductDTO last6 = latestProducts.get(latestProducts.size()-6);
        model.addAttribute("last1", last1);
        model.addAttribute("last2", last2);
        model.addAttribute("last3", last3);
        model.addAttribute("last4", last4);
        model.addAttribute("last5", last5);
        model.addAttribute("last6", last6);
        model.addAttribute("latestProducts", latestProducts);
        model.addAttribute("top3Slider", top3Slider);

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
        List<Blog> Top4BlogList = blogService.get4Blog();
        model.addAttribute("blogDTOList", blogDTOList);
        model.addAttribute("Top4BlogList",Top4BlogList);
        return "user/blog";
    }

    @GetMapping("/contact")
    public String getContactPage(Model model) {
        return "user/contact";
    }


    @GetMapping("/cart")
    public String getCartPage(HttpSession session ,Model model) {
        List<CartDetails> cartItems = cartService.getCartItems();
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalVnPay = BigDecimal.ZERO;

        for (CartDetails cartItem : cartItems) {
            Product product = cartItem.getProductVariant().getProduct();
            BigDecimal price = product.getPrice();
            BigDecimal discount = BigDecimal.valueOf(product.getDiscount());

            // Tính giá sau giảm giá
            BigDecimal discountedPrice = price.multiply(BigDecimal.valueOf(100).subtract(discount))
                    .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

            // Tổng tiền của sản phẩm đó
            BigDecimal itemTotal = discountedPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            total = total.add(itemTotal);
        }
        totalVnPay = total;
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        session.setAttribute("totalVnPay", totalVnPay);
        return "user/cart";
    }

}
