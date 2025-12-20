package dev.saiganapavarapu.commerce.order;
import dev.saiganapavarapu.commerce.payment.*; import dev.saiganapavarapu.commerce.product.*; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.UUID;
@Service public class OrderService {
  private final ProductRepository products; private final PurchaseOrderRepository orders; private final PaymentGateway payments;
  public OrderService(ProductRepository products,PurchaseOrderRepository orders,PaymentGateway payments){this.products=products;this.orders=orders;this.payments=payments;}
  @Transactional public PurchaseOrder create(String key,CreateOrderRequest request){
    if(key==null||key.isBlank()) throw new IllegalArgumentException("Idempotency-Key is required"); var existing=orders.findByIdempotencyKey(key); if(existing.isPresent()) return existing.get();
    PurchaseOrder order=new PurchaseOrder(key,request.customerEmail());
    for(var item:request.items()){Product p=products.findByIdForUpdate(item.productId()).orElseThrow(() -> new IllegalArgumentException("Unknown product: "+item.productId()));p.reserve(item.quantity());order.addItem(p.getId(),p.getSku(),p.getName(),p.getPrice(),item.quantity());}
    var result=payments.authorize(order.getCustomerEmail(),order.getTotal()); if(!result.approved()) throw new PaymentDeclinedException(result.reason()); order.markPaid(result.reference()); return orders.save(order);
  }
  @Transactional(readOnly=true) public PurchaseOrder get(UUID id){return orders.findByPublicId(id).orElseThrow();}
}
