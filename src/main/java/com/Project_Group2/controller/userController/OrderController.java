package com.Project_Group2.controller.userController;

import com.Project_Group2.entity.OrderDetails;
import com.Project_Group2.entity.Orders;
import com.Project_Group2.entity.User;
import com.Project_Group2.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String getOrderCustomer(Model model, HttpSession session) {
        User customer = (User)session.getAttribute("loggedInUser");
        List<Orders> listOrder = orderService.getCustomerOrderDetail(customer);
        model.addAttribute("listOrder", listOrder);
     return "user/orders";
    }

    @GetMapping("/orders/{orderId}")
    public String viewOrderDetails(@PathVariable("orderId") int orderId, Model model, HttpSession session) {
        Orders order = orderService.getOrderById(orderId);
        List<OrderDetails> orderDetails = orderService.getOrderDetailsByOrder(order);

        // Kiểm tra quyền truy cập
        if (!orderService.isUserAuthorized(session, order)) {
            return "redirect:/login";
        }

        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);

        return "user/order-details";
    }

    @GetMapping("/orders/cancel/{orderId}")
    public String cancelOrder(@PathVariable("orderId") int orderId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Nếu chưa đăng nhập, yêu cầu login
        }

        try {
            orderService.cancelOrder(orderId);
        } catch (RuntimeException e) {
            session.setAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/orders"; // Quay lại danh sách đơn hàng
    }
}
