package in.alifclothing.PersistanceRepository;

import in.alifclothing.model.OrderModel;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderModel,Integer> {
    @Query("select o from  OrderModel o where o.userModel.user_id= :id")
    List<OrderModel> findByUserId(@Param("id") Integer id);
}
