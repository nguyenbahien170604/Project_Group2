package com.Project_Group2.controller.userController;

import com.Project_Group2.dto.FilterRequest;
import com.Project_Group2.dto.ProductDTO;
import com.Project_Group2.entity.*;
import com.Project_Group2.repository.BrandRepository;
import com.Project_Group2.repository.CategoryRepository;
import com.Project_Group2.repository.ProductRepository;
import com.Project_Group2.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductController {
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    ProductRepository productRepository;
    ProductService productService;

    public ProductController(ProductRepository productRepository, ProductService productService, CategoryRepository categoryRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
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

    @GetMapping("/shopSort")
    public String getProducts(Model model,
                              @RequestParam(name = "sort", required = false, defaultValue = "name_asc") String sortBy) {
        List<Category> categories = categoryRepository.findAll();
        List<Brand> brands = brandRepository.findAll();
        List<ProductDTO> listProducts = productService.getSortedProducts(sortBy);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("currentSort", sortBy);
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        return "user/shop";
    }

    @GetMapping("/shopCategory")
    public String shopCategory(@RequestParam(value = "categoryId", required = false) Integer categoryId, Model model) {
        List<Category> categories = categoryRepository.findAll();
        List<ProductDTO> listProducts;
        List<Brand> brands = brandRepository.findAll();
        if (categoryId != null) {
            listProducts = productService.getProductsByCategory(categoryId);
        } else {
            listProducts = productService.getAllProducts();
        }
        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        model.addAttribute("listProducts", listProducts);

        return "user/shop";
    }

    @GetMapping("/shopBrand")
    public String shopBrand(@RequestParam(value = "brandId", required = false) Integer brandId, Model model) {
        List<Category> categories = categoryRepository.findAll();
        List<ProductDTO> listProducts;
        List<Brand> brands = brandRepository.findAll();

        if (brandId != null) {
            listProducts = productService.getProductsByBrand(brandId);
        } else {
            listProducts = productService.getAllProducts();
        }
        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        model.addAttribute("listProducts", listProducts);

        return "user/shop";
    }

    @GetMapping("/shopFilter")
    public String shopFilter(
            @RequestParam(value = "sizes", required = false) List<String> sizes,
            @RequestParam(value = "colors", required = false) List<String> colors,
            @RequestParam(value = "categories", required = false) List<Integer> categories,
            @RequestParam(value = "brands", required = false) List<Integer> brands,
            Model model) {

        // Xử lý khi danh sách null hoặc rỗng
        sizes = (sizes == null || sizes.isEmpty()) ? null : sizes;
        colors = (colors == null || colors.isEmpty()) ? null : colors;
        categories = (categories == null || categories.isEmpty()) ? null : categories;
        brands = (brands == null || brands.isEmpty()) ? null : brands;

        // Debug: In giá trị truyền vào để kiểm tra
        System.out.println("Sizes: " + sizes);
        System.out.println("Colors: " + colors);
        System.out.println("Categories: " + categories);
        System.out.println("Brands: " + brands);

        // Lọc sản phẩm
        List<ProductDTO> filteredProducts = productService.filterProducts(sizes, colors, categories, brands);

        // Đưa dữ liệu vào Model
        model.addAttribute("listProducts", filteredProducts);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("brands", brandRepository.findAll());

        // Giữ trạng thái đã chọn
        model.addAttribute("sizesSelected", sizes);
        model.addAttribute("colorsSelected", colors);
        model.addAttribute("categoriesSelected", categories);
        model.addAttribute("brandsSelected", brands);

        return "user/shop";
    }
}

