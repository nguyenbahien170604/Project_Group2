package com.Project_Group2.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Payment_Statuses")
public class PaymentStatuses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private int statusId;

    @Column(name = "status_name", unique = true, nullable = false, length = 50)
    private String statusName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean isDeleted;

    public PaymentStatuses() {
    }

    public PaymentStatuses(String statusName, Date createdAt, Boolean isDeleted) {
        this.statusName = statusName;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }

    public PaymentStatuses(int statusId, String statusName, Date createdAt, Boolean isDeleted) {
        this.statusId = statusId;
        this.statusName = statusName;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
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
        return "PaymentStatuses{" +
                "statusId=" + statusId +
                ", statusName='" + statusName + '\'' +
                ", createdAt=" + createdAt +
                ", isDeleted=" + isDeleted +
                '}';
    }
}

