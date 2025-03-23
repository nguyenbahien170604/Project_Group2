package com.Project_Group2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ClassifyDTO {
    @NotEmpty(message = "Color list cannot be empty")
    private List<@NotBlank(message = "Color cannot be blank") String> color;

    @NotEmpty(message = "Images list cannot be empty")
    private List<@NotNull(message = "Image cannot be null") MultipartFile> images;

    @NotEmpty(message = "Size list cannot be empty")
    private List<@NotBlank(message = "Size cannot be blank") String> size;

    @NotEmpty(message = "Quantity list cannot be empty")
    private List<@NotNull(message = "Quantity cannot be null") Integer> quantity;

    public List<String> getColor() {
        return color;
    }

    public void setColor(List<String> color) {
        this.color = color;
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

    public List<String> getSize() {
        return size;
    }

    public void setSize(List<String> size) {
        this.size = size;
    }

    public List<Integer> getQuantity() {
        return quantity;
    }

    public void setQuantity(List<Integer> quantity) {
        this.quantity = quantity;
    }
}
