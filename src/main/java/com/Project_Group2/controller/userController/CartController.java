package com.Project_Group2.controller.userController;

import com.Project_Group2.entity.*;
import com.Project_Group2.repository.*;
import com.Project_Group2.service.CartService;
import com.Project_Group2.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Autowired
    private VNPayService vnpayService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartDetailsRepository cartDetailsRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository;


    @PostMapping("/cart/add")
    public String addToCart(HttpSession session,
                            @RequestParam("productId") int productId,
                            @RequestParam("size") String size,
                            @RequestParam("color") String color,
                            @RequestParam("quantity") int quantity) {
        try {
            cartService.addToCart(session, productId, size, color, quantity);
            return "redirect:/product-details?id="+productId;
        } catch (Exception e) {
            session.setAttribute("errorMessage", e.getMessage());
            return "redirect:/product-details?id="+productId;
        }
    }

    @GetMapping("/cart/remove")
    public String removeCartItem(@RequestParam("id") int cartDetailId, HttpSession session) {
        int cartSize = cartService.removeCartItem(cartDetailId);
        if (cartSize != -1) {
            session.setAttribute("cartSize", cartSize);
        }
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session,
                           @RequestParam("receivedName") String receivedName,
                           @RequestParam("receivedPhone") String receivedPhone,
                           @RequestParam("receivedAddress") String receivedAddress,
                           Model model) {

        boolean success = cartService.checkout(session, receivedName, receivedPhone, receivedAddress);

        if (!success) {
            model.addAttribute("error", "Checkout failed! Please check your cart.");
            return "redirect:/cart";
        }

        return "user/order-success";
    }

    @PostMapping("/checkout/vnpay")
    public String checkout2(HttpSession session,
                           @RequestParam String receivedName,
                           @RequestParam String receivedPhone,
                           @RequestParam String receivedAddress,
                            @RequestParam String amount,
                           HttpServletRequest request) throws Exception {
        try {
            System.out.println(orderRepository.findTop1ByOrderByOrderIdDesc().getOrderId()+1);
            User user = (User) session.getAttribute("loggedInUser");
            //BigDecimal totalVnPay = (BigDecimal) session.getAttribute("totalVnPay");
            BigDecimal total = new BigDecimal(amount);
            if (user == null) {
                return "redirect:/login";
            }
            Optional<Carts> cartOpt = cartRepository.findByUserId(user.getId());
            if (!cartOpt.isPresent()) {
                System.out.println(" Checkout failed: Cart not found for user " + user.getId());
                return null;
            }
            Carts cart = cartOpt.get();

            List<CartDetails> cartItems = cartDetailsRepository.findByCart(cart);
            if (cartItems.isEmpty()) {
                System.out.println(" Checkout failed: Cart is empty");
                return null;
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

            Orders order = new Orders();
            order.setUser(user);
            order.setReceivedName(receivedName);
            order.setReceivedPhone(receivedPhone);
            order.setReceivedAddress(receivedAddress);
            order.setDeleted(false);
            order.setTotalPrice(total); // Tính tổng tiền từ giỏ hàng
            order.setStatus(orderStatusRepository.findOrderStatusesByStatusId(1));
            order.setCreatedAt(new Date());
            orderRepository.save(order);

            for (CartDetails cartItem : cartItems) {
                OrderDetails orderDetail = new OrderDetails();
                orderDetail.setOrder(order);
                orderDetail.setProduct(cartItem.getProductVariant().getProduct());
                orderDetail.setProductVariant(cartItem.getProductVariant());
                orderDetail.setProductImage(cartItem.getProductVariant().getProduct().getImages().get(0));
                orderDetail.setQuantity(cartItem.getQuantity());
                orderDetail.setPriceProduct(total);
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
            session.setAttribute("cartVNPay",cart);
            session.setAttribute("cartDetailVNPay",cartItems);
            String paymentUrl = vnpayService.createPaymentUrl(orderRepository.findTop1ByOrderByOrderIdDesc().getOrderId()+1, order.getTotalPrice(), request);
            return "redirect:" + paymentUrl; // Chuyển hướng đến VNPay
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}

