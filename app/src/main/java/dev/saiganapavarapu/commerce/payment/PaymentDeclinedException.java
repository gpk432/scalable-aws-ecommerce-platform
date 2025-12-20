package dev.saiganapavarapu.commerce.payment;
public class PaymentDeclinedException extends RuntimeException {
    public PaymentDeclinedException(String message) { super(message); }
}