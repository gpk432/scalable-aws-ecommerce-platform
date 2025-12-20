package dev.saiganapavarapu.commerce.payment;
import java.math.BigDecimal;
public interface PaymentGateway {
    PaymentResult authorize(String customerEmail, BigDecimal amount);
    record PaymentResult(boolean approved, String reference, String reason) {
        public static PaymentResult approved(String reference) { return new PaymentResult(true, reference, null); }
        public static PaymentResult declined(String reason) { return new PaymentResult(false, null, reason); }
    }
}