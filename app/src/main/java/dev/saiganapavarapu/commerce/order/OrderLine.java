package dev.saiganapavarapu.commerce.order;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_lines")
public class OrderLine {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private PurchaseOrder order;
    @Column(name = "product_id", nullable = false)
    private Long productId;
    @Column(nullable = false, length = 80)
    private String sku;
    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;
    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;
    @Column(nullable = false)
    private int quantity;
    @Column(name = "line_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal lineTotal;

    protected OrderLine() {}
    OrderLine(PurchaseOrder order, Long productId, String sku, String productName, BigDecimal unitPrice, int quantity) {
        this.order = order; this.productId = productId; this.sku = sku; this.productName = productName;
        this.unitPrice = unitPrice; this.quantity = quantity;
        this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    public Long getProductId() { return productId; }
    public String getSku() { return sku; }
    public String getProductName() { return productName; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
    public BigDecimal getLineTotal() { return lineTotal; }
}