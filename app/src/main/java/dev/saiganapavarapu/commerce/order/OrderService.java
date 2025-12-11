package dev.saiganapavarapu.commerce.order;
import dev.saiganapavarapu.commerce.product.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
@Service
public class OrderService {
  private final ProductRepository products; private final PurchaseOrderRepository orders;
  public OrderService(ProductRepository products, PurchaseOrderRepository orders){this.products=products;this.orders=orders;}
  @Transactional public PurchaseOrder create(CreateOrderRequest request){
    PurchaseOrder order=new PurchaseOrder(request.customerEmail());
    for(var item:request.items()){
      Product p=products.findById(item.productId()).orElseThrow(() -> new IllegalArgumentException("Unknown product: "+item.productId()));
      order.addItem(new PurchaseOrder.ProductSnapshot(p.getId(),p.getSku(),p.getName(),p.getPrice()),item.quantity());
    }
    return orders.save(order);
  }
  @Transactional(readOnly=true) public PurchaseOrder get(UUID id){return orders.findByPublicId(id).orElseThrow();}
}
