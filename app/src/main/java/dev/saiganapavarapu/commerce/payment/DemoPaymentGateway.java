package dev.saiganapavarapu.commerce.payment;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.UUID;
@Component
public class DemoPaymentGateway implements PaymentGateway {
    @Override public PaymentResult authorize(String customerEmail, BigDecimal amount) {
        if (customerEmail.toLowerCase().contains("decline")) return PaymentResult.declined("Demo payment declined");
        return PaymentResult.approved("pay_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16));
    }
}