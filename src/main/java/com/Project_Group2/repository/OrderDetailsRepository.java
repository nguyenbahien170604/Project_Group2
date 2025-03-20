package com.Project_Group2.repository;

import com.Project_Group2.entity.OrderDetails;
import com.Project_Group2.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    // Lấy danh sách chi tiết đơn hàng theo đơn hàng
    List<OrderDetails> findByOrder(Orders order);
}
