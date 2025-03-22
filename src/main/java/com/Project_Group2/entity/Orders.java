package com.Project_Group2.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "Orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "received_name", nullable = false, length = 100)
    private String receivedName;

    @Column(name = "received_phone", nullable = false, length = 20)
    private String receivedPhone;

    @Column(name = "received_address", nullable = false, columnDefinition = "NVARCHAR(4000)")
    private String receivedAddress;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private OrderStatuses status;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
    @Column(name = "is_paid" ,columnDefinition = "BIT DEFAULT 0")
    private Boolean isPaid;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean isDeleted;

    public Orders() {
    }

    public Orders(User user, String receivedName, String receivedPhone, String receivedAddress, OrderStatuses status, BigDecimal totalPrice, Date createdAt, Boolean isPaid, Boolean isDeleted) {
        this.user = user;
        this.receivedName = receivedName;
        this.receivedPhone = receivedPhone;
        this.receivedAddress = receivedAddress;
        this.status = status;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.isPaid = isPaid;
        this.isDeleted = isDeleted;
    }

    public Orders(int orderId, User user, String receivedName, String receivedPhone, String receivedAddress, OrderStatuses status, BigDecimal totalPrice, Date createdAt, Boolean isPaid, Boolean isDeleted) {
        this.orderId = orderId;
        this.user = user;
        this.receivedName = receivedName;
        this.receivedPhone = receivedPhone;
        this.receivedAddress = receivedAddress;
        this.status = status;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.isPaid = isPaid;
        this.isDeleted = isDeleted;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReceivedName() {
        return receivedName;
    }

    public void setReceivedName(String receivedName) {
        this.receivedName = receivedName;
    }

    public String getReceivedPhone() {
        return receivedPhone;
    }

    public void setReceivedPhone(String receivedPhone) {
        this.receivedPhone = receivedPhone;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getReceivedAddress() {
        return receivedAddress;
    }

    public void setReceivedAddress(String receivedAddress) {
        this.receivedAddress = receivedAddress;
    }

    public OrderStatuses getStatus() {
        return status;
    }

    public void setStatus(OrderStatuses status) {
        this.status = status;
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

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "orderId=" + orderId +
                '}';
    }
}
