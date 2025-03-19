package com.Project_Group2.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Payment_Methods")
public class PaymentMethods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "method_id")
    private int methodId;

    @Column(name = "method_name", unique = true, nullable = false, length = 100)
    private String methodName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean isDeleted;

    public PaymentMethods() {
    }

    public PaymentMethods(String methodName, Date createdAt, Boolean isDeleted) {
        this.methodName = methodName;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }

    public PaymentMethods(int methodId, String methodName, Date createdAt, Boolean isDeleted) {
        this.methodId = methodId;
        this.methodName = methodName;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }

    public int getMethodId() {
        return methodId;
    }

    public void setMethodId(int methodId) {
        this.methodId = methodId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
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
        return "PaymentMethods{" +
                "methodId=" + methodId +
                ", methodName='" + methodName + '\'' +
                ", createdAt=" + createdAt +
                ", isDeleted=" + isDeleted +
                '}';
    }
}

