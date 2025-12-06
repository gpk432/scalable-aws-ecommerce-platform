package dev.saiganapavarapu.commerce.product;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository products;
    public ProductController(ProductRepository products) { this.products = products; }
    @GetMapping public List<Product> list() { return products.findAll(); }
}
