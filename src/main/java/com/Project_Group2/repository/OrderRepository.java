package com.Project_Group2.repository;

import com.Project_Group2.entity.Blog;
import com.Project_Group2.entity.Orders;
import com.Project_Group2.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query("SELECT b FROM Orders b WHERE b.isDeleted = false")
    Page<Orders> findAllActiveOrder(Pageable pageable);

    @Query("SELECT b FROM Orders b WHERE b.isDeleted = false " +
            "AND (COALESCE(:phone, '') = '' OR b.receivedPhone LIKE CONCAT('%', :phone, '%')) " +
            "OR (COALESCE(:name, '') = '' OR b.receivedName LIKE CONCAT('%', :name, '%')) ")
    Page<Orders> findAllOrderHistory(
            @Param("title") String name,
            @Param("content") String phone,
            Pageable pageable);
}
