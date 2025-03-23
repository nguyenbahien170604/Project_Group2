package com.Project_Group2.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public class AddProductDTO {

    @NotBlank(message = "Product name cannot be blank")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "Short description cannot be blank")
    private String shortDescription;

    @NotNull(message = "Discount cannot be null")
    @Min(value = 0, message = "Discount cannot be negative")
    private Integer discount;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Brand ID cannot be null")
    private Integer brandId;

    @NotNull(message = "Category ID cannot be null")
    private Integer categoryId;

    @NotEmpty(message = "Classifies list cannot be empty")
    @Valid
    private List<ClassifyDTO> classifies;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public List<ClassifyDTO> getClassifies() {
        return classifies;
    }

    public void setClassifies(List<ClassifyDTO> classifies) {
        this.classifies = classifies;
    }
}
