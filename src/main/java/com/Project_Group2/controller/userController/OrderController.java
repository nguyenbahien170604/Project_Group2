package com.Project_Group2.controller.userController;

import com.Project_Group2.entity.OrderDetails;
import com.Project_Group2.entity.OrderStatuses;
import com.Project_Group2.entity.Orders;
import com.Project_Group2.entity.User;
import com.Project_Group2.repository.OrderRepository;
import com.Project_Group2.repository.OrderStatusRepository;
import com.Project_Group2.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Autowired
    private OrderRepository orderRepository;

@GetMapping("/orders")
public String getOrders(
        @RequestParam(value = "status", required = false) Integer statusId,
        @RequestParam(value = "isPaid", required = false) Boolean isPaid,
        @RequestParam(value = "sortBy", required = false) String sortBy,
        HttpSession session,
        Model model) {

    // Lấy danh sách trạng thái đơn hàng
    List<OrderStatuses> statuses = orderStatusRepository.findAll();
    model.addAttribute("statuses", statuses);

    // Lấy user đang đăng nhập từ session
    User user = (User) session.getAttribute("loggedInUser");
    if (user == null) {
        return "redirect:/login";
    }

    // Lấy danh sách đơn hàng theo bộ lọc
    List<Orders> listOrder;
    if (statusId != null && isPaid != null) {
        listOrder = orderRepository.findByUser_idAndStatus_StatusIdAndIsPaid(user.getId(), statusId, isPaid);
    } else if (statusId != null) {
        listOrder = orderRepository.findByUser_idAndStatus_StatusId(user.getId(), statusId);
    } else if (isPaid != null) {
        listOrder = orderRepository.findByUser_idAndIsPaid(user.getId(), isPaid);
    } else {
        listOrder = orderRepository.findByUser_id(user.getId());
    }

    // Sắp xếp theo newest hoặc oldest
    if ("oldest".equals(sortBy)) {
        listOrder.sort(Comparator.comparing(Orders::getCreatedAt)); // Cũ nhất
    } else {
        listOrder.sort(Comparator.comparing(Orders::getCreatedAt).reversed()); // Mới nhất
    }

    // Đưa dữ liệu vào model
    model.addAttribute("listOrder", listOrder);
    model.addAttribute("selectedStatus", statusId);
    model.addAttribute("selectedPaymentStatus", isPaid);
    model.addAttribute("selectedSortBy", sortBy);

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
