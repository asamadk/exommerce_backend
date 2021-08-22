package in.alifclothing.PersistanceRepository;

import in.alifclothing.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus,Integer> {
}
