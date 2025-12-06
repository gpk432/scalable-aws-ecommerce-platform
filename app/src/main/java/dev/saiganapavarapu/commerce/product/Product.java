package dev.saiganapavarapu.commerce.product;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 80)
    private String sku;
    @Column(nullable = false, length = 200)
    private String name;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
    @Column(nullable = false)
    private int stock;

    protected Product() {}
    public Product(String sku, String name, BigDecimal price, int stock) {
        this.sku = sku; this.name = name; this.price = price; this.stock = stock;
    }
    public void reserve(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be positive");
        if (stock < quantity) throw new IllegalStateException("Insufficient stock for " + sku);
        stock -= quantity;
    }
    public Long getId() { return id; }
    public String getSku() { return sku; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
}