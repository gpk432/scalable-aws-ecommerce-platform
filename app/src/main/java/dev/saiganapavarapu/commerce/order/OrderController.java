package dev.saiganapavarapu.commerce.order;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
@RestController @RequestMapping("/api/orders")
public class OrderController {
  private final OrderService service; public OrderController(OrderService service){this.service=service;}
  @PostMapping @ResponseStatus(HttpStatus.CREATED) public PurchaseOrder create(@Valid @RequestBody CreateOrderRequest request){return service.create(request);}
  @GetMapping("/{id}") public PurchaseOrder get(@PathVariable UUID id){return service.get(id);}
}
