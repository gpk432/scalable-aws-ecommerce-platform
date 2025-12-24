package dev.saiganapavarapu.commerce.order;

import dev.saiganapavarapu.commerce.payment.*;
import dev.saiganapavarapu.commerce.product.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class OrderService {
    private final ProductRepository products;
    private final PurchaseOrderRepository orders;
    private final PaymentGateway payments;
    public OrderService(ProductRepository products, PurchaseOrderRepository orders, PaymentGateway payments) {
        this.products = products; this.orders = orders; this.payments = payments;
    }

    @Transactional
    public PurchaseOrder create(String idempotencyKey, CreateOrderRequest request) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) throw new IllegalArgumentException("Idempotency-Key is required");
        if (idempotencyKey.length() > 200) throw new IllegalArgumentException("Idempotency-Key is too long");
        var existing = orders.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) return existing.get();

        PurchaseOrder order = new PurchaseOrder(idempotencyKey, request.customerEmail());
        for (var item : request.items()) {
            Product p = products.findByIdForUpdate(item.productId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown product: " + item.productId()));
            try { p.reserve(item.quantity()); }
            catch (IllegalStateException ex) { throw new InventoryUnavailableException(ex.getMessage()); }
            order.addItem(p.getId(), p.getSku(), p.getName(), p.getPrice(), item.quantity());
        }
        var result = payments.authorize(order.getCustomerEmail(), order.getTotal());
        if (!result.approved()) throw new PaymentDeclinedException(result.reason());
        order.markPaid(result.reference());
        try { return orders.saveAndFlush(order); }
        catch (DataIntegrityViolationException ex) {
            // A concurrent retry may have won the unique idempotency-key race.
            return orders.findByIdempotencyKey(idempotencyKey).orElseThrow(() -> ex);
        }
    }

    @Transactional(readOnly = true)
    public PurchaseOrder get(UUID id) {
        return orders.findByPublicId(id).orElseThrow(() -> new OrderNotFoundException(id));
    }
}