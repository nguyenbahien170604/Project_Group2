package com.Project_Group2.repository;

import com.Project_Group2.entity.Slider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SliderRepository extends JpaRepository<Slider, Integer> {
    List<Slider> findTop3ByOrderBySliderIdDesc();

    @Query("SELECT s FROM Slider s WHERE s.isDeleted = false")
    Page<Slider> findAllActiveSlider(Pageable pageable);

    @Query("SELECT s FROM Slider s WHERE s.isDeleted = false " +
            "AND (COALESCE(:title, '') = '' OR s.title LIKE CONCAT('%', :title, '%')) " +
            "AND (COALESCE(:product, '') = '' OR s.product.productName LIKE CONCAT('%', :product, '%')) ")
    Page<Slider> findByIsDeletedFalse(
            @Param("title") String title,
            @Param("product") String product,
            Pageable pageable);
}
