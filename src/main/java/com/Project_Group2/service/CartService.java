package com.Project_Group2.service;

import com.Project_Group2.entity.*;
import com.Project_Group2.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;

    private final CartDetailsRepository cartDetailsRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final ProductVariantRepository productVariantRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;


    private final HttpSession httpSession;

    public CartService(CartRepository cartRepository, CartDetailsRepository cartDetailsRepository, UserRepository userRepository, ProductRepository productRepository, ProductVariantRepository productVariantRepository, HttpSession httpSession) {
        this.cartRepository = cartRepository;
        this.cartDetailsRepository = cartDetailsRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productVariantRepository = productVariantRepository;
        this.httpSession = httpSession;
    }

    public int addToCart(HttpSession session, int productId, String size, String color, int quantity) {
        try {
            // 1. Kiểm tra người dùng đăng nhập
            User user = (User) session.getAttribute("loggedInUser");
            if (user == null) {
                throw new IllegalArgumentException("User not logged in");
            }

            // 2. Lấy sản phẩm từ DB
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            System.out.println("Product found: " + product.getProductName());

            // 3. Tìm biến thể của sản phẩm theo màu sắc và kích thước
            ProductVariant variant = productVariantRepository.findByProductAndSizeAndColor(product, size, color)
                    .orElseThrow(() -> new IllegalArgumentException("Product variant not found"));
            System.out.println("Variant found: Size - " + variant.getSize() + ", Color - " + variant.getColor());

            // 4. Kiểm tra số lượng tồn kho
            if (variant.getQuantityInStock() < quantity) {
                throw new IllegalArgumentException("Not enough stock available");
            }
            System.out.println("Stock available: " + variant.getQuantityInStock());

            // 5. Tính giá giảm giá
            BigDecimal price = product.getPrice();
            BigDecimal discount = BigDecimal.valueOf(100 - product.getDiscount());
            BigDecimal discountedPrice = price.multiply(discount).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

            // 6. Lấy hoặc tạo giỏ hàng của user
            Carts cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> {
                Carts newCart = new Carts();
                newCart.setUser(user);
                newCart.setTotalAmount(BigDecimal.ZERO);
                newCart.setDeleted(false);
                return cartRepository.save(newCart);
            });

            // 7. Kiểm tra sản phẩm đã tồn tại trong giỏ hàng hay chưa
            CartDetails cartDetail = cartDetailsRepository.findByCartAndProductVariant(cart, variant).orElseGet(() -> {
                CartDetails newCartDetail = new CartDetails();
                newCartDetail.setCart(cart);
                newCartDetail.setProductVariant(variant);
                newCartDetail.setQuantity(0);
                newCartDetail.setPriceProduct(discountedPrice);
                return newCartDetail;
            });

            // 8. Cập nhật số lượng sản phẩm trong giỏ hàng
            cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
            cartDetail.setPriceProduct(discountedPrice); // Đảm bảo giá luôn cập nhật đúng
            cartDetailsRepository.save(cartDetail);

            // 9. Cập nhật tổng tiền của giỏ hàng
            BigDecimal totalAmount = cartDetailsRepository.findTotalAmountByCart(cart.getCartId());
            cart.setTotalAmount(totalAmount);
            cartRepository.save(cart);

            // 10. Cập nhật số lượng sản phẩm trong giỏ hàng lên session
            int cartSize = cartDetailsRepository.countDistinctProductByCart(cart.getCartId());
            session.setAttribute("cartSize", cartSize);

            System.out.println("Cart updated successfully!");
            return cartSize;
        } catch (Exception e) {
            System.out.println("Error in addToCart: " + e.getMessage());
        }
        return 0;
    }

    public int removeCartItem(int cartDetailId) {
        Optional<CartDetails> cartDetailOpt = cartDetailsRepository.findById(cartDetailId);
        if (cartDetailOpt.isPresent()) {
            CartDetails cartDetail = cartDetailOpt.get();
            Carts cart = cartDetail.getCart();

            // Xóa sản phẩm khỏi giỏ hàng
            cartDetailsRepository.delete(cartDetail);

            // Cập nhật số lượng sản phẩm trong giỏ hàng
            return cartDetailsRepository.countDistinctProductByCart(cart.getCartId());
        }
        return -1; // Nếu không tìm thấy cartDetail, trả về -1
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
    @Transactional
    public boolean checkout(HttpSession session, String receivedName, String receivedPhone, String receivedAddress) {
        try {
            User user = (User) session.getAttribute("loggedInUser");
            if (user == null) {
                System.out.println("️ Checkout failed: User not logged in");
                return false;
            }

            Optional<Carts> cartOpt = cartRepository.findByUserId(user.getId());
            if (!cartOpt.isPresent()) {
                System.out.println(" Checkout failed: Cart not found for user " + user.getId());
                return false;
            }
            Carts cart = cartOpt.get();

            List<CartDetails> cartItems = cartDetailsRepository.findByCart(cart);
            if (cartItems.isEmpty()) {
                System.out.println(" Checkout failed: Cart is empty");
                return false;
            }

            BigDecimal totalPrice = BigDecimal.ZERO;
            for (CartDetails cartItem : cartItems) {
                Product product = cartItem.getProductVariant().getProduct();
                BigDecimal price = product.getPrice();
                BigDecimal discount = BigDecimal.valueOf(product.getDiscount());

                // Tính giá đã giảm
                BigDecimal discountedPrice = price.multiply(BigDecimal.valueOf(100).subtract(discount))
                        .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

                // Tổng tiền của sản phẩm
                BigDecimal itemTotal = discountedPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

                totalPrice = totalPrice.add(itemTotal);
            }

            OrderStatuses status = orderStatusRepository.findById(2)
                    .orElseThrow(() -> new RuntimeException("⚠️ Order status not found"));

            Orders newOrder = new Orders();
            newOrder.setUser(user);
            newOrder.setReceivedName(receivedName);
            newOrder.setReceivedAddress(receivedAddress);
            newOrder.setReceivedPhone(receivedPhone);
            newOrder.setTotalPrice(totalPrice);
            newOrder.setStatus(status);
            newOrder.setCreatedAt(new Date());
            newOrder.setDeleted(false);
            orderRepository.save(newOrder);
            System.out.println(" Order created successfully: " + newOrder.getOrderId());

            for (CartDetails cartItem : cartItems) {
                OrderDetails orderDetail = new OrderDetails();
                orderDetail.setOrder(newOrder);
                orderDetail.setProduct(cartItem.getProductVariant().getProduct());
                orderDetail.setProductVariant(cartItem.getProductVariant());
                orderDetail.setProductImage(cartItem.getProductVariant().getProduct().getImages().get(0));
                orderDetail.setQuantity(cartItem.getQuantity());
                orderDetail.setPriceProduct(cartItem.getProductVariant().getProduct().getPrice());
                orderDetailsRepository.save(orderDetail);
                System.out.println(" Added to OrderDetails: " + cartItem.getProductVariant().getProduct().getProductName());

                ProductVariant variant = cartItem.getProductVariant();
                if (variant.getQuantityInStock() < cartItem.getQuantity()) {
                    throw new RuntimeException(" Not enough stock for product: " + variant.getProduct().getProductName());
                }
                variant.setQuantityInStock(variant.getQuantityInStock() - cartItem.getQuantity());
                productVariantRepository.save(variant);
                System.out.println(" Stock updated for " + variant.getProduct().getProductName());
            }

            cartDetailsRepository.deleteAll(cartItems);
            cartRepository.delete(cart);
            System.out.println(" Cart cleared after checkout");
            session.removeAttribute("cartSize");
            return true;
        } catch (NoSuchElementException e) {
            System.out.println(" Checkout failed: Missing required data - " + e.getMessage());
        } catch (DataAccessException e) {
            System.out.println(" Database error during checkout: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Unexpected error during checkout: " + e.getMessage());
        }
        return false;
    }

}
