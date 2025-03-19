package com.Project_Group2.controller.userController;

import com.Project_Group2.dto.ProductDTO;
import com.Project_Group2.entity.Product;
import com.Project_Group2.entity.ProductImage;
import com.Project_Group2.entity.ProductVariant;
import com.Project_Group2.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {
    ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/product-details")
    public String productDetails(Model model, @RequestParam("id") int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        List<ProductImage> listProductImages = product.getImages();
        List<ProductVariant> listProductVariants = product.getVariants();
        List<Product> listSameCategory = productRepository.findByCategoryAndProductIdNot(product.getCategory(), productId);

        for (ProductImage productImage : listProductImages) {
            System.out.println(productImage.getImageUrl());
            System.out.println(" ");
            System.out.println(" ");
            System.out.println("abc");
        }
        ProductImage productImage1 = listProductImages.get(0);
        ProductImage productImage2 = listProductImages.get(1);
        model.addAttribute("product", product);
        model.addAttribute("productImage1", productImage1);
        model.addAttribute("productImage2", productImage2);
        model.addAttribute("listProductImages", listProductImages);
        model.addAttribute("listSameCategory", listSameCategory);
        return "user/product-details";
    }
}
