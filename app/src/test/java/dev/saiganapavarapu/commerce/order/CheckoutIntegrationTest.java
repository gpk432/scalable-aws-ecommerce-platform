package dev.saiganapavarapu.commerce.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest
class CheckoutIntegrationTest {
    @Container @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");
    @Autowired OrderService orders;
    @Test void sameIdempotencyKeyReturnsSameOrder() {
        var request = new CreateOrderRequest("demo@example.com", List.of(new CreateOrderRequest.LineItem(1L, 1)));
        var first = orders.create("integration-key", request); var second = orders.create("integration-key", request);
        assertEquals(first.getPublicId(), second.getPublicId());
    }
}