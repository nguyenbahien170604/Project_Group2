package com.Project_Group2.controller.managerController;

import com.Project_Group2.dto.ProductDTO;
import com.Project_Group2.dto.SliderDTO;
import com.Project_Group2.entity.Slider;
import com.Project_Group2.service.ProductService;
import com.Project_Group2.service.SliderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/sliders")
public class SliderController {

    @Autowired
    private SliderService sliderService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listSliders(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "2") int size,
                              Model model) {
        Page<Slider> sliderPage = sliderService.getSlidersWithPagination(page, size);
        model.addAttribute("sliderPage", sliderPage);
        model.addAttribute("currentPage", page);
        return "manager/slider";
    }

//    @GetMapping("/create")
//    public String createSliderForm(Model model) {
//        model.addAttribute("slider", new SliderDTO());
//        return "manager/sliderCreate";
//    }

    @PostMapping("/save")
    public String saveSlider(@ModelAttribute("slider") SliderDTO sliderDTO,
                             @RequestParam("imageFile") MultipartFile imageFile) {
        System.out.println(sliderDTO.getProductId());
        sliderService.saveSlider(sliderDTO, imageFile);
        return "redirect:/sliders";
    }

    @GetMapping("/delete/{id}")
    public String deleteSlider(@PathVariable int id) {
        sliderService.deleteSlider(id);
        return "redirect:/sliders";
    }

    @GetMapping("/create")
    public String createSliderForm(Model model,
                                   @RequestParam(required = false) String search,
                                   @RequestParam(required = false) Integer productId,
                                   @RequestParam(required = false) String productName) {
        model.addAttribute("slider", new SliderDTO());
        if (productId != null) {
            model.addAttribute("selectedProductId", productId);
            model.addAttribute("selectedProductName", productName);
        }
        // Nếu có tham số tìm kiếm, tìm các sản phẩm phù hợp
        if (search != null && !search.trim().isEmpty()) {
            List<ProductDTO> products = productService.searchProducts(search);
            model.addAttribute("products", products);
        }

        return "manager/sliderCreate";
    }

    @GetMapping("/edit/{id}")
    public String editSliderForm(@PathVariable int id, Model model) {
        Slider slider = sliderService.getSliderById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy slider với ID: " + id));

        SliderDTO sliderDTO = new SliderDTO();
        sliderDTO.setSliderId(slider.getSliderId());
        sliderDTO.setProductId(slider.getProduct().getProductId());
        sliderDTO.setTitle(slider.getTitle());
        sliderDTO.setDescription(slider.getDescription());
        sliderDTO.setImageUrl(slider.getImageUrl());
        sliderDTO.setDisplayOrder(slider.getDisplayOrder());

        model.addAttribute("slider", sliderDTO);
        model.addAttribute("productName", slider.getProduct().getProductName());

        return "manager/sliderEdit";
    }

    @GetMapping("/edit/{id}/changeProduct")
    public String changeProductForm(@PathVariable int id,
                                    @RequestParam(required = false) String search,
                                    Model model) {
        model.addAttribute("sliderId", id);
        List<ProductDTO> products = (search != null && !search.trim().isEmpty())
                ? productService.searchProducts(search)
                : productService.getAllProducts();
        model.addAttribute("products", products);

        return "manager/sliderSearch";
    }



    @GetMapping("/edit/{id}/selectProduct")
    public String selectProduct(@PathVariable int id, @RequestParam int productId) {
        sliderService.updateSliderProduct(id, productId);
        return "redirect:/sliders/edit/" + id;
    }

    @PostMapping("/update")
    public String updateSlider(@ModelAttribute SliderDTO sliderDTO,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        sliderService.updateSlider(sliderDTO, imageFile);
        return "redirect:/sliders";
    }
}