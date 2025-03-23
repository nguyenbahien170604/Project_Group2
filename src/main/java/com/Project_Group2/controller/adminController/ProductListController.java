package com.Project_Group2.controller.adminController;

import com.Project_Group2.dto.AddProductDTO;
import com.Project_Group2.dto.ClassifyDTO;
import com.Project_Group2.dto.ManagerDTO;
import com.Project_Group2.dto.ProductDetailsDTO;
import com.Project_Group2.entity.Brand;
import com.Project_Group2.entity.Category;
import com.Project_Group2.entity.Product;
import com.Project_Group2.entity.User;
import com.Project_Group2.service.BrandService;
import com.Project_Group2.service.CategoryService;
import com.Project_Group2.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductListController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/productList")
    public String managerList(@RequestParam(value = "search", required = false) String search,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {

        Page<Product> products;

        if (search != null && !search.trim().isEmpty()) {
            products = productService.searchProducts(search, page, size);
        } else {
            products = productService.getAllProduct(page, size);
        }

        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("search", search);
        return "admin/productList";
    }

    @GetMapping("/addProduct")
    public String addManager(Model model) {
        List<Brand> getAllBrands = brandService.findAll();
        List<Category> getAllCategories = categoryService.findAll();
        model.addAttribute("categories", getAllCategories);
        model.addAttribute("brands", getAllBrands);
        return "admin/addProduct";
    }

    @PostMapping("/addProduct")
    public String createStore(@Valid AddProductDTO addProductDTO,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                redirectAttributes.addFlashAttribute("error", fieldError.getDefaultMessage());
            }
            return "redirect:/addProduct";
        }
        
        try {
            Product product = productService.addProduct(addProductDTO, redirectAttributes);
            if (product == null) {
                return "redirect:/addProduct";
            }
            model.addAttribute("product", product);
            redirectAttributes.addFlashAttribute("success", "Added product successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred, please try again!");
        }

        return "redirect:/productList";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable int id, Model model) {
        Product product = productService.findProductById(id);
        if (product == null) {
            return "redirect:/productList";
        }
        model.addAttribute("product", product);
        return "admin/editProduct";
    }

    @GetMapping("/productDetails/{id}")
    public String productDetails(@PathVariable int id, Model model) {
        Product product = productService.findProductById(id);
        if (product == null) {
            return "redirect:/productList";
        }

        ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO();
        productDetailsDTO.setName(product.getProductName());
        productDetailsDTO.setPrice(product.getPrice());
        productDetailsDTO.setDescription(product.getDescription());
        productDetailsDTO.setColors(
                product.getVariants().stream()
                        .map(variant -> variant.getColor())
                        .collect(Collectors.toList())
        );
        productDetailsDTO.setSizes(
                product.getVariants().stream()
                        .map(variant -> variant.getSize())
                        .collect(Collectors.toList())
        );
        productDetailsDTO.setQuantities(
                product.getVariants().stream()
                        .map(variant -> variant.getQuantityInStock())
                        .collect(Collectors.toList())
        );
        productDetailsDTO.setImageUrl(
                product.getImages().stream()
                        .map(image -> image.getImageUrl())
                        .collect(Collectors.toList())
        );
        model.addAttribute("productDetailsDTO", productDetailsDTO);
        model.addAttribute("product", product);
        return "admin/productDetails";
    }
}
