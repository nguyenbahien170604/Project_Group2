package com.Project_Group2.repository;

import com.Project_Group2.entity.Product;
import com.Project_Group2.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
    Optional<ProductVariant> findByProductAndSizeAndColor(Product product, String size, String color);
}
