package com.Project_Group2.service;

import com.Project_Group2.entity.Brand;
import com.Project_Group2.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    public Brand findById(int id) {
        return brandRepository.findByBrandId(id);
    }
}
