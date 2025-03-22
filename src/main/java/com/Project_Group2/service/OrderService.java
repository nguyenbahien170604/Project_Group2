package com.Project_Group2.service;

import com.Project_Group2.entity.OrderDetails;
import com.Project_Group2.entity.Orders;
import com.Project_Group2.entity.User;
import com.Project_Group2.repository.OrderDetailsRepository;
import com.Project_Group2.repository.OrderRepository;
import com.Project_Group2.repository.OrderStatusRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    public List<Orders> getCustomerOrderDetail(User user){
        List<Orders> orders = orderRepository.findByUser(user);
        return orders;
    }

    public Orders getOrderById(int orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<OrderDetails> getOrderDetailsByOrder(Orders order) {
        return orderDetailsRepository.findByOrder(order);
    }

    public boolean isUserAuthorized(HttpSession session, Orders order) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        return loggedInUser != null && order.getUser().getId() == loggedInUser.getId();
    }

    public void cancelOrder(int orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!"Preparation".equalsIgnoreCase(order.getStatus().getStatusName())) {
            throw new RuntimeException("Only Preparation orders can be canceled.");
        }

        order.setStatus(orderStatusRepository.findOrderStatusesByStatusId(4));
        orderRepository.save(order);
    }
}
