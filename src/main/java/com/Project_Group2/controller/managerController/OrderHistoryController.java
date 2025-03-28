package com.Project_Group2.controller.managerController;

import com.Project_Group2.entity.OrderDetails;
import com.Project_Group2.entity.OrderStatuses;
import com.Project_Group2.entity.Orders;
import com.Project_Group2.service.OrderService;
import com.Project_Group2.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderHistoryController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/test")
    private String test() {
        return "manager/datatables";
    }

    @GetMapping("/history")
    public String viewOrderHistory(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "5") int size,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) String phone,
                                   Model model, HttpSession session) {

        Page<Orders> orderHistoryPage;
        if (name != null && !name.isEmpty() || phone != null && !phone.isEmpty()) {
            orderHistoryPage = orderService.getUserOrderHistory(name, phone, page, size);
            model.addAttribute("name", name);
            model.addAttribute("phone", phone);
        } else {
            orderHistoryPage = orderService.getAllBlogsWithPagination(page, size);
        }
        List<Orders> orderList = orderHistoryPage.getContent();

        List<OrderStatuses> orderStatuses = orderService.getAllOrderStatuses();
        Map<Integer, List<OrderDetails>> orderDetailsMap = orderService.getOrderDetailsForOrders(orderList);

        model.addAttribute("orderHistory", orderHistoryPage);
        model.addAttribute("orderStatuses", orderStatuses);
        model.addAttribute("orderDetailsMap", orderDetailsMap);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderHistoryPage.getTotalPages());
        return "manager/order-history";
    }

    @GetMapping("/details/{orderId}")
    public String viewOrderDetails(@PathVariable("orderId") int orderId, Model model, HttpSession session) {

        Orders order = orderService.getOrderById(orderId);
        List<OrderDetails> orderDetails = orderService.getOrderDetailsByOrder(order);

        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);

        return "manager/order-details";
    }

    @PostMapping("/update-status")
    public String updateOrderStatus(@RequestParam("orderId") int orderId,
                                    @RequestParam("statusId") int statusId,
                                    RedirectAttributes redirectAttributes) {
        boolean updated = orderService.updateOrderStatus(orderId, statusId);

        if (updated) {
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Cập nhật trạng thái thất bại!");
        }

        return "redirect:/orders/history";
    }
}
