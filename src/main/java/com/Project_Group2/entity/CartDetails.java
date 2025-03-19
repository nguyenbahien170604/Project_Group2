package com.Project_Group2.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Cart_Details")
public class CartDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_detail_id")
    private int cartDetailId;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Carts cart;

    @ManyToOne
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @Column(name = "quantity", nullable = false)
    private int quantity = 1;

    @Column(name = "price_product", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceProduct;

    public CartDetails() {
    }

    public CartDetails(Carts cart, ProductVariant productVariant, int quantity, BigDecimal priceProduct) {
        this.cart = cart;
        this.productVariant = productVariant;
        this.quantity = quantity;
        this.priceProduct = priceProduct;
    }

    public CartDetails(int cartDetailId, Carts cart, ProductVariant productVariant, int quantity, BigDecimal priceProduct) {
        this.cartDetailId = cartDetailId;
        this.cart = cart;
        this.productVariant = productVariant;
        this.quantity = quantity;
        this.priceProduct = priceProduct;
    }

    public int getCartDetailId() {
        return cartDetailId;
    }

    public void setCartDetailId(int cartDetailId) {
        this.cartDetailId = cartDetailId;
    }

    public Carts getCart() {
        return cart;
    }

    public void setCart(Carts cart) {
        this.cart = cart;
    }

    public BigDecimal getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(BigDecimal priceProduct) {
        this.priceProduct = priceProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    @Override
    public String toString() {
        return "CartDetails{" +
                "cartDetailId=" + cartDetailId +
                ", cart=" + cart +
                ", productVariant=" + productVariant +
                ", quantity=" + quantity +
                ", priceProduct=" + priceProduct +
                '}';
    }
}

