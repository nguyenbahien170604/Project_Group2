package com.Project_Group2.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private int brandId;

    @Column(name = "brand_name", nullable = false, unique = true, length = 100)
    private String brandName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean isDeleted;

    public Brand() {
    }

    public Brand(String brandName, Date createdAt, Boolean isDeleted) {
        this.brandName = brandName;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }

    public Brand(int brandId, String brandName, Boolean isDeleted, Date createdAt) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
    }

    // Getters và Setters

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "Brand{id=" + brandId + ", name='" + brandName + "'}";
    }
}

