package com.Project_Group2.repository;

import com.Project_Group2.entity.CartDetails;
import com.Project_Group2.entity.Carts;
import com.Project_Group2.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartDetailsRepository extends JpaRepository<CartDetails, Integer> {
    Optional<CartDetails> findByCartAndProductVariant(Carts cart, ProductVariant productVariant);
    List<CartDetails> findByCart(Carts cart);
    @Query("SELECT COUNT(DISTINCT cd.productVariant.variantId) FROM CartDetails cd WHERE cd.cart.cartId = :cartId")
    int countDistinctProductByCart(@Param("cartId") int cartId);
}
