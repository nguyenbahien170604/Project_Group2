package com.Project_Group2.repository;

import com.Project_Group2.entity.CartDetails;
import com.Project_Group2.entity.Carts;
import com.Project_Group2.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartDetailsRepository extends JpaRepository<CartDetails, Integer> {
    Optional<CartDetails> findByCartAndProductVariant(Carts cart, ProductVariant productVariant);
}
