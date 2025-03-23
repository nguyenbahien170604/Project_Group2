package com.Project_Group2.controller.userController;

import com.Project_Group2.entity.*;
import com.Project_Group2.repository.*;
import com.Project_Group2.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class PaymentController {

    private final VNPayService vnPayService;
    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final UserRepository userRepository;
    private final CartDetailsRepository cartDetailsRepository;
    private final CartRepository cartRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    public PaymentController(VNPayService vnPayService, OrderRepository orderRepository, OrderStatusRepository orderStatusRepository, UserRepository userRepository, CartDetailsRepository cartDetailsRepository, CartRepository cartRepository, OrderDetailsRepository orderDetailsRepository) {
        this.vnPayService = vnPayService;
        this.orderRepository = orderRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.userRepository = userRepository;
        this.cartDetailsRepository = cartDetailsRepository;
        this.cartRepository = cartRepository;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    @GetMapping("/checkout/vnpay")
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
            Orders order = new Orders();
            order.setUser(user);
            order.setReceivedName(receivedName);
            order.setReceivedPhone(receivedPhone);
            order.setReceivedAddress(receivedAddress);
            order.setPaid(false);
            order.setDeleted(false);
            order.setTotalPrice(total); // Tính tổng tiền từ giỏ hàng
            order.setStatus(orderStatusRepository.findOrderStatusesByStatusId(4));
            order.setCreatedAt(new Date());
            orderRepository.save(order);
            // Gọi VNPayService để tạo URL thanh toán
            String paymentUrl = vnPayService.createPaymentUrl(orderRepository.findTop1ByOrderByOrderIdDesc().getOrderId()+1, order.getTotalPrice(), request);
            return "redirect:" + paymentUrl; // Chuyển hướng đến VNPay
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("payment/vnpay-return")
    public String vnpayReturn(HttpSession session,@RequestParam Map<String, String> params, Model model) {
        String orderId = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode"); // Mã phản hồi từ VNPay

        Orders order = orderRepository.findById(orderRepository.findTop1ByOrderByOrderIdDesc().getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if ("00".equals(responseCode)) { // 00 là mã giao dịch thành công
            order.setStatus(orderStatusRepository.findOrderStatusesByStatusId(1));
            order.setPaid(true);
            List<CartDetails> listCartDetails = (List<CartDetails>)session.getAttribute("cartDetailVNPay");
            Carts carts = (Carts)session.getAttribute("cartVNPay");
            cartDetailsRepository.deleteAll(listCartDetails);
            cartRepository.delete(carts);
            session.removeAttribute("cartSize");
            model.addAttribute("message", "Thanh toán thành công!");
        } else {
            order.setStatus(orderStatusRepository.findOrderStatusesByStatusId(4));
            List<OrderDetails> listOrderDetails = orderDetailsRepository.findByOrder(order);
            for (OrderDetails orderDetails : listOrderDetails) {
                orderDetails.getProductVariant().setQuantityInStock(orderDetails.getProductVariant().getQuantityInStock() + orderDetails.getQuantity());
            }
            model.addAttribute("message", "Thanh toán thất bại, vui lòng thử lại.");
        }
        orderRepository.save(order);
        model.addAttribute("order"
       , order);
        return "redirect:/"; // Trang hiển thị kết quả thanh toán
    }
}

