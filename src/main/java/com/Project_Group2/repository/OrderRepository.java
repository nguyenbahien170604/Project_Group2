package com.Project_Group2.repository;

import com.Project_Group2.entity.Orders;
import com.Project_Group2.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByUser(User user);
    Orders findTop1ByOrderByOrderIdDesc();
    List<Orders> findByUser_id(Integer userId);
    List<Orders> findByUser_idAndStatus_StatusId(Integer userId, Integer statusId);
    List<Orders> findByUser_idAndStatus_StatusIdAndIsPaid(Integer userId, Integer statusId, Boolean isPaid);
    List<Orders> findByUser_idAndIsPaid(Integer userId, Boolean isPaid);
    Page<Orders> findByUserAndIsDeletedFalseOrderByCreatedAtDesc(User user, Pageable pageable);

    @Query("SELECT o FROM Orders o WHERE o.isDeleted = false ORDER BY o.createdAt DESC")
    Page<Orders> findAllOrderHistory(Pageable pageable);
}
