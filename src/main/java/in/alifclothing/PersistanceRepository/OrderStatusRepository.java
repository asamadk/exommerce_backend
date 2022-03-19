package in.alifclothing.PersistanceRepository;

import in.alifclothing.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderStatusRepository extends JpaRepository<OrderStatus,Integer> {

    @Query("select os from OrderStatus os where os.StatusName = :name")
    Optional<OrderStatus> findByStatusName(@Param("name") String statusName);
}
