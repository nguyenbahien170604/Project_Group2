package com.Project_Group2.dto;

public class SliderDTO {
    private String sliderTitle;
    private String sliderDescription;
    private String sliderImage;
    private String productName;

    public SliderDTO() {
    }

    public SliderDTO(String sliderTitle, String sliderDescription, String sliderImage, String productName) {
        this.sliderTitle = sliderTitle;
        this.sliderDescription = sliderDescription;
        this.sliderImage = sliderImage;
        this.productName = productName;
    }

    public String getSliderTitle() {
        return sliderTitle;
    }

    public void setSliderTitle(String sliderTitle) {
        this.sliderTitle = sliderTitle;
    }

    public String getSliderDescription() {
        return sliderDescription;
    }

    public void setSliderDescription(String sliderDescription) {
        this.sliderDescription = sliderDescription;
    }

    public String getSliderImage() {
        return sliderImage;
    }

    public void setSliderImage(String sliderImage) {
        this.sliderImage = sliderImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "SliderDTO{" +
                "sliderTitle='" + sliderTitle + '\'' +
                ", sliderDescription='" + sliderDescription + '\'' +
                ", sliderImage='" + sliderImage + '\'' +
                ", productName='" + productName + '\'' +
                '}';
    }
}
