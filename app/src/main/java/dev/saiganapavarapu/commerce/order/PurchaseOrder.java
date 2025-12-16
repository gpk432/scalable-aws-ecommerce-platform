package dev.saiganapavarapu.commerce.order;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.Instant; import java.util.*;
@Entity @Table(name="purchase_orders")
public class PurchaseOrder {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(name="public_id",nullable=false,unique=true) private UUID publicId;
  @Column(name="idempotency_key",nullable=false,unique=true,length=200) private String idempotencyKey;
  @Column(name="customer_email",nullable=false,length=320) private String customerEmail;
  @Enumerated(EnumType.STRING) @Column(nullable=false,length=40) private OrderStatus status;
  @Column(nullable=false,precision=12,scale=2) private BigDecimal total;
  @Column(name="created_at",nullable=false) private Instant createdAt;
  @OneToMany(mappedBy="order",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.EAGER) private List<OrderLine> items=new ArrayList<>();
  protected PurchaseOrder(){}
  PurchaseOrder(String idempotencyKey,String customerEmail){this.publicId=UUID.randomUUID();this.idempotencyKey=idempotencyKey;this.customerEmail=customerEmail;this.status=OrderStatus.CONFIRMED;this.total=BigDecimal.ZERO;this.createdAt=Instant.now();}
  void addItem(Long productId,String sku,String name,BigDecimal price,int quantity){OrderLine line=new OrderLine(this,productId,sku,name,price,quantity);items.add(line);total=total.add(line.getLineTotal());}
  public UUID getPublicId(){return publicId;} public String getCustomerEmail(){return customerEmail;} public OrderStatus getStatus(){return status;}
  public BigDecimal getTotal(){return total;} public Instant getCreatedAt(){return createdAt;} public List<OrderLine> getItems(){return List.copyOf(items);}
}
