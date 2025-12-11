package dev.saiganapavarapu.commerce.order;
import java.util.List;
public record CreateOrderRequest(String customerEmail, List<LineItem> items) {
  public record LineItem(Long productId, int quantity) {}
}
