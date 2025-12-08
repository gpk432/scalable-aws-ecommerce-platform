package dev.saiganapavarapu.commerce.order;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
@Entity @Table(name="purchase_orders")
public class PurchaseOrder {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(name="public_id",nullable=false,unique=true) private UUID publicId;
  @Column(name="customer_email",nullable=false,length=320) private String customerEmail;
  @Enumerated(EnumType.STRING) @Column(nullable=false,length=40) private OrderStatus status;
  @Column(nullable=false,precision=12,scale=2) private BigDecimal total;
  @Column(name="created_at",nullable=false) private Instant createdAt;
  @OneToMany(mappedBy="order",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.EAGER)
  private List<OrderLine> items = new ArrayList<>();
  protected PurchaseOrder() {}
  PurchaseOrder(String customerEmail) { this.publicId=UUID.randomUUID(); this.customerEmail=customerEmail; this.status=OrderStatus.CONFIRMED; this.total=BigDecimal.ZERO; this.createdAt=Instant.now(); }
  void addItem(ProductSnapshot p, int quantity) { OrderLine line=new OrderLine(this,p.id(),p.sku(),p.name(),p.price(),quantity); items.add(line); total=total.add(line.getLineTotal()); }
  record ProductSnapshot(Long id,String sku,String name,BigDecimal price) {}
  public UUID getPublicId(){return publicId;} public String getCustomerEmail(){return customerEmail;} public OrderStatus getStatus(){return status;}
  public BigDecimal getTotal(){return total;} public Instant getCreatedAt(){return createdAt;} public List<OrderLine> getItems(){return List.copyOf(items);}
}
