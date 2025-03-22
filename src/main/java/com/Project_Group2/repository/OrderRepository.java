package com.Project_Group2.repository;

import com.Project_Group2.entity.Orders;
import com.Project_Group2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByUser(User user);
    Orders findTop1ByOrderByOrderIdDesc();
}
