package com.Project_Group2.service;

import com.Project_Group2.dto.ProductDTO;
import com.Project_Group2.dto.ProductVariantDTO;
import com.Project_Group2.entity.Product;
import com.Project_Group2.entity.ProductImage;
import com.Project_Group2.entity.ProductVariant;
import com.Project_Group2.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
}
