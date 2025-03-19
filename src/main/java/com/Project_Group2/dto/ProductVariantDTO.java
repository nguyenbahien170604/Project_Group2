package com.Project_Group2.dto;

public class ProductVariantDTO {
    private String size;
    private String color;
    private int quantityInStock;

    public ProductVariantDTO() {}

    public ProductVariantDTO(String size, String color, int quantityInStock) {
        this.size = size;
        this.color = color;
        this.quantityInStock = quantityInStock;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    @Override
    public String toString() {
        return "ProductVariantDTO{" +
                "size='" + size + '\'' +
                ", color='" + color + '\'' +
                ", quantityInStock=" + quantityInStock +
                '}';
    }
}
