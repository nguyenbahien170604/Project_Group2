package com.Project_Group2.repository;

import com.Project_Group2.dto.ProductDTO;
import com.Project_Group2.entity.Category;
import com.Project_Group2.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByProductIdAndIsDeletedFalse(int productId);
    List<Product> findAllByIsDeletedFalse();
    List<Product> findAllByCategory(Category category);
    List<Product> findAllByCategory_CategoryIdAndIsDeletedFalse(int categoryId);
    List<Product> findTop6ByOrderByCreatedAtDesc();
    List<Product> findByCategoryCategoryIdAndProductIdNot(int categoryId, int productId);
    List<Product> findByCategory_CategoryId(Integer categoryId);
    List<Product> findByBrand_BrandId(Integer categoryId);
    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.variants v " +
            "WHERE (:sizes IS NULL OR v.size IN (:sizes)) " +
            "AND (:colors IS NULL OR v.color IN (:colors)) " +
            "AND (:categories IS NULL OR p.category.categoryId IN (:categories)) " +
            "AND (:brands IS NULL OR p.brand.brandId IN (:brands))")
    List<Product> findByFilters(
            @Param("sizes") List<String> sizes,
            @Param("colors") List<String> colors,
            @Param("categories") List<Integer> categories,
            @Param("brands") List<Integer> brands
    );
}
