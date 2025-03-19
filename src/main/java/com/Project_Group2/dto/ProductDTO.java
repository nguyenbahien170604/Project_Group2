package com.Project_Group2.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProductDTO {
    private int id;
    private String productName;
    private String productShortDescription;
    private String productDescription;
    private BigDecimal productPrice;
    private int productDiscount;
    private List<String> productImage;
    private String productBrandName;
    private String productCategoryName;
    private List<ProductVariantDTO> productVariants;

    public ProductDTO() {
    }

    public ProductDTO(int id, String productName, String productShortDescription, String productDescription, BigDecimal productPrice, int productDiscount, List<String> productImage, String productBrandName, String productCategoryName, List<ProductVariantDTO> productVariants) {
        this.id = id;
        this.productName = productName;
        this.productShortDescription = productShortDescription;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productDiscount = productDiscount;
        this.productImage = productImage;
        this.productBrandName = productBrandName;
        this.productCategoryName = productCategoryName;
        this.productVariants = productVariants;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductShortDescription() {
        return productShortDescription;
    }

    public void setProductShortDescription(String productShortDescription) {
        this.productShortDescription = productShortDescription;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(int productDiscount) {
        this.productDiscount = productDiscount;
    }

    public List<String> getProductImage() {
        return productImage;
    }

    public void setProductImage(List<String> productImage) {
        this.productImage = productImage;
    }

    public String getProductBrandName() {
        return productBrandName;
    }

    public void setProductBrandName(String productBrandName) {
        this.productBrandName = productBrandName;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public List<ProductVariantDTO> getProductVariants() {
        return productVariants;
    }

    public void setProductVariants(List<ProductVariantDTO> productVariants) {
        this.productVariants = productVariants;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "productName='" + productName + '\'' +
                ", productShortDescription='" + productShortDescription + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productPrice=" + productPrice +
                ", productDiscount=" + productDiscount +
                ", productImage=" + productImage +
                ", productBrandName='" + productBrandName + '\'' +
                ", productCategoryName='" + productCategoryName + '\'' +
                ", productVariants=" + productVariants +
                '}';
    }
}
