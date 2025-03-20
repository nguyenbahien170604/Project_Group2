package com.Project_Group2.service;

import com.Project_Group2.entity.*;
import com.Project_Group2.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
           User user = (User) session.getAttribute("loggedInUser");
           if (user == null) {
               throw new RuntimeException("User not logged in");
           }

           Optional<Product> productOpt = productRepository.findById(productId);
           if (!productOpt.isPresent()) {
               throw new RuntimeException("Product not found");
           }
           Product product = productOpt.get();
           System.out.println(" Product found: " + product.getProductName());

           Optional<ProductVariant> variantOpt = productVariantRepository.findByProductAndSizeAndColor(product, size, color);
           if (!variantOpt.isPresent()) {
               throw new RuntimeException("Product variant not found");
           }
           ProductVariant variant = variantOpt.get();
           System.out.println(" Variant found: Size - " + variant.getSize() + ", Color - " + variant.getColor());

           if (variant.getQuantityInStock() < quantity) {
               throw new RuntimeException("Not enough stock available");
           }
           System.out.println(" Stock available: " + variant.getQuantityInStock());

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
           Optional<CartDetails> cartDetailOpt = cartDetailsRepository.findByCartAndProductVariant(cart, variant);
           CartDetails cartDetail;
           if (cartDetailOpt.isPresent()) {
               cartDetail = cartDetailOpt.get();
               cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
               System.out.println(" Updated quantity: " + cartDetail.getQuantity());
           } else {
               cartDetail = new CartDetails();
               cartDetail.setCart(cart);
               cartDetail.setProductVariant(variant);
               cartDetail.setQuantity(quantity);
               cartDetail.setPriceProduct(cart.getTotalAmount());
               System.out.println(" New CartDetail added: " + product.getProductName());
           }
           cartDetailsRepository.save(cartDetail);
           System.out.println(" Cart updated successfully!");
           int cartSize = cartDetailsRepository.countDistinctProductByCart(cart.getCartId());
           session.setAttribute("cartSize", cartSize);
           return cartDetailsRepository.countDistinctProductByCart(cart.getCartId());
       }catch (Exception e) {
           System.out.println(e.getMessage());
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
                BigDecimal itemTotal = cartItem.getProductVariant().getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                totalPrice = totalPrice.add(itemTotal);
            }

            OrderStatuses status = orderStatusRepository.findById(1)
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
                orderDetail.setQuantity(cartItem.getQuantity());
                orderDetail.setPriceProduct(cartItem.getProductVariant().getProduct().getPrice());
                orderDetailsRepository.save(orderDetail);
                System.out.println(" Added to OrderDetails: " + cartItem.getProductVariant().getProduct().getProductName());

                ProductVariant variant = cartItem.getProductVariant();
                if (variant.getQuantityInStock() < cartItem.getQuantity()) {
                    throw new RuntimeException("⚠️ Not enough stock for product: " + variant.getProduct().getProductName());
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
