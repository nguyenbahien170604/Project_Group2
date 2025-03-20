package com.Project_Group2.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Order_Details")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private int orderDetailId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price_product", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceProduct;

    public OrderDetails() {
    }

    public OrderDetails(Orders order, int quantity, BigDecimal priceProduct, Product product) {
        this.order = order;
        this.quantity = quantity;
        this.priceProduct = priceProduct;
        this.product = product;
    }

    public OrderDetails(int orderDetailId, Orders order, Product product, int quantity, BigDecimal priceProduct) {
        this.orderDetailId = orderDetailId;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.priceProduct = priceProduct;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(BigDecimal priceProduct) {
        this.priceProduct = priceProduct;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "orderDetailId=" + orderDetailId +
                '}';
    }
}

