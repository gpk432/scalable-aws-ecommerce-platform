package dev.saiganapavarapu.commerce.common;

import dev.saiganapavarapu.commerce.order.*;
import dev.saiganapavarapu.commerce.payment.PaymentDeclinedException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    ProblemDetail badRequest(Exception ex) { return problem(HttpStatus.BAD_REQUEST, "Invalid request", ex.getMessage()); }
    @ExceptionHandler({InventoryUnavailableException.class, PaymentDeclinedException.class})
    ProblemDetail conflict(RuntimeException ex) { return problem(HttpStatus.CONFLICT, "Checkout conflict", ex.getMessage()); }
    @ExceptionHandler(OrderNotFoundException.class)
    ProblemDetail notFound(OrderNotFoundException ex) { return problem(HttpStatus.NOT_FOUND, "Not found", ex.getMessage()); }
    private ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(status, detail); p.setTitle(title); p.setType(URI.create("about:blank")); return p;
    }
}