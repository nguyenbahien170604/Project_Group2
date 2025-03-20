package com.Project_Group2.repository;

import com.Project_Group2.entity.OrderStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatuses, Integer> {
}
