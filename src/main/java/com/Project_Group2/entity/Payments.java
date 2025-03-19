package com.Project_Group2.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "Payments")
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private int paymentId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "payment_date", nullable = false)
    private Date paymentDate;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private PaymentStatuses status;

    @ManyToOne
    @JoinColumn(name = "method_id", nullable = false)
    private PaymentMethods method;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean isDeleted;

    public Payments() {
    }

    public Payments(Orders order, Date paymentDate, BigDecimal amount, PaymentStatuses status, PaymentMethods method, Date createdAt, Boolean isDeleted) {
        this.order = order;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.status = status;
        this.method = method;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }

    public Payments(int paymentId, Orders order, Date paymentDate, PaymentStatuses status, BigDecimal amount, PaymentMethods method, Date createdAt, Boolean isDeleted) {
        this.paymentId = paymentId;
        this.order = order;
        this.paymentDate = paymentDate;
        this.status = status;
        this.amount = amount;
        this.method = method;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public PaymentMethods getMethod() {
        return method;
    }

    public void setMethod(PaymentMethods method) {
        this.method = method;
    }

    public PaymentStatuses getStatus() {
        return status;
    }

    public void setStatus(PaymentStatuses status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "Payments{" +
                "paymentId=" + paymentId +
                ", order=" + order +
                ", paymentDate=" + paymentDate +
                ", amount=" + amount +
                ", status=" + status +
                ", method=" + method +
                ", createdAt=" + createdAt +
                ", isDeleted=" + isDeleted +
                '}';
    }
}

