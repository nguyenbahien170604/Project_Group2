package com.Project_Group2.service;

import com.Project_Group2.entity.Slider;
import com.Project_Group2.repository.SliderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SliderService {

    @Autowired
    private SliderRepository sliderRepository;

    public List<Slider> getTop3Slider(){
        return sliderRepository.findTop3ByOrderBySliderIdDesc();
    }


}
