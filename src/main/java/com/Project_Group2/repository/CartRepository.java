package com.Project_Group2.repository;

import com.Project_Group2.entity.Carts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CartRepository extends JpaRepository<Carts, Integer> {
    Optional<Carts> findByUserId(int userId);
}
