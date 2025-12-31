package dev.saiganapavarapu.commerce.order;

import dev.saiganapavarapu.commerce.payment.*;
import dev.saiganapavarapu.commerce.product.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {
    @Test void reservesInventoryAndCapturesSnapshot() {
        ProductRepository products = mock(ProductRepository.class); PurchaseOrderRepository orders = mock(PurchaseOrderRepository.class);
        PaymentGateway payments = (email, amount) -> PaymentGateway.PaymentResult.approved("pay_test");
        Product product = new Product("KB-001", "Keyboard", new BigDecimal("10.00"), 5);
        when(products.findByIdForUpdate(1L)).thenReturn(Optional.of(product)); when(orders.findByIdempotencyKey("k1")).thenReturn(Optional.empty());
        when(orders.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));
        PurchaseOrder created = new OrderService(products, orders, payments).create("k1", new CreateOrderRequest("a@b.com", List.of(new CreateOrderRequest.LineItem(1L, 2))));
        assertEquals(new BigDecimal("20.00"), created.getTotal()); assertEquals(3, product.getStock()); assertEquals("KB-001", created.getItems().getFirst().getSku());
    }
    @Test void paymentFailureLeavesExceptionForTransactionRollback() {
        ProductRepository products = mock(ProductRepository.class); PurchaseOrderRepository orders = mock(PurchaseOrderRepository.class);
        when(orders.findByIdempotencyKey("k2")).thenReturn(Optional.empty()); when(products.findByIdForUpdate(1L)).thenReturn(Optional.of(new Product("KB", "Keyboard", BigDecimal.TEN, 5)));
        OrderService service = new OrderService(products, orders, (e,a) -> PaymentGateway.PaymentResult.declined("declined"));
        assertThrows(PaymentDeclinedException.class, () -> service.create("k2", new CreateOrderRequest("a@b.com", List.of(new CreateOrderRequest.LineItem(1L, 1)))));
        verify(orders, never()).saveAndFlush(any());
    }
}