package dev.saiganapavarapu.commerce.order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Long> {
  Optional<PurchaseOrder> findByPublicId(UUID publicId);
}
