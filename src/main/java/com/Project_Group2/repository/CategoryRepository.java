package com.Project_Group2.repository;

import com.Project_Group2.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
