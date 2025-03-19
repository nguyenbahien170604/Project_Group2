package com.Project_Group2.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="productId", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Comment() {}

    public Comment(Product product, User user, LocalDateTime createdAt, String content, LocalDateTime updatedAt) {
        this.product = product;
        this.user = user;
        this.createdAt = createdAt;
        this.content = content;
        this.updatedAt = updatedAt;
    }

    public Comment(int id, Product product, User user, LocalDateTime createdAt, String content, LocalDateTime updatedAt) {
        this.id = id;
        this.product = product;
        this.user = user;
        this.createdAt = createdAt;
        this.content = content;
        this.updatedAt = updatedAt;
    }



    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", product=" + product +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

