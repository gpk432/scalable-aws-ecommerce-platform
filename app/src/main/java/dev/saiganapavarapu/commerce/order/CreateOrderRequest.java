package dev.saiganapavarapu.commerce.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record CreateOrderRequest(
    @NotBlank @Email String customerEmail,
    @NotEmpty List<@Valid LineItem> items
) {
    public record LineItem(@NotNull Long productId, @Min(1) int quantity) {}
}
