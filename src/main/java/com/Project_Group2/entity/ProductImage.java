package com.Project_Group2.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Product_Images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private int imageId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    public ProductImage() {
    }

    public ProductImage(Product product, String imageUrl, Date updatedAt, boolean isDeleted, Date createdAt) {
        this.product = product;
        this.imageUrl = imageUrl;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
    }

    public ProductImage(int imageId, String imageUrl, Date createdAt, boolean isDeleted, Date updatedAt, Product product) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
        this.updatedAt = updatedAt;
        this.product = product;
    }

    // Getters và Setters

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "ProductImage{" +
                "imageId=" + imageId +
                ", product=" + product +
                ", imageUrl='" + imageUrl + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isDeleted=" + isDeleted +
                '}';
    }
}

