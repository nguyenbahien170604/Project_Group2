package com.Project_Group2.service;

import com.Project_Group2.dto.SliderDTO;
import com.Project_Group2.entity.Blog;
import com.Project_Group2.entity.Product;
import com.Project_Group2.entity.Slider;
import com.Project_Group2.repository.ProductRepository;
import com.Project_Group2.repository.SliderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SliderService {

    @Autowired
    private SliderRepository sliderRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<Slider> getTop3Slider(){
        return sliderRepository.findTop3ByOrderBySliderIdDesc();
    }

    public Page<Slider> getSlidersWithPagination(String title, String productName,int page, int size) {
        return sliderRepository.findByIsDeletedFalse(title, productName, PageRequest.of(page-1, size));
    }

    public Page<Slider> getAllBlogsWithPagination(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo , pageSize, Sort.by("createdAt").descending());
        return sliderRepository.findAllActiveSlider(pageable);
    }
    public Optional<Slider> getSliderById(int id) {
        return sliderRepository.findById(id);
    }

    private static final String UPLOAD_DIR = "src/main/resources/static/assets/img/slider/";

    public void saveSlider(SliderDTO sliderDTO, MultipartFile imageFile) {
        try {
            String imageUrl = null;

            // Xử lý lưu ảnh nếu có ảnh mới
            if (imageFile != null && !imageFile.isEmpty()) {
                String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR, filename);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, imageFile.getBytes());
                imageUrl = "/assets/img/slider/" + filename;
            }

            Product product = productRepository.findById(sliderDTO.getProductId()).orElseThrow();


            Slider slider = new Slider();
            slider.setProduct(product);
            slider.setImageUrl(imageUrl != null ? imageUrl : sliderDTO.getImageUrl()); // Giữ ảnh cũ nếu không có ảnh mới
            slider.setTitle(sliderDTO.getTitle());
            slider.setDescription(sliderDTO.getDescription());
            slider.setDisplayOrder(sliderDTO.getDisplayOrder());
            slider.setDeleted(false);

            sliderRepository.save(slider);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi lưu ảnh slider: " + e.getMessage());
        }
    }
    public void deleteSlider(int id) {
        Slider slider = sliderRepository.findById(id).orElseThrow();
        slider.setDeleted(true);
        sliderRepository.save(slider);
    }
    public void updateSlider(SliderDTO sliderDTO, MultipartFile imageFile) {
        try {
            Slider slider = sliderRepository.findById(sliderDTO.getSliderId())
                    .orElseThrow(() -> new RuntimeException("Slider không tồn tại"));

            // Xử lý lưu ảnh mới nếu có
            if (imageFile != null && !imageFile.isEmpty()) {
                String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR, filename);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, imageFile.getBytes());
                slider.setImageUrl("/assets/img/slider/" + filename);
                System.out.println(slider.getImageUrl());
            } else {
                // Giữ nguyên ảnh cũ nếu không có ảnh mới
                slider.setImageUrl(sliderDTO.getImageUrl());
            }

            Product product = productRepository.findById(sliderDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            slider.setProduct(product);
            slider.setTitle(sliderDTO.getTitle());
            slider.setDescription(sliderDTO.getDescription());
            slider.setDisplayOrder(sliderDTO.getDisplayOrder());

            sliderRepository.save(slider);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi cập nhật slider: " + e.getMessage());
        }
    }

    public void updateSliderProduct(int sliderId, int productId) {
        try {
            Slider slider = sliderRepository.findById(sliderId)
                    .orElseThrow(() -> new RuntimeException("Slider không tồn tại"));

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            slider.setProduct(product);
            sliderRepository.save(slider);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi cập nhật sản phẩm cho slider: " + e.getMessage());
        }
    }

}
