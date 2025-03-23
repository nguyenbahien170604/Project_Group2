package com.Project_Group2.repository;

import com.Project_Group2.entity.Category;
import com.Project_Group2.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
