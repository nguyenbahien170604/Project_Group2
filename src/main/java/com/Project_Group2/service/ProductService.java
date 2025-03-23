package com.Project_Group2.service;

import com.Project_Group2.dto.AddProductDTO;
import com.Project_Group2.dto.ClassifyDTO;
import com.Project_Group2.dto.ProductDTO;
import com.Project_Group2.dto.ProductVariantDTO;
import com.Project_Group2.entity.*;
import com.Project_Group2.repository.CategoryRepository;
import com.Project_Group2.repository.ProductImageRepository;
import com.Project_Group2.repository.ProductRepository;
import com.Project_Group2.repository.ProductVariantRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private HttpSession session;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductDTO getProductById(int productId) {
        Product product = productRepository.findByProductIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToProductDTO(product);
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAllByIsDeletedFalse();
        return products.stream().map(this::mapToProductDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> getSortedProducts(String sortBy) {
        List<Product> products = productRepository.findAll();

        // Thực hiện sắp xếp dựa trên giá trị `sortBy`
        return products.stream()
                .sorted(getComparator(sortBy))
                .map(this::mapToProductDTO)
                .collect(Collectors.toList());
    }

    private Comparator<Product> getComparator(String sortBy) {
        switch (sortBy) {
            case "name_asc":
                return Comparator.comparing(Product::getProductName);
            case "name_desc":
                return Comparator.comparing(Product::getProductName).reversed();
            case "price_asc":
                return Comparator.comparing(Product ->
                        Product.getPrice().multiply(BigDecimal.valueOf(100 - Product.getDiscount())).divide(BigDecimal.valueOf(100))
                );
            case "price_desc":
                return Comparator.comparing(Product::getPrice).reversed();
            default:
                return Comparator.comparing(Product::getProductName);
        }
    }

    public List<ProductDTO> getProductsByCategory(Integer categoryId) {
        List<Product> products = productRepository.findByCategory_CategoryId(categoryId);
        return products.stream()
                .map(this::mapToProductDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByBrand(Integer brandId) {
        List<Product> products = productRepository.findByBrand_BrandId(brandId);
        return products.stream()
                .map(this::mapToProductDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> filterProducts(List<String> sizes, List<String> colors, List<Integer> categories, List<Integer> brands) {
        if ((sizes == null || sizes.isEmpty()) &&
                (colors == null || colors.isEmpty()) &&
                (categories == null || categories.isEmpty()) &&
                (brands == null || brands.isEmpty())) {
            return productRepository.findAll()
                    .stream()
                    .map(this::mapToProductDTO)
                    .collect(Collectors.toList());
        }

        // Chuyển danh sách rỗng thành NULL để tránh lỗi SQL Server
        sizes = (sizes == null || sizes.isEmpty()) ? null : sizes;
        colors = (colors == null || colors.isEmpty()) ? null : colors;
        categories = (categories == null || categories.isEmpty()) ? null : categories;
        brands = (brands == null || brands.isEmpty()) ? null : brands;

        List<Product> products = productRepository.findByFilters(sizes, colors, categories, brands);
        return products.stream().map(this::mapToProductDTO).collect(Collectors.toList());
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setShortDescription(productDTO.getProductShortDescription());
        product.setDescription(productDTO.getProductDescription());
        product.setPrice(productDTO.getProductPrice());
        product.setDiscount(productDTO.getProductDiscount());
        product.setDeleted(false);

        Product savedProduct = productRepository.save(product);
        return mapToProductDTO(savedProduct);
    }


    public ProductDTO updateProduct(int productId, ProductDTO productDTO) {
        Product product = productRepository.findByProductIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setProductName(productDTO.getProductName());
        product.setShortDescription(productDTO.getProductShortDescription());
        product.setDescription(productDTO.getProductDescription());
        product.setPrice(productDTO.getProductPrice());
        product.setDiscount(productDTO.getProductDiscount());

        Product updatedProduct = productRepository.save(product);
        return mapToProductDTO(updatedProduct);
    }


    public void deleteProduct(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setDeleted(true);  // Xóa mềm (Soft delete)
        productRepository.save(product);
    }

    public Map<String, List<ProductDTO>> getProductsByCategory() {
        // Lấy danh sách tất cả danh mục (giả sử có CategoryService)
        List<Category> categories = categoryRepository.findAll();

        // Tạo Map để lưu danh sách sản phẩm theo từng danh mục
        Map<String, List<ProductDTO>> productsByCategory = new HashMap<>();

        for (Category category : categories) {
            // Lấy danh sách sản phẩm theo từng danh mục
            List<Product> products = productRepository.findAllByCategory(category);

            // Chuyển danh sách sản phẩm sang ProductDTO
            List<ProductDTO> productDTOs = products.stream()
                    .map(this::mapToProductDTO)
                    .collect(Collectors.toList());

            // Đưa vào Map (Key: Tên danh mục, Value: Danh sách sản phẩm)
            productsByCategory.put(category.getCategoryName(), productDTOs);
        }

        return productsByCategory;
    }

    public List<ProductDTO> getProductsByCategoryId(int categoryId) {
        List<Product> products = productRepository.findAllByCategory_CategoryIdAndIsDeletedFalse(categoryId);

        return products.stream()
                .map(this::mapToProductDTO) // Chuyển Product -> ProductDTO
                .collect(Collectors.toList());
    }

    public List<ProductDTO> get6LatestProducts() {
        List<Product> latestProducts = productRepository.findTop6ByOrderByCreatedAtDesc();
        return latestProducts.stream()
                .map(this::mapToProductDTO) // Chuyển Product -> ProductDTO
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getSameCategoryProducts(int categoryId, int productId) {
        // Tìm category
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Tìm sản phẩm cùng danh mục, nhưng loại trừ productId hiện tại
        List<Product> sameCategoryProducts = productRepository.findByCategoryCategoryIdAndProductIdNot(category.getCategoryId(), productId);

        // Chuyển đổi từ Product -> ProductDTO
        return sameCategoryProducts.stream()
                .map(this::mapToProductDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> searchProducts(String keyword) {
        List<Product> products = productRepository.findByProductNameContainingAndIsDeletedFalse(keyword);

        return products.stream()
                .map(this::mapToProductDTO)
                .collect(Collectors.toList());
    }

    private ProductDTO mapToProductDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductShortDescription(product.getShortDescription());
        dto.setProductDescription(product.getDescription());
        dto.setProductPrice(product.getPrice());
        dto.setProductDiscount(product.getDiscount() != null ? product.getDiscount() : 0);

        // Lấy danh sách hình ảnh
        List<String> imageUrls = product.getImages().stream()
                .map(ProductImage::getImageUrl)
                .collect(Collectors.toList());
        dto.setProductImage(imageUrls);

        // Lấy danh sách biến thể sản phẩm
        List<ProductVariantDTO> variantDTOs = product.getVariants().stream()
                .map(variant -> new ProductVariantDTO(variant.getSize(), variant.getColor(), variant.getQuantityInStock()))
                .collect(Collectors.toList());
        dto.setProductVariants(variantDTOs);

        return dto;
    }


    public Page<Product> getAllProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }

    public Page<Product> searchProducts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll(pageable);
        }

        return productRepository.searchProducts(keyword, pageable);
    }

    public Product findProductById(int productId) {
        return productRepository.findByProductId(productId);
    }

    public Product addProduct(AddProductDTO addProductDTO,
                              RedirectAttributes redirectAttributes) {
        boolean hasError = false;

        if (productRepository.findByProductName(addProductDTO.getName()) != null) {
            redirectAttributes.addFlashAttribute("error", "Product name already exists!");
            hasError = true;
        }

        if (hasError) {
            return null;
        }

        Product product = new Product();
        product.setProductName(addProductDTO.getName());
        product.setDescription(addProductDTO.getDescription());
        product.setShortDescription(addProductDTO.getShortDescription());
        product.setPrice(addProductDTO.getPrice());
        product.setDiscount(addProductDTO.getDiscount());
        product.setDeleted(false);

        User user = (User) session.getAttribute("loggedInUser");
        product.setUserId(user);

        Brand brand = brandService.findById(addProductDTO.getBrandId());
        product.setBrand(brand);

        Category category = categoryService.findById(addProductDTO.getCategoryId());
        product.setCategory(category);

        product = productRepository.save(product);

        List<ProductImage> productImages = new ArrayList<>();
        List<ProductVariant> productVariants = new ArrayList<>();

        if (addProductDTO.getClassifies() != null) {
            for (ClassifyDTO classify : addProductDTO.getClassifies()) {

                List<String> imageUrls = saveImages(classify.getImages());
                for (String imageUrl : imageUrls) {
                    ProductImage productImage = new ProductImage();
                    productImage.setProduct(product);
                    productImage.setImageUrl(imageUrl);
                    productImages.add(productImage);
                }

                for (int i = 0; i < classify.getSize().size(); i++) {
                    ProductVariant productVariant = new ProductVariant();
                    productVariant.setProduct(product);
                    productVariant.setColor(classify.getColor().get(i));
                    productVariant.setSize(classify.getSize().get(i));
                    productVariant.setQuantityInStock(classify.getQuantity().get(i));
                    productVariant.setDeleted(false);
                    productVariants.add(productVariant);
                }
            }
        }

        productImageRepository.saveAll(productImages);
        productVariantRepository.saveAll(productVariants);

        return productRepository.save(product);
    }

    private static final String UPLOAD_DIR = "src/main/resources/static/assets/img/product/";

    private List<String> saveImages(List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : images) {
            try {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR, fileName);

                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());

                imageUrls.add("assets/img/product/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageUrls;
    }


}
