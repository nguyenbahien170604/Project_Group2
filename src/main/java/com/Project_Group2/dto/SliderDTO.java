package com.Project_Group2.dto;

public class SliderDTO {
    private int sliderId;
    private int productId;
    private String imageUrl;
    private String title;
    private String description;
    private int displayOrder;

    public SliderDTO() {
    }

    public SliderDTO(int productId, String imageUrl, String title, String description, int displayOrder) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.displayOrder = displayOrder;
    }

    public int getSliderId() {
        return sliderId;
    }

    public void setSliderId(int sliderId) {
        this.sliderId = sliderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
