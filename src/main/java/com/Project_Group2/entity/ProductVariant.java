package com.Project_Group2.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Product_Variants")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variant_id")
    private int variantId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "size", nullable = false, length = 10)
    private String size;

    @Column(name = "color", nullable = false, length = 50)
    private String color;

    @Column(name = "quantity_in_stock", nullable = false)
    private int quantityInStock = 0;

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

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean isDeleted;

    public ProductVariant() {
    }

    public ProductVariant(Product product, String size, String color, int quantityInStock, Date createdAt, Date updatedAt, Boolean isDeleted) {
        this.product = product;
        this.size = size;
        this.color = color;
        this.quantityInStock = quantityInStock;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    public ProductVariant(int variantId, Product product, String size, String color, int quantityInStock, Date createdAt, Date updatedAt, Boolean isDeleted) {
        this.variantId = variantId;
        this.product = product;
        this.size = size;
        this.color = color;
        this.quantityInStock = quantityInStock;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    // Getters và Setters

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "ProductVariant{id=" + variantId + ", size=" + size + ", color='" + color + "'}";
    }
}
