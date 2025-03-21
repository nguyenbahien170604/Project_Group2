package com.Project_Group2.controller.userController;

import com.Project_Group2.dto.ProductDTO;
import com.Project_Group2.entity.Product;
import com.Project_Group2.entity.ProductImage;
import com.Project_Group2.entity.ProductVariant;
import com.Project_Group2.repository.ProductRepository;
import com.Project_Group2.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductController {
    ProductRepository productRepository;
    ProductService productService;

    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @GetMapping("/product-details")
    public String productDetails(Model model, @RequestParam("id") int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        List<ProductImage> listProductImages = product.getImages();
        List<ProductVariant> listProductVariants = product.getVariants();
        List<ProductDTO> listSameCategory = productService.getSameCategoryProducts(product.getCategory().getCategoryId(), productId);
        ProductImage productImage1 = listProductImages.get(0);
        ProductImage productImage2 = listProductImages.get(1);
        List<String> uniqueSizes = product.getVariants().stream()
                .map(ProductVariant::getSize)
                .distinct()
                .collect(Collectors.toList());

        List<String> uniqueColors = product.getVariants().stream()
                .map(ProductVariant::getColor)
                .distinct()
                .collect(Collectors.toList());

        // Thêm vào model
        model.addAttribute("uniqueSizes", uniqueSizes);
        model.addAttribute("uniqueColors", uniqueColors);
        model.addAttribute("product", product);
        model.addAttribute("productImage1", productImage1);
        model.addAttribute("productImage2", productImage2);
        model.addAttribute("listProductImages", listProductImages);
        model.addAttribute("listSameCategory", listSameCategory);
        return "user/product-details";
    }
}
