package com.Project_Group2.service;

import com.Project_Group2.entity.*;
import com.Project_Group2.repository.OrderDetailsRepository;
import com.Project_Group2.repository.OrderRepository;
import com.Project_Group2.repository.OrderStatusRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<OrderDetails> listOrderDetails = orderDetailsRepository.findByOrder(order);
        for (OrderDetails orderDetails : listOrderDetails) {
            orderDetails.getProductVariant().setQuantityInStock(orderDetails.getProductVariant().getQuantityInStock() + orderDetails.getQuantity());
        }
        order.setStatus(orderStatusRepository.findOrderStatusesByStatusId(4));
        orderRepository.save(order);
    }


    public Page<Orders> getUserOrderHistory(String name, String phone, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return orderRepository.findAllOrderHistory(name, phone, pageable);
    }
    public Page<Orders> getAllBlogsWithPagination(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo , pageSize, Sort.by("createdAt").descending());
        return orderRepository.findAllActiveOrder(pageable);
    }
    public Page<Orders> getUserOrderHistory(User user, Pageable pageable) {
        return orderRepository.findByUserAndIsDeletedFalseOrderByCreatedAtDesc(user, pageable);
    }

    public boolean updateOrderStatus(int orderId, int statusId) {
        Orders order = orderRepository.findById(orderId).orElse(null);
        OrderStatuses status = orderStatusRepository.findById(statusId).orElse(null);

        if (order != null && status != null) {
            order.setStatus(status);
            orderRepository.save(order);
            return true;
        }
        return false;
    }

    public List<OrderStatuses> getAllOrderStatuses() {
        return orderStatusRepository.findAll();
    }

    public Map<Integer, List<OrderDetails>> getOrderDetailsForOrders(List<Orders> orders) {
        Map<Integer, List<OrderDetails>> orderDetailsMap = new HashMap<>();

        for (Orders order : orders) {
            List<OrderDetails> details = orderDetailsRepository.findByOrder(order);
            orderDetailsMap.put(order.getOrderId(), details);
        }

        return orderDetailsMap;
    }
}
