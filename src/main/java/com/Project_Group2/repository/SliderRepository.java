package com.Project_Group2.repository;

import com.Project_Group2.entity.Slider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SliderRepository extends JpaRepository<Slider, Integer> {
    List<Slider> findTop3ByOrderBySliderIdDesc();
    Page<Slider> findByIsDeletedFalse(Pageable pageable);
}
