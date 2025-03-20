package com.Project_Group2.service;

import com.Project_Group2.entity.CartDetails;
import com.Project_Group2.entity.Carts;
import com.Project_Group2.entity.Product;
import com.Project_Group2.entity.ProductVariant;
import com.Project_Group2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;

    private final CartDetailsRepository cartDetailsRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final ProductVariantRepository productVariantRepository;

    public CartService(CartRepository cartRepository, CartDetailsRepository cartDetailsRepository, UserRepository userRepository, ProductRepository productRepository, ProductVariantRepository productVariantRepository) {
        this.cartRepository = cartRepository;
        this.cartDetailsRepository = cartDetailsRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productVariantRepository = productVariantRepository;
    }

    public void addToCart(int userId, int productId, String size, String color, int quantity) {
        // Lấy sản phẩm theo ID
        Optional<Product> productOpt = productRepository.findById(productId);
        if (!productOpt.isPresent()) {
            throw new RuntimeException("Product not found");
        }
        Product product = productOpt.get();

        // Kiểm tra biến thể sản phẩm theo size và color
        Optional<ProductVariant> variantOpt = productVariantRepository.findByProductAndSizeAndColor(product, size, color);
        if (!variantOpt.isPresent()) {
            throw new RuntimeException("Product variant not found");
        }
        ProductVariant variant = variantOpt.get();

        if (variant.getQuantityInStock() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        // Lấy giỏ hàng của user (giả định user đã có giỏ hàng)
        Carts cart = cartRepository.findByUserId(1).orElseGet(() -> {
            Carts newCart = new Carts();
            newCart.setUser(userRepository.findById(1).get());
            cartRepository.save(newCart);
            return newCart;
        });

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        Optional<CartDetails> cartDetailOpt = cartDetailsRepository.findByCartAndProductVariant(cart, variant);
        CartDetails cartDetail;
        if (cartDetailOpt.isPresent()) {
            cartDetail = cartDetailOpt.get();
            cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
        } else {
            cartDetail = new CartDetails();
            cartDetail.setCart(cart);
            cartDetail.setProductVariant(variant);
            cartDetail.setQuantity(quantity);
        }

        cartDetailsRepository.save(cartDetail);
    }
}
