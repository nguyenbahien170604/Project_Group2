package com.Project_Group2.repository;

import com.Project_Group2.dto.ProductDTO;
import com.Project_Group2.entity.Category;
import com.Project_Group2.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
