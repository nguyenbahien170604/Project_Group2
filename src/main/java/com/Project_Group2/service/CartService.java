package com.Project_Group2.service;

import com.Project_Group2.entity.*;
import com.Project_Group2.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;

    private final CartDetailsRepository cartDetailsRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final ProductVariantRepository productVariantRepository;
    private final HttpSession httpSession;

    public CartService(CartRepository cartRepository, CartDetailsRepository cartDetailsRepository, UserRepository userRepository, ProductRepository productRepository, ProductVariantRepository productVariantRepository, HttpSession httpSession) {
        this.cartRepository = cartRepository;
        this.cartDetailsRepository = cartDetailsRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productVariantRepository = productVariantRepository;
        this.httpSession = httpSession;
    }

    public void addToCart(HttpSession session, int productId, String size, String color, int quantity) {
       try {
           // 🔴 Bước 1: Lấy User từ session
           User user = (User) session.getAttribute("loggedInUser");
           if (user == null) {
               throw new RuntimeException("User not logged in");
           }
           System.out.println("✅ User found: " + user.getId());

           // 🔴 Bước 2: Kiểm tra sản phẩm có tồn tại không
           Optional<Product> productOpt = productRepository.findById(productId);
           if (!productOpt.isPresent()) {
               throw new RuntimeException("Product not found");
           }
           Product product = productOpt.get();
           System.out.println("✅ Product found: " + product.getProductName());

           // 🔴 Bước 3: Tìm ProductVariant theo size và color
           Optional<ProductVariant> variantOpt = productVariantRepository.findByProductAndSizeAndColor(product, size, color);
           if (!variantOpt.isPresent()) {
               throw new RuntimeException("Product variant not found");
           }
           ProductVariant variant = variantOpt.get();
           System.out.println("✅ Variant found: Size - " + variant.getSize() + ", Color - " + variant.getColor());

           // 🔴 Bước 4: Kiểm tra số lượng tồn kho
           if (variant.getQuantityInStock() < quantity) {
               throw new RuntimeException("Not enough stock available");
           }
           System.out.println("✅ Stock available: " + variant.getQuantityInStock());

           // 🔴 Bước 5: Kiểm tra giỏ hàng của user
           Optional<Carts> cartOpt = cartRepository.findByUserId(user.getId());
           Carts cart;
           if (cartOpt.isPresent()) {
               cart = cartOpt.get();
           } else {
               cart = new Carts();
               cart.setUser(user);
               cart.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
               cart.setDeleted(false);
               cartRepository.save(cart);
           }
           // 🔴 Bước 6: Kiểm tra sản phẩm đã có trong giỏ hàng chưa
           Optional<CartDetails> cartDetailOpt = cartDetailsRepository.findByCartAndProductVariant(cart, variant);
           CartDetails cartDetail;
           if (cartDetailOpt.isPresent()) {
               cartDetail = cartDetailOpt.get();
               cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
               System.out.println("✅ Updated quantity: " + cartDetail.getQuantity());
           } else {
               cartDetail = new CartDetails();
               cartDetail.setCart(cart);
               cartDetail.setProductVariant(variant);
               cartDetail.setQuantity(quantity);
               cartDetail.setPriceProduct(cart.getTotalAmount());
               System.out.println("✅ New CartDetail added: " + product.getProductName());
           }

           cartDetailsRepository.save(cartDetail); // 🟢 Đảm bảo lưu vào DB
           System.out.println("✅ Cart updated successfully!");
       }catch (Exception e) {
           System.out.println(e.getMessage());
       }
    }


    public List<CartDetails> getCartItems() {
        // Lấy user từ session
        try {
            HttpSession session = httpSession;
            User user = (User) session.getAttribute("loggedInUser");
            if (user == null) {
                throw new RuntimeException("User not logged in");
            }
            // Lấy giỏ hàng của user
            Optional<Carts> cartOpt = cartRepository.findByUserId(user.getId());
            if (!cartOpt.isPresent()) {
                return List.of(); // Trả về danh sách rỗng nếu chưa có giỏ hàng
            }
            // Lấy danh sách sản phẩm trong giỏ hàng
            return cartDetailsRepository.findByCart(cartOpt.get());
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
